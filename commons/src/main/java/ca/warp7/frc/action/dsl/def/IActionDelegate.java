package ca.warp7.frc.action.dsl.def;

import ca.warp7.frc.core.IAction;

import java.util.List;

public interface IActionDelegate {

    default List<IAction> getActionQueue() {
        return null;
    }
}
