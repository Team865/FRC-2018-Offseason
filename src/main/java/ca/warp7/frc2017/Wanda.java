package ca.warp7.frc2017;

import ca.warp7.frc.robot.RobotCallback;
import ca.warp7.frc2017.Mapping.DriveConstants;
import ca.warp7.frc2017.Mapping.RIO;
import ca.warp7.frc2017.controls.DualRemote;
import ca.warp7.frc2017.controls.IControlsInterface;

import static ca.warp7.frc2017.Mapping.Subsystems.*;

public class Wanda extends RobotCallback<IControlsInterface> {

	@Override
	public void onInit() {
		System.out.print("Hello me is robit!");
		setMappingClass(Mapping.class);
		setController(new DualRemote(0, 1));
	}

	@Override
	public void onTeleopInit() {
		compressor.setClosedLoop(!getDriverStation().isFMSAttached());
	}

	@Override
	public void onTeleopPeriodic(IControlsInterface control) {
		// Compressor
		if (control.compressorShouldSwitch()) compressor.toggleClosedLoop();
		// Drive
		drive.setReversed(control.driveShouldReverse());
		drive.setShift(control.driveShouldShift());
		drive.cheesyDrive(control);
		//Shooter
		switch (control.getShooterMode()) {
			case RPM_1:
				shooter.setRPM(4425);
			case RPM_2:
				shooter.setRPM(4450);
			case NONE:
				shooter.setRPM(0);
		}
		if (control.hopperShouldReverse()) {
			shooter.setHopperSpeed(-1.0);
			shooter.setIntakeSpeed(1.0);
			shooter.setTowerSpeed(0.0);
		} else if (control.shooterShouldShoot()) {
			shooter.setHopperSpeed(1.0);
			shooter.setIntakeSpeed(1.0);
			if (shooter.withinRange(25) && shooter.getSetPoint() > 0.0) {
				shooter.setTowerSpeed(1.0);
			} else if (shooter.getSensor()) {
				shooter.setTowerSpeed(0.5);
			} else {
				shooter.setTowerSpeed(0.0);
			}
		} else if (control.shooterShouldStop()) {
			shooter.setIntakeSpeed(0.0);
			shooter.setHopperSpeed(0.0);
			shooter.setTowerSpeed(0.0);
		}
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
