package ca.warp7.frc.commons.scheduler;

@SuppressWarnings("unused")
public class ScheduleBuilder {

    private enum PostCallMode {
        CHAIN, HOLD
    }

    private ActionGraph mActionGraph;
    private ActionNode mCurrentNode;
    private PostCallMode mPostCallMode;

    public ScheduleBuilder() {
        mActionGraph = new ActionGraph();
        mCurrentNode = null;
        mPostCallMode = PostCallMode.CHAIN;
    }

    public IAction getAction() {
        return mActionGraph;
    }

    public ScheduleBuilder addToEnd(IAction action) {
        ActionNode node = new ActionNode(mActionGraph, action);

        ActionTrigger currentEndTrigger;
        currentEndTrigger = mCurrentNode == null ? mActionGraph.getEntryPoint() : mCurrentNode.getOnEndTrigger();

        System.out.println("current end trigger: " + currentEndTrigger);

        ActionTrigger actualStarterTrigger;
        if (currentEndTrigger == null) {
            actualStarterTrigger = mActionGraph.createTrigger(mCurrentNode.getName());
            mCurrentNode.setOnEndTrigger(actualStarterTrigger);
        } else {
            actualStarterTrigger = currentEndTrigger;
        }

        System.out.println("actual starter trigger:" + actualStarterTrigger);

        node.setStarterTrigger(actualStarterTrigger);
        mActionGraph.addNode(node);

        switch (mPostCallMode) {
            case CHAIN:
                mCurrentNode = node;
                break;
        }
        return this;
    }

    public ScheduleBuilder startParallel() {
        mPostCallMode = PostCallMode.HOLD;
        return this;
    }

    public ScheduleBuilder startSeries() {
        mPostCallMode = PostCallMode.CHAIN;
        return this;
    }
}
