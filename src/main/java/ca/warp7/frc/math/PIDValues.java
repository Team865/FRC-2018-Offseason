package ca.warp7.frc.math;

@SuppressWarnings("WeakerAccess")
public class PIDValues {
	double P;
	double I;
	double D;
	double F;

	public PIDValues(double p, double i, double d, double f) {
		P = p;
		I = i;
		D = d;
		F = f;
	}

	public PIDValues(double p, double i, double d) {
		this(p, i, d, 0);
	}

	@Override
	public String toString() {
		return String.format("P: %s,I: %s,D: %s,F: %s", P, I, D, F);
	}
}
