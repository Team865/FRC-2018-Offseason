package ca.warp7.frc2018_3.auto.modes;

import ca.warp7.frc.commons.core.IAutoMode;
import ca.warp7.frc.commons.scheduler.IAction;
import ca.warp7.frc.commons.scheduler.ScheduleBuilder;
import ca.warp7.frc.commons.state.PIDValues;
import ca.warp7.frc2018_3.auto.actions.DriveForDistanceAction;
import ca.warp7.frc2018_3.auto.actions.OuttakeCube;

public class OneSwitch implements IAutoMode {

    @Override
    public IAction getMainAction() {
//        return new DriveForDistanceAction(new PIDValues(0.02, 0.0013, 0.23), 10, 0);
        return new ScheduleBuilder()
                .startSeries()
                .addToEnd(new DriveForDistanceAction(new PIDValues(0.018, 0.00001, 0.23), 50, 1))
                .addToEnd(new OuttakeCube(0.5, -0.75))
                .getAction();
//
//        return new ActionSeries(
//                new DriveForDistanceAction(new PIDValues(0.018, 0.00001, 0.23), 50, 1),
//                new OuttakeCube(0.5, -0.75)
//        );
    }
}
