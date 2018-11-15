package ca.warp7.action.impl;

import ca.warp7.action.IActionPredicate;

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
