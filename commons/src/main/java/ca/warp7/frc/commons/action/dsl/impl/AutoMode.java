package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.action.dsl.IActionDSL;
import ca.warp7.frc.commons.action.dsl.IActionDelegate;
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
    public IActionDSL asyncUntil(Predicate<IActionDelegate> predicate, IAction... actions) {
        return queue().asyncUntil(predicate, actions);
    }

    @Override
    public IActionDSL branch(Predicate<IActionDelegate> predicate, IAction ifAction, IAction elseAction) {
        return queue().branch(predicate, ifAction, elseAction);
    }

    @Override
    public IActionDSL asyncDetach(double detachedInterval, double timeout, IAction action) {
        return queue().asyncDetach(detachedInterval, timeout, action);
    }

    @Override
    public IActionDSL onlyIf(Predicate<IActionDelegate> predicate, IAction action) {
        return queue().onlyIf(predicate, action);
    }

    @Override
    public IActionDSL queue(IAction... actions) {
        return new QueueDSL(actions);
    }

    @Override
    public IActionDSL debug(IAction action) {
        return queue().debug(action);
    }

    @Override
    public IActionDSL waitFor(double seconds) {
        return queue().waitFor(seconds);
    }

    @Override
    public IActionDSL waitUntil(Predicate<IActionDelegate> predicate) {
        return queue().waitUntil(predicate);
    }

    @Override
    public IActionDSL asyncLoop(IAction action, ILoop loop) {
        return queue().asyncLoop(action, loop);
    }

    @Override
    public IActionDSL broadcast(Object... triggers) {
        return queue().broadcast(triggers);
    }

    @Override
    public IActionDSL broadcastWhen(Predicate<IActionDelegate> predicate, Object... triggers) {
        return queue().broadcastWhen(predicate, triggers);
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
