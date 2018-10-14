package ca.warp7.frc2018_3.operator_input;

import ca.warp7.frc.commons.state.ButtonState;
import ca.warp7.frc.commons.wpi_wrapper.XboxController;

import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

public class DualRemote extends SingleRemote {

    private final XboxController mOperator;

    public DualRemote() {
        super();
        mOperator = new XboxController(1);
    }

    @Override
    public double getClimberSpeed() {
        if (mOperator.getBButton() == ButtonState.HELD_DOWN) {
            return -mOperator.getY(kRight);
        }
        return super.getClimberSpeed(); // Hands control to driver after operator
    }
}
