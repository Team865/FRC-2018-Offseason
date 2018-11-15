package ca.warp7.frc2018_3.auto;

import ca.warp7.action.IAction;
import ca.warp7.action.IActionMode;
import ca.warp7.frc2018_3.actions.DriveForTimeAction;

public class Baseline implements IActionMode {

    @Override
    public IAction getAction() {
        return new DriveForTimeAction(8, 0.2, 0.2);
    }
}
