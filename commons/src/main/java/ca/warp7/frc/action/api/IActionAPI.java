package ca.warp7.frc.action.api;

import java.util.Arrays;

/**
 * A declarative, chain-able API syntax for scheduling autos.
 *
 * The API collection contains the following interfaces:
 * {@link IActionConsumer}
 * {@link IActionDelegate}
 * {@link IActionMode}
 * {@link IActionParent}
 * {@link IActionPredicate}
 * {@link IActionResources}
 *
 * @version 2.6 Revised 11/14/2018
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

    abstract class SyntaxProvider {

        protected static IActionPredicate triggeredOnce(String name) {
            return d -> d.getResources().countBroadcast(name) == 1;
        }

        protected static IActionPredicate triggeredRepeat(String name) {
            return d -> d.getResources().countBroadcast(name) > 1;
        }

        protected static IActionPredicate triggeredAll(String name) {
            return d -> d.getResources().countBroadcast(name) == d.getResources().countBroadcastSources(name);
        }

        protected static IActionPredicate triggeredSome(String name, int times) {
            return d -> d.getResources().countBroadcast(name) == times;
        }

        protected static IActionPredicate elapsed(double timeInSeconds) {
            return d -> !d.hasParent() || d.getParent().getDelegate().getElapsed() > timeInSeconds;
        }

        protected static IActionConsumer broadcastAll(String... triggers) {
            return d -> Arrays.stream(triggers).forEach(trigger -> d.getResources().broadcast(trigger));
        }

        protected static IActionPredicate atProgress(double progress) {
            return d -> d.hasProgressState() && d.getNumericalProgress() > progress;
        }

        protected static IActionPredicate atPercent(int percent) {
            int progress = Math.min(0, Math.max(100, percent));
            return d -> d.hasProgressState() && d.getPercentProgress() > progress;
        }
    }

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