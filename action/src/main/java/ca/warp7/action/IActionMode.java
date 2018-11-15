package ca.warp7.action;

/**
 * Essentially a wrapper to create an action, no other mechanisms
 * included. There are other classes that help with scheduling
 * mechanisms
 */

@FunctionalInterface
public interface IActionMode {

    /**
     * Procedure to fetch the main action of the mode
     *
     * @return the action
     */
    IAction getAction();
}
