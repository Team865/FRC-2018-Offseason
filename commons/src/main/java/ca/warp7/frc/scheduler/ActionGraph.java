package ca.warp7.frc.scheduler;

import ca.warp7.action.IAction;

import java.util.ArrayList;
import java.util.List;

class ActionGraph implements IAction, ITriggerSender {
    private final List<ActionNode> mActionNodes;
    private final List<ActionTrigger> mPeriodicTriggers;
    private final List<ActionTrigger> mTriggerPool;
    private final ActionTrigger mEntryPoint;

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
    public void start() {
        mPeriodicTriggers.add(mEntryPoint);
    }

    @Override
    public boolean shouldFinish() {
        return mActionNodes.isEmpty();
    }

    @Override
    public void update() {
        for (ActionNode node : mActionNodes) {
            if (node.shouldFinish()) {
                node.stop();
            } else {
                node.update();
            }
        }
        for (ActionTrigger trigger : mPeriodicTriggers) {
            mActionNodes.forEach(actionMode -> actionMode.testTrigger(trigger));
        }
        mActionNodes.removeIf(ActionNode::isDone);
        mPeriodicTriggers.clear();
    }

    @Override
    public void stop() {
        mActionNodes.forEach(ActionNode::stop);
    }

    @Override
    public void sendTrigger(ActionTrigger trigger) {
        if (trigger != null) {
            mPeriodicTriggers.add(trigger);
        }
    }
}
