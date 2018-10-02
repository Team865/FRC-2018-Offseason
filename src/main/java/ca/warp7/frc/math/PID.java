package ca.warp7.frc.math;

@SuppressWarnings({"unused", "WeakerAccess", "SpellCheckingInspection"})
public class PID {

	public static class InputState {

		private double measuredValue;
		private double targetValue;
		private double P;
		private double I;
		private double D;
		private double F;

		public void setMeasuredValue(double measuredValue) {
			this.measuredValue = measuredValue;
		}

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
			measuredValue = 0;
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

		public CurrentState() {
			mStateHandler = new MiniPID(0, 0, 0, 0);
		}

		public double calculate(InputState input) {
			mStateHandler.setPID(input.P, input.I, input.D, input.F);
			return mStateHandler.getOutput(input.measuredValue, input.targetValue);
		}

		public void reset() {
			mStateHandler.reset();
		}

		@Override
		public String toString() {
			return "MiniPID State";
		}
	}

}
