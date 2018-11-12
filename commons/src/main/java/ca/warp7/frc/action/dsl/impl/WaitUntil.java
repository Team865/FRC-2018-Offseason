package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionPredicate;

public class WaitUntil extends BaseAction {
    private IActionPredicate mSupplier;

    WaitUntil(IActionPredicate supplier) {
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
