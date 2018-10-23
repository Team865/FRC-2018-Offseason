package ca.warp7.frc2018_3.operator_input;

import ca.warp7.frc.commons.XboxController;

import static ca.warp7.frc.commons.ButtonState.HELD_DOWN;
import static ca.warp7.frc.commons.ButtonState.PRESSED;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

@SuppressWarnings("WeakerAccess")
@Deprecated
public class SingleRemote implements IOperatorController {

    private final XboxController mDriver;

    public SingleRemote() {
        mDriver = new XboxController(0);
    }

    @Override
    public double getWheel() {
        return mDriver.getX(kRight);
    }

    @Override
    public double getThrottle() {
        return -mDriver.getY(kLeft);
    }

    @Override
    public boolean shouldQuickTurn() {
        return mDriver.getBumper(kLeft) == HELD_DOWN;
    }

    @Override
    public boolean driveShouldReverse() {
        return mDriver.getStickButton(kRight) == PRESSED;
    }

    @Override
    public boolean driveShouldSolenoidBeOnForShifter() {
        return mDriver.getBumper(kRight) != HELD_DOWN;
    }

    @Override
    public boolean compressorShouldSwitch() {
        return mDriver.getBackButton() == PRESSED;
    }

    @Override
    public IntakeMode getIntakeMode() {
        if (mDriver.getTrigger(kRight) == HELD_DOWN) {

            return IntakeMode.INTAKE;

        } else if (mDriver.getTrigger(kLeft) == HELD_DOWN) {

            return IntakeMode.SLOW_OUTTAKE;

        } else if (mDriver.getDPad(270) == HELD_DOWN) {

            return IntakeMode.FAST_OUTTAKE;

        } else {

            return IntakeMode.NONE;
        }
    }

    @Override
    public double getClimberSpeed() {
        if (mDriver.getBButton() == HELD_DOWN){
            return -mDriver.getY(kRight); // This overrides Cheesy Drive
        }
        return 0;
    }

    @Override
    public double getArmSpeed() {
        if (mDriver.getBumper(kLeft) == HELD_DOWN) {
            return mDriver.getY(kRight); // This overrides Cheesy Drive
        }
        return 0;
    }

    @Override
    public double getArmDistance() {
        if (mDriver.getYButton() == HELD_DOWN) {
            return mDriver.getY(kRight); // This overrides Cheesy Drive
        }
        return 0;
    }

    @Override
    public boolean intakeShouldTogglePiston() {
        return mDriver.getAButton() == PRESSED;
    }

    @Override
    public boolean cameraShouldSwitch() {
        return mDriver.getXButton() == PRESSED;
    }

    @Override
    public boolean grapplingHookShouldDeploy() {
        return mDriver.getStartButton() == HELD_DOWN;
    }

    @Override
    public double getActualClimberSpeed() {
        return 0;
    }
}
