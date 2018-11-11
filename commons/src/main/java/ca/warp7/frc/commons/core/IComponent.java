package ca.warp7.frc.commons.core;

/**
 * Provides an interface of deferred construction for robot components
 */

@FunctionalInterface
public interface IComponent {
    /**
     * <p>Called when constructing the subsystem</p>
     *
     * <p>This method should connect any hardware components such as motors, gyros,
     * and encoders and perform any initial settings such as their direction.
     * This method should be used instead of class constructors since subsystems are often
     * created statically and it is preferred to initialize the systems in the proper order.
     * In other words, this is a "deferred" constructor</p>
     *
     * <p>There are some special cases where it is necessary to configure a system a certain
     * way as soon as possible after the program starts (e.g. charging pneumatics). In these
     * cases it is okay to do that in this method</p>
     */
    void onConstruct();
}
