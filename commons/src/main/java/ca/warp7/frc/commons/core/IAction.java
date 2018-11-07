package ca.warp7.frc.commons.core;

/**
 * An interface that describes an iterative action that has a start, loop, and end.
 * An Action is the unit of basis for autonomous programs. It may run a list of other
 * Actions as part of its program
 *
 * @author Team 254, modified by Team 865
 */

public interface IAction {

    /**
     * Run code once when the action is started, usually for set up
     */
    default void onStart() {
    }

    /**
     * Returns whether or not the code has finished execution.
     * <b>IMPORTANT:</b> We must make sure the changes in onStart
     * actually get applied to subsystems because {@link ISubsystem#onUpdateState()}
     * will not run on the first call of this method after onStart
     *
     * @return boolean
     */
    boolean shouldFinish();

    /**
     * Called by runAction in AutoModeBase iteratively until isFinished returns true.
     * Iterative logic lives in this method
     */
    default void onUpdate() {
    }

    /**
     * Run code once when the action finishes, usually for clean up
     */
    default void onStop() {
    }

    /**
     * Used to determine if the action wants to control the loop sleep time
     */
    default boolean shouldAcceptLoopManagement() {
        return false;
    }

    /**
     * Called for the action to sleep before updating
     */
    default void onSleep() throws InterruptedException {
        Thread.sleep(0);
    }

    /**
     * Used to determine if the action can estimate the amount of time it will
     * take to complete itself
     */
    default boolean shouldPredictActionDuration() {
        return false;
    }

    /**
     * If the action can predict it, get the predicted duration in seconds
     */
    default double getPredictedActionDuration() {
        return 0;
    }
}