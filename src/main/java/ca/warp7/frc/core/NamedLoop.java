package ca.warp7.frc.core;

public class NamedLoop implements ILoop {
	private final String mName;
	private final Runnable mLoopFunction;

	NamedLoop(String name, Runnable loopFunction) {
		mName = name;
		mLoopFunction = loopFunction;
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onStop() {
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public void onLoop() {
		mLoopFunction.run();
	}
}
