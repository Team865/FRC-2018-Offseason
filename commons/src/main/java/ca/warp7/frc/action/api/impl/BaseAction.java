package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.def.IActionConsumer;
import ca.warp7.frc.action.api.def.IActionDelegate;
import ca.warp7.frc.action.api.def.IActionParent;
import ca.warp7.frc.action.api.def.IActionResources;
import ca.warp7.frc.core.IAction;

@SuppressWarnings("WeakerAccess")
abstract class BaseAction implements IAction, IActionDelegate {

    private IActionParent mParent;
    private IActionResources mResources;
    private double mStartTime;

    void setParent(IActionParent parent) {
        mParent = parent;
    }

    abstract void _onStart();

    abstract void _onUpdate();

    abstract void _onStop();

    @Override
    public void onStart() {
        mStartTime = resources().getTime();
        _onStart();
    }

    @Override
    public void onUpdate() {
        _onUpdate();
    }

    @Override
    public void onStop() {
        _onStop();
    }

    @Override
    public double elapsed() {
        return resources().getTime() - mStartTime;
    }

    @Override
    public double getTotalElapsed() {
        return getRoot() != null ? getRoot().delegate().getTotalElapsed() : elapsed();
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
        if (mResources != null) return mResources;
        mResources = mParent != null ? mParent.delegate().resources() : new ActionResources();
        return mResources;
    }
}
