package ca.warp7.frc.action.dsl.def;

public interface IActionDelegate {
    double getElapsed();

    boolean isDetached();

    double getTotalElapsed();

    IActionParent asParent();

    boolean hasParent();

    IActionParent getParent();

    IActionParent getRoot();

    boolean isConsumed(IActionConsumer consumer);

    void interrupt();

    IActionResources resources();
}