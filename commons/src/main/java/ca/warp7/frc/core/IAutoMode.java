package ca.warp7.frc.core;

/**
 * Essentially a wrapper to create an action, no other mechanisms
 * included. There are other classes that help with scheduling
 * mechanisms
 */

@FunctionalInterface
public interface IAutoMode {

    /**
     * Procedure to fetch the main action of the mode
     *
     * @return the action
     */
    IAction getAction();
}
