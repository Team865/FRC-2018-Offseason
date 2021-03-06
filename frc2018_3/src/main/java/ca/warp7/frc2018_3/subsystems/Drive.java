package ca.warp7.frc2018_3.subsystems;

import ca.warp7.action.IAction;
import ca.warp7.frc.*;
import ca.warp7.frc.core.ISubsystem;
import ca.warp7.frc2018_3.Constants;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.stormbots.MiniPID;
import edu.wpi.first.wpilibj.*;

import java.util.LinkedList;

import static ca.warp7.frc.Functions.constrainMinimum;
import static ca.warp7.frc.Functions.limit;
import static ca.warp7.frc.core.Robot.reportInputAndState;
import static ca.warp7.frc2018_3.Components.navx;

public class Drive implements ISubsystem {

    private static final int kConfigTimeout = 100;
    private static final int kVelocityQueueSize = 5;
    private static final double kAbsoluteMaxOutput = 1.0;
    private static final double kMaximumPIDPower = 0.7;
    private static final double kMaxLinearRamp = 1.0 / 20;
    private static final double kOutputPowerEpsilon = 1.0E-3;
    private static final double kTimeDeltaEpsilon = 1.0E-10;
    private static final double kLeftDriftOffset = 1.0;
    private static final double kRightDriftOffset = 1.0;
    private static final double kWheelRadius = 3.0;
    // TODO update kWheelBaseRadius for 2 front Colson drive train
    private static final double kWheelBaseRadius = 12.50;
    private static final double kEncoderTicksPerRevolution = 256.0;
    private static final double kPreDriftSpeedLimit = 1.0;
    private static final double kVoltageCompensationSaturation = 12.0;
    private static final double kNeutralDeadBand = 0.04;
    private static final double kOpenLoopRampNeutralToFull = 0.4;
    private static final double kClosedLoopRampNeutralToFull = 0.4;
    private static final double kAxisDeadband = 0.2;
    private CheesyDrive mCheesyDrive;
    private AHRS mAHRS;
    private VictorSPX mLeftA;
    private VictorSPX mLeftB;
    private VictorSPX mRightA;
    private VictorSPX mRightB;
    private SpeedControllerGroup mLeftGroup;
    private SpeedControllerGroup mRightGroup;
    private DifferentialVector<Encoder> mEncoders;
    private Solenoid mShifterSolenoid;
    private final boolean mIsUsingNativeVictorAPI = false;
    private final Input mInput = new Input();
    private final State mState = new State();

    private enum Action {
        Brake,
        OpenLoop,
        LinearPID
    }

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

    @SuppressWarnings("unused")
    private static void configMaster(VictorSPX master) {
        master.setNeutralMode(NeutralMode.Brake);
        master.enableVoltageCompensation(true);
        master.configVoltageCompSaturation(kVoltageCompensationSaturation, kConfigTimeout);
        master.configNeutralDeadband(kNeutralDeadBand, kConfigTimeout);
        master.configOpenloopRamp(kOpenLoopRampNeutralToFull, kConfigTimeout);
        master.configClosedloopRamp(kClosedLoopRampNeutralToFull, kConfigTimeout);
        master.configPeakOutputForward(kAbsoluteMaxOutput, kConfigTimeout);
        master.configPeakOutputReverse(kAbsoluteMaxOutput, kConfigTimeout);
        master.configNominalOutputForward(kOutputPowerEpsilon, kConfigTimeout);
        master.configNominalOutputReverse(kOutputPowerEpsilon, kConfigTimeout);
        master.configForwardSoftLimitEnable(false, kConfigTimeout);
        master.configReverseSoftLimitEnable(false, kConfigTimeout);
    }

    private static double linearScaleDeadband(double n) {
        return Math.abs(n) < kAxisDeadband ? 0 : (n - Math.copySign(kAxisDeadband, n)) / (1 - kAxisDeadband);
    }

    private void configAll() {
        //configMaster(mLeftA);
        //configMaster(mRightA);
        mLeftA.setInverted(true);
        mLeftB.setInverted(true);
        mLeftB.set(ControlMode.Follower, mLeftA.getDeviceID());
        mRightB.set(ControlMode.Follower, mRightA.getDeviceID());
    }

    @Override
    public void onConstruct() {
        mLeftA = createVictor(Constants.kDriveLeftA);
        mLeftB = createVictor(Constants.kDriveLeftB);
        mRightA = createVictor(Constants.kDriveRightA);
        mRightB = createVictor(Constants.kDriveRightB);
        Encoder leftEncoder = configEncoder(Constants.kDriveLeftEncoderA, Constants.kDriveLeftEncoderB, false);
        Encoder rightEncoder = configEncoder(Constants.kDriveRightEncoderA, Constants.kDriveRightEncoderB, true);

        mEncoders = new DifferentialVector<>(leftEncoder, rightEncoder);
        mAHRS = navx.getAhrs();
        mLeftGroup = new SpeedControllerGroup(cast(mLeftA), cast(mLeftB));
        mRightGroup = new SpeedControllerGroup(cast(mRightA), cast(mRightB));
        if (mIsUsingNativeVictorAPI) configAll();
        else mLeftGroup.setInverted(true);

        mShifterSolenoid = new Solenoid(Constants.kDriveShifterSolenoidPin);
        mShifterSolenoid.set(true);
        mShifterSolenoid.set(false);

        mCheesyDrive = new CheesyDrive(this::openLoopDrive);
        mCheesyDrive.disableInternalDeadband();
    }

    @Override
    public synchronized void onDisabled() {
        mInput.rightPercentOutputDemand = 0.0;
        mInput.leftPercentOutputDemand = 0.0;
        mInput.shouldReverse = false;
        mInput.leftTargetDistance = 0;
        mInput.rightTargetDistance = 0;
        mInput.shouldSolenoidBeOnForShifter = false;
        mShifterSolenoid.set(false);
        if (mIsUsingNativeVictorAPI) {
            mLeftA.set(ControlMode.Disabled, 0);
            mRightA.set(ControlMode.Disabled, 0);
        } else {
            mLeftGroup.disable();
            mRightGroup.disable();
        }
        mInput.wantedAction = Action.Brake;
        onUpdateState();
    }

    @Override
    public synchronized void onMeasure() {
        double oldLeft = mState.leftDistance;
        double oldRight = mState.rightDistance;
        mState.leftDistance = mEncoders.getLeft().getDistance();
        mState.rightDistance = mEncoders.getRight().getDistance();
        double timestamp = Timer.getFPGATimestamp();
        double dt = constrainMinimum(timestamp - mState._timestamp, kTimeDeltaEpsilon);
        mState._timestamp = timestamp;
        double dLeft = mState.leftDistance - oldLeft;
        double dRight = mState.rightDistance - oldRight;
        DifferentialVector<Double> dDistance = new DifferentialVector<>(dLeft, dRight);
        DifferentialVector<Double> oldRate = new DifferentialVector<>(mState.encoderRate);
        mState.encoderRate.set(mEncoders.transformed(Encoder::getRate));
        double velocitySum = mState.encoderRate.getLeft() + mState.encoderRate.getRight();
        double velocityDiff = mState.encoderRate.getLeft() - mState.encoderRate.getRight();
        mState.chassisLinearVelocity = kWheelRadius * velocitySum / 2.0;
        mState.chassisAngularVelocity = (kWheelRadius * velocityDiff) / (2.0 * kWheelBaseRadius);
        double oldYaw = mState._yaw;
        mState._yaw = mAHRS.getYaw() + 90;
        double yawInRadians = Unit.Degrees.toRadians(mState._yaw);
        double dAverageDistance = (dLeft + dRight) / 2.0;
        mState.predictedX += Math.cos(yawInRadians) * dAverageDistance;
        mState.predictedY += Math.sin(yawInRadians) * dAverageDistance;
        mState.velocityAverages.addLast(dDistance.transformed(distance -> new DtMeasurement(dt, distance)));
        if (mState.velocityAverages.size() > kVelocityQueueSize) mState.velocityAverages.removeFirst();
        DifferentialVector<DtMeasurement> sum = new DifferentialVector<>(new DtMeasurement(), new DtMeasurement());
        mState.velocityAverages.forEach(wheels -> sum.transform(wheels, DtMeasurement::getAddedInPlace));
        mState.measuredVelocity.set(sum.transformed(DtMeasurement::getRatio));
        mState.encoderAcceleration.set(mState.encoderRate.transformed(oldRate, (now, prev) -> (now - prev) / dt));
        mState.yawChangeVelocity = (mState._yaw - oldYaw) / dt;
    }

    @Override
    public synchronized void onZeroSensors() {
        mState.leftDistance = 0.0;
        mState.rightDistance = 0.0;
        mState.predictedX = 0;
        mState.predictedY = 0;
        mEncoders.apply(Encoder::reset);
        mAHRS.zeroYaw();
    }

    @Override
    public synchronized void onUpdateState() {
        mState.action = mInput.wantedAction;
        mState.isReversed = mInput.shouldReverse;
        mState.isSolenoidOnForShifter = mInput.shouldSolenoidBeOnForShifter;
        switch (mState.action) {
            case Brake:
                mState.leftPercentOutput = 0;
                mState.rightPercentOutput = 0;
                break;
            case OpenLoop:
                double demandedLeft = mInput.leftPercentOutputDemand * (mState.isReversed ? -1 : 1);
                double demandedRight = mInput.rightPercentOutputDemand * (mState.isReversed ? -1 : 1);
                if (mIsUsingNativeVictorAPI) {
                    mState.leftPercentOutput = demandedLeft;
                    mState.rightPercentOutput = demandedRight;
                } else {
                    double leftDiff = demandedLeft - mState.leftPercentOutput;
                    double rightDiff = demandedRight - mState.rightPercentOutput;
                    mState.leftPercentOutput += Math.min(kMaxLinearRamp, Math.abs(leftDiff)) * Math.signum(leftDiff);
                    mState.rightPercentOutput += Math.min(kMaxLinearRamp, Math.abs(rightDiff)) * Math.signum(rightDiff);
                }
                mState.leftPercentOutput = constrainMinimum(mState.leftPercentOutput, kOutputPowerEpsilon);
                mState.rightPercentOutput = constrainMinimum(mState.rightPercentOutput, kOutputPowerEpsilon);
                //println(String.format("%.3f, %.3f", mState.leftPercentOutput, mState.rightPercentOutput));
                //println(String.format("%.3f, %.3f", mInput.leftPercentOutputDemand, mInput.rightPercentOutputDemand));
                break;
            case LinearPID:
                mState.leftLinearPID.setSetpoint(mInput.leftTargetDistance);
                mState.rightLinearPID.setSetpoint(mInput.rightTargetDistance);
                mState.leftPercentOutput = mState.leftLinearPID.getOutput(mState.leftDistance);
                mState.rightPercentOutput = mState.rightLinearPID.getOutput(mState.rightDistance);
                break;
        }
    }

    @Override
    public synchronized void onOutput() {
        double limitedLeft = limit(mState.leftPercentOutput, kPreDriftSpeedLimit);
        double limitedRight = limit(mState.rightPercentOutput, kPreDriftSpeedLimit);
        if (mIsUsingNativeVictorAPI) {
            mLeftA.set(ControlMode.PercentOutput, limitedLeft * kLeftDriftOffset);
            mRightA.set(ControlMode.PercentOutput, limitedRight * kRightDriftOffset);
            mLeftB.follow(mLeftA);
            mRightB.follow(mRightA);
        } else {
            mLeftGroup.set(limit(limitedLeft * kLeftDriftOffset, kAbsoluteMaxOutput));
            mRightGroup.set(limit(limitedRight * kRightDriftOffset, kAbsoluteMaxOutput));
        }
        if (mState.isSolenoidOnForShifter) {
            if (!mShifterSolenoid.get()) {
                mShifterSolenoid.set(true);
            }
        } else if (mShifterSolenoid.get()) {
            mShifterSolenoid.set(false);
        }
    }

    @Override
    public synchronized void onReportState() {
        mState.accelerationX = mAHRS.getWorldLinearAccelX();
        mState.accelerationY = mAHRS.getWorldLinearAccelY();
        mState.leftVoltage = mLeftA.getMotorOutputVoltage();
        mState.rightVoltage = mRightA.getMotorOutputVoltage();
        mState.maxVoltage = Math.max(Math.max(mState.leftVoltage, mState.rightVoltage), mState.maxVoltage);
        mState.minVoltage = Math.min(Math.min(mState.leftVoltage, mState.rightVoltage), mState.minVoltage);
        mState.leftCurrent = mLeftA.getOutputCurrent();
        mState.rightCurrent = mRightA.getOutputCurrent();
        mState.leftTemperature = mLeftA.getTemperature();
        mState.rightTemperature = mRightA.getTemperature();
        reportInputAndState(this, mInput, mState);
    }

    @InputModifier
    public synchronized void cheesyDrive(double wheel, double throttle, boolean isQuickTurn) {
        mCheesyDrive.cheesyDrive(linearScaleDeadband(wheel), linearScaleDeadband(throttle), isQuickTurn);
    }

    @InputModifier
    public synchronized void openLoopDrive(double leftSpeedDemand, double rightSpeedDemand) {
        mInput.wantedAction = Action.OpenLoop;
        mInput.leftPercentOutputDemand = leftSpeedDemand;
        mInput.rightPercentOutputDemand = rightSpeedDemand;
    }

    @InputModifier
    public synchronized void setPIDTargetDistance(PIDValues pidValues, double targetDistance) {
        mInput.wantedAction = Action.LinearPID;
        pidValues.copyTo(mState.leftLinearPID);
        pidValues.copyTo(mState.rightLinearPID);
        mInput.leftTargetDistance = targetDistance;
        mInput.rightTargetDistance = targetDistance;
        mState.leftLinearPID.setOutputRampRate(kMaxLinearRamp);
        mState.rightLinearPID.setOutputRampRate(kMaxLinearRamp);
        mState.leftLinearPID.setOutputLimits(kMaximumPIDPower);
        mState.rightLinearPID.setOutputLimits(kMaximumPIDPower);
    }

    @InputModifier
    public synchronized void setReversed(boolean reversed) {
        mInput.shouldReverse = reversed;
    }

    @InputModifier
    public synchronized void setShouldSolenoidBeOnForShifter(boolean shouldSolenoidBeOnForShifter) {
        mInput.shouldSolenoidBeOnForShifter = shouldSolenoidBeOnForShifter;
    }

    public synchronized boolean isWithinDistanceRange(double range, double tolerance) {
        return Math.abs(range - mState.leftDistance) < Math.abs(tolerance) &&
                Math.abs(range - mState.rightDistance) < Math.abs(tolerance);
    }

    public boolean isPIDReached(IAction.Delegate delegate) {
        return true;
    }

    public double getProgress() {
        return 0;
    }

    public boolean shouldBeginOpenLoop() {
        return mInput.wantedAction == Action.OpenLoop;
    }

    public boolean shouldBeginLinearPID() {
        return mInput.wantedAction == Action.LinearPID;
    }

    static class Input {
        Action wantedAction;
        boolean shouldReverse;
        boolean shouldSolenoidBeOnForShifter;
        double leftPercentOutputDemand;
        double rightPercentOutputDemand;
        double leftTargetDistance;
        double rightTargetDistance;
    }

    static class State {
        Action action;
        boolean isReversed;
        boolean isSolenoidOnForShifter;
        double _timestamp;
        double leftPercentOutput;
        double rightPercentOutput;
        double leftDistance;
        double rightDistance;
        double accelerationX;
        double accelerationY;
        double _yaw;
        double yawChangeVelocity;
        double chassisLinearVelocity;
        double chassisAngularVelocity;
        double leftVoltage;
        double rightVoltage;
        double maxVoltage;
        double minVoltage;
        double leftCurrent;
        double rightCurrent;
        double leftTemperature;
        double rightTemperature;
        double predictedX;
        double predictedY;
        final DifferentialVector<Double> measuredVelocity = DifferentialVector.zeroes();
        final LinkedList<DifferentialVector<DtMeasurement>> velocityAverages = new LinkedList<>();
        final DifferentialVector<Double> encoderRate = DifferentialVector.zeroes();
        final DifferentialVector<Double> encoderAcceleration = DifferentialVector.zeroes();
        final MiniPID leftLinearPID = new MiniPID(0, 0, 0, 0);
        final MiniPID rightLinearPID = new MiniPID(0, 0, 0, 0);
    }
}