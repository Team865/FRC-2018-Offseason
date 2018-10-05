package ca.warp7.frc.core;

import ca.warp7.frc.scheduler.IAction;

class AutoRunner {

	private IAction mMainAction;
	private Thread mRunThread;

	AutoRunner() {
		mRunThread = null;
	}

	void setAutoMode(IAutoMode mode) {
		if (mRunThread == null && mode != null) {
			mMainAction = mode.getMainAction();
		}
	}

	/**
	 * Start running the auto action
	 *
	 * @throws NoAutoException when the auto mode is null
	 */

	void onStart() throws NoAutoException {
		if (mMainAction == null) {
			throw new NoAutoException("The auto mode will do nothing!");
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
