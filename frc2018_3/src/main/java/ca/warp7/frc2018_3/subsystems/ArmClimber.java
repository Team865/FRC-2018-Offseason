package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.StateType;
import ca.warp7.frc.commons.wrapper.MotorGroup;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import static ca.warp7.frc.commons.Functions.limit;
import static ca.warp7.frc2018_3.Constants.kArmPin;

/**
 * Based on climber subsystem (for testing purposes)
 */

public class ArmClimber implements ISubsystem {

    private static final double kAbsoluteMaxOutputPower = 1.0;

    @InputStateField
    private final InputState mInputState = new InputState();
    @CurrentStateField
    private final CurrentState mCurrentState = new CurrentState();

    private MotorGroup mArmMotor;

    public void setSpeed(double speed) {
        mInputState.demandedSpeed = speed;
    }

    @Override
    public void onConstruct() {
        mArmMotor = new MotorGroup(WPI_VictorSPX.class, kArmPin);
    }

    @Override
    public void onDisabled() {
        mInputState.demandedSpeed = 0;
    }

    @Override
    public void onOutput() {
        mArmMotor.set(limit(mCurrentState.speed, kAbsoluteMaxOutputPower));
    }

    @Override
    public void onUpdateState() {
        mCurrentState.speed += (mInputState.demandedSpeed - mCurrentState.speed) / 10;
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
