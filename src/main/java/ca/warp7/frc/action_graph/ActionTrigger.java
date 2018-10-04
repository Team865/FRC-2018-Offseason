package ca.warp7.frc.action_graph;

class ActionTrigger {
	private String mName;

	ActionTrigger(String name) {
		mName = name;
	}

	String getName() {
		return mName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ActionTrigger) {
			return mName.equals(((ActionTrigger) obj).mName);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return mName.hashCode();
	}
}
