package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.IAction;
import ca.warp7.frc.action.api.IActionDelegate;
import ca.warp7.frc.action.api.IActionParent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

abstract class AsyncBase extends BaseAction implements IActionParent {

    final List<IAction> mActions;

    AsyncBase(IAction... actions) {
        mActions = Arrays.asList(actions);
    }

    @Override
    public List<IAction> getActionQueue() {
        if (mActions.size() == 1) return Collections.singletonList(mActions.get(0));
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
        mActions.forEach(action -> {
            link(this, action);
            action.onStart();
        });
    }

    @Override
    public abstract boolean _shouldFinish();

    @Override
    public void _onUpdate() {
        mActions.forEach(IAction::onUpdate);
    }

    @Override
    public void _onStop() {
        mActions.forEach(IAction::onStop);
    }
}
