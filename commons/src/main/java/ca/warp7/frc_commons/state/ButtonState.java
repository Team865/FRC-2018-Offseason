package ca.warp7.frc_commons.state;

/**
 * The state of a controller button
 */
public enum ButtonState {

	/**
	 * Indicates the button was kept up for more than one iteration
	 */
	KEPT_UP,

	/*
	Indicates the button was held down for more than one iteration
	 */
	HELD_DOWN,

	/**
	 * Indicates the button was just pressed
	 */
	PRESSED,

	/**
	 * Indicates the button was just released
	 */
	RELEASED
}
