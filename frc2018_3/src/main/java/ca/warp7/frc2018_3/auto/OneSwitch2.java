package ca.warp7.frc2018_3.auto;

import ca.warp7.action.IAction;
import ca.warp7.action.impl.ActionMode;
import ca.warp7.frc.PIDValues;
import ca.warp7.frc2018_3.actions.DriveForDistanceAction;
import ca.warp7.frc2018_3.actions.OuttakeCube;
import edu.wpi.first.wpilibj.DriverStation;


public class OneSwitch2 extends ActionMode {

    private char side;

    public OneSwitch2(char side) {
        this.side = side;
    }

    @Override
    public IAction getAction() {
        if (DriverStation.getInstance().getGameSpecificMessage().charAt(0) == side) return queue(
                new DriveForDistanceAction(new PIDValues(0.018, 0.00001, 0.23), 103, 5),
                new OuttakeCube(0.5, -0.75)
        );
        else return new DriveForDistanceAction(new PIDValues(0.018, 0.00001, 0.23), 103, 5);
    }
}
