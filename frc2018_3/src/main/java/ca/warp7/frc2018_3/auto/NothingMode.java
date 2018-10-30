package ca.warp7.frc2018_3.auto;

import ca.warp7.frc.commons.core.IAutoMode;
import ca.warp7.frc.commons.core.IAction;

public class NothingMode implements IAutoMode {
    @Override
    public IAction getMainAction() {
        return null;
    }
}
