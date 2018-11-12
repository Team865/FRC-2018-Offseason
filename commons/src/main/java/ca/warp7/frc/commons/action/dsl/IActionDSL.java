package ca.warp7.frc.commons.action.dsl;

import ca.warp7.frc.commons.core.IAction;
import ca.warp7.frc.commons.core.ILoop;

import java.util.function.Predicate;

/**
 * A declarative, chain-able DSL syntax for scheduling autos
 */

public interface IActionDSL extends IAction {
    IActionDSL async(IAction... actions);

    IActionDSL asyncAny(IAction... actions);

    IActionDSL asyncDetach(double detachedInterval, double timeout, IAction action);

    IActionDSL asyncInverse(IAction... actions);

    IActionDSL asyncLoop(IAction action, ILoop loop);

    IActionDSL asyncMaster(IAction master, IAction... slaves);

    IActionDSL asyncUntil(Predicate<IActionDelegate> predicate, IAction... actions);

    IActionDSL broadcast(Object... triggers);

    IActionDSL broadcastWhen(Predicate<IActionDelegate> predicate, Object... triggers);

    IActionDSL branch(Predicate<IActionDelegate> predicate, IAction ifAction, IAction elseAction);

    IActionDSL debug(IAction action);

    IActionDSL listenForAny(IAction receiver, Object... of);

    IActionDSL listenForAll(IAction receiver, Object... of);

    IActionDSL onlyIf(Predicate<IActionDelegate> predicate, IAction action);

    IActionDSL queue(IAction... actions);

    IActionDSL waitFor(double seconds);

    IActionDSL waitUntil(Predicate<IActionDelegate> predicate);
}