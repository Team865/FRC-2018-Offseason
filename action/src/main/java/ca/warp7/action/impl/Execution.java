package ca.warp7.action.impl;

class Execution extends ActionBase {
    private Consumer mConsumer;

    Execution(Consumer consumer) {
        mConsumer = consumer;
    }

    @Override
    public void prepare() {
        mConsumer.accept(this);
    }
}
