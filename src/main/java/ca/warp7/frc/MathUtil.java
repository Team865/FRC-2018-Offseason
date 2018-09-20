package ca.warp7.frc;

public class MathUtil {

	public static double limit(double val, double lim) {
		lim = Math.abs(lim);
		return Math.max(-lim, Math.min(val, lim));
	}

//	public static double correct_angle(double angle) {
//		return angle + 360 * Math.floor(0.5 - angle / 360);
//	}

}
