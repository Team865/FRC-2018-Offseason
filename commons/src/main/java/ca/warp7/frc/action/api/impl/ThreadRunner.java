package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.def.IActionParent;
import ca.warp7.frc.action.api.def.IActionResources;
import ca.warp7.frc.action.api.def.IActionTimer;
import ca.warp7.frc.core.IAction;

import java.util.Objects;

class ThreadRunner extends BaseAction {

    @SuppressWarnings({"unused", "SameParameterValue"})
    static IAction create(IActionTimer timer, double interval, double timeout, BaseAction action) {
        Objects.requireNonNull(action);
        action.getResources().setActionTimer(timer);
        return new ThreadRunner(interval, timeout, action);
    }

    private BaseAction mAction;
    private Thread mRunThread;
    private long mInterval;
    private double mTimeout;

    private ThreadRunner(double interval, double timeout, BaseAction action) {
        mAction = action;
        mInterval = (long) (interval * 1000);
        mTimeout = timeout;
    }

    @Override
    public IActionParent getParent() {
        return null;
    }

    @Override
    public void _onStart() {
        // Make sure autos are not running right now before continuing
        if (mRunThread != null) {
            System.err.println("ERROR a Thread is already running!!!");
            return;
        }

        IActionResources actionRes = mAction.getResources();
        if (actionRes.getActionTimer() == null) actionRes.setActionTimer(getResources().getActionTimer());
        incrementDetachDepth(mAction);

        // Create and start the thread;
        mRunThread = new Thread(() -> {
            System.out.println("Thread starting");
            double startTime = actionRes.getTime();
            double currentTime = startTime;
            mAction.onStart();

            // Loop forever until an exit condition is met
            // Stop priority #1: Check if the onStop method has been called to terminate this thread
            while (!Thread.currentThread().isInterrupted()) {
                currentTime = actionRes.getTime() - startTime;

                // Stop priority #2: Check for explicit timeouts used in setAutoMode
                if (currentTime >= mTimeout) break;

                // Stop priority #3: Check if the action should finish
                // Note the main action may have recursive actions under it and all of those actions
                // should contribute to this check
                if (mAction.shouldFinish()) break;

                // Update the action now after no exit conditions are met
                mAction.onUpdate();
                try {
                    // Delay for a certain amount of time so the update function is not called so often
                    Thread.sleep(mInterval);
                } catch (InterruptedException e) {
                    // Breaks out the loop instead of returning so that onStop can be called
                    break;
                }
            }

            mAction.onStop();
            System.out.printf("Thread ending after %.3fs\n", currentTime);
            if (currentTime < mTimeout)
                System.out.printf("ERROR Detached ended early by %.3fs\n", mTimeout - currentTime);

            // Assign null to the thread so this runner can be called again
            // without robot code restarting
            mRunThread = null;
        });

        mRunThread.start();
    }

    @Override
    public boolean _shouldFinish() {
        return mRunThread == null;
    }

    @Override
    public void _onStop() {
        if (mRunThread != null) mRunThread.interrupt();
    }
}
