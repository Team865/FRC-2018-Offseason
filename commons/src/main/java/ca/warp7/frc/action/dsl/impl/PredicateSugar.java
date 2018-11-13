package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionPredicate;

@SuppressWarnings({"SameParameterValue", "unused"})
public class PredicateSugar {
    protected static IActionPredicate triggeredOnce(String name) {
        return t -> t.resources().countTrigger(name) == 1;
    }

    protected static IActionPredicate triggeredRepeat(String name) {
        return t -> t.resources().countTrigger(name) > 1;
    }

    protected static IActionPredicate allTriggered(String name) {
        return t -> t.resources().countTrigger(name) == t.resources().countTriggerSources(name);
    }

    protected static IActionPredicate someTriggered(String name, int times) {
        return t -> t.resources().countTrigger(name) == times;
    }

    protected static IActionPredicate elapsed(double timeInSeconds) {
        return d -> !d.hasParent() || d.parent().delegate().elapsed() > timeInSeconds;
    }
}
