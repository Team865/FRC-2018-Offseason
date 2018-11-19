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
    public void _start() {
        mSelected = mPredicate.test(this) ? mIf : mElse;
        if (mSelected != null) mSelected.start();
    }

    @Override
    public void _update() {
        if (mSelected != null) mSelected.update();
    }

    @Override
    public void _stop() {
        if (mSelected != null) mSelected.stop();
    }

    @Override
    public boolean _shouldFinish() {
        return mSelected == null || mSelected.shouldFinish();
    }
}
