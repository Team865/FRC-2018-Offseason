package ca.warp7.frc.scheduler;

import java.util.ArrayList;
import java.util.List;

class ActionGraph implements IAction, ITriggerSender {
	private List<ActionNode> mActionNodes;
	private List<ActionTrigger> mPeriodicTriggers;
	private List<ActionTrigger> mTriggerPool;
	private ActionTrigger mEntryPoint;

	ActionGraph() {
		mActionNodes = new ArrayList<>();
		mPeriodicTriggers = new ArrayList<>();
		mTriggerPool = new ArrayList<>();
		mEntryPoint = new ActionTrigger("Auto Entry Point");
		mTriggerPool.add(mEntryPoint);
	}

	ActionTrigger createTrigger(String name) {
		for (ActionTrigger trigger : mTriggerPool) {
			if (trigger.getName().equals(name)) {
				name = name + "_" + String.valueOf((int) (Math.random() * 10000));
			}
		}
		ActionTrigger newTrigger = new ActionTrigger(name);
		mTriggerPool.add(newTrigger);
		return newTrigger;
	}

	void addNode(ActionNode node) {
		mActionNodes.add(node);
	}

	ActionTrigger getEntryPoint() {
		return mEntryPoint;
	}

	@Override
	public void onStart() {
		mPeriodicTriggers.add(mEntryPoint);
	}

	@Override
	public boolean shouldFinish() {
		return mActionNodes.isEmpty();
	}

	@Override
	public void onUpdate() {
		for (ActionTrigger trigger : mPeriodicTriggers) {
			mActionNodes.forEach(actionMode -> actionMode.testTrigger(trigger));
		}
		for (ActionNode node : mActionNodes) {
			if (node.shouldFinish()) {
				node.onStop();
			} else {
				node.onUpdate();
			}
		}
		mActionNodes.removeIf(ActionNode::isDone);
		mPeriodicTriggers.clear();
	}

	@Override
	public void onStop() {
		mActionNodes.forEach(ActionNode::onStop);
	}

	@Override
	public void sendTrigger(ActionTrigger trigger) {
		mPeriodicTriggers.add(trigger);
	}
}
