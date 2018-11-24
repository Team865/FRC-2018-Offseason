package ca.warp7.frc.core;

import ca.warp7.action.IAction;
import ca.warp7.action.impl.ActionMode;
import edu.wpi.first.wpilibj.Timer;

public class RobotLoader {

    private IAction.Mode mRunningMode;
    private double mTimeout;

    private IAction mActionRunner;

    public final void setAutoMode(IAction.Mode mode, double testTimeout) {
        mRunningMode = mode;
        mTimeout = testTimeout;
        if (mTimeout >= 15 || mTimeout < 0) mTimeout = 15;
    }

    void startAutonomous() {
        assert mRunningMode != null;
        IAction action = mRunningMode.getAction();
        assert action != null;
        mActionRunner = ActionMode.createRunner(Timer::getFPGATimestamp, 20, mTimeout, action, true);
        mActionRunner.start();
    }

    void stopAutonomous() {
        mActionRunner.stop();
    }

    public static XboxControlsState createXboxController(int controllerPort, boolean available) {
        return Robot.getState().createXboxController(available ? controllerPort : -1);
    }
}
