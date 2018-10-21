package ca.warp7.frc.commons.core;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * Base class for managing all the robot's stuff. Extend this class
 * to create a runnable robot. See documentation in the class of
 * each field in this class.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class Robot extends IterativeRobot {

    private final Components mComponents = new Components();
    private final SubsystemsManager mSubsystems = new SubsystemsManager();
    private final LoopsManager mLoops = new LoopsManager();
    private final StateManager mState = new StateManager();
    private final AutoRunner mAutoRunner = new AutoRunner();

    protected final double kAutoMaxTimeout = AutoRunner.kMaxAutoTimeoutSeconds;
    protected final double kAutoWaitForDriverStation = Double.POSITIVE_INFINITY;
    private static StateManager sState;

    @Override
    public final void startCompetition() {
        sState = mState;
        this.setComponents(Components.reflectFromPackageName(getClass().getPackage().getName()));
        this.onCreate();
        if (mComponents.hasClass() && mLoops.hasOIRunner()) {
            super.startCompetition();
        } else {
            System.out.println("ERROR Robot code does not have components or teleop code");
        }
    }

    @Override
    public final void robotInit() {
        mState.logRobotState("Initializing");
        mComponents.createAll();
        mSubsystems.setSubsystems(mComponents.getSubsystems());
        mLoops.setPeriodicSource(mSubsystems, mState);
        mComponents.constructExtras();
        mSubsystems.constructAll();
        mLoops.startObservers();
        mSubsystems.zeroAllSensors();
    }

    @Override
    public final void disabledInit() {
        mState.logRobotState("Disabled");
        mAutoRunner.stop();
        mLoops.disable();
        mSubsystems.disableAll();
        mSubsystems.updateAll();
    }

    @Override
    public final void autonomousInit() {
        mState.logRobotState("Autonomous");
        mSubsystems.onAutonomousInit();
        mLoops.disableController();
        mLoops.enable();
        mAutoRunner.start();
    }

    @Override
    public final void teleopInit() {
        mState.logRobotState("Teleop");
        mAutoRunner.stop();
        mSubsystems.onTeleopInit();
        mLoops.enableController();
        mLoops.enable();
    }

    @Override
    public final void testInit() {
        mState.logRobotState("Test");
    }

    protected abstract void onCreate();

    protected final void setTeleop(Runnable runner) {
        mLoops.setOIRunner(runner);
    }

    protected final void setAutoMode(IAutoMode mode, double timeout) {
        mAutoRunner.setAutoMode(mode, timeout);
    }

    protected final void setComponents(Class<?> components) {
        mComponents.setClass(components);
    }

    public static void println(Object o) {
        sState.report(null, StateType.PRINTLN, o);
    }

    public static void warning(Object o) {
        sState.report(null, StateType.WARNING_PRINTLN, o);
    }

    public static void error(Object o) {
        sState.report(null, StateType.ERROR_PRINTLN, o);
    }

    public static void report(Object owner, StateType type, Object state) {
        sState.report(owner, type, state);
    }
}