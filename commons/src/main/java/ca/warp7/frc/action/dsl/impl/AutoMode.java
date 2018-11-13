package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionConsumer;
import ca.warp7.frc.action.dsl.def.IActionDSL;
import ca.warp7.frc.action.dsl.def.IActionPredicate;
import ca.warp7.frc.core.IAction;
import ca.warp7.frc.core.IAutoMode;

@SuppressWarnings("SameParameterValue")
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
    public IActionDSL detachThread(double detachedInterval, double timeout, IAction action) {
        return queue().detachThread(detachedInterval, timeout, action);
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
    public IActionDSL await(IActionPredicate predicate) {
        return queue().await(predicate);
    }

    @Override
    public IActionDSL exec(IActionConsumer consumer) {
        return queue().exec(consumer);
    }

    @Override
    public void onStart() {
    }

    protected static IActionPredicate triggered(String name) {
        return t -> t.countTrigger(name) > 0;
    }

    protected static IActionPredicate allTriggered(String name) {
        return t -> t.countTrigger(name) > 0;
    }
}
