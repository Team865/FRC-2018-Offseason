package ca.warp7.frc2017;

import ca.warp7.frc.core.Robot;
import ca.warp7.frc2017.controls.DualRemote;
import ca.warp7.frc2017.controls.IControlsInput;
import ca.warp7.frc2017.mapping.DefaultMapping;
import ca.warp7.frc2017.mapping.DefaultOI;

public final class Wanda extends Robot<IControlsInput> {

	@Override
	protected void onConstruct() {
		System.out.println("Hello me is robit!");
		setController(new DualRemote(0, 1));
		DefaultMapping.configure();
	}

	@Override
	protected void onOperatorInput(IControlsInput controller) {
		DefaultOI.onUpdate(controller);
	}
}
