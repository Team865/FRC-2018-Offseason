package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionConsumer;
import ca.warp7.frc.action.dsl.def.IActionDelegate;
import ca.warp7.frc.action.dsl.def.IActionParent;
import ca.warp7.frc.action.dsl.def.IActionResources;

@SuppressWarnings("WeakerAccess")
abstract class BaseAction extends StopAction implements IActionDelegate {

    private IActionParent mParent;

    void setParent(IActionParent parent) {
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
        if (mParent == null) return null;
        return mParent.getDelegate().getRoot();
    }

    @Override
    public boolean isConsumed(IActionConsumer consumer) {
        return false;
    }

    @Override
    public void interrupt() {

    }

    @Override
    public boolean isDetached() {
        return false;
    }

    @Override
    public IActionParent asParent() {
        return null;
    }

    @Override
    public boolean hasParent() {
        return false;
    }

    @Override
    public IActionResources resources() {
        return null;
    }
}
