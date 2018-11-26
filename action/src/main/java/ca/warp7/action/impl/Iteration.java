package ca.warp7.action.impl;

public class Iteration extends Singleton {

    private Consumer mConsumer;

    Iteration(Consumer consumer) {
        mConsumer = consumer;
    }

    @Override
    void start_() {
    }

    @Override
    boolean shouldFinish_() {
        return false;
    }

    @Override
    public void update() {
        mConsumer.accept(this);
    }
}
