package ca.warp7.frc2018_4.subsysterms;

import ca.warp7.frc.core.ISubsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import static ca.warp7.frc2018_4.constants.Pins.kClimberPin;

public class Wrist implements ISubsystem {
    private SpeedControllerGroup mWristMotorGroup;
    public InputState mInputState = new InputState();
    public State mState = new State();
    @Override
    public void onConstruct() {
        mClimbMotorGroup = new SpeedControllerGroup(new WPI_VictorSPX(kClimberPin));
    }

    @Override
    public void onDisabled() {

    }

    @Override
    public void onAutonomousInit() {

    }

    @Override
    public void onTeleopInit() {

    }

    @Override
    public void onMeasure() {

    }

    @Override
    public void onZeroSensors() {

    }

    @Override
    public void onOutput() {

    }

    @Override
    public void onUpdateState() {

    }

    @Override
    public void onReportState() {

    }

    public class InputState{

        double mWristAngularVelocity;
    }

    public class State{
        double mWristAngularVelocity;
    }
}
