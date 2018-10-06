package ca.warp7.frc.scheduler;

/**
 * An interface that describes an iterative action. It is run by an autonomous action, called by the
 * method runAction in AutoModeBase (or more commonly in autonomous modes that extend AutoModeBase)
 *
 * @author Team 254, modified by Team 865
 */

public interface IAction {

	/**
	 * Run code once when the action is started, for set up
	 */
	void onStart();

	/**
	 * Returns whether or not the code has finished execution.
	 *
	 * @return boolean
	 */
	boolean shouldFinish();

	/**
	 * Called by runAction in AutoModeBase iteratively until isFinished returns true. Iterative logic lives in this
	 * method
	 */
	void onUpdate();

	/**
	 * Run code once when the action finishes, usually for clean up
	 */
	void onStop();
}
