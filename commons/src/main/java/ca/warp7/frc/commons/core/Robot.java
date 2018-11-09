package ca.warp7.frc.commons.core;

import edu.wpi.first.wpilibj.IterativeRobot;

import static ca.warp7.frc.commons.core.StateType.*;
import static ca.warp7.frc.commons.core.StateType.Error;

/**
 * Base class for managing all the robot's stuff. Extend this class
 * to create a runnable robot. See documentation in the class of
 * each field in this class.
 */
//@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class Robot extends IterativeRobot {

    private final Components mComponents = new Components();
    private final AutoRunner mAutoRunner = new AutoRunner();
    private final LoopsManager mLoopsManager = new LoopsManager();
    private static final StateManager state = new StateManager();
    private static final RobotLoader loader = new RobotLoader();

    @Override
    public final void startCompetition() {
        state.attachRobotInstance(this);
        mLoopsManager.setComponentsSource(mComponents);
        mComponents.reflectFromPackage(getClass().getPackage().getName());
        this.onCreate();
        if (mComponents.isReadyToStart()) super.startCompetition();
        else System.out.println("ERROR Robot code does not have components or teleop code");
    }

    @Override
    public final void robotInit() {
        state.logInit();
        mComponents.onConstruct();
        mLoopsManager.start();
    }

    @Override
    public final void disabledInit() {
        state.logDisabled();
        mAutoRunner.stop();
        mLoopsManager.disable();
        mComponents.onDisabled();
    }

    @Override
    public final void autonomousInit() {
        state.logAutonomous();
        mComponents.onAutonomousInit();
        mLoopsManager.enable();
        mAutoRunner.start();
    }

    @Override
    public final void teleopInit() {
        state.logTeleop();
        mAutoRunner.stop();
        mComponents.onTeleopInit();
        mLoopsManager.enable();
    }

    @Override
    public final void testInit() {
        state.logTest();
    }

    protected abstract void onCreate();

    protected final void setTeleop(IControls loop) {
        mComponents.setControllerLoop(loop);
    }

    protected final void setAutoMode(IAutoMode mode, double testTimeout) {
        mAutoRunner.setAutoMode(mode, testTimeout);
    }

    public static void println(Object o) {
        state.report(null, Println, o);
    }

    public static void warning(Object o) {
        state.report(null, Warning, o);
    }

    public static void error(Object o) {
        state.report(null, Error, o);
    }

    public static void report(Object owner, StateType type, Object o) {
        state.report(owner, type, o);
    }

    public static void reportInputAndState(Object owner, Object _input, Object _state) {
        state.report(owner, ComponentInput, _input);
        state.report(owner, ComponentState, _state);
    }

    protected static RobotLoader getLoader() {
        return loader;
    }

    static StateManager getState() {
        return state;
    }
}