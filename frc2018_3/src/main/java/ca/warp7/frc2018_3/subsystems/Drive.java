package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.PIDValues;
import ca.warp7.frc.commons.cheesy_drive.CheesyDrive;
import ca.warp7.frc.commons.cheesy_drive.ICheesyDriveInput;
import ca.warp7.frc.commons.core.Components;
import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.StateType;
import ca.warp7.frc.commons.wrapper.MotorGroup;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.stormbots.MiniPID;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;

import static ca.warp7.frc.commons.Functions.constrainMinimum;
import static ca.warp7.frc.commons.Functions.limit;
import static ca.warp7.frc2018_3.Constants.*;

/**
 * Controls the motors turing the wheels in the drive train
 * and responsible for altering the robot's pose (location + heading)
 */

public class Drive implements ISubsystem {

    private static final double kAbsoluteMaxOutputPower = 1.0;
    private static final double kMaximumPIDPower = 0.7;
    private static final double kRampIntervalMultiplier = 0.5;
    private static final double kMinRampRate = 1.0E-04;
    private static final double kMinOutputPower = 1.0E-3;
    private static final double kMaxLinearRampRate = 1.0 / 5;

    @InputStateField
    private final InputState mInputState = new InputState();
    @CurrentStateField
    private final CurrentState mCurrentState = new CurrentState();

    private CheesyDrive mCheesyDrive;
    private MotorGroup mLeftMotorGroup;
    private MotorGroup mRightMotorGroup;
    private Encoder mLeftEncoder;
    private Encoder mRightEncoder;

    @Override
    public void onConstruct() {
        mCheesyDrive = new CheesyDrive((left, right) -> {
            mInputState.demandedLeftSpeed = left;
            mInputState.demandedRightSpeed = right;
        });

        mLeftMotorGroup = new MotorGroup(WPI_VictorSPX.class, kDriveLeftPins);
        mRightMotorGroup = new MotorGroup(WPI_VictorSPX.class, kDriveRightPins);
        mRightMotorGroup.setInverted(true);

        mLeftEncoder = Components.encoder(kDriveLeftEncoderChannels, true, EncodingType.k4X);
        mLeftEncoder.setDistancePerPulse(kInchesPerTick);

        mRightEncoder = Components.encoder(kDriveRightEncoderChannels, false, EncodingType.k4X);
        mRightEncoder.setDistancePerPulse(kInchesPerTick);
    }

    @Override
    public synchronized void onDisabled() {
        mInputState.demandedRightSpeed = 0.0;
        mInputState.demandedLeftSpeed = 0.0;
        mInputState.shouldBeginOpenLoop = false;
        mInputState.shouldBeginPIDLoop = false;
        mInputState.shouldReverse = false;
        mInputState.targetLeftDistance = 0;
        mInputState.targetRightDistance = 0;
        mInputState.leftPIDValues.reset();
        mInputState.rightPIDValues.reset();
        onZeroSensors();
    }

    @Override
    public synchronized void onMeasure() {
        mCurrentState.measuredLeftDistance = mLeftEncoder.getDistance() * -1;
        mCurrentState.measuredRightDistance = mRightEncoder.getDistance() * -1;
//        System.out.printf("%s, %s\n", mCurrentState.measuredDistance, mCurrentState.measuredRightDistance);
    }

    @Override
    public synchronized void onZeroSensors() {
        mCurrentState.measuredLeftDistance = 0.0;
        mCurrentState.measuredRightDistance = 0.0;
        mLeftEncoder.reset();
        mRightEncoder.reset();
    }

    @Override
    public synchronized void onOutput() {
        double limitedLeft = limit(mCurrentState.leftSpeed, kPreDriftSpeedLimit);
        double limitedRight = limit(mCurrentState.rightSpeed, kPreDriftSpeedLimit);
        mLeftMotorGroup.set(limit(limitedLeft * kLeftDriftOffset, kAbsoluteMaxOutputPower));
        mRightMotorGroup.set(limit(limitedRight * kRightDriftOffset, kAbsoluteMaxOutputPower));
    }

    @Override
    public synchronized void onUpdateState() {
        if (mInputState.shouldBeginOpenLoop) {
            if (!mCurrentState.isOpenLoop) {
                mCurrentState.isOpenLoop = true;
                mCurrentState.isPIDLoop = false;
            }
        } else if (mInputState.shouldBeginPIDLoop) {
            if (!mCurrentState.isPIDLoop) {
                mCurrentState.isOpenLoop = false;
                mCurrentState.isPIDLoop = true;
            }
        } else {
            mCurrentState.isPIDLoop = false;
            mCurrentState.isOpenLoop = false;
        }

        mCurrentState.isReversed = mInputState.shouldReverse;

        if (mCurrentState.isOpenLoop) {

            double demandedLeft = mInputState.demandedLeftSpeed * (mCurrentState.isReversed ? -1 : 1);
            double demandedRight = mInputState.demandedRightSpeed * (mCurrentState.isReversed ? -1 : 1);
            double leftSpeedDiff = demandedLeft - mCurrentState.leftSpeed;
            double rightSpeedDiff = demandedRight - mCurrentState.rightSpeed;

            // Apply a linear ramp constraint to the demanded speeds

            mCurrentState.leftSpeed += constrainMinimum(Math.min(kMaxLinearRampRate,
                    Math.abs(leftSpeedDiff * kRampIntervalMultiplier)) * Math.signum(leftSpeedDiff), kMinRampRate);
            mCurrentState.rightSpeed += constrainMinimum(Math.min(kMaxLinearRampRate,
                    Math.abs(rightSpeedDiff * kRampIntervalMultiplier)) * Math.signum(rightSpeedDiff), kMinRampRate);

            mCurrentState.leftSpeed = constrainMinimum(mCurrentState.leftSpeed, kMinOutputPower);
            mCurrentState.rightSpeed = constrainMinimum(mCurrentState.rightSpeed, kMinOutputPower);

            //For debugging
            //System.out.println(String.format("%.3f, %.3f", mCurrentState.leftSpeed, mCurrentState.rightSpeed));
            //System.out.println(String.format("%.3f, %.3f", mInputState.demandedLeftSpeed, mInputState.demandedRightSpeed));

        } else if (mCurrentState.isPIDLoop) {

            mInputState.leftPIDValues.copyTo(mCurrentState.leftMiniPID);
            mInputState.rightPIDValues.copyTo(mCurrentState.rightMiniPID);
            mCurrentState.leftMiniPID.setSetpoint(mInputState.targetLeftDistance);
            mCurrentState.rightMiniPID.setSetpoint(mInputState.targetRightDistance);
            mCurrentState.leftSpeed = mCurrentState.leftMiniPID.getOutput(mCurrentState.measuredLeftDistance);
            mCurrentState.rightSpeed = mCurrentState.rightMiniPID.getOutput(mCurrentState.measuredRightDistance);

        } else {

            mCurrentState.leftSpeed = 0;
            mCurrentState.rightSpeed = 0;
        }
    }

    @Override
    public synchronized void onReportState() {
        Robot.report(this, StateType.SUBSYSTEM_INPUT, mInputState);
        Robot.report(this, StateType.SUBSYSTEM_STATE, mCurrentState);
    }

    @InputModifier
    public synchronized void cheesyDrive(ICheesyDriveInput driver) {
        mInputState.shouldBeginOpenLoop = true;
        mInputState.shouldBeginPIDLoop = false;
        mCheesyDrive.setInputsFromControls(driver);
        mCheesyDrive.calculateFeed();
    }

    @InputModifier
    public synchronized void openLoopDrive(double leftSpeedDemand, double rightSpeedDemand) {
        mInputState.shouldBeginOpenLoop = true;
        mInputState.shouldBeginPIDLoop = false;
        mInputState.demandedLeftSpeed = leftSpeedDemand;
        mInputState.demandedRightSpeed = rightSpeedDemand;
    }

    @InputModifier
    public synchronized void setPIDTargetDistance(PIDValues pidValues, double targetDistance) {
        mInputState.shouldBeginOpenLoop = false;
        mInputState.shouldBeginPIDLoop = true;
        pidValues.copyTo(mInputState.leftPIDValues);
        pidValues.copyTo(mInputState.rightPIDValues);
        mInputState.targetLeftDistance = targetDistance;
        mInputState.targetRightDistance = targetDistance;
        mCurrentState.leftMiniPID.setOutputRampRate(kMaxLinearRampRate);
        mCurrentState.rightMiniPID.setOutputRampRate(kMaxLinearRampRate);
        mCurrentState.leftMiniPID.setOutputLimits(kMaximumPIDPower);
        mCurrentState.rightMiniPID.setOutputLimits(kMaximumPIDPower);
    }

    @InputModifier
    public synchronized void setReversed(boolean reversed) {
        mInputState.shouldReverse = reversed;
    }

    public synchronized boolean isWithinDistanceRange(double distanceRange, double tolerance) {
        double average = (mCurrentState.measuredLeftDistance + mCurrentState.measuredRightDistance) / 2;
//        System.out.printf("average: %s\n", average);
        return Math.abs(distanceRange - average) < Math.abs(tolerance);
    }

    public boolean shouldBeginOpenLoop() {
        return mInputState.shouldBeginOpenLoop;
    }

    public boolean shouldBeginPIDLoop() {
        return mInputState.shouldBeginPIDLoop;
    }

    static class InputState {
        boolean shouldReverse;
        boolean shouldBeginOpenLoop;
        boolean shouldBeginPIDLoop;
        double demandedLeftSpeed;
        double demandedRightSpeed;
        double targetLeftDistance;
        double targetRightDistance;
        final PIDValues leftPIDValues = new PIDValues(0, 0, 0, 0);
        final PIDValues rightPIDValues = new PIDValues(0, 0, 0, 0);
    }

    static class CurrentState {
        boolean isReversed;
        boolean isOpenLoop;
        boolean isPIDLoop;
        double leftSpeed;
        double rightSpeed;
        double measuredLeftDistance;
        double measuredRightDistance;
        final MiniPID leftMiniPID = new MiniPID(0, 0, 0, 0);
        final MiniPID rightMiniPID = new MiniPID(0, 0, 0, 0);
    }
}