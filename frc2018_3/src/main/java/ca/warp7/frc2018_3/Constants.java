package ca.warp7.frc2018_3;

import ca.warp7.frc.commons.Pins;

import static ca.warp7.frc.commons.Pins.pin;
import static ca.warp7.frc.commons.Pins.pins;

public final class Constants {

    public static final int kDriveLeftMaster = 3;
    public static final int kDriveLeftSlave = 4;
    public static final int kDriveRightMaster = 5;
    public static final int kDriveRightSlave = 1;

    public static final int kDriveLeftEncoderA = 4;
    public static final int kDriveLeftEncoderB = 5;
    public static final int kDriveRightEncoderA = 0;
    public static final int kDriveRightEncoderB = 1;

    public static final int kPneumaticsCompressorPin = 0;

    public static final int kIntakePistonSolenoidPin = 0;
    public static final int kDriveShifterSolenoidPin = 1;
    public static final int kGrapplingHookSolenoidPin = 2;

    public static final Pins kIntakeLeftPin = pin(7);
    public static final Pins kIntakeRightPin = pin(2);

    public static final Pins kClimberPins = pins(9, 0);

    public static final Pins kActualClimberPins = pin(8);

    public static final double kLeftDriftOffset = 1.0;
    public static final double kRightDriftOffset = 1.0;
    public static final double kInchesPerTick = (6 * Math.PI) / 256;
    public static final double kPreDriftSpeedLimit = 0.98;
}