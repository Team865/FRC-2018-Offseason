package ca.warp7.frc;

import ca.warp7.action.IAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Subsystem {

    protected Subsystem() {
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

    public class State {
        @Override
        public boolean equals(Object obj) {
            return false;
        }

        public void set(IAction action) {
            RobotRuntime.ROBOT_RUNTIME.setState(action, Subsystem.this);
        }

        public void setQueue(IAction... action) {
            RobotRuntime.ROBOT_RUNTIME.setState(action[0], Subsystem.this);
        }

        public void idle() {
            set(null);
        }
    }

    private IAction state;

    public void setState(IAction state) {
        this.state = state;
    }

    private final List<IAction> nextStates = new ArrayList<>();

    public List<IAction> getNextStates() {
        return nextStates;
    }

    public void resetNextStates(IAction... states) {
        idle();
        nextStates.clear();
        nextStates.addAll(Arrays.asList(states));
    }

    public void setQueue(IAction... action) {
        RobotRuntime.ROBOT_RUNTIME.setState(action[0], Subsystem.this);
    }

    public IAction getState() {
        return state;
    }

    public void idle() {
        state = null;
    }
}
