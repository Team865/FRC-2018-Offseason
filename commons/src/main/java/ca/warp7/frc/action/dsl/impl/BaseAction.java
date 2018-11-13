package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionConsumer;
import ca.warp7.frc.action.dsl.def.IActionDelegate;
import ca.warp7.frc.action.dsl.def.IActionParent;

@SuppressWarnings("WeakerAccess")
abstract class BaseAction extends StopAction implements IActionDelegate {

    private IActionParent mParent;

    @Override
    public void setParent(IActionParent parent) {
        mParent = parent;
    }

    @Override
    public double getElapsed() {
        return 0;
    }

    @Override
    public double getTotalElapsed() {
        return 0;
    }

    @Override
    public IActionParent getParent() {
        return mParent;
    }

    @Override
    public IActionParent getRoot() {
        return null;
    }

    @Override
    public void setVar(String name, Object value) {

    }

    @Override
    public Object getVar(String name, Object defaultVal) {
        return null;
    }

    @Override
    public double getDouble(String name, double defaultVal) {
        return 0;
    }

    @Override
    public boolean isConsumed(IActionConsumer consumer) {
        return false;
    }

    @Override
    public void interrupt() {

    }

    @Override
    public int countTrigger(String name) {
        return 0;
    }

    @Override
    public int countTriggerSources(String name) {
        return 0;
    }
}
