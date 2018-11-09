package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.DifferentialVector;
import ca.warp7.frc.commons.DtMeasurement;
import ca.warp7.frc.commons.PIDValues;
import ca.warp7.frc.commons.cheesy_drive.CheesyDrive;
import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.Robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
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
    private static final double kMaxLinearRamp = 1.0 / 20;
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
    private static final double kOpenLoopRampNeutralToFull = 0.4;
    private static final double kClosedLoopRampNeutralToFull = 0.4;
    private static final double kAxisDeadband = 0.2;
    private CheesyDrive mCheesyDrive;
    private AHRS mAHRS;
    private VictorSPX mLeftMaster;
    private VictorSPX mLeftSlave;
    private VictorSPX mRightMaster;
    private VictorSPX mRightSlave;
    private SpeedControllerGroup mLeftGroup;
    private SpeedControllerGroup mRightGroup;
    private DifferentialVector<Encoder> mEncoders;
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
    }

    private static double linearScaleDeadband(double n) {
        return Math.abs(n) < kAxisDeadband ? 0 : (n - Math.copySign(kAxisDeadband, n)) / (1 - kAxisDeadband);
    }

    @SuppressWarnings("unused")
    private void configAll() {
        //configMaster(mLeftMaster);
        //configMaster(mRightMaster);

        mLeftMaster.setInverted(true);
        mLeftSlave.setInverted(true);

        mLeftSlave.set(ControlMode.Follower, mLeftMaster.getDeviceID());
        mRightSlave.set(ControlMode.Follower, mRightMaster.getDeviceID());
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
        mEncoders = new DifferentialVector<>(leftEncoder, rightEncoder);
        mAHRS = navX.getAhrs();
        resetAllVictors();
        if (mIsUsingNativeVictorAPI) configAll();
        else {
            mLeftGroup = new SpeedControllerGroup(cast(mLeftMaster), cast(mLeftSlave));
            mRightGroup = new SpeedControllerGroup(cast(mRightMaster), cast(mRightSlave));
            mLeftGroup.setInverted(true);
        }
        mCheesyDrive = new CheesyDrive(this::openLoopDrive);
        mCheesyDrive.disableInternalDeadband();
    }

    @Override
    public synchronized void onDisabled() {
        mInput.wantedAction = SystemAction.Idle;
        mInput.demandedRightPercentOutput = 0.0;
        mInput.demandedLeftPercentOutput = 0.0;
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
        DifferentialVector<Double> dDistance = new DifferentialVector<>(dLeft, dRight);
        DifferentialVector<Double> oldRate = new DifferentialVector<>(mState.encoderRate);
        mState.encoderRate.set(mEncoders.transformed(Encoder::getRate));
        double velocitySum = mState.encoderRate.getLeft() + mState.encoderRate.getRight();
        double velocityDiff = mState.encoderRate.getLeft() - mState.encoderRate.getRight();
        mState.chassisLinearVelocity = kWheelRadius * velocitySum / 2.0;
        mState.chassisAngularVelocity = (kWheelRadius * velocityDiff) / (2.0 * kWheelBaseRadius);

        if (dt != 0) {
            mState.velocityAverages.addLast(dDistance.transformed(distance -> new DtMeasurement(dt, distance)));
            if (mState.velocityAverages.size() > kVelocityQueueSize) mState.velocityAverages.removeFirst();
            DifferentialVector<DtMeasurement> sum = new DifferentialVector<>(new DtMeasurement(), new DtMeasurement());
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

    @Override
    public synchronized void onUpdateState() {
        mState.action = mInput.wantedAction;
        mState.isReversed = mInput.shouldReverse;
        switch (mState.action) {
            case Idle:
                mState.leftPercentOutput = 0;
                mState.rightPercentOutput = 0;
                break;
            case OpenLoop:
                double demandedLeft = mInput.demandedLeftPercentOutput * (mState.isReversed ? -1 : 1);
                double demandedRight = mInput.demandedRightPercentOutput * (mState.isReversed ? -1 : 1);
                if (mIsUsingNativeVictorAPI) {
                    mState.leftPercentOutput = demandedLeft;
                    mState.rightPercentOutput = demandedRight;
                } else {
                    double leftDiff = demandedLeft - mState.leftPercentOutput;
                    double rightDiff = demandedRight - mState.rightPercentOutput;
                    mState.leftPercentOutput += Math.min(kMaxLinearRamp, Math.abs(rightDiff)) * Math.signum(leftDiff);
                    mState.rightPercentOutput += Math.min(kMaxLinearRamp, Math.abs(rightDiff)) * Math.signum(rightDiff);
                }
                mState.leftPercentOutput = constrainMinimum(mState.leftPercentOutput, kOutputPowerEpsilon);
                mState.rightPercentOutput = constrainMinimum(mState.rightPercentOutput, kOutputPowerEpsilon);
                //System.out.println(String.format("%.3f, %.3f", mState.leftPercentOutput,
                // mState.rightPercentOutput));
                //System.out.println(String.format("%.3f, %.3f", mInput.demandedLeftPercentOutput,
                // mInput.demandedRightPercentOutput));
                break;
            case LinearPID:
                mState.leftLinearPID.setSetpoint(mInput.targetLeftDistance);
                mState.rightLinearPID.setSetpoint(mInput.targetRightDistance);
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
    public synchronized void cheesyDrive(double wheel, double throttle, boolean isQuickTurn) {
        mCheesyDrive.cheesyDrive(linearScaleDeadband(wheel), linearScaleDeadband(throttle), isQuickTurn);
    }

    @InputModifier
    public synchronized void openLoopDrive(double leftSpeedDemand, double rightSpeedDemand) {
        mInput.wantedAction = SystemAction.OpenLoop;
        mInput.demandedLeftPercentOutput = leftSpeedDemand;
        mInput.demandedRightPercentOutput = rightSpeedDemand;
    }

    @InputModifier
    public synchronized void setPIDTargetDistance(PIDValues pidValues, double targetDistance) {
        mInput.wantedAction = SystemAction.LinearPID;
        pidValues.copyTo(mState.leftLinearPID);
        pidValues.copyTo(mState.rightLinearPID);
        mInput.targetLeftDistance = targetDistance;
        mInput.targetRightDistance = targetDistance;
        mState.leftLinearPID.setOutputRampRate(kMaxLinearRamp);
        mState.rightLinearPID.setOutputRampRate(kMaxLinearRamp);
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
        return mInput.wantedAction == SystemAction.OpenLoop;
    }

    public boolean shouldBeginLinearPID() {
        return mInput.wantedAction == SystemAction.LinearPID;
    }

    enum SystemAction {
        Idle,
        OpenLoop,
        LinearPID
    }

    static class Input {
        SystemAction wantedAction;
        boolean shouldReverse;
        double demandedLeftPercentOutput;
        double demandedRightPercentOutput;
        double targetLeftDistance;
        double targetRightDistance;
    }

    static class State {
        SystemAction action;
        boolean isReversed;
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
        final DifferentialVector<Double> measuredVelocity = DifferentialVector.zeroes();
        final LinkedList<DifferentialVector<DtMeasurement>> velocityAverages = new LinkedList<>();
        final DifferentialVector<Double> encoderRate = DifferentialVector.zeroes();
        final DifferentialVector<Double> encoderAcceleration = DifferentialVector.zeroes();
        final MiniPID leftLinearPID = new MiniPID(0, 0, 0, 0);
        final MiniPID rightLinearPID = new MiniPID(0, 0, 0, 0);
    }
}