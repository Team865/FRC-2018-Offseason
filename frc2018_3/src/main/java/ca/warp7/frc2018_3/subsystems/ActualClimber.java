package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.StateType;
import ca.warp7.frc.commons.wpi_wrapper.MotorGroup;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import static ca.warp7.frc.commons.core.Functions.limit;
import static ca.warp7.frc2018_3.Constants.kActualClimberPins;

public class ActualClimber implements ISubsystem {

    private static final double kAbsoluteMaxOutputPower = 1.0;

    @ISubsystem.InputStateField
    private final InputState mInputState = new InputState();
    @ISubsystem.CurrentStateField
    private final CurrentState mCurrentState = new CurrentState();

    private MotorGroup mClimberMotors;

    public void setSpeed(double speed) {
        mInputState.demandedSpeed = speed;
    }

    @Override
    public void onConstruct() {
        mClimberMotors = new MotorGroup(WPI_VictorSPX.class, kActualClimberPins);
    }

    @Override
    public void onDisabled() {
        mInputState.demandedSpeed = 0;
    }

    @Override
    public void onOutput() {
        mClimberMotors.set(limit(mCurrentState.speed, kAbsoluteMaxOutputPower));
    }

    @Override
    public void onUpdateState() {
        mCurrentState.speed += (mInputState.demandedSpeed - mCurrentState.speed) / 6.0;
    }

    @Override
    public void onReportState() {
        Robot.reportState(this, StateType.REFLECT_STATE_INPUT, mInputState);
        Robot.reportState(this, StateType.REFLECT_STATE_CURRENT, mCurrentState);
    }

    static class InputState {
        double demandedSpeed;
    }

    static class CurrentState {
        double speed;
    }
}
