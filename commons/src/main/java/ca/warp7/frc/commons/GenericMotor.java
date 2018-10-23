package ca.warp7.frc.commons;

import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.StateType;
import edu.wpi.first.wpilibj.SpeedController;

import static ca.warp7.frc.commons.Functions.limit;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class GenericMotor implements ISubsystem {

    @InputStateField
    private final InputState mInputState = new InputState();
    @CurrentStateField
    private final CurrentState mCurrentState = new CurrentState();

    private SpeedController mMotor;
    private double mMaxPower = 1.0;
    private double mRampSpeed = 6.0;

    public void setSpeed(double speed) {
        mInputState.demandedSpeed = speed;
    }

    public void setMaxPower(double maxPower) {
        mMaxPower = maxPower;
    }

    public void setRampSpeed(double rampSpeed) {
        mRampSpeed = rampSpeed;
    }

    public abstract SpeedController getSpeedController();

    @Override
    public void onConstruct() {
        mMotor = getSpeedController();
    }

    @Override
    public void onDisabled() {
        mInputState.demandedSpeed = 0;
    }

    @Override
    public void onOutput() {
        mMotor.set(limit(mCurrentState.speed, mMaxPower));
    }

    @Override
    public void onUpdateState() {
        double diff = mInputState.demandedSpeed - mCurrentState.speed;
        mCurrentState.speed += diff / mRampSpeed;
    }

    @Override
    public void onReportState() {
        Robot.report(this, StateType.COMPONENT_INPUT, mInputState);
        Robot.report(this, StateType.COMPONENT_STATE, mCurrentState);
    }

    static class InputState {
        double demandedSpeed;
    }

    static class CurrentState {
        double speed;
    }
}
