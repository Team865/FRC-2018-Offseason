package ca.warp7.action.impl;

public class Iteration extends ActionBase {

    private Consumer mConsumer;

    Iteration(Consumer consumer) {
        mConsumer = consumer;
    }

    @Override
    public void _onUpdate() {
        mConsumer.accept(this);
    }
}
