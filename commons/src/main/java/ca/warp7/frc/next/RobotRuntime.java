package ca.warp7.frc.next;

import ca.warp7.action.IAction;
import ca.warp7.action.impl.ActionMode;
import ca.warp7.frc.core.XboxControlsState;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Runnable mLoop = () -> {
        double time = Timer.getFPGATimestamp();
        double diff = time - mPreviousTime;
        mPreviousTime = time;
        synchronized (mRuntimeLock) {
            mInputSystems.forEach(inputSystem -> inputSystem.onMeasure(diff));
            if (mEnabled) {
                mOutputSystems.forEach((outputSystem, action) -> {
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
        Thread.currentThread().setName("Robot");
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        mLoopNotifier = new Notifier(mLoop);
        mLoopNotifier.startPeriodic(loopsPerSecond);
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

    public XboxControlsState getXboxController(int port, boolean isActive) {
        return null;
    }

    public void disableOutputs() {
        mActionRunner.stop();
        synchronized (mRuntimeLock) {
            mEnabled = false;
        }
    }

    public void initAutonomousMode(IAction.Mode mode, double timeout) {
        IAction action = mode.getAction();
        mActionRunner = ActionMode.createRunner(Timer::getFPGATimestamp, 20, timeout, action, true);
        mActionRunner.start();
    }

    public void initControls(ControlLoop controlLoop) {
        mActionRunner.stop();
        controlLoop.setup();
    }
}
