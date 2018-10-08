package ca.warp7.frc2017_v2.auto.test_modes;

import ca.warp7.frc.core.IAutoMode;
import ca.warp7.frc.scheduler.IAction;
import ca.warp7.frc.scheduler.ScheduledMode;
import ca.warp7.frc2017_v2.auto.actions.DriveForDistanceAction;
import ca.warp7.frc2017_v2.auto.actions.DriveForTimeAction;

public class TestDriveMode implements IAutoMode {
	@Override
	public IAction getMainAction() {

		return new ScheduledMode()
				.chain()
				.addToEnd(new DriveForTimeAction(0.5, 0.5, 0.5))
				.addToEnd(new DriveForDistanceAction(5, 30))
				.getAction();
	}
}
