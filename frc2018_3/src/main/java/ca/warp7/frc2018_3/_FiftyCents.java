package ca.warp7.frc2018_3;

import ca.warp7.frc.commons.core.Robot;

import static ca.warp7.frc2018_3.AutoModes.*;
import static ca.warp7.frc2018_3.Components.*;

public class _FiftyCents extends Robot {
    static {
        registerComponents(drive, pneumatics, intake, armLift, climber, limelight, navX);
        registerAutoModes(nothing, baseline, baselinePID, oneSwitchLeft, oneSwitchRight);
        setControls(DualRemote::new);
    }

    @Override
    protected void onCreate() {
        System.out.println("Hello me is robit!");
        setAutoMode(oneSwitchRight, 15);
        setTeleop(new SingleRemote());
    }
}
