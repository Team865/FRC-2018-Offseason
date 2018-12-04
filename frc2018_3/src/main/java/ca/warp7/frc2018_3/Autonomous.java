package ca.warp7.frc2018_3;

import ca.warp7.action.IAction;
import ca.warp7.frc2018_3.auto.Baseline;
import ca.warp7.frc2018_3.auto.BaselinePID;
import ca.warp7.frc2018_3.auto.NothingMode;
import ca.warp7.frc2018_3.auto.OneSwitch2;

@SuppressWarnings("unused")
class Autonomous {
    static final IAction.Mode nothing = new NothingMode();
    static final IAction.Mode baseline = new Baseline();
    static final IAction.Mode baselinePID = new BaselinePID();
    static final IAction.Mode oneSwitchLeft = new OneSwitch2('L');
    static final IAction.Mode oneSwitchRight = new OneSwitch2('R');
}
