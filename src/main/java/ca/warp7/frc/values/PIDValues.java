package ca.warp7.frc.values;

@SuppressWarnings("WeakerAccess")
public class PIDValues {
	public final double P;
	public final double I;
	public final double D;
	public final double F;

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
