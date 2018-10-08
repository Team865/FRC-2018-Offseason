package ca.warp7.frc.math;

import ca.warp7.frc.values.PIDValues;

@SuppressWarnings({"unused", "WeakerAccess"})
public class PID {
	public static class InputState {
		private double targetValue;
		private double P;
		private double I;
		private double D;
		private double F;

		public void setTargetValue(double targetValue) {
			this.targetValue = targetValue;
		}

		public InputState setPID(double p, double i, double d, double f) {
			P = p;
			I = i;
			D = d;
			F = f;
			return this;
		}

		public InputState setPID(double p, double i, double d) {
			return setPID(p, i, d, 0);
		}

		public InputState setPID(PIDValues values) {
			return setPID(values.P, values.I, values.D, values.F);
		}

		public void reset() {
			targetValue = 0;
			setPID(0, 0, 0, 0);
		}

		@Override
		public String toString() {
			return String.format("P: %s,  I: %s,  D: %s,  F: %s", P, I, D, F);
		}
	}

	public static class CurrentState {
		private MiniPID mStateHandler;
		private double mMeasuredValue;

		public CurrentState() {
			mStateHandler = new MiniPID(0, 0, 0, 0);
			mMeasuredValue = 0;
		}

		public void setMeasuredValue(double measuredValue) {
			mMeasuredValue = measuredValue;
		}

		public double calculate(InputState input) {
			mStateHandler.setPID(input.P, input.I, input.D, input.F);
			return mStateHandler.getOutput(mMeasuredValue, input.targetValue);
		}

		public void reset() {
			mMeasuredValue = 0;
			mStateHandler.reset();
		}

		@Override
		public String toString() {
			return String.format("Measured Value: %.3f", mMeasuredValue);
		}
	}

}
