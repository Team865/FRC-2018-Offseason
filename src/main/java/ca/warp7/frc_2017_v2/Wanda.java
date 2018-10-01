package ca.warp7.frc_2017_v2;

import ca.warp7.frc.core.Robot;
import ca.warp7.frc_2017_v2.controls.DualRemote;
import ca.warp7.frc_2017_v2.mapping.DefaultMapping;
import ca.warp7.frc_2017_v2.mapping.OI;

public final class Wanda extends Robot {

	@Override
	protected void onConstruct() {
		System.out.println("Hello me is robit!");

		DefaultMapping.configure();

		setAutonomousMode(null);

		OI.setController(new DualRemote(0, 1));

		setOperatorInput(OI::onUpdate);
	}
}
