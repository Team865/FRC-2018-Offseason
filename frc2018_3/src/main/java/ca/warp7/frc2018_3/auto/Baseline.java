package ca.warp7.frc2018_3.auto;

import ca.warp7.frc.core.IAction;
import ca.warp7.frc.core.IAutoMode;
import ca.warp7.frc2018_3.actions.DriveForTimeAction;

public class Baseline implements IAutoMode {

    @Override
    public IAction getAction() {
        return new DriveForTimeAction(8, 0.2, 0.2);
    }
}
