package ca.warp7.frc.core;

import edu.wpi.first.wpilibj.Notifier;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Team 254, modified by Team 865
 */

class Looper {
	private final Notifier mNotifier;
	private final List<ILoop> mLoops;
	private ILoop mStartLoop;
	private ILoop mFinalLoop;
	private final Object mTaskRunningLock;

	private boolean mIsRunning;
	private double mPeriod;

	Looper(double delta) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					synchronized (mTaskRunningLock) {
						if (mIsRunning) {
							if (mStartLoop != null) mStartLoop.onLoop();
							mLoops.forEach(ILoop::onLoop);
							if (mFinalLoop != null) mFinalLoop.onLoop();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		mTaskRunningLock = new Object();
		mNotifier = new Notifier(runnable);
		mIsRunning = false;
		mLoops = new ArrayList<>();
		mPeriod = delta;
	}

	synchronized void registerLoop(ILoop loop) {
		synchronized (mTaskRunningLock) {
			mLoops.add(loop);
		}
	}

	synchronized void registerStartLoop(ILoop startLoop) {
		synchronized (mTaskRunningLock) {
			mStartLoop = startLoop;
		}
	}

	synchronized void registerFinalLoop(ILoop finalLoop) {
		synchronized (mTaskRunningLock) {
			mFinalLoop = finalLoop;
		}
	}

	synchronized void startLoops() {
		if (!mIsRunning) {
			synchronized (mTaskRunningLock) {
				if (mStartLoop != null) mStartLoop.onStart();
				mLoops.forEach(ILoop::onStart);
				if (mFinalLoop != null) mFinalLoop.onStart();
				mIsRunning = true;
			}
			mNotifier.startPeriodic(mPeriod);
		}
	}

	synchronized void stopLoops() {
		if (mIsRunning) {
			mNotifier.stop();
			synchronized (mTaskRunningLock) {
				mIsRunning = false;
				if (mStartLoop != null) mStartLoop.onStop();
				mLoops.forEach(ILoop::onStop);
				if (mFinalLoop != null) mFinalLoop.onStop();
			}
		}
	}
}
