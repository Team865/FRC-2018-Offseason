package ca.warp7.frc.core;

public class SimpleLoop implements ILoop {
	private Runnable mLoopFunction;

	SimpleLoop(Runnable loopFunction) {
		mLoopFunction = loopFunction;
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onLoop() {
		mLoopFunction.run();
	}

	@Override
	public void onStop() {
	}
}
