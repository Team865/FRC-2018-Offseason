package ca.warp7.action.impl;

import ca.warp7.action.IAction;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

abstract class AsyncForward extends ActionBase {

    final List<IAction> mActions;

    AsyncForward(IAction... actions) {
        mActions = Arrays.asList(actions);
        mActions.forEach(action -> link(this, action));
        //mActions.forEach(action -> System.out.println("Adding Async: " + action + " to " + AsyncForward.this));
    }

    @Override
    public List<IAction> getQueue() {
        return mActions.size() == 1 ? Collections.singletonList(mActions.get(0)) : null;
    }

    @Override
    public void _onStart() {
        mActions.forEach(IAction::onStart);
    }

    @Override
    public void _onUpdate() {
        mActions.forEach(IAction::onUpdate);
    }

    @Override
    public void _onStop() {
        mActions.forEach(IAction::onStop);
    }

    static class All extends AsyncForward {

        All(IAction... actions) {
            super(actions);
        }

        @Override
        public boolean _shouldFinish() {
            return mActions.stream().allMatch(IAction::shouldFinish);
        }
    }

    static class Any extends AsyncForward {

        Any(IAction... actions) {
            super(actions);
        }

        @Override
        public boolean _shouldFinish() {
            return mActions.stream().anyMatch(IAction::shouldFinish);
        }
    }

}
