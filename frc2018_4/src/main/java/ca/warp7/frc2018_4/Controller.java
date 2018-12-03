package ca.warp7.frc2018_4;

import ca.warp7.frc.core.IControls;
import ca.warp7.frc.core.RobotLoader;
import ca.warp7.frc.core.XboxControlsState;

import ca.warp7.frc2018_4.Components.*;

import static ca.warp7.frc2018_4.Components.drive;

public class Controller implements IControls {
    XboxControlsState Driver = RobotLoader.createXboxController(0, true);
    XboxControlsState Operator = RobotLoader.createXboxController(1, true);

    @Override
    public void mainPeriodic() {

        // Driving
        drive.setShouldSolenoidBeOnForShifter(Driver.RightBumper != HeldDown);
        drive.setReversed(Driver.RightStickButton == HeldDown);
        if (Driver.BButton != HeldDown) {
            drive.cheesyDrive(Driver.RightXAxis, Driver.LeftYAxis, Driver.LeftBumper == HeldDown);
        }
    }
}
