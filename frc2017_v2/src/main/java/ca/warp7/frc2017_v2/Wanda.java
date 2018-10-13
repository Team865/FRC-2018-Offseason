package ca.warp7.frc2017_v2;

import ca.warp7.frc_commons.core.Robot;
import ca.warp7.frc2017_v2.auto.actions.DriveForTimeAction;
import ca.warp7.frc2017_v2.constants.DefaultMap;
import ca.warp7.frc2017_v2.constants.RobotMap;
import ca.warp7.frc2017_v2.operator_input.OperatorInput;
import ca.warp7.frc2017_v2.operator_input.SingleRemote;

/**
 * * __        __              _
 * * \ \      / /_ _ _ __   __| | __ _
 * *  \ \ /\ / / _` | '_ \ / _` |/ _` |
 * *   \ V  V / (_| | | | | (_| | (_| |
 * *    \_/\_/ \__,_|_| |_|\__,_|\__,_|
 */

public final class Wanda extends Robot {

	@Override
	protected void onCreate() {
		System.out.println("Hello me is robit!");

		setMapping(RobotMap.class);
		DefaultMap.configure();

		//setAutoMode(new NothingMode());
		setAutoMode(() -> new DriveForTimeAction(2, -0, -0.5));

		//OperatorInput.setController(new DualRemote(0, 1));
		OperatorInput.setController(new SingleRemote(0));
		setOIRunner(OperatorInput::onUpdate);
	}
}

