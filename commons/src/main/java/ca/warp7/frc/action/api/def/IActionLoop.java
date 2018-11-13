package ca.warp7.frc.action.api.def;

import ca.warp7.frc.core.IAction;

@FunctionalInterface
public interface IActionLoop extends IAction {
    @Override
    default void onStart() {
    }

    @Override
    void onUpdate();
}
