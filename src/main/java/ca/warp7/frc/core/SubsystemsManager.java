package ca.warp7.frc.core;

import java.util.List;

class SubsystemsManager {
	private List<ISubsystem> mSubsystems;

	public void setSubsystems(List<ISubsystem> subsystems) {
		mSubsystems = subsystems;
	}

	void constructAll() {
		mSubsystems.forEach(ISubsystem::onConstruct);
	}

	void resetAll() {
		mSubsystems.forEach(ISubsystem::onDisabledReset);
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
