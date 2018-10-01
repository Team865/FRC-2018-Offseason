package ca.warp7.frc2017.controls;

import ca.warp7.frc.cheesy_drive.ICheesyDriveInout;

public interface IControlsInput extends ICheesyDriveInout {

	boolean compressorShouldSwitch();

	boolean driveShouldShift();

	boolean driveShouldReverse();

	ShooterMode getShooterMode();

	boolean hopperShouldReverse();

	boolean shooterShouldShoot();

	boolean shooterShouldStop();
}
