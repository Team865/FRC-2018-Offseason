package ca.warp7.frc.core;

import ca.warp7.action.IAction;
import ca.warp7.action.impl.ActionMode;
import edu.wpi.first.wpilibj.Timer;

/**
 * Runner class for the auto mode, capable of starting
 * and stopping actions, on a separate thread
 */

class AutoRunner {

    private IAction mActionRunner;

    void start() {
        IAction.Mode autoMode = Robot.loader.getRunningMode();
        assert autoMode != null;
        IAction action = autoMode.getAction();
        assert action != null;
        double timeout = Robot.loader.getTestTimeOut();
        if (timeout >= 15 || timeout < 0) timeout = 15;
        mActionRunner = ActionMode.createRunner(Timer::getFPGATimestamp, 20, timeout, action, true);
        mActionRunner.start();
    }

    void stop() {
        mActionRunner.stop();
    }
}
