package ca.warp7.frc;

import static ca.warp7.frc.Functions.limit;

public class CheesyDrive {

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

	private static class InternalState {
		private double quickStopAccumulator = 0;
		private double oldWheel = 0;
	}

	private InternalState mState = new InternalState();
	private IDriveSignalReceiver mReceiver;

	public void setDriveSignalReceiver(IDriveSignalReceiver receiver) {
		mReceiver = receiver;
	}

	public void feedForward(IControls driver) {
		feedForward(driver.getWheel(), driver.getThrottle(), driver.shouldQuickTurn(), driver.shouldAltQuickTurn());
	}

	@SuppressWarnings("SameParameterValue")
	private void feedForward(double wheel, double throttle, boolean quickTurn, boolean altQuickTurn) {
		double rightPwm;
		double leftPwm;
		double negInertiaScalar;
		double negInertia;
		double negInertiaAccumulator;
		double overPower, angularPower;

		wheel = deadBand(wheel);
		throttle = deadBand(throttle);

		negInertia = wheel - mState.oldWheel;
		mState.oldWheel = wheel;

		wheel = sinScale(wheel, 0.9f, 1, 0.9f);
		negInertiaScalar = wheel * negInertia > 0 ? 2.5f : Math.abs(wheel) > .65 ? 6 : 4;
		negInertiaAccumulator = negInertia * negInertiaScalar;
		wheel += negInertiaAccumulator;

		if (altQuickTurn) {
			if (Math.abs(throttle) < 0.2) {
				double alpha = .1f;
				mState.quickStopAccumulator = ((1 - alpha) * mState.quickStopAccumulator) +
						(alpha * limit(wheel, 1.0) * 5);
			}
			overPower = -wheel * .75;
			angularPower = -wheel * 1;
		} else if (quickTurn) {
			if (Math.abs(throttle) < 0.2) {
				double alpha = .1f;
				mState.quickStopAccumulator = ((1 - alpha) * mState.quickStopAccumulator) +
						(alpha * limit(wheel, 1.0) * 5);
			}
			overPower = 1;
			angularPower = -wheel * 1;
		} else {
			overPower = 0;
			double sensitivity = .9;
			angularPower = throttle * wheel * sensitivity - mState.quickStopAccumulator;
			mState.quickStopAccumulator = wrapAccumulator(mState.quickStopAccumulator);
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

		mReceiver.onDriveSpeedDemand(leftPwm, rightPwm);
	}

	public interface IControls {
		double getWheel();

		double getThrottle();

		boolean shouldQuickTurn();

		boolean shouldAltQuickTurn();
	}
}
