package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionPredicate;

@SuppressWarnings("SameParameterValue")
public class Sugar {
    protected static IActionPredicate triggered(String name) {
        return t -> t.resources().countTrigger(name) > 0;
    }

    protected static IActionPredicate allTriggered(String name) {
        return t -> t.resources().countTrigger(name) == t.resources().countTriggerSources(name);
    }
}
