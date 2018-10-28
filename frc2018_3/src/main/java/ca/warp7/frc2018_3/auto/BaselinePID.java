package ca.warp7.frc2018_3.auto;

import ca.warp7.frc.commons.PIDValues;
import ca.warp7.frc.commons.core.IAutoMode;
import ca.warp7.frc.commons.core.IAction;
import ca.warp7.frc2018_3.auto.actions.DriveForDistanceAction;

public class BaselinePID implements IAutoMode {

    @Override
    public IAction getMainAction() {
        return new DriveForDistanceAction(new PIDValues(0.018, 0.00001, 0.23), 150, 5);
    }
}
