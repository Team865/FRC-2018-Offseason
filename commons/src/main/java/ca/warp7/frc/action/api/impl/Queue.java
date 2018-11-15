package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.IAction;
import ca.warp7.frc.action.api.IActionAPI;
import ca.warp7.frc.action.api.IActionConsumer;
import ca.warp7.frc.action.api.IActionPredicate;

class Queue extends QueueBase implements IActionAPI {

    @Override
    public IActionAPI queue(IAction... actions) {
        addToQueue(actions);
        return this;
    }

    @Override
    public IActionAPI asyncAll(IAction... actions) {
        return queue(new AsyncForward.All(actions));
    }

    @Override
    public IActionAPI asyncAny(IAction... actions) {
        return queue(new AsyncForward.Any(actions));
    }

    @Override
    public IActionAPI asyncInverse(IAction... actions) {
        return queue(new AsyncInverse(actions));
    }

    @Override
    public IActionAPI asyncMaster(IAction master, IAction... slaves) {
        return queue(new AsyncForward.Master(master, slaves));
    }

    @Override
    public IActionAPI runIf(IActionPredicate predicate, IAction ifAction, IAction elseAction) {
        return queue(new Condition(predicate, ifAction, elseAction));
    }

    @Override
    public IActionAPI exec(IActionConsumer consumer) {
        return queue(new Execution(consumer));
    }

    @Override
    public IActionAPI await(IActionPredicate predicate) {
        return queue(new Await(predicate));
    }

    @Override
    public IActionAPI broadcast(String... triggers) {
        getResources().addBroadcastSources(triggers);
        return IActionAPI.super.broadcast(triggers);
    }

    static IActionAPI _queue(IAction... actions) {
        return actions.length == 0 ? new Queue() : new Queue().queue(actions);
    }
}
