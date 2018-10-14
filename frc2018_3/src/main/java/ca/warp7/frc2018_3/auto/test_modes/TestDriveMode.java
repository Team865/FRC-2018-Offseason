package ca.warp7.frc2018_3.auto.test_modes;

import ca.warp7.frc.commons.core.IAutoMode;
import ca.warp7.frc.commons.scheduler.IAction;
import ca.warp7.frc.commons.scheduler.ScheduleBuilder;
import ca.warp7.frc2018_3.auto.actions.DriveForDistanceAction;
import ca.warp7.frc2018_3.auto.actions.DriveForTimeAction;
import ca.warp7.frc2018_3.Constants;

public class TestDriveMode implements IAutoMode {
    @Override
    public IAction getMainAction() {

        return new ScheduleBuilder()
                .chain()
                .addToEnd(new DriveForTimeAction(0.5, 0.5, 0.5))
                .addToEnd(new DriveForDistanceAction(Constants.kUniformDrivePID, 5, 30))
                .getAction();
    }
}
