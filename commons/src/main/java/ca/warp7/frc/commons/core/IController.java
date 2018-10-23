package ca.warp7.frc.commons.core;

/**
 * Defines a controller that can be called by {@link Components}
 */
public interface IController {
    default void onUpdateData() {
    }
}
