package ca.warp7.frc.commons.core;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class StateObserver {
    private final Object mObservedObject;
    private final Field[] mCachedFields;
    private final Map<String, Object> mObservedMap;
    private final NetworkTable mTable;

    StateObserver(NetworkTable table, Object object) {
        mObservedObject = object;
        mCachedFields = mObservedObject.getClass().getDeclaredFields();
        mObservedMap = new HashMap<>();
        mTable = table;
        for (Field field : mCachedFields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
        }
    }

    boolean isSameAs(Object other) {
        return mObservedObject == other;
    }

    void updateData() {
        for (Field stateField : mCachedFields) {
            try {
                String fieldName = stateField.getName();
                Object value = stateField.get(mObservedObject);
                mObservedMap.put(fieldName, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNetworkTableValue(NetworkTableEntry entry, Object value) {
        if (value instanceof Number) entry.setNumber((Number) value);
        else if (value instanceof Boolean) entry.setBoolean((Boolean) value);
        else if (value instanceof String) entry.setString((String) value);
        else if (value.getClass().isEnum()) entry.setString(value.toString());
        else entry.setString(value.getClass().getSimpleName() + " Object");
    }

    void updateNetworkTables() {
        for (String entryKey : mObservedMap.keySet()) {
            Object value = mObservedMap.get(entryKey);
            if (value instanceof ICollectiveState) ((ICollectiveState) value).getCollection().forEach(
                    (s, object) -> sendNetworkTableValue(mTable.getEntry(entryKey + "/" + s), object));
            else sendNetworkTableValue(mTable.getEntry(entryKey), value);
        }
    }
}
