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

    IActionDSL asyncDetach(IAction action);

    IActionDSL asyncInverse(IAction... actions);

    IActionDSL asyncLoop(IAction action, ILoop loop);

    IActionDSL asyncMaster(IAction master, IAction... slaves);

    IActionDSL asyncUntil(Predicate<IAction> predicate, IAction... actions);

    IActionDSL broadcast(Object... triggers);

    IActionDSL broadcastWhen(Predicate<IAction> predicate, Object... triggers);

    IActionDSL branch(Predicate<IAction> predicate, IAction ifAction, IAction elseAction);

    IActionDSL debug(IAction action);

    IActionDSL listen(IAction receiver, Object... triggers);

    IActionDSL listenForAll(IAction receiver, Object... triggers);

    IActionDSL onlyIf(Predicate<IAction> predicate, IAction action);

    IActionDSL queue(IAction... actions);

    IActionDSL waitFor(double seconds);

    IActionDSL waitUntil(Predicate<IAction> predicate);
}