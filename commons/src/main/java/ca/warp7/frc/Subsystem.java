package ca.warp7.frc;

import ca.warp7.action.IAction;

public abstract class Subsystem {

    protected Subsystem() {
        RobotRuntime.ROBOT_RUNTIME.registerSubsystem(this);
    }

    /**
     * <p>Called when the robot is disabled</p>
     *
     * <p>This method should reset everything having to do with output so as to put
     * the subsystem in a disabled state</p>
     */
    public void onDisabled() {
    }


    public void onIdle() {
        onDisabled();
    }

    /**
     * <p>Called periodically for the subsystem to send outputs to its output device.
     * This method is called from the State Change Looper.</p>
     *
     * <p>This method is guaranteed to not be called when the robot is disabled.
     * Any output limits should be applied here for safety reasons.</p>
     */
    public void onOutput() {
    }

    void update() {
        if (state == null) onIdle();
        else state.update();
    }

    private IAction state;

    public void setState(IAction state) {
        this.state = state;
    }

    public IAction getState() {
        return state;
    }

    public void idle() {
        state = null;
    }
}
