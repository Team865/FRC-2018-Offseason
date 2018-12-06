package ca.warp7.frc2018_4.subsysterms;

import ca.warp7.frc.core.ISubsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import static ca.warp7.frc2018_4.constants.Pins.kLiftPin;

public class Lift implements ISubsystem {

    private SpeedControllerGroup mLiftMotorGroup;

    @Override
    public void onConstruct() {
        mLiftMotorGroup = new SpeedControllerGroup(new WPI_VictorSPX(kLiftPin));
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
        public double mDemandedHeight;
        public double mSuperstructureDemandedHeight;
    }

    private class State{
        double mHeight;
        double mSpeed;
    }
}
