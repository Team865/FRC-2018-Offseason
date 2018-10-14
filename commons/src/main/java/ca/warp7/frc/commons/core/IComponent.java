package ca.warp7.frc.commons.core;

/**
 * Provides an interface of deferred construction for robot components
 */

public interface IComponent {
    /**
     * <p>Called when constructing the component</p>
     * <p>See {@link ISubsystem#onConstruct()} for details</p>
     */
    void onConstruct();
}
