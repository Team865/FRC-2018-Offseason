package ca.warp7.frc2018_3.operator_input;

import ca.warp7.frc.commons.state.ButtonState;
import ca.warp7.frc.commons.wpi_wrapper.XboxController;

import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;

public class DualRemote extends SingleRemote {

    private final XboxController mOperator;

    public DualRemote() {
        super();
        mOperator = new XboxController(1);
    }

    /*
    @Override
    public double getClimberSpeed() {
        if (mOperator.getBButton() == ButtonState.HELD_DOWN) {
            return -mOperator.getY(kLeft);
        }
        return super.getClimberSpeed(); // Hands control to driver after operator
    }
    */
    @Override
    public double getArmSpeed() {
        if (mOperator.getAButton() == ButtonState.HELD_DOWN) {
            return mOperator.getY(kLeft); // This overrides Cheesy Drive
        }

        return super.getArmSpeed();
    }
    /*
    @Override
    public double getArmDistance() {
        if (mOperator.getYButton() == ButtonState.HELD_DOWN) {
            return mOperator.getY(kLeft); // This overrides Cheesy Drive
        }

        return super.getArmSpeed();
    }
    */
}
