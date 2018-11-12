package ca.warp7.frc.core;

import java.util.List;
import java.util.function.Supplier;

@FunctionalInterface
public interface IActionSupplier extends Supplier<IAction> {

    /**
     * Returns whether the auto mode have different user-selectable configurations
     */
    default boolean isConfigurable() {
        return false;
    }

    /**
     * Get a post fix identifier so the user can properly select them
     */
    default String getConfiguration() {
        return isConfigurable() ? String.valueOf((int) (Math.random() * 1000)) : "";
    }

    default List<IAction> getActionQueue() {
        return null;
    }
}
