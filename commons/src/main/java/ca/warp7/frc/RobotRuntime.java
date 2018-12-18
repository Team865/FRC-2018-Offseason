package ca.warp7.frc;

import ca.warp7.action.IAction;
import ca.warp7.action.impl.ActionMode;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;

import java.util.ArrayList;
import java.util.List;

import static ca.warp7.frc.RobotUtils.*;

public class RobotRuntime {

    private Notifier mLoopNotifier;
    private boolean mEnabled;
    private double mPreviousTime;
    private final Object mRuntimeLock = new Object();
    private IAction mActionRunner;
    private final List<ControlInstance> mControls = new ArrayList<>();
    private List<Subsystem> mSubsystems = new ArrayList<>();
    private List<Input> mInputs = new ArrayList<>();
    private NetworkTable mSystemsTable;
    private NetworkTable mControllersTable;
    private int mLoopsPerSecond;
    private ControlLoop mControlLoop;

    private Runnable mLoop = () -> {
        double time = Timer.getFPGATimestamp();
        double diff = time - mPreviousTime;
        mPreviousTime = time;
        synchronized (mRuntimeLock) {
            for (ControlInstance instance : mControls) {
                if (instance.isActive()) {
                    collect(instance.getState(), instance.getController());
                    String controllerName = "Controller " + instance.getPort();
                    sendObjectDescription(mControllersTable, instance.getState(), controllerName);
                }
            }
            for (Input input : mInputs) {
                input.onMeasure(diff);
                sendObjectDescription(mSystemsTable, input);
            }
            if (mEnabled) {
                if (mControlLoop != null) mControlLoop.periodic();
                for (Subsystem subsystem : mSubsystems) {
                    sendObjectDescription(mSystemsTable, subsystem);
                    subsystem.updateState();
                    subsystem.onOutput();
                }
            }
        }
        updateSystemStream();
    };

    public void registerInput(Input input) {
        synchronized (mRuntimeLock) {
            mInputs.add(input);
        }
    }

    public void start(int loopsPerSecond) {
        if (mLoopNotifier == null) {
            synchronized (mRuntimeLock) {
                Thread.currentThread().setName("Robot");
                initSystemStream();
                NetworkTableInstance instance = NetworkTableInstance.getDefault();
                mSystemsTable = instance.getTable("Systems");
                mControllersTable = instance.getTable("Controllers");
                mEnabled = false;
                mLoopsPerSecond = loopsPerSecond;
                mLoopNotifier = new Notifier(mLoop);
                mLoopNotifier.startPeriodic(1.0 / mLoopsPerSecond);
            }
        }
    }

    public RobotController getController(int port, boolean isActive) {
        int port0 = isActive ? port : -1;
        synchronized (mRuntimeLock) {
            for (ControlInstance instance : mControls)
                if (instance.getPort() == port0) return instance.getState();
            ControlInstance newInstance = new ControlInstance(port0);
            mControls.add(newInstance);
            return newInstance.getState();
        }
    }

    public void disableOutputs() {
        System.out.println("Robot State: Disabled");
        if (mActionRunner != null) mActionRunner.stop();
        synchronized (mRuntimeLock) {
            mEnabled = false;
            mControlLoop = null;
            mSubsystems.forEach(subsystem -> {
                subsystem.onDisabled();
                sendObjectDescription(mSystemsTable, subsystem);
            });
        }
    }

    public void initAuto(IAction.Mode mode, double timeout) {
        System.out.println(String.format("Robot State: Autonomous [%s]", mode.getClass().getSimpleName()));
        IAction action = mode.getAction();
        if (mActionRunner != null) mActionRunner.stop();
        mActionRunner = ActionMode.createRunner(Timer::getFPGATimestamp,
                1.0 / mLoopsPerSecond, timeout, action, true);
        synchronized (mRuntimeLock) {
            mEnabled = true;
            mControlLoop = null;
            mInputs.forEach(Input::onZeroSensors);
        }
        mActionRunner.start();
    }

    public void initControls(ControlLoop controlLoop) {
        System.out.println(String.format("Robot State: Teleop [%s]", controlLoop.getClass().getSimpleName()));
        if (mActionRunner != null) mActionRunner.stop();
        synchronized (mRuntimeLock) {
            mEnabled = true;
            mInputs.forEach(Input::onZeroSensors);
            mControlLoop = controlLoop;
            mControlLoop.setup();
        }
    }

    void registerSubsystem(Subsystem subsystem) {
        synchronized (mRuntimeLock) {
            mSubsystems.add(subsystem);
        }
    }

    public static final RobotRuntime ROBOT_RUNTIME;

    static {
        ROBOT_RUNTIME = new RobotRuntime();
    }

    private RobotRuntime() {
    }
}
