package ca.warp7.frc.scheduler;

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
}
