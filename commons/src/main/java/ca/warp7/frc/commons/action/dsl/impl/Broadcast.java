package ca.warp7.frc.commons.action.dsl.impl;

class Broadcast extends BaseAction {

    private Object[] mTriggers;

    Broadcast(Object... message) {
        mTriggers = message;
    }

    @Override
    public void onStart() {
        for (Object trigger : mTriggers) {
            onBroadcast(trigger);
        }
    }
}
