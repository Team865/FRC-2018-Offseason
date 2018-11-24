package ca.warp7.action.impl;

import ca.warp7.action.IAction;

import java.util.Objects;

class ActionRunner extends ActionBase {

    private IAction mAction;
    private Thread mRunThread;
    private long mInterval;
    private double mTimeout;
    private ITimer mTimer;
    private boolean mVerbose;

    ActionRunner(ITimer timer, double interval, double timeout, IAction action, boolean verbose) {
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
    public void prepare() {
        // Make sure autos are not running right now before continuing
        if (mRunThread != null) {
            System.err.println("ERROR an ActionRunner is already running!!!");
            return;
        }
        // Check if a timer has already been assigned
        if (mTimer == null) mTimer = getResources().getActionTimer();
        // Get the cast resources of current action
        Resources resources = (Resources) getResources();
        // Pass the timer to the resources
        resources.setActionTimer(mTimer);
        // Convert interval into seconds and pass to resources
        resources.setInterval(mInterval / 1000.0);
        // Use a variable to better name the thread
        String actionName = null;
        // Operate on the action if it extends ActionBase
        if (mAction instanceof ActionBase) {
            ActionBase actionBase = (ActionBase) mAction;
            // Link the runner to the action
            performSafeLink(this, actionBase);
            // Increment the detachment state of the child
            incrementDetachDepth(actionBase);
            // Fetch and store the resources pointer from the parent
            actionBase.getResources();
            // Get the action name if it exists
            if (!actionBase.getName().isEmpty()) actionName = actionBase.getName();
        }
        // Give the action its class name if it does not exist
        if (actionName == null) actionName = mAction.getClass().getSimpleName();
        // Create the thread name based on the action name
        String threadName = String.format("ActionRunner[%d:%s]", getDetachDepth() + 1, actionName);
        // Create a new run thread
        mRunThread = new Thread(() -> {
            if (mVerbose) System.out.printf("Thread %s starting\n", threadName);
            // measure the start time and start the action
            double startTime = mTimer.getTime();
            double currentTime = startTime;
            mAction.start();
            // Loop forever until an exit condition is met
            // Stop priority #1: Check if the stop method has been called to terminate this thread
            while (!Thread.currentThread().isInterrupted()) {
                currentTime = mTimer.getTime() - startTime;
                // Stop priority #2: Check for explicit timeouts used in setAutoMode
                // Stop priority #3: Check if the action should finish
                // Note the main action may have recursive actions under it and all of those actions
                // should contribute to this check
                if (currentTime >= mTimeout || mAction.shouldFinish()) break;
                // Update the action now after no exit conditions are met
                mAction.update();
                try {
                    // Delay for a certain amount of time so the update function is not called so often
                    Thread.sleep(mInterval);
                } catch (InterruptedException e) {
                    // Breaks out the loop instead of returning so that stop can be called
                    break;
                }
            }
            mAction.stop();
            // Print out info about the execution if verbose
            if (mVerbose) {
                if (currentTime < mTimeout) System.out.printf("ActionRunner %s ended early by %.3fs\n",
                        threadName, mTimeout - currentTime);
                else System.out.printf("ActionRunner %s ending after %.3fs\n", threadName, currentTime);
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
    public void stop() {
        if (mRunThread != null) mRunThread.interrupt();
    }
}
