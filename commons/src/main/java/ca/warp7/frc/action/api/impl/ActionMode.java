package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.IAction;
import ca.warp7.frc.action.api.IActionAPI;
import ca.warp7.frc.action.api.IActionMode;

public abstract class ActionMode extends HeadAPI implements IActionMode {

    public static boolean isUsingActionAPI(IAction action) {
        return action instanceof ActionBase;
    }

    public static IAction createRunner(ITimer timer, double interval, double timeout, IAction action) {
        return ThreadRunner.create(timer, interval, timeout, (ActionBase) action);
    }

    @Override
    public IActionAPI queue(IAction... actions) {
        return Queue._queue(actions);
    }

    @Override
    public void onStart() {
    }

    @SuppressWarnings("unused")
    public IActionAPI detachThread(double interval, double timeout, ActionBase action) {
        return queue(createRunner(null, interval, timeout, action));
    }
}
