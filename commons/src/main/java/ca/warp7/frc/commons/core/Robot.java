package ca.warp7.frc.commons.core;

import ca.warp7.frc.commons.wpi_wrapper.IterativeRobotWrapper;

/**
 * Base class for managing all the robot's stuff. Extend this class
 * to create a robot
 */
public abstract class Robot extends IterativeRobotWrapper {

    private final Components mComponents = new Components();
    private final SubsystemsManager mSubsystemsManager = new SubsystemsManager();
    private final LoopsManager mLoopsManager = new LoopsManager();
    private final StateManager mStateManager = new StateManager();
    private final AutoRunner mAutoRunner = new AutoRunner();

    @Override
    public final void startCompetition() {
        sState = mStateManager;
        this.setComponents(Components.tryReflectComponentsFromPackage(getPackageName()));
        this.onCreate();
        if (mComponents.hasClass() && mLoopsManager.hasOIRunner()) {
            super.startCompetition();
        } else {
            printError("Robot is not set up");
        }
    }

    @Override
    public final void robotInit() {
        super.robotInit();
        mComponents.createAll();
        mSubsystemsManager.setSubsystems(mComponents.getSubsystems());
        mLoopsManager.setPeriodicSource(mSubsystemsManager, mStateManager);
        mComponents.constructExtras();
        mSubsystemsManager.constructAll();
        mLoopsManager.startObservers();
        mSubsystemsManager.zeroAllSensors();
    }

    @Override
    public final void disabledInit() {
        super.disabledInit();
        mAutoRunner.onStop();
        mLoopsManager.disable();
        mSubsystemsManager.disableAll();
        mSubsystemsManager.updateAll();
    }

    @Override
    public final void autonomousInit() {
        super.autonomousInit();
        mSubsystemsManager.onAutonomousInit();
        mLoopsManager.disableController();
        mLoopsManager.enable();
        mAutoRunner.onStart();
    }

    @Override
    public final void teleopInit() {
        super.teleopInit();
        mAutoRunner.onStop();
        mSubsystemsManager.onTeleopInit();
        mLoopsManager.enableController();
        mLoopsManager.enable();
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void testInit() {
        super.testInit();
    }

    @SuppressWarnings("unused")
    protected final double kMaxAutoTimeout = AutoRunner.kMaxAutoTimeoutSeconds;
    protected final double kAutoWaitForDriverStation = Double.POSITIVE_INFINITY;

    protected abstract void onCreate();

    protected final void setOIRunner(Runnable OIRunner) {
        mLoopsManager.setOIRunner(OIRunner);
    }

    @SuppressWarnings("SameParameterValue")
    protected final void setAutoMode(IAutoMode mode, double timeoutSec) {
        mAutoRunner.setAutoMode(mode, timeoutSec);
    }

    @SuppressWarnings("WeakerAccess")
    protected final void setComponents(Class<?> componentsClass) {
        mComponents.setClass(componentsClass);
    }

    private static StateManager sState;

    @SuppressWarnings("WeakerAccess")
    public static void printLine(Object o) {
        sState.report(null, StateType.PRINT_LINE, o);
    }

    @SuppressWarnings("WeakerAccess")
    public static void printError(Object o) {
        sState.report(null, StateType.ERROR_PRINT_LINE, o);
    }

    public static void reportState(Object owner, StateType type, Object state) {
        sState.report(owner, type, state);
    }
}