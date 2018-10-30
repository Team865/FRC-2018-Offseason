package ca.warp7.frc2018_2;


import ca.warp7.frc2018_2.auto.AutonomousBase;
import ca.warp7.frc2018_2.controls.ControlsBase;
import ca.warp7.frc2018_2.controls.DualRemote;
import ca.warp7.frc2018_2.subsystems.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static ca.warp7.frc2018_2.Constants.COMPRESSOR_PIN;


public class Robot extends IterativeRobot {
    public static Limelight limelight;

    public static Drive drive;
    public static Climber climber;
    public static Lift lift;
    public static Intake intake;

    public static AutonomousBase auto;
    private static ControlsBase controls;

    //shutup >:(
    //no u
    public static Compressor compressor;

    public static Navx navx;

    private static DriverStation driverStation;

    private AnalogInput a0;
    private AnalogInput a1;
    private AnalogInput a2;
    private AnalogInput a3;

    public void robotInit() {
        System.out.println("Hello me is robit");

        limelight = new Limelight();
        navx = new Navx();
        drive = new Drive();
        intake = new Intake();
        lift = new Lift();
        climber = new Climber();

        //shutup >:(
        compressor = new Compressor(COMPRESSOR_PIN);

        driverStation = DriverStation.getInstance();
        //navx.startUpdateDisplacement(60);

        a0 = new AnalogInput(0);
        a1 = new AnalogInput(1);
        a2 = new AnalogInput(2);
        a3 = new AnalogInput(3);

		/*RTS liftRTS = new RTS("liftRTS", 60);
		Runnable liftPer = () -> lift.periodic();
		liftRTS.addTask(liftPer);
		liftRTS.start();*/
    }

    private int pin = 0;

    public void autonomousInit() {
        System.out.println("robot.java autonomousInit()");
        lift.zeroEncoder();
        lift.setLoc(0);
        auto = new AutonomousBase();
        pin = autoSelector();
        drive.resetDistance();
        navx.resetAngle();
        lift.disableSpeedLimit = true;
        drive.setGear(false);
        if (limelight.getCamMode() == 0)
            limelight.switchCamera();
    }

    public void autonomousPeriodic() {
        lift.periodic();
        String gameData = driverStation.getGameSpecificMessage();
        auto.autonomousPeriodic(gameData, pin);//pin
    }

    public void teleopInit() {
        //if (auto.trajA.notifierRunning)
        //	auto.trajA.notifier.stop();
        lift.disableSpeedLimit = false;
        lift.overrideIntake = false;
        drive.setSpeedLimit(1);
        drive.tankDrive(0, 0);
        //navx.startUpdateDisplacement(60);
        //navx.resetDisplacement();
        compressor.setClosedLoopControl(true);
        drive.resetDistance();
        if (limelight.getCamMode() == 0)
            limelight.switchCamera();
    }

    public void teleopPeriodic() {

        controls = new DualRemote();
        double a = 0;
        while (isOperatorControl() && isEnabled()) {
            controls.periodic();
            limelight.mutiPipeline();
            intake.periodic();
            lift.periodic();
            double b = lift.getEncoderVal();
            if (a < b)
                a = b;
            SmartDashboard.putNumber("pipeline id", limelight.getPipeline());
            SmartDashboard.putBoolean("inake hasCube", intake.hasCube());
            //drive.periodic();

            SmartDashboard.putNumber("0", a0.getAverageVoltage());
            SmartDashboard.putNumber("1", a1.getAverageVoltage());
            SmartDashboard.putNumber("2", a2.getAverageVoltage());
            SmartDashboard.putNumber("3", a3.getAverageVoltage());
            System.out.println("Lift:" + a);
            SmartDashboard.putNumber("Lift", a);
            SmartDashboard.putNumber("Lift raw", b);
            SmartDashboard.putNumber("Drive Right Dist", drive.getRightDistance());
            SmartDashboard.putNumber("Drive Left Dist", drive.getLeftDistance());
            SmartDashboard.putNumber("pitch", navx.getPitch());

            Timer.delay(0.005);
        }
    }

    public void disabledInit() {
        //if (navx.getDisplacementUpdater().isRunning())
        //navx.stopUpdateDisplacement();
    }

    public void testInit() {
        //makeRobitDriveStraight();
        //limelightthing();
        calibrateLift();
    }

    public void testPeriodic() {

    }

    public void calibrateLift() {
        double speed = 0;
        while (lift.isBottom()) {
            lift.setSpeed(speed);
            speed -= 0.005;
            Timer.delay(0.5);
        }
        lift.setSpeed(0);
        System.out.println(speed);
        SmartDashboard.putNumber("Lift power", speed);
    }

    public void makeRobitDriveStraight() {
        double i = 0;
        while (i < 1) {
            Timer.delay(0.05);
            drive.tankDrive(i, i);
            i += 0.01;
        }
        drive.resetDistance();
        Timer.delay(5);
        while (i > 0) {
            Timer.delay(0.05);
            drive.tankDrive(i, i);
            i -= 0.01;
        }
        drive.tankDrive(0, 0);
        Timer.delay(2);
        double left = drive.getLeftDistance();
        double right = drive.getRightDistance();
        double offset = left / right;
        if (offset >= 1) {
            SmartDashboard.putNumber("LeftOffset", right / left);
            SmartDashboard.putNumber("RightOffset", 1);
        } else {
            SmartDashboard.putNumber("LeftOffset", 1);
            SmartDashboard.putNumber("RightOffset", offset);
        }

    }

    private void limelightthing() {
        while (isOperatorControl() && isEnabled()) {
            SmartDashboard.putNumber("limelight area", limelight.getArea());
        }
    }

    private int autoSelector() {
        double voltage = 0;
        int number = 0;
        if (a0.getAverageVoltage() > voltage) {
            number = 3;
            voltage = a0.getAverageVoltage();
        }
        if (a1.getAverageVoltage() > voltage) {
            number = 0;
            voltage = a1.getAverageVoltage();
        }
        if (a2.getAverageVoltage() > voltage) {
            number = 2;
            voltage = a2.getAverageVoltage();
        }
        if (a3.getAverageVoltage() > voltage) {
            number = 1;
            voltage = a3.getAverageVoltage();
        }
        System.out.println("volt: " + voltage);
        return number;
    }
}

