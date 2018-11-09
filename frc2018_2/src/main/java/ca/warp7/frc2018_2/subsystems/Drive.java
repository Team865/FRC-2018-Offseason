package ca.warp7.frc2018_2.subsystems;

import ca.warp7.frc2018_2.Robot;
import ca.warp7.frc2018_2.misc.DataPool;
import ca.warp7.frc2018_2.misc.MotorGroup;
import ca.warp7.frc2018_2.misc.Util;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static ca.warp7.frc2018_2.Constants.*;

public class Drive {

    private Navx navx = Robot.navx;
    private Limelight limelight = Robot.limelight;

    public static DataPool drivePool;

    private MotorGroup rightDrive;
    private MotorGroup leftDrive;
    private Solenoid shifter;

    private Encoder leftEncoder;
    private Encoder rightEncoder;

    private double quickstop_accumulator = 0;
    private double old_wheel = 0;
    private boolean driveReversed = true;

    private double speedLimit = 0.999;

    private double previous_displacement = 0;
    private double x_displacement = 0;
    private double y_displacement = 0;
    private double displacement_since_last_iteration;
    private double current_displacement;
    private double yaw_offsset;
    private double x_target;
    private double y_target;

    public Drive() {
        drivePool = new DataPool("Drive");

        // setup drive train motors
        rightDrive = new MotorGroup(RIGHT_DRIVE_MOTOR_IDS, WPI_VictorSPX.class);
        leftDrive = new MotorGroup(LEFT_DRIVE_MOTOR_IDS, WPI_VictorSPX.class);
        rightDrive.setInverted(true);

        // setup drive train gear shifter
        shifter = new Solenoid(DRIVE_SHIFTER_PORT);
        shifter.set(false);

        // setup drive train encoders
        leftEncoder = new Encoder(LEFT_DRIVE_ENCODER_A, LEFT_DRIVE_ENCODER_B, false, EncodingType.k4X);
        rightEncoder = new Encoder(RIGHT_DRIVE_ENCODER_A, RIGHT_DRIVE_ENCODER_B, false, EncodingType.k4X);
        leftEncoder.setDistancePerPulse(DRIVE_INCHES_PER_TICK);
        leftEncoder.setReverseDirection(true);
        rightEncoder.setReverseDirection(false);
        rightEncoder.setDistancePerPulse(DRIVE_INCHES_PER_TICK);
    }

    public void setGear(boolean gear) {
        if (shifter.get() != gear)
            shifter.set(gear);
    }

    public void update_x_y_predictions(){
        double angle = navx.getAbsYaw();
        current_displacement = (this.getLeftDistance() + this.getRightDistance())/ 2;
        displacement_since_last_iteration = current_displacement - previous_displacement;
        x_displacement += displacement_since_last_iteration * Math.cos(Math.toRadians(angle));
        y_displacement += displacement_since_last_iteration * Math.sin(Math.toRadians(angle));
        previous_displacement = current_displacement;
    }

    public double get_x_displacement(){
        return this.x_displacement;
    }

    public double get_y_displacement(){
        return this.y_displacement;
    }

    public void set_x_target(double target){
        x_target = target;
    }
    public void set_y_target(double target){
        y_target = target;
    }
    public void trackCube(double driveSpeed, double angleThresh) {
        double cubeAngleOffset = limelight.getXOffset();
        double turnSpeed = 1 - Math.abs(cubeAngleOffset / angleThresh);
        if (turnSpeed < 0)
            turnSpeed = 0;
        System.out.println(cubeAngleOffset + ":" + turnSpeed);
        if (cubeAngleOffset >= 0)// turn right
            tankDrive(driveSpeed, driveSpeed * turnSpeed);
        else { // turn left
            tankDrive(driveSpeed * turnSpeed, driveSpeed);
        }
    }

    public void tankDrive(double left, double right) {
        //double scaledBalance = autoBalance();
        left = limit(left, speedLimit);
        right = limit(right, speedLimit);
        leftDrive.set(left * LEFT_DRIFT_OFFSET);
        rightDrive.set(right * RIGHT_DRIFT_OFFSET);
    }

    public void cheesyDrive(double wheel, double throttle, boolean quickturn, boolean altQuickturn, boolean shift) {
        /*
         * Poofs! :param wheel: The speed that the robot should turn in the X
         * direction. 1 is right [-1.0..1.0] :param throttle: The speed that the
         * robot should drive in the Y direction. -1 is forward. [-1.0..1.0]
         * :param quickturn: If the robot should drive arcade-drive style
         */
        //System.out.println("rate= " + rightEncoder.getRate());

        throttle = Util.deadband(throttle);
        wheel = Util.deadband(wheel);
        if (driveReversed)
            wheel *= -1;
        double right_pwm;
        double left_pwm;
        double neg_inertia_scalar;
        double neg_inertia = wheel - old_wheel;
        old_wheel = wheel;
        wheel = Util.sinScale(wheel, 0.9f, 1, 0.9f);

        if (wheel * neg_inertia > 0) {
            neg_inertia_scalar = 2.5f;
        } else {
            if (Math.abs(wheel) > .65) {
                neg_inertia_scalar = 6;
            } else {
                neg_inertia_scalar = 4;
            }
        }

        double neg_inertia_accumulator = neg_inertia * neg_inertia_scalar;

        wheel += neg_inertia_accumulator;

        double over_power, angular_power;
        if (altQuickturn) {
            if (Math.abs(throttle) < 0.2) {
                double alpha = .1f;
                quickstop_accumulator = (1 - alpha) * quickstop_accumulator + alpha * limit(wheel, 1.0) * 5;
            }
            over_power = -wheel * .75;
            angular_power = -wheel * 1;
        } else if (quickturn) {
            if (Math.abs(throttle) < 0.2) {
                double alpha = .1f;
                quickstop_accumulator = (1 - alpha) * quickstop_accumulator + alpha * limit(wheel, 1.0) * 5;
            }
            over_power = 1;
            angular_power = -wheel * 1;
        } else {
            over_power = 0;
            double sensitivity = .9;
            angular_power = throttle * wheel * sensitivity - quickstop_accumulator;
            quickstop_accumulator = Util.wrap_accumulator(quickstop_accumulator);
        }


        if (shift) {
            if (!shifter.get())
                shifter.set(true);
        } else {
            if (shifter.get())
                shifter.set(false);
        }


        right_pwm = left_pwm = throttle;

        left_pwm += angular_power;
        right_pwm -= angular_power;

        if (left_pwm > 1) {
            right_pwm -= over_power * (left_pwm - 1);
            left_pwm = 1;
        } else if (right_pwm > 1) {
            left_pwm -= over_power * (right_pwm - 1);
            right_pwm = 1;
        } else if (left_pwm < -1) {
            right_pwm += over_power * (-1 - left_pwm);
            left_pwm = -1;
        } else if (right_pwm < -1) {
            left_pwm += over_power * (-1 - right_pwm);
            right_pwm = -1;
        }

        if (driveReversed) {
            left_pwm *= -1;
            right_pwm *= -1;
        }

        if (shifter.get()) { // if low gear
            //leftDrive.set(left_pwm);
            //rightDrive.set(right_pwm);
            moveRamped(left_pwm, right_pwm);
        } else {
            moveRamped(left_pwm, right_pwm);
        }
        SmartDashboard.putNumber("teleop leftV=", leftEncoder.getRate());
        SmartDashboard.putNumber("teleop rightV=", rightEncoder.getRate());
        SmartDashboard.putNumber("teleop left distance=", getLeftDistance());
        SmartDashboard.putNumber("teleop right distance=", getRightDistance());
    }

    private double limit(double wheel, double d) {
        return Util.limit(wheel, d);
    }

    public double leftRamp = 0.0;
    public double rightRamp = 0.0;
    public double a;
    public double b;

    public void moveRamped(double desiredLeft, double desiredRight) {
        double rampSpeed = 6;
        leftRamp += (desiredLeft - leftRamp) / rampSpeed;
        rightRamp += (desiredRight - rightRamp) / rampSpeed;
        tankDrive(leftRamp, rightRamp);
    }

    public void autoShift(boolean b) {
        if (shifter.get() != b)
            shifter.set(b);
    }

    public void periodic() {
        drivePool.logDouble("gyro_angle", getRotation());
        drivePool.logDouble("left_enc", rightEncoder.getDistance());
        drivePool.logDouble("right_enc", leftEncoder.getDistance());
    }

    public void setDrivetrainReversed(boolean reversed) {
        driveReversed = reversed;
    }

    public double leftVelocity() {
        return -leftEncoder.getRate();
    }

    public double rightVelocity() {
        return -rightEncoder.getRate();
    }

    public boolean driveReversed() {
        return driveReversed;
    }

    public double getRotation() {
        return navx.getAngle();
    }

    public double getLeftDistance() {
        return -leftEncoder.getDistance() * 2.54;
    }

    public double getRightDistance() {
        return -rightEncoder.getDistance() * 2.54;
    }

    public void resetDistance() {
        leftEncoder.reset();
        rightEncoder.reset();
    }


    private static final double kOonBalanceAngleThresholdDegrees = 5;
    private boolean autoBalance = true;

    public double autoBalance() {
        if (autoBalance) {
            double pitchAngleDegrees = navx.getPitch();
            double scaledPower = 1 + (0 - pitchAngleDegrees - kOonBalanceAngleThresholdDegrees) / kOonBalanceAngleThresholdDegrees;
            if (scaledPower > 2)
                scaledPower = 2;
            //return scaledPower;
        }

        return 0;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = limit(speedLimit, 0.999);
    }
}