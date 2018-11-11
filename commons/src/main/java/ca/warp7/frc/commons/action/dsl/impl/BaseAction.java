package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.core.IAction;

import java.util.List;

@SuppressWarnings("WeakerAccess")
abstract class BaseAction implements IAction {

    private BaseAction mParent;

    @Override
    public void onStart() {
    }

    BaseAction getParent() {
        return mParent;
    }

    void setParent(BaseAction parent) {
        mParent = parent;
    }

    List<IAction> getChildren() {
        return null;
    }

    void onBroadcast(Object trigger) {
        if (mParent != null) mParent.onBroadcast(trigger);
        else {
            List<IAction> children = getChildren();
            if (children != null) {
                for (IAction child : children) {
                    if (child instanceof BaseAction) {
                        ((BaseAction) child).onReceive(trigger);
                    }
                }
            }
        }
    }

    void onReceive(Object trigger) {
    }
}
