package ca.warp7.frc2018;

import ca.warp7.frc.construct.Pins;
import ca.warp7.frc2018.subsystems.Compressors;
import ca.warp7.frc2018.subsystems.Drive;

/**
 * Static variables that maps the robot and includes constants
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Mapping {
	public static class Subsystems {
		public static Drive drive;
		public static Compressors compressors;
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