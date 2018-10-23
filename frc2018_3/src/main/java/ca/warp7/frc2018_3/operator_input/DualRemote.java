package ca.warp7.frc2018_3.operator_input;

import ca.warp7.frc.commons.XboxController;

import static ca.warp7.frc.commons.ButtonState.HELD_DOWN;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;

@Deprecated
public class DualRemote extends SingleRemote {

    private final XboxController mOperator;

    public DualRemote() {
        super();
        mOperator = new XboxController(1);
    }

    @Override
    public double getClimberSpeed() {
        if (mOperator.getBButton() == HELD_DOWN) {
            return -mOperator.getY(kLeft);
        }
        return super.getClimberSpeed(); // Hands control to driver after operator
    }

    @Override
    public double getArmSpeed() {
        if (mOperator.getAButton() == HELD_DOWN) {
            return mOperator.getY(kLeft); // This overrides Cheesy Drive
        }

        return super.getArmSpeed();
    }

    @Override
    public double getArmDistance() {
        if (mOperator.getYButton() == HELD_DOWN) {
            return mOperator.getY(kLeft); // This overrides Cheesy Drive
        }

        return super.getArmSpeed();
    }

    @Override
    public boolean grapplingHookShouldDeploy() {
        return super.grapplingHookShouldDeploy() || mOperator.getStartButton() == HELD_DOWN;
    }

    @Override
    public double getActualClimberSpeed() {
        return mOperator.getStartButton() == HELD_DOWN ? mOperator.getY(kLeft) : 0;
    }
}
