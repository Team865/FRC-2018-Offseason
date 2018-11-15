package ca.warp7.frc.action.api.impl;

import ca.warp7.frc.action.api.IAction;
import ca.warp7.frc.action.api.IActionAPI;
import ca.warp7.frc.action.api.IActionConsumer;
import ca.warp7.frc.action.api.IActionPredicate;

abstract class HeadAPI extends IActionAPI.SyntaxProvider implements IActionAPI {

    @Override
    public IActionAPI async(IAction... actions) {
        return queue().async(actions);
    }

    @Override
    public IActionAPI asyncAny(IAction... actions) {
        return queue().asyncAny(actions);
    }

    @Override
    public IActionAPI asyncInverse(IAction... actions) {
        return queue().asyncInverse(actions);
    }

    @Override
    public IActionAPI asyncMaster(IAction master, IAction... slaves) {
        return queue().asyncMaster(master, slaves);
    }

    @Override
    public IActionAPI runIf(IActionPredicate predicate, IAction ifAction, IAction elseAction) {
        return queue().runIf(predicate, ifAction, elseAction);
    }

    @Override
    public IActionAPI await(IActionPredicate predicate) {
        return queue().await(predicate);
    }

    @Override
    public IActionAPI exec(IActionConsumer consumer) {
        return queue().exec(consumer);
    }

    @Override
    public IActionAPI broadcast(String... triggers) {
        return queue().broadcast(triggers);
    }
}
