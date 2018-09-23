package ca.warp7.frc2017.controls;

import ca.warp7.frc.controller.StatefulXboxController;

import static ca.warp7.frc.controller.ControllerState.*;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

public class DualRemote implements IControlsInterface {

	private final StatefulXboxController mDriver;
	private final StatefulXboxController mOperator;

	public DualRemote(int driverPort, int operatorPort) {
		mDriver = new StatefulXboxController(driverPort);
		mOperator = new StatefulXboxController(operatorPort);
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
		if (mOperator.getBButton() == HELD_DOWN) {
			return ShooterMode.RPM_4425;
		} else if (mOperator.getTrigger(kLeft) == HELD_DOWN) {
			return ShooterMode.RPM_4450;
		}
		return ShooterMode.NONE;
	}

	@Override
	public boolean hopperShouldReverse() {
		return mOperator.getDpad(90) == HELD_DOWN;
	}

	@Override
	public boolean shooterShouldShoot() {
		return mOperator.getAButton() == HELD_DOWN;
	}

	@Override
	public boolean shooterShouldStop() {
		return mOperator.getAButton() == KEPT_UP;
	}
}
