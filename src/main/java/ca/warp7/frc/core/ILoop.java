package ca.warp7.frc.core;

/**
 * Defines a loop mechanism
 *
 * @author Team 254
 */

@SuppressWarnings("WeakerAccess")
@FunctionalInterface
public interface ILoop {

	default void onStart() {
	}

	void onLoop();

	default void onStop() {
	}
}
