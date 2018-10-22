package ca.warp7.frc2018_3;

import ca.warp7.frc.commons.Pins;

import static ca.warp7.frc.commons.Pins.*;

public final class Constants {

    public static final Pins kDriveLeftPins = pins(3, 4);
    public static final Pins kDriveRightPins = pins(5, 1);
    public static final Pins kDriveLeftEncoderChannels = channels(4, 5);
    public static final Pins kDriveRightEncoderChannels = channels(0, 1);

    public static final Pins kPneumaticsShifterSolenoidPin = pin(1);
    public static final Pins kPneumaticsCompressorPin = pin(0);

    public static final Pins kGrapplingHookSolenoidPin = pin(2);

    public static final Pins kIntakeLeftPin = pin(7);
    public static final Pins kIntakeRightPin = pin(2);
    public static final Pins kIntakePistonSolenoidPin = pin(0);

    public static final Pins kClimberPins = pins(9, 0);

    public static final Pins kActualClimberPins = pin(8);

    public static final Pins kArmPin = pin(6);
    public static final Pins kArmEncoderChannel = channels(6, 7);

    public static final double kLeftDriftOffset = 1.0;
    public static final double kRightDriftOffset = 1.0;
    public static final double kInchesPerTick = (6 * Math.PI) / 256;
    public static final double kArmInchesPerTick = (6 * Math.PI) / 256;
    public static final double kPreDriftSpeedLimit = 0.98;
}