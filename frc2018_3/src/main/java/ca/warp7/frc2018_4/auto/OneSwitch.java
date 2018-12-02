package ca.warp7.frc2018_4.auto;

import ca.warp7.frc.PIDValues;
import ca.warp7.action.IAction;
import ca.warp7.frc.scheduler.ScheduleBuilder;
import ca.warp7.frc2018_4.actions.DriveForDistanceAction;
import ca.warp7.frc2018_4.actions.OuttakeCube;
import edu.wpi.first.wpilibj.DriverStation;

@SuppressWarnings("deprecation")
public class OneSwitch implements IAction.Mode {

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
