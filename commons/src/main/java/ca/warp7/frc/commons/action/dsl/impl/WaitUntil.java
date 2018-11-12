package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.action.dsl.IActionDelegate;

import java.util.function.Predicate;

public class WaitUntil extends BaseAction {
    private Predicate<IActionDelegate> mSupplier;

    WaitUntil(Predicate<IActionDelegate> supplier) {
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
