package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.def.IActionTimer;
import ca.warp7.frc.core.IAction;

@SuppressWarnings("unused")
public class DetachedRunner {
    static IAction create(IActionTimer timer, double interval, double timeout, BaseAction action) {
        if (action != null) action.getResources().setTimer(timer);
        return new RunThread(interval, timeout, action);
    }
}