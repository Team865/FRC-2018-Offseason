package ca.warp7.frc.math;

public class Functions {
	public static double limit(double val, double lim) {
		lim = Math.abs(lim);
		return Math.max(-lim, Math.min(val, lim));
	}

	public static double constrainMin(double diff, double tolerance) {
		return Math.abs(diff) > Math.abs(tolerance) ? diff : 0;
	}
}
