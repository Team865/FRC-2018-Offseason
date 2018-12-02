package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.core.ISubsystem;
import ca.warp7.frc.core.Robot;
import ca.warp7.frc.core.StateType;
import ca.warp7.frc2018_3.Constants;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;

import static ca.warp7.frc.Functions.limit;

public class Intake implements ISubsystem {

    private static final double kAbsoluteMaxOutputPower = 1.0;

    public static final double kIntakePower = 0.75;
    public static final double kFastOuttakePower = -0.95;
    public static final double kSlowOuttakePower = -0.65;

    @InputField
    private final InputState mInputState = new InputState();
    @StateField
    private final CurrentState mCurrentState = new CurrentState();

    private SpeedController mIntakeMotorRight;
    private SpeedController mIntakeMotorLeft;
    private Solenoid mIntakePistons;

    @Override
    public void onConstruct() {
        mIntakeMotorLeft = new WPI_VictorSPX(Constants.kIntakeLeftPin);
        mIntakeMotorRight = new WPI_VictorSPX(Constants.kIntakeRightPin);
        mIntakeMotorRight.setInverted(true);
        mIntakePistons = new Solenoid(Constants.kIntakePistonSolenoidPin);
    }

    @Override
    public void onDisabled() {
        mInputState.demandedLeftSpeed = 0;
        mInputState.demandedLeftSpeed = 0;
    }

    @Override
    public void onOutput() {
        mIntakeMotorLeft.set(limit(mCurrentState.leftSpeed, kAbsoluteMaxOutputPower));
        mIntakeMotorRight.set(limit(mCurrentState.rightSpeed, kAbsoluteMaxOutputPower));
    }

    @Override
    public void onUpdateState() {
        mCurrentState.leftSpeed += (mInputState.demandedLeftSpeed - mCurrentState.leftSpeed) / 6.0;
        mCurrentState.rightSpeed += (mInputState.demandedRightSpeed - mCurrentState.rightSpeed) / 6.0;
    }

    @Override
    public void onReportState() {
        Robot.report(this, StateType.ComponentInput, mInputState);
        Robot.report(this, StateType.ComponentState, mCurrentState);
    }

    public void setSpeed(double speed) {
        mInputState.demandedLeftSpeed = speed;
        mInputState.demandedRightSpeed = speed;
    }

    public void togglePiston() {
        mIntakePistons.set(!mIntakePistons.get());
    }

    static class InputState {
        double demandedLeftSpeed;
        double demandedRightSpeed;
    }

    static class CurrentState {
        double leftSpeed;
        double rightSpeed;
    }
}
