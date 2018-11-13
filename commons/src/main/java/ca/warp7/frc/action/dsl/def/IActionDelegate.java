package ca.warp7.frc.action.dsl.def;

public interface IActionDelegate {

    double getElapsed();

    boolean isDetached();

    double getTotalElapsed();

    IActionParent asParent();

    boolean hasParent();

    IActionParent getParent();

    IActionParent getRoot();

    void setVar(String name, Object value);

    Object getVar(String name, Object defaultVal);

    double getDouble(String name, double defaultVal);

    boolean isConsumed(IActionConsumer consumer);

    void interrupt();

    int countTrigger(String name);

    int countTriggerSources(String name);
}
