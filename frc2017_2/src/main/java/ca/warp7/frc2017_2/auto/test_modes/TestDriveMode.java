package ca.warp7.frc2017_2.auto.test_modes;

import ca.warp7.action.IAction;
import ca.warp7.frc.scheduler.ScheduleBuilder;
import ca.warp7.frc2017_2.auto.actions.DriveForDistanceAction;
import ca.warp7.frc2017_2.auto.actions.DriveForTimeAction;
import ca.warp7.frc2017_2.constants.RobotMap;

public class TestDriveMode implements IAction.Mode {
    @Override
    public IAction getAction() {

        return new ScheduleBuilder()
                .startSeries()
                .add(new DriveForTimeAction(0.5, 0.5, 0.5))
                .add(new DriveForDistanceAction(RobotMap.PID.uniformDrivePID, 5, 30))
                .getActionGraph();
    }
}
