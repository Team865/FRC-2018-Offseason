package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionConsumer;

class Execution extends BaseAction {
    private IActionConsumer mConsumer;

    Execution(IActionConsumer consumer) {
        mConsumer = consumer;
    }

    @Override
    public void onStart() {
        mConsumer.accept(this);
    }
}
