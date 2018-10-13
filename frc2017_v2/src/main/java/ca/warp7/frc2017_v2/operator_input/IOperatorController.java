package ca.warp7.frc2017_v2.operator_input;

import ca.warp7.frc_commons.cheesy_drive.ICheesyDriveInput;

interface IOperatorController extends ICheesyDriveInput {

	boolean compressorShouldSwitch();

	boolean driveShouldShift();

	boolean driveShouldReverse();

	ShooterMode getShooterMode();

	boolean hopperShouldReverse();

	boolean shooterShouldShoot();

	boolean shooterShouldStop();
}
