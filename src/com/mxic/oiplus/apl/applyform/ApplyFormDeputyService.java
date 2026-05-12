package com.mxic.oiplus.apl.applyform;

import java.sql.Connection;
import java.util.ArrayList;
import com.mxic.oiplus.util.GPRSDB;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.oiplus.resource.*;
import com.mxic.gprs.deputy.DeputyService;

public class ApplyFormDeputyService {

	/**
	 * (代理人使用1)取得員工及所有上層主管資料
	 * 
	 * @param empNo
	 *            員工工號
	 * @param cond
	 *            條件
	 * @param Level
	 *            level代表層級 0為自已，1為直屬主管.....
	 * @return
	 */
	public static String getDeputyByEmp(Connection con, String in_query_type, String in_query_id, String in_return_type) {

		DeputyService deputyService = new DeputyService();
		String emp = "";
		if (in_query_type.equals("1")) {
			if (in_return_type.equals("EMP_NO"))
				emp = deputyService.getDeputyEmpNoByNo(in_query_id);
			else if (in_return_type.equals("EMP_ID"))
				emp = deputyService.getDeputyEmpIdByNo(in_query_id);
			else if (in_return_type.equals("EMAIL"))
				emp = deputyService.getDeputyMailByNo(in_query_id);
		}
		return emp == null ? "" : emp;

		/*
		 * String return_value = ""; try { String sql = "SELECT  TDS.GET_DEPUTY(?,?,?,'OTHER') RETURN_VALUE FROM DUAL \n"; ArrayList al = GPRSDB.qryListBySql(con, sql, new Object[] { in_query_type, in_query_id, in_return_type }); if (al != null && al.size() != 0) { return_value = String.valueOf(al.get(0)); } } catch (Exception e) { TDSLogger.println(e); } finally { } return return_value;
		 */
	}

	/**
	 * (代理人使用1)取得員工及所有上層主管資料
	 * 
	 * @param empNo
	 *            員工工號
	 * @param cond
	 *            條件
	 * @param Level
	 *            level代表層級 0為自已，1為直屬主管.....
	 * @return
	 */
	public static String getDeputyByEmp(String in_query_type, String in_query_id, String in_return_type) {

		String return_value = "";
		Connection con = null;
		try {
			con = DBConnection.getConnection();
			return_value = getDeputyByEmp(con, in_query_type, in_query_id, in_return_type);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(con);
		}
		return return_value;
	}

	public static void addDeputyList(ArrayList al, String userid) {
		if (userid == null || "".equals(userid))
			return;
		String ss = ApplyFormDeputyService.getDeputyByEmp("1", userid, "EMP_NO");
		if (!"".equals(ss)) {
			al.add(ss);
		}
	}

	public static boolean isDeputyByEmp(String columnid, String empid) {
		if (columnid == null || "".equals(columnid) || empid == null || "".equals(empid))
			return false;
		String ss = ApplyFormDeputyService.getDeputyByEmp("1", columnid, "EMP_NO");
		if (!"".equals(ss) && empid.equals(ss)) {
			return true;
		} else {
			return false;
		}
	}

}
