package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.core.IAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


abstract class BaseQueue implements IAction {

    private final List<IAction> mCandidates = new ArrayList<>();
    private List<IAction> mCachedActionQueue;
    private List<IAction> mRuntimeQueue;
    private IAction mCurrentAction;

    void addToQueue(IAction... actions) {
        mCandidates.addAll(Arrays.asList(actions));
        mCachedActionQueue = null;
    }

    @Override
    public void onStart() {
        mCurrentAction = null;
        mRuntimeQueue = getActionQueue();
    }

    @Override
    public void onUpdate() {
        if (mCurrentAction == null) {
            if (mRuntimeQueue.isEmpty()) return;
            mCurrentAction = mRuntimeQueue.remove(0);
            mCurrentAction.onStart();
        }
        mCurrentAction.onUpdate();
        if (mCurrentAction.shouldFinish()) {
            mCurrentAction.onStop();
            mCurrentAction = null;
        }
    }

    @Override
    public void onStop() {
        if (mCurrentAction != null) {
            mCurrentAction.onStop();
        }
    }

    @Override
    public boolean shouldFinish() {
        return mRuntimeQueue.isEmpty() && mCurrentAction == null;
    }

    @Override
    public List<IAction> getActionQueue() {
        if (mCachedActionQueue != null) return mCachedActionQueue;
        List<IAction> actionQueue = new ArrayList<>();
        for (IAction action : mCandidates) {
            List<IAction> elementQueue = action.getActionQueue();
            if (elementQueue == null) actionQueue.add(action);
            else actionQueue.addAll(elementQueue);
        }
        mCachedActionQueue = actionQueue;
        return actionQueue;
    }
}
