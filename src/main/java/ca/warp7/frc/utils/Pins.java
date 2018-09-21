package ca.warp7.frc.utils;

public class Pins {
	private final int[] mPins;

	public Pins(int[] pins) {
		if (pins.length <= 0) throw new AssertionError();
		mPins = pins;
	}

	public int get(int index){
		return mPins[index];
	}

	public int first(){
		return mPins[0];
	}

	public int[] array(){
		return mPins;
	}
}
