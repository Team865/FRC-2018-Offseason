package ca.warp7.action.impl;

import ca.warp7.action.IAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Async extends ActionBase {
    private final List<State> mStates;
    private final AsyncStartMode mStartMode;
    private final AsyncStopMode mStopMode;
    private double mStaticEstimate;
    private double mInterval;

    Async(AsyncStartMode startMode, AsyncStopMode stopMode, IAction... actions) {
        mStartMode = startMode;
        mStopMode = stopMode;
        mStates = new ArrayList<>();
        for (IAction action : actions) mStates.add(new State(action));
        mStates.forEach(action -> linkChild(this, action.realAction));
    }

    @Override
    public List<IAction> getQueue() {
        return mStates.size() == 1 ? Collections.singletonList(mStates.get(0).realAction) : null;
    }

    @Override
    void prepare() {
        mStates.forEach(State::updateRemaining);
        mStates.forEach(State::updateStaticRemaining);
        for (State state : mStates) if (state.remaining > mStaticEstimate) mStaticEstimate = state.remaining;
        if (mStartMode == AsyncStartMode.OnStart) mStates.forEach(State::start);
        mInterval = getResources().getInterval();
    }

    @Override
    public void update() {
        double elapsed = getElapsed();
        mStates.forEach(State::updateRemaining);
        switch (mStartMode) {
            case OnStart:
                break;
            case OnStaticInverse:
                double staticRemaining = mStaticEstimate - elapsed - mInterval;
                for (State state : mStates) if (state.staticRemaining > staticRemaining) state.start();
                break;
            case OnDynamicInverse:
                break;
        }
        switch (mStopMode) {
            case OnEachFinished:
                break;
            case OnAnyFinished:
                break;
            case OnAllFinished:
                break;
            case OnStaticEstimate:
                break;
        }
        mStates.forEach(State::update);
    }

    @Override
    public boolean shouldFinish() {
        switch (mStopMode) {
            case OnAnyFinished:
                for (State state : mStates) if (state.shouldFinish()) return true;
                return false;
            case OnEachFinished:
            case OnAllFinished:
                for (State state : mStates) if (!state.shouldFinish()) return false;
                return true;
            case OnStaticEstimate:
                return getElapsed() > mStaticEstimate;
        }
        return true;
    }

    @Override
    public void stop() {
    }

    static class State implements IAction {
        private Delegate delegate;
        private boolean isRunning;
        private boolean hasRemaining;
        private boolean shouldUpdate;

        IAction realAction;
        double remaining;
        double staticRemaining;

        State(IAction action) {
            realAction = action;
            delegate = action instanceof Delegate ? (Delegate) action : null;
            isRunning = false;
            remaining = 0;
            staticRemaining = 0;
            hasRemaining = delegate != null && delegate.hasRemainingTime();
        }

        void updateRemaining() {
            if (hasRemaining && shouldUpdate) remaining = delegate.getRemainingTime();
            shouldUpdate = false;
        }

        void updateStaticRemaining() {
            staticRemaining = remaining;
        }

        @Override
        public void start() {
            isRunning = true;
            shouldUpdate = true;
            realAction.start();
        }

        @Override
        public boolean shouldFinish() {
            return !isRunning || realAction.shouldFinish();
        }

        @Override
        public void update() {
            if (isRunning) {
                realAction.update();
                shouldUpdate = true;
            }
        }

        @Override
        public void stop() {
            if (isRunning) realAction.stop();
        }
    }
}
