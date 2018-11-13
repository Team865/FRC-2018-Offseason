package ca.warp7.frc.action.api.def;

public interface IActionDelegate {
    double elapsed();

    boolean isDetached();

    double getTotalElapsed();

    IActionParent asParent();

    boolean hasParent();

    IActionParent parent();

    IActionParent getRoot();

    boolean isConsumed(IActionConsumer consumer);

    void interrupt();

    IActionResources resources();
}