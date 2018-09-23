package ca.warp7.frc.robot;

public interface IRobotCallback<C> {
	void onInitWithBaseRobot(BaseRobot<C> robot);
	void onSetMapping();
	void onTeleopInit();
	void onTeleopPeriodic(C remote);
}