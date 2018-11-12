package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionParent;
import ca.warp7.frc.core.IAction;

import java.util.Arrays;
import java.util.List;

class ListenForAny extends BaseAction implements IActionParent {

    private List<Object> mTriggers;
    private IAction mReceiver;
    private boolean mReceived;

    ListenForAny(IAction receiver, Object... triggers) {
        mReceiver = receiver;
        mTriggers = Arrays.asList(triggers);
        mReceived = false;
    }

    @Override
    public boolean shouldFinish() {
        return mReceived && mReceiver.shouldFinish();
    }

    @Override
    void onReceive(Object message) {
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
