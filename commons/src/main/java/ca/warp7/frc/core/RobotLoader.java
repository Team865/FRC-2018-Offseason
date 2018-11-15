package ca.warp7.frc.core;

import ca.warp7.action.IAction;

public class RobotLoader {

    private IAction.Mode mRunningMode;
    private double mTestTimeOut;

    public final void setAutoMode(IAction.Mode mode, double testTimeout) {
        mRunningMode = mode;
        mTestTimeOut = testTimeout;
    }

    double getTestTimeOut() {
        return mTestTimeOut;
    }

    IAction.Mode getRunningMode() {
        return mRunningMode;
    }

    public static XboxControlsState createXboxController(int controllerPort, boolean available) {
        return Robot.getState().createXboxController(available ? controllerPort : -1);
    }
}
