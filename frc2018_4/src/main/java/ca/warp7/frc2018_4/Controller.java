package ca.warp7.frc2018_4;

import ca.warp7.frc.core.IControls;
import ca.warp7.frc.core.RobotLoader;
import ca.warp7.frc.core.XboxControlsState;

import ca.warp7.frc2018_4.Components.*;

import static ca.warp7.frc2018_4.Components.climber;
import static ca.warp7.frc2018_4.Components.drive;
import static ca.warp7.frc2018_4.Components.wrist;

public class Controller implements IControls {
    XboxControlsState Driver = RobotLoader.createXboxController(0, true);
    XboxControlsState Operator = RobotLoader.createXboxController(1, true);

    @Override
    public void mainPeriodic() {
        // Climbing
        if (Operator.BButton == HeldDown){
            climber.mInputState.mDemandedMotorSpeed = Operator.LeftYAxis;
        }

        // Superstructure

        // Wrist
        if (Operator.XButton == HeldDown){
            wrist.mInputState.mShouldSlowFall = true;
            wrist.mInputState.mShouldUseTargetAngle = false;
        }
        else if (Operator.LeftBumper == HeldDown) {
            wrist.mInputState.mShouldSlowFall = false;
            wrist.mInputState.mShouldUseTargetAngle = false;
            wrist.mInputState.mDemandedWristAngle = Operator.RightYAxis;
        }
        else {
            wrist.mInputState.mShouldSlowFall = false;
            wrist.mInputState.mShouldUseTargetAngle = false;
            wrist.mInputState.mDemandedWristAngularVelocity = Operator.RightYAxis;
        }

        // Driving
        drive.setShouldSolenoidBeOnForShifter(Driver.RightBumper != HeldDown);
        drive.setReversed(Driver.RightStickButton == HeldDown);
        if (Driver.BButton != HeldDown) {
            drive.cheesyDrive(Driver.RightXAxis, Driver.LeftYAxis, Driver.LeftBumper == HeldDown);
        }
    }
}
