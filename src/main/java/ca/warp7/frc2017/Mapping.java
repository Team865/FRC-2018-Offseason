package ca.warp7.frc2017;

import ca.warp7.frc.Robot.Pins;
import ca.warp7.frc2017.subsystems.Drive;
import ca.warp7.frc2017.subsystems.Pneumatics;
import ca.warp7.frc2017.subsystems.Shooter;

/**
 * Static variables that maps the robot and includes constants
 */
public final class Mapping {
	static final class Subsystems {
		static Drive drive;
		static Pneumatics compressor;
		static Shooter shooter;
	}

	public static final class RIO {
		public static Pins driveLeftPins;
		public static Pins driveRightPins;
		public static Pins driveLeftEncoderChannels;
		public static Pins driveRightEncoderChannels;
		public static Pins driveShifterPins;
		public static Pins compressorPin;

		public static Pins hopperSpinPins;
		public static Pins towerSpinPins;
		public static Pins intakeSpinPins;
		public static Pins photoSensorPin;

		public static Pins shooterMaster;
		public static Pins shooterSlave;
	}

	public static final class DriveConstants {
		public static double leftDriftOffset;
		public static double rightDriftOffset;
		public static double inchesPerTick;
		public static double speedLimit;
	}
}