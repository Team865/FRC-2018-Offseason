package ca.warp7.frc2018_3.operator_input;

import ca.warp7.frc.commons.wpi_wrapper.XboxController;

import static ca.warp7.frc.commons.state.ButtonState.HELD_DOWN;
import static ca.warp7.frc.commons.state.ButtonState.PRESSED;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

@SuppressWarnings("WeakerAccess")
public class SingleRemote implements IOperatorController {

    private final XboxController mDriver;

    public SingleRemote() {
        mDriver = new XboxController(0);
    }

    @Override
    public double getWheel() {
        return -mDriver.getX(kRight);
    }

    @Override
    public double getThrottle() {
        return mDriver.getY(kLeft);
    }

    @Override
    public boolean shouldQuickTurn() {
        return mDriver.getBumper(kLeft) == HELD_DOWN;
    }

    @Override
    public boolean shouldAltQuickTurn() {
        return false;
    }

    @Override
    public boolean driveShouldReverse() {
        return mDriver.getStickButton(kRight) == PRESSED;
    }

    @Override
    public boolean driveShouldShift() {
        return mDriver.getBumper(kRight) != HELD_DOWN;
    }

    @Override
    public boolean compressorShouldSwitch() {
        return mDriver.getBackButton() == PRESSED;
    }

    @Override
    public IntakeMode getIntakeMode() {
        if (mDriver.getTrigger(kRight) == HELD_DOWN || mDriver.getDpad(90) == HELD_DOWN) {

            return IntakeMode.INTAKE;

        } else if (mDriver.getTrigger(kLeft) == HELD_DOWN) {

            return IntakeMode.SLOW_OUTTAKE;

        } else if (mDriver.getDpad(270) == HELD_DOWN) {

            return IntakeMode.FAST_OUTTAKE;

        } else {

            return IntakeMode.NONE;
        }
    }
}
