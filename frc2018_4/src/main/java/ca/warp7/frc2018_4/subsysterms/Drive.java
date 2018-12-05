package ca.warp7.frc2018_4.subsysterms;

import ca.warp7.frc.CheesyDrive;
import ca.warp7.frc.DifferentialVector;
import ca.warp7.frc.DtMeasurement;
import ca.warp7.frc.Unit;
import ca.warp7.frc.core.ISubsystem;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.stormbots.MiniPID;
import edu.wpi.first.wpilibj.*;

import static ca.warp7.frc.Functions.constrainMinimum;
import static ca.warp7.frc.Functions.limit;
import static ca.warp7.frc.core.Robot.reportInputAndState;
import static ca.warp7.frc2018_4.Components.navx;
import java.util.LinkedList;

import static ca.warp7.frc2018_4.constants.DriveConstants.*;
import static ca.warp7.frc2018_4.constants.Pins.*;

public class Drive implements ISubsystem {
    private CheesyDrive mCheesyDrive;
    private AHRS mAHRS;
    private VictorSPX mLeftDriveMotorA;
    private VictorSPX mLeftDriveMotorB;
    private VictorSPX mRightDriveMotorA;
    private VictorSPX mRightDriveMotorB;
    private SpeedControllerGroup mLeftGroup;
    private SpeedControllerGroup mRightGroup;
    private DifferentialVector<Encoder> mEncoders;
    private Solenoid mShifterSolenoid;

    private final Input mInput = new Input();
    private final State mState = new State();

    @Override
    public synchronized void onDisabled() {
        mInput.rightPercentOutputDemand = 0.0;
        mInput.leftPercentOutputDemand = 0.0;
        mInput.shouldReverse = false;
        mInput.leftTargetDistance = 0;
        mInput.rightTargetDistance = 0;
        mInput.shouldSolenoidBeOnForShifter = false;
        mShifterSolenoid.set(false);
        mInput.wantedAction = Action.Brake;
        onUpdateState();
    }

    @Override
    public synchronized void onMeasure() {
        double oldLeft = mState.leftDistance;
        double oldRight = mState.rightDistance;
        mState.leftDistance = mEncoders.getLeft().getDistance();
        mState.rightDistance = mEncoders.getRight().getDistance();
        double newTimestamp = Timer.getFPGATimestamp();
        double dt = constrainMinimum(newTimestamp - mState._timestamp, kTimeDeltaEpsilon);
        mState._timestamp = newTimestamp;
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
    public synchronized void onOutput() {
        double limitedLeft = limit(mState.leftPercentOutput, kPreDriftSpeedLimit);
        double limitedRight = limit(mState.rightPercentOutput, kPreDriftSpeedLimit);
        mLeftGroup.set(limit(limitedLeft * kLeftDriftOffset, kAbsoluteMaxOutput));
        mRightGroup.set(limit(limitedRight * kRightDriftOffset, kAbsoluteMaxOutput));
        if (mState.isSolenoidOnForShifter) {
            if (!mShifterSolenoid.get()) {
                mShifterSolenoid.set(true);
            }
        } else if (mShifterSolenoid.get()) {
            mShifterSolenoid.set(false);
        }
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
                double leftDiff = demandedLeft - mState.leftPercentOutput;
                double rightDiff = demandedRight - mState.rightPercentOutput;
                mState.leftPercentOutput += Math.min(kMaxLinearRamp, Math.abs(leftDiff)) * Math.signum(leftDiff);
                mState.rightPercentOutput += Math.min(kMaxLinearRamp, Math.abs(rightDiff)) * Math.signum(rightDiff);
                mState.leftPercentOutput = constrainMinimum(mState.leftPercentOutput, kOutputPowerEpsilon);
                mState.rightPercentOutput = constrainMinimum(mState.rightPercentOutput, kOutputPowerEpsilon);
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
    public synchronized void onReportState() {
        mState.leftVoltage = mLeftDriveMotorA.getMotorOutputVoltage();
        mState.rightVoltage = mRightDriveMotorA.getMotorOutputVoltage();
        reportInputAndState(this, mState, mInput);
    }

    @Override
    public synchronized void onConstruct() {
        mLeftDriveMotorA = createVictor(kDriveLeftA);
        mLeftDriveMotorB = createVictor(kDriveLeftB);
        mRightDriveMotorA = createVictor(kDriveRightA);
        mRightDriveMotorB = createVictor(kDriveRightB);
        Encoder leftEncoder = configEncoder(kDriveLeftEncoderA, kDriveLeftEncoderB, kLefttSideEncodeReversed);
        Encoder rightEncoder = configEncoder(kDriveRightEncoderA, kDriveRightEncoderB, kRightSideEncodeReversed);

        mEncoders = new DifferentialVector<>(leftEncoder, rightEncoder);
        mAHRS = navx.getAhrs();
        mLeftGroup = new SpeedControllerGroup(cast(mLeftDriveMotorA), cast(mLeftDriveMotorB));
        mRightGroup = new SpeedControllerGroup(cast(mRightDriveMotorA), cast(mRightDriveMotorB));
        mLeftGroup.setInverted(kLeftSideInverted);
        mRightGroup.setInverted(kRightSideInverted);

        mShifterSolenoid = new Solenoid(kDriveShifterSolenoidPin);
        mShifterSolenoid.set(true);
        mShifterSolenoid.set(false);

        mCheesyDrive = new CheesyDrive(this::openLoopDrive);
        mCheesyDrive.disableInternalDeadband();
    }

    private synchronized void openLoopDrive(double leftSpeedDemand, double rightSpeedDemand) {
        mInput.wantedAction = Action.OpenLoop;
        mInput.leftPercentOutputDemand = leftSpeedDemand;
        mInput.rightPercentOutputDemand = rightSpeedDemand;
    }

    private synchronized Encoder configEncoder(int channelA, int channelB, boolean reversed) {
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

    @InputModifier
    public synchronized void cheesyDrive(double wheel, double throttle, boolean isQuickTurn) {
        mCheesyDrive.cheesyDrive(linearScaleDeadband(wheel), linearScaleDeadband(throttle), isQuickTurn);
    }

    @InputModifier
    public synchronized void setShouldSolenoidBeOnForShifter(boolean shouldSolenoidBeOnForShifter) {
        mInput.shouldSolenoidBeOnForShifter = shouldSolenoidBeOnForShifter;
    }

    @InputModifier
    public synchronized void setReversed(boolean reversed) {
        mInput.shouldReverse = reversed;
    }

    private synchronized double linearScaleDeadband(double n) {
        return Math.abs(n) < kAxisDeadband ? 0 : (n - Math.copySign(kAxisDeadband, n)) / (1 - kAxisDeadband);
    }


    private enum Action {
        Brake,
        OpenLoop,
        LinearPID
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
