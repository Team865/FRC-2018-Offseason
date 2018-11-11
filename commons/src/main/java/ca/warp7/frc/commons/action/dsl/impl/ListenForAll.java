package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.core.IAction;

import java.util.Arrays;
import java.util.List;

class ListenForAll extends BaseAction {

    private List<Object> mTriggers;
    private IAction mReceiver;
    private boolean mReceived;

    ListenForAll(IAction receiver, Object... triggers) {
        mReceiver = receiver;
        mTriggers = Arrays.asList(triggers);
        mReceived = false;
    }

    @Override
    public boolean shouldFinish() {
        return mReceived && mReceiver.shouldFinish();
    }

    @Override
    void onReceive(Object trigger) {
        mTriggers.removeIf(trigger::equals);
        mReceived = !mReceived && mTriggers.isEmpty();
        if (mReceived) mReceiver.onStart();
    }

    @Override
    public void onUpdate() {
        if (mReceived) mReceiver.onUpdate();
    }

    @Override
    public void onStop() {
        if (mReceived) mReceiver.onStop();
    }
}
