package ca.warp7.frc.next;

import ca.warp7.action.IAction;
import ca.warp7.action.impl.ActionMode;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class RobotRuntime {

    public static final RobotRuntime RUNTIME;

    static {
        RUNTIME = new RobotRuntime();
    }

    private RobotRuntime() {
    }

    private Notifier mLoopNotifier;
    private boolean mEnabled;
    private List<InputSystem> mInputSystems;
    private Map<OutputSystem, IAction> mOutputSystems;
    private double mPreviousTime;
    private final Object mRuntimeLock = new Object();
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private IAction mActionRunner;
    private final List<RobotController.Pair> mControllers = new ArrayList<>();
    private NetworkTable mSubsystemsTable;

    private Runnable mLoop = () -> {
        double time = Timer.getFPGATimestamp();
        double diff = time - mPreviousTime;
        mPreviousTime = time;
        synchronized (mRuntimeLock) {
            for (RobotController.Pair pair : mControllers)
                if (pair.isActive()) {
                    RobotController.collect(pair.getState(), pair.getController());
                }
            mInputSystems.forEach(inputSystem -> inputSystem.onMeasure(diff));
            if (mEnabled) {
                mOutputSystems.forEach((outputSystem, action) -> {
//                    for (Method m : outputSystem.getClass().getMethods())
//                        if (m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
//                            try {
//                                final Object r = m.invoke(outputSystem);
//                            } catch (IllegalAccessException | InvocationTargetException e) {
//                                e.printStackTrace();
//                            }
//                        }
                    mSubsystemsTable.getEntry("hi").setBoolean(true);  //FIXME
                    if (action == null) outputSystem.onIdle();
                    else action.update();
                    outputSystem.onOutput();
                });
            }
        }
        originalOut.println(outContent.toString());
        originalErr.println(errContent.toString());
    };

    public void setInputSystems(InputSystem... inputSystems) {
        if (mLoopNotifier == null) mInputSystems = Arrays.asList(inputSystems);
    }

    public void setOutputSystems(OutputSystem... outputSystems) {
        if (mLoopNotifier == null) {
            mOutputSystems = new HashMap<>();
            for (OutputSystem system : outputSystems) mOutputSystems.put(system, null);
        }
    }

    public void start(int loopsPerSecond) {
        assert mLoopNotifier == null;
        Thread.currentThread().setName("Robot");
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        mSubsystemsTable = NetworkTableInstance.getDefault().getTable("Subsystems");
        mEnabled = true;
        mLoopNotifier = new Notifier(mLoop);
        mLoopNotifier.startPeriodic(1.0 / loopsPerSecond);
    }

    public void setState(OutputSystem system, IAction next) {
        synchronized (mRuntimeLock) {
            IAction current = mOutputSystems.get(system);
            if (current == next) return;
            if (current != null) current.stop();
            if (next != null) next.start();
            mOutputSystems.put(system, next);
        }
    }

    public RobotController getController(int port, boolean isActive) {
        int port0 = isActive ? port : -1;
        for (RobotController.Pair pair : mControllers) if (pair.getPort() == port0) return pair.getState();
        RobotController.Pair newPair = new RobotController.Pair(port0);
        mControllers.add(newPair);
        RobotController.reset(newPair.getState());
        return newPair.getState();
    }

    public void disableOutputs() {
        System.out.println("Robot State: Disabled");
        mActionRunner.stop();
        synchronized (mRuntimeLock) {
            mEnabled = false;
            mOutputSystems.keySet().forEach(OutputSystem::onDisabled);
        }
    }

    public void initAutonomousMode(IAction.Mode mode, double timeout) {
        System.out.println(String.format("Robot State: Autonomous [%s]", mode.getClass().getSimpleName()));
        synchronized (mRuntimeLock) {
            mEnabled = true;
            mInputSystems.forEach(InputSystem::onZeroSensors);
        }
        IAction action = mode.getAction();
        mActionRunner = ActionMode.createRunner(Timer::getFPGATimestamp, 20, timeout, action, true);
        mActionRunner.start();
    }

    public void initControls(ControlLoop controlLoop) {
        System.out.println(String.format("Robot State: Teleop [%s]", controlLoop.getClass().getSimpleName()));
        synchronized (mRuntimeLock) {
            mEnabled = true;
            mInputSystems.forEach(InputSystem::onZeroSensors);
        }
        mActionRunner.stop();
        controlLoop.setup();
    }
}
