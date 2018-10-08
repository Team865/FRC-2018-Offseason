package ca.warp7.frc.scheduler;

@SuppressWarnings("unused")
public class ScheduledMode {

	private enum PostCallMode {
		CHAIN, HOLD
	}

	private ActionGraph mActionGraph;
	private ActionNode mCurrentNode;
	private PostCallMode mPostCallMode;

	public ScheduledMode() {
		mActionGraph = new ActionGraph();
		mCurrentNode = null;
		mPostCallMode = PostCallMode.CHAIN;
	}

	public IAction getAction() {
		return mActionGraph;
	}

	public ScheduledMode addToEnd(IAction action) {
		ActionNode node = new ActionNode(mActionGraph, action);

		ActionTrigger currentEndTrigger;
		currentEndTrigger = mCurrentNode == null ? mActionGraph.getEntryPoint() : mCurrentNode.getOnEndTrigger();

		ActionTrigger actualStarterTrigger;
		if (currentEndTrigger == null) {
			actualStarterTrigger = mActionGraph.createTrigger(mCurrentNode.getName());
			mCurrentNode.setOnEndTrigger(actualStarterTrigger);
		} else {
			actualStarterTrigger = currentEndTrigger;
		}

		node.setStarterTrigger(actualStarterTrigger);
		mActionGraph.addNode(node);

		switch (mPostCallMode) {
			case CHAIN:
				mCurrentNode = node;
				break;
		}
		return this;
	}

	public ScheduledMode hold() {
		mPostCallMode = PostCallMode.HOLD;
		return this;
	}

	public ScheduledMode chain() {
		mPostCallMode = PostCallMode.CHAIN;
		return this;
	}
}
