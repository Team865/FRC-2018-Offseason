package ca.warp7.frc_2017_v2.mapping;

import ca.warp7.frc.math.PIDValues;

import static ca.warp7.frc.core.RobotUtils.*;
import static ca.warp7.frc_2017_v2.mapping.Mapping.DriveConstants.*;
import static ca.warp7.frc_2017_v2.mapping.Mapping.PID.uniformDrivePID;
import static ca.warp7.frc_2017_v2.mapping.Mapping.RIO.*;

public class MappingDefault {
	public static void configure() {
		// Pins
		compressorPin = pin(0);

		driveLeftPins = pins(2, 3);
		driveLeftPins = pins(2, 3);
		driveRightPins = pins(0, 1);
		driveLeftEncoderChannels = channels(0, 1);
		driveRightEncoderChannels = channels(2, 3);
		driveShifterPins = pin(5);

		hopperSpinPins = pin(7);
		towerSpinPins = pin(6);
		intakeSpinPins = pin(8);
		photoSensorPin = pin(9);
		shooterSlave = pin(0);
		shooterMaster = pin(1);

		// Drive constants
		inchesPerTick = (4 * Math.PI) / 1024;
		leftDriftOffset = 1.0;
		rightDriftOffset = 1.0;
		speedLimit = 0.99;

		// PID
		uniformDrivePID = new PIDValues(0, 0, 0);
	}
}
