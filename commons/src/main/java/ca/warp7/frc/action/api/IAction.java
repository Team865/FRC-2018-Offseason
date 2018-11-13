package ca.warp7.frc.action.api;

/**
 * <p>
 * An {@link IAction} defines any self contained action that can be executed by the robot.
 * An Action is the unit of basis for autonomous programs. Actions may contain anything,
 * which means we can run sub-actions in various ways, in combination with the start,
 * loo, end, and shouldFinish methods
 * </p>
 * <b>PLEASE NO WHILE LOOPS</b>
 */

@FunctionalInterface
public interface IAction {

    /**
     * Run code once when the action is started, usually for set up.
     * This method must be called first before shouldFinish is called.
     * <p>
     * This method is the only non-default one, making it a functional interface
     * that can be used t create singleton actions
     */
    void onStart();

    /**
     * Returns whether or not the code has finished execution.
     * <b>IMPORTANT:</b> We must make sure the changes in onStart
     * actually get applied to subsystems because updateState
     * will not run on the first call of this method after onStart
     *
     * @return boolean
     */
    default boolean shouldFinish() {
        return true;
    }

    /**
     * Called by runAction in AutoModeBase iteratively until isFinished returns true.
     * Iterative logic lives in this method. <b>PLEASE NO WHILE LOOPS</b>
     */
    default void onUpdate() {
    }

    /**
     * Run code once when the action finishes, usually for clean up
     */
    default void onStop() {
    }

    /**
     * An internal interface that keep track of time.
     * This makes the Action API independent of WPILib's timer api
     */
    @FunctionalInterface
    interface ITimer {
        /**
         * Gets the time since the Robot is started
         *
         * @return time in seconds
         */
        double getTime();
    }
}
