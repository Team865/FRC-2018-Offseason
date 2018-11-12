package ca.warp7.frc.core;

/**
 * Essentially a wrapper to create an action, no other mechanisms
 * included. There are other classes that help with scheduling
 * mechanisms
 */

@FunctionalInterface
public interface IAutoMode extends IActionSupplier {

    /**
     * Procedure to fetch the main action of the mode
     *
     * @return the action
     */
    IActionSupplier getMainAction();

    @Override
    default IAction get() {
        return getMainAction().get();
    }
}
