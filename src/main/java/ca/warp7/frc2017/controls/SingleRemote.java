package ca.warp7.frc2017.controls;

import ca.warp7.frc.controller.StatefulXboxController;

import static ca.warp7.frc.controller.ControllerState.HELD_DOWN;
import static ca.warp7.frc.controller.ControllerState.PRESSED;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

public class SingleRemote implements IControlsInterface {

	private final StatefulXboxController mDriver;

	public SingleRemote(int driverPort) {
		mDriver = new StatefulXboxController(driverPort);
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
		return mDriver.getBumper(kLeft) == HELD_DOWN;
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
	public boolean driveShouldShift() {
		return mDriver.getBumper(kRight) == HELD_DOWN;
	}

	@Override
	public boolean compressorShouldSwitch() {
		return mDriver.getBackButton() == PRESSED;
	}

	@Override
	public ShooterMode getShooterMode() {
		return ShooterMode.NONE;
	}

	@Override
	public boolean hopperShouldReverse() {
		return false;
	}

	@Override
	public boolean shooterShouldShoot() {
		return false;
	}

	@Override
	public boolean shooterShouldStop() {
		return false;
	}
}
