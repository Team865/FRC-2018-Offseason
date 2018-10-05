package ca.warp7.frc.comms;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class StateObserver {
	private final Object mObservedObject;
	private final String mObservedPrefix;
	private final Field[] mCachedFields;
	private Map<String, Object> mObservedMap;

	public StateObserver(String prefix, Object object) {
		mObservedPrefix = prefix;
		mObservedObject = object;
		mCachedFields = mObservedObject.getClass().getDeclaredFields();
		mObservedMap = new HashMap<>();
		for (Field field : mCachedFields) {
			field.setAccessible(true);
		}
	}

	public boolean isSameAs(Object other) {
		return mObservedObject == other;
	}

	public void updateData() {
		for (Field stateField : mCachedFields) {
			try {
				String fieldName = stateField.getName();
				Object value = stateField.get(mObservedObject);
				String entryKey = mObservedPrefix + "." + fieldName;
				mObservedMap.put(entryKey, value);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateSmartDashboard() {
		for (String entryKey : mObservedMap.keySet()) {
			Object value = mObservedMap.get(entryKey);
			if (value instanceof Number) {
				SmartDashboard.putNumber(entryKey, ((Number) value).doubleValue());
			} else if (value instanceof Boolean) {
				SmartDashboard.putBoolean(entryKey, (Boolean) value);
			} else {
				SmartDashboard.putString(entryKey, String.valueOf(value));
			}
		}
	}
}
