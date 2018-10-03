package ca.warp7.frc.loop;

public class BooleanLoop extends SimpleLoop {
	private boolean mLoopEnabled;

	public BooleanLoop(String name, Runnable loopFunction) {
		super(name, loopFunction);
		mLoopEnabled = false;
	}

	@Override
	public void onLoop() {
		if (mLoopEnabled) {
			super.onLoop();
		}
	}

	public void setEnabled(boolean enabled) {
		mLoopEnabled = enabled;
	}
}
