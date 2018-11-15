package ca.warp7.frc.action.api;

/**
 * A declarative, chain-able API syntax for scheduling autos
 *
 * @version 2.5 Modified 11/14/2018
 */

@SuppressWarnings("ALL")
public interface IActionAPI extends IAction {

    IActionAPI async(IAction... actions);

    IActionAPI asyncAny(IAction... actions);

    IActionAPI asyncInverse(IAction... actions);

    IActionAPI asyncMaster(IAction master, IAction... slaves);

    IActionAPI await(IActionPredicate predicate);

    IActionAPI runIf(IActionPredicate predicate, IAction ifAction, IAction elseAction);

    IActionAPI exec(IActionConsumer consumer);

    IActionAPI queue(IAction... actions);

    default IActionAPI broadcast(String... triggers) {
        return exec(SyntaxProvider.broadcastAll(triggers));
    }

    default IActionAPI waitFor(double seconds) {
        return await(SyntaxProvider.elapsed(seconds));
    }

    default IActionAPI onlyIf(IActionPredicate predicate, IAction action) {
        return runIf(predicate, action, null);
    }

    default IActionAPI asyncUntil(IActionPredicate predicate, IAction... actions) {
        return asyncMaster(await(predicate), actions);
    }

    default IActionAPI broadcastWhen(IActionPredicate predicate, String... triggers) {
        return await(predicate).broadcast(triggers);
    }

    default IActionAPI when(IActionPredicate predicate, IAction... actions) {
        return await(predicate).queue(actions);
    }
}