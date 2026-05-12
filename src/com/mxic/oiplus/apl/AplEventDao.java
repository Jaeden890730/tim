package com.mxic.oiplus.apl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mxic.oiplus.apl.bean.AplEventBean;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.GPRSDB;
import com.mxic.oiplus.util.TDSLogger;

public class AplEventDao {
	
	public AplEventBean[] getEventsByAppNo(String app_no){
		ArrayList al = new ArrayList();
		Connection con =null;
		try {
			con = DBConnection.getConnection();
			String sql =
				"select b.dept_id,\n" +
				"       nvl2(translate(a.USER_ID, '\\1234567890', '\\'),\n" + 
				"            a.USER_ID,\n" + 
				"            b.EMP_NAME_C) real_name,\n" + 
				"       a.status,\n" + 
				"       to_char(a.event_date, 'yyyy/mm/dd hh24:mi:ss') as event_date,\n" +
				"       a.is_agree,\n" +
				"       a.comments,\n" + 
				"       a.USER_ID,\n" + 
				"       b.TITLE emp_title,\n" +
				"       a.app_no\n" + 
				"  from ap_app_sign_event a,\n" + 
				"       (select distinct DEPT_ID, EMP_NO, EMP_NAME_C, TITLE\n" + 
				"          from BA_MXIC_EMP\n" + 
				"         where EMP_NO != '     '\n" + 
				"            or EMP_NAME_C = '¦@¥Î±b¸¹') b\n" + 
				" where a.USER_ID = b.EMP_NO(+)\n" + 
				"   and a.app_no = ?\n" + 
				" order by a.event_date";

			ResultSet rs = GPRSDB.qryRSBySql(con, sql, new Object[]{app_no});
			while(rs.next()){
				al.add((AplEventBean)GPRSDB.RStoObjectBean(rs, AplEventBean.class));
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		}finally{
			DBConnection.close(con);
		}
		return (AplEventBean[])al.toArray(new AplEventBean[]{});
	}
	
	public boolean insert(AplEventBean bean){
		boolean result = true;

		Connection con =null;
		try {
			con = DBConnection.getConnection();
			String sql = "Insert into ap_app_sign_event (APP_NO,EVENT_DATE,STATUS,USER_ID,IS_AGREE,COMMENTS)" +
			" VALUES(?,sysdate,?,?,?,?)";
			GPRSDB.execDML(con, sql, new Object[]{bean.getApp_no(),bean.getStatus(),
												bean.getUser_id(),bean.getIs_agree(),bean.getComments()});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnection.close(con);
		}

		return result;
	}
	
	public void insert(Connection con, AplEventBean bean) throws Exception{
		String sql = "Insert into ap_app_sign_event (APP_NO,EVENT_DATE,STATUS,USER_ID,IS_AGREE,COMMENTS)" +
			" VALUES(?,sysdate,?,?,?,?)";
		GPRSDB.execDML(con, sql, new Object[]{bean.getApp_no(),bean.getStatus(),
												bean.getUser_id(),bean.getIs_agree(),bean.getComments()});
	}	
}
