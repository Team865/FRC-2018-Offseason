package ca.warp7.frc2018_3;

import ca.warp7.action.IActionMode;
import ca.warp7.frc2018_3.auto.Baseline;
import ca.warp7.frc2018_3.auto.BaselinePID;
import ca.warp7.frc2018_3.auto.NothingMode;
import ca.warp7.frc2018_3.auto.OneSwitch;

@SuppressWarnings("unused")
class Autonomous {
    static final IActionMode nothing = new NothingMode();
    static final IActionMode baseline = new Baseline();
    static final IActionMode baselinePID = new BaselinePID();
    static final IActionMode oneSwitchLeft = new OneSwitch('L');
    static final IActionMode oneSwitchRight = new OneSwitch('R');
}
