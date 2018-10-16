package ca.warp7.frc2018_3;

import ca.warp7.frc.commons.core.ISubsystem;
import ca.warp7.frc2018_3.sensors.Limelight;
import ca.warp7.frc2018_3.sensors.Navx;
import ca.warp7.frc2018_3.subsystems.*;

@ISubsystem.RobotComponentsPool
public final class Components {
    public static final Drive drive = new Drive();
    public static final Pneumatics pneumatics = new Pneumatics();
    public static final Intake intake = new Intake();
    public static final Climber climber = new Climber();
    public static final ArmClimber armClimber = new ArmClimber();
    public static final Limelight limelight = new Limelight();
    public static final Navx navx = new Navx();
}
