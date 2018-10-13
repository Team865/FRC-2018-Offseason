package ca.warp7.frc2017_v2.auto.modes;

import ca.warp7.frc_commons.core.IAutoMode;
import ca.warp7.frc_commons.scheduler.IAction;

public class NothingMode implements IAutoMode {
	@Override
	public IAction getMainAction() {
		return new IAction() {
			@Override
			public void onStart() {
				System.out.println("NothingMode is doing nothing");
			}

			@Override
			public boolean shouldFinish() {
				return false;
			}

			@Override
			public void onUpdate() {
			}

			@Override
			public void onStop() {
				System.out.println("NothingMode is done");
			}
		};
	}
}
