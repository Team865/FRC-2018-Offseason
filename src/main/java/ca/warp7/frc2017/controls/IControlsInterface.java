package ca.warp7.frc2017.controls;

import ca.warp7.frc.CheesyDrive;

public interface IControlsInterface extends CheesyDrive.IControls {
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
