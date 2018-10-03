package ca.warp7.frc_2017v2.operator_input;

import ca.warp7.frc.cheesy_drive.ICheesyDriveInput;

public interface IOperatorController extends ICheesyDriveInput {

	boolean compressorShouldSwitch();

	boolean driveShouldShift();

	boolean driveShouldReverse();

	ShooterMode getShooterMode();

	boolean hopperShouldReverse();

	boolean shooterShouldShoot();

	boolean shooterShouldStop();
}
