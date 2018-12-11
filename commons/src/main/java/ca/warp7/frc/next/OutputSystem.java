package ca.warp7.frc.next;

public interface OutputSystem {

    /**
     * <p>Called when the robot is disabled</p>
     *
     * <p>This method should reset everything having to do with output so as to put
     * the subsystem in a disabled state</p>
     */
    default void onDisabled() {
    }

    /**
     * <p>Called at the start of auto for initial setup</p>
     *
     * <p>Auto loops don't start until this method returns, therefore the implementation
     * must execute quickly</p>
     */
    default void onAutonomousInit() {
    }

    /**
     * <p>Called at the start of Teleop for initial setup</p>
     *
     * <p>Teleop loops don't start until this method returns, therefore the implementation
     * must execute quickly</p>
     */
    default void onTeleopInit() {
    }

    /**
     * <p>Called periodically for the subsystem to send outputs to its output device.
     * This method is called from the State Change Looper.</p>
     *
     * <p>This method is guaranteed to not be called when the robot is disabled.
     * Any output limits should be applied here for safety reasons.</p>
     */
    default void onOutput() {
    }
}
