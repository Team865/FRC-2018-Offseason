package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.DifferentialWheels;
import ca.warp7.frc.commons.DtMeasurement;
import ca.warp7.frc.commons.PIDValues;
import ca.warp7.frc.commons.cheesy_drive.CheesyDrive;
import ca.warp7.frc.commons.cheesy_drive.ICheesyDriveInput;
import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.Robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
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

    private static final int kConfigTimeout = 100;
    private static final int kVelocityQueueSize = 5;
    private static final double kAbsoluteMaxOutput = 1.0;
    private static final double kMaximumPIDPower = 0.7;
    private static final double kMaxLinearRampRate = 1.0 / 20;
    private static final double kOutputPowerEpsilon = 1.0E-3;
    private static final double kTimeDeltaEpsilon = 1.0E-10;
    private static final double kLeftDriftOffset = 1.0;
    private static final double kRightDriftOffset = 1.0;
    private static final double kWheelRadius = 3.0;
    private static final double kWheelBaseRadius = 12.50;
    private static final double kEncoderTicksPerRevolution = 256.0;
    private static final double kPreDriftSpeedLimit = 1.0;
    private static final double kVoltageCompensationSaturation = 12.0;
    private static final double kNeutralDeadBand = 0.04;
    private static final double kRampSecondsFromNeutralToFull = 0.4;
    private AHRS mAHRS;
    private CheesyDrive mCheesyDrive;
    private VictorSPX mLeftMaster;
    private VictorSPX mLeftSlave;
    private VictorSPX mRightMaster;
    private VictorSPX mRightSlave;
    private SpeedControllerGroup mLeftGroup;
    private SpeedControllerGroup mRightGroup;
    private DifferentialWheels<Encoder> mEncoders;
    private final boolean mIsUsingNativeVictorAPI = false;
    private final Input mInput = new Input();
    private final State mState = new State();

    private static Encoder configEncoder(int channelA, int channelB, boolean reversed) {
        Encoder encoder = new Encoder(channelA, channelB, reversed, CounterBase.EncodingType.k4X);
        encoder.setDistancePerPulse((kWheelRadius * Math.PI) / kEncoderTicksPerRevolution);
        encoder.reset();
        return encoder;
    }

    private static WPI_VictorSPX createVictor(int deviceID) {
        return new WPI_VictorSPX(deviceID);
    }

    private static WPI_VictorSPX cast(VictorSPX victorSPX) {
        return (WPI_VictorSPX) victorSPX;
    }

    private static void configMaster(VictorSPX victor) {
        victor.enableVoltageCompensation(true);
        victor.configVoltageCompSaturation(kVoltageCompensationSaturation, kConfigTimeout);
        victor.configNeutralDeadband(kNeutralDeadBand, kConfigTimeout);
        victor.configOpenloopRamp(kRampSecondsFromNeutralToFull, kConfigTimeout);
        victor.configPeakOutputForward(kAbsoluteMaxOutput, kConfigTimeout);
        victor.configPeakOutputReverse(kAbsoluteMaxOutput, kConfigTimeout);
        victor.configNominalOutputForward(kOutputPowerEpsilon, kConfigTimeout);
        victor.configNominalOutputReverse(kOutputPowerEpsilon, kConfigTimeout);
    }

    @SuppressWarnings("unused")
    private void configAll() {
        configMaster(mLeftMaster);
        configMaster(mRightMaster);
        mRightMaster.setInverted(true);
        mLeftSlave.set(ControlMode.Follower, mLeftMaster.getDeviceID());
        mRightSlave.set(ControlMode.Follower, mRightMaster.getDeviceID());
        mRightSlave.setInverted(true);
    }

    private void resetAllVictors() {
        mLeftMaster.set(ControlMode.PercentOutput, 0);
        mRightMaster.set(ControlMode.PercentOutput, 0);
        mLeftSlave.set(ControlMode.PercentOutput, 0);
        mRightSlave.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void onConstruct() {
        mLeftMaster = createVictor(kDriveLeftMaster);
        mLeftSlave = createVictor(kDriveLeftSlave);
        mRightMaster = createVictor(kDriveRightMaster);
        mRightSlave = createVictor(kDriveRightSlave);
        Encoder leftEncoder = configEncoder(kDriveLeftEncoderA, kDriveLeftEncoderB, false);
        Encoder rightEncoder = configEncoder(kDriveRightEncoderA, kDriveRightEncoderB, true);
        mEncoders = new DifferentialWheels<>(leftEncoder, rightEncoder);
        mAHRS = navX.getAhrs();
        if (mIsUsingNativeVictorAPI) configAll();
        else {
            resetAllVictors();
            mLeftGroup = new SpeedControllerGroup(cast(mLeftMaster), cast(mLeftSlave));
            mRightGroup = new SpeedControllerGroup(cast(mRightMaster), cast(mRightSlave));
            mRightGroup.setInverted(true);
        }
        mCheesyDrive = new CheesyDrive((left, right) -> {
            mInput.demandedLeftPercentOutput = left;
            mInput.demandedRightPercentOutput = right;
        });
    }

    @Override
    public synchronized void onDisabled() {
        mInput.demandedRightPercentOutput = 0.0;
        mInput.demandedLeftPercentOutput = 0.0;
        mInput.shouldBeginOpenLoop = false;
        mInput.shouldBeginLinearPID = false;
        mInput.shouldReverse = false;
        mInput.targetLeftDistance = 0;
        mInput.targetRightDistance = 0;
        if (mIsUsingNativeVictorAPI) {
            mLeftMaster.set(ControlMode.Disabled, 0);
            mRightMaster.set(ControlMode.Disabled, 0);
        } else {
            mLeftGroup.disable();
            mRightGroup.disable();
        }
        onUpdateState();
    }

    @Override
    public synchronized void onMeasure() {
        mState.accelerationX = mAHRS.getWorldLinearAccelX();
        mState.accelerationY = mAHRS.getWorldLinearAccelY();
        double oldYaw = mState.yaw;
        mState.yaw = mAHRS.getYaw();
        mState.leftVoltage = mLeftMaster.getMotorOutputVoltage();
        mState.rightVoltage = mRightMaster.getMotorOutputVoltage();
        mState.maxVoltage = Math.max(Math.max(mState.leftVoltage, mState.rightVoltage), mState.maxVoltage);
        mState.minVoltage = Math.min(Math.min(mState.leftVoltage, mState.rightVoltage), mState.minVoltage);
        double oldLeft = mState.leftDistance;
        double oldRight = mState.rightDistance;
        mState.leftDistance = mEncoders.getLeft().getDistance();
        mState.rightDistance = mEncoders.getRight().getDistance();
        double timestamp = Timer.getFPGATimestamp();
        double dt = constrainMinimum(timestamp - mState.timestamp, kTimeDeltaEpsilon);
        mState.timestamp = timestamp;
        double dLeft = mState.leftDistance - oldLeft;
        double dRight = mState.rightDistance - oldRight;
        DifferentialWheels<Double> dDistance = new DifferentialWheels<>(dLeft, dRight);
        DifferentialWheels<Double> oldRate = new DifferentialWheels<>(mState.encoderRate);
        mState.encoderRate.set(mEncoders.transformed(Encoder::getRate));
        double velocitySum = mState.encoderRate.getLeft() + mState.encoderRate.getRight();
        double velocityDiff = mState.encoderRate.getLeft() - mState.encoderRate.getRight();
        mState.chassisLinearVelocity = kWheelRadius * velocitySum / 2.0;
        mState.chassisAngularVelocity = (kWheelRadius * velocityDiff) / (2.0 * kWheelBaseRadius);
        if (dt != 0) {
            mState.velocityAverages.addLast(dDistance.transformed(distance -> new DtMeasurement(dt, distance)));
            if (mState.velocityAverages.size() > kVelocityQueueSize) mState.velocityAverages.removeFirst();
            DifferentialWheels<DtMeasurement> sum = new DifferentialWheels<>(new DtMeasurement(), new DtMeasurement());
            mState.velocityAverages.forEach(wheels -> sum.transform(wheels, DtMeasurement::getAddedInPlace));
            mState.measuredVelocity.set(sum.transformed(DtMeasurement::getRatio));
            mState.encoderAcceleration.set(mState.encoderRate.transformed(oldRate, (now, prev) -> (now - prev) / dt));
            mState.yawChangeVelocity = (mState.yaw - oldYaw) / dt;
        }
    }

    @Override
    public synchronized void onZeroSensors() {
        mState.leftDistance = 0.0;
        mState.rightDistance = 0.0;
        mEncoders.apply(Encoder::reset);
        mAHRS.zeroYaw();
    }

    private synchronized void updateStateType() {
        if (mInput.shouldBeginOpenLoop) {
            if (!mState.isOpenLoop) {
                mState.isOpenLoop = true;
                mState.isLinearPID = false;
            }
        } else if (mInput.shouldBeginLinearPID) {
            if (!mState.isLinearPID) {
                mState.isOpenLoop = false;
                mState.isLinearPID = true;
            }
        } else {
            mState.isLinearPID = false;
            mState.isOpenLoop = false;
        }
    }

    @Override
    public synchronized void onUpdateState() {
        updateStateType();
        mState.isReversed = mInput.shouldReverse;
        if (mState.isOpenLoop) {
            double demandedLeft = mInput.demandedLeftPercentOutput * (mState.isReversed ? -1 : 1);
            double demandedRight = mInput.demandedRightPercentOutput * (mState.isReversed ? -1 : 1);
            if (mIsUsingNativeVictorAPI) {
                mState.leftPercentOutput = demandedLeft;
                mState.rightPercentOutput = demandedRight;
            } else {
                double leftDiff = demandedLeft - mState.leftPercentOutput;
                double rightDiff = demandedRight - mState.rightPercentOutput;
                mState.leftPercentOutput += Math.min(kMaxLinearRampRate, Math.abs(rightDiff)) * Math.signum(leftDiff);
                mState.rightPercentOutput += Math.min(kMaxLinearRampRate, Math.abs(rightDiff)) * Math.signum(rightDiff);
            }
            mState.leftPercentOutput = constrainMinimum(mState.leftPercentOutput, kOutputPowerEpsilon);
            mState.rightPercentOutput = constrainMinimum(mState.rightPercentOutput, kOutputPowerEpsilon);
            //System.out.println(String.format("%.3f, %.3f", mState.leftPercentOutput, mState.rightPercentOutput));
            //System.out.println(String.format("%.3f, %.3f", mInput.demandedLeftPercentOutput, mInput.demandedRightPercentOutput));
        } else if (mState.isLinearPID) {
            mState.leftLinearPID.setSetpoint(mInput.targetLeftDistance);
            mState.rightLinearPID.setSetpoint(mInput.targetRightDistance);
            mState.leftPercentOutput = mState.leftLinearPID.getOutput(mState.leftDistance);
            mState.rightPercentOutput = mState.rightLinearPID.getOutput(mState.rightDistance);
        } else {
            mState.leftPercentOutput = 0;
            mState.rightPercentOutput = 0;
        }
    }

    @Override
    public synchronized void onOutput() {
        double limitedLeft = limit(mState.leftPercentOutput, kPreDriftSpeedLimit);
        double limitedRight = limit(mState.rightPercentOutput, kPreDriftSpeedLimit);
        if (mIsUsingNativeVictorAPI) {
            mLeftMaster.set(ControlMode.PercentOutput, limitedLeft * kLeftDriftOffset);
            mRightMaster.set(ControlMode.PercentOutput, limitedRight * kRightDriftOffset);
            mLeftSlave.follow(mLeftMaster);
            mRightSlave.follow(mRightMaster);
        } else {
            mLeftGroup.set(limit(limitedLeft * kLeftDriftOffset, kAbsoluteMaxOutput));
            mRightGroup.set(limit(limitedRight * kRightDriftOffset, kAbsoluteMaxOutput));
        }
    }

    @Override
    public synchronized void onReportState() {
        Robot.reportInputAndState(this, mInput, mState);
    }

    @InputModifier
    @Deprecated
    public synchronized void cheesyDrive(ICheesyDriveInput driver) {
        mInput.shouldBeginOpenLoop = true;
        mInput.shouldBeginLinearPID = false;
        mCheesyDrive.setInputsFromControls(driver);
        mCheesyDrive.calculateFeed();
    }

    @InputModifier
    public synchronized void cheesyDrive(double wheel, double throttle, boolean isQuickTurn) {
        mInput.shouldBeginOpenLoop = true;
        mInput.shouldBeginLinearPID = false;
        mCheesyDrive.setInputs(wheel, throttle, isQuickTurn);
        mCheesyDrive.calculateFeed();
    }

    @InputModifier
    public synchronized void openLoopDrive(double leftSpeedDemand, double rightSpeedDemand) {
        mInput.shouldBeginOpenLoop = true;
        mInput.shouldBeginLinearPID = false;
        mInput.demandedLeftPercentOutput = leftSpeedDemand;
        mInput.demandedRightPercentOutput = rightSpeedDemand;
    }

    @InputModifier
    public synchronized void setPIDTargetDistance(PIDValues pidValues, double targetDistance) {
        mInput.shouldBeginOpenLoop = false;
        mInput.shouldBeginLinearPID = true;
        pidValues.copyTo(mState.leftLinearPID);
        pidValues.copyTo(mState.rightLinearPID);
        mInput.targetLeftDistance = targetDistance;
        mInput.targetRightDistance = targetDistance;
        mState.leftLinearPID.setOutputRampRate(kMaxLinearRampRate);
        mState.rightLinearPID.setOutputRampRate(kMaxLinearRampRate);
        mState.leftLinearPID.setOutputLimits(kMaximumPIDPower);
        mState.rightLinearPID.setOutputLimits(kMaximumPIDPower);
    }

    @InputModifier
    public synchronized void setReversed(boolean reversed) {
        mInput.shouldReverse = reversed;
    }

    public synchronized boolean isWithinDistanceRange(double range, double tolerance) {
        return Math.abs(range - mState.leftDistance) < Math.abs(tolerance) &&
                Math.abs(range - mState.rightDistance) < Math.abs(tolerance);
    }

    public boolean shouldBeginOpenLoop() {
        return mInput.shouldBeginOpenLoop;
    }

    public boolean shouldBeginLinearPID() {
        return mInput.shouldBeginLinearPID;
    }

    static class Input {
        boolean shouldReverse;
        boolean shouldBeginOpenLoop;
        boolean shouldBeginLinearPID;
        double demandedLeftPercentOutput;
        double demandedRightPercentOutput;
        double targetLeftDistance;
        double targetRightDistance;
    }

    static class State {
        boolean isReversed;
        boolean isOpenLoop;
        boolean isLinearPID;
        double timestamp;
        double leftPercentOutput;
        double rightPercentOutput;
        double leftDistance;
        double rightDistance;
        double accelerationX;
        double accelerationY;
        double yaw;
        double yawChangeVelocity;
        double chassisLinearVelocity;
        double chassisAngularVelocity;
        double leftVoltage;
        double rightVoltage;
        double maxVoltage;
        double minVoltage;
        final DifferentialWheels<Double> measuredVelocity = DifferentialWheels.zeroes();
        final LinkedList<DifferentialWheels<DtMeasurement>> velocityAverages = new LinkedList<>();
        final DifferentialWheels<Double> encoderRate = DifferentialWheels.zeroes();
        final DifferentialWheels<Double> encoderAcceleration = DifferentialWheels.zeroes();
        final MiniPID leftLinearPID = new MiniPID(0, 0, 0, 0);
        final MiniPID rightLinearPID = new MiniPID(0, 0, 0, 0);
    }
}