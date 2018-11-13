package ca.warp7.frc.action.dsl.def;

import ca.warp7.frc.core.IAction;

/**
 * A declarative, chain-able DSL syntax for scheduling autos
 *
 * @version 2.2 Modified 11/12/2018
 */

@SuppressWarnings("ALL")
public interface IActionDSL extends IAction {

    IActionDSL async(IAction... actions);

    IActionDSL asyncAny(IAction... actions);

    IActionDSL asyncInverse(IAction... actions);

    IActionDSL asyncWatch(IAction action, IActionConsumer consumer);

    IActionDSL asyncMaster(IAction master, IAction... slaves);

    IActionDSL await(IActionPredicate predicate);

    IActionDSL branch(IActionPredicate predicate, IAction ifAction, IAction elseAction);

    IActionDSL detachThread(double detachedInterval, double timeout, IAction action);

    IActionDSL exec(IActionConsumer consumer);

    IActionDSL queue(IAction... actions);

    default IActionDSL broadcast(String... triggers) {
        return exec(SyntaxProvider.broadcastTriggers(triggers));
    }

    default IActionDSL waitFor(double seconds) {
        return await(SyntaxProvider.elapsed(seconds));
    }

    default IActionDSL onlyIf(IActionPredicate predicate, IAction action) {
        return branch(predicate, action, null);
    }

    default IActionDSL asyncUntil(IActionPredicate predicate, IAction... actions) {
        return asyncMaster(await(predicate), actions);
    }

    default IActionDSL broadcastWhen(IActionPredicate predicate, String... triggers) {
        return await(predicate).broadcast(triggers);
    }

    default IActionDSL when(IActionPredicate predicate, IAction... actions) {
        return await(predicate).queue(actions);
    }
}