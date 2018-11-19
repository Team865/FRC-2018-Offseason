package ca.warp7.frc.scheduler;

import ca.warp7.action.IAction;

class ActionNode implements IAction {

    private final IAction mContainedAction;
    private final ITriggerSender mTriggerSender;
    private ActionTrigger mStarterTrigger;
    private ActionTrigger mOnEndTrigger;
    private boolean mIsActive;
    private boolean mIsDone;

    ActionNode(ITriggerSender triggerSender, IAction action) {
        mTriggerSender = triggerSender;
        mContainedAction = action;
        mIsActive = false;
        mIsDone = false;
    }

    void setStarterTrigger(ActionTrigger starterTrigger) {
        mStarterTrigger = starterTrigger;
    }

    void setOnEndTrigger(ActionTrigger onEndTrigger) {
        mOnEndTrigger = onEndTrigger;
    }

    ActionTrigger getStarterTrigger() {
        return mStarterTrigger;
    }

    ActionTrigger getOnEndTrigger() {
        return mOnEndTrigger;
    }

    void testTrigger(ActionTrigger trigger) {
        // System.out.println("Testing " + trigger + " against " + mStarterTrigger);
        if (mStarterTrigger != null && mStarterTrigger.equals(trigger)) {
            mIsActive = true;
            start();
        }
    }

    String getName() {
        return mContainedAction.getClass().getSimpleName();
    }

    boolean isDone() {
        return mIsDone;
    }

    @Override
    public void start() {
        if (mIsActive) {
            mContainedAction.start();
        }
    }

    @Override
    public boolean shouldFinish() {
        if (mIsActive) {
            return mContainedAction.shouldFinish();
        }
        return false;
    }

    @Override
    public void update() {
        if (mIsActive) {
            mContainedAction.update();
        }
    }

    @Override
    public void stop() {
        if (mIsActive) {
            mContainedAction.stop();
            mTriggerSender.sendTrigger(mOnEndTrigger);
            mIsActive = false;
            mIsDone = true;
        }
    }
}
