package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.core.IAction;

import java.util.function.Predicate;

public class WaitUntil extends BaseAction {
    private Predicate<IAction> mSupplier;

    WaitUntil(Predicate<IAction> supplier) {
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
