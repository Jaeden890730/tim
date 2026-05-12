package com.mxic.oiplus.batch.archive;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import com.mxic.gprs.dao.BaseDao;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.rs.TDSConfig;
import com.mxic.oiplus.util.GPRSDB;
import com.mxic.oiplus.util.TDSLogger;

public class TfInformationDao {

	/**
	 * 取得備份清單, by 產單資料
	 * @param productType
	 * @param productBody
	 * @param brand
	 * @param version
	 * @return
	 */
	public static ArrayList getBackupListByPorduct(String productType, String productBody, String brand, String version){
		Properties prop;
		String reduceNumber = "-2";
		try {
			prop = TDSConfig.getProperty("TIMPdf");
			reduceNumber = prop.getProperty("oi_archive_version_to_reduce");
		} catch (Exception e) {
			TDSLogger.println(e);
		}
		
		String sql = 
			"select *\n" +
			"from tf_information a\n" + 
			"where a.status = 'R'\n" + 
			"   and (a.achv_flag is null or a.achv_flag != 'Y') \n" + 
			"   and a.product_type = '" + productType + "'\n";
		if(productBody != null && !productBody.equals(""))
			sql += "   and a.product_body = '" + productBody + "'\n";
		if(brand != null && !brand.equals(""))
			sql += "   and a.brand = '" + brand + "'\n";
		if(version != null && !version.equals("")){
			sql += "   and a.version = '" + version + "'\n";
		}else{
			sql +=
				"   and version <= (select max(to_number(version))" + reduceNumber + "\n" + 
				"                   from tf_information b\n" + 
				"                   where a.product_body = b.product_body\n" + 
				"                      and a.brand = b.brand\n" + 
				"                      and b.status = 'R')";
		}
		TDSLogger.println(sql);
		
		ArrayList al = new ArrayList();
 		Connection con = null;
 		try {
			con= DBConnection.getConnection();

			ResultSet rs = GPRSDB.qryRSBySql(con, sql, new Object[0]);
			while(rs.next()){
				TfInformationBean bean = (TfInformationBean)GPRSDB.RStoObjectBean(rs, TfInformationBean.class);
				al.add(bean);
			}			
 		} catch (Exception e) {
 			TDSLogger.println(e);
		}finally{
			DBConnection.close(con);
		}
		
		return al;
	}
	
	/**
	 * 取得備份清單, by 時間範圍
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static ArrayList getBackupListByTime(String startTime, String endTime){
		Properties prop;
		String reduceNumber = "-2";
		try {
			prop = TDSConfig.getProperty("TIMPdf");
			reduceNumber = prop.getProperty("oi_archive_version_to_reduce");
		} catch (Exception e) {
			TDSLogger.println(e);
		}
		
		String sql = 
			"select *\n" +
			"from tf_information a\n" + 
			"where a.status = 'R'\n" + 
			"   and (a.achv_flag is null or a.achv_flag != 'Y')\n" + 
			"   and a.log_time between to_date('" + startTime + " 00:00:00', 'YYYY/MM/DD HH24:MI:SS') \n" +
			"                      and to_date('" + endTime + " 23:59:59', 'YYYY/MM/DD HH24:MI:SS') \n" + 
			"   and version <= (select max(to_number(version))" + reduceNumber + "\n" + 
			"                   from tf_information b\n" + 
			"                   where a.product_body = b.product_body\n" + 
			"                      and a.brand = b.brand\n" + 
			"                      and b.status = 'R')";
		TDSLogger.println(sql);
		
		ArrayList al = new ArrayList();
		Connection con = null;
		try {
			con = DBConnection.getConnection();
			
			ResultSet rs = GPRSDB.qryRSBySql(con, sql, new Object[0]);
			while(rs.next()){
				TfInformationBean bean = (TfInformationBean)GPRSDB.RStoObjectBean(rs, TfInformationBean.class);
				al.add(bean);
			}
		}catch(Exception e){	
			TDSLogger.println(e);
		}finally{
			DBConnection.close(con);
		}
		return al;
	}
		
	/**
	 * 更新 Archive Flag
	 * @param con
	 * @param tfInfoBean
	 * @throws Exception
	 */
	public static void updateArchiveFlag(Connection con, TfInformationBean tfInfoBean) throws Exception{
		try {
			BaseDao.update(con, tfInfoBean, "tf_information", 
					new ArrayList(Arrays.asList(new String[]{"product_body", "brand", "version"})));
		} catch (Exception e) {
			TDSLogger.println(e);
			throw new Exception("update tf_information table error!");
		}
	}	
}
