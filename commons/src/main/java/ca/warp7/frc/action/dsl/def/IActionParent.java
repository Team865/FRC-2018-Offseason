package ca.warp7.frc.action.dsl.def;


import ca.warp7.frc.core.IAction;

import java.util.List;

public interface IActionParent {

    default List<IAction> getActionQueue() {
        return null;
    }

    default void insert(IAction action) {
    }

    default IActionDelegate getDelegate() {
        return null;
    }

    default int size() {
        return 0;
    }
}