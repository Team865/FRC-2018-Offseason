package ca.warp7.frc.core;

/**
 * Defines a loop mechanism
 */

@SuppressWarnings("WeakerAccess")
@FunctionalInterface
public interface ILoop {

    /**
     * Starts the loop
     */
    default void onStart() {
    }

    /**
     * Loops the loop
     */
    void onLoop();

    /**
     * Stops the loop
     */
    default void onStop() {
    }
}
