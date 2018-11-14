package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.IAction;
import ca.warp7.frc.action.api.IActionDelegate;
import ca.warp7.frc.action.api.IActionParent;
import ca.warp7.frc.action.api.IActionResources;

@SuppressWarnings("WeakerAccess")
abstract class BaseAction implements IAction, IActionDelegate {

    private IActionParent mParent;
    private IActionResources mResources;
    private double mStartTime;
    private boolean mIsInterrupted;
    private int mDetachDepth;

    static void link(IActionParent parent, IAction action) {
        if (action instanceof BaseAction) ((BaseAction) action).mParent = parent;
    }

    static void incrementDetachDepth(BaseAction action) {
        action.mDetachDepth++;
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
    public IActionParent getParent() {
        return mParent;
    }

    @Override
    public void interrupt() {
        mIsInterrupted = true;
    }

    @Override
    public int getDetachDepth() {
        return mDetachDepth;
    }

    @Override
    public IActionResources getResources() {
        if (mResources != null) return mResources;
        mResources = getParent() != null ? getParent().getDelegate().getResources() : new ActionResources();
        return mResources;
    }
}
