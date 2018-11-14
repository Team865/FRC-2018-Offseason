package ca.warp7.frc.action.api;

import java.util.Arrays;

/**
 * Provides convenience wrappers around common functions
 */
@SuppressWarnings({"SameParameterValue", "unused", "WeakerAccess"})
public class SyntaxProvider {
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
