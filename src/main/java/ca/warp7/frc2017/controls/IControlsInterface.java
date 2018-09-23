package ca.warp7.frc2017.controls;

import ca.warp7.frc.drive.ICheesyDriveController;

public interface IControlsInterface extends ICheesyDriveController {
	enum ShooterMode {
		RPM_1, RPM_2, NONE
	}

	boolean compressorShouldSwitch();

	boolean driveShouldShift();

	boolean driveShouldReverse();

	ShooterMode getShooterMode();

	boolean hopperShouldReverse();

	boolean shooterShouldShoot();

	boolean shooterShouldStop();
}
