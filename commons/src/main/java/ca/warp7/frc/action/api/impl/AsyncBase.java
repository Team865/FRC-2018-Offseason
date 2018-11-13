package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.def.IActionDelegate;
import ca.warp7.frc.action.api.def.IActionParent;
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
    public IActionDelegate getDelegate() {
        return this;
    }

    @Override
    public int size() {
        return mActions.size();
    }

    @Override
    public void _onStart() {
        mActions.forEach(IAction::onStart);
    }

    @Override
    public abstract boolean shouldFinish();

    @Override
    public void _onUpdate() {
        mActions.forEach(IAction::onUpdate);
    }

    @Override
    public void _onStop() {
        mActions.forEach(IAction::onStop);
    }
}
