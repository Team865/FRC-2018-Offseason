package ca.warp7.frc.commons.core;

import ca.warp7.frc.commons.Pins;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Use reflection to initialize the components of the Robot
 */

public class Components {

    private static final String kComponentsClassName = ".Components";

    static Class<?> reflectFromPackageName(String packageName) {
        String potentialClassName = packageName + kComponentsClassName;
        try {
            return Class.forName(potentialClassName);
        } catch (ClassNotFoundException e) {
            System.err.println("Components not found");
            return null;
        }
    }

    public static Encoder encoder(Pins pins, boolean reverse, CounterBase.EncodingType encodingType) {
        return new Encoder(pins.get(0), pins.get(1), reverse, encodingType);
    }

    private Class<?> mComponentsClass;
    private List<ISubsystem> mSubsystems = new ArrayList<>();
    private List<IComponent> mExtraComponents = new ArrayList<>();
    private List<IController> mControllers = new ArrayList<>();

    /**
     * A procedure passed into the {@link LoopsManager} runner called in teleop
     */
    private IControllerLoop mControllerLoop;

    void setClass(Class<?> componentsClass) {
        mComponentsClass = componentsClass;
    }

    boolean hasClass() {
        return mComponentsClass != null;
    }

    public void registerController(IController controller) {
        mControllers.add(controller);
    }

    /**
     * Sets the Operator Input loop, which should get input from controllers and
     * pass them to subsystems
     */
    void setControllerLoop(IControllerLoop controllerLoop) {
        mControllerLoop = controllerLoop;
        mControllerLoop.onInit(this);
    }

    ILoop getControllerLoop() {
        return () -> {
            mControllers.forEach(IController::updateValues);
            mControllerLoop.onPeriodic();
        };
    }

    boolean hasControlLoop() {
        return mControllerLoop != null;
    }

    List<ISubsystem> getSubsystems() {
        return mSubsystems;
    }

    void constructExtras() {
        mExtraComponents.forEach(IComponent::onConstruct);
    }

    void createAll() {
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
                } else if (IComponent.class.isAssignableFrom(componentField.getType())) {
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
