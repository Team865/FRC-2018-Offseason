package ca.warp7.frc.action.dsl.def;

public interface IActionResources {
    void setVar(String name, Object value);

    Object getVar(String name, Object defaultVal);

    int countTrigger(String name);

    int countTriggerSources(String name);

    default void broadcast(String trigger) {
        setVar(trigger, getInt(trigger, 0) + 1);
    }

    default double getDouble(String name, double defaultVal) {
        Object var = getVar(name, null);
        if (var instanceof Double) return (double) var;
        return defaultVal;
    }

    default int getInt(String name, int defaultVal) {
        Object var = getVar(name, null);
        if (var instanceof Integer) return (int) var;
        return defaultVal;
    }

    default String getString(String name, String defaultVal) {
        Object var = getVar(name, null);
        if (var instanceof String) return (String) var;
        return defaultVal;
    }
}