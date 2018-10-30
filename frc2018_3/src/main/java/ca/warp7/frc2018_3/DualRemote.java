package ca.warp7.frc2018_3;

import ca.warp7.frc.commons.core.IControls;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.XboxControlsState;
import ca.warp7.frc2018_3.subsystems.Intake;

import static ca.warp7.frc2018_3.Components.*;

public class DualRemote implements IControls {

    private XboxControlsState Driver = Robot.getXboxController(0);
    private XboxControlsState Operator = Robot.getXboxController(1);

    @Override
    public void periodic() {
        // Limelight
        if (Driver.XButton == Pressed) limelight.switchCamera();

        // Pneumatics
        if (Driver.BackButton == Pressed) pneumatics.toggleClosedLoop();
        pneumatics.setShouldSolenoidBeOnForShifter(Driver.RightBumper != HeldDown);
        pneumatics.setGrapplingHook(Driver.StartButton == HeldDown);

        // Driving
        drive.setReversed(Driver.RightStickButton == Pressed);
        if (Driver.BButton != HeldDown) {
            drive.cheesyDrive(Driver.RightXAxis * -1, Driver.LeftYAxis, Driver.LeftBumper == HeldDown);
        }

        // Intake
        if (Driver.AButton == Pressed) intake.togglePiston();
        if (Driver.LeftDPad == HeldDown) intake.setSpeed(Intake.kSlowOuttakePower);
        else if (Driver.LeftTrigger == HeldDown) intake.setSpeed(Intake.kFastOuttakePower);
        else if (Driver.RightTrigger == HeldDown) intake.setSpeed(Intake.kIntakePower);
        else intake.setSpeed(0);

        // Arm lift
        if (Operator.BButton == HeldDown) armLift.setSpeed(Operator.LeftYAxis);
        else if (Driver.BButton == HeldDown) armLift.setSpeed(Driver.LeftYAxis);
        else armLift.setSpeed(0);

        // Climber
        if (Operator.StartButton == HeldDown) climber.setSpeed(Operator.LeftYAxis);
        else climber.setSpeed(0);
    }
}
