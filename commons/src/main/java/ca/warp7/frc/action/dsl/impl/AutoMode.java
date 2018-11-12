package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionConsumer;
import ca.warp7.frc.action.dsl.def.IActionDSL;
import ca.warp7.frc.action.dsl.def.IActionPredicate;
import ca.warp7.frc.core.IAction;
import ca.warp7.frc.core.IAutoMode;

public abstract class AutoMode implements IAutoMode, IActionDSL {

    @Override
    public IActionDSL async(IAction... actions) {
        return queue().async(actions);
    }

    @Override
    public IActionDSL asyncAny(IAction... actions) {
        return queue().asyncAny(actions);
    }

    @Override
    public IActionDSL asyncInverse(IAction... actions) {
        return queue().asyncInverse(actions);
    }

    @Override
    public IActionDSL asyncMaster(IAction master, IAction... slaves) {
        return queue().asyncMaster(master, slaves);
    }

    @Override
    public IActionDSL branch(IActionPredicate predicate, IAction ifAction, IAction elseAction) {
        return queue().branch(predicate, ifAction, elseAction);
    }

    @Override
    public IActionDSL asyncDetach(double detachedInterval, double timeout, IAction action) {
        return queue().asyncDetach(detachedInterval, timeout, action);
    }

    @Override
    public IActionDSL asyncWatch(IAction action, IActionConsumer consumer) {
        return queue().asyncWatch(action, consumer);
    }

    @Override
    public IActionDSL queue(IAction... actions) {
        return Queue.head(actions);
    }

    @Override
    public IActionDSL waitFor(double seconds) {
        return queue().waitFor(seconds);
    }

    @Override
    public IActionDSL waitUntil(IActionPredicate predicate) {
        return queue().waitUntil(predicate);
    }

    @Override
    public IActionDSL broadcast(Object... triggers) {
        return queue().broadcast(triggers);
    }

    @Override
    public IActionDSL consume(IActionConsumer consumer) {
        return queue().consume(consumer);
    }

    @Override
    public IActionDSL listenForAny(IAction receiver, Object... of) {
        return queue().listenForAny(receiver);
    }

    @Override
    public IActionDSL listenForAll(IAction receiver, Object... of) {
        return queue().listenForAll(receiver, of);
    }

    @Override
    public IAction get() {
        return IAutoMode.super.get();
    }

    @Override
    public void onStart() {
    }
}
