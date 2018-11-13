package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionPredicate;

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
