package ca.warp7.frc.action.api;

import java.util.Arrays;

@SuppressWarnings({"SameParameterValue", "unused", "WeakerAccess"})
public class SyntaxProvider {
    protected static IActionPredicate triggeredOnce(String name) {
        return t -> t.getResources().countTrigger(name) == 1;
    }

    protected static IActionPredicate triggeredRepeat(String name) {
        return t -> t.getResources().countTrigger(name) > 1;
    }

    protected static IActionPredicate allTriggered(String name) {
        return t -> t.getResources().countTrigger(name) == t.getResources().countTriggerSources(name);
    }

    protected static IActionPredicate someTriggered(String name, int times) {
        return t -> t.getResources().countTrigger(name) == times;
    }

    protected static IActionPredicate elapsed(double timeInSeconds) {
        return d -> !d.hasParent() || d.getParent().getDelegate().getElapsed() > timeInSeconds;
    }

    protected static IActionConsumer broadcastTriggers(String... triggers) {
        return d -> Arrays.stream(triggers).forEach(trigger -> d.getResources().broadcast(trigger));
    }
}
