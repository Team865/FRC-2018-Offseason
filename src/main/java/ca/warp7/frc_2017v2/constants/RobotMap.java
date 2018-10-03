package ca.warp7.frc_2017v2.constants;

import ca.warp7.frc.values.PIDValues;
import ca.warp7.frc.values.Pins;
import ca.warp7.frc_2017v2.subsystems.drive.Drive;
import ca.warp7.frc_2017v2.subsystems.pneumatics.Pneumatics;
import ca.warp7.frc_2017v2.subsystems.shooter.Shooter;

/**
 * Static variables that maps the robot and includes constants
 */
public final class RobotMap {

	@SuppressWarnings("WeakerAccess")
	public static final class Subsystems {
		public static final Drive drive = new Drive();
		public static final Pneumatics pneumatics = new Pneumatics();
		public static final Shooter shooter = new Shooter();
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

	public static final class PID {
		public static PIDValues uniformDrivePID;
	}

	public static final class DriveConstants {
		public static double leftDriftOffset;
		public static double rightDriftOffset;
		public static double inchesPerTick;
		public static double speedLimit;
	}
}