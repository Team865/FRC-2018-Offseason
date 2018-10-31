package ca.warp7.frc.commons.core;

import ca.warp7.frc.commons.scheduler.CompositeAction;

/**
 * Essentially a wrapper to create an action, no other mechanisms
 * included. There are other classes that help with scheduling
 * mechanisms such as {@link CompositeAction}
 */
public interface IAutoMode {

    /**
     * Procedure to fetch the main action of the mode
     *
     * @return the action
     */
    IAction getMainAction();

    /**
     * Returns whether the auto mode have different user-selectable configurations
     */
    default boolean isConfigurable() {
        return false;
    }

    /**
     * Get a post fix identifier so the user can properly select them
     */
    default String getConfigurationPostfix() {
        return isConfigurable() ? String.valueOf((int) (Math.random() * 1000)) : "";
    }
}
