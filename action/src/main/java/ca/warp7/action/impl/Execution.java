package ca.warp7.action.impl;

import ca.warp7.action.IActionConsumer;

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
