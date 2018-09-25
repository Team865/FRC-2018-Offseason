package ca.warp7.frc;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.hal.HAL;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public abstract class Robot<C> extends IterativeRobot {
	private double mLoopDelta;
	private boolean mOverrideLoopDisabled;
	private List<ISubsystem> mSubsystems;
	private ICallback<C> mCallback;
	private Class<?> mMappingClass;
	private C mController;
	private DriverStation mDriverStation;
	private Utils mUtils;
	private StateObserver mStateObserver;
	private Map<String, Object> mObservedObjects;
	private ReflectedMaps mObjectStateMaps;
	private Notifier mMainLoopNotifier;

	@SuppressWarnings("WeakerAccess")
	public static Robot.Utils utils;

	private static final int kMaxMilliseconds = 1000 * 60 * 60 * 24;
	private static final double kUnassignedLoopDelta = -1;
	private static final double kMaxLoopDelta = 1;

	protected static final double WAIT_FOR_DRIVER_STATION = 0;

	@SuppressWarnings("SameParameterValue")
	protected void setLoopDelta(double loopDelta) {
		this.mLoopDelta = loopDelta;
	}

	@SuppressWarnings("SameParameterValue")
	protected void setMappingClass(Class<?> mappingClass) {
		mMappingClass = mappingClass;
	}

	@SuppressWarnings("WeakerAccess")
	protected void setCallback(ICallback<C> callback) {
		mCallback = callback;
	}

	protected void disableOverrideLoop() {
		this.mOverrideLoopDisabled = true;
	}

	protected boolean isFMSAttached() {
		return mDriverStation.isFMSAttached();
	}

	protected void setController(C controller) {
		mController = controller;
	}

	public static Encoder encoderFromPins(Pins pins, boolean reverse, EncodingType encodingType) {
		return new Encoder(pins.get(0), pins.get(1), reverse, encodingType);
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

	interface ICallback<C> {
		void onInit(Robot<C> robot);

		void onSetMapping();

		void onTeleopInit();

		void onTeleopPeriodic(C controller);
	}

	public interface ISubsystem {
		Object getState();

		void onInit();

		void onReset();
	}

	public abstract static class Callback<C> extends Robot<C> implements ICallback<C> {
		public Callback() {
			super();
			setCallback(this);
		}

		@Override
		public void onInit(Robot<C> robot) {
			onInit();
		}

		protected abstract void onInit();
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


	Robot() {
		super();
		mOverrideLoopDisabled = false;
		mLoopDelta = kUnassignedLoopDelta;
		mUtils = new Utils();
		mObjectStateMaps = new ReflectedMaps();
		mObservedObjects = new HashMap<>();
		mStateObserver = new StateObserver();
		mMainLoopNotifier = new Notifier(this::mainLoop);
		mDriverStation = m_ds;
	}

	private static class _NativeObserver {
		private static void start() {
			HAL.observeUserProgramStarting();
		}
	}

	private static class ReflectedMaps {
		private Map<String, Map<String, String>> mInnerMap;

		private ReflectedMaps() {
			mInnerMap = new HashMap<>();
		}

		private Map<String, String> getMap(String name) {
			if (!mInnerMap.containsKey(name)) {
				mInnerMap.put(name, new HashMap<>());
			}
			return mInnerMap.get(name);
		}
	}

	@SuppressWarnings("WeakerAccess")
	public class StateObserver {
		private void updateObjectStateMaps() {
			for (String objectName : mObservedObjects.keySet()) {
				Object observedObject = mObservedObjects.get(objectName);
				Map<String, String> objectStateMap = mObjectStateMaps.getMap(objectName);
				Class<?> stateClass = observedObject.getClass();
				Field[] stateFields = stateClass.getDeclaredFields();
				for (Field stateField : stateFields) {
					stateField.setAccessible(true);
					try {
						String fieldValue = stateField.get(observedObject).toString();
						objectStateMap.put(stateField.getName(), fieldValue);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}

		private void register(String name, Object object) {
			if (object != null) {
				mObservedObjects.put(name, object);
			}
		}

		public void register(ISubsystem subsystem) {
			register(subsystem.getClass().getSimpleName(), subsystem.getState());
		}

		public void registerAllSubsystems() {
			mSubsystems.forEach(this::register);
		}

		public void clearObserved() {
			mObservedObjects.clear();
		}
	}

	private Class<?> getInspectedSubsystemsClass() {
		for (Class mappingSubclass : mMappingClass.getClasses()) {
			if (mappingSubclass.getSimpleName().equals("Subsystems")) {
				return mappingSubclass;
			}
		}
		return null;
	}

	private void createLazySubsystems(Class<?> subsystemsClass) {
		mSubsystems = new ArrayList<>();
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
		return mLoopDelta != kUnassignedLoopDelta && mCallback != null && mMappingClass != null;
	}

	private void callbackInit() {
		System.out.print("(" + getRobotClassName() + ") ");
		mCallback.onInit(this);
	}

	private void systemsInit() {
		utils = mUtils;
		Class<?> subsystemsClass = getInspectedSubsystemsClass();
		createLazySubsystems(subsystemsClass);
		mCallback.onSetMapping();
		mSubsystems.forEach(ISubsystem::onInit);
		resetSubsystems();
		mStateObserver.updateObjectStateMaps();
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
		mMainLoopNotifier.startPeriodic(mLoopDelta);
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
		_NativeObserver.start();
		if (mLoopDelta <= 0 || mLoopDelta > kMaxLoopDelta) {
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
	}

	@Override
	public void testPeriodic() {
	}
}
