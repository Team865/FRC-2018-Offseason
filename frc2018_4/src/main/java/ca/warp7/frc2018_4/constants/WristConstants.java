package ca.warp7.frc2018_4.constants;

public class WristConstants {
    public static final double kAbsoluteMaxOutputPower = 1;
    public static final double kRandomDiffDivision = 6;
    public static final boolean kWristOverridesSuperstructure = true;
    public static final double kMaxOutputPower = 0.5;
    public static final double kWristSlowFallTargetSpeed = 0.1;
    public static final double kBagMotorGenericTheoreticalApproximateMaxSpeed = 13180; //TODO Put this in commons?
    public static final double kReduction = 100 / 1;
    public static final double kTheoreticalMaxRPM = kBagMotorGenericTheoreticalApproximateMaxSpeed / kReduction;
    public static final boolean kIsWristMotorReversed = false; //TODO verify this
}
