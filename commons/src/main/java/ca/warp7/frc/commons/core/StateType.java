package ca.warp7.frc.commons.core;

public enum StateType {
    /**
     * Reflects the input state of a subsystem
     */
    SUBSYSTEM_INPUT,

    /**
     * Reflects the current state of a subsystem
     */
    SUBSYSTEM_STATE,

    /**
     * Prints a line
     */
    PRINTLN,

    /**
     * Prints a warning
     */

    WARNING_PRINTLN,

    /**
     * Prints an error
     */
    ERROR_PRINTLN
}
