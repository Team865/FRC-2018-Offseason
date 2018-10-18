package ca.warp7.frc.commons.core;

import ca.warp7.frc.commons.scheduler.ActionSeries;
import ca.warp7.frc.commons.scheduler.IAction;
import ca.warp7.frc.commons.scheduler.ScheduleBuilder;

/**
 * Essentially a wrapper to create an action, no other mechanisms
 * included. There are other classes that help with scheduling
 * mechanisms such as {@link ScheduleBuilder}
 * and {@link ActionSeries}
 */
public interface IAutoMode {

    /**
     * Procedure to fetch the main action of the mode
     *
     * @return the action
     */
    IAction getMainAction();
}
