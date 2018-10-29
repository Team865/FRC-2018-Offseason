package ca.warp7.frc2018_3;

import ca.warp7.frc.commons.core.IControls;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.XboxControlsState;
import ca.warp7.frc2018_3.subsystems.Intake;

import static ca.warp7.frc.commons.core.ButtonState.HELD_DOWN;
import static ca.warp7.frc.commons.core.ButtonState.PRESSED;
import static ca.warp7.frc2018_3.Components.*;

public class DualRemote implements IControls {

    private XboxControlsState DRIVER = Robot.getXboxController(0);
    private XboxControlsState OPERATOR = Robot.getXboxController(1);

    @Override
    public void periodic() {
        // Limelight
        if (DRIVER.XButton == PRESSED) limelight.switchCamera();

        // Pneumatics
        if (DRIVER.BackButton == PRESSED) pneumatics.toggleClosedLoop();
        pneumatics.setShouldSolenoidBeOnForShifter(DRIVER.RightBumper != HELD_DOWN);
        pneumatics.setGrapplingHook(DRIVER.StartButton == HELD_DOWN);

        // Driving
        drive.setReversed(DRIVER.RightStickButton == PRESSED);
        drive.cheesyDrive(DRIVER.RightXAxis * -1, DRIVER.LeftXAxis, DRIVER.LeftBumper == HELD_DOWN);

        // Intake
        if (DRIVER.AButton == PRESSED) intake.togglePiston();
        if (DRIVER.LeftDirectionalPad == HELD_DOWN) intake.setSpeed(Intake.kSlowOuttakePower);
        else if (DRIVER.LeftTrigger == HELD_DOWN) intake.setSpeed(Intake.kFastOuttakePower);
        else if (DRIVER.RightTrigger == HELD_DOWN) intake.setSpeed(Intake.kIntakePower);
        else intake.setSpeed(0);

        // Arm lift
        if (OPERATOR.BButton == HELD_DOWN) armLift.setSpeed(OPERATOR.LeftYAxis);
        else if (DRIVER.BButton == HELD_DOWN) armLift.setSpeed(DRIVER.LeftYAxis);
        else armLift.setSpeed(0);

        // Climber
        if (OPERATOR.StartButton == HELD_DOWN) climber.setSpeed(OPERATOR.LeftYAxis);
        else climber.setSpeed(0);
    }
}
