package ca.warp7.frc.core;

import static ca.warp7.frc.core.Robot.utils;

public class SimpleLoop implements ILoop {
	private String mName;
	private Runnable mLoopFunction;

	SimpleLoop(String name, Runnable loopFunction) {
		mName = name;
		mLoopFunction = loopFunction;
	}

	@Override
	public void onStart() {
		utils.prefixedPrintln("Starting Loop: " + mName);
	}

	@Override
	public void onLoop() {
		mLoopFunction.run();
	}

	@Override
	public void onStop() {
		utils.prefixedPrintln("Stopping Loop: " + mName);
	}
}
