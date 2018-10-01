package ca.warp7.frc.core;

public class Pins {
	private final int[] mPinsArray;

	Pins(int[] pins) {
		mPinsArray = pins;
	}

	int get(int index) {
		return mPinsArray[index];
	}

	public int first() {
		return mPinsArray[0];
	}

	public int[] array() {
		return mPinsArray;
	}
}
