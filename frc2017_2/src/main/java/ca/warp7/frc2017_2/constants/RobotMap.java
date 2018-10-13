package ca.warp7.frc2017_2.constants;

import ca.warp7.frc.commons.state.PIDValues;
import ca.warp7.frc.commons.state.Pins;
import ca.warp7.frc2017_2.subsystems.Drive;
import ca.warp7.frc2017_2.subsystems.Pneumatics;

/**
 * Static variables that maps the robot and includes constants
 */

public final class RobotMap {

	@SuppressWarnings("WeakerAccess")
	public static final class Subsystems {
		public static final Drive drive = new Drive();
		public static final Pneumatics pneumatics = new Pneumatics();
		//public static final Shooter shooter = new Shooter();
	}

	public static final class RIO {
		public static Pins driveLeftPins;
		public static Pins driveRightPins;
		public static Pins driveLeftEncoderChannels;
		public static Pins driveRightEncoderChannels;
		public static Pins pneumaticsShifterSolenoidPin;
		public static Pins pneumaticsCompressorPin;

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
		public static double preDriftSpeedLimit;
	}
}