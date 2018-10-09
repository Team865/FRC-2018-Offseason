package ca.warp7.frc.core;

public class SimpleLoop implements ILoop {
	private final String mName;
	private final Runnable mLoopFunction;

	SimpleLoop(String name, Runnable loopFunction) {
		mName = name;
		mLoopFunction = loopFunction;
	}

	@Override
	public void onStart() {
		Robot.prefixedPrintln("Starting Loop: " + mName);
	}

	@Override
	public void onLoop() {
		mLoopFunction.run();
	}

	@Override
	public void onStop() {
		Robot.prefixedPrintln("Stopping Loop: " + mName);
	}
}
