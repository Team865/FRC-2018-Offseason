package ca.warp7.frc.controller;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;

import static ca.warp7.frc.controller.ControllerState.*;

/*
An add-on interface to the WPILib's XboxController that
remembers each button's previous state. The methods
maps to XboxController and all return a ControllerState
 */

@SuppressWarnings("unused")
public class XboxControllerV2 {

	private XboxController mInnerController;

	// Tracked Controller States
	// The following are boolean values of previous state that are used in conjunction
	// with the current state to determine if a button is KEPT_UP, HELD_DOWN, PRESSED, or RELEASED

	private boolean mAButton, mBButton, mXButton, mYButton = false;
	private boolean mLeftBumper, mRightBumper = false;
	private boolean mLeftTrigger, mRightTrigger = false;
	private boolean mLeftStickButton, mRightStickButton = false;
	private boolean mStartButton, mBackButton = false;
	private int mDirectionalPad = -1;

	public XboxControllerV2(int port) {
		mInnerController = new XboxController(port);
	}

	private ControllerState compareBooleanState(boolean previousState, boolean newState) {
		return newState != previousState ? newState ?
				PRESSED : RELEASED : newState ? HELD_DOWN : KEPT_UP;
	}

	public ControllerState getAButton() {
		boolean previousState = this.mAButton;
		boolean newState = mInnerController.getAButton();
		this.mAButton = newState;
		return compareBooleanState(previousState, newState);
	}

	public ControllerState getBButton() {
		boolean previousState = mBButton;
		boolean newState = mInnerController.getBButton();
		this.mBButton = newState;
		return compareBooleanState(previousState, newState);
	}

	public ControllerState getXButton() {
		boolean previousState = mXButton;
		boolean newState = mInnerController.getXButton();
		mXButton = newState;
		return compareBooleanState(previousState, newState);
	}

	public ControllerState getYButton() {
		boolean previousState = mYButton;
		boolean newState = mInnerController.getYButton();
		mYButton = newState;
		return compareBooleanState(previousState, newState);
	}

	public ControllerState getBumper(Hand h) {
		boolean previousState = h == Hand.kLeft ? mLeftBumper : mRightBumper;
		boolean newState = mInnerController.getBumper(h);
		if (h == Hand.kLeft)
			mLeftBumper = newState;
		else
			mRightBumper = newState;
		return compareBooleanState(previousState, newState);
	}

	public ControllerState getTrigger(Hand h) {
		boolean previousState = h == Hand.kLeft ? mLeftTrigger : mRightTrigger;
		boolean newState = mInnerController.getTriggerAxis(h) >= 0.5;
		if (h == Hand.kLeft)
			mLeftTrigger = newState;
		else
			mRightTrigger = newState;
		return compareBooleanState(previousState, newState);
	}

	public ControllerState getStickButton(Hand h) {
		boolean previousState = h == Hand.kLeft ?
				mLeftStickButton : mRightStickButton;
		boolean newState = mInnerController.getStickButton(h);
		if (h == Hand.kLeft)
			mLeftStickButton = newState;
		else
			mRightStickButton = newState;
		return compareBooleanState(previousState, newState);
	}

	public ControllerState getStartButton() {
		boolean previousState = mStartButton;
		boolean newState = mInnerController.getStartButton();
		mStartButton = newState;
		return compareBooleanState(previousState, newState);
	}

	public ControllerState getBackButton() {
		boolean previousState = mBackButton;
		boolean newState = mInnerController.getBackButton();
		mBackButton = newState;
		return compareBooleanState(previousState, newState);
	}

	public ControllerState getDpad(int value) {
		int previousState = mDirectionalPad;
		int newState = mInnerController.getPOV(0);
		mDirectionalPad = newState;
		return newState != previousState ? newState == value ?
				PRESSED : RELEASED : newState == value ? HELD_DOWN : KEPT_UP;
	}

	public void setRumble(RumbleType type, double d) {
		mInnerController.setRumble(type, d);
	}

	public double getX(Hand hand) {
		return mInnerController.getX(hand);
	}

	public double getY(Hand hand) {
		return mInnerController.getY(hand);
	}
}
