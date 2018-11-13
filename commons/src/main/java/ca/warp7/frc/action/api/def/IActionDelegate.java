package ca.warp7.frc.action.api.def;

public interface IActionDelegate {
    double getElapsed();

    double getTotalElapsed();

    int getDetachDepth();

    IActionParent getParent();

    boolean isConsumed(IActionConsumer consumer);

    void interrupt();

    IActionResources getResources();

    default boolean hasParent() {
        return getParent() != null;
    }
}