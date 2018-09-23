package ca.warp7.frc2017;

import ca.warp7.frc.robot.RobotCallback;
import ca.warp7.frc2017.Mapping.DriveConstants;
import ca.warp7.frc2017.Mapping.RIO;
import ca.warp7.frc2017.controls.IControlsInterface;
import ca.warp7.frc2017.controls.SingleRemote;

import static ca.warp7.frc2017.Mapping.Subsystems.compressor;
import static ca.warp7.frc2017.Mapping.Subsystems.drive;

public class Wanda extends RobotCallback<IControlsInterface> {

	@Override
	public void onInit() {
		System.out.print("Hello me is robit!");
		setMappingClass(Mapping.class);
		setController(new SingleRemote(0));
	}

	@Override
	public void onTeleopInit() {
		compressor.setClosedLoop(!getDriverStation().isFMSAttached());
	}

	@Override
	public void onTeleopPeriodic(IControlsInterface remote) {
		if (remote.compressorShouldSwitch()) compressor.toggleClosedLoop();
		drive.setReversed(remote.driveShouldReverse());
		drive.setShift(remote.driveShouldShift());
		drive.cheesyDrive(remote);
	}

	@Override
	public void onSetMapping() {
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
