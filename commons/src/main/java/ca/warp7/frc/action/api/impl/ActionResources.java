package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.IActionResources;
import ca.warp7.frc.action.api.IActionTimer;

import java.util.HashMap;
import java.util.Map;

public class ActionResources implements IActionResources {

    private Map<String, Object> mResPool = new HashMap<>();
    private Map<String, Integer> mSourceCount = new HashMap<>();
    private IActionTimer mTimer;

    @Override
    public void put(String name, Object value) {
        mResPool.put(name, value);
    }

    @Override
    public Object get(String name, Object defaultVal) {
        return mResPool.getOrDefault(name, defaultVal);
    }

    @Override
    public int countTrigger(String trigger) {
        return getInt(broadcastName(trigger), 0);
    }

    @Override
    public int countTriggerSources(String trigger) {
        return mSourceCount.getOrDefault(broadcastName(trigger), 0);
    }

    @Override
    public void addBroadcastSource(String trigger) {
        String name = broadcastName(trigger);
        mSourceCount.put(name, mSourceCount.getOrDefault(name, 0) + 1);
    }

    @Override
    public String broadcastName(String trigger) {
        return "Broadcast/" + trigger;
    }

    @Override
    public void setActionTimer(IActionTimer timer) {
        mTimer = timer;
    }

    @Override
    public IActionTimer getActionTimer() {
        return mTimer;
    }

    @Override
    public double getTime() {
        return mTimer != null ? mTimer.getTime() : 0;
    }
}
