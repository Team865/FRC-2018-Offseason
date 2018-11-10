package ca.warp7.frc.commons.core;

public class RobotLoader {

    private IAutoMode mRunningMode;
    private double mTestTimeOut;
    private IControls mTeleop;
    private int[] mAvailableControls = {};

    public final void setTeleop(IControls loop, int... availableControls) {
        mTeleop = loop;
        mAvailableControls = availableControls;
    }

    public final void setAutoMode(IAutoMode mode, double testTimeout) {
        mRunningMode = mode;
        mTestTimeOut = testTimeout;
    }

    int[] getAvailableControls() {
        return mAvailableControls;
    }

    double getTestTimeOut() {
        return mTestTimeOut;
    }

    public IControls getTeleop() {
        return mTeleop;
    }

    IAutoMode getRunningMode() {
        return mRunningMode;
    }

    public static XboxControlsState createXboxController(int controllerPort) {
        return Robot.getState().createXboxController(controllerPort);
    }
}
