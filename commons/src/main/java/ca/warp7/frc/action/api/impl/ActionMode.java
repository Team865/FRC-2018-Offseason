package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.*;

public abstract class ActionMode extends SyntaxProvider implements IActionMode, IActionAPI {

    @Override
    public IActionAPI async(IAction... actions) {
        return queue().async(actions);
    }

    @Override
    public IActionAPI asyncAny(IAction... actions) {
        return queue().asyncAny(actions);
    }

    @Override
    public IActionAPI asyncInverse(IAction... actions) {
        return queue().asyncInverse(actions);
    }

    @Override
    public IActionAPI asyncMaster(IAction master, IAction... slaves) {
        return queue().asyncMaster(master, slaves);
    }

    @Override
    public IActionAPI branch(IActionPredicate predicate, IAction ifAction, IAction elseAction) {
        return queue().branch(predicate, ifAction, elseAction);
    }

    @Override
    public IActionAPI asyncWatch(IAction action, IActionConsumer consumer) {
        return queue().asyncWatch(action, consumer);
    }

    @Override
    public IActionAPI queue(IAction... actions) {
        return Queue.head(actions);
    }

    @Override
    public IActionAPI await(IActionPredicate predicate) {
        return queue().await(predicate);
    }

    @Override
    public IActionAPI exec(IActionConsumer consumer) {
        return queue().exec(consumer);
    }

    @Override
    public void onStart() {
    }

    @SuppressWarnings("unused")
    public IActionAPI detachThread(double interval, double timeout, BaseAction action) {
        return queue(ThreadRunner.create(null, interval, timeout, action));
    }
}
