package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.DifferentialWheels;
import ca.warp7.frc.commons.PIDValues;
import ca.warp7.frc.commons.cheesy_drive.CheesyDrive;
import ca.warp7.frc.commons.cheesy_drive.ICheesyDriveInput;
import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.StateType;
import ca.warp7.frc.commons.wrapper.MotorGroup;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.stormbots.MiniPID;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

import static ca.warp7.frc.commons.Functions.constrainMinimum;
import static ca.warp7.frc.commons.Functions.limit;
import static ca.warp7.frc2018_3.Constants.*;
import static edu.wpi.first.wpilibj.CounterBase.EncodingType.k4X;

public class Drive implements ISubsystem {

    private static final double kAbsoluteMaxOutputPower = 1.0;
    private static final double kMaximumPIDPower = 0.7;
    private static final double kRampIntervalMultiplier = 0.5;
    private static final double kMinRampRate = 1.0E-04;
    private static final double kMinOutputPower = 1.0E-3;
    private static final double kMaxLinearRampRate = 1.0 / 5;
    private static final double kEpsilon = 1.0E-10;
    private static final double kVelocityMeasurePhase = 50.0;

    @InputStateField
    private final Input mInput = new Input();
    @CurrentStateField
    private final State mState = new State();

    private CheesyDrive mCheesyDrive;
    private double mLastMeasuredTimeStamp;
    private DifferentialWheels<MotorGroup> mMotors;
    private DifferentialWheels<Encoder> mEncoders;

    @Override
    public void onConstruct() {
        mCheesyDrive = new CheesyDrive((left, right) -> {
            mInput.demandedLeftPower = left;
            mInput.demandedRightPower = right;
        });

        MotorGroup leftMotors = new MotorGroup(WPI_VictorSPX.class, kDriveLeftPins);
        MotorGroup rightMotors = new MotorGroup(WPI_VictorSPX.class, kDriveRightPins);
        rightMotors.setInverted(true);

        Encoder leftEncoder = new Encoder(kDriveLeftEncoder.get(0), kDriveLeftEncoder.get(1), true, k4X);
        leftEncoder.setDistancePerPulse(kInchesPerTick);
        Encoder rightEncoder = new Encoder(kDriveRightEncoder.get(0), kDriveRightEncoder.get(1), false, k4X);
        rightEncoder.setDistancePerPulse(kInchesPerTick);

        mMotors = new DifferentialWheels<>(leftMotors, rightMotors);
        mEncoders = new DifferentialWheels<>(leftEncoder, rightEncoder);
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
        onZeroSensors();
    }

    @Override
    public synchronized void onMeasure() {
        double oldLeftDistance = mState.measuredLeftDistance;
        double oldRightDistance = mState.measuredRightDistance;
        mState.measuredLeftDistance = mEncoders.getLeft().getDistance() * -1;
        mState.measuredRightDistance = mEncoders.getRight().getDistance() * -1;
        double deltaLeftDistance = mState.measuredLeftDistance - oldLeftDistance;
        double deltaRightDistance = mState.measuredRightDistance - oldRightDistance;
        double now = Timer.getFPGATimestamp();
        mState.measuredDt = constrainMinimum(now - mLastMeasuredTimeStamp, kEpsilon);
        mLastMeasuredTimeStamp = now;
        if (mState.measuredDt != 0) {
            mState.measuredLeftVelocity += (deltaLeftDistance / mState.measuredDt) * (1 / kVelocityMeasurePhase);
            mState.measuredRightVelocity += (deltaRightDistance / mState.measuredDt) * (1 / kVelocityMeasurePhase);
        }
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
        mMotors.getLeft().set(limit(limitedLeft * kLeftDriftOffset, kAbsoluteMaxOutputPower));
        mMotors.getRight().set(limit(limitedRight * kRightDriftOffset, kAbsoluteMaxOutputPower));
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
                    Math.abs(leftSpeedDiff * kRampIntervalMultiplier)) * Math.signum(leftSpeedDiff), kMinRampRate);
            mState.rightPower += constrainMinimum(Math.min(kMaxLinearRampRate,
                    Math.abs(rightSpeedDiff * kRampIntervalMultiplier)) * Math.signum(rightSpeedDiff), kMinRampRate);

            mState.leftPower = constrainMinimum(mState.leftPower, kMinOutputPower);
            mState.rightPower = constrainMinimum(mState.rightPower, kMinOutputPower);

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
        double measuredDt;
        double measuredLeftVelocity;
        double measuredRightVelocity;
        final MiniPID leftMiniPID = new MiniPID(0, 0, 0, 0);
        final MiniPID rightMiniPID = new MiniPID(0, 0, 0, 0);
    }
}