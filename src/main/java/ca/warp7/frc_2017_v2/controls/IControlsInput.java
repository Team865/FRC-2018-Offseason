package ca.warp7.frc_2017_v2.controls;

import ca.warp7.frc.cheesy_drive.ICheesyDriveInput;

public interface IControlsInput extends ICheesyDriveInput {

	boolean compressorShouldSwitch();

	boolean driveShouldShift();

	boolean driveShouldReverse();

	ShooterMode getShooterMode();

	boolean hopperShouldReverse();

	boolean shooterShouldShoot();

	boolean shooterShouldStop();
}
