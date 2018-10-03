package ca.warp7.frc.loop;

import ca.warp7.frc.core.Robot;

public class SimpleLoop implements ILoop {
	private String mName;
	private Runnable mLoopFunction;

	public SimpleLoop(String name, Runnable loopFunction) {
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
