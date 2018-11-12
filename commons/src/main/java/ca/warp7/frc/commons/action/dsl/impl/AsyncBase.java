package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.action.dsl.IActionParent;
import ca.warp7.frc.commons.core.IAction;

import java.util.Arrays;
import java.util.List;

abstract class AsyncBase extends BaseAction implements IActionParent {

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
        mActions.forEach(IAction::onStart);
    }

    @Override
    public abstract boolean shouldFinish();

    @Override
    public void onUpdate() {
        mActions.forEach(IAction::onUpdate);
    }

    @Override
    public void onStop() {
        mActions.forEach(IAction::onStop);
    }
}
