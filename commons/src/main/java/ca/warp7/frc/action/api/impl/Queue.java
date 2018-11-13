package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.def.IActionConsumer;
import ca.warp7.frc.action.api.def.IActionAPI;
import ca.warp7.frc.action.api.def.IActionPredicate;
import ca.warp7.frc.core.IAction;

class Queue extends QueueBase implements IActionAPI {

    static IActionAPI head(IAction... actions) {
        if (actions.length == 0) {
            return new Queue();
        }
        return new Queue().queue(actions);
    }

    @Override
    public IActionAPI queue(IAction... actions) {
        addToQueue(actions);
        return this;
    }

    @Override
    public IActionAPI async(IAction... actions) {
        return queue(new AsyncAll(actions));
    }

    @Override
    public IActionAPI asyncAny(IAction... actions) {
        return queue(new AsyncAny(actions));
    }

    @Override
    public IActionAPI detachThread(double detachedInterval, double timeout, IAction action) {
        return queue(new Detachment(detachedInterval, timeout, action));
    }

    @Override
    public IActionAPI asyncInverse(IAction... actions) {
        return queue(new AsyncInverse(actions));
    }

    @Override
    public IActionAPI asyncMaster(IAction master, IAction... slaves) {
        return queue(new AsyncMaster(master, slaves));
    }

    @Override
    public IActionAPI asyncWatch(IAction action, IActionConsumer consumer) {
        return queue(new AsyncWatch(action, consumer));
    }

    @Override
    public IActionAPI branch(IActionPredicate predicate, IAction ifAction, IAction elseAction) {
        return queue(new Branch(predicate, ifAction, elseAction));
    }

    @Override
    public IActionAPI exec(IActionConsumer consumer) {
        return queue(new Execution(consumer));
    }

    @Override
    public IActionAPI await(IActionPredicate predicate) {
        return queue(new Await(predicate));
    }
}
