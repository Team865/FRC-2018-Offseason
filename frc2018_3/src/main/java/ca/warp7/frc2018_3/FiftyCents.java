package ca.warp7.frc2018_3;

import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc2018_3.auto.modes.OneSwitch;
import ca.warp7.frc2018_3.operator_input.DualRemote;
import ca.warp7.frc2018_3.operator_input.OperatorInput;

public class FiftyCents extends Robot {
    @Override
    protected void onCreate() {
        System.out.println("Hello me is robit!");
        //setAutoMode(new BaselinePID(), 10);
        //setAutoMode(() -> new OuttakeCube(0.5, -0.75), 1);
        setAutoMode(new OneSwitch('R'), 15);
        setOIRunner(new OperatorInput(new DualRemote())::onUpdate);
    }
}
