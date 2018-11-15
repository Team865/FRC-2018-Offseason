package ca.warp7.action.impl;

import ca.warp7.action.IActionConsumer;

public class Iteration extends ActionBase {

    private IActionConsumer mConsumer;

    Iteration(IActionConsumer consumer) {
        mConsumer = consumer;
    }

    @Override
    public void _onUpdate() {
        mConsumer.accept(this);
    }
}
