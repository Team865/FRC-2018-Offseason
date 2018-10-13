package ca.warp7.frc2018_3;

import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc2018_3.auto.modes.NothingMode;
import ca.warp7.frc2018_3.constants.DefaultMap;
import ca.warp7.frc2018_3.constants.RobotMap;
import ca.warp7.frc2018_3.operator_input.DualRemote;
import ca.warp7.frc2018_3.operator_input.OperatorInput;

public class FiftyCents extends Robot {
    @Override
    protected void onCreate() {
        System.out.println("Hello me is robit!");

        setMapping(RobotMap.class);
        DefaultMap.configure();

        setAutoMode(new NothingMode());

        OperatorInput.setController(new DualRemote(0, 1));
        //OperatorInput.setController(new SingleRemote(0));
        setOIRunner(OperatorInput::onUpdate);
    }
}
