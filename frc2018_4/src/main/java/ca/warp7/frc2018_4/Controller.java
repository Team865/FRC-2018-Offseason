package ca.warp7.frc2018_4;

import ca.warp7.frc.core.IControls;
import ca.warp7.frc.core.RobotLoader;
import ca.warp7.frc.core.XboxControlsState;

import ca.warp7.frc2018_4.Components.*;

import static ca.warp7.frc2018_4.Components.drive;

public class Controller implements IControls {
    @Override
    public void mainPeriodic() {
        XboxControlsState Driver = new RobotLoader().createXboxController(0, true);
        XboxControlsState Operator = new RobotLoader().createXboxController(1, true);

        // Driving
        drive.setShouldSolenoidBeOnForShifter(Driver.RightBumper != HeldDown);
        drive.setReversed(Driver.RightStickButton == HeldDown);
        if (Driver.BButton != HeldDown) {
            drive.cheesyDrive(Driver.RightXAxis, Driver.LeftYAxis, Driver.LeftBumper == HeldDown);
        }
    }
}
