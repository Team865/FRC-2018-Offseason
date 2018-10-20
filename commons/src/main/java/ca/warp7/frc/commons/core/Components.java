package ca.warp7.frc.commons.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Use reflection to initialize the components of the Robot
 */

class Components {

    private static final String kComponentsClassName = ".Components";

    static Class<?> reflectComponentsFromPackageName(String packageName) {
        String potentialClassName = packageName + kComponentsClassName;
        try {
            return Class.forName(potentialClassName);
        } catch (ClassNotFoundException e) {
            System.err.println("Components not found");
            return null;
        }
    }

    private Class<?> mComponentsClass;
    private List<ISubsystem> mSubsystems = new ArrayList<>();
    private List<IComponent> mExtraComponents = new ArrayList<>();

    void setClass(Class<?> componentsClass) {
        mComponentsClass = componentsClass;
    }

    boolean hasClass(){
        return mComponentsClass != null;
    }

    List<ISubsystem> getSubsystems(){
        return mSubsystems;
    }

    void constructExtras(){
        mExtraComponents.forEach(IComponent::onConstruct);
    }

    void createAll(){
        if (mComponentsClass != null) {
            Field[] componentFields = mComponentsClass.getFields();
            for (Field componentField : componentFields) {
                if (ISubsystem.class.isAssignableFrom(componentField.getType())) {
                    try {
                        ISubsystem currentValue = (ISubsystem) componentField.get(null);
                        if (currentValue == null) {
                            Class subsystemType = componentField.getType();
                            ISubsystem instance = (ISubsystem) subsystemType.newInstance();
                            componentField.set(null, instance);
                            mSubsystems.add(instance);
                        } else {
                            mSubsystems.add(currentValue);
                        }
                    } catch (IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                } else if (IComponent.class.isAssignableFrom(componentField.getType())){
                    try {
                        IComponent currentValue = (IComponent) componentField.get(null);
                        if (currentValue == null) {
                            Class componentType = componentField.getType();
                            IComponent instance = (IComponent) componentType.newInstance();
                            componentField.set(null, instance);
                            mExtraComponents.add(instance);
                        } else {
                            mExtraComponents.add(currentValue);
                        }
                    } catch (IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
