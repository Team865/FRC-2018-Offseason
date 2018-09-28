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
import static edu.wpi.first.wpilibj.hal.HAL.observeUserProgramStarting;

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

		void onConfigureMapping();

		void onTeleopInit();

		void onTeleopPeriodic(C controller);
	}

	public interface ILoop {
		void onStart();

		void onLoop();

		void onStop();
	}

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

	protected static final double WAIT_FOR_DRIVER_STATION = 0;

	protected void setControlLoopDelta(double loopDelta) {
		mControlLoopDelta = loopDelta;
	}

	protected void setMapping(Class<?> mappingClass) {
		mMappingClass = mappingClass;
	}

	protected void disableOverrideLoop() {
		mOverrideLoopDisabled = true;
	}

	protected boolean isFMSAttached() {
		return mDriverStation.isFMSAttached();
	}

	protected void setController(C controller) {
		mController = controller;
	}

	protected static Pins pins(int... n) {
		return new Pins(n);
	}

	protected static Pins channels(int... n) {
		return pins(n);
	}

	protected static Pins pin(int n) {
		return pins(n);
	}

	private static final String kSubsystemsClassName = "Subsystems";
	private static final String kMappingClassPostfix = ".Mapping";
	private static final int kMaxMilliseconds = 1000 * 60 * 60 * 24;
	private static final double kUnassignedLoopDelta = -1;
	private static final double kMaxLoopDelta = 1;
	private double mControlLoopDelta;
	private boolean mOverrideLoopDisabled;
	private Utils mUtils;
	private C mController;
	private ICallback<C> mCallback;
	private Class<?> mMappingClass;
	private List<ISubsystem> mSubsystems;
	private DriverStation mDriverStation;
	private StateObserver mStateObserver;
	private List<LockedObserver> mLockedObservers;
	private Notifier mControlLoopNotifier;

	private abstract static class _Mask<C> extends Robot<C> implements ICallback<C> {
		public _Mask() {
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

		public LockedObserver(String name, Object object) {
			mStateObjectName = name;
			mStateObject = object;
			synchronized (mStateObject) {
				mCachedFields = mStateObject.getClass().getDeclaredFields();
				for (Field field : mCachedFields) {
					field.setAccessible(true);
				}
			}
		}

		public void observeAndSendStates() {
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

		public void clear() {
			for (Field stateField : mCachedFields) {
				String fieldName = stateField.getName();
				String entryKey = mStateObjectName + "." + fieldName;
				SmartDashboard.delete(entryKey);
			}
		}
	}

	private class Looper {
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

		public Looper(double delta) {
			mTaskRunningLock = new Object();
			mNotifier = new Notifier(runnable);
			mIsRunning = false;
			mLoops = new ArrayList<>();
			mPeriod = delta;
		}

		public synchronized void register(ILoop loop) {
			synchronized (mTaskRunningLock) {
				mLoops.add(loop);
			}
		}

		public synchronized void start() {
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

		public synchronized void stop() {
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

	Robot() {
		super();
		mUtils = new Utils();
		mOverrideLoopDisabled = false;
		mControlLoopDelta = kUnassignedLoopDelta;
		mOverrideLoopDisabled = false;
		mStateObserver = new StateObserver();
		mSubsystems = new ArrayList<>();
		mLockedObservers = new ArrayList<>();
		mDriverStation = m_ds;
		mControlLoopNotifier = new Notifier(this::mainLoop);
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
			for (Field subsystemClassField : subsystemsClassFields) {
				if (ISubsystem.class.isAssignableFrom(subsystemClassField.getType())) {
					try {
						Class subsystemType = subsystemClassField.getType();
						ISubsystem instance = (ISubsystem) subsystemType.newInstance();
						mSubsystems.add(instance);
						subsystemClassField.set(null, instance);
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

	private void resetSubsystems() {
		mSubsystems.forEach(ISubsystem::onReset);
	}

	private boolean isInitialSetupDone() {
		return mControlLoopDelta != kUnassignedLoopDelta && mCallback != null && mMappingClass != null;
	}

	private void callbackInit() {
		System.out.print("(" + getRobotClassName() + ") ");
		Class<?> mappingClass = reflectMappingClass();
		if (mappingClass != null) {
			setMapping(mappingClass);
		}
		mCallback.onInit(this);
	}

	private void systemsInit() {
		utils = mUtils;
		createReflectedSubsystems(reflectSubsystemsClass());
		mCallback.onConfigureMapping();
		mSubsystems.forEach(ISubsystem::onInit);
		resetSubsystems();
		mStateObserver.observeAndSendStates();
	}

	private void mainLoop() {
		super.loopFunc();
	}

	private void iterativeLoop() {
		while (!Thread.interrupted()) {
			mDriverStation.waitForData();
			mainLoop();
		}
	}

	private void timedLoop() {
		mControlLoopNotifier.startPeriodic(mControlLoopDelta);
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(kMaxMilliseconds);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private void overrideStartCompetition() {
		robotInit();
		observeUserProgramStarting();
		if (mControlLoopDelta <= 0 || mControlLoopDelta > kMaxLoopDelta) {
			iterativeLoop();
		} else {
			timedLoop();
		}
	}

	@Override
	public void startCompetition() {
		callbackInit();
		if (isInitialSetupDone()) {
			if (mOverrideLoopDisabled) {
				super.startCompetition();
			} else {
				overrideStartCompetition();
			}
		} else {
			System.err.println("Robot not set up");
		}
	}

	@Override
	public void robotInit() {
		systemsInit();
	}

	@Override
	public void disabledInit() {
		resetSubsystems();
	}

	@Override
	public void autonomousInit() {
	}

	@Override
	public void teleopInit() {
		resetSubsystems();
		mCallback.onTeleopInit();
	}

	@Override
	public void testInit() {
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
		mStateObserver.observeAndSendStates();
		mStateObserver.observeAndSendStates();
	}

	@Override
	public void testPeriodic() {
	}
}
