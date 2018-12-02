package ca.warp7.frc2018_4;

import ca.warp7.action.IAction;
import ca.warp7.frc2018_4.auto.Baseline;
import ca.warp7.frc2018_4.auto.BaselinePID;
import ca.warp7.frc2018_4.auto.NothingMode;
import ca.warp7.frc2018_4.auto.OneSwitch;

@SuppressWarnings("unused")
class Autonomous {
    static final IAction.Mode nothing = new NothingMode();
    static final IAction.Mode baseline = new Baseline();
    static final IAction.Mode baselinePID = new BaselinePID();
    static final IAction.Mode oneSwitchLeft = new OneSwitch('L');
    static final IAction.Mode oneSwitchRight = new OneSwitch('R');
}
