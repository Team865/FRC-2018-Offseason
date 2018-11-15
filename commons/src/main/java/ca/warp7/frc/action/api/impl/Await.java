package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.IActionPredicate;

public class Await extends ActionBase {
    private IActionPredicate mPredicate;

    Await(IActionPredicate predicate) {
        mPredicate = predicate;
    }

    @Override
    public boolean _shouldFinish() {
        return mPredicate.test(this);
    }
}
