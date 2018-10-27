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

    private static StateManager state;
    protected final double kAutoMaxTimeout = AutoRunner.kMaxAutoTimeoutSeconds;
    protected final double kAutoWaitForDriverStation = Double.POSITIVE_INFINITY;

    @Override
    public final void startCompetition() {
        state = mState;
        mLoops.setPeriodicSource(mComponents, mState);
        mComponents.reflectFromPackage(getClass().getPackage().getName());
        this.onCreate();
        if (mComponents.readyForStart()) super.startCompetition();
        else System.out.println("ERROR Robot code does not have components or teleop code");
    }

    @Override
    public final void robotInit() {
        mState.logInit();
        mComponents.onConstruct();
        mLoops.start();
    }

    @Override
    public final void disabledInit() {
        mState.logDisabled();
        mAutoRunner.stop();
        mLoops.disable();
        mComponents.onDisabled();
    }

    @Override
    public final void autonomousInit() {
        mState.logAutonomous();
        mComponents.onAutonomousInit();
        mLoops.enable();
        mAutoRunner.start();
    }

    @Override
    public final void teleopInit() {
        mState.logTeleop();
        mAutoRunner.stop();
        mComponents.onTeleopInit();
        mLoops.enable();
    }

    @Override
    public final void testInit() {
        mState.logTest();
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
        state.report(null, StateType.PRINTLN, o);
    }

    public static void warning(Object o) {
        state.report(null, StateType.WARNING_PRINTLN, o);
    }

    public static void error(Object o) {
        state.report(null, StateType.ERROR_PRINTLN, o);
    }

    public static void report(Object owner, StateType type, Object o) {
        state.report(owner, type, o);
    }
}