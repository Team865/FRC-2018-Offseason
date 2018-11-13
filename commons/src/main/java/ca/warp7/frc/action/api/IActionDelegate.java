package ca.warp7.frc.action.api;

@SuppressWarnings("unused")
public interface IActionDelegate {
    double getElapsed();

    int getDetachDepth();

    IActionParent getParent();

    boolean isConsumed(IActionConsumer consumer);

    void interrupt();

    IActionResources getResources();

    default boolean hasParent() {
        return getParent() != null;
    }
}