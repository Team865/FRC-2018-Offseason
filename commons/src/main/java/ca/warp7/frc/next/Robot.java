package ca.warp7.frc.next;

import ca.warp7.action.IAction;
import ca.warp7.frc.core.XboxControlsState;

public interface Robot {

    static void runAction(IAction action) {
    }

    static void runAction(OutputSystem system, IAction action) {
    }

    static XboxControlsState getController(int port, boolean isActive) {
        return null;
    }

    static void setInputSystems(InputSystem... inputSystems) {
    }

    static void setOutputSystems(OutputSystem... outputSystems) {
    }

    static void disable() {
    }

    static void initAutonomousMode(IAction.Mode mode, double timeout) {
    }

    static void initTeleop(ControlLoop teleopControls) {
    }

    static void initTest(ControlLoop testControls) {
    }
}
