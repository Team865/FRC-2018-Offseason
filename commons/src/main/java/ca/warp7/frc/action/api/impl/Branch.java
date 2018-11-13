package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.def.IActionPredicate;
import ca.warp7.frc.core.IAction;

class Branch extends BaseAction {
    private IActionPredicate mPredicate;
    private IAction mIf;
    private IAction mElse;
    private IAction mSelected;

    Branch(IActionPredicate predicate, IAction ifAction, IAction elseAction) {
        mPredicate = predicate;
        mIf = ifAction;
        mElse = elseAction;
    }

    @Override
    public void _onStart() {
        mSelected = mPredicate.test(this) ? mIf : mElse;
        if (mSelected != null) mSelected.onStart();
    }

    @Override
    public void _onUpdate() {
        if (mSelected != null) mSelected.onUpdate();
    }

    @Override
    public void _onStop() {
        if (mSelected != null) mSelected.onStop();
    }

    @Override
    public boolean _shouldFinish() {
        return mSelected == null || mSelected.shouldFinish();
    }
}
