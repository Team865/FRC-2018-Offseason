package ca.warp7.frc2017.controls;

import ca.warp7.frc.drive.ICheesyDriveController;

public interface IControlsInterface extends ICheesyDriveController {
	boolean compressorShouldSwitch();

	boolean driveShouldShift();

	boolean driveShouldReverse();
}
