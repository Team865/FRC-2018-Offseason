package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionPredicate;
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
    public void onStart() {
        mSelected = mPredicate.test(this) ? mIf : mElse;
        mSelected.onStart();
    }

    @Override
    public void onUpdate() {
        mSelected.onUpdate();
    }

    @Override
    public void onStop() {
        mSelected.onStop();
    }

    @Override
    public boolean shouldFinish() {
        return mSelected.shouldFinish();
    }

}
