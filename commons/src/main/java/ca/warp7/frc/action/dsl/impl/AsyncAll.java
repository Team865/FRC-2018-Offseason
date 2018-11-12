package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.core.IAction;

class AsyncAll extends AsyncBase {
    AsyncAll(IAction... actions) {
        super(actions);
    }

    @Override
    public boolean shouldFinish() {
        for (IAction action : mActions) if (!action.shouldFinish()) return false;
        return true;
    }
}
