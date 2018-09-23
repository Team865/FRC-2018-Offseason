package ca.warp7.frc2017.controls;

import ca.warp7.frc.drive.ICheesyDriveController;

public interface IControlsInterface extends ICheesyDriveController {
	enum ShooterMode {
		RPM_4425, RPM_4450, NONE
	}

	boolean compressorShouldSwitch();

	boolean driveShouldShift();

	boolean driveShouldReverse();

	ShooterMode getShooterMode();

	boolean hopperShouldReverse();

	boolean shooterShouldShoot();

	boolean shooterShouldStop();
}
