package ca.warp7.frc.drive;

import ca.warp7.frc.MathUtil;

public class CheesyDrive {

	private static double deadBand(double n) {
		return Math.abs(n) < 0.18 ? 0 : (n - (0.18 * Math.signum(n))) * 1.22;
	}

	private static double wrap_accumulator(double accumulator) {
		if (accumulator > 1) {
			accumulator -= 1;
		} else if (accumulator < -1) {
			accumulator += 1;
		} else {
			accumulator = 0;
		}
		return accumulator;
	}

	private static double sinScale(double val, double non_linearity, int passes, double lim) {
		double scaled = lim * Math.sin(Math.PI / 2 * non_linearity * val) / Math.sin(Math.PI / 2 * non_linearity);
		return passes == 1 ? scaled : sinScale(scaled, non_linearity, passes - 1, lim);
	}

	private double mQuickStopAccumulator = 0;
	private double mOldWheel = 0;
	private IDriveSignalReceiver mReceiver;

	public void setSignalReceiver(IDriveSignalReceiver receiver) {
		mReceiver = receiver;
	}

	public void driveWithController(ICheesyDriveController driver) {
		drive(driver.getWheel(), driver.getThrottle(), driver.shouldQuickTurn(), driver.shouldAltQuickTurn());
	}

	@SuppressWarnings("SameParameterValue")
	private void drive(double wheel, double throttle, boolean quickTurn, boolean altQuickTurn) {
		double rightPwm;
		double leftPwm;
		double negInertiaScalar;
		double negInertia;
		double negInertiaAccumulator;
		double overPower, angularPower;

		wheel = deadBand(wheel);
		throttle = deadBand(throttle);

		negInertia = wheel - mOldWheel;
		mOldWheel = wheel;

		wheel = sinScale(wheel, 0.9f, 1, 0.9f);
		negInertiaScalar = wheel * negInertia > 0 ? 2.5f : Math.abs(wheel) > .65 ? 6 : 4;
		negInertiaAccumulator = negInertia * negInertiaScalar;
		wheel += negInertiaAccumulator;

		if (altQuickTurn) {
			if (Math.abs(throttle) < 0.2) {
				double alpha = .1f;
				mQuickStopAccumulator = ((1 - alpha) * mQuickStopAccumulator) +
						(alpha * MathUtil.limit(wheel, 1.0) * 5);
			}
			overPower = -wheel * .75;
			angularPower = -wheel * 1;
		} else if (quickTurn) {
			if (Math.abs(throttle) < 0.2) {
				double alpha = .1f;
				mQuickStopAccumulator = ((1 - alpha) * mQuickStopAccumulator) +
						(alpha * MathUtil.limit(wheel, 1.0) * 5);
			}
			overPower = 1;
			angularPower = -wheel * 1;
		} else {
			overPower = 0;
			double sensitivity = .9;
			angularPower = throttle * wheel * sensitivity - mQuickStopAccumulator;
			mQuickStopAccumulator = wrap_accumulator(mQuickStopAccumulator);
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

		mReceiver.onDrive(leftPwm, rightPwm);
	}
}
