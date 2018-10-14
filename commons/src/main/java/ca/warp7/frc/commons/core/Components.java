package ca.warp7.frc.commons.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Use reflection to get the subsystems
 */

class Components {

    private static final String kComponentsClassName = ".Components";

    static List<ISubsystem> getSubsystems(Class<?> componentsClass) {
        List<ISubsystem> subsystems = new ArrayList<>();
        if (componentsClass != null) {
            Field[] subsystemsClassFields = componentsClass.getFields();
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

    static Class<?> tryGetComponentsFromPackage(String packageName) {
        String potentialClassName = packageName + kComponentsClassName;
        try {
            return Class.forName(potentialClassName);
        } catch (ClassNotFoundException e) {
            System.err.println("Components not found");
            return null;
        }
    }
}
