package ca.warp7.frc.action.api;


import java.util.List;

public interface IActionParent {

    default List<IAction> getActionQueue() {
        return null;
    }

    default IActionDelegate getDelegate() {
        return null;
    }

    default int size() {
        return 0;
    }
}