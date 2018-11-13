package ca.warp7.frc2018_3.auto;

import ca.warp7.frc.PIDValues;
import ca.warp7.frc.action.api.IAction;
import ca.warp7.frc.action.api.IActionMode;
import ca.warp7.frc.scheduler.ScheduleBuilder;
import ca.warp7.frc2018_3.actions.DriveForDistanceAction;
import ca.warp7.frc2018_3.actions.OuttakeCube;
import edu.wpi.first.wpilibj.DriverStation;

@SuppressWarnings("deprecation")
public class OneSwitch implements IActionMode {

    private char side;

    public OneSwitch(char side) {
        this.side = side;
    }

    @Override
    public IAction getAction() {
        String gameMessage = DriverStation.getInstance().getGameSpecificMessage();
        if (gameMessage.charAt(0) == side) {
            return new ScheduleBuilder()
                    .startSeries()
                    .add(new DriveForDistanceAction(new PIDValues(0.018, 0.00001, 0.23), 103, 5))
                    .add(new OuttakeCube(0.5, -0.75))
                    .getActionGraph();
        }
        return new DriveForDistanceAction(new PIDValues(0.018, 0.00001, 0.23), 103, 5);
    }
}
