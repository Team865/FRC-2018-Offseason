package ca.warp7.action.impl;

import ca.warp7.action.IAction;

import java.util.HashMap;
import java.util.Map;

public class ActionResources implements IAction.Resources {

    private Map<String, Object> mPool = new HashMap<>();
    private Map<String, Integer> mBroadcastSources = new HashMap<>();
    private IAction.ITimer mTimer;
    private double mStartTime = 0;

    @Override
    public void put(String name, Object value) {
        mPool.put(name, value);
    }

    @Override
    public Object get(String name, Object defaultValue) {
        return mPool.getOrDefault(name, defaultValue);
    }

    @Override
    public int getBroadcastCount(String trigger) {
        return getInt(broadcastName(trigger), 0);
    }

    @Override
    public int getBroadcastSources(String trigger) {
        return mBroadcastSources.getOrDefault(broadcastName(trigger), 0);
    }

    @Override
    public void addBroadcastSources(String... triggers) {
        for (String trigger : triggers) {
            String name = broadcastName(trigger);
            mBroadcastSources.put(name, mBroadcastSources.getOrDefault(name, 0) + 1);
        }
    }

    @Override
    public String broadcastName(String trigger) {
        return "Broadcast/" + trigger;
    }

    @Override
    public IAction.ITimer getActionTimer() {
        return mTimer;
    }

    @Override
    public void setActionTimer(IAction.ITimer timer) {
        mTimer = timer;
    }

    @Override
    public double getTime() {
        return mTimer != null ? mTimer.getTime() : 0;
    }

    @Override
    public double getTotalElapsed() {
        if (mStartTime == 0) return 0;
        return mTimer.getTime() - mStartTime;
    }

    @Override
    public void startTimer() {
        if (mStartTime == 0) mStartTime = mTimer.getTime();
    }
}