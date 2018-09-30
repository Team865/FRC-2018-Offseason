package ca.warp7.frc;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.warp7.frc.ControllerState.*;

public abstract class Robot<C> extends IterativeRobot {

	public static Robot.Utils utils;

	public enum StateType {
		INPUT, CURRENT
	}

	public interface ISubsystem {
		void onConstruct();

		void onDisabledReset();

		void onInputLoop();

		void onOutputLoop();

		void onUpdateState();

		void onReportState();
	}

	public interface ICallback<C> {
		void onConstruct(Robot<C> robot);

		void onTeleopInit();

		void onControllerInput(C controller);
	}

	public interface ILoop {
		void onStart();

		void onLoop();

		void onStop();
	}

	@SuppressWarnings("unused")
	public class Utils {
		public void print(Object object) {
			mObservationAccumulator.print(object);
		}

		public void println(Object object) {
			mObservationAccumulator.print(object);
			mObservationAccumulator.print("\n");
		}

		public void reportState(Object owner, StateType stateType, Object state) {
			mObservationAccumulator.reportState(owner, stateType, state);
		}

		public LoopsManager getLoopsManager() {
			return mLoopsManager;
		}
	}

	public abstract static class Main<C> extends _Mask<C> {
		public Main() {
			super();
			setCallback(this);
		}
	}

	public static class Pins {
		private final int[] mPinsArray;

		Pins(int[] pins) {
			mPinsArray = pins;
		}

		int get(int index) {
			return mPinsArray[index];
		}

		public int first() {
			return mPinsArray[0];
		}

		int[] array() {
			return mPinsArray;
		}
	}

	@SuppressWarnings("unused")
	public static class XboxController {
		private edu.wpi.first.wpilibj.XboxController mInnerController;
		private boolean mAButton;
		private boolean mBButton;
		private boolean mXButton;
		private boolean mYButton;
		private boolean mLeftBumper;
		private boolean mRightBumper;
		private boolean mLeftTrigger;
		private boolean mRightTrigger;
		private boolean mLeftStickButton;
		private boolean mRightStickButton;
		private boolean mStartButton;
		private boolean mBackButton;
		private int mDirectionalPad = -1;

		public XboxController(int portNumber) {
			mInnerController = new edu.wpi.first.wpilibj.XboxController(portNumber);
		}

		private ControllerState compareBooleanState(boolean previousState, boolean newState) {
			return newState != previousState ? newState ? PRESSED : RELEASED : newState ? HELD_DOWN : KEPT_UP;
		}

		public ControllerState getAButton() {
			boolean previousState = this.mAButton;
			boolean newState = mInnerController.getAButton();
			this.mAButton = newState;
			return compareBooleanState(previousState, newState);
		}

		public ControllerState getBButton() {
			boolean previousState = mBButton;
			boolean newState = mInnerController.getBButton();
			this.mBButton = newState;
			return compareBooleanState(previousState, newState);
		}

		public ControllerState getXButton() {
			boolean previousState = mXButton;
			boolean newState = mInnerController.getXButton();
			mXButton = newState;
			return compareBooleanState(previousState, newState);
		}

		public ControllerState getYButton() {
			boolean previousState = mYButton;
			boolean newState = mInnerController.getYButton();
			mYButton = newState;
			return compareBooleanState(previousState, newState);
		}

		public ControllerState getBumper(Hand h) {
			boolean previousState = h == Hand.kLeft ? mLeftBumper : mRightBumper;
			boolean newState = mInnerController.getBumper(h);
			if (h == Hand.kLeft)
				mLeftBumper = newState;
			else
				mRightBumper = newState;
			return compareBooleanState(previousState, newState);
		}

		public ControllerState getTrigger(Hand h) {
			boolean previousState = h == Hand.kLeft ? mLeftTrigger : mRightTrigger;
			boolean newState = mInnerController.getTriggerAxis(h) >= 0.5;
			if (h == Hand.kLeft)
				mLeftTrigger = newState;
			else
				mRightTrigger = newState;
			return compareBooleanState(previousState, newState);
		}

		public ControllerState getStickButton(Hand h) {
			boolean previousState = h == Hand.kLeft ?
					mLeftStickButton : mRightStickButton;
			boolean newState = mInnerController.getStickButton(h);
			if (h == Hand.kLeft)
				mLeftStickButton = newState;
			else
				mRightStickButton = newState;
			return compareBooleanState(previousState, newState);
		}

		public ControllerState getStartButton() {
			boolean previousState = mStartButton;
			boolean newState = mInnerController.getStartButton();
			mStartButton = newState;
			return compareBooleanState(previousState, newState);
		}

		public ControllerState getBackButton() {
			boolean previousState = mBackButton;
			boolean newState = mInnerController.getBackButton();
			mBackButton = newState;
			return compareBooleanState(previousState, newState);
		}

		public ControllerState getDpad(int value) {
			int previousState = mDirectionalPad;
			int newState = mInnerController.getPOV(0);
			mDirectionalPad = newState;
			return newState != previousState ? newState == value ?
					PRESSED : RELEASED : newState == value ? HELD_DOWN : KEPT_UP;
		}

		public void setRumble(RumbleType type, double d) {
			mInnerController.setRumble(type, d);
		}

		public double getX(Hand hand) {
			return mInnerController.getX(hand);
		}

		public double getY(Hand hand) {
			return mInnerController.getY(hand);
		}
	}

	public class LoopsManager {
		void addInitialLoops() {
			mStateObservationLooper.registerStartLoop(mStateReportingLoop);
			mStateObservationLooper.registerLoop(mStateSenderLoop);
			mInputLooper.registerStartLoop(mSystemInputLoop);
			mStateChangeLooper.registerLoop(mSystemStateUpdateLoop);
			mStateChangeLooper.registerFinalLoop(mSystemOutputLoop);
		}

		void registerControllerLoop() {
			mInputLooper.registerFinalLoop(mControllerInputLoop);
		}

		void robotInit() {
			mStateObservationLooper.startLoops();
		}

		void disable() {
			mInputLooper.registerFinalLoop(null); // remove controller input
			mStateChangeLooper.stopLoops();
		}

		void enable() {
			mStateChangeLooper.startLoops();
		}
	}

	public static Pins pins(int... n) {
		return new Pins(n);
	}

	public static Pins channels(int... n) {
		return pins(n);
	}

	public static Pins pin(int n) {
		return pins(n);
	}

	public static Encoder encoderFromPins(Pins pins, boolean reverse, EncodingType encodingType) {
		return new Encoder(pins.get(0), pins.get(1), reverse, encodingType);
	}

	@Override
	public void robotInit() {
		logRobotState("Started");
		robotSystemsInit();
		mLoopsManager.robotInit();
	}

	@Override
	public void disabledInit() {
		logRobotState("Disabled");
		mLoopsManager.disable();
		mSubsystemsIterator.resetAll();
	}

	@Override
	public void autonomousInit() {
		logRobotState("Auto");
		mLoopsManager.enable();
	}

	@Override
	public void teleopInit() {
		logRobotState("Teleop");
		mLoopsManager.enable();
		mLoopsManager.registerControllerLoop();
		mCallback.onTeleopInit();
	}

	@Override
	public void testInit() {
		logRobotState("Test");
	}

	@Override
	public void robotPeriodic() {
	}

	@Override
	public void disabledPeriodic() {
	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopPeriodic() {
	}

	@Override
	public void testPeriodic() {
	}

	@Override
	public void startCompetition() {
		beginConstruction();
		if (mCallback != null && mMappingClass != null) {
			super.startCompetition();
		} else {
			System.err.println("Robot not set up");
		}
	}

	@SuppressWarnings("WeakerAccess")
	protected void setMapping(Class<?> mappingClass) {
		mMappingClass = mappingClass;
	}

	protected void setController(C controller) {
		mController = controller;
	}

	@SuppressWarnings("unused")
	protected boolean isFMSAttached() {
		return mDriverStation.isFMSAttached();
	}

	private static final String kSubsystemsClassName = "Subsystems";
	private static final String kMappingClassPostfix = ".mapping.Mapping";
	private static final double kObservationLooperDelta = 0.5;
	private static final double kStateChangeLooperDelta = 0.02;
	private static final double kInputLooperDelta = 0.02;
	private static final int kMaxPrintLength = 255;

	private int mPrintCounter;
	private String mLoggedRobotState;
	private Utils mUtils;
	private C mController;
	private ICallback<C> mCallback;
	private Class<?> mMappingClass;
	private List<ISubsystem> mSubsystems;
	private DriverStation mDriverStation;
	private ObservationAccumulator mObservationAccumulator;
	private PrintStream mAccumulatedPrinter;
	private List<StateObserver> mStateObservers;
	private SubsystemsIterator mSubsystemsIterator;
	private LoopsManager mLoopsManager;
	private Looper mStateObservationLooper;
	private Looper mInputLooper;
	private Looper mStateChangeLooper;
	private ILoop mStateReportingLoop; // Loop asking each system to report its state
	private ILoop mStateSenderLoop; // Loop that sends data to the driver station
	private ILoop mSystemInputLoop; // Loop asking each system to read sensor values
	private ILoop mControllerInputLoop; // Loop asking the callback to process the controller input
	private ILoop mSystemStateUpdateLoop; // Look asking each system to modify its current state based on its input
	private ILoop mSystemOutputLoop; // Loop asking each system to perform its output

	Robot() {
		super();
		mPrintCounter = 0;
		mDriverStation = DriverStation.getInstance();
		mUtils = new Utils();
		mObservationAccumulator = new ObservationAccumulator();
		mStateObservers = new ArrayList<>();
		mSubsystems = new ArrayList<>();
		mSubsystemsIterator = new SubsystemsIterator();
		mLoopsManager = new LoopsManager();
		mStateObservationLooper = new Looper(kObservationLooperDelta);
		mAccumulatedPrinter = new PrintStream(System.out, false);
		mStateChangeLooper = new Looper(kStateChangeLooperDelta);
		mInputLooper = new Looper(kInputLooperDelta);
		mStateReportingLoop = new SimpleLoop(mSubsystemsIterator::reportAll);
		mStateSenderLoop = new SimpleLoop(mObservationAccumulator::sendAllStates);
		mSystemInputLoop = new SimpleLoop(mSubsystemsIterator::inputAll);
		mControllerInputLoop = new SimpleLoop(this::onControllerInput);
		mSystemOutputLoop = new SimpleLoop(mSubsystemsIterator::outputAll);
		mSystemStateUpdateLoop = new SimpleLoop(mSubsystemsIterator::updateAll);
	}

	private class ObservationAccumulator {
		synchronized void reportState(Object owner, StateType stateType, Object state) {
			String prefix = resolveStatePrefix(owner, stateType);
			boolean foundCachedObserver = false;
			for (StateObserver observer : mStateObservers) {
				if (observer.isSameAs(state)) {
					observer.updateData();
					foundCachedObserver = true;
				}
			}
			if (!foundCachedObserver) {
				StateObserver observer = new StateObserver(prefix, state);
				observer.updateData();
				mStateObservers.add(observer);
			}
		}

		synchronized void print(Object object) {
			String value = String.valueOf(object);
			if (mPrintCounter <= kMaxPrintLength) {
				mPrintCounter += value.length();
				mAccumulatedPrinter.print(value);
			}
		}

		synchronized void sendAllStates() {
			mStateObservers.forEach(StateObserver::updateSmartDashboard);
			if (mPrintCounter > kMaxPrintLength) {
				DriverStation.reportError("Printing has exceeded the limit", false);
			}
			mPrintCounter = 0;
			mAccumulatedPrinter.flush();
		}

		private String resolveStatePrefix(Object owner, StateType stateType) {
			String prefix = owner.getClass().getSimpleName();
			if (stateType == StateType.INPUT) {
				return prefix.concat(".in");
			}
			return prefix.concat(".");
		}
	}

	private static class StateObserver {
		private final Object mObservedObject;
		private final String mObservedPrefix;
		private final Field[] mCachedFields;
		private Map<String, Object> mObservedMap;

		StateObserver(String prefix, Object object) {
			mObservedPrefix = prefix;
			mObservedObject = object;
			mCachedFields = mObservedObject.getClass().getDeclaredFields();
			mObservedMap = new HashMap<>();
			for (Field field : mCachedFields) {
				field.setAccessible(true);
			}
		}

		boolean isSameAs(Object other) {
			return mObservedObject == other;
		}

		void updateData() {
			for (Field stateField : mCachedFields) {
				try {
					String fieldName = stateField.getName();
					Object value = stateField.get(mObservedObject);
					String entryKey = mObservedPrefix + "." + fieldName;
					mObservedMap.put(entryKey, value);

				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		void updateSmartDashboard() {
			for (String entryKey : mObservedMap.keySet()) {
				Object value = mObservedMap.get(entryKey);
				if (value instanceof Number) {
					SmartDashboard.putNumber(entryKey, ((Number) value).doubleValue());
				} else if (value instanceof Boolean) {
					SmartDashboard.putBoolean(entryKey, (Boolean) value);
				} else {
					SmartDashboard.putString(entryKey, String.valueOf(value));
				}
			}
		}
	}

	private class SubsystemsIterator {
		void constructAll() {
			mSubsystems.forEach(ISubsystem::onConstruct);
		}

		void resetAll() {
			mSubsystems.forEach(ISubsystem::onDisabledReset);
		}

		void outputAll() {
			mSubsystems.forEach(ISubsystem::onOutputLoop);
		}

		void inputAll() {
			mSubsystems.forEach(ISubsystem::onInputLoop);
		}

		void reportAll() {
			mSubsystems.forEach(ISubsystem::onReportState);
		}

		void updateAll() {
			mSubsystems.forEach(ISubsystem::onUpdateState);
		}
	}

	private static class SimpleLoop implements ILoop {
		private Runnable mLoopFunction;

		SimpleLoop(Runnable loopFunction) {
			mLoopFunction = loopFunction;
		}

		@Override
		public void onStart() {
		}

		@Override
		public void onLoop() {
			mLoopFunction.run();
		}

		@Override
		public void onStop() {
		}
	}

	private static class Looper {
		private final Notifier mNotifier;
		private final List<ILoop> mLoops;
		private ILoop mStartLoop;
		private ILoop mFinalLoop;
		private final Object mTaskRunningLock;

		private boolean mIsRunning;
		private double mPeriod;

		private final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					synchronized (mTaskRunningLock) {
						if (mIsRunning) {
							if (mStartLoop != null) mStartLoop.onLoop();
							mLoops.forEach(ILoop::onLoop);
							if (mFinalLoop != null) mFinalLoop.onLoop();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		Looper(double delta) {
			mTaskRunningLock = new Object();
			mNotifier = new Notifier(runnable);
			mIsRunning = false;
			mLoops = new ArrayList<>();
			mPeriod = delta;
		}

		synchronized void registerLoop(ILoop loop) {
			synchronized (mTaskRunningLock) {
				mLoops.add(loop);
			}
		}

		synchronized void registerStartLoop(ILoop startLoop) {
			synchronized (mTaskRunningLock) {
				mStartLoop = startLoop;
			}
		}

		synchronized void registerFinalLoop(ILoop finalLoop) {
			synchronized (mTaskRunningLock) {
				mFinalLoop = finalLoop;
			}
		}

		synchronized void startLoops() {
			if (!mIsRunning) {
				synchronized (mTaskRunningLock) {
					if (mStartLoop != null) mStartLoop.onStart();
					mLoops.forEach(ILoop::onStart);
					if (mFinalLoop != null) mFinalLoop.onStart();
					mIsRunning = true;
				}
				mNotifier.startPeriodic(mPeriod);
			}
		}

		synchronized void stopLoops() {
			if (mIsRunning) {
				mNotifier.stop();
				synchronized (mTaskRunningLock) {
					mIsRunning = false;
					if (mStartLoop != null) mStartLoop.onStop();
					mLoops.forEach(ILoop::onStop);
					if (mFinalLoop != null) mFinalLoop.onStop();
				}
			}
		}
	}

	private abstract static class _Mask<C> extends Robot<C> implements ICallback<C> {
		_Mask() {
			super();
		}

		@Override
		public void onConstruct(Robot<C> robot) {
			onConstruct();
		}

		protected abstract void onConstruct();
	}

	void setCallback(ICallback<C> callback) {
		mCallback = callback;
	}

	private void onControllerInput() {
		mCallback.onControllerInput(mController);
	}

	private void beginConstruction() {
		printPrefix();
		Class<?> mappingClass = reflectMappingClass();
		if (mappingClass != null) {
			setMapping(mappingClass);
		}
		mCallback.onConstruct(this);
	}

	private void robotSystemsInit() {
		utils = mUtils;
		createReflectedSubsystems(reflectSubsystemsClass());
		mSubsystemsIterator.constructAll();
		mLoopsManager.addInitialLoops();
	}

	private Class<?> reflectMappingClass() {
		String mappingClassName = getClass().getPackage().getName() + kMappingClassPostfix;
		try {
			return Class.forName(mappingClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Class<?> reflectSubsystemsClass() {
		for (Class mappingSubclass : mMappingClass.getClasses()) {
			if (mappingSubclass.getSimpleName().equals(kSubsystemsClassName)) {
				return mappingSubclass;
			}
		}
		return null;
	}

	private void createReflectedSubsystems(Class<?> subsystemsClass) {
		mSubsystems.clear();
		if (subsystemsClass != null) {
			Field[] subsystemsClassFields = subsystemsClass.getFields();
			for (Field subsystemField : subsystemsClassFields) {
				if (ISubsystem.class.isAssignableFrom(subsystemField.getType())) {
					try {
						ISubsystem currentValue = (ISubsystem) subsystemField.get(null);
						if (currentValue == null) {
							Class subsystemType = subsystemField.getType();
							ISubsystem instance = (ISubsystem) subsystemType.newInstance();
							subsystemField.set(null, instance);
							mSubsystems.add(instance);
						} else {
							mSubsystems.add(currentValue);
						}
					} catch (IllegalAccessException | InstantiationException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void printPrefix() {
		System.out.print("(" + getClass().getSimpleName() + ") ");
	}

	private void logRobotState(String state) {
		if (state.equals(mLoggedRobotState)) {
			return;
		}
		mLoggedRobotState = state;
		SmartDashboard.putString("Robot State", state);
		printPrefix();
		System.out.println("Robot State: " + state);
	}
}
