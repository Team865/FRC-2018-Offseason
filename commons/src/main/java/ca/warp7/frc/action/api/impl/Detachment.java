package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.def.IActionResources;
import ca.warp7.frc.core.IAction;

class Detachment extends BaseAction {

    private IAction mAction;
    private Thread mRunThread;
    private long mDetachedInterval;
    private double mTimeout;

    Detachment(double detachedInterval, double timeout, IAction action) {
        mAction = action;
        mDetachedInterval = (long) (detachedInterval * 1000);
        mTimeout = timeout;
    }

    @Override
    public void _onStart() {
        // Make sure autos are not running right now before continuing
        if (mRunThread != null) {
            System.err.println("ERROR Detached Thread is already running!!!");
            return;
        }

        // Make sure a valid action is returned by the mode
        if (mAction == null) {
            System.err.println("WARNING there isn't an action to run!!!");
            return;
        }

        IActionResources res = getResources();

        // Create and start the thread;
        mRunThread = new Thread(() -> {
            System.out.println("Detached Thread starting");
            double startTime = res.getTime();
            double currentTime = startTime;
            mAction.onStart();

            // Loop forever until an exit condition is met
            // Stop priority #1: Check if the onStop method has been called to terminate this thread
            while (!Thread.currentThread().isInterrupted()) {
                currentTime = res.getTime() - startTime;

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
                    Thread.sleep(mDetachedInterval);
                } catch (InterruptedException e) {
                    // Breaks out the loop instead of returning so that onStop can be called
                    break;
                }
            }

            mAction.onStop();
            System.out.printf("Detached Thread ending after %.3fs\n", currentTime);
            if (currentTime < mTimeout) {
                System.out.printf("ERROR Detached Thread ended early by %.3fs\n", mTimeout - currentTime);
            }

            // Assign null to the thread so this runner can be called again
            // without robot code restarting
            mRunThread = null;
        });

        mRunThread.start();
    }

    @Override
    public boolean shouldFinish() {
        return mRunThread == null;
    }

    @Override
    public void _onStop() {
        if (mRunThread != null) {
            mRunThread.interrupt();
        }
    }

    @Override
    void _onUpdate() {
    }
}
