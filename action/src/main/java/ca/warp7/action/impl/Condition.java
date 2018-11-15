package ca.warp7.action.impl;

import ca.warp7.action.IAction;

class Condition extends ActionBase {
    private Predicate mPredicate;
    private IAction mIf;
    private IAction mElse;
    private IAction mSelected;

    Condition(Predicate predicate, IAction ifAction, IAction elseAction) {
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
