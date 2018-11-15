package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.IActionConsumer;

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
