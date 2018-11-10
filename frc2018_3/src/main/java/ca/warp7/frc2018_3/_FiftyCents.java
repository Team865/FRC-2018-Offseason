package ca.warp7.frc2018_3;

import ca.warp7.frc.commons.core.Robot;

public class _FiftyCents extends Robot {

    public _FiftyCents() {
        System.out.println("Hello me is robit!");
        loader.setAutoMode(Autonomous.oneSwitchRight, 15);
        loader.setTeleop(new TeleopRemote(), 0, 1);
    }

    @Override
    protected void onCreate() {
        setAutoMode(Autonomous.oneSwitchRight, 15);
        setTeleop(new TeleopRemote());
    }
}
