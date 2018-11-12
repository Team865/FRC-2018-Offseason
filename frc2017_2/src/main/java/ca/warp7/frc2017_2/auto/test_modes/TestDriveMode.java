package ca.warp7.frc2017_2.auto.test_modes;

import ca.warp7.frc.action.scheduler.ScheduleBuilder;
import ca.warp7.frc.core.IAction;
import ca.warp7.frc.core.IAutoMode;
import ca.warp7.frc2017_2.auto.actions.DriveForDistanceAction;
import ca.warp7.frc2017_2.auto.actions.DriveForTimeAction;
import ca.warp7.frc2017_2.constants.RobotMap;

public class TestDriveMode implements IAutoMode {
    @Override
    public IAction getMainAction() {

        return new ScheduleBuilder()
                .startSeries()
                .add(new DriveForTimeAction(0.5, 0.5, 0.5))
                .add(new DriveForDistanceAction(RobotMap.PID.uniformDrivePID, 5, 30))
                .getActionGraph();
    }
}
