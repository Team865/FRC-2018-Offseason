package ca.warp7.frc.core;

public interface ISubsystem {
	void onConstruct();

	void onDisabledReset();

	void onInputLoop();

	void onOutputLoop();

	void onUpdateState();

	void onReportState();
}
