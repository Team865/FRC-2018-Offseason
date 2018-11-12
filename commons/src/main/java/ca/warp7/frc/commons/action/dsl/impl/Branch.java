package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.action.dsl.IActionDelegate;
import ca.warp7.frc.commons.core.IAction;

import java.util.function.Predicate;

class Branch extends BaseAction {
    private Predicate<IActionDelegate> mPredicate;
    private IAction mIf;
    private IAction mElse;
    private IAction mSelected;

    Branch(Predicate<IActionDelegate> predicate, IAction ifAction, IAction elseAction) {
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
