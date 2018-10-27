package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.DifferentialWheels;
import ca.warp7.frc.commons.DtMeasurement;
import ca.warp7.frc.commons.IUnit;
import ca.warp7.frc.commons.PIDValues;
import ca.warp7.frc.commons.cheesy_drive.CheesyDrive;
import ca.warp7.frc.commons.cheesy_drive.ICheesyDriveInput;
import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.StateType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.stormbots.MiniPID;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;

import java.util.LinkedList;

import static ca.warp7.frc.commons.Functions.constrainMinimum;
import static ca.warp7.frc.commons.Functions.limit;
import static ca.warp7.frc2018_3.Components.navX;
import static ca.warp7.frc2018_3.Constants.*;

public class Drive implements ISubsystem {

    private static final double kAbsoluteMaxOutput = 1.0;
    private static final double kMaximumPIDPower = 0.7;
    private static final double kRampIntervalMultiplier = 0.5;
    private static final double kMaxLinearRampRate = 1.0 / 5;
    private static final double kRampRateEpsilon = 1.0E-04;
    private static final double kOutputPowerEpsilon = 1.0E-3;
    private static final double kTimeDeltaEpsilon = 1.0E-10;

    private static final int kVelocityAverageQueueSize = 5;

    @InputField
    private final Input mInput = new Input();
    @StateField
    private final State mState = new State();

    private CheesyDrive mCheesyDrive;
    private double mLastMeasuredTimeStamp;
    private WPI_VictorSPX mLeftMaster;
    private WPI_VictorSPX mLeftSlave;
    private WPI_VictorSPX mRightMaster;
    private WPI_VictorSPX mRightSlave;
    private SpeedControllerGroup mLeftGroup;
    private SpeedControllerGroup mRightGroup;
    private DifferentialWheels<Encoder> mEncoders;
    private AHRS mAHRS;
    private final boolean mUseVictorConfig = false;

    private static Encoder configEncoder(int channelA, int channelB, boolean reversed) {
        Encoder encoder = new Encoder(channelA, channelB, reversed, CounterBase.EncodingType.k4X);
        encoder.setDistancePerPulse(kInchesPerTick);
        encoder.reset();
        return encoder;
    }

    private static void configMaster(WPI_VictorSPX victor) {
        victor.enableVoltageCompensation(true);
        victor.configVoltageCompSaturation(12.0, kConfigTimeout);
        victor.configNeutralDeadband(0.04, kConfigTimeout);
        victor.configOpenloopRamp(0.0, kConfigTimeout);
        victor.configClosedloopRamp(0.0, kConfigTimeout);
        victor.configPeakOutputForward(kAbsoluteMaxOutput, kConfigTimeout);
        victor.configPeakOutputReverse(kAbsoluteMaxOutput, kConfigTimeout);
        victor.configNominalOutputForward(kOutputPowerEpsilon, kConfigTimeout);
        victor.configNominalOutputReverse(kOutputPowerEpsilon, kConfigTimeout);
    }

    @SuppressWarnings("unused")
    private void configAll() {
        configMaster(mLeftMaster);
        configMaster(mRightMaster);
        mLeftSlave.set(ControlMode.Follower, mLeftMaster.getDeviceID());
        mRightSlave.set(ControlMode.Follower, mRightMaster.getDeviceID());
    }

    @Override
    public void onConstruct() {
        mCheesyDrive = new CheesyDrive((left, right) -> {
            mInput.demandedLeftPower = left;
            mInput.demandedRightPower = right;
        });

        mLeftMaster = new WPI_VictorSPX(kDriveLeftMaster);
        mLeftSlave = new WPI_VictorSPX(kDriveLeftSlave);
        mRightMaster = new WPI_VictorSPX(kDriveRightMaster);
        mRightSlave = new WPI_VictorSPX(kDriveRightSlave);

        if (mUseVictorConfig) {
            configAll();
        } else {
            mLeftGroup = new SpeedControllerGroup(mLeftMaster, mLeftSlave);
            mRightGroup = new SpeedControllerGroup(mRightMaster, mRightSlave);
            mRightGroup.setInverted(true);
        }

        Encoder leftEncoder = configEncoder(kDriveLeftEncoderA, kDriveLeftEncoderB, false);
        Encoder rightEncoder = configEncoder(kDriveRightEncoderA, kDriveRightEncoderB, true);

        mEncoders = new DifferentialWheels<>(leftEncoder, rightEncoder);
        mAHRS = navX.getAhrs();
    }

    @Override
    public synchronized void onDisabled() {
        mInput.demandedRightPower = 0.0;
        mInput.demandedLeftPower = 0.0;
        mInput.shouldBeginOpenLoop = false;
        mInput.shouldBeginPIDLoop = false;
        mInput.shouldReverse = false;
        mInput.targetLeftDistance = 0;
        mInput.targetRightDistance = 0;
        mInput.leftPIDValues.reset();
        mInput.rightPIDValues.reset();
    }

    @Override
    public synchronized void onMeasure() {
        double oldLeft = mState.measuredLeftDistance;
        double oldRight = mState.measuredRightDistance;
        mState.measuredLeftDistance = mEncoders.getLeft().getDistance();
        mState.measuredRightDistance = mEncoders.getRight().getDistance();
        double now = Timer.getFPGATimestamp();
        double dt = constrainMinimum(now - mLastMeasuredTimeStamp, kTimeDeltaEpsilon);
        mLastMeasuredTimeStamp = now;

        DifferentialWheels<Double> dDistance = new DifferentialWheels<>(
                mState.measuredLeftDistance - oldLeft, mState.measuredRightDistance - oldRight);

        DifferentialWheels<Double> previousRate = new DifferentialWheels<>(mState.encoderRate);
        mState.encoderRate.set(mEncoders.transformed(Encoder::getRate));

        if (dt != 0) {
            // Add a new DtMeasurement object to the queue
            mState.velocityAverages.addLast(dDistance.transformed(distance -> new DtMeasurement(dt, distance)));
            if (mState.velocityAverages.size() > kVelocityAverageQueueSize) {
                mState.velocityAverages.removeFirst();
            }
            // Create a sum object
            DifferentialWheels<DtMeasurement> sum = new DifferentialWheels<>(new DtMeasurement(), new DtMeasurement());
            // Add up all the velocity averages
            mState.velocityAverages.forEach(wheels -> sum.transform(wheels, DtMeasurement::getAddedInPlace));
            // Calculate the ratio get meters per seconds
            mState.measuredVelocity.set(sum.transformed(DtMeasurement::getRatio));

            mState.encoderAcceleration.set(mState.encoderRate.transformed(previousRate,
                    (rateNow, ratePrev) -> (rateNow - ratePrev) / dt));
        }

        mState.accelerationX = mAHRS.getWorldLinearAccelX();
        mState.accelerationY = mAHRS.getWorldLinearAccelY();
        mState.accelerationZ = mAHRS.getWorldLinearAccelZ();
    }

    @Override
    public synchronized void onZeroSensors() {
        mState.measuredLeftDistance = 0.0;
        mState.measuredRightDistance = 0.0;
        mEncoders.apply(Encoder::reset);
    }

    @Override
    public synchronized void onOutput() {
        double limitedLeft = limit(mState.leftPower, kPreDriftSpeedLimit);
        double limitedRight = limit(mState.rightPower, kPreDriftSpeedLimit);
        if (mUseVictorConfig) {
            mLeftMaster.set(ControlMode.PercentOutput, limitedLeft * kLeftDriftOffset);
            mRightMaster.set(ControlMode.PercentOutput, limitedRight * kRightDriftOffset);
        } else {
            mLeftGroup.set(limit(limitedLeft * kLeftDriftOffset, kAbsoluteMaxOutput));
            mRightGroup.set(limit(limitedRight * kRightDriftOffset, kAbsoluteMaxOutput));
        }
    }

    @Override
    public synchronized void onUpdateState() {
        if (mInput.shouldBeginOpenLoop) {
            if (!mState.isOpenLoop) {
                mState.isOpenLoop = true;
                mState.isPIDLoop = false;
            }
        } else if (mInput.shouldBeginPIDLoop) {
            if (!mState.isPIDLoop) {
                mState.isOpenLoop = false;
                mState.isPIDLoop = true;
            }
        } else {
            mState.isPIDLoop = false;
            mState.isOpenLoop = false;
        }

        mState.isReversed = mInput.shouldReverse;

        if (mState.isOpenLoop) {

            double demandedLeft = mInput.demandedLeftPower * (mState.isReversed ? -1 : 1);
            double demandedRight = mInput.demandedRightPower * (mState.isReversed ? -1 : 1);
            double leftSpeedDiff = demandedLeft - mState.leftPower;
            double rightSpeedDiff = demandedRight - mState.rightPower;

            mState.leftPower += constrainMinimum(Math.min(kMaxLinearRampRate,
                    Math.abs(leftSpeedDiff * kRampIntervalMultiplier)) * Math.signum(leftSpeedDiff), kRampRateEpsilon);
            mState.rightPower += constrainMinimum(Math.min(kMaxLinearRampRate,
                    Math.abs(rightSpeedDiff * kRampIntervalMultiplier)) * Math.signum(rightSpeedDiff), kRampRateEpsilon);

            mState.leftPower = constrainMinimum(mState.leftPower, kOutputPowerEpsilon);
            mState.rightPower = constrainMinimum(mState.rightPower, kOutputPowerEpsilon);

            //For debugging
            //System.out.println(String.format("%.3f, %.3f", mState.leftPower, mState.rightPower));
            //System.out.println(String.format("%.3f, %.3f", mInput.demandedLeftPower, mInput.demandedRightPower));

        } else if (mState.isPIDLoop) {

            mInput.leftPIDValues.copyTo(mState.leftMiniPID);
            mInput.rightPIDValues.copyTo(mState.rightMiniPID);
            mState.leftMiniPID.setSetpoint(mInput.targetLeftDistance);
            mState.rightMiniPID.setSetpoint(mInput.targetRightDistance);
            mState.leftPower = mState.leftMiniPID.getOutput(mState.measuredLeftDistance);
            mState.rightPower = mState.rightMiniPID.getOutput(mState.measuredRightDistance);

        } else {

            mState.leftPower = 0;
            mState.rightPower = 0;
        }
    }

    @Override
    public synchronized void onReportState() {
        Robot.report(this, StateType.COMPONENT_INPUT, mInput);
        Robot.report(this, StateType.COMPONENT_STATE, mState);
    }

    @InputModifier
    @Deprecated
    public synchronized void cheesyDrive(ICheesyDriveInput driver) {
        mInput.shouldBeginOpenLoop = true;
        mInput.shouldBeginPIDLoop = false;
        mCheesyDrive.setInputsFromControls(driver);
        mCheesyDrive.calculateFeed();
    }

    @InputModifier
    public synchronized void cheesyDrive(double wheel, double throttle, boolean isQuickTurn) {
        mInput.shouldBeginOpenLoop = true;
        mInput.shouldBeginPIDLoop = false;
        mCheesyDrive.setInputs(wheel, throttle, isQuickTurn);
        mCheesyDrive.calculateFeed();
    }

    @InputModifier
    public synchronized void openLoopDrive(double leftSpeedDemand, double rightSpeedDemand) {
        mInput.shouldBeginOpenLoop = true;
        mInput.shouldBeginPIDLoop = false;
        mInput.demandedLeftPower = leftSpeedDemand;
        mInput.demandedRightPower = rightSpeedDemand;
    }

    @InputModifier
    public synchronized void setPIDTargetDistance(PIDValues pidValues, double targetDistance) {
        mInput.shouldBeginOpenLoop = false;
        mInput.shouldBeginPIDLoop = true;
        pidValues.copyTo(mInput.leftPIDValues);
        pidValues.copyTo(mInput.rightPIDValues);
        mInput.targetLeftDistance = targetDistance;
        mInput.targetRightDistance = targetDistance;
        mState.leftMiniPID.setOutputRampRate(kMaxLinearRampRate);
        mState.rightMiniPID.setOutputRampRate(kMaxLinearRampRate);
        mState.leftMiniPID.setOutputLimits(kMaximumPIDPower);
        mState.rightMiniPID.setOutputLimits(kMaximumPIDPower);
    }

    @InputModifier
    public synchronized void setReversed(boolean reversed) {
        mInput.shouldReverse = reversed;
    }

    public synchronized boolean isWithinDistanceRange(double distanceRange, double tolerance) {
        return Math.abs(distanceRange - mState.measuredLeftDistance) < Math.abs(tolerance) &&
                Math.abs(distanceRange - mState.measuredRightDistance) < Math.abs(tolerance);
    }

    public boolean shouldBeginOpenLoop() {
        return mInput.shouldBeginOpenLoop;
    }

    public boolean shouldBeginPIDLoop() {
        return mInput.shouldBeginPIDLoop;
    }

    static class Input {
        boolean shouldReverse;
        boolean shouldBeginOpenLoop;
        boolean shouldBeginPIDLoop;
        double demandedLeftPower;
        double demandedRightPower;
        double targetLeftDistance;
        double targetRightDistance;
        final PIDValues leftPIDValues = new PIDValues(0, 0, 0, 0);
        final PIDValues rightPIDValues = new PIDValues(0, 0, 0, 0);
    }

    static class State {
        boolean isReversed;
        boolean isOpenLoop;
        boolean isPIDLoop;
        double leftPower;
        double rightPower;
        double measuredLeftDistance;
        double measuredRightDistance;
        double accelerationX;
        double accelerationY;
        double accelerationZ;

        @IUnit.InchesPerSecond
        final DifferentialWheels<Double> measuredVelocity = DifferentialWheels.zeroes();
        @IUnit.InchesPerSecond
        final LinkedList<DifferentialWheels<DtMeasurement>> velocityAverages = new LinkedList<>();

        @IUnit.InchesPerSecond
        final DifferentialWheels<Double> encoderRate = DifferentialWheels.zeroes();

        @IUnit.InchesPerSecondSquared
        final DifferentialWheels<Double> encoderAcceleration = DifferentialWheels.zeroes();

        final MiniPID leftMiniPID = new MiniPID(0, 0, 0, 0);
        final MiniPID rightMiniPID = new MiniPID(0, 0, 0, 0);
    }
}