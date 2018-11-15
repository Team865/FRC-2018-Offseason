package ca.warp7.action.impl;

import ca.warp7.action.IAction;

class Queue extends QueueBase implements IAction.API {

    @Override
    public API asyncAll(IAction... actions) {
        return queue(new AsyncForward.All(actions));
    }

    @Override
    public API asyncAny(IAction... actions) {
        return queue(new AsyncForward.Any(actions));
    }

    @Override
    public API asyncInverse(IAction... actions) {
        return queue(new AsyncInverse(actions));
    }

    @Override
    public API asyncMaster(IAction master, IAction... slaves) {
        return queue(new AsyncForward.Master(master, slaves));
    }

    @Override
    public API await(Predicate predicate) {
        return queue(new Await(predicate));
    }

    @Override
    public API broadcast(String... triggers) {
        getResources().addBroadcastSources(triggers);
        return API.super.broadcast(triggers);
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

    static API _queue(IAction... actions) {
        return actions.length == 0 ? new Queue() : new Queue().queue(actions);
    }
}
