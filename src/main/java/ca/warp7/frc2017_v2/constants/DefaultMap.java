package ca.warp7.frc2017_v2.constants;

import ca.warp7.frc.values.PIDValues;
import ca.warp7.frc.values.Pins;

import static ca.warp7.frc2017_v2.constants.RobotMap.DriveConstants.*;
import static ca.warp7.frc2017_v2.constants.RobotMap.PID.uniformDrivePID;
import static ca.warp7.frc2017_v2.constants.RobotMap.RIO.*;

public class DefaultMap {
	public static void configure() {

		// Pins
		pneumaticsCompressorPin = Pins.pin(0);

		driveLeftPins = Pins.pins(2, 3);
		driveLeftPins = Pins.pins(2, 3);
		driveRightPins = Pins.pins(0, 1);
		driveLeftEncoderChannels = Pins.channels(0, 1);
		driveRightEncoderChannels = Pins.channels(2, 3);
		pneumaticsShifterSolenoidPin = Pins.pin(5);

		hopperSpinPins = Pins.pin(7);
		towerSpinPins = Pins.pin(6);
		intakeSpinPins = Pins.pin(8);
		photoSensorPin = Pins.pin(9);
		shooterSlave = Pins.pin(0);
		shooterMaster = Pins.pin(1);

		// Drive constants
		driveInchesPerTick = (4 * Math.PI) / 1024;
		leftDriftOffset = 1.0;
		rightDriftOffset = 1.0;
		preDriftSpeedLimit = 0.98;

		// PID
		uniformDrivePID = new PIDValues(0, 0, 0);
	}
}
