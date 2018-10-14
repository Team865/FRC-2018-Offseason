package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.wpi_wrapper.MotorGroup;
import ca.warp7.frc2018_3.constants.RobotMap;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class Climber {

    private MotorGroup ClimberMotors;

    public Climber() {
        ClimberMotors = new MotorGroup(WPI_VictorSPX.class, RobotMap.RIO.climberPins);
    }

    private double ramp = 0;

    public void setSpeed(double speed) {
        ramp += (speed - ramp) / 6.0;
        ClimberMotors.set(ramp);
    }
}
