package ca.warp7.frc.commons.scheduler;

public class CompositeAction {

    private enum PostCallMode {
        CHAIN, HOLD
    }

    private ActionGraph mActionGraph;
    private ActionNode mCurrentNode;
    private PostCallMode mPostCallMode;

    public CompositeAction() {
        mActionGraph = new ActionGraph();
        mCurrentNode = null;
        mPostCallMode = PostCallMode.CHAIN;
    }

    public IAction getActionGraph() {
        return mActionGraph;
    }

    public CompositeAction add(IAction action) {
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

    public CompositeAction startParallel() {
        mPostCallMode = PostCallMode.HOLD;
        return this;
    }

    public CompositeAction startSeries() {
        mPostCallMode = PostCallMode.CHAIN;
        return this;
    }
}
