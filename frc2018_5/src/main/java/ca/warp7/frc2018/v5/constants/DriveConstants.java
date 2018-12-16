package ca.warp7.frc2018.v5.constants;

public class DriveConstants {
    public static final int kConfigTimeout = 100;
    public static final int kVelocityQueueSize = 5;
    public static final double kAbsoluteMaxOutput = 1.0;
    public static final double kMaximumPIDPower = 0.7;
    public static final double kMaxLinearRamp = 1.0 / 20;
    public static final double kOutputPowerEpsilon = 1.0E-3;
    public static final double kTimeDeltaEpsilon = 1.0E-10;
    public static final double kLeftDriftOffset = 1.0;
    public static final double kRightDriftOffset = 1.0;
    public static final double kWheelRadius = 3.0;
    public static final double kWheelBaseRadius = 12.50;
    public static final double kEncoderTicksPerRevolution = 256.0;
    public static final double kPreDriftSpeedLimit = 1.0;
    public static final double kVoltageCompensationSaturation = 12.0;
    public static final double kNeutralDeadBand = 0.04;
    public static final double kOpenLoopRampNeutralToFull = 0.4;
    public static final double kClosedLoopRampNeutralToFull = 0.4;
    public static final double kAxisDeadband = 0.2;
    public static final boolean kLeftSideInverted = true;
    public static final boolean kRightSideInverted = false;
    public static final boolean kLefttSideEncodeReversed = false;
    public static final boolean kRightSideEncodeReversed = true;
}
