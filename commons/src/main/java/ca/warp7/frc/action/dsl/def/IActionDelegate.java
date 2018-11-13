package ca.warp7.frc.action.dsl.def;

public interface IActionDelegate {

    void setParent(IActionParent parent);

    default double getElapsed() {
        return 0;
    }

    default boolean isDetached() {
        return false;
    }

    default double getTotalElapsed() {
        return 0;
    }

    default IActionParent asParent() {
        if (this instanceof IActionParent) return (IActionParent) this;
        return null;
    }

    default boolean hasParent() {
        return getParent() != null;
    }

    default IActionParent getParent() {
        return null;
    }

    default IActionParent getRoot() {
        return null;
    }

    default void setVar(String name, Object value) {
    }

    default Object getVar(String name, Object defaultVal) {
        return defaultVal;
    }

    default double getDouble(String name, double defaultVal) {
        return defaultVal;
    }

    default boolean isConsumed(IActionConsumer consumer) {
        return true;
    }

    default void interrupt() {
    }

    default int countTrigger(String name) {
        return 0;
    }

    default int countTriggerSources(String name) {
        return 0;
    }
}
