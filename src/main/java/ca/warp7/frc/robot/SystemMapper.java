package ca.warp7.frc.robot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class SystemMapper {
	private final List<ISubsystem> mSubsystems;
	private final IConstructCallback mCallback;

	SystemMapper(Class<?> mappingClass, IConstructCallback callback) {
		mCallback = callback;
		mSubsystems = new ArrayList<>();
		Class<?> subsystemsClass = null;
		for (Class mappingSubclass : mappingClass.getClasses()){
			if (mappingSubclass.getSimpleName().equals("Subsystems")){
				subsystemsClass = mappingSubclass;
			}
		}
		if (subsystemsClass != null) {
			Field[] subsystemsClassFields = subsystemsClass.getFields();
			for (Field subsystemClassField : subsystemsClassFields){
				if(ISubsystem.class.isAssignableFrom(subsystemClassField.getType())){
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

	void initMappingAndSubsystems(){
		mCallback.onSetupMapping();
		mSubsystems.forEach(ISubsystem::onInit);
	}

	void resetSubsystems(){
		mSubsystems.forEach(ISubsystem::onReset);
	}
}
