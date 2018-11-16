package ca.warp7.action.impl;

import ca.warp7.action.IAction;

import java.util.Objects;

class ThreadRunner extends ActionBase {

    private IAction mAction;
    private Thread mRunThread;
    private long mInterval;
    private double mTimeout;
    private ITimer mTimer;
    private boolean mVerbose;

    ThreadRunner(ITimer timer, double interval, double timeout, IAction action, boolean verbose) {
        Objects.requireNonNull(action);
        mTimer = timer;
        mAction = action;
        mInterval = (long) (interval * 1000);
        mTimeout = timeout;
        mVerbose = verbose;
    }

    @Override
    public Delegate getParent() {
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

        // Check if a timer has already been assigned
        if (mTimer == null) mTimer = getResources().getActionTimer();

        // Assign the timer to the parent actionBase
        getResources().setActionTimer(mTimer);

        // Use a variable to better name the thread
        String actionName = null;

        // Operate on the action if it extends ActionBase
        if (mAction instanceof ActionBase) {
            link(this, mAction);
            ActionBase actionBase = (ActionBase) mAction;
            incrementDetachDepth(actionBase);
            Resources actionRes = actionBase.getResources();
            if (actionRes.getActionTimer() == null) actionRes.setActionTimer(mTimer);
            if (!actionBase.getName().isEmpty()) actionName = actionBase.getName();
        }

        // Give the action its class name if it is not given another name
        if (actionName == null) actionName = mAction.getClass().getSimpleName();
        String threadName = String.format("ActionRunner[%d:%s]", getDetachDepth() + 1, actionName);

        // Create the thread;
        mRunThread = new Thread(() -> {
            if (mVerbose) System.out.printf("Thread %s starting\n", threadName);
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

            // Print out info about the
            if (mVerbose) {
                if (currentTime < mTimeout)
                    System.out.printf("Thread %s ended early by %.3fs\n", threadName, mTimeout - currentTime);
                else System.out.printf("Thread %s ending after %.3fs\n", threadName, currentTime);
            }

            // Assign null to the thread so this runner can be called again
            // without robot code restarting
            mRunThread = null;
        });

        // Start the thread
        mRunThread.setDaemon(false);
        mRunThread.setName(threadName);
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
