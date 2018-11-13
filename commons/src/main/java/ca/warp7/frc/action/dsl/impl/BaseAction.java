package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionConsumer;
import ca.warp7.frc.action.dsl.def.IActionDelegate;
import ca.warp7.frc.action.dsl.def.IActionParent;
import ca.warp7.frc.action.dsl.def.IActionResources;

@SuppressWarnings("WeakerAccess")
abstract class BaseAction extends StopAction implements IActionDelegate {

    private IActionParent mParent;
    private IActionResources mResources;

    void setParent(IActionParent parent) {
        mParent = parent;
    }

    @Override
    public double elapsed() {
        return 0;
    }

    @Override
    public double getTotalElapsed() {
        return 0;
    }

    @Override
    public IActionParent parent() {
        return mParent;
    }

    @Override
    public IActionParent getRoot() {
        if (mParent == null) return null;
        return mParent.delegate().getRoot();
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
        if (mParent != null) return mParent.delegate().resources();
        if (mResources == null) mResources = new ActionResources();
        return mResources;
    }
}
