package ca.warp7.frc.commons.core;

import ca.warp7.frc.commons.scheduler.IAction;
import edu.wpi.first.wpilibj.Timer;

/**
 * Runner class for the auto mode, capable of starting
 * and stopping actions, on a separate thread
 */

class AutoRunner {

    static final double kMaxAutoTimeoutSeconds = 15;

    private static final long kAutoLoopDeltaMilliseconds = 20;

    static class NoAutoException extends Exception {
    }

    /**
     * The main action to run, extracted from the mode
     */
    private IAction mMainAction;

    /**
     * The thread that autos are run on. If this is null, then no autos are or should be running
     */
    private Thread mRunThread = null;

    /**
     * Sets an explicit timeout to the auto phase for safety and testing
     */
    private double mExplicitTimeout;

    /**
     * When true, doesn't end auto unless the driver station calls teleopInit
     */
    private boolean mOverrideExplicitTimeout;

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

                System.out.println("Auto starting");

                double startTime = Timer.getFPGATimestamp();

                mMainAction.onStart();

                // Loop forever until an exit condition is met
                while (true) {

                    // Stop priority #1: Check if the onStop method has been called to terminate this thread
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }

                    // Stop priority #2: Check for explicit timeouts used in setAutoMode
                    if (!mOverrideExplicitTimeout) {
                        if ((Timer.getFPGATimestamp() - startTime) >= mExplicitTimeout) {
                            break;
                        }
                    }

                    // Stop priority #3: Check if the action should finish
                    // Note the main action may have recursive actions under it and all of those actions
                    // should contribute to this check
                    if (mMainAction.shouldFinish()) {
                        break;
                    }

                    // Update the action now after no exit conditions are met
                    mMainAction.onUpdate();

                    try {

                        // Delay for 20ms so the update function is not called so often
                        Thread.sleep(kAutoLoopDeltaMilliseconds);

                    } catch (InterruptedException e) {

                        // Breaks out the loop instead of returning so that onStop can be called
                        break;

                    }
                }

                mMainAction.onStop();

                System.out.println(String.format("Auto end after %.2fs", Timer.getFPGATimestamp() - startTime));

                // Assign null to the thread so this runner can be called again
                mRunThread = null;
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
        }
    }

    /**
     * Sets the auto mode
     *
     * @param mode           the {@link IAutoMode} that provides the main action
     * @param timeOutSeconds the number of seconds before the auto program times out
     */
    void setAutoMode(IAutoMode mode, double timeOutSeconds) {

        // Make sure that autos are not currently running
        // And make sure mode doesn't throw a NullPointerException
        if (mRunThread == null && mode != null) {

            mMainAction = mode.getMainAction();
            mExplicitTimeout = timeOutSeconds;

            // Wait for Driver Station only if timeout is infinity
            mOverrideExplicitTimeout = mExplicitTimeout == Double.POSITIVE_INFINITY;

            if (mExplicitTimeout >= kMaxAutoTimeoutSeconds || mExplicitTimeout < 0) {
                mExplicitTimeout = kMaxAutoTimeoutSeconds;
            }

        } else {
            // Now onStart will throw NoAutosException
            mMainAction = null;
        }
    }
}
