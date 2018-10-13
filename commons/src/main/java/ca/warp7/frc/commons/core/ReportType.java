package ca.warp7.frc.commons.core;

public enum ReportType {
	/**
	 * Reflects the input state of a subsystem
	 */
	REFLECT_STATE_INPUT,

	/**
	 * Reflects the current state of a subsystem
	 */
	REFLECT_STATE_CURRENT,

	/**
	 * Prints a line
	 */
	PRINT_LINE,

	/**
	 * Prints an error
	 */
	ERROR_PRINT_LINE
}
