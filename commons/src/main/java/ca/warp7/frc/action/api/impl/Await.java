package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.def.IActionPredicate;

public class Await extends BaseAction {
    private IActionPredicate mPredicate;

    Await(IActionPredicate predicate) {
        mPredicate = predicate;
    }

    @Override
    public boolean shouldFinish() {
        return mPredicate.test(this);
    }
}
