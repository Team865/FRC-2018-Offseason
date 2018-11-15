package ca.warp7.action;

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
 * @version 3.0 (Revision #11) Revised 11/14/2018
 */

@SuppressWarnings("ALL")
public interface IActionAPI extends IAction {

    IActionAPI asyncAll(IAction... actions);

    IActionAPI asyncAny(IAction... actions);

    IActionAPI asyncInverse(IAction... actions);

    IActionAPI asyncMaster(IAction master, IAction... slaves);

    IActionAPI await(IActionPredicate predicate);

    IActionAPI exec(IActionConsumer consumer);

    IActionAPI iterate(IActionConsumer consumer);

    IActionAPI runIf(IActionPredicate predicate, IAction ifAction, IAction elseAction);

    IActionAPI queue(IAction... actions);

    abstract class FunctionProvider {

        protected static IActionPredicate triggeredOnce(String name) {
            return d -> d.getResources().getBroadcastCount(name) == 1;
        }

        protected static IActionPredicate triggeredRepeat(String name) {
            return d -> d.getResources().getBroadcastCount(name) > 1;
        }

        protected static IActionPredicate triggeredAll(String name) {
            return d -> d.getResources().getBroadcastCount(name) == d.getResources().getBroadcastSources(name);
        }

        protected static IActionPredicate triggeredSome(String name, int times) {
            return d -> d.getResources().getBroadcastCount(name) == times;
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

    default IActionAPI async(IAction... actions) {
        return asyncAll(actions);
    }

    default IActionAPI asyncUntil(IActionPredicate predicate, IAction... actions) {
        return asyncMaster(await(predicate), actions);
    }

    default IActionAPI broadcast(String... triggers) {
        return exec(FunctionProvider.broadcastAll(triggers));
    }

    default IActionAPI broadcastWhen(IActionPredicate predicate, String... triggers) {
        return await(predicate).broadcast(triggers);
    }

    default IActionAPI onlyIf(IActionPredicate predicate, IAction action) {
        return runIf(predicate, action, null);
    }

    default IActionAPI waitFor(double seconds) {
        return await(FunctionProvider.elapsed(seconds));
    }

    default IActionAPI when(IActionPredicate predicate, IAction... actions) {
        return await(predicate).queue(actions);
    }

    abstract class Head extends FunctionProvider implements IActionAPI {

        @Override
        public IActionAPI asyncAll(IAction... actions) {
            return queue().asyncAll(actions);
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
        public IActionAPI await(IActionPredicate predicate) {
            return queue().await(predicate);
        }

        @Override
        public IActionAPI broadcast(String... triggers) {
            return queue().broadcast(triggers);
        }

        @Override
        public IActionAPI exec(IActionConsumer consumer) {
            return queue().exec(consumer);
        }

        @Override
        public IActionAPI iterate(IActionConsumer consumer) {
            return queue().iterate(consumer);
        }

        @Override
        public IActionAPI runIf(IActionPredicate predicate, IAction ifAction, IAction elseAction) {
            return queue().runIf(predicate, ifAction, elseAction);
        }

        @Override
        public void onStart() {
        }
    }
}