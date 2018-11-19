package ca.warp7.action.impl;

public class Iteration extends ActionBase {

    private Consumer mConsumer;

    Iteration(Consumer consumer) {
        mConsumer = consumer;
    }

    @Override
    boolean _shouldFinish() {
        return false;
    }

    @Override
    public void _update() {
        mConsumer.accept(this);
    }
}
