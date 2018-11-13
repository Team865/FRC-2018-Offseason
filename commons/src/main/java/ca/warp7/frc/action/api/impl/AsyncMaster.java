package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.IAction;

class AsyncMaster extends AsyncBase {

    private IAction mMaster;

    AsyncMaster(IAction master, IAction... slaves) {
        super(slaves);
        mMaster = master;
    }

    @Override
    public int size() {
        return super.size() + 1;
    }

    @Override
    public void _onStart() {
        link(this, mMaster);
        mMaster.onStart();
        super._onStart();
    }

    @Override
    public boolean _shouldFinish() {
        return mMaster.shouldFinish();
    }

    @Override
    public void _onUpdate() {
        mMaster.onUpdate();
        super._onUpdate();
    }

    @Override
    public void _onStop() {
        mMaster.onStop();
        super._onStop();
    }
}
