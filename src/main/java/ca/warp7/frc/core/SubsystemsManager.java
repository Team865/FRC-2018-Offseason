package ca.warp7.frc.core;

import java.util.List;

/**
 * Calls the subsystem functions by batch
 */
class SubsystemsManager {
	private List<ISubsystem> mSubsystems;

	public void setSubsystems(List<ISubsystem> subsystems) {
		mSubsystems = subsystems;
	}

	void constructAll() {
		mSubsystems.forEach(ISubsystem::onConstruct);
	}

	void disableAll() {
		mSubsystems.forEach(ISubsystem::onDisabled);
	}

	void onAutonomousInit() {
		mSubsystems.forEach(ISubsystem::onAutonomousInit);
	}

	void onTeleopInit() {
		mSubsystems.forEach(ISubsystem::onTeleopInit);
	}

	void outputAll() {
		mSubsystems.forEach(ISubsystem::onOutputLoop);
	}

	void inputAll() {
		mSubsystems.forEach(ISubsystem::onInputLoop);
	}

	void reportAll() {
		mSubsystems.forEach(ISubsystem::onReportState);
	}

	void updateAll() {
		mSubsystems.forEach(ISubsystem::onUpdateState);
	}
}
