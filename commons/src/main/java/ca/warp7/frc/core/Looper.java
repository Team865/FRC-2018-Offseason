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

class Looper {
    private final Notifier mNotifier;
    private final Object mTaskRunningLock;
    private final double mInterval;
    private List<Loop> mLoops;
    private boolean mIsRunning;

    Looper(double delta) {
        mTaskRunningLock = new Object();
        mNotifier = new Notifier(() -> {
            try {
                synchronized (mTaskRunningLock) {
                    if (mIsRunning) mLoops.forEach(Loop::update);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        mIsRunning = false;
        mLoops = new ArrayList<>();
        mInterval = delta;
    }

    synchronized void registerLoop(Loop loop) {
        synchronized (mTaskRunningLock) {
            mLoops.add(loop);
        }
    }

    synchronized void startLoops() {
        if (!mIsRunning) {
            synchronized (mTaskRunningLock) {
                mLoops.forEach(Loop::start);
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
                mLoops.forEach(Loop::stop);
            }
        }
    }

    /**
     * Defines a loop mechanism
     */
    @FunctionalInterface
    public interface Loop extends IAction {

        @Override
        default void start() {
        }

        @Override
        default boolean shouldFinish() {
            return false;
        }

        @Override
        void update();

        @Override
        default void stop() {
        }
    }
}
