package ca.warp7.frc.core;

/**
 * An interface that describes an iterative action. It is run by an autonomous action, called by the
 * method runAction in AutoModeBase (or more commonly in autonomous modes that extend AutoModeBase)
 *
 * @author Team 254, modified by Team 865
 */

public interface IAutonomousAction {
	/**
	 * Returns whether or not the code has finished execution. When implementing this interface, this method is used by
	 * the runAction method every cycle to know when to stop running the action
	 *
	 * @return boolean
	 */
	boolean actionShouldFinish();

	/**
	 * Called by runAction in AutoModeBase iteratively until isFinished returns true. Iterative logic lives in this
	 * method
	 */
	void onUpdate();

	/**
	 * Run code once when the action finishes, usually for clean up
	 */
	void onStop();

	/**
	 * Run code once when the action is started, for set up
	 */
	void onStart();
}
