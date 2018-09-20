package ca.warp7.frc2018.controller;

import ca.warp7.frc.drive.UnitController;

import static ca.warp7.frc.drive.ControllerState.DOWN;
import static ca.warp7.frc.drive.ControllerState.PRESSED;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

public class SingleRemote implements IController {

	private final UnitController mDriver;

	public SingleRemote(int driverPort) {
		mDriver = new UnitController(driverPort);
	}

	@Override
	public double getWheel() {
		return mDriver.getX(kLeft);
	}

	@Override
	public double getThrottle() {
		return mDriver.getY(kRight);
	}

	@Override
	public boolean shouldQuickTurn() {
		return mDriver.getBumper(kLeft) == DOWN;
	}

	@Override
	public boolean shouldAltQuickTurn() {
		return false;
	}

	@Override
	public boolean driveShouldReverse() {
		return mDriver.getStickButton(kRight) == PRESSED;
	}

	@Override
	public boolean compressorShouldSwitch() {
		return mDriver.getBackButton() == PRESSED;
	}

	@Override
	public boolean driveShouldShift() {
		return mDriver.getBumper(kRight) == DOWN;
	}
}
