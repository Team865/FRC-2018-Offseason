package ca.warp7.frc.commons;

import ca.warp7.frc.commons.core.IController;
import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.StateType;
import edu.wpi.first.wpilibj.XboxController;

import static ca.warp7.frc.commons.ButtonState.*;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

@SuppressWarnings("unused")
public class XboxController2 implements IController {

    private static final double kTriggerDeadBand = 0.5;
    private static final int kUpPOV = 0;
    private static final int kRightPOV = 90;
    private static final int kDownPOV = 180;
    private static final int kLeftPOV = 270;

    private XboxController mController;
    private State mS = new State();

    private static class State {
        private ButtonState AButton;
        private ButtonState BButton;
        private ButtonState XButton;
        private ButtonState YButton;
        private ButtonState LeftBumper;
        private ButtonState RightBumper;
        private ButtonState LeftTrigger;
        private ButtonState RightTrigger;
        private ButtonState LeftStickButton;
        private ButtonState RightStickButton;
        private ButtonState StartButton;
        private ButtonState BackButton;
        private ButtonState UpDirectionalPad;
        private ButtonState RightDirectionalPad;
        private ButtonState DownDirectionalPad;
        private ButtonState LeftDirectionalPad;
    }

    public XboxController2(int port) {
        mController = new XboxController(port);
    }

    private static ButtonState update(ButtonState old, boolean bool) {
        return bool ? ((old == PRESSED) ? HELD_DOWN : PRESSED) : ((old == RELEASED) ? KEPT_UP : RELEASED);
    }

    @Override
    public void onUpdateData() {
        int POV = mController.getPOV();
        mS.AButton = update(mS.AButton, mController.getAButton());
        mS.BButton = update(mS.BButton, mController.getBButton());
        mS.XButton = update(mS.XButton, mController.getXButton());
        mS.YButton = update(mS.YButton, mController.getYButton());
        mS.LeftBumper = update(mS.LeftBumper, mController.getBumper(kLeft));
        mS.RightBumper = update(mS.RightBumper, mController.getBumper(kRight));
        mS.LeftTrigger = update(mS.LeftTrigger, mController.getTriggerAxis(kLeft) > kTriggerDeadBand);
        mS.RightTrigger = update(mS.RightTrigger, mController.getTriggerAxis(kRight) > kTriggerDeadBand);
        mS.LeftStickButton = update(mS.LeftStickButton, mController.getStickButton(kLeft));
        mS.RightStickButton = update(mS.RightStickButton, mController.getStickButton(kRight));
        mS.StartButton = update(mS.StartButton, mController.getStartButton());
        mS.BackButton = update(mS.BackButton, mController.getBackButton());
        mS.UpDirectionalPad = update(mS.UpDirectionalPad, POV == kUpPOV);
        mS.RightDirectionalPad = update(mS.RightDirectionalPad, POV == kRightPOV);
        mS.DownDirectionalPad = update(mS.DownDirectionalPad, POV == kDownPOV);
        mS.LeftDirectionalPad = update(mS.LeftDirectionalPad, POV == kLeftPOV);
        Robot.report(String.format("XboxController[%s]", mController.getPort()), StateType.COMPONENT_STATE, mS);
    }

    public ButtonState getAButton() {
        return mS.AButton;
    }

    public ButtonState getBButton() {
        return mS.BButton;
    }

    public ButtonState getXButton() {
        return mS.XButton;
    }

    public ButtonState getYButton() {
        return mS.YButton;
    }

    public ButtonState getLeftBumper() {
        return mS.LeftBumper;
    }

    public ButtonState getLeftTrigger() {
        return mS.LeftTrigger;
    }

    public ButtonState getLeftStickButton() {
        return mS.LeftStickButton;
    }

    public ButtonState getRightBumper() {
        return mS.RightBumper;
    }

    public ButtonState getRightTrigger() {
        return mS.RightTrigger;
    }

    public ButtonState getRightStickButton() {
        return mS.RightStickButton;
    }

    public ButtonState getStartButton() {
        return mS.StartButton;
    }

    public ButtonState getBackButton() {
        return mS.BackButton;
    }

    public ButtonState getUpDirectionalPad() {
        return mS.UpDirectionalPad;
    }

    public ButtonState getRightDirectionalPad() {
        return mS.RightDirectionalPad;
    }

    public ButtonState getDownDirectionalPad() {
        return mS.DownDirectionalPad;
    }

    public ButtonState getLeftDirectionalPad() {
        return mS.LeftDirectionalPad;
    }

    public double getLeftTriggerAxis() {
        return mController.getTriggerAxis(kLeft);
    }

    public double getRightTriggerAxis() {
        return mController.getTriggerAxis(kRight);
    }

    public double getLeftXAxis() {
        return mController.getX(kLeft);
    }

    public double getLeftYAxis() {
        return mController.getY(kLeft);
    }

    public double getRightXAxis() {
        return mController.getX(kRight);
    }

    public double getRightYAxis() {
        return mController.getY(kRight);
    }
}
