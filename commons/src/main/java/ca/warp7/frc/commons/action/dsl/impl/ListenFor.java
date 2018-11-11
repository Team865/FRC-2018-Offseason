package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.core.IAction;

import java.util.Arrays;
import java.util.List;

class ListenFor extends BaseAction {

    private List<String> mTriggers;
    private IAction mReceiver;
    private boolean mReceived;

    ListenFor(IAction receiver, String... triggers) {
        mReceiver = receiver;
        mTriggers = Arrays.asList(triggers);
        mReceived = false;
    }

    @Override
    public boolean shouldFinish() {
        return mReceived && mReceiver.shouldFinish();
    }

    @Override
    void onReceive(String message) {
        mReceived = mReceived || mTriggers.stream().anyMatch(message::equals);
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
