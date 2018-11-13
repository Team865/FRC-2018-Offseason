package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionPredicate;

public class Await extends BaseAction {
    private IActionPredicate mSupplier;

    Await(IActionPredicate supplier) {
        mSupplier = supplier;
    }

    @Override
    public void onStart() {
    }

    @Override
    public boolean shouldFinish() {
        return mSupplier.test(this);
    }
}
