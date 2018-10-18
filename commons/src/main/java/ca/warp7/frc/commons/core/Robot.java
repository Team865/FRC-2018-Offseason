package ca.warp7.frc.commons.core;

import ca.warp7.frc.commons.wpi_wrapper.IterativeRobotWrapper;

/**
 * Base class for managing all the robot's stuff
 */
public abstract class Robot extends IterativeRobotWrapper {
    /**
     * Contains utilities to initialize the components of the robot. See {@link Components}
     */
    private final Components mComponents = new Components();

    /**
     * Contains an array of subsystems. See {@link SubsystemsManager} for details
     */
    private final SubsystemsManager mSubsystemsManager = new SubsystemsManager();

    /**
     * Keeps track of the robot's looper and loops. See {@link LoopsManager} for details
     */
    private final LoopsManager mLoopsManager = new LoopsManager();

    /**
     * Keep track of state reporting and sending. See {@link StateManager} for details
     */
    private final StateManager mStateManager = new StateManager();

    /**
     * Starts and ends auto programs. See {@link AutoRunner} for details
     */
    private final AutoRunner mAutoRunner = new AutoRunner();

    @Override
    public final void startCompetition() {
        this.displayQualifier();
        this.setComponents(Components.tryReflectComponentsFromPackage(getPackageName()));
        sAccessor = new InstanceAccessor();
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
        try {
            mAutoRunner.onStart();
        } catch (final AutoRunner.NoAutoException e) {
            printError("There is not a specified main action!!!");
        }
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

    /**
     * Method should call the following three methods to setup the robot
     */
    protected abstract void onCreate();

    protected final double kMaxAutoTimeout = AutoRunner.kMaxAutoTimeoutSeconds;
    protected final double kAutoWaitForDriverStation = Double.POSITIVE_INFINITY;

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

    /**
     * Prints an object to System.out
     */
    @SuppressWarnings("WeakerAccess")
    public static void printLine(Object o) {
        sAccessor.report(null, ReportType.PRINT_LINE, o);
    }

    /**
     * Prints an error to System.err
     */
    @SuppressWarnings("WeakerAccess")
    public static void printError(Object o) {
        sAccessor.report(null, ReportType.ERROR_PRINT_LINE, o);
    }

    /**
     * See {@link StateManager#report(Object, ReportType, Object)}
     */
    public static void reportState(Object owner, ReportType reportType, Object state) {
        sAccessor.report(owner, reportType, state);
    }

    private static InstanceAccessor sAccessor;

    private class InstanceAccessor {
        private void report(Object owner, ReportType reportType, Object state) {
            mStateManager.report(owner, reportType, state);
        }
    }
}