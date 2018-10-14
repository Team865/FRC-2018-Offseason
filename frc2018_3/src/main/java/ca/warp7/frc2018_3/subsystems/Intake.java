package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.wpi_wrapper.MotorGroup;
import ca.warp7.frc2018_3.sensors.Limelight;
import ca.warp7.frc2018_3.sensors.LimelightPhotoSensor;
import ca.warp7.frc2018_3.constants.RobotMap;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Solenoid;

public class Intake {
    private MotorGroup intakeMotorRight;
    private MotorGroup intakeMotorLeft;
    private Solenoid intakePistons;
    private LimelightPhotoSensor photoSensor;

    public Intake() {
        intakeMotorLeft = new MotorGroup(WPI_VictorSPX.class, RobotMap.RIO.intakeLeftPin);
        intakeMotorRight = new MotorGroup(WPI_VictorSPX.class, RobotMap.RIO.intakeRightPin);
        intakeMotorRight.setInverted(true);
        intakePistons = new Solenoid(RobotMap.RIO.intakePiston.first());
        photoSensor = new LimelightPhotoSensor(Limelight.getInstance(), 1);
        //pistonToggle();
    }

    private double ramp = 0;
    public void rampSpeed(double speed) {
        // Ramp to prevent brown outs
        ramp += (speed - ramp) / 6.0;
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
        return photoSensor.isTriggered();
    }
}
