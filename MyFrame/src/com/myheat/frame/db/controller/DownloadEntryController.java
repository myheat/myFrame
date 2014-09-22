package com.myheat.frame.db.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.myheat.frame.db.DBController;
import com.myheat.frame.db.DBNotInitializeException;
import com.myheat.frame.entities.DownloadEntry;
import com.myheat.frame.tool.TextUtil;

/**
 * 数据库控制类
 * 
 * @author renzhiqiang
 *
 */
public class DownloadEntryController {

	private static Dao<DownloadEntry, String> getDao() throws SQLException,
			DBNotInitializeException {
		return DBController.getDB().getDao(DownloadEntry.class);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean addOrUpdate(DownloadEntry dto) {
		try {
			getDao().createOrUpdate(dto);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DBNotInitializeException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param callback
	 */
	public static void addOrUpdate(ArrayList<DownloadEntry> callback) {
		if (!TextUtil.isValidate(callback)) {
			return;
		}
		try {
			for (DownloadEntry homework : callback) {
				getDao().createOrUpdate(homework);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DBNotInitializeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static DownloadEntry queryById(String id) {
		try {
			return getDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DBNotInitializeException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static boolean delete(String id) {
		try {
			getDao().deleteById(id);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DBNotInitializeException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 查找所有未完成任务记录
	 * 
	 * @return
	 */
	public static LinkedHashMap<String, DownloadEntry> queryAllUnCompletedRecord() {
		LinkedHashMap<String, DownloadEntry> queue = null;
		List<DownloadEntry> resultList = null;
		try {
			QueryBuilder<DownloadEntry, String> queryBuilder = getDao()
					.queryBuilder();
			queryBuilder.orderBy(DownloadEntry.CREATE_TIME, false);

			resultList = getDao().query(queryBuilder.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DBNotInitializeException e) {
			e.printStackTrace();
		}
		if (TextUtil.isValidate(resultList)) {
			queue = new LinkedHashMap<String, DownloadEntry>();
			for (DownloadEntry entry : resultList) {
				queue.put(entry.getId(), entry);
			}
		}
		return queue;
	}
}
