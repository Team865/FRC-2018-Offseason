package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionConsumer;
import ca.warp7.frc.core.IAction;

class AsyncWatch extends BaseAction {

    private IAction mAction;
    private IActionConsumer mConsumer;

    AsyncWatch(IAction action, IActionConsumer consumer) {
        mAction = action;
        mConsumer = consumer;
    }

    @Override
    public void _onStart() {
        mAction.onStart();
        mConsumer.accept(this);
    }

    @Override
    public boolean shouldFinish() {
        return mAction.shouldFinish();
    }

    @Override
    public void _onUpdate() {
        mAction.onUpdate();
        mConsumer.accept(this);
    }

    @Override
    public void _onStop() {
        mAction.onStop();
        mConsumer.accept(this);
    }
}
