package ca.warp7.frc_2017_v2.controls;

import ca.warp7.frc.wpi_wrappers.XboxController;

import static ca.warp7.frc.wpi_wrappers.ControllerState.HELD_DOWN;
import static ca.warp7.frc.wpi_wrappers.ControllerState.PRESSED;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

@SuppressWarnings("WeakerAccess")
public class SingleRemote implements IControlsInput {

	private final XboxController mDriver;

	public SingleRemote(int driverPort) {
		mDriver = new XboxController(driverPort);
	}

	@Override
	public double getWheel() {
		return -mDriver.getX(kRight);
	}

	@Override
	public double getThrottle() {
		return mDriver.getY(kLeft);
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
		return mDriver.getBumper(kRight) != HELD_DOWN;
	}

	@Override
	public boolean compressorShouldSwitch() {
		return mDriver.getBackButton() == PRESSED;
	}

	// Unimplemented Methods

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
