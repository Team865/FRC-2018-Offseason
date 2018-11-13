package ca.warp7.frc.action.dsl.def;


import ca.warp7.frc.core.IAction;

public interface IActionParent {

    default void insert(IAction action) {
    }

    default IActionDelegate getDelegate() {
        return null;
    }

    default int size() {
        return 0;
    }
}