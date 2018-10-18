package ca.warp7.frc2018_brian.subsystems;

import ca.warp7.frc2018_brian.misc.MotorGroup;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Encoder;

import static ca.warp7.frc2018_brian.Constants.CLIMBER_MOTORS_IDS;

public class Climber {

    private MotorGroup ClimberMotors;
    private Encoder climberPot; // --> string potentiometer
    private double setLocation;

    public Climber() {
        ClimberMotors = new MotorGroup(CLIMBER_MOTORS_IDS, WPI_VictorSPX.class);
    }

    private double ramp = 0;
    private final double rampSpeed = 6;

    public void setSpeed(double speed) {
        // Ramp to prevent brown outs
        ramp += (speed - ramp) / rampSpeed;
        ClimberMotors.set(ramp);
    }

    public void setLoc(double loc) {
        setLocation = loc;
    }

    private static final double tolerance = 0.10;
    private double scaledLift = 0;

    public void periodic() {
        if (false) //zero switch is active zero encoder
            scaledLift = 0;
        //else
        //	scaledLift = CLIMBER_HEIGHT/liftEncoder.getDistance();

        double speed = 1 + (setLocation - scaledLift - tolerance) / tolerance;
        //if (scaledLift >= setLocation-tolerance && scaledLift <= setLocation+tolerance)
        //setSpeed(speed);
    }
}
