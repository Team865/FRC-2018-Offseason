package ca.warp7.action.impl;

import ca.warp7.action.IAction;
import ca.warp7.action.IActionDelegate;
import ca.warp7.action.IActionParent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class AsyncInverse extends ActionBase implements IActionParent {

    private final List<Inverse> mInverse = new ArrayList<>();

    AsyncInverse(IAction... actions) {
        for (IAction action : actions) {
            Inverse inverse = new Inverse();
            inverse.action = action;
            inverse.delegate = action instanceof IActionDelegate ? (IActionDelegate) action : null;
            inverse.hasDelegate = inverse.delegate != null;
            inverse.started = false;
            inverse.remainingTime = 0;
            mInverse.add(inverse);
        }
    }

    @Override
    public List<IAction> getActionQueue() {
        if (mInverse.size() == 1) return Collections.singletonList(mInverse.get(0).action);
        return null;
    }

    @Override
    public IActionDelegate getDelegate() {
        return this;
    }

    @Override
    public int size() {
        return mInverse.size();
    }

    @Override
    public void _onStart() {
        mInverse.forEach(inverse -> link(this, inverse.action));
    }

    @Override
    public void _onUpdate() {
        mInverse.forEach(inverse -> inverse.remainingTime = inverse.hasDelegate &&
                inverse.delegate.hasRemainingTime() ? inverse.delegate.getRemainingTime() : 0);
        double maxRemaining = 0;
        for (Inverse inverse : mInverse) maxRemaining = Math.max(maxRemaining, inverse.remainingTime);
        double finalMaxRemaining = maxRemaining;
        mInverse.forEach(inverse -> {
            if (inverse.remainingTime == finalMaxRemaining) {
                inverse.started = true;
                inverse.action.onStart();
            }
        });
        mInverse.stream().filter(inverse -> inverse.started).forEach(inverse -> inverse.action.onUpdate());
    }

    @Override
    public void _onStop() {
        mInverse.stream().filter(inverse -> inverse.started).forEach(inverse -> inverse.action.onStop());
    }

    private static class Inverse {
        private IAction action;
        private IActionDelegate delegate;
        private boolean hasDelegate;
        private boolean started;
        private double remainingTime;
    }
}
