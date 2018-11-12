package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionConsumer;

class Consume extends BaseAction {
    private IActionConsumer mConsumer;

    Consume(IActionConsumer consumer) {
        mConsumer = consumer;
    }

    @Override
    public void onStart() {
        mConsumer.accept(this);
    }
}
