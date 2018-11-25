package ca.warp7.action.impl;

import ca.warp7.action.IAction;

abstract class ActionBase implements IAction, IAction.Delegate {

    private Delegate mParent;
    private SingletonResources mResources;
    private double mStartTime;
    private boolean mIsInterrupted;
    private int mDetachDepth;
    private String mName = "";

    static void linkChild(Delegate parent, IAction action) {
        if (action instanceof ActionBase) safeLinkChild(parent, (ActionBase) action);
    }

    static void safeLinkChild(Delegate parent, ActionBase actionBase) {
        actionBase.mParent = parent;
    }

    static void incrementDetachDepth(ActionBase action) {
        action.mDetachDepth++;
    }

    @Override
    public void start() {
        prepare();
//        System.out.print(Thread.currentThread().getName() + " ");
//        System.out.println("Start: " + this);
        getResources().startTimer();
        mStartTime = mResources.getTime();
    }

    @Override
    public boolean shouldFinish() {
        return mIsInterrupted || _shouldFinish();
    }

    @Override
    public double getElapsed() {
        return getResources().getTime() - mStartTime;
    }

    @Override
    public Delegate getParent() {
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
    public SingletonResources getResources() {
        if (mResources != null) return mResources;
        if (hasParent()) mResources = getParent().getResources();
        else {
//            System.out.print(Thread.currentThread().getName() + " ");
//            System.out.println("Creating Resources For: " + this);
            mResources = new ca.warp7.action.impl.Resources();
        }
        mResources = mResources != null ? mResources : new ca.warp7.action.impl.Resources();
        return mResources;
    }

    @Override
    public void setName(String name) {
        mName = name;
    }

    @Override
    public String getName() {
        return mName;
    }


    void prepare() {
    }

    boolean _shouldFinish() {
        return true;
    }
}
