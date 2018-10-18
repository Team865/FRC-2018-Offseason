package ca.warp7.frc.commons.scheduler;

class ActionTrigger {
    private final String mName;

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

    @Override
    public String toString() {
        return mName;
    }
}
