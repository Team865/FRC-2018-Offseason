package ca.warp7.frc2018_3.auto;

import ca.warp7.frc.commons.core.IAutoMode;
import ca.warp7.frc.commons.scheduler.IAction;
import ca.warp7.frc2018_3.auto.actions.DriveForTimeAction;

public class Baseline implements IAutoMode {

    @Override
    public IAction getMainAction() {
        return new DriveForTimeAction(8, 0.2, 0.2);
    }
}
