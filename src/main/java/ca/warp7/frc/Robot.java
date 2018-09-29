package ca.warp7.frc;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static ca.warp7.frc.ControllerState.*;

@SuppressWarnings({"unused", "SameParameterValue", "WeakerAccess"})
public abstract class Robot<C> extends IterativeRobot {
	public static Robot.Utils utils;

	public interface IStateOwner {
		Object getState();
	}

	public interface ISubsystem extends IStateOwner {
		void onInit();

		void onReset();
	}

	public interface ICallback<C> {
		void onInit(Robot<C> robot);

		void onTeleopInit();

		void onTeleopPeriodic(C controller);
	}

	public interface ILoop {
		void onStart();

		void onLoop();

		void onStop();
	}

	@SuppressWarnings("unused")
	public class Utils {
		public String getRobotName() {
			return getRobotClassName();
		}

		public void print(Object object) {
			System.out.println(object);
		}

		public Robot<C> getRobot() {
			return Robot.this;
		}

		public DriverStation getDriverStation() {
			return mDriverStation;
		}

		public StateObserver getStateObserver() {
			return mStateObserver;
		}
	}

	public class StateObserver {
		public void observeAndSendStates() {
			mLockedObservers.forEach(LockedObserver::observeAndSendStates);
		}

		public void register(IStateOwner owner) {
			if (owner != null) {
				LockedObserver observer;
				observer = new LockedObserver(owner.getClass().getSimpleName(), owner.getState());
				mLockedObservers.add(observer);
			}
		}

		public void registerAllSubsystems() {
			mSubsystems.forEach(this::register);
		}

		public void clearAll() {
			mLockedObservers.forEach(LockedObserver::clear);
			mLockedObservers.clear();
		}
	}

	public abstract static class Main<C> extends _Mask<C> {
		public Main() {
			super();
			setCallback(this);
		}
	}

	public static class SimpleLoop implements ILoop {
		private Runnable mLoopFunction;

		public SimpleLoop(Runnable loopFunction) {
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

		public int[] array() {
			return mPinsArray;
		}
	}

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

	public static Encoder encoderFromPins(Pins pins, boolean reverse, EncodingType encodingType) {
		return new Encoder(pins.get(0), pins.get(1), reverse, encodingType);
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

	protected void setMapping(Class<?> mappingClass) {
		mMappingClass = mappingClass;
	}

	protected void setController(C controller) {
		mController = controller;
	}

	protected boolean isFMSAttached() {
		return mDriverStation.isFMSAttached();
	}

	private static final String kSubsystemsClassName = "Subsystems";
	private static final String kMappingClassPostfix = ".Mapping";
	private static final double kObservationLooperDelta = 0.5;

	private String mLoggedRobotState;
	private Utils mUtils;
	private C mController;
	private ICallback<C> mCallback;
	private Class<?> mMappingClass;
	private List<ISubsystem> mSubsystems;
	private DriverStation mDriverStation;
	private StateObserver mStateObserver;
	private List<LockedObserver> mLockedObservers;
	private ILoop mStateObservationLoop;
	private Looper mObservationLooper;
	private LoopsManager mLoopsManager;

	Robot() {
		super();
		mDriverStation = DriverStation.getInstance();
		mUtils = new Utils();
		mStateObserver = new StateObserver();
		mSubsystems = new ArrayList<>();
		mLockedObservers = new ArrayList<>();
		mStateObservationLoop = new SimpleLoop(mStateObserver::observeAndSendStates);
		mObservationLooper = new Looper(kObservationLooperDelta);
		mLoopsManager = new LoopsManager();
	}

	private abstract static class _Mask<C> extends Robot<C> implements ICallback<C> {
		_Mask() {
			super();
		}

		@Override
		public void onInit(Robot<C> robot) {
			onInit();
		}

		protected abstract void onInit();
	}

	private class LockedObserver {
		private final Object mStateObject;
		private final String mStateObjectName;
		private final Field[] mCachedFields;

		LockedObserver(String name, Object object) {
			mStateObjectName = name;
			mStateObject = object;
			synchronized (mStateObject) {
				mCachedFields = mStateObject.getClass().getDeclaredFields();
				for (Field field : mCachedFields) {
					field.setAccessible(true);
				}
			}
		}

		void observeAndSendStates() {
			synchronized (mStateObject) {
				for (Field stateField : mCachedFields) {
					try {
						String fieldName = stateField.getName();
						Object value = stateField.get(mStateObject);
						String entryKey = mStateObjectName + "." + fieldName;
						if (value instanceof Number) {
							SmartDashboard.putNumber(entryKey, ((Number) value).doubleValue());
						} else if (value instanceof Boolean) {
							SmartDashboard.putBoolean(entryKey, (Boolean) value);
						} else {
							SmartDashboard.putString(entryKey, String.valueOf(value));
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}

		void clear() {
			for (Field stateField : mCachedFields) {
				String fieldName = stateField.getName();
				String entryKey = mStateObjectName + "." + fieldName;
				SmartDashboard.delete(entryKey);
			}
		}
	}

	private class LoopsManager {
		void robotInit() {
			mObservationLooper.startLoops();
		}

		void disable() {

		}
	}

	private static class Looper {
		private final Notifier mNotifier;
		private final List<ILoop> mLoops;
		private final Object mTaskRunningLock;

		private boolean mIsRunning;
		private double mTimestamp = 0;
		private double mMeasuredDelta = 0;
		private double mPeriod;

		private final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					synchronized (mTaskRunningLock) {
						if (mIsRunning) {
							double now = Timer.getFPGATimestamp();
							for (ILoop loop : mLoops) {
								loop.onLoop();
							}
							mMeasuredDelta = now - mTimestamp;
							mTimestamp = now;
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

		public synchronized void registerLoop(ILoop loop) {
			synchronized (mTaskRunningLock) {
				mLoops.add(loop);
			}
		}

		synchronized void startLoops() {
			if (!mIsRunning) {
				synchronized (mTaskRunningLock) {
					mTimestamp = Timer.getFPGATimestamp();
					for (ILoop loop : mLoops) {
						loop.onStart();
					}
					mIsRunning = true;
				}
				mNotifier.startPeriodic(0);
			}
		}

		synchronized void stopLoops() {
			if (mIsRunning) {
				mNotifier.stop();
				synchronized (mTaskRunningLock) {
					mIsRunning = false;
					for (ILoop loop : mLoops) {
						loop.onStop();
					}
				}
			}
		}
	}

	void setCallback(ICallback<C> callback) {
		mCallback = callback;
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

	private String getRobotClassName() {
		return getClass().getSimpleName();
	}

	private boolean isInitialSetupDone() {
		return mCallback != null && mMappingClass != null;
	}

	private void printPrefix() {
		System.out.print("(" + getRobotClassName() + ") ");
	}

	private void initCallback() {
		printPrefix();
		Class<?> mappingClass = reflectMappingClass();
		if (mappingClass != null) {
			setMapping(mappingClass);
		}
		mCallback.onInit(this);
	}

	private void robotSystemsInit() {
		utils = mUtils;
		createReflectedSubsystems(reflectSubsystemsClass());
		mSubsystems.forEach(ISubsystem::onInit);
		mSubsystems.forEach(ISubsystem::onReset);
		mObservationLooper.registerLoop(mStateObservationLoop);
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

	@Override
	public void startCompetition() {
		initCallback();
		if (isInitialSetupDone()) {
			super.startCompetition();
		} else {
			System.err.println("Robot not set up");
		}
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
		mSubsystems.forEach(ISubsystem::onReset);
		mLoopsManager.disable();
	}

	@Override
	public void autonomousInit() {
		logRobotState("Auto");
	}

	@Override
	public void teleopInit() {
		logRobotState("Teleop");
		mSubsystems.forEach(ISubsystem::onReset);
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
		mCallback.onTeleopPeriodic(mController);
	}

	@Override
	public void testPeriodic() {
	}
}
