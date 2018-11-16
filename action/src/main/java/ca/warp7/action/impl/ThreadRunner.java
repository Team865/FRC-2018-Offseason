package ca.warp7.action.impl;

import ca.warp7.action.IAction;

import java.util.Objects;

class ThreadRunner extends ActionBase {

    private IAction mAction;
    private Thread mRunThread;
    private long mInterval;
    private double mTimeout;
    private ITimer mTimer;

    private ThreadRunner(ITimer timer, double interval, double timeout, IAction action) {
        mTimer = timer;
        mAction = action;
        mInterval = (long) (interval * 1000);
        mTimeout = timeout;
    }

    static IAction create(ITimer timer, double interval, double timeout, IAction action) {
        Objects.requireNonNull(action);
        return new ThreadRunner(timer, interval, timeout, action);
    }

    @Override
    public Parent getParent() {
        // Return null so the resources are not shared in the hierarchy of actions
        return null;
    }

    @Override
    public void _onStart() {
        // Make sure autos are not running right now before continuing
        if (mRunThread != null) {
            System.err.println("ERROR a ThreadRunner is already running!!!");
            return;
        }

        getResources().setActionTimer(mTimer);

        // Check if a timer has already been assigned
        if (mTimer == null) mTimer = getResources().getActionTimer();

        // Operate on the action if it extends ActionBase
        if (mAction instanceof ActionBase) {
            ActionBase actionBase = (ActionBase) mAction;
            incrementDetachDepth(actionBase);
            Resources actionRes = actionBase.getResources();
            if (actionRes.getActionTimer() == null) actionRes.setActionTimer(getResources().getActionTimer());
        }

        // Create the thread;
        mRunThread = new Thread(() -> {
            System.out.println("Thread starting");
            double startTime = mTimer.getTime();
            double currentTime = startTime;
            mAction.onStart();

            // Loop forever until an exit condition is met
            // Stop priority #1: Check if the onStop method has been called to terminate this thread
            while (!Thread.currentThread().isInterrupted()) {
                currentTime = mTimer.getTime() - startTime;

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
            System.out.printf("ThreadRunner ending after %.3fs\n", currentTime);
            if (currentTime < mTimeout)
                System.out.printf("ERROR ThreadRunner ended early by %.3fs\n", mTimeout - currentTime);

            // Assign null to the thread so this runner can be called again
            // without robot code restarting
            mRunThread = null;
        });

        // Start the thread
        mRunThread.setDaemon(false);
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
