package ca.warp7.frc.commons.action.dsl.impl;

import ca.warp7.frc.commons.core.IAction;

import java.util.List;

@SuppressWarnings("WeakerAccess")
abstract class BaseAction extends StopAction {

    private BaseAction mParent;

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
            if (children != null) children.stream().filter(child -> child instanceof BaseAction)
                    .forEach(child -> ((BaseAction) child).onReceive(trigger));
        }
    }

    void onReceive(Object trigger) {
    }
}
