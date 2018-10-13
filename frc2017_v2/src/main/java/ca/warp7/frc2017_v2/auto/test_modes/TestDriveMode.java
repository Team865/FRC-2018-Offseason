package ca.warp7.frc2017_v2.auto.test_modes;

import ca.warp7.frc_commons.core.IAutoMode;
import ca.warp7.frc_commons.scheduler.IAction;
import ca.warp7.frc_commons.scheduler.ScheduleBuilder;
import ca.warp7.frc2017_v2.auto.actions.DriveForDistanceAction;
import ca.warp7.frc2017_v2.auto.actions.DriveForTimeAction;
import ca.warp7.frc2017_v2.constants.RobotMap;

public class TestDriveMode implements IAutoMode {
	@Override
	public IAction getMainAction() {

		return new ScheduleBuilder()
				.chain()
				.addToEnd(new DriveForTimeAction(0.5, 0.5, 0.5))
				.addToEnd(new DriveForDistanceAction(RobotMap.PID.uniformDrivePID, 5, 30))
				.getAction();
	}
}
