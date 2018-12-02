package ca.warp7.frc2018_4.auto;

import ca.warp7.action.IAction;
import ca.warp7.frc2018_4.actions.DriveForTimeAction;

public class Baseline implements IAction.Mode {

    @Override
    public IAction getAction() {
        return new DriveForTimeAction(8, 0.2, 0.2);
    }
}
