package ca.warp7.frc.commons.action.dsl;

import ca.warp7.frc.commons.action.dsl.impl.QueueDSL;
import ca.warp7.frc.commons.core.IAction;
import ca.warp7.frc.commons.core.IAutoMode;
import ca.warp7.frc.commons.core.ILoop;

import java.util.function.Predicate;

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
    public IActionDSL asyncUntil(Predicate<IAction> predicate, IAction... actions) {
        return queue().asyncUntil(predicate, actions);
    }

    @Override
    public IActionDSL branch(Predicate<IAction> predicate, IAction ifAction, IAction elseAction) {
        return queue().branch(predicate, ifAction, elseAction);
    }

    @Override
    public IActionDSL asyncDetach(IAction action) {
        return queue().asyncDetach(action);
    }

    @Override
    public IActionDSL onlyIf(Predicate<IAction> predicate, IAction action) {
        return queue().onlyIf(predicate, action);
    }

    @Override
    public IActionDSL queue(IAction... actions) {
        return new QueueDSL();
    }

    @Override
    public IActionDSL verbose(IAction action) {
        return queue().verbose(action);
    }

    @Override
    public IActionDSL waitFor(double seconds) {
        return queue().waitFor(seconds);
    }

    @Override
    public IActionDSL waitUntil(Predicate<IAction> predicate) {
        return queue().waitUntil(predicate);
    }

    @Override
    public IActionDSL asyncLoop(IAction action, ILoop loop) {
        return queue().asyncLoop(action, loop);
    }

    @Override
    public IActionDSL broadcast(String message) {
        return queue().broadcast(message);
    }

    @Override
    public IActionDSL listen(IAction receiver, String... triggers) {
        return queue().listen(receiver);
    }

    @Override
    public IAction get() {
        return IAutoMode.super.get();
    }

    @Override
    public void onStart() {
    }
}
