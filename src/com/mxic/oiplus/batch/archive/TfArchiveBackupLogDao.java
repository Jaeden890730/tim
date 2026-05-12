package com.mxic.oiplus.batch.archive;

import java.sql.Connection;
import java.util.ArrayList;

import com.mxic.gprs.applyform.ApplyFormUtil;
import com.mxic.gprs.dao.BaseDao;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.GPRSDB;
import com.mxic.oiplus.util.TDSLogger;

public class TfArchiveBackupLogDao {
	
	public static final String TABLE_NAME = "tf_archive_backup_log";
	
	/**
	 * 新增一筆備份log
	 * @param con
	 * @param appId
	 * @param backupDir
	 * @param fileName
	 * @throws GprsDaoException
	 */
	public static void insert(Connection con, String productType, String productBody, String brand, String version, String backupDir, String fileName)  throws Exception{
		TfArchiveBackupLogBean bean = new TfArchiveBackupLogBean();
		try {
			bean.setAchv_sid(getSequence());
			bean.setBackup_date(ApplyFormUtil.getNowDate());
			bean.setProduct_type(productType);
			bean.setProduct_body(productBody);
			bean.setBrand(brand);
			bean.setVersion(version);
			bean.setBackup_dir(backupDir);
			bean.setFile_name(fileName);
			bean.setBackup_result("Pass");
			BaseDao.insert(con, bean, TABLE_NAME); 
		} catch (Exception e) {
			TDSLogger.println(e);
			throw new Exception("GprsArchiveBackupLogDao.insert() error: - achvSid: " + bean.getAchv_sid() + ", error: " + e.getMessage());
		}
	}
	
	public static void insertComments(Connection con, String productType, String productBody, String brand, String version, String backupDir, String comments)  throws Exception{
		TfArchiveBackupLogBean bean = new TfArchiveBackupLogBean();
		try {
			bean.setAchv_sid(getSequence());
			bean.setBackup_date(ApplyFormUtil.getNowDate());
			bean.setProduct_type(productType);
			bean.setProduct_body(productBody);
			bean.setBrand(brand);
			bean.setVersion(version);
			bean.setBackup_dir(backupDir);
			bean.setBackup_result("Fail");
			bean.setComments(comments);
			BaseDao.insert(con, bean, TABLE_NAME); 
		} catch (Exception e) {
			TDSLogger.println(e);
			throw new Exception("GprsArchiveBackupLogDao.insert() error: - achvSid: " + bean.getAchv_sid() + ", error: " + e.getMessage());
		}
	}
	
	/**
	 * Get Sequence 
	 * @return
	 */
	private static String getSequence(){
		String seq = null;
	    Connection con = null;
	    try {
	    	con = DBConnection.getConnection();
			String sql = "select tf_archive_backup_log_seq.nextval from dual";
			ArrayList al = GPRSDB.qryListBySql(con, sql, new Object[] {});
			if(al!=null && al.size()!=0)
				seq = String.valueOf(al.get(0));
			else
				throw new RuntimeException("cann't get tf_archive_backup_log_seq sequence.");
		} catch (Exception e) {
			TDSLogger.println(e);
			throw new RuntimeException(e.getMessage());
		} finally {
			DBConnection.close(con);
		}
		return seq;
	}
}
