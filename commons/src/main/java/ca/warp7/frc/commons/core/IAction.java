package ca.warp7.frc.commons.core;

/**
 * An interface that describes an iterative action that has a start, loop, and end.
 * An Action is the unit of basis for autonomous programs. It may run a list of other
 * Actions as part of its program
 */

@FunctionalInterface
public interface IAction extends IActionSupplier {

    /**
     * Run code once when the action is started, usually for set up
     */
    void onStart();

    /**
     * Returns whether or not the code has finished execution.
     * <b>IMPORTANT:</b> We must make sure the changes in onStart
     * actually get applied to subsystems because {@link ISubsystem#onUpdateState()}
     * will not run on the first call of this method after onStart
     *
     * @return boolean
     */
    default boolean shouldFinish() {
        return true;
    }

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

    @Override
    default IAction get() {
        return this;
    }
}
