package ca.warp7.frc.commons.core;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

/**
 * Runner class for the auto mode, capable of starting
 * and stopping actions, on a separate thread
 */

class AutoRunner {

    private static final double kMaxAutoTimeoutSeconds = 15;
    private static final long kDefaultLoopDelta = 20;

    /**
     * The auto mode that can create the action on demand
     */
    private IAutoMode mAutoMode;

    /**
     * The main action to run, extracted from the mode
     */
    private IAction mAction;

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
     * The periodic Runnable for the auto program
     *
     * <p>The mechanism in which actions are running means that there cannot be blocking operations in
     * both {@link IAction#onUpdate()} and {@link IAction#onStop()} or auto may not end on time</p>
     *
     * <p>The proper code mechanism should use implement {@link IAction}for a monitoring/locking purpose,
     * and the actual IO loops should be run instead in the IO looper. This would also make the actual
     * periodic delay not very relevant</p>
     */
    private Runnable mPeriodicRunner = () -> {

        System.out.println("Auto starting");

        double startTime = Timer.getFPGATimestamp();

        mAction.onStart();

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
            if (mAction.shouldFinish()) {
                break;
            }

            // Update the action now after no exit conditions are met
            mAction.onUpdate();

            try {

                // Delay for a certain amount of time so the update function is not called so often
                if (mAction.shouldAcceptLoopManagement()) {
                    mAction.onSleep();
                } else {
                    Thread.sleep(kDefaultLoopDelta);
                }

            } catch (InterruptedException e) {

                // Breaks out the loop instead of returning so that onStop can be called
                break;

            }
        }

        mAction.onStop();

        System.out.printf("Auto program ending after %.3fs \n", Timer.getFPGATimestamp() - startTime);

        // Assign null to the thread so this runner can be called again
        // without robot code restarting
        mRunThread = null;
    };

    /**
     * Get input from the Robot Loader and starts the auto
     */
    void start() {

        // Make sure autos are not running right now before continuing
        if (mRunThread != null) {
            System.err.println("ERROR an auto program is already running!!!");
            return;
        }

        mAutoMode = Robot.loader.getRunningMode();

        // Make sure there is a mode to create the main action from
        if (mAutoMode == null) {
            System.err.println("ERROR There is no Auto Mode!!!");
            return;
        }

        // Wait for Driver Station only if timeout is infinity or the FMS is present
        boolean hasFMS = DriverStation.getInstance().isFMSAttached();

        mExplicitTimeout = Robot.loader.getTestTimeOut();
        mOverrideExplicitTimeout = hasFMS || mExplicitTimeout == Double.POSITIVE_INFINITY;

        // Limit the explicit timeout to make it reasonable
        if (mExplicitTimeout >= kMaxAutoTimeoutSeconds || mExplicitTimeout < 0) {
            mExplicitTimeout = kMaxAutoTimeoutSeconds;
        }

        // Use the mode to create the actual action to run
        // This is used so that the main action can be run multiple times
        // even if the robot code is not restarted
        mAction = mAutoMode.get();

        // Make sure a valid action is returned by the mode
        if (mAction == null) {
            System.err.println("WARNING there isn't a action returned by the mode!!!");
            return;
        }

        // Create and start the thread;
        mRunThread = new Thread(mPeriodicRunner);
        mRunThread.start();
    }

    /**
     * Stops the thread if it is running and nullify the variables
     */
    void stop() {
        if (mRunThread != null) {
            mRunThread.interrupt();
        }
    }
}
