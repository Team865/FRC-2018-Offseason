package ca.warp7.action.impl;

public class Await extends ActionBase {
    private Predicate mPredicate;

    Await(Predicate predicate) {
        mPredicate = predicate;
    }

    @Override
    public boolean _shouldFinish() {
        return mPredicate.test(this);
    }
}
