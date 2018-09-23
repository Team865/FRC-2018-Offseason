package ca.warp7.frc;

public interface ISubsystem {
	Object getState();
	void onInit();
	void onReset();
}
