package ca.warp7.frc.cheesy_drive;

import static ca.warp7.frc.core.Functions.limit;

public class CheesyDrive {

	private final InputState mInputState = new InputState();
	private final CurrentState mCurrentState = new CurrentState();

	private ISignalReceiver mReceiver;

	private static double deadBand(double n) {
		return Math.abs(n) < 0.18 ? 0 : (n - (0.18 * Math.signum(n))) * 1.22;
	}

	private static double wrapAccumulator(double accumulator) {
		if (accumulator > 1) {
			accumulator -= 1;
		} else if (accumulator < -1) {
			accumulator += 1;
		} else {
			accumulator = 0;
		}
		return accumulator;
	}

	private static double sinScale(double val, double nonLinearity, int passes, double lim) {
		double scaled = lim * Math.sin(Math.PI / 2 * nonLinearity * val) / Math.sin(Math.PI / 2 * nonLinearity);
		return passes == 1 ? scaled : sinScale(scaled, nonLinearity, passes - 1, lim);
	}

	public void setDriveSignalReceiver(ISignalReceiver receiver) {
		mReceiver = receiver;
	}

	public void setInputsFromControls(ICheesyDriveInput controls) {
		mInputState.wheel = controls.getWheel();
		mInputState.throttle = controls.getThrottle();
		mInputState.quickTurn = controls.shouldQuickTurn();
		mInputState.altQuickTurn = controls.shouldAltQuickTurn();
	}

	public void calculateFeed() {
		double rightPwm;
		double leftPwm;
		double negInertiaScalar;
		double negInertia;
		double negInertiaAccumulator;
		double overPower, angularPower;

		double wheel = deadBand(mInputState.wheel);
		double throttle = deadBand(mInputState.throttle);

		negInertia = wheel - mCurrentState.oldWheel;
		mCurrentState.oldWheel = wheel;

		wheel = sinScale(wheel, 0.9f, 1, 0.9f);
		negInertiaScalar = wheel * negInertia > 0 ? 2.5f : Math.abs(wheel) > .65 ? 6 : 4;
		negInertiaAccumulator = negInertia * negInertiaScalar;
		wheel += negInertiaAccumulator;

		if (mInputState.altQuickTurn) {
			if (Math.abs(throttle) < 0.2) {
				double alpha = .1f;
				mCurrentState.quickStopAccumulator = ((1 - alpha) * mCurrentState.quickStopAccumulator) +
						(alpha * limit(wheel, 1.0) * 5);
			}
			overPower = -wheel * .75;
			angularPower = -wheel * 1;

		} else if (mInputState.quickTurn) {
			if (Math.abs(throttle) < 0.2) {
				double alpha = .1f;
				mCurrentState.quickStopAccumulator = ((1 - alpha) * mCurrentState.quickStopAccumulator) +
						(alpha * limit(wheel, 1.0) * 5);
			}
			overPower = 1;
			angularPower = -wheel * 1;

		} else {
			overPower = 0;
			double sensitivity = .9;
			angularPower = throttle * wheel * sensitivity - mCurrentState.quickStopAccumulator;
			mCurrentState.quickStopAccumulator = wrapAccumulator(mCurrentState.quickStopAccumulator);
		}

		rightPwm = leftPwm = throttle;
		leftPwm += angularPower;
		rightPwm -= angularPower;

		if (leftPwm > 1) {
			rightPwm -= overPower * (leftPwm - 1);
			leftPwm = 1;
		} else if (rightPwm > 1) {
			leftPwm -= overPower * (rightPwm - 1);
			rightPwm = 1;
		} else if (leftPwm < -1) {
			rightPwm += overPower * (-1 - leftPwm);
			leftPwm = -1;
		} else if (rightPwm < -1) {
			leftPwm += overPower * (-1 - rightPwm);
			rightPwm = -1;
		}

		mReceiver.setDemandedDriveSpeed(leftPwm, rightPwm);
	}

	static class CurrentState {
		double quickStopAccumulator = 0;
		double oldWheel = 0;
	}

	static class InputState {
		double wheel;
		double throttle;
		boolean quickTurn;
		boolean altQuickTurn;
	}
}
