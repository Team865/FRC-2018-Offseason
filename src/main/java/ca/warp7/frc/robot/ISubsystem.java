package ca.warp7.frc.robot;

public interface ISubsystem {
	Object getState();
	void onInit();
	void onReset();
}
