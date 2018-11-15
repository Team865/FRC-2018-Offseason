package ca.warp7.frc.core;

import ca.warp7.action.IActionMode;

public class RobotLoader {

    private IActionMode mRunningMode;
    private double mTestTimeOut;

    public final void setAutoMode(IActionMode mode, double testTimeout) {
        mRunningMode = mode;
        mTestTimeOut = testTimeout;
    }

    double getTestTimeOut() {
        return mTestTimeOut;
    }

    IActionMode getRunningMode() {
        return mRunningMode;
    }

    public static XboxControlsState createXboxController(int controllerPort, boolean available) {
        return Robot.getState().createXboxController(available ? controllerPort : -1);
    }
}
