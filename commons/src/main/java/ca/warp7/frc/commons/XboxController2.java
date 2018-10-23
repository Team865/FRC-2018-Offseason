package ca.warp7.frc.commons;

import ca.warp7.frc.commons.core.IController;
import ca.warp7.frc.commons.wrapper.WPILibXboxController;

import static ca.warp7.frc.commons.ButtonState.*;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

//@SuppressWarnings("unused")
public class XboxController2 implements IController {

    private static final double kTriggerDeadBand = 0.5;

    private WPILibXboxController mController;
    private ButtonState mAButton;
    private ButtonState mBButton;
    private ButtonState mXButton;
    private ButtonState mYButton;
    private ButtonState mLeftBumper;
    private ButtonState mRightBumper;
    private ButtonState mLeftTrigger;
    private ButtonState mRightTrigger;
    private ButtonState mLeftStickButton;
    private ButtonState mRightStickButton;
    private ButtonState mStartButton;
    private ButtonState mBackButton;
    private ButtonState mUpDirectionalPad;
    private ButtonState mRightDirectionalPad;
    private ButtonState mDownDirectionalPad;
    private ButtonState mLeftDirectionalPad;

    public XboxController2(int portNumber) {
        mController = new WPILibXboxController(portNumber);
    }

    private static ButtonState compare(ButtonState old, boolean bool) {
        return bool ? ((old == PRESSED) ? HELD_DOWN : PRESSED) : ((old == RELEASED) ? KEPT_UP : RELEASED);
    }

    @Override
    public void updateValues() {
        int POV = mController.getPOV(0);
        mAButton = compare(mAButton, mController.getAButton());
        mBButton = compare(mBButton, mController.getBButton());
        mXButton = compare(mXButton, mController.getXButton());
        mYButton = compare(mYButton, mController.getYButton());
        mLeftBumper = compare(mLeftBumper, mController.getBumper(kLeft));
        mRightBumper = compare(mRightBumper, mController.getBumper(kRight));
        mLeftTrigger = compare(mLeftTrigger, mController.getTriggerAxis(kLeft) > kTriggerDeadBand);
        mRightTrigger = compare(mRightTrigger, mController.getTriggerAxis(kRight) > kTriggerDeadBand);
        mLeftStickButton = compare(mLeftStickButton, mController.getStickButton(kLeft));
        mRightStickButton = compare(mRightStickButton, mController.getStickButton(kRight));
        mStartButton = compare(mStartButton, mController.getStartButton());
        mBackButton = compare(mBackButton, mController.getBackButton());
        mUpDirectionalPad = compare(mUpDirectionalPad, POV == 0);
        mRightDirectionalPad = compare(mRightDirectionalPad, POV == 90);
        mDownDirectionalPad = compare(mDownDirectionalPad, POV == 180);
        mLeftDirectionalPad = compare(mLeftDirectionalPad, POV == 270);
    }

    public ButtonState getAButton() {
        return mAButton;
    }

    public ButtonState getBButton() {
        return mBButton;
    }

    public ButtonState getXButton() {
        return mXButton;
    }

    public ButtonState getYButton() {
        return mYButton;
    }

    public ButtonState getLeftBumper() {
        return mLeftBumper;
    }

    public ButtonState getLeftTrigger() {
        return mLeftTrigger;
    }

    public ButtonState getLeftStickButton() {
        return mLeftStickButton;
    }

    public ButtonState getRightBumper() {
        return mRightBumper;
    }

    public ButtonState getRightTrigger() {
        return mRightTrigger;
    }

    public ButtonState getRightStickButton() {
        return mRightStickButton;
    }

    public ButtonState getStartButton() {
        return mStartButton;
    }

    public ButtonState getBackButton() {
        return mBackButton;
    }

    public ButtonState getUpDirectionalPad() {
        return mUpDirectionalPad;
    }

    public ButtonState getRightDirectionalPad() {
        return mRightDirectionalPad;
    }

    public ButtonState getDownDirectionalPad() {
        return mDownDirectionalPad;
    }

    public ButtonState getLeftDirectionalPad() {
        return mLeftDirectionalPad;
    }

    public double getLeftX() {
        return mController.getX(kLeft);
    }

    public double getLeftY() {
        return mController.getY(kLeft);
    }

    public double getRightX() {
        return mController.getX(kRight);
    }

    public double getRightY() {
        return mController.getY(kRight);
    }
}
