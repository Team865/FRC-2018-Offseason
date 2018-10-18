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
        return new ScheduleBuilder()
                .startSeries()
                .addToEnd(new DriveForDistanceAction(new PIDValues(0.018, 0.00001, 0.23), 103, 3))
                .addToEnd(new OuttakeCube(0.5, -0.75))
                .getAction();
    }
}
