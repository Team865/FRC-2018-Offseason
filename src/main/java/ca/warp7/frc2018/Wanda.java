package ca.warp7.frc2018;

import ca.warp7.frc.construct.RobotCallback;
import ca.warp7.frc2018.Mapping.DriveConstants;
import ca.warp7.frc2018.Mapping.RIO;
import ca.warp7.frc2018.controller.IController;
import ca.warp7.frc2018.controller.SingleRemote;

import static ca.warp7.frc2018.Mapping.Subsystems.compressors;
import static ca.warp7.frc2018.Mapping.Subsystems.drive;

public class Wanda extends RobotCallback<IController> {

	@Override
	public void onInit() {
		System.out.print("Hello me is robit!");
		setMappingClass(Mapping.class);
		setController(new SingleRemote(0));
	}

	@Override
	public void onTeleopInit() {
		compressors.setClosedLoop(!getDriverStation().isFMSAttached());
	}

	@Override
	public void onTeleopPeriodic(IController remote) {
		if (remote.compressorShouldSwitch()) compressors.toggleClosedLoop();
		drive.setReversed(remote.driveShouldReverse());
		drive.setShift(remote.driveShouldShift());
		drive.cheesyDrive(remote);
	}

	@Override
	public void onSetupMapping() {
		// Pins
		RIO.driveLeftPins = pins(2, 3);
		RIO.driveLeftPins = pins(2, 3);
		RIO.driveRightPins = pins(0, 1);
		RIO.driveLeftEncoderChannels = channels(0, 1);
		RIO.driveRightEncoderChannels = channels(2, 3);
		RIO.driveShifterPins = pin(1);
		RIO.compressorPin = pin(0);

		// Drive constants
		DriveConstants.inchesPerTick = (4 * Math.PI) / 1024;
		DriveConstants.leftDriftOffset = 1.0;
		DriveConstants.rightDriftOffset = 1.0;
		DriveConstants.speedLimit = 0.99;
	}
}
