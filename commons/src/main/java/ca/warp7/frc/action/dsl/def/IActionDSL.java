package ca.warp7.frc.action.dsl.def;

import ca.warp7.frc.core.IAction;

/**
 * A declarative, chain-able DSL syntax for scheduling autos
 */

@SuppressWarnings("ALL")
public interface IActionDSL extends IAction {

    IActionDSL async(IAction... actions);

    IActionDSL asyncAny(IAction... actions);

    IActionDSL asyncDetach(double detachedInterval, double timeout, IAction action);

    IActionDSL asyncInverse(IAction... actions);

    IActionDSL asyncWatch(IAction action, IActionConsumer consumer);

    IActionDSL asyncMaster(IAction master, IAction... slaves);

    IActionDSL broadcast(Object... triggers);

    IActionDSL branch(IActionPredicate predicate, IAction ifAction, IAction elseAction);

    IActionDSL consume(IActionConsumer consumer);

    IActionDSL listenForAny(IAction receiver, Object... of);

    IActionDSL listenForAll(IAction receiver, Object... of);

    IActionDSL queue(IAction... actions);

    IActionDSL waitFor(double seconds);

    IActionDSL waitUntil(IActionPredicate predicate);

    default IActionDSL onlyIf(IActionPredicate predicate, IAction action) {
        return branch(predicate, action, null);
    }

    default IActionDSL asyncUntil(IActionPredicate predicate, IAction... actions) {
        return asyncMaster(waitUntil(predicate), actions);
    }

    default IActionDSL broadcastWhen(IActionPredicate predicate, Object... triggers) {
        return waitUntil(predicate).broadcast(triggers);
    }

    default IActionDSL when(IActionPredicate predicate, IAction... actions) {
        return waitUntil(predicate).queue(actions);
    }
}