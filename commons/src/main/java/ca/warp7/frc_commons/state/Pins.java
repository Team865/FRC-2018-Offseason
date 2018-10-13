package ca.warp7.frc_commons.state;

public class Pins {
	private final int[] mPinsArray;

	private Pins(int[] pins) {
		mPinsArray = pins;
	}

	public static Pins pins(int... n) {
		return new Pins(n);
	}

	public static Pins channels(int... n) {
		return pins(n);
	}

	public static Pins pin(int n) {
		return pins(n);
	}

	public int get(int index) {
		return mPinsArray[index];
	}

	public int first() {
		return mPinsArray[0];
	}

	public int[] array() {
		return mPinsArray;
	}
}
