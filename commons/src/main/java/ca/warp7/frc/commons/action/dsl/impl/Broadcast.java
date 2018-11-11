package ca.warp7.frc.commons.action.dsl.impl;

class Broadcast extends BaseAction {

    private String mMessage;

    Broadcast(String message) {
        mMessage = message;
    }

    @Override
    public void onStart() {
        onBroadcast(mMessage);
    }
}
