package ca.warp7.frc.robot;

public interface IConstructCallback<C> {
	void onInitWithConstruct(ConstructRobot<C> robot);
	void onSetupMapping();
	void onTeleopInit();
	void onTeleopPeriodic(C remote);
}