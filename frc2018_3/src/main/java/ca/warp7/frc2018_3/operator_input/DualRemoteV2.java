package ca.warp7.frc2018_3.operator_input;

import ca.warp7.frc.commons.XboxController2;
import ca.warp7.frc.commons.core.IController;
import ca.warp7.frc.commons.core.IControllerLoop;
import ca.warp7.frc2018_3.subsystems.Intake;

import java.util.Arrays;
import java.util.List;

import static ca.warp7.frc.commons.ButtonState.HELD_DOWN;
import static ca.warp7.frc.commons.ButtonState.PRESSED;
import static ca.warp7.frc2018_3.Components.*;

public class DualRemoteV2 implements IControllerLoop {

    private XboxController2 DRIVER;
    private XboxController2 OPERATOR;

    @Override
    public List<IController> onCreateControllers() {
        DRIVER = new XboxController2(0);
        OPERATOR = new XboxController2(1);
        return Arrays.asList(DRIVER, OPERATOR);
    }

    @Override
    public void onPeriodic() {
        // Limelight
        if (DRIVER.getXButton() == PRESSED) limelight.switchCamera();

        // Pneumatics
        if (DRIVER.getBackButton() == PRESSED) pneumatics.toggleClosedLoop();
        pneumatics.setShouldSolenoidBeOnForShifter(DRIVER.getRightBumper() != HELD_DOWN);
        pneumatics.setGrapplingHook(DRIVER.getStartButton() == HELD_DOWN);

        // Driving
        drive.setReversed(DRIVER.getRightStickButton() == PRESSED);
        drive.cheesyDrive(DRIVER.getRightXAxis() * -1, DRIVER.getLeftYAxis(),
                DRIVER.getLeftBumper() == HELD_DOWN);

        // Intake
        if (DRIVER.getAButton() == PRESSED) intake.togglePiston();
        if (DRIVER.getLeftDirectionalPad() == HELD_DOWN) intake.setSpeed(Intake.kSlowOuttakePower);
        else if (DRIVER.getLeftTrigger() == HELD_DOWN) intake.setSpeed(Intake.kFastOuttakePower);
        else if (DRIVER.getRightTrigger() == HELD_DOWN) intake.setSpeed(Intake.kIntakePower);
        else intake.setSpeed(0);

        // "Climber" that is actually arm
        if (OPERATOR.getBButton() == HELD_DOWN) climber.setSpeed(OPERATOR.getLeftYAxis());
        else if (DRIVER.getBButton() == HELD_DOWN) climber.setSpeed(DRIVER.getLeftYAxis());
        else climber.setSpeed(0);

        // Actual climbing mechanism
        if (OPERATOR.getStartButton() == HELD_DOWN) actualClimber.setSpeed(OPERATOR.getLeftYAxis());
        else actualClimber.setSpeed(0);
    }
}
