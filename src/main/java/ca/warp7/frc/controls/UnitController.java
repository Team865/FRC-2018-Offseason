package ca.warp7.frc.controls;

import ca.warp7.frc.controls.ControllerState;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;

import static ca.warp7.frc.controls.ControllerState.*;

/*
An add-on interface to the WPILib's XboxController that
remembers each button's previous state. The methods
maps to XboxController and all return a ControllerState
 */

@SuppressWarnings("unused")
public class UnitController {

	private XboxController mInnerController;

	// Tracked Controller States
	// The following are boolean values of previous state that are used in conjunction
	// with the current state to determine if a button is UP, DOWN, PRESSED, or RELEASED

	private boolean mAButton, mBButton, mXButton, mYButton = false;
	private boolean mLeftBumper, mRightBumper = false;
	private boolean mLeftTrigger, mRightTrigger = false;
	private boolean mLeftStickButton, mRightStickButton = false;
	private boolean mStartButton, mBackButton = false;
	private int mDirectionalPad = -1;

	public UnitController(int port) {
		mInnerController = new XboxController(port);
	}

	private ControllerState compareBooleanState(boolean previous_state, boolean new_state) {
		return new_state != previous_state ? new_state ?
				PRESSED : RELEASED : new_state ? DOWN : UP;
	}

	public ControllerState getAButton() {
		boolean previous_state = this.mAButton;
		boolean new_state = mInnerController.getAButton();
		this.mAButton = new_state;
		return compareBooleanState(previous_state, new_state);
	}

	public ControllerState getBButton() {
		boolean previous_state = mBButton;
		boolean new_state = mInnerController.getBButton();
		this.mBButton = new_state;
		return compareBooleanState(previous_state, new_state);
	}

	public ControllerState getXButton() {
		boolean previous_state = mXButton;
		boolean new_state = mInnerController.getXButton();
		mXButton = new_state;
		return compareBooleanState(previous_state, new_state);
	}

	public ControllerState getYButton() {
		boolean previous_state = mYButton;
		boolean new_state = mInnerController.getYButton();
		mYButton = new_state;
		return compareBooleanState(previous_state, new_state);
	}

	public ControllerState getBumper(Hand h) {
		boolean previous_state = h == Hand.kLeft ?
				mLeftBumper : mRightBumper;
		boolean new_state = mInnerController.getBumper(h);
		if (h == Hand.kLeft)
			mLeftBumper = new_state;
		else
			mRightBumper = new_state;
		return compareBooleanState(previous_state, new_state);
	}

	public ControllerState getTrigger(Hand h) {
		boolean previous_state = h == Hand.kLeft ?
				mLeftTrigger : mRightTrigger;
		boolean new_state = mInnerController.getTriggerAxis(h) >= 0.5;
		if (h == Hand.kLeft)
			mLeftTrigger = new_state;
		else
			mRightTrigger = new_state;
		return compareBooleanState(previous_state, new_state);
	}

	public ControllerState getStickButton(Hand h) {
		boolean previous_state = h == Hand.kLeft ?
				mLeftStickButton : mRightStickButton;
		boolean new_state = mInnerController.getStickButton(h);
		if (h == Hand.kLeft)
			mLeftStickButton = new_state;
		else
			mRightStickButton = new_state;
		return compareBooleanState(previous_state, new_state);
	}

	public ControllerState getStartButton() {
		boolean previous_state = mStartButton;
		boolean new_state = mInnerController.getStartButton();
		mStartButton = new_state;
		return compareBooleanState(previous_state, new_state);
	}

	public ControllerState getBackButton() {
		boolean previous_state = mBackButton;
		boolean new_state = mInnerController.getBackButton();
		mBackButton = new_state;
		return compareBooleanState(previous_state, new_state);
	}

	public ControllerState getDpad(int value) {
		int previous_state = mDirectionalPad;
		int new_state = mInnerController.getPOV(0);
		mDirectionalPad = new_state;
		return new_state != previous_state ? new_state == value ?
				PRESSED : RELEASED : new_state == value ? DOWN : UP;
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
