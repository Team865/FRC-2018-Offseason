package ca.warp7.frc.core;

import ca.warp7.frc.scheduler.IAction;

/**
 * Runner class for the auto mode, capable of starting
 * and stopping actions, on a separate thread
 */

class AutoRunner {

	private static final long kAutoLoopDeltaMilliseconds = 20;

	/**
	 * The main action to run, extracted from the mode
	 */
	private IAction mMainAction;

	/**
	 * The thread that autos are run on.
	 * If this is null, then no autos are
	 * or should be running
	 */
	private Thread mRunThread;

	AutoRunner() {
		mRunThread = null;
	}

	void setAutoMode(IAutoMode mode) {
		// Make sure that autos are not currently running
		// And make sure mode doesn't throw a NullPointerException
		if (mRunThread == null && mode != null) {
			mMainAction = mode.getMainAction();
		}
	}

	/**
	 * Start running the auto action.
	 * <p>
	 * The mechanism which each action is running on means that
	 * there cannot be blocking operations in both
	 * {@link IAction#onUpdate()} and {@link IAction#onStop()}
	 * or auto may not end on time</p>
	 * <p>
	 * The proper code mechanism should use implement {@link IAction}
	 * for a monitoring/locking purpose, and the actual feed forward
	 * and feedback loops should be run instead in the IO loops. This
	 * would also make the actual periodic delay not very relevant</p>
	 *
	 * @throws NoAutoException when the auto mode is null
	 */
	void onStart() throws NoAutoException {

		// Make sure an auto exists
		if (mMainAction == null) {
			throw new NoAutoException();
		}

		// Make sure autos are not running right now
		if (mRunThread == null) {

			mRunThread = new Thread(() -> {

				mMainAction.onStart();

				// If the thread is interrupted, it checks both during the start of the
				// while loop and while it's running Thread.sleep

				while (!Thread.currentThread().isInterrupted() && !mMainAction.shouldFinish()) {
					mMainAction.onUpdate();
					try {
						Thread.sleep(kAutoLoopDeltaMilliseconds);
					} catch (InterruptedException e) {
						// Breaks out the loop instead of returning so that onStop can be called
						break;
					}
				}

				mMainAction.onStop();

			});

			mRunThread.start();
		}
	}

	/**
	 * Stops the thread if it is running and nullify the variables
	 */
	void onStop() {
		if (mRunThread != null) {
			mRunThread.interrupt();
			mMainAction = null;
		}
	}
}
