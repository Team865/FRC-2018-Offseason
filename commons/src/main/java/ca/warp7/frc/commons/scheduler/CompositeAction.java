package ca.warp7.frc.commons.scheduler;

import ca.warp7.frc.commons.core.IAction;
import ca.warp7.frc.commons.core.IActionSupplier;

import java.util.function.BooleanSupplier;

public class CompositeAction implements IAction{

    public CompositeAction add(IActionSupplier... supplier){
        return this;
    }

    public CompositeAction async(IActionSupplier... entryPoints){
        return this;
    }

    public CompositeAction asyncAny(IActionSupplier... entryPoints){
        return this;
    }

    public CompositeAction waitFor(double seconds){
        return this;
    }

    public CompositeAction waitUntil(BooleanSupplier supplier){
        return this;
    }

    @Override
    public boolean shouldFinish() {
        return false;
    }

}
