package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.StateType;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import static ca.warp7.frc.commons.Functions.limit;
import static ca.warp7.frc2018_3.Constants.kArmLiftPinA;
import static ca.warp7.frc2018_3.Constants.kArmLiftPinB;

public class ArmLift implements ISubsystem {

    private static final double kAbsoluteMaxOutputPower = 0.8;

    @InputField
    private final InputState mInputState = new InputState();
    @StateField
    private final CurrentState mCurrentState = new CurrentState();

    private SpeedControllerGroup mArmLiftMotors;

    public void setSpeed(double speed) {
        mInputState.demandedSpeed = speed;
    }

    @Override
    public void onConstruct() {
        mArmLiftMotors = new SpeedControllerGroup(
                new WPI_VictorSPX(kArmLiftPinA), new WPI_VictorSPX(kArmLiftPinB));
    }

    @Override
    public void onDisabled() {
        mInputState.demandedSpeed = 0;
    }

    @Override
    public void onOutput() {
        mArmLiftMotors.set(limit(mCurrentState.speed, kAbsoluteMaxOutputPower));
    }

    @Override
    public void onUpdateState() {
        mCurrentState.speed += (mInputState.demandedSpeed - mCurrentState.speed) / 6.0;
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
