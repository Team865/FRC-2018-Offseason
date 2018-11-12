package ca.warp7.frc.action;

import ca.warp7.frc.core.IAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Executes one action at a time. Useful as a member of {@link ParallelAction}
 *
 * @author Team 254, modified by Team 865
 */

@Deprecated
public class SeriesAction implements IAction {

    private IAction mCurAction;
    private final List<IAction> mRemainingActions;

    public SeriesAction(List<IAction> actions) {
        mRemainingActions = new ArrayList<>(actions);
        mCurAction = null;
    }

    public SeriesAction(IAction... actions) {
        mRemainingActions = Arrays.asList(actions);
        mCurAction = null;
    }

    @Override
    public void onStart() {
    }

    @Override
    public boolean shouldFinish() {
        return mRemainingActions.isEmpty() && mCurAction == null;
    }

    @Override
    public void onUpdate() {
        if (mCurAction == null) {
            if (mRemainingActions.isEmpty()) {
                return;
            }

            mCurAction = mRemainingActions.remove(0);
            mCurAction.onStart();
        }

        mCurAction.onUpdate();

        if (mCurAction.shouldFinish()) {
            mCurAction.onStop();
            mCurAction = null;
        }
    }
}
