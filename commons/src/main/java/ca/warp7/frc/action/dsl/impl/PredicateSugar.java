package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionPredicate;

@SuppressWarnings({"SameParameterValue", "unused"})
public class PredicateSugar {
    protected static IActionPredicate triggered(String name) {
        return t -> t.resources().countTrigger(name) > 0;
    }

    protected static IActionPredicate allTriggered(String name) {
        return t -> t.resources().countTrigger(name) == t.resources().countTriggerSources(name);
    }

    protected static IActionPredicate elapsed(double timeInSeconds) {
        return d -> !d.hasParent() || d.getParent().delegate().getElapsed() > timeInSeconds;
    }
}
