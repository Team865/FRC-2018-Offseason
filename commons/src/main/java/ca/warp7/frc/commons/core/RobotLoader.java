package ca.warp7.frc.commons.core;

public class RobotLoader {

    private IAutoMode mRunningMode;
    private double mTestTimeOut;

    public final void setAutoMode(IAutoMode mode, double testTimeout) {
        mRunningMode = mode;
        mTestTimeOut = testTimeout;
    }

    double getTestTimeOut() {
        return mTestTimeOut;
    }

    IAutoMode getRunningMode() {
        return mRunningMode;
    }

    public static XboxControlsState createXboxController(int controllerPort, boolean available) {
        return Robot.getState().createXboxController(available ? controllerPort : -1);
    }
}
