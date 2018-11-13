package ca.warp7.frc.action.dsl.impl;

import ca.warp7.frc.action.dsl.def.IActionDelegate;
import ca.warp7.frc.core.IAction;

import java.util.List;

@SuppressWarnings("WeakerAccess")
abstract class BaseAction extends StopAction implements IActionDelegate {

    private BaseAction mParent;

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
