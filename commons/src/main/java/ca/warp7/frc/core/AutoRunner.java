package ca.warp7.frc.core;

import ca.warp7.action.IAction;
import ca.warp7.action.impl.ActionMode;
import edu.wpi.first.wpilibj.Timer;

/**
 * Runner class for the auto mode, capable of starting
 * and stopping actions, on a separate thread
 */

class AutoRunner {

    private static final double kMaxAutoTimeoutSeconds = 15;
    private static final long kLoopDelta = 20;

    private IAction mActionRunner;

    /**
     * Get input from the Robot Loader and starts the auto
     */
    void start() {

        // Get the auto mode that can create the action on demand
        IAction.Mode autoMode = Robot.loader.getRunningMode();

        // Make sure there is a mode to create the main action from
        if (autoMode == null) {
            System.err.println("ERROR There is no Auto Mode!!!");
            return;
        }

        // Use the mode to create the actual action to run
        IAction mAction = autoMode.getAction();

        // Make sure a valid action is returned by the mode
        if (mAction == null) {
            System.err.println("WARNING there isn't an action!!!");
            return;
        }

        double timeout = Robot.loader.getTestTimeOut();

        // Limit the explicit timeout to make it reasonable
        if (timeout >= kMaxAutoTimeoutSeconds || timeout < 0) timeout = kMaxAutoTimeoutSeconds;

        // Create and start the runner
        mActionRunner = ActionMode.createRunner(Timer::getFPGATimestamp, timeout, kLoopDelta, mAction, true);
        mActionRunner.start();
    }

    /**
     * Stops the thread if it is running and nullify the variables
     */
    void stop() {
        mActionRunner.stop();
    }
}
