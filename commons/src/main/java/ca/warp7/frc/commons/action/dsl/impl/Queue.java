package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.action.dsl.IActionDSL;
import ca.warp7.frc.commons.action.dsl.IActionDelegate;
import ca.warp7.frc.commons.action.dsl.ILoopAction;
import ca.warp7.frc.commons.core.IAction;
import ca.warp7.frc.commons.core.ILoop;

import java.util.function.Predicate;

class Queue extends QueueBase implements IActionDSL {

    static IActionDSL head(IAction... actions) {
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
    public IActionDSL asyncInverse(IAction... actions) {
        return queue(new AsyncInverse());
    }

    @Override
    public IActionDSL asyncLoop(IAction action, ILoop loop) {
        return asyncMaster(action, (ILoopAction) loop::onLoop);
    }

    @Override
    public IActionDSL asyncMaster(IAction master, IAction... slaves) {
        return queue(new AsyncMaster(master, slaves));
    }

    @Override
    public IActionDSL asyncUntil(Predicate<IActionDelegate> predicate, IAction... actions) {
        return asyncMaster(new WaitUntil(predicate), actions);
    }

    @Override
    public IActionDSL branch(Predicate<IActionDelegate> predicate, IAction ifAction, IAction elseAction) {
        return queue(new Branch(predicate, ifAction, elseAction));
    }

    @Override
    public IActionDSL broadcast(Object... triggers) {
        return queue(new Broadcast(triggers));
    }

    @Override
    public IActionDSL broadcastWhen(Predicate<IActionDelegate> predicate, Object... triggers) {
        return waitUntil(predicate).broadcast(triggers);
    }

    @Override
    public IActionDSL asyncDetach(double detachedInterval, double timeout, IAction action) {
        return queue(new AsyncDetach(detachedInterval, timeout, action));
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
    public IActionDSL onlyIf(Predicate<IActionDelegate> predicate, IAction action) {
        return branch(predicate, action, new StopAction());
    }

    @Override
    public IActionDSL debug(IAction action) {
        return queue(new Debug());
    }

    @Override
    public IActionDSL waitFor(double seconds) {
        return queue(new WaitFor(seconds));
    }

    @Override
    public IActionDSL waitUntil(Predicate<IActionDelegate> predicate) {
        return queue(new WaitUntil(predicate));
    }
}
