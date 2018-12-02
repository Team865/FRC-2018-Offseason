package ca.warp7.frc2018_4.Constants;

public class LiftConstants {
    public static final double kLiftHeight = 13000;

    public static final double kRampSpeed = 6;

    public static final boolean overrideIntake = false;
    public static final boolean disableSpeedLimit = false;

    private static final boolean shouldSlowFall = false;

    // PID values
    public static final double kLiftP = 6.5;
    public static final double kLiftI = 0;
    public static final double kLiftD = 20;
}
