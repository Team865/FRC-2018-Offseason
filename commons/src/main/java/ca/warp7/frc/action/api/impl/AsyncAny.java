package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.core.IAction;

class AsyncAny extends AsyncBase {
    AsyncAny(IAction... actions) {
        super(actions);
    }

    @Override
    public boolean _shouldFinish() {
        for (IAction action : mActions) if (action.shouldFinish()) return true;
        return false;
    }
}
