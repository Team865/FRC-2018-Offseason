package ca.warp7.frc2018.controller;

import ca.warp7.frc.drive.ICheesyDriveController;

public interface IController extends ICheesyDriveController {
	boolean compressorShouldSwitch();

	boolean driveShouldShift();

	boolean driveShouldReverse();
}
