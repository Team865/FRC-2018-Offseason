package ca.warp7.frc.commons.core;

public class RobotLoader {

    private IAutoMode mRunningMode;
    private double mTestTimeOut;
    private IControls mTeleop;

    public final void setTeleop(IControls loop) {
        mTeleop = loop;
    }

    public final void setAutoMode(IAutoMode mode, double testTimeout) {
        mRunningMode = mode;
        mTestTimeOut = testTimeout;
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

    public static XboxControlsState createXboxController(int controllerPort, boolean available) {
        return Robot.getState().createXboxController(available ? controllerPort : -1);
    }
}
