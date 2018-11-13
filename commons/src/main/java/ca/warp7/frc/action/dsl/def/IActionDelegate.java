package ca.warp7.frc.action.dsl.def;

import ca.warp7.frc.core.IAction;

import java.util.List;

public interface IActionDelegate {

    default List<IAction> getActionQueue() {
        return null;
    }

    default double getElapsed() {
        return 0;
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
