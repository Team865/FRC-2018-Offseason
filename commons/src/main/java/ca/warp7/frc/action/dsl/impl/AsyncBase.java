package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionDelegate;
import ca.warp7.frc.action.dsl.def.IActionParent;
import ca.warp7.frc.core.IAction;

import java.util.Arrays;
import java.util.List;

abstract class AsyncBase extends BaseAction implements IActionParent {

    final List<IAction> mActions;

    AsyncBase(IAction... actions) {
        mActions = Arrays.asList(actions);
    }

    @Override
    public List<IAction> getActionQueue() {
        return null;
    }

    @Override
    public void insert(IAction action) {
        mActions.add(action);
    }

    @Override
    public IActionDelegate getDelegate() {
        return this;
    }

    @Override
    public int size() {
        return mActions.size();
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
