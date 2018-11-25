package ca.warp7.action.impl;

import ca.warp7.action.IAction;

class Queue extends QueueBase implements IAction.API {

    @Override
    public API head() {
        return new Queue();
    }

    @Override
    public API asyncOp(OpStart startMode, OpStop stopMode, IAction... actions) {
        return queue(new AsyncOp(startMode, stopMode, actions));
    }

    @Override
    public API asyncAll(IAction... actions) {
        return queue(new AsyncOp.All(actions));
    }

    @Override
    public API asyncAny(IAction... actions) {
        return queue(new AsyncOp.Any(actions));
    }

    @Override
    public API await(Predicate predicate) {
        return queue(new Await(predicate));
    }

    @Override
    public API exec(Consumer consumer) {
        return queue(new Execution(consumer));
    }

    @Override
    public API iterate(Consumer consumer) {
        return queue(new Iteration(consumer));
    }

    @Override
    public API queue(IAction... actions) {
        addToQueue(actions);
        return this;
    }

    @Override
    public API runIf(Predicate predicate, IAction ifAction, IAction elseAction) {
        return queue(new Condition(predicate, ifAction, elseAction));
    }
}
