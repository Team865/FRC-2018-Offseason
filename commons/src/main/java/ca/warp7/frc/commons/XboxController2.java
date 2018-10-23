package ca.warp7.frc.commons;

import ca.warp7.frc.commons.core.IController;
import ca.warp7.frc.commons.wrapper.WPILibXboxController;

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

    private WPILibXboxController mController;
    private State mState = new State();

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

    public XboxController2(int portNumber) {
        mController = new WPILibXboxController(portNumber);
    }

    private static ButtonState compare(ButtonState old, boolean bool) {
        return bool ? ((old == PRESSED) ? HELD_DOWN : PRESSED) : ((old == RELEASED) ? KEPT_UP : RELEASED);
    }

    @Override
    public void onUpdateData() {
        int POV = mController.getPOV();
        mState.AButton = compare(mState.AButton, mController.getAButton());
        mState.BButton = compare(mState.BButton, mController.getBButton());
        mState.XButton = compare(mState.XButton, mController.getXButton());
        mState.YButton = compare(mState.YButton, mController.getYButton());
        mState.LeftBumper = compare(mState.LeftBumper, mController.getBumper(kLeft));
        mState.RightBumper = compare(mState.RightBumper, mController.getBumper(kRight));
        mState.LeftTrigger = compare(mState.LeftTrigger, mController.getTriggerAxis(kLeft) > kTriggerDeadBand);
        mState.RightTrigger = compare(mState.RightTrigger, mController.getTriggerAxis(kRight) > kTriggerDeadBand);
        mState.LeftStickButton = compare(mState.LeftStickButton, mController.getStickButton(kLeft));
        mState.RightStickButton = compare(mState.RightStickButton, mController.getStickButton(kRight));
        mState.StartButton = compare(mState.StartButton, mController.getStartButton());
        mState.BackButton = compare(mState.BackButton, mController.getBackButton());
        mState.UpDirectionalPad = compare(mState.UpDirectionalPad, POV == kUpPOV);
        mState.RightDirectionalPad = compare(mState.RightDirectionalPad, POV == kRightPOV);
        mState.DownDirectionalPad = compare(mState.DownDirectionalPad, POV == kDownPOV);
        mState.LeftDirectionalPad = compare(mState.LeftDirectionalPad, POV == kLeftPOV);
    }

    @Override
    public void onReportState() {
    }

    public ButtonState getAButton() {
        return mState.AButton;
    }

    public ButtonState getBButton() {
        return mState.BButton;
    }

    public ButtonState getXButton() {
        return mState.XButton;
    }

    public ButtonState getYButton() {
        return mState.YButton;
    }

    public ButtonState getLeftBumper() {
        return mState.LeftBumper;
    }

    public ButtonState getLeftTrigger() {
        return mState.LeftTrigger;
    }

    public ButtonState getLeftStickButton() {
        return mState.LeftStickButton;
    }

    public ButtonState getRightBumper() {
        return mState.RightBumper;
    }

    public ButtonState getRightTrigger() {
        return mState.RightTrigger;
    }

    public ButtonState getRightStickButton() {
        return mState.RightStickButton;
    }

    public ButtonState getStartButton() {
        return mState.StartButton;
    }

    public ButtonState getBackButton() {
        return mState.BackButton;
    }

    public ButtonState getUpDirectionalPad() {
        return mState.UpDirectionalPad;
    }

    public ButtonState getRightDirectionalPad() {
        return mState.RightDirectionalPad;
    }

    public ButtonState getDownDirectionalPad() {
        return mState.DownDirectionalPad;
    }

    public ButtonState getLeftDirectionalPad() {
        return mState.LeftDirectionalPad;
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
