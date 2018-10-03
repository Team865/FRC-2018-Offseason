package ca.warp7.frc.wpi_wrapper;

import ca.warp7.frc.values.ButtonState;
import edu.wpi.first.wpilibj.GenericHID;

import static ca.warp7.frc.values.ButtonState.*;

@SuppressWarnings("unused")
public class XboxController {
	private LocalXboxController mInnerController;
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
		mInnerController = new LocalXboxController(portNumber);
	}

	private ButtonState compareBooleanState(boolean previousState, boolean newState) {
		return newState != previousState ? newState ? PRESSED : RELEASED : newState ? HELD_DOWN : KEPT_UP;
	}

	public ButtonState getAButton() {
		boolean previousState = this.mAButton;
		boolean newState = mInnerController.getAButton();
		this.mAButton = newState;
		return compareBooleanState(previousState, newState);
	}

	public ButtonState getBButton() {
		boolean previousState = mBButton;
		boolean newState = mInnerController.getBButton();
		this.mBButton = newState;
		return compareBooleanState(previousState, newState);
	}

	public ButtonState getXButton() {
		boolean previousState = mXButton;
		boolean newState = mInnerController.getXButton();
		mXButton = newState;
		return compareBooleanState(previousState, newState);
	}

	public ButtonState getYButton() {
		boolean previousState = mYButton;
		boolean newState = mInnerController.getYButton();
		mYButton = newState;
		return compareBooleanState(previousState, newState);
	}

	public ButtonState getBumper(GenericHID.Hand h) {
		boolean previousState = h == GenericHID.Hand.kLeft ? mLeftBumper : mRightBumper;
		boolean newState = mInnerController.getBumper(h);
		if (h == GenericHID.Hand.kLeft)
			mLeftBumper = newState;
		else
			mRightBumper = newState;
		return compareBooleanState(previousState, newState);
	}

	public ButtonState getTrigger(GenericHID.Hand h) {
		boolean previousState = h == GenericHID.Hand.kLeft ? mLeftTrigger : mRightTrigger;
		boolean newState = mInnerController.getTriggerAxis(h) >= 0.5;
		if (h == GenericHID.Hand.kLeft)
			mLeftTrigger = newState;
		else
			mRightTrigger = newState;
		return compareBooleanState(previousState, newState);
	}

	public ButtonState getStickButton(GenericHID.Hand h) {
		boolean previousState = h == GenericHID.Hand.kLeft ?
				mLeftStickButton : mRightStickButton;
		boolean newState = mInnerController.getStickButton(h);
		if (h == GenericHID.Hand.kLeft)
			mLeftStickButton = newState;
		else
			mRightStickButton = newState;
		return compareBooleanState(previousState, newState);
	}

	public ButtonState getStartButton() {
		boolean previousState = mStartButton;
		boolean newState = mInnerController.getStartButton();
		mStartButton = newState;
		return compareBooleanState(previousState, newState);
	}

	public ButtonState getBackButton() {
		boolean previousState = mBackButton;
		boolean newState = mInnerController.getBackButton();
		mBackButton = newState;
		return compareBooleanState(previousState, newState);
	}

	public ButtonState getDpad(int value) {
		int previousState = mDirectionalPad;
		int newState = mInnerController.getPOV(0);
		mDirectionalPad = newState;
		return newState != previousState ? newState == value ?
				PRESSED : RELEASED : newState == value ? HELD_DOWN : KEPT_UP;
	}

	public void setRumble(GenericHID.RumbleType type, double d) {
		mInnerController.setRumble(type, d);
	}

	public double getX(GenericHID.Hand hand) {
		return mInnerController.getX(hand);
	}

	public double getY(GenericHID.Hand hand) {
		return mInnerController.getY(hand);
	}
}
