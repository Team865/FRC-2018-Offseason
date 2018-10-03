package ca.warp7.frc_2017v2.operator_input;

import ca.warp7.frc.wpi_wrapper.XboxController;

import static ca.warp7.frc.values.ButtonState.HELD_DOWN;
import static ca.warp7.frc.values.ButtonState.KEPT_UP;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;

public class DualRemote extends SingleRemote {

	private final XboxController mOperator;

	public DualRemote(int driverPort, int operatorPort) {
		super(driverPort);
		mOperator = new XboxController(operatorPort);
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
