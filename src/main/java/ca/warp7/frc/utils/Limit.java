package ca.warp7.frc.utils;

public class Limit {
	public static double limit(double val, double lim) {
		lim = Math.abs(lim);
		return Math.max(-lim, Math.min(val, lim));
	}
}
