package ca.warp7.action.impl;

import ca.warp7.action.IAction;

abstract class ActionBase implements IAction, IAction.Delegate {

    private Parent mParent;
    private Resources mResources;
    private double mStartTime;
    private boolean mIsInterrupted;
    private int mDetachDepth;

    static void link(Parent parent, IAction action) {
        if (action instanceof ActionBase) ((ActionBase) action).mParent = parent;
    }

    static void incrementDetachDepth(ActionBase action) {
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
        _onStart();
        getResources().startTimer();
        mStartTime = mResources.getTime();
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
    public Parent getParent() {
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
    public Resources getResources() {
        if (mResources != null) return mResources;
        mResources = getParent() != null ? getParent().getDelegate().getResources() : new ActionResources();
        mResources = mResources != null ? mResources : new ActionResources();
        return mResources;
    }
}
