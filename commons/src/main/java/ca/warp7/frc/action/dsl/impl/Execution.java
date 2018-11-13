package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionConsumer;

class Execution extends BaseAction {
    private IActionConsumer mConsumer;

    Execution(IActionConsumer consumer) {
        mConsumer = consumer;
    }

    @Override
    public void _onStart() {
        mConsumer.accept(this);
    }

    @Override
    void _onUpdate() {

    }

    @Override
    void _onStop() {

    }
}
