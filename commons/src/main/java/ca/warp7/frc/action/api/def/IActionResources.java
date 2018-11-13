package ca.warp7.frc.action.api.def;

@SuppressWarnings("unused")
public interface IActionResources {
    void put(String name, Object value);

    Object get(String name, Object defaultVal);

    int countTrigger(String trigger);

    int countTriggerSources(String trigger);

    void addBroadcastSource(String trigger);

    String broadcastName(String trigger);

    double getTime();

    default void broadcast(String trigger) {
        String name = broadcastName(trigger);
        put(name, getInt(name, 0) + 1);
    }

    default double getDouble(String name, double defaultVal) {
        Object var = get(name, null);
        if (var instanceof Double) return (double) var;
        return defaultVal;
    }

    default int getInt(String name, int defaultVal) {
        Object var = get(name, null);
        if (var instanceof Integer) return (int) var;
        return defaultVal;
    }

    default String getString(String name, String defaultVal) {
        Object var = get(name, null);
        if (var instanceof String) return (String) var;
        return defaultVal;
    }
}