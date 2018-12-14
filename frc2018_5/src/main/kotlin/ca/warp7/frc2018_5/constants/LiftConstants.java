package ca.warp7.frc2018_5.constants;

public class LiftConstants {
    public static final double kLiftHeight = 13000;

    public static final double kRampSpeed = 6;

    public static final boolean overrideIntake = false;
    public static final boolean disableSpeedLimit = false;

    public static final boolean kLiftShouldUseSlowFall = false;

    public static final double kLiftSlowFallTargetSpeed = -0.1;

    public static final double kLiftZeroDeadband = 0.1;
    public static final boolean kIsEncoderReversed = false;

    public static final double kLiftInputExponentialScaleValue = 1;
    // Set Points
    public static final double kSetPoint1 = 0.11;
    public static final double kSetPoint2 = 0.4;

    // PID values
    public static final double kLiftP = 6.5;
    public static final double kLiftI = 0;
    public static final double kLiftD = 20;

    public static final double kLiftMaxOutput = 1;
    public static final double kLiftMinOutput = -0.4;
}
