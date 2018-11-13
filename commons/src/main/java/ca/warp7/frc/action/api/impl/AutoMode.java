package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.def.IActionConsumer;
import ca.warp7.frc.action.api.def.IActionAPI;
import ca.warp7.frc.action.api.def.IActionPredicate;
import ca.warp7.frc.action.api.def.SyntaxProvider;
import ca.warp7.frc.core.IAction;
import ca.warp7.frc.core.IAutoMode;

@SuppressWarnings("SameParameterValue")
public abstract class AutoMode extends SyntaxProvider implements IAutoMode, IActionAPI {

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
    public IActionAPI detachThread(double detachedInterval, double timeout, IAction action) {
        return queue().detachThread(detachedInterval, timeout, action);
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
}
