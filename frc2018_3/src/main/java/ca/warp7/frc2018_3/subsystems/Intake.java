package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.StateType;
import ca.warp7.frc.commons.wrapper.MotorGroup;
import ca.warp7.frc2018_3.sensors.LimelightPhotoSensor;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Solenoid;

import static ca.warp7.frc.commons.Functions.limit;
import static ca.warp7.frc2018_3.Components.limelight;
import static ca.warp7.frc2018_3.Constants.*;

public class Intake implements ISubsystem {

    private static final double kAbsoluteMaxOutputPower = 1.0;

    public static final double kIntakePower = 0.75;
    public static final double kFastOuttakePower = -0.95;
    public static final double kSlowOuttakePower = -0.65;

    @InputField
    private final InputState mInputState = new InputState();
    @StateField
    private final CurrentState mCurrentState = new CurrentState();

    private MotorGroup mIntakeMotorRight;
    private MotorGroup mIntakeMotorLeft;
    private Solenoid mIntakePistons;
    private LimelightPhotoSensor mPhotoSensor;

    @Override
    public void onConstruct() {
        mIntakeMotorLeft = new MotorGroup(WPI_VictorSPX.class, kIntakeLeftPin);
        mIntakeMotorRight = new MotorGroup(WPI_VictorSPX.class, kIntakeRightPin);
        mIntakeMotorRight.setInverted(true);
        mIntakePistons = new Solenoid(kIntakePistonSolenoidPin);
        mPhotoSensor = new LimelightPhotoSensor(limelight, 1);
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
        Robot.report(this, StateType.COMPONENT_INPUT, mInputState);
        Robot.report(this, StateType.COMPONENT_STATE, mCurrentState);
    }

    public void setSpeed(double speed) {
        mInputState.demandedLeftSpeed = speed;
        mInputState.demandedRightSpeed = speed;
    }

    public double getSpeed() {
        return mIntakeMotorRight.get();
    }

    public void togglePiston() {
        mIntakePistons.set(!mIntakePistons.get());
    }

    public boolean hasCube() {
        return mPhotoSensor.isTriggered();
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
