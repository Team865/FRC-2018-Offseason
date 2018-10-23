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
    private final AutoRunner mAutoRunner = new AutoRunner();
    private final LoopsManager mLoops = new LoopsManager();
    private final StateManager mState = new StateManager();

    private static StateManager sState;
    protected final double kAutoMaxTimeout = AutoRunner.kMaxAutoTimeoutSeconds;
    protected final double kAutoWaitForDriverStation = Double.POSITIVE_INFINITY;

    @Override
    public final void startCompetition() {
        sState = mState;
        this.setComponents(Components.reflectFromPackageName(getClass().getPackage().getName()));
        this.onCreate();
        if (mComponents.hasClass() && mComponents.hasControlLoop()) {
            super.startCompetition();
        } else {
            System.out.println("ERROR Robot code does not have components or teleop code");
        }
    }

    @Override
    public final void robotInit() {
        mState.logRobotState("Initializing");
        mComponents.allocateObjects();
        mLoops.setPeriodicSource(mComponents, mState);
        mComponents.onConstruct();
        mLoops.startObservers();
    }

    @Override
    public final void disabledInit() {
        mState.logRobotState("Disabled");
        mAutoRunner.stop();
        mLoops.disable();
        mComponents.onDisabled();
    }

    @Override
    public final void autonomousInit() {
        mState.logRobotState("Autonomous");
        mComponents.onAutonomousInit();
        mLoops.enable();
        mAutoRunner.start();
    }

    @Override
    public final void teleopInit() {
        mState.logRobotState("Teleop");
        mAutoRunner.stop();
        mComponents.onTeleopInit();
        mLoops.enable();
    }

    @Override
    public final void testInit() {
        mState.logRobotState("Test");
    }

    protected abstract void onCreate();

    protected final void setControllerLoop(IControllerLoop loop) {
        mComponents.setControllerLoop(loop);
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