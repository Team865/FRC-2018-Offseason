package ca.warp7.frc2018_brian.subsystems;

import ca.warp7.frc2018_brian.Robot;
import ca.warp7.frc2018_brian.misc.LimelightPhotosensor;
import ca.warp7.frc2018_brian.misc.MotorGroup;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Solenoid;

import static ca.warp7.frc2018_brian.Constants.*;

public class Intake {
    private MotorGroup intakeMotorRight;
    private MotorGroup intakeMotorLeft;
    private Solenoid intakePistons;
    private LimelightPhotosensor photosensor;
    private Lift lift = Robot.lift;

    public Intake() {
        intakeMotorLeft = new MotorGroup(INTAKE_MOTOR_LEFT_IDS, WPI_VictorSPX.class);
        intakeMotorRight = new MotorGroup(INTAKE_MOTOR_RIGHT_IDS, WPI_VictorSPX.class);
        intakeMotorRight.setInverted(true);
        intakePistons = new Solenoid(INTAKE_PISTONS);
        photosensor = new LimelightPhotosensor(Robot.limelight, 1);
        //pistonToggle();
    }

    private double ramp = 0;
    private final double rampSpeed = 6;

    public void rampSpeed(double speed) {
        // Ramp to prevent brown outs
        ramp += (speed - ramp) / rampSpeed;
        intakeMotorLeft.set(ramp);
        intakeMotorRight.set(ramp);
    }

    public void setSpeed(double speed) {
        intakeMotorLeft.set(speed);
        intakeMotorRight.set(speed);
    }

    public double getSpeed() {
        return intakeMotorRight.get();
    }

    public void pistonToggle() {
        intakePistons.set(!intakePistons.get());
    }

    public boolean hasCube() {
        return photosensor.isTriggered();
    }

    public void periodic() {
        //if (lift.isBottom())
        //	photosensor.update();
    }
}
