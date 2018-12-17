package ca.warp7.frc2018_2.controls;

import ca.warp7.frc2018_2.Constants;
import ca.warp7.frc2018_2.Robot;
import ca.warp7.frc2018_2.misc.Util;

import static ca.warp7.frc2018_2.controls.Control.DOWN;
import static ca.warp7.frc2018_2.controls.Control.PRESSED;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

public class DualRemote extends ControlsBase {

    private boolean intakeTracking = false;

    @Override
    public void periodic(){
        // TODO Discuss intake and outtake settings with Drive
        if (driver.getTrigger(kRight) == DOWN) {//intake
            intake.rampSpeed(0.75);
        } else if (driver.getTrigger(kLeft) == DOWN) {//outtake
            intake.rampSpeed(-0.5);
        } else if (driver.getDpad(0) == DOWN) {
            lift.overrideIntake = true;
            intake.rampSpeed(-0.1);
        } else if (driver.getDpad(90) == DOWN) {
            lift.overrideIntake = true;
            intake.rampSpeed(0.75);
        } else if (driver.getDpad(270) == DOWN) {
            lift.overrideIntake = true;
            intake.rampSpeed(-0.95);
        } else {
            lift.overrideIntake = false;
            intake.rampSpeed(0);
        }

        // Toggle intake tracking
        if (driver.getDpad(180) == PRESSED)
            intakeTracking = !intakeTracking;

        // Toggle Drive Train Reversing
        if (driver.getStickButton(kRight) == PRESSED)
            drive.setDrivetrainReversed(!drive.driveReversed());

        if (driver.getAButton() == PRESSED)
            intake.pistonToggle();

        // Toggle Limelight between camera and vision modes
        if (driver.getXButton() == PRESSED) {
            Robot.limelight.switchCamera();
            System.out.println("switching camera");
        }

        // Exchange setpoint
        if (operator.getXButton() == DOWN) {
            lift.setLoc(0.11);
        }

        // Switch setpoint
        if (operator.getTrigger(kRight) == DOWN) {
            lift.setLoc(0.4);
        }

        // PID lift control
        if (operator.getAButton() == DOWN) {
            double x = operator.getY((kLeft));
            double target;
            if (operator.getTrigger(kLeft) == DOWN) {
                double absY = Math.abs(x);
                target = Math.pow(absY, 0.2);
            }
            else if (operator.getBumper(kLeft) == DOWN){
                double initialSlope = 2;
                double slopeChangePoint = 0.25;
                double targetAtSlopeChange = slopeChangePoint * initialSlope;
                double finalSlope = (1 - targetAtSlopeChange)/(1 - slopeChangePoint);
                double yIntAtFinalSlope = 1 - finalSlope * 1; // b = y - mx
                if (x < slopeChangePoint){
                    target = x * initialSlope;
                }
                else{
                    target = initialSlope * x + finalSlope;
                }
            }
            else {
                target = x;
            }
            lift.setLoc(target);
        }

        if (operator.getYButton() == DOWN && !lift.isBottom()) {
            lift.setShouldSlowFall(true);
            lift.setLoc(0);
        } else {
            lift.setShouldSlowFall(false);
        }

        if (operator.getBButton() == DOWN)
            climber.setSpeed(operator.getY(kRight) * -1);

        double wristSpeed = Util.deadband(operator.getY(kRight));
        if (!wrist.actuationSpeedIsRamped)
            if (wristSpeed <= Constants.ACTUATION_MOTOR_SPEED_LIMIT)
                wrist.setActuationSpeed(wristSpeed / 2);
            else
                wrist.setActuationSpeed(Constants.ACTUATION_MOTOR_SPEED_LIMIT);
        else if (wristSpeed <= Constants.ACTUATION_MOTOR_SPEED_LIMIT)
            wrist.actuationRamp(wristSpeed / 2);
        else
            wrist.actuationRamp(Constants.ACTUATION_MOTOR_SPEED_LIMIT);

        if (driver.getYButton() == PRESSED){
            lift.disableSpeedLimit = !lift.disableSpeedLimit;
        }
        if (driver.getBButton() == DOWN) {
            climber.setSpeed(driver.getY(kLeft) * -1); // TODO ask Owen about this control structure. Did Owen actually ever raise the climber?
        }
        else {
            if (intakeTracking && lift.isBottom() && intake.getSpeed() > 0.4)
                drive.trackCube(driver.getY(kLeft), 4);
            else
                drive.cheesyDrive(-driver.getX(kRight), driver.getY(kLeft), driver.getBumper(kLeft) == DOWN, false, driver.getBumper(kRight) != DOWN);
        }
    }
}
