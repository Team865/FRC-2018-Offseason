package ca.warp7.frc;

import ca.warp7.frc.utils.Pins;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.hal.HAL;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Robot<C> extends IterativeRobot {
	private double mLoopDelta;
	private boolean mOverrideLoopDisabled;
	private List<ISubsystem> mSubsystems;
	private ICallback<C> mCallback;
	private Class<?> mMappingClass;
	private C mController;
	private DriverStation mDriverStation;
	private ReflectedMaps mStateMaps;

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

	@SuppressWarnings("unused")
	protected void disableOverrideLoop() {
		this.mOverrideLoopDisabled = true;
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

	protected boolean isFMSAttached() {
		return mDriverStation.isFMSAttached();
	}

	protected void setController(C controller) {
		mController = controller;
	}

	interface ICallback<C> {
		void onInit(Robot<C> robot);

		void onSetMapping();

		void onTeleopInit();

		void onTeleopPeriodic(C controller);
	}

	Robot() {
		super();
		mOverrideLoopDisabled = false;
		mLoopDelta = -1;
		mStateMaps = new ReflectedMaps();
		mDriverStation = m_ds;
	}

	private static class Inspector {

		private static List<ISubsystem> createSubsystems(Class<?> subsystemsClass) {
			List<ISubsystem> subsystems = new ArrayList<>();
			if (subsystemsClass != null) {
				Field[] subsystemsClassFields = subsystemsClass.getFields();
				for (Field subsystemClassField : subsystemsClassFields) {
					if (ISubsystem.class.isAssignableFrom(subsystemClassField.getType())) {
						try {
							Class subsystemType = subsystemClassField.getType();
							ISubsystem instance = (ISubsystem) subsystemType.newInstance();
							subsystems.add(instance);
							subsystemClassField.set(null, instance);
						} catch (IllegalAccessException | InstantiationException e) {
							e.printStackTrace();
						}
					}
				}
			}
			return subsystems;
		}

		private static Class<?> getSubsystemsClass(Class<?> mappingClass) {
			for (Class mappingSubclass : mappingClass.getClasses()) {
				if (mappingSubclass.getSimpleName().equals("Subsystems")) {
					return mappingSubclass;
				}
			}
			return null;
		}
	}

	private static class Observer {
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

	private void updateStateMaps() {
		for (ISubsystem subsystem : mSubsystems) {
			String subsystemsName = subsystem.getClass().getSimpleName();
			Map<String, String> subsystemStateMap = mStateMaps.getMap(subsystemsName);
			Object subsystemState = subsystem.getState();
			Class<?> stateClass = subsystemState.getClass();
			Field[] stateFields = stateClass.getDeclaredFields();
			for (Field stateField : stateFields) {
				stateField.setAccessible(true);
				try {
					String fieldValue = stateField.get(subsystemState).toString();
					subsystemStateMap.put(stateField.getName(), fieldValue);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getRobotName() {
		return getClass().getSimpleName();
	}

	private void resetSubsystems() {
		mSubsystems.forEach(ISubsystem::onReset);
	}

	private void initSubsystems() {
		mCallback.onSetMapping();
		mSubsystems.forEach(ISubsystem::onInit);
		resetSubsystems();
	}

	private void lazySubsystems() {
		Class<?> subsystemsClass = Inspector.getSubsystemsClass(mMappingClass);
		mSubsystems = Inspector.createSubsystems(subsystemsClass);
		initSubsystems();
	}

	private void realInit() {
		System.out.print("(" + getRobotName() + ") ");
		mCallback.onInit(this);
		lazySubsystems();
		updateStateMaps();
	}

	private void mainLoop() {
		super.loopFunc();
	}

	private void waitingLoop() {
		while (!Thread.interrupted()) {
			mDriverStation.waitForData();
			mainLoop();
		}
	}

	private static final int kMaxMilliseconds = 1000 * 60 * 60 * 24;

	private void timedLoop() {
		Notifier loop = new Notifier(this::mainLoop);
		loop.startPeriodic(mLoopDelta);
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(kMaxMilliseconds);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private boolean checkSetup() {
		return mLoopDelta != -1 && mCallback != null && mMappingClass != null;
	}

	private void overrideCompetition() {
		robotInit();
		Observer.start();
		if (mLoopDelta <= 0 || mLoopDelta > 1) {
			waitingLoop();
		} else {
			timedLoop();
		}
	}

	@Override
	public void startCompetition() {
		if (checkSetup()) {
			if (mOverrideLoopDisabled) {
				super.startCompetition();
			} else {
				overrideCompetition();
			}
		} else {
			DriverStation.reportError("Robot not set up", false);
		}
	}

	@Override
	public void robotInit() {
		realInit();
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
