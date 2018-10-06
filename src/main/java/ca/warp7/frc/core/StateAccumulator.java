package ca.warp7.frc.core;

import ca.warp7.frc.comms.ReportType;
import ca.warp7.frc.comms.StateObserver;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

class StateAccumulator {

	private static final int kMaxPrintLength = 255;

	private int mPrintCounter;
	private List<StateObserver> mStateObservers;
	private PrintStream mAccumulatedPrinter;

	StateAccumulator() {
		mPrintCounter = 0;
		mStateObservers = new ArrayList<>();
		mAccumulatedPrinter = new PrintStream(System.out, false);
	}

	synchronized void reportState(Object owner, ReportType reportType, Object state) {
		String prefix = owner.getClass().getSimpleName();
		if (reportType == ReportType.STATE_INPUT) {
			prefix = prefix.concat(".in");
		}
		boolean foundCachedObserver = false;
		for (StateObserver observer : mStateObservers) {
			if (observer.isSameAs(state)) {
				observer.updateData();
				foundCachedObserver = true;
			}
		}
		if (!foundCachedObserver) {
			StateObserver observer = new StateObserver(prefix, state);
			observer.updateData();
			mStateObservers.add(observer);
		}
	}

	synchronized void print(Object object) {
		String value = String.valueOf(object);
		if (mPrintCounter <= kMaxPrintLength) {
			mPrintCounter += value.length();
			mAccumulatedPrinter.print(value);
		}
	}

	synchronized void sendAll() {
		mStateObservers.forEach(StateObserver::updateSmartDashboard);
		if (mPrintCounter > kMaxPrintLength) {
			System.err.println("Printing has exceeded the limit");
		}
		mPrintCounter = 0;
		mAccumulatedPrinter.flush();
	}
}
