package ca.warp7.frc2018_5;

import ca.warp7.frc.core.IControls;
import ca.warp7.frc.core.RobotLoader;
import ca.warp7.frc.core.XboxControlsState;
import ca.warp7.frc2018_5.subsystems.Intake;

import static ca.warp7.frc2018_5.Components.*;

public class TeleopRemote implements IControls {

    private XboxControlsState Driver = RobotLoader.createXboxController(0, true);
    private XboxControlsState Operator = RobotLoader.createXboxController(1, false);

    @Override
    public void mainPeriodic() {
        // Limelight
        if (Driver.XButton == Pressed) limelight.switchCamera();

        // Pneumatics
        if (Driver.BackButton == Pressed) pneumatics.toggleClosedLoop();
        pneumatics.setGrapplingHook(Driver.StartButton == HeldDown);

        // Driving
        drive.setShouldSolenoidBeOnForShifter(Driver.RightBumper != HeldDown);
        drive.setReversed(Driver.RightStickButton == HeldDown);
        if (Driver.BButton != HeldDown) {
            drive.cheesyDrive(Driver.RightXAxis, Driver.LeftYAxis, Driver.LeftBumper == HeldDown);
        }

        // Intake
        if (Driver.AButton == Pressed) intake.togglePiston();
        if (Driver.LeftDPad == HeldDown) intake.setSpeed(Intake.kSlowOuttakePower);
        else if (Driver.LeftTrigger == HeldDown) intake.setSpeed(Intake.kFastOuttakePower);
        else if (Driver.RightTrigger == HeldDown) intake.setSpeed(Intake.kIntakePower);
        else intake.setSpeed(0);
    }
}
