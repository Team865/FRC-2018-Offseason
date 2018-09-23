package ca.warp7.frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;

import java.util.List;

public abstract class BaseRobot<C> extends IterativeRobot {

	private List<ISubsystem> mSubsystems;
	private IRobotCallback<C> mCallback;
	private Class<?> mMappingClass;
	private C mController;

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

	void setCallback(IRobotCallback<C> callback) {
		mCallback = callback;
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

	@Override
	public void robotInit() {
		mCallback.onInitWithBaseRobot(this);
		System.out.println(" (a.k.a " + getRobotName() + ")");
		Class<?> subsystemsClass = Inspector.getSubsystemsClass(mMappingClass);
		mSubsystems = Inspector.createSubsystems(subsystemsClass);
		initSubsystems();
	}

	@Override
	public void disabledInit() {
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
