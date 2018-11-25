package ca.warp7.action.impl;

import ca.warp7.action.IAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AsyncOp extends ActionBase {
    private final List<State> mStates;
    private final AsyncStartMode mStartMode;
    private final AsyncStopMode mStopMode;
    private double mStaticEstimate;
    private double mInterval;

    AsyncOp(AsyncStartMode startMode, AsyncStopMode stopMode, IAction... actions) {
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
        for (State s : mStates) if (s.staticRemaining > mStaticEstimate) mStaticEstimate = s.staticRemaining;
        if (mStartMode == AsyncStartMode.OnStart) mStates.forEach(State::start);
        mInterval = getResources().getInterval();
    }

    @Override
    public void update() {
        switch (mStartMode) {
            case OnStaticInverse:
                double elapsed = getElapsed();
                double staticRemaining = mStaticEstimate - elapsed - mInterval;
                for (State s : mStates) if (s.staticRemaining > staticRemaining) s.start();
                break;
            case OnDynamicInverse:
                mStates.forEach(State::updateRemaining);
                double dynamicEstimate = 0;
                for (State s : mStates) if (s.remaining > dynamicEstimate) dynamicEstimate = s.remaining;
                double dynamicRemaining = dynamicEstimate - mInterval;
                for (State s : mStates) if (s.remaining > dynamicRemaining) s.start();
                break;
        }
        mStates.forEach(State::update);
    }

    @Override
    public boolean shouldFinish() {
        switch (mStopMode) {
            case OnAnyFinished:
                for (State s : mStates) if (s.shouldFinish()) return true;
                return false;
            case OnEachFinished:
                for (State s : mStates) {
                    if (!s.shouldFinish()) return false;
                    else s.stop();
                }
                return true;
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

    static class State {
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

        public void start() {
            isRunning = true;
            shouldUpdate = true;
            realAction.start();
        }

        public boolean shouldFinish() {
            return !isRunning || realAction.shouldFinish();
        }

        public void update() {
            if (isRunning) {
                realAction.update();
                shouldUpdate = true;
            }
        }

        public void stop() {
            if (isRunning) realAction.stop();
        }
    }

    abstract static class SimpleForward extends ActionBase {

        final List<IAction> mActions;

        SimpleForward(IAction... actions) {
            mActions = Arrays.asList(actions);
            mActions.forEach(action -> linkChild(this, action));
            //mActions.forEach(action -> System.out.println("Adding Async: " + action + " to " + SimpleForward.this));
        }

        @Override
        public List<IAction> getQueue() {
            return mActions.size() == 1 ? Collections.singletonList(mActions.get(0)) : null;
        }

        @Override
        public void prepare() {
            mActions.forEach(IAction::start);
        }

        @Override
        public void update() {
            mActions.forEach(IAction::update);
        }

        @Override
        public void stop() {
            mActions.forEach(IAction::stop);
        }

    }

    static class Any extends SimpleForward {

        Any(IAction... actions) {
            super(actions);
        }

        @Override
        public boolean _shouldFinish() {
            return mActions.stream().anyMatch(IAction::shouldFinish);
        }
    }

    static class All extends SimpleForward {

        All(IAction... actions) {
            super(actions);
        }

        @Override
        public boolean _shouldFinish() {
            return mActions.stream().allMatch(IAction::shouldFinish);
        }
    }
}
