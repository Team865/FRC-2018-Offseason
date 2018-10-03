package ca.warp7.frc.core;

class AutoRunner {

	private IAutoAction mMainAction;
	private Thread mRunThread;

	AutoRunner() {
		mRunThread = null;
	}

	void setAutoMode(IAutoMode mode) {
		if (mRunThread == null) {
			mMainAction = mode.getMainAction();
		}
	}

	/**
	 * Start running the auto action
	 *
	 * @throws NoAutoModeException when the auto mode is null
	 */
	void onStart() throws NoAutoModeException {
		if (mMainAction == null) {
			throw new NoAutoModeException("No auto mode was specified!");
		}
		if (mRunThread == null) {
			mRunThread = new Thread(() -> {
				mMainAction.onStart();
				while (!Thread.currentThread().isInterrupted() && !mMainAction.shouldFinish()) {
					mMainAction.onUpdate();
					try {
						Thread.sleep((long) 20.0);
					} catch (InterruptedException e) {
						break;
					}
				}
				mMainAction.onStop();
			});
			mRunThread.start();
		}
	}

	void onStop() {
		if (mRunThread != null) {
			mRunThread.interrupt();
			mRunThread = null;
		}
	}
}
