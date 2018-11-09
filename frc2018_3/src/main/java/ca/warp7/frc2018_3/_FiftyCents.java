package ca.warp7.frc2018_3;

import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc.commons.core.RobotLoader;

import static ca.warp7.frc2018_3.Autonomous.*;
import static ca.warp7.frc2018_3.Components.*;

public class _FiftyCents extends Robot {

    public _FiftyCents() {
        RobotLoader loader = getLoader();
        loader.registerComponents(drive, pneumatics, intake, armLift, climber, limelight, navX);
        loader.registerAutoModes(nothing, baseline, baselinePID, oneSwitchLeft, oneSwitchRight);
        loader.setControls(DualRemote::new);
    }

    @Override
    protected void onCreate() {
        System.out.println("Hello me is robit!");
        setAutoMode(oneSwitchRight, 15);
        setTeleop(new SingleRemote());
    }
}
