package ca.warp7.frc.action.api;

@SuppressWarnings("unused")
public interface IActionDelegate {
    double getElapsed();

    int getDetachDepth();

    IActionParent getParent();

    void interrupt();

    IActionResources getResources();

    default boolean hasRemainingTime() {
        return false;
    }

    default double getRemainingTime() {
        return 0;
    }

    default boolean hasProgressState() {
        return false;
    }

    default double getPercentProgress() {
        return 0;
    }

    default double getNumericalProgress() {
        return 0;
    }

    default boolean hasParent() {
        return getParent() != null;
    }
}