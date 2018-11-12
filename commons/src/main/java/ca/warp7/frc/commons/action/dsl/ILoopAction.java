package ca.warp7.frc.commons.action.dsl;

import ca.warp7.frc.commons.core.IAction;

@FunctionalInterface
public interface ILoopAction extends IAction {
    @Override
    default void onStart() {
    }

    @Override
    void onUpdate();
}
