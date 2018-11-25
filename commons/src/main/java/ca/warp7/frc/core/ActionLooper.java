package ca.warp7.frc.core;

import ca.warp7.action.IAction;
import edu.wpi.first.wpilibj.Notifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs a list of loops using the {@link Notifier}
 *
 * @author Team 254, modified by Team 865
 */

class ActionLooper {
    private final Notifier mNotifier;
    private final Object mTaskRunningLock;
    private final double mInterval;
    private List<IAction> mLoops;
    private boolean mIsRunning;

    ActionLooper(double delta) {
        mTaskRunningLock = new Object();
        mNotifier = new Notifier(() -> {
            try {
                synchronized (mTaskRunningLock) {
                    if (mIsRunning) mLoops.forEach(IAction::update);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        mIsRunning = false;
        mLoops = new ArrayList<>();
        mInterval = delta;
    }

    synchronized void registerLoop(IAction loop) {
        synchronized (mTaskRunningLock) {
            mLoops.add(loop);
        }
    }

    synchronized void startLoops() {
        if (!mIsRunning) {
            synchronized (mTaskRunningLock) {
                mLoops.forEach(IAction::start);
                mIsRunning = true;
            }
            mNotifier.startPeriodic(mInterval);
        }
    }

    synchronized void stopLoops() {
        if (mIsRunning) {
            mNotifier.stop();
            synchronized (mTaskRunningLock) {
                mIsRunning = false;
                mLoops.forEach(IAction::stop);
            }
        }
    }
}
