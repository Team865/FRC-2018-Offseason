package ca.warp7.action.impl;

import ca.warp7.action.IAction;
import ca.warp7.action.IActionAPI;
import ca.warp7.action.IActionMode;

@SuppressWarnings("unused")
public abstract class ActionMode extends IActionAPI.Head implements IActionMode {

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

    public IActionAPI detachThread(double interval, double timeout, ActionBase action) {
        return queue(createRunner(null, interval, timeout, action));
    }
}
