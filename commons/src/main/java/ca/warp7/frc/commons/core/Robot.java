package ca.warp7.frc.commons.core;

import ca.warp7.frc.commons.wpi_wrapper.IterativeRobotWrapper;

/**
 * Base class for managing all the robot's stuff
 */

public abstract class Robot extends IterativeRobotWrapper {

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

    /**
     * A procedure passed into the {@link LoopsManager} runner called in teleop
     */
    private Runnable mOIRunner;

    /**
     * User class representing all the components of the robot
     */
    private Class<?> mComponents;

    /**
     * Call the onCreate method defined by the subclass to try to get the mapping class and OI updater.
     * If successful, call the normal startCompetition to start everything
     */
    @Override
    public final void startCompetition() {
        setComponents(Components.tryGetComponentsFromPackage(getPackageName()));
        sAccessor = new InstanceAccessor();
        displayQualifier();
        onCreate();
        if (mComponents != null && mOIRunner != null) {
            super.startCompetition();
        } else {
            printError("Robot is not set up");
        }
    }

    /**
     * Performs all actual setup steps for the robot
     */
    @Override
    public final void robotInit() {
        super.robotInit();
        mSubsystemsManager.setSubsystems(Components.getSubsystems(mComponents));
        mLoopsManager.setSource(mSubsystemsManager, mStateManager::sendAll, mOIRunner);
        mSubsystemsManager.constructAll();
        mSubsystemsManager.zeroAllSensors();
        mSubsystemsManager.reportAll();
        mLoopsManager.startObservers();
    }

    /**
     * Disables the auto thread, managed loops and resets the subsystems when the robot should disable
     */
    @Override
    public final void disabledInit() {
        super.disabledInit();
        mAutoRunner.onStop();
        mLoopsManager.disable();
        mSubsystemsManager.disableAll();
        mSubsystemsManager.updateAll();
    }

    /**
     * Enables the managed loops and tries to start auto. Prints error if there isn't an auto mod available
     */
    @Override
    public final void autonomousInit() {
        super.autonomousInit();
        mSubsystemsManager.onAutonomousInit();
        mLoopsManager.disableController();
        mLoopsManager.enable();
        try {
            mAutoRunner.onStart();
        } catch (final AutoRunner.NoAutoException e) {
            printError("The auto mode will do nothing!");
        }
    }

    /**
     * Stops any autos and makes sure the loops are enabled
     */
    @Override
    public final void teleopInit() {
        super.teleopInit();
        mAutoRunner.onStop();
        mSubsystemsManager.onTeleopInit();
        mLoopsManager.enableController();
        mLoopsManager.enable();
    }

    /**
     * Nothing in the testing state
     */
    @SuppressWarnings("EmptyMethod")
    @Override
    public void testInit() {
        super.testInit();
    }

    /**
     * Method should call the following three methods to set the robot's subsystems, the OI
     * runner, and the auto mode
     */
    protected abstract void onCreate();

    /**
     * Sets the Operator Input runner, which should get input from controllers and
     * pass them to subsystems
     */
    protected final void setOIRunner(Runnable OIRunner) {
        mOIRunner = OIRunner;
    }

    /**
     * Sets the auto mode for the robot. See {@link IAutoMode}.
     */
    @SuppressWarnings("SameParameterValue")
    protected final void setAutoMode(IAutoMode mode) {
        mAutoRunner.setAutoMode(mode);
    }

    /**
     * Sets the components class
     */
    @SuppressWarnings("WeakerAccess")
    protected final void setComponents(Class<?> components) {
        mComponents = components;
    }

    /**
     * Prints an object to System.out
     */
    @SuppressWarnings("WeakerAccess")
    public static void printLine(Object object) {
        sAccessor.reportState(null, ReportType.PRINT_LINE, object);
    }

    /**
     * Prints an error to System.err
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static void printError(Object object) {
        sAccessor.reportState(null, ReportType.ERROR_PRINT_LINE, object);
    }

    /**
     * See {@link StateManager#reportState(Object, ReportType, Object)}
     */
    public static void reportState(Object owner, ReportType reportType, Object state) {
        sAccessor.reportState(owner, reportType, state);
    }

    /**
     * A static accessor that refers to the Robot instance
     */
    private static InstanceAccessor sAccessor;

    private class InstanceAccessor {
        private void reportState(Object owner, ReportType reportType, Object state) {
            mStateManager.reportState(owner, reportType, state);
        }
    }
}