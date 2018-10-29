package ca.warp7.frc2018_3;

import ca.warp7.frc.commons.core.Robot;
import ca.warp7.frc2018_3.auto.Baseline;
import ca.warp7.frc2018_3.auto.BaselinePID;
import ca.warp7.frc2018_3.auto.NothingMode;
import ca.warp7.frc2018_3.auto.OneSwitch;
import ca.warp7.frc2018_3.subsystems.*;

@SuppressWarnings("WeakerAccess")
public final class Components {

    public static final Drive drive = new Drive();
    public static final Pneumatics pneumatics = new Pneumatics();
    public static final Intake intake = new Intake();
    public static final ArmLift armLift = new ArmLift();
    public static final Climber climber = new Climber();
    public static final Limelight limelight = new Limelight();
    public static final NavX navX = new NavX();

    static {

        Robot.registerComponents(
                drive, pneumatics, intake, armLift, climber, limelight, navX
        );

        Robot.registerSelectableAutoModes(
                new NothingMode(),
                new Baseline(),
                new BaselinePID(),
                new OneSwitch('L'), new OneSwitch('R')
        );
    }
}
