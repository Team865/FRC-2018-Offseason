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

public class Components implements ISubsystem {

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
    private IControllerLoop mControllerLoop;

    @Override
    public void onConstruct() {
        mExtraComponents.forEach(IComponent::onConstruct);
        mSubsystems.forEach(ISubsystem::onConstruct);
        this.onZeroSensors();
    }

    @Override
    public void onDisabled() {
        mSubsystems.forEach(ISubsystem::onDisabled);
        this.onUpdateState();
    }

    @Override
    public void onAutonomousInit() {
        mSubsystems.forEach(ISubsystem::onAutonomousInit);
    }

    @Override
    public void onTeleopInit() {
        mSubsystems.forEach(ISubsystem::onTeleopInit);
    }

    @Override
    public void onMeasure() {
        mSubsystems.forEach(ISubsystem::onMeasure);
    }

    @Override
    public void onZeroSensors() {
        mSubsystems.forEach(ISubsystem::onZeroSensors);
    }

    @Override
    public void onOutput() {
        mSubsystems.forEach(ISubsystem::onOutput);
    }

    @Override
    public void onUpdateState() {
        mSubsystems.forEach(ISubsystem::onUpdateState);
    }

    @Override
    public void onReportState() {
        mSubsystems.forEach(ISubsystem::onReportState);
    }

    void setClass(Class<?> componentsClass) {
        mComponentsClass = componentsClass;
    }

    boolean hasClass() {
        return mComponentsClass != null;
    }

    @SuppressWarnings("unused")
    public void registerController(IController controller) {
        mControllers.add(controller);
    }

    void setControllerLoop(IControllerLoop controllerLoop) {
        mControllerLoop = controllerLoop;
        mControllerLoop.onRegister(this);
    }

    void controllerPeriodic() {
        mControllerLoop.onPeriodic();
    }

    void controllerUpdate() {
        mControllers.forEach(IController::onUpdateData);
    }

    boolean hasControlLoop() {
        return mControllerLoop != null;
    }

    List<ISubsystem> getSubsystems() {
        return mSubsystems;
    }

    void allocateObjects() {
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
