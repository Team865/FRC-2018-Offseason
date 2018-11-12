package ca.warp7.frc;

import ca.warp7.frc.wrapper.WPILibXboxController;
import edu.wpi.first.wpilibj.GenericHID;

import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;

@Deprecated
public class XboxController {
    private WPILibXboxController mWPILibController;
    private boolean mAButton;
    private boolean mBButton;
    private boolean mXButton;
    private boolean mYButton;
    private boolean mLeftBumper;
    private boolean mRightBumper;
    private boolean mLeftTrigger;
    private boolean mRightTrigger;
    private boolean mLeftStickButton;
    private boolean mRightStickButton;
    private boolean mStartButton;
    private boolean mBackButton;
    private int mDirectionalPad = -1;

    public XboxController(int portNumber) {
        mWPILibController = new WPILibXboxController(portNumber);
    }

    private static ButtonState compareBooleanState(boolean previousState, boolean newState) {
        return newState != previousState ? newState ? ButtonState.PRESSED : ButtonState.RELEASED : newState ? ButtonState.HELD_DOWN : ButtonState.KEPT_UP;
    }

    public ButtonState getAButton() {
        boolean previousState = this.mAButton;
        boolean newState = mWPILibController.getAButton();
        this.mAButton = newState;
        return compareBooleanState(previousState, newState);
    }

    public ButtonState getBButton() {
        boolean previousState = mBButton;
        boolean newState = mWPILibController.getBButton();
        this.mBButton = newState;
        return compareBooleanState(previousState, newState);
    }

    public ButtonState getXButton() {
        boolean previousState = mXButton;
        boolean newState = mWPILibController.getXButton();
        mXButton = newState;
        return compareBooleanState(previousState, newState);
    }

    public ButtonState getYButton() {
        boolean previousState = mYButton;
        boolean newState = mWPILibController.getYButton();
        mYButton = newState;
        return compareBooleanState(previousState, newState);
    }

    public ButtonState getBumper(GenericHID.Hand hand) {
        boolean previousState = hand == kLeft ? mLeftBumper : mRightBumper;
        boolean newState = mWPILibController.getBumper(hand);
        if (hand == kLeft)
            mLeftBumper = newState;
        else
            mRightBumper = newState;
        return compareBooleanState(previousState, newState);
    }

    public ButtonState getTrigger(GenericHID.Hand hand) {
        boolean previousState = hand == kLeft ? mLeftTrigger : mRightTrigger;
        boolean newState = mWPILibController.getTriggerAxis(hand) >= 0.5;
        if (hand == kLeft)
            mLeftTrigger = newState;
        else
            mRightTrigger = newState;
        return compareBooleanState(previousState, newState);
    }

    public ButtonState getStickButton(GenericHID.Hand hand) {
        boolean previousState = hand == kLeft ?
                mLeftStickButton : mRightStickButton;
        boolean newState = mWPILibController.getStickButton(hand);
        if (hand == kLeft)
            mLeftStickButton = newState;
        else
            mRightStickButton = newState;
        return compareBooleanState(previousState, newState);
    }

    public ButtonState getStartButton() {
        boolean previousState = mStartButton;
        boolean newState = mWPILibController.getStartButton();
        mStartButton = newState;
        return compareBooleanState(previousState, newState);
    }

    public ButtonState getBackButton() {
        boolean previousState = mBackButton;
        boolean newState = mWPILibController.getBackButton();
        mBackButton = newState;
        return compareBooleanState(previousState, newState);
    }

    public ButtonState getDPad(int direction) {
        int previousState = mDirectionalPad;
        int newState = mWPILibController.getPOV(0);
        mDirectionalPad = newState;
        return newState != previousState ? newState == direction ?
                ButtonState.PRESSED : ButtonState.RELEASED : newState == direction ? ButtonState.HELD_DOWN : ButtonState.KEPT_UP;
    }

    public double getX(GenericHID.Hand hand) {
        return mWPILibController.getX(hand);
    }

    public double getY(GenericHID.Hand hand) {
        return mWPILibController.getY(hand);
    }
}
