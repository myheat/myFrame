package com.myheat.frame.db;

import com.myheat.frame.MyApplication;

/**
 * 数据初始类
 * @author renzhiqiang
 *
 */
public class DBController {
	
	private static final int DB_VERSION =1;
	private static DatabaseHelper db = null;
	private static final String DB_NAME="myframe.db";

	public static void createDB(){
		   initialDB();
	}
	
	private static synchronized void initialDB(){
		if(db == null){
		   db = new DatabaseHelper(MyApplication.mContext,DB_NAME, null, DB_VERSION);
		}
	}

	public static DatabaseHelper getDB() throws DBNotInitializeException {
		initialDB();
		if (db == null) {
			throw new DBNotInitializeException("DB not created.");
		}
		return db;
	}


	public static synchronized void destoryDB() {
		if (db != null) {
			db.close();
//			OpenHelperManager.releaseHelper();
			db = null;
		}
	}
	
	public static synchronized void clearAllData(){
		if (db != null) {
			db.clearAllData();
		}
	}
}

