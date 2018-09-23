package ca.warp7.frc;

import ca.warp7.frc.utils.Pins;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRobot<C> extends IterativeRobot {

	private List<ISubsystem> mSubsystems;
	private ICallback<C> mCallback;
	private Class<?> mMappingClass;
	private Map<String, Map<String, String>> mStringStateMap;
	private C mController;

	BaseRobot() {
		super();
		mStringStateMap = new HashMap<>();
	}

	void setCallback(ICallback<C> callback) {
		mCallback = callback;
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

	protected DriverStation getDriverStation() {
		return DriverStation.getInstance();
	}


	protected void setController(C controller) {
		mController = controller;
	}

	@SuppressWarnings("SameParameterValue")
	protected void setMappingClass(Class<?> mappingClass) {
		mMappingClass = mappingClass;
	}

	private static Class<?> getSubsystemsClass(Class<?> mappingClass) {
		for (Class mappingSubclass : mappingClass.getClasses()) {
			if (mappingSubclass.getSimpleName().equals(getSubsystemsName())) {
				return mappingSubclass;
			}
		}
		return null;
	}

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

	private static String getSubsystemsName() {
		return "Subsystems";
	}

	private void updateStateMap() {
		for (ISubsystem subsystem : mSubsystems) {
			String subsystemsName = subsystem.getClass().getSimpleName();
			if (!mStringStateMap.containsKey(subsystemsName)) {
				mStringStateMap.put(subsystemsName, new HashMap<>());
			}
			Map<String, String> subsystemStateMap = mStringStateMap.get(subsystemsName);
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

	private void lazyInspectSubsystems() {
		Class<?> subsystemsClass = getSubsystemsClass(mMappingClass);
		mSubsystems = createSubsystems(subsystemsClass);
		initSubsystems();
	}

	public interface ICallback<C> {

		void onInit(BaseRobot<C> robot);

		void onSetMapping();

		void onTeleopInit();

		void onTeleopPeriodic(C remote);

	}

	@Override
	public void robotInit() {
		System.out.print("(" + getRobotName() + ") ");
		mCallback.onInit(this);
		lazyInspectSubsystems();
		updateStateMap();
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
