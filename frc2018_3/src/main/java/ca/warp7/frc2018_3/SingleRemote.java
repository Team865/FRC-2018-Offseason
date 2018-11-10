package ca.warp7.frc2018_3;

import ca.warp7.frc.commons.core.IControls;
import ca.warp7.frc.commons.core.RobotLoader;
import ca.warp7.frc.commons.core.XboxControlsState;
import ca.warp7.frc2018_3.subsystems.Intake;

import static ca.warp7.frc2018_3.Components.*;

@Deprecated
public class SingleRemote implements IControls {

    private XboxControlsState Driver = RobotLoader.createXboxController(0);

    @Override
    public void mainPeriodic() {
        // Limelight
        if (Driver.XButton == Pressed) limelight.switchCamera();

        // Pneumatics
        if (Driver.BackButton == Pressed) pneumatics.toggleClosedLoop();
        pneumatics.setShouldSolenoidBeOnForShifter(Driver.RightBumper != HeldDown);
        pneumatics.setGrapplingHook(Driver.StartButton == HeldDown);

        // Driving/ArmLift
        drive.setReversed(Driver.RightStickButton == HeldDown);
        if (Driver.BButton == HeldDown) armLift.setSpeed(Driver.LeftYAxis);
        else {
            armLift.setSpeed(0);
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
