package ca.warp7.frc2018_3.auto;

import ca.warp7.frc.PIDValues;
import ca.warp7.frc.action.api.IAction;
import ca.warp7.frc.action.api.IActionMode;
import ca.warp7.frc2018_3.actions.DriveForDistanceAction;

public class BaselinePID implements IActionMode {

    @Override
    public IAction getAction() {
        return new DriveForDistanceAction(new PIDValues(0.018, 0.00001, 0.23), 150, 5);
    }
}
