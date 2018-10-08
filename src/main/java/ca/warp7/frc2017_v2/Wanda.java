package ca.warp7.frc2017_v2;

import ca.warp7.frc.core.Robot;
import ca.warp7.frc2017_v2.auto.modes.NothingMode;
import ca.warp7.frc2017_v2.constants.DefaultMap;
import ca.warp7.frc2017_v2.constants.RobotMap;
import ca.warp7.frc2017_v2.operator_input.DualRemote;
import ca.warp7.frc2017_v2.operator_input.OperatorInput;

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

		setAutoMode(new NothingMode());
		//setAutoMode(() -> new DriveForTimeAction(1, 0.5, 0.5));

		OperatorInput.setController(new DualRemote(0, 1));
		setOIRunner(OperatorInput::onUpdate);
	}
}

