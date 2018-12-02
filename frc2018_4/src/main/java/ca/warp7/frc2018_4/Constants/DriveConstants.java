package ca.warp7.frc2018_4.Constants;

public class DriveConstants {
    private static final int kConfigTimeout = 100;
    private static final int kVelocityQueueSize = 5;
    private static final double kAbsoluteMaxOutput = 1.0;
    private static final double kMaximumPIDPower = 0.7;
    private static final double kMaxLinearRamp = 1.0 / 20;
    private static final double kOutputPowerEpsilon = 1.0E-3;
    private static final double kTimeDeltaEpsilon = 1.0E-10;
    private static final double kLeftDriftOffset = 1.0;
    private static final double kRightDriftOffset = 1.0;
    private static final double kWheelRadius = 3.0;
    private static final double kWheelBaseRadius = 12.50;
    private static final double kEncoderTicksPerRevolution = 256.0;
    private static final double kPreDriftSpeedLimit = 1.0;
    private static final double kVoltageCompensationSaturation = 12.0;
    private static final double kNeutralDeadBand = 0.04;
    private static final double kOpenLoopRampNeutralToFull = 0.4;
    private static final double kClosedLoopRampNeutralToFull = 0.4;
    private static final double kAxisDeadband = 0.2;
}
