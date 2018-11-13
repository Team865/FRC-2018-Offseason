package ca.warp7.frc.action.dsl.def;


import ca.warp7.frc.core.IAction;

public interface IActionParent {

    default void insert(IAction action) {
    }

    default IActionDelegate getDelegate() {
        return null;
    }

    default boolean hasDelegate() {
        return false;
    }

    default boolean isModifiable() {
        return false;
    }

    default int size() {
        return 0;
    }


}