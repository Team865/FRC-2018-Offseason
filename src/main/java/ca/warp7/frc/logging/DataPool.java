package ca.warp7.frc.logging;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class DataPool {
	
	private static final NetworkTable table = NetworkTableInstance.getDefault().getTable("data");
	private static final ArrayList<DataPool> sDataPools = new ArrayList<>();
	private final ArrayList<Object> mData;
	private final ArrayList<String> mKeys;
	private final String mName;

	
	public DataPool(String name) {
		sDataPools.add(this);
		mData = new ArrayList<>();
		mKeys = new ArrayList<>();
		this.mName = name;
	}

	private void logData(String key, Object o) {
		boolean alreadyExists = false;
		int index = 0;
		for (String item : mKeys) {
			if (item.equals(key)) {
				alreadyExists = true;
				break;
			}
			index++;
		}
		if (alreadyExists) {
			mData.set(index, o);
		} else {
			mData.add(o);
			mKeys.add(key);
		}
	}

	public void logInt(String key, int i) {
		logData(key, i);
	}

	public void logBoolean(String key, boolean b) {
		logData(key, b);
	}

	public void logDouble(String key, double d) {
		logData(key, d);
	}

	public static void collectAllData() {
		if (sDataPools.isEmpty())
			return;
		for (DataPool pool : sDataPools) {
			if (!pool.mData.isEmpty()) {
				NetworkTable poolTable = table.getSubTable(pool.mName);
				for (int i = 0; i < pool.mData.size(); i++) {
					NetworkTableEntry binTable = poolTable.getEntry(pool.mKeys.get(i));
					binTable.setValue(pool.mData.get(i));
				}
			}
		}
	}
	
	private static Object getObjectData(String subTableName, String valueName){
		NetworkTable data = NetworkTableInstance.getDefault().getTable("data").getSubTable(subTableName);
		NetworkTableEntry stuff = data.getEntry(valueName);
		return stuff.getValue().getValue();
	}
	
	private static String getStringData(String subTableName, String valueName){
		return getObjectData(subTableName, valueName).toString();
	}
	
	public static double getDoubleData(String subTableName, String valueName) throws NumberFormatException{
		return Double.parseDouble(getStringData(subTableName, valueName));
	}
	
	public static int getIntData(String subTableName, String valueName) throws NumberFormatException{
		return Integer.parseInt(getStringData(subTableName, valueName));
	}
	
	public static boolean getBooleanData(String subTableName, String valueName){
		return Boolean.parseBoolean(getStringData(subTableName, valueName));
	}
}