package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.core.IAction;

class AsyncAny extends AsyncBase {
    AsyncAny(IAction... actions) {
        super(actions);
    }

    @Override
    public boolean shouldFinish() {
        for (IAction action : mActions) if (action.shouldFinish()) return true;
        return false;
    }
}
