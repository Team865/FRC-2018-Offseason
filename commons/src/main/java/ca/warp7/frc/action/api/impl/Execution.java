package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.IActionConsumer;

class Execution extends ActionBase {
    private IActionConsumer mConsumer;

    Execution(IActionConsumer consumer) {
        mConsumer = consumer;
    }

    @Override
    public void _onStart() {
        mConsumer.accept(this);
    }
}
