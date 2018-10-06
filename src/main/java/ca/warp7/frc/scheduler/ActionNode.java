package ca.warp7.frc.scheduler;

class ActionNode implements IAction {

	private IAction mContainedAction;
	private ITriggerSender mTriggerSender;
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
		if (mStarterTrigger != null && mStarterTrigger.equals(trigger)) {
			mIsActive = true;
			onStart();
		}
	}

	String getName() {
		return mContainedAction.getClass().getSimpleName();
	}

	boolean isDone() {
		return mIsDone;
	}

	@Override
	public void onStart() {
		if (mIsActive) {
			mContainedAction.onStart();
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
	public void onUpdate() {
		if (mIsActive) {
			mContainedAction.onUpdate();
		}
	}

	@Override
	public void onStop() {
		if (mIsActive) {
			mContainedAction.onStop();
			mTriggerSender.sendTrigger(mOnEndTrigger);
			mIsActive = false;
			mIsDone = true;
		}
	}
}