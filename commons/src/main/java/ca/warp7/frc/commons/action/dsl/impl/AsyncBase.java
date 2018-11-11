package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.core.IAction;

import java.util.Arrays;
import java.util.List;

abstract class AsyncBase extends BaseAction {

    final List<IAction> mActions;

    AsyncBase(IAction... actions) {
        mActions = Arrays.asList(actions);
    }

    @Override
    List<IAction> getChildren() {
        return mActions;
    }

    @Override
    public void onStart() {
        for (IAction action : mActions) {
            action.onStart();
        }
    }

    @Override
    public abstract boolean shouldFinish();

    @Override
    public void onUpdate() {
        for (IAction action : mActions) {
            action.onUpdate();
        }
    }

    @Override
    public void onStop() {
        for (IAction action : mActions) {
            action.onStop();
        }
    }
}
