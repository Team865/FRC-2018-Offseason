package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionConsumer;
import ca.warp7.frc.action.dsl.def.IActionDSL;
import ca.warp7.frc.action.dsl.def.IActionPredicate;
import ca.warp7.frc.core.IAction;

class Queue extends QueueBase implements IActionDSL {

    static IActionDSL head(IAction... actions) {
        if (actions.length == 0) {
            return new Queue();
        }
        return new Queue().queue(actions);
    }

    @Override
    public IActionDSL queue(IAction... actions) {
        addToQueue(actions);
        return this;
    }

    @Override
    public IActionDSL async(IAction... actions) {
        return queue(new AsyncAll(actions));
    }

    @Override
    public IActionDSL asyncAny(IAction... actions) {
        return queue(new AsyncAny(actions));
    }

    @Override
    public IActionDSL asyncDetach(double detachedInterval, double timeout, IAction action) {
        return queue(new AsyncDetach(detachedInterval, timeout, action));
    }

    @Override
    public IActionDSL asyncInverse(IAction... actions) {
        return queue(new AsyncInverse(actions));
    }

    @Override
    public IActionDSL asyncMaster(IAction master, IAction... slaves) {
        return queue(new AsyncMaster(master, slaves));
    }

    @Override
    public IActionDSL asyncWatch(IAction action, IActionConsumer consumer) {
        return null;
    }

    @Override
    public IActionDSL branch(IActionPredicate predicate, IAction ifAction, IAction elseAction) {
        return queue(new Branch(predicate, ifAction, elseAction));
    }

    @Override
    public IActionDSL broadcast(Object... triggers) {
        return queue(new Broadcast(triggers));
    }

    @Override
    public IActionDSL consume(IActionConsumer consumer) {
        return queue(new Consume(consumer));
    }

    @Override
    public IActionDSL listenForAny(IAction receiver, Object... of) {
        return queue(new ListenForAny(receiver, of));
    }

    @Override
    public IActionDSL listenForAll(IAction receiver, Object... of) {
        return queue(new ListenForAll(receiver, of));
    }

    @Override
    public IActionDSL waitFor(double seconds) {
        return queue(new WaitFor(seconds));
    }

    @Override
    public IActionDSL waitUntil(IActionPredicate predicate) {
        return queue(new WaitUntil(predicate));
    }
}
