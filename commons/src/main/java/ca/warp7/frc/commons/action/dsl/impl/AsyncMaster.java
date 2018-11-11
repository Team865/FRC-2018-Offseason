package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.core.IAction;

class AsyncMaster implements IAction {

    AsyncMaster(IAction master, IAction... slaves) {
    }

    @Override
    public void onStart() {
    }
}
