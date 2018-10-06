package ca.warp7.frc.core;

import ca.warp7.frc.scheduler.IAction;

/**
 * Essentially a wrapper to get an action, no other mechanisms
 * included. There are other classes that help with scheduling
 * mechanisms such as {@link ca.warp7.frc.scheduler.ScheduledMode}
 * and {@link ca.warp7.frc.scheduler.ActionSeries}
 */
public interface IAutoMode {

	/**
	 * Procedure to fetch the main action of the mode
	 *
	 * @return the action
	 */
	IAction getMainAction();
}
