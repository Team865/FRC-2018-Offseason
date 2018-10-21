package ca.warp7.frc.commons.core;

/**
 * Defines a loop mechanism
 */

@SuppressWarnings("WeakerAccess")
@FunctionalInterface
public interface ILoop {

    /**
     * @return The name of the loop
     */
    default String getName() {
        return getClass().getSimpleName();
    }

    /**
     * Starts the loop
     */
    default void onStart() {
        Robot.println("Starting Loop: " + getName());
    }

    /**
     * Loops the loop
     */
    void onLoop();

    /**
     * Stops the loop
     */
    default void onStop() {
        Robot.println("Stopping Loop: " + getName());
    }
}
