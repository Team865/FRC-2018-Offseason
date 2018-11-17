package ca.warp7.action.impl;

import ca.warp7.action.IAction;

@SuppressWarnings("unused")
public abstract class ActionMode extends IAction.HeadClass implements IAction.Mode {

    public static boolean isUsingActionAPI(IAction action) {
        return action instanceof ActionBase;
    }

    public static IAction createRunner(ITimer timer, double interval, double timeout, IAction action, boolean verbose) {
        return new ThreadRunner(timer, interval, timeout, action, verbose);
    }

    @Override
    public API head() {
        return new Queue();
    }

    public IAction.API detachThread(double interval, double timeout, ActionBase action) {
        return queue(createRunner(null, interval, timeout, action, true));
    }
}
