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
    private boolean mIsInterrupted;

    static void link(IActionParent parent, IAction action) {
        if (action instanceof BaseAction) ((BaseAction) action).mParent = parent;
    }

    void _onStart() {
    }

    void _onUpdate() {
    }

    void _onStop() {
    }

    boolean _shouldFinish() {
        return true;
    }

    @Override
    public void onStart() {
        mStartTime = getResources().getTime();
        _onStart();
    }

    @Override
    public boolean shouldFinish() {
        return mIsInterrupted || _shouldFinish();
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
    public double getElapsed() {
        return getResources().getTime() - mStartTime;
    }

    @Override
    public double getTotalElapsed() {
        return getRoot() != null ? getRoot().getDelegate().getTotalElapsed() : getElapsed();
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
        mIsInterrupted = true;
    }

    @Override
    public boolean isDetached() {
        return false;
    }

    @Override
    public IActionResources getResources() {
        if (mResources != null) return mResources;
        mResources = getParent() != null ? getParent().getDelegate().getResources() : new ActionResources();
        return mResources;
    }
}
