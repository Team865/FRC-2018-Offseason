package ca.warp7.frc.core;

import ca.warp7.action.IAction;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * Base class for managing all the robot's stuff. Extend this class
 * to create a runnable robot. See documentation in the class of
 * each field in this class.
 */
//@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class Robot extends IterativeRobot {

    private final Components mComponents = new Components();
    private final LoopsManager mLoopsManager = new LoopsManager();
    private static final StateManager state = new StateManager();
    protected static final RobotLoader loader = new RobotLoader();

    @Override
    public final void startCompetition() {
        state.attach();
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
        loader.stopAutonomous();
        mLoopsManager.disable();
        mComponents.onDisabled();
    }

    @Override
    public final void autonomousInit() {
        state.logAutonomous();
        mComponents.onAutonomousInit();
        mLoopsManager.enable();
        loader.startAutonomous();
    }

    @Override
    public final void teleopInit() {
        state.logTeleop();
        loader.stopAutonomous();
        mComponents.onTeleopInit();
        mLoopsManager.enable();
    }

    @Override
    public final void testInit() {
        state.logTest();
    }

    protected void onCreate() {
    }

    protected final void setTeleop(IControls loop) {
        mComponents.setControllerLoop(loop);
    }

    protected final void setAutoMode(IAction.Mode mode) {
        loader.setAutoMode(mode, 15);
    }

    public static void println(Object o) {
        state.report(null, StateType.Println, o);
    }

    public static void warning(Object o) {
        state.report(null, StateType.Warning, o);
    }

    public static void error(Object o) {
        state.report(null, StateType.Error, o);
    }

    public static void report(Object owner, StateType type, Object o) {
        state.report(owner, type, o);
    }

    public static void reportInputAndState(Object owner, Object _input, Object _state) {
        state.report(owner, StateType.ComponentInput, _input);
        state.report(owner, StateType.ComponentState, _state);
    }

    static StateManager getState() {
        return state;
    }
}