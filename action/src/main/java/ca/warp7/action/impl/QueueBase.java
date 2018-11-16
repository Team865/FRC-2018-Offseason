package ca.warp7.action.impl;

import ca.warp7.action.IAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


abstract class QueueBase extends ActionBase {

    private final List<IAction> mCandidates = new ArrayList<>();
    private List<IAction> mCachedActionQueue;
    private List<IAction> mRuntimeQueue;
    private IAction mCurrentAction;

    void addToQueue(IAction... actions) {
//        Arrays.stream(actions).forEach(action -> System.out.println("Adding Queue: " + action + " to " + QueueBase.this));
        mCandidates.addAll(Arrays.asList(actions));
        mCachedActionQueue = null;
    }

    @Override
    public void _onStart() {
        mCurrentAction = null;
        mRuntimeQueue = getQueue();
        mRuntimeQueue.forEach(action -> {
//            System.out.print(Thread.currentThread().getName() + " ");
//            System.out.println("Link: " + this + " with " + action);
            link(this, action);
        });
    }

    @Override
    public void _onUpdate() {
        if (mCurrentAction == null) {
            if (mRuntimeQueue.isEmpty()) return;
            mCurrentAction = mRuntimeQueue.remove(0);
//            System.out.print(Thread.currentThread().getName() + " ");
//            System.out.println("Queue Start: " + mCurrentAction);
            mCurrentAction.onStart();
        }
        mCurrentAction.onUpdate();
        if (mCurrentAction.shouldFinish()) {
//            System.out.print(Thread.currentThread().getName() + " ");
//            System.out.println("Queue Done: " + mCurrentAction);
            mCurrentAction.onStop();
            mCurrentAction = null;
        }
    }

    @Override
    public void _onStop() {
        if (mCurrentAction != null) mCurrentAction.onStop();
    }

    @Override
    public boolean _shouldFinish() {
        return mRuntimeQueue.isEmpty() && mCurrentAction == null;
    }

    @Override
    public List<IAction> getQueue() {
        if (mCachedActionQueue != null) return mCachedActionQueue;
        List<IAction> actionQueue = new ArrayList<>();
        for (IAction action : mCandidates) {
            if (action instanceof Delegate) {
                List<IAction> elementQueue = ((Delegate) action).getQueue();
                if (elementQueue == null) actionQueue.add(action);
                else {
//                    System.out.print(Thread.currentThread().getName() + " ");
//                    System.out.println("Merging From: " + action);
//                    elementQueue.forEach(e -> System.out.println("Merging: " + e + " into " + QueueBase.this));
                    actionQueue.addAll(elementQueue);
                }
            } else actionQueue.add(action);
        }
        mCachedActionQueue = actionQueue;
        return actionQueue;
    }
}
