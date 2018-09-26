package ca.warp7.frc;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.wpi.first.wpilibj.hal.HAL.observeUserProgramStarting;

@SuppressWarnings({"unused", "SameParameterValue", "WeakerAccess"})
public abstract class Robot<C> extends IterativeRobot {
	public static Robot.Utils utils;

	public interface ICallback<C> {
		void onInit(Robot<C> robot);

		void onConfigureMapping();

		void onTeleopInit();

		void onTeleopPeriodic(C controller);
	}

	public interface IStateOwner {
		Object getState();
	}

	public interface ISubsystem extends IStateOwner {
		void onInit();

		void onReset();
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
		private void updateObjectStates() {
			for (String objectName : mObservedObjects.keySet()) {
				Object observedObject = mObservedObjects.get(objectName);
				Map<String, Object> objectStateMap = mObjectStateMaps.getMap(objectName);
				if (!mObservedFieldsCache.containsKey(objectName)) {
					Class<?> stateClass = observedObject.getClass();
					Field[] stateFields = stateClass.getDeclaredFields();
					for (Field stateField : stateFields) {
						stateField.setAccessible(true);
					}
					mObservedFieldsCache.put(objectName, stateFields);
				}
				for (Field stateField : mObservedFieldsCache.get(objectName)) {
					try {
						Object fieldValue = stateField.get(observedObject);
						objectStateMap.put(stateField.getName(), fieldValue);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}

		public void sendStates() {
			for (String objectName : mObservedObjects.keySet()) {
				Map<String, Object> objectStateMap = mObjectStateMaps.getMap(objectName);
				for (String fieldName : objectStateMap.keySet()) {
					String entryKey = objectName + "." + fieldName;
					Object value = objectStateMap.get(fieldName);
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

		public void register(IStateOwner stateOwner) {
			if (stateOwner != null) {
				mObservedObjects.put(stateOwner.getClass().getSimpleName(), stateOwner.getState());
			}
		}

		public void registerAllSubsystems() {
			mSubsystems.forEach(this::register);
		}

		public void clearAll() {
			for (String objectName : mObservedObjects.keySet()) {
				Map<String, Object> objectStateMap = mObjectStateMaps.getMap(objectName);
				for (String fieldName : objectStateMap.keySet()) {
					SmartDashboard.delete(objectName + "." + fieldName);
				}
			}
			mObservedFieldsCache.clear();
			mObservedObjects.clear();
			mObjectStateMaps.clear();
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

	public static Encoder encoderFromPins(Pins pins, boolean reverse, EncodingType encodingType) {
		return new Encoder(pins.get(0), pins.get(1), reverse, encodingType);
	}

	protected static final double WAIT_FOR_DRIVER_STATION = 0;

	protected void setMainLoopDelta(double loopDelta) {
		mMainLoopDelta = loopDelta;
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

	private double mMainLoopDelta;
	private boolean mOverrideLoopDisabled;
	private List<ISubsystem> mSubsystems;
	private ICallback<C> mCallback;
	private Class<?> mMappingClass;
	private C mController;
	private DriverStation mDriverStation;
	private Utils mUtils;
	private StateObserver mStateObserver;
	private Map<String, Object> mObservedObjects;
	private Map<String, Field[]> mObservedFieldsCache;
	private ReflectedMaps mObjectStateMaps;
	private Notifier mMainLoopNotifier;
	private static final int kMaxMilliseconds = 1000 * 60 * 60 * 24;
	private static final double kUnassignedLoopDelta = -1;
	private static final double kMaxLoopDelta = 1;
	private static final String kSubsystemsClassName = "Subsystems";
	private static final String kMappingClassPostfix = ".Mapping";

	private abstract static class _Mask<C> extends Robot<C> implements ICallback<C> {
		@Override
		public void onInit(Robot<C> robot) {
			onInit();
		}

		protected abstract void onInit();
	}


	private static class ReflectedMaps {
		private Map<String, Map<String, Object>> mInnerMap;

		private ReflectedMaps() {
			mInnerMap = new HashMap<>();
		}

		private Map<String, Object> getMap(String name) {
			if (!mInnerMap.containsKey(name)) {
				mInnerMap.put(name, new HashMap<>());
			}
			return mInnerMap.get(name);
		}

		private void clear() {
			mInnerMap.clear();
		}
	}

	Robot() {
		super();
		mOverrideLoopDisabled = false;
		mMainLoopDelta = kUnassignedLoopDelta;
		mUtils = new Utils();
		mObjectStateMaps = new ReflectedMaps();
		mObservedObjects = new HashMap<>();
		mObservedFieldsCache = new HashMap<>();
		mStateObserver = new StateObserver();
		mMainLoopNotifier = new Notifier(this::mainLoop);
		mDriverStation = m_ds;
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
		return mMainLoopDelta != kUnassignedLoopDelta && mCallback != null && mMappingClass != null;
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
		Class<?> subsystemsClass = reflectSubsystemsClass();
		createReflectedSubsystems(subsystemsClass);
		mCallback.onConfigureMapping();
		mSubsystems.forEach(ISubsystem::onInit);
		resetSubsystems();
		mStateObserver.updateObjectStates();
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
		mMainLoopNotifier.startPeriodic(mMainLoopDelta);
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
		if (mMainLoopDelta <= 0 || mMainLoopDelta > kMaxLoopDelta) {
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
		mStateObserver.updateObjectStates();
		mStateObserver.sendStates();
	}

	@Override
	public void testPeriodic() {
	}
}
