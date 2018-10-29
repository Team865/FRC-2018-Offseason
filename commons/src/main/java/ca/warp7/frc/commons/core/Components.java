package ca.warp7.frc.commons.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Use reflection to initialize the components of the Robot
 */

class Components implements ISubsystem {

    private static final String kComponentsClassName = ".Components";

    private Class<?> mComponentsClass;
    private List<ISubsystem> mSubsystems = new ArrayList<>();
    private List<IComponent> mExtraComponents = new ArrayList<>();
    private IControls mControllerLoop;
    private boolean mControllerEnabled;

    @Override
    public void onConstruct() {
        this.collectObjects();
        mExtraComponents.forEach(IComponent::onConstruct);
        mSubsystems.forEach(ISubsystem::onConstruct);
        this.onZeroSensors();
    }

    @Override
    public void onDisabled() {
        mSubsystems.forEach(ISubsystem::onDisabled);
        mControllerEnabled = false;
    }

    @Override
    public void onAutonomousInit() {
        mSubsystems.forEach(ISubsystem::onAutonomousInit);
        mControllerEnabled = false;
    }

    @Override
    public void onTeleopInit() {
        mSubsystems.forEach(ISubsystem::onTeleopInit);
        mControllerEnabled = true;
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

    boolean isReadyToStart() {
        return mComponentsClass != null && mControllerLoop != null;
    }

    void setControllerLoop(IControls controllerLoop) {
        mControllerLoop = controllerLoop;
    }

    void controllerPeriodic() {
        if (mControllerEnabled) {
            mControllerLoop.periodic();
        }
    }

    private void collectObjects() {
        if (mComponentsClass != null) {
            Field[] componentFields = mComponentsClass.getFields();
            for (Field componentField : componentFields) {
                try {
                    if (ISubsystem.class.isAssignableFrom(componentField.getType())) {
                        ISubsystem fieldValue = (ISubsystem) componentField.get(null);
                        if (fieldValue != null) {
                            mSubsystems.add(fieldValue);
                        }
                    } else if (IComponent.class.isAssignableFrom(componentField.getType())) {
                        IComponent fieldValue = (IComponent) componentField.get(null);
                        if (fieldValue != null) {
                            mExtraComponents.add(fieldValue);
                        }
                    }
                } catch (IllegalAccessException e) {
                    System.out.println("ERROR Illegal Access collecting objects");
                }
            }
            int subsystemsSize = mSubsystems.size();
            int componentsSize = mExtraComponents.size();
            int skipped = componentFields.length - subsystemsSize - componentsSize;
            System.out.println(String.format("Components - Collected %d subsystems, %d extra components, and skipped %d fields",
                    subsystemsSize, componentsSize, skipped));
        } else {
            System.out.println("ERROR No objects to collect");
        }
    }

    void reflectFromPackage(String packageName) {
        String potentialClassName = packageName + kComponentsClassName;
        try {
            mComponentsClass = Class.forName(potentialClassName);
        } catch (ClassNotFoundException e) {
            System.err.println("Components not found");
            mComponentsClass = null;
        }
    }
}
