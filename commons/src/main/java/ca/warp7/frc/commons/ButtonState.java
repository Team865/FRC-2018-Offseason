package ca.warp7.frc.commons;

/**
 * The state of a controller button
 */
@Deprecated
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