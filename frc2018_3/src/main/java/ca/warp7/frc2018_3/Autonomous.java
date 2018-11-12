package ca.warp7.frc2018_3;

import ca.warp7.frc.core.IAutoMode;
import ca.warp7.frc2018_3.auto.Baseline;
import ca.warp7.frc2018_3.auto.BaselinePID;
import ca.warp7.frc2018_3.auto.NothingMode;
import ca.warp7.frc2018_3.auto.OneSwitch;

@SuppressWarnings("unused")
class Autonomous {
    static final IAutoMode nothing = new NothingMode();
    static final IAutoMode baseline = new Baseline();
    static final IAutoMode baselinePID = new BaselinePID();
    static final IAutoMode oneSwitchLeft = new OneSwitch('L');
    static final IAutoMode oneSwitchRight = new OneSwitch('R');
}
