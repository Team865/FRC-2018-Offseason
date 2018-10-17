package ca.warp7.frc2018_3.auto.modes;

import ca.warp7.frc.commons.core.IAutoMode;
import ca.warp7.frc.commons.scheduler.IAction;
import ca.warp7.frc.commons.state.PIDValues;
import ca.warp7.frc2018_3.auto.actions.DriveForDistanceAction;

public class BaselinePID implements IAutoMode {

    @Override
    public IAction getMainAction() {
        return new DriveForDistanceAction(new PIDValues(0.02, 0.0013, 0.23), 10, 3);
    }
}
