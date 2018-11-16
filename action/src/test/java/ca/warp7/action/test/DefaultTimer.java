package ca.warp7.action.test;

import ca.warp7.action.IAction;

public class DefaultTimer implements IAction.ITimer {
    @Override
    public double getTime() {
        return System.nanoTime() / 1.0e09;
    }
}
