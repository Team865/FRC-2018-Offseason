package ca.warp7.frc.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class MapInspector {

	private static final String kSubsystemsClassName = "Subsystems";
	private static final String kMappingClassPostfix = ".constants.RobotMap";

	static List<ISubsystem> createReflectedSubsystems(Class<?> subsystemsClass) {
		List<ISubsystem> subsystems = new ArrayList<>();
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
							subsystems.add(instance);
						} else {
							subsystems.add(currentValue);
						}
					} catch (IllegalAccessException | InstantiationException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return subsystems;
	}

	static Class<?> reflectSubsystemsClass(Class<?> mMappingClass) {
		for (Class mappingSubclass : mMappingClass.getClasses()) {
			if (mappingSubclass.getSimpleName().equals(kSubsystemsClassName)) {
				return mappingSubclass;
			}
		}
		return null;
	}

	static Class<?> getMappingClass(String packageName) {
		String mappingClassName = packageName + kMappingClassPostfix;
		try {
			return Class.forName(mappingClassName);
		} catch (ClassNotFoundException e) {
			System.err.println("Mapping class not found");
			return null;
		}
	}
}
