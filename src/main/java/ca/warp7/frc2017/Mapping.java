package ca.warp7.frc2017;

import ca.warp7.frc.utils.Pins;
import ca.warp7.frc2017.subsystems.Compressor_;
import ca.warp7.frc2017.subsystems.Drive;

/**
 * Static variables that maps the robot and includes constants
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Mapping {
	public static class Subsystems {
		public static Drive drive;
		public static Compressor_ compressor;
	}

	public static class RIO {
		public static Pins driveLeftPins;
		public static Pins driveRightPins;
		public static Pins driveLeftEncoderChannels;
		public static Pins driveRightEncoderChannels;
		public static Pins driveShifterPins;
		public static Pins compressorPin;
	}

	public static class DriveConstants {
		public static double leftDriftOffset;
		public static double rightDriftOffset;
		public static double inchesPerTick;
		public static double speedLimit;
	}
}