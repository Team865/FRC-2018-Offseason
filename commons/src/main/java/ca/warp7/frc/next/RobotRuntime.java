package ca.warp7.frc.next;

import ca.warp7.action.IAction;
import ca.warp7.action.impl.ActionMode;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private NetworkTable mSystemsNetworkTable;

    private Runnable mLoop = () -> {
        double time = Timer.getFPGATimestamp();
        double diff = time - mPreviousTime;
        mPreviousTime = time;
        synchronized (mRuntimeLock) {
            for (RobotController.Pair pair : mControllers)
                if (pair.isActive()) {
                    RobotController.collect(pair.getState(), pair.getController());
                }
            mInputSystems.forEach(inputSystem -> {
                inputSystem.onMeasure(diff);
                sendObjectDescription(inputSystem);
            });
            if (mEnabled) {
                mOutputSystems.forEach((outputSystem, action) -> {
                    sendObjectDescription(outputSystem);
                    if (action == null) outputSystem.onIdle();
                    else action.update();
                    outputSystem.onOutput();
                });
            }
        }
        originalOut.println(outContent.toString());
        outContent.reset();
        String[] errors = errContent.toString().split(System.lineSeparator());
        for (String error : errors) originalErr.println("ERROR " + error);
        errContent.reset();
    };

    private void sendObjectDescription(Object system) {
        String subTable = system.getClass().getSimpleName();
        for (Method method : system.getClass().getMethods()) {
            String name = method.getName();
            if (name.startsWith("get")) {
                String entry = subTable + "/" + name.substring(3);
                try {
                    sendNetworkTableValue(mSystemsNetworkTable.getEntry(entry), method.invoke(system));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void sendNetworkTableValue(NetworkTableEntry entry, Object value) {
        if (value instanceof Number) {
            double n = ((Number) value).doubleValue();
            if (Math.abs(n) < 0.001) n = 0;
            entry.setNumber(n);
        } else if (value instanceof Boolean) entry.setBoolean((Boolean) value);
        else if (value instanceof String) entry.setString((String) value);
        else if (value.getClass().isEnum()) entry.setString(value.toString());
        else entry.setString(value.getClass().getSimpleName() + " Object");
    }

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
        mSystemsNetworkTable = NetworkTableInstance.getDefault().getTable("Systems");
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

    public void initAutonomousMode(IAction.Mode mode, int intervalMs, double timeout) {
        System.out.println(String.format("Robot State: Autonomous [%s]", mode.getClass().getSimpleName()));
        IAction action = mode.getAction();
        mActionRunner = ActionMode.createRunner(Timer::getFPGATimestamp, intervalMs, timeout, action, true);
        synchronized (mRuntimeLock) {
            mEnabled = true;
            mInputSystems.forEach(InputSystem::onZeroSensors);
        }
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
