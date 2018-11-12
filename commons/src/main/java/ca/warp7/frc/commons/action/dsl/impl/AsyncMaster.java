package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.core.IAction;

class AsyncMaster extends AsyncBase {

    private IAction mMaster;

    AsyncMaster(IAction master, IAction... slaves) {
        super(slaves);
        mMaster = master;
    }

    @Override
    public void onStart() {
        mMaster.onStart();
        super.onStart();
    }

    @Override
    public boolean shouldFinish() {
        return mMaster.shouldFinish();
    }

    @Override
    public void onUpdate() {
        mMaster.onUpdate();
        super.onUpdate();
    }

    @Override
    public void onStop() {
        mMaster.onStop();
        super.onStop();
    }
}
