package ca.warp7.frc.action_graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Executes one action at a time. Useful as a member of {@link ParallelActions}
 *
 * @author Team 254, modified by Team 865
 */

public class ActionSeries implements IAction {

	private IAction mCurAction;
	private final List<IAction> mRemainingActions;

	public ActionSeries(List<IAction> actions) {
		mRemainingActions = new ArrayList<>(actions);
		mCurAction = null;
	}

	public ActionSeries(IAction... actions) {
		mRemainingActions = Arrays.asList(actions);
		mCurAction = null;
	}

	@Override
	public boolean shouldFinish() {
		return mRemainingActions.isEmpty() && mCurAction == null;
	}

	@Override
	public void onStart() {
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

	@Override
	public void onStop() {
	}
}
