package ca.warp7.action.impl;

import ca.warp7.action.IAction;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

abstract class AsyncForward extends ActionBase implements IAction.Parent {

    final List<IAction> mActions;

    AsyncForward(IAction... actions) {
        mActions = Arrays.asList(actions);
    }

    @Override
    public List<IAction> getActionQueue() {
        if (mActions.size() == 1) return Collections.singletonList(mActions.get(0));
        return null;
    }

    @Override
    public Delegate getDelegate() {
        return this;
    }

    @Override
    public int size() {
        return mActions.size();
    }

    @Override
    public void _onStart() {
        mActions.forEach(action -> link(this, action));
        mActions.forEach(IAction::onStart);
    }

    @Override
    public abstract boolean _shouldFinish();

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
            for (IAction action : mActions) if (!action.shouldFinish()) return false;
            return true;
        }
    }

    static class Any extends AsyncForward {

        Any(IAction... actions) {
            super(actions);
        }

        @Override
        public boolean _shouldFinish() {
            for (IAction action : mActions) if (action.shouldFinish()) return true;
            return false;
        }
    }

    static class Master extends AsyncForward {

        private IAction mMaster;

        Master(IAction master, IAction... slaves) {
            super(slaves);
            mMaster = master;
        }

        @Override
        public int size() {
            return super.size() + 1;
        }

        @Override
        public void _onStart() {
            link(this, mMaster);
            mMaster.onStart();
            super._onStart();
        }

        @Override
        public boolean _shouldFinish() {
            return mMaster.shouldFinish();
        }

        @Override
        public void _onUpdate() {
            mMaster.onUpdate();
            super._onUpdate();
        }

        @Override
        public void _onStop() {
            mMaster.onStop();
            super._onStop();
        }
    }
}
