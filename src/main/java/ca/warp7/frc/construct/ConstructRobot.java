package ca.warp7.frc.construct;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;

public abstract class ConstructRobot<C> extends IterativeRobot {
	private SystemMapper mMapper;
	private IConstructCallback<C> mCallback;
	private Class<?> mMappingClass;
	private C mController;

	private String getRobotName() {
		return getClass().getSimpleName();
	}

	private void resetSubsystems() {
		mMapper.resetSubsystems();
	}

	private void initSubsystems() {
		mMapper.initMappingAndSubsystems();
		resetSubsystems();
	}

	void callback(IConstructCallback<C> callback) {
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
		mCallback.onInitWithConstruct(this);
		System.out.println(" (a.k.a " + getRobotName() + ")");
		mMapper = new SystemMapper(mMappingClass, mCallback);
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
