package ca.warp7.frc.action.api;

@FunctionalInterface
public interface IActionLoop extends IAction {
    @Override
    default void onStart() {
    }

    @Override
    void onUpdate();
}
