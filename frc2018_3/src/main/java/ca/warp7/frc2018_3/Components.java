package ca.warp7.frc2018_3;

import ca.warp7.frc.commons.core.RobotComponents;
import ca.warp7.frc2018_3.sensors.Limelight;
import ca.warp7.frc2018_3.sensors.NavX;
import ca.warp7.frc2018_3.subsystems.*;

@RobotComponents
public final class Components {
    public static final Drive drive = new Drive();
    public static final Pneumatics pneumatics = new Pneumatics();
    public static final Intake intake = new Intake();
    public static final Climber armFromClimber = new Climber();
    public static final ActualClimber actualClimber = new ActualClimber();
    public static final Limelight limelight = new Limelight();
    public static final NavX navX = new NavX();
}
