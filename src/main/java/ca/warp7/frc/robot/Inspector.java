package ca.warp7.frc.robot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class Inspector {
	private static final String kSubsystemsName = "Subsystems";

	static Class<?> getSubsystemsClass(Class<?> mappingClass) {
		Class<?> subsystemsClass = null;
		for (Class mappingSubclass : mappingClass.getClasses()) {
			if (mappingSubclass.getSimpleName().equals(kSubsystemsName)) {
				subsystemsClass = mappingSubclass;
			}
		}
		return subsystemsClass;
	}

	static List<ISubsystem> createSubsystems(Class<?> subsystemsClass) {
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
}
