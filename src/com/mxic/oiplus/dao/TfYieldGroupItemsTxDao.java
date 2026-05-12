package com.mxic.oiplus.dao;

import java.sql.Connection;
import java.util.HashMap;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.GPRSDB;
import com.mxic.oiplus.util.TDSLogger;

public class TfYieldGroupItemsTxDao {
	
	/**
	 * type: hold or downgrade
	 * 
	 * @return
	 */
	public static HashMap<String, String>[] getList(String type, String productCode, String testMode, String prodLevel, String version){
		HashMap<String, String>[] hm = null;
		String sql = "select * from tf_yield_groupitems_tx\n" +
					"where function_type = 0 and type = ? " +
					"and product_code = ? and test_mode = ? and prod_level = ? and version = ?\n" + 
					"order by group_no, item_no, item_type, item";
		//TDSLogger.println(sql);		
		Connection con = null;
		try {
			con = DBConnection.getConnection();
			hm = GPRSDB.qryHashMapBySql(con, sql, new Object[]{type, productCode, testMode, prodLevel, version});
		}catch(Exception e){	
			TDSLogger.println(e);
		}finally{
			DBConnection.close(con);
		}
		return hm;
	}
	
	
	/**
	 * §R°£ group
	 * @param con
	 * @param bean
	 * @throws Exception 
	 * @throws GprsDaoException
	 */
	public static void delete(Connection conn, String type, String productCode, String testMode, String prodLevel, String version) throws Exception {
			HashMap<String, String> cond = new HashMap<String, String>();
			cond.put("type", type);
			cond.put("product_code", productCode);
			cond.put("test_mode", testMode);
			cond.put("prod_level", prodLevel);
			cond.put("version", version);
			GPRSDB.delete(conn, "tf_yield_groupitems_tx", cond);
	}

	public static void delete(Connection conn, String sid, String productCode, String testMode) throws Exception {
		String sql = "delete from tf_yield_groupitems_tx where sid = ? and product_code = ? and test_mode like ?";
		GPRSDB.execDML(conn, sql, new Object[]{sid, productCode, testMode});
	}
	
	/*
	public static void deleteAllByProductBody(Connection con, String productBody, String version) throws Exception {
		String sql = "delete from tf_yield_groupitems_tx where product_code like '?%' and version = ?";
		GPRSDB.execDML(con, sql, new Object[]{productBody, version});
	}
	*/



	public static void insert(Connection conn, YieldGroupItemsBean groupItemsBean) throws Exception{
		if(groupItemsBean.getGroup_name() != null){
			for(int i=0; i<groupItemsBean.getGroup_name().length; i++){
				TDSLogger.println(groupItemsBean.getGroup_name()[i]+";"+groupItemsBean.getGroup_no()[i]+";"+groupItemsBean.getItem_no()[i]+
						";"+groupItemsBean.getItem_type()[i]+";"+groupItemsBean.getItem_name()[i]+";"+groupItemsBean.getRange_value()[i]);				
				
				insert(conn, groupItemsBean.getSid(), groupItemsBean.getHold_downgrade(), groupItemsBean.getProduct_code(), groupItemsBean.getTest_mode(), groupItemsBean.getWafer_level(), groupItemsBean.getVersion(),
						groupItemsBean.getGroup_no()[i], groupItemsBean.getItem_no()[i], groupItemsBean.getItem_type()[i], groupItemsBean.getItem_name()[i], 
						groupItemsBean.getRange_value()[i]);			
			}
		}
	}
	
	/**
	 * ·s¼W group item
	 * @param con
	 * @param bean
	 * @throws Exception 
	 */
	public static void insert(Connection con, String sid, String type, String productCode, String testMode, String prodLevel, String version, String groupNo, String itemNo, String itemType, String item, String rangeValues) throws Exception {
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("sid", sid);
		values.put("type", type);
		values.put("product_code", productCode);
		values.put("test_mode", testMode);
		values.put("prod_level", prodLevel);
		values.put("version", version);
		values.put("group_no", groupNo);
		values.put("item_no", itemNo);
		values.put("item_type", itemType);
		values.put("item", item);		
		values.put("range_values", rangeValues);	
		GPRSDB.insert(con, "tf_yield_groupitems_tx", values);
	}
	
	//
	public static void deleteBySid(Connection con, String sid) throws Exception {
		String sql = "delete from tf_yield_groupitems_tx where sid = ?";
		GPRSDB.execDML(con, sql, new Object[]{sid});
	}

	public static void copyRelesedGroupItems(Connection con, String newSid, String productBody, String brand, int relesedVersion) throws Exception {
		String sql = 
				"insert into tf_yield_groupitems_tx (sid, type, product_code, prod_level, test_mode, version, group_no, item_no, item_type, item, range_values)\n" +
						"select distinct ?, a.type, a.product_code, a.prod_level, a.test_mode, b.version+1, a.group_no, a.item_no, a.item_type, a.item, a.range_values\n" + 
						"from tf_yield_groupitems a, tf_information b\n" + 
						"where b.product_body = ? and b.brand = ? and b.version = ? and a.sid = b.sid\n" + 
						"order by a.type, prod_level, test_mode, a.group_no, a.item_no";

		GPRSDB.execDML(con, sql, new Object[]{newSid, productBody, brand, relesedVersion});
	}
	
	public static void copyRelesedGroupItems(Connection con, String sid, String fromProduct, String fromMode, String toProduct, String toMode, String brand) throws Exception {
		String sql = 
			"insert into tf_yield_groupitems_tx (sid, type, product_code, prod_level, test_mode, version, group_no, item_no, item_type, item, range_values)\n" +
			"select ?, a.type, ?, a.prod_level, ?, (select version from tf_information where sid = ?) version, \n" +
			"	a.group_no, a.item_no, a.item_type, a.item, a.range_values\n" + 
			"from tf_yield_groupitems a, tf_current_version_vw b\n" + 
			"where b.product_body = substr(?, 0, 4)  and b.brand = ? and a.sid = b.sid\n" + 
			"    and a.product_code = ? and a.test_mode like ?\n" +
			"order by type, group_no, item_no, item_type";
		GPRSDB.execDML(con, sql, new Object[]{sid, toProduct, toMode, sid, fromProduct, brand, fromProduct, fromMode});
	}

	public static void copyRelesedGroupItems(Connection con, String sid, String fromProduct, String toProduct, String brand) throws Exception {
		String sql = 
			"insert into tf_yield_groupitems_tx (sid, type, product_code, prod_level, test_mode, version, group_no, item_no, item_type, item, range_values)\n" +
			"select ?, a.type, ?, a.prod_level, a.test_mode, (select version from tf_information where sid = ?) version, \n" +
			"	a.group_no, a.item_no, a.item_type, a.item, a.range_values\n" + 
			"from tf_yield_groupitems a, tf_current_version_vw b\n" + 
			"where b.product_body = substr(?, 0, 4)  and b.brand = ? and a.sid = b.sid\n" + 
			"    and a.product_code = ? \n" +
			"order by a.type, prod_level, test_mode, a.group_no, a.item_no";
		GPRSDB.execDML(con, sql, new Object[]{sid, toProduct, sid, fromProduct, brand, fromProduct});
	}
	
	public static void copyTxGroupItems(Connection con, String sid, String fromProduct, String fromMode, String toProduct, String toMode) throws Exception {
		String sql = 
				"insert into tf_yield_groupitems_tx (sid, type, product_code, prod_level, test_mode, version, group_no, item_no, item_type, item, range_values)\n" +
						"select a.sid, a.type, ?, a.prod_level, ?, a.version, a.group_no, a.item_no, a.item_type, a.item, a.range_values\n" + 
						"from tf_yield_groupitems_tx a\n" + 
						"where a.sid = ? and a.product_code = ? and a.test_mode = ? \n" + 
						"order by a.type, prod_level, test_mode, a.group_no, a.item_no";

		GPRSDB.execDML(con, sql, new Object[]{toProduct, toMode, sid, fromProduct, fromMode});
	}
	
	public static void copyTxGroupItems(Connection con, String sid, String fromProduct, String toProduct) throws Exception {
		String sql = 
				"insert into tf_yield_groupitems_tx (sid, type, product_code, prod_level, test_mode, version, group_no, item_no, item_type, item, range_values)\n" +
						"select a.sid, a.type, ?, a.prod_level, a.test_mode, a.version, a.group_no, a.item_no, a.item_type, a.item, a.range_values\n" + 
						"from tf_yield_groupitems_tx a\n" + 
						"where a.sid = ? and a.product_code = ? \n" + 
						"order by a.type, prod_level, test_mode, a.group_no, a.item_no";

		GPRSDB.execDML(con, sql, new Object[]{toProduct, sid, fromProduct});
	}
	
}
