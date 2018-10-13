package ca.warp7.frc2018_3.operator_input;

import ca.warp7.frc.commons.wpi_wrapper.XboxController;

public class DualRemote extends SingleRemote {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final XboxController mOperator;

    public DualRemote(int driverPort, int operatorPort) {
        super(driverPort);
        mOperator = new XboxController(operatorPort);
    }
}
