package com.mxic.oiplus.eif;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.tdsplus.util.DataHandlerUtil;
import com.mxic.tdsplus.util.TDSLogger;

public class EIFService {
	  
	public static int updateInterfaceTime(String interface_name, String timestamp) throws Exception {
		Connection conn = null;
		conn = DBConnection.getConnection();
		int succ = updateInterfaceTime(interface_name, timestamp, conn);
		
		DBConnection.close(conn);
		return succ;
	}
	public static int updateInterfaceTime(String interface_name, String timestamp, Connection conn) throws Exception {
		//Connection conn = null;
		//conn = DBConnection.getConnection();
		String sql = null;
		if (timestamp.equals("CURRENT_TIME"))
			sql = "update if_interface_time set current_time = sysdate where interface = '"+interface_name+"'";
		else if (timestamp.equals("LAST_TIME"))
			sql = "update if_interface_time set last_time = current_time where interface = '"+interface_name+"'";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		int succ = pstmt.executeUpdate();
		pstmt.close();
		//DBConnection.close(conn);
		return succ;
	}
	
	
	public static void interfaceStart(Connection conn, String interfaceName) {
		String sql = "UPDATE IF_INTERFACE_TIME SET CURRENT_TIME = SYSDATE WHERE INTERFACE = ? ";
		Object[] wheres = new Object[1];
		wheres[0] = interfaceName;
		try {
			int count = new DataHandlerUtil().updateByPrepareSQL(conn, sql, wheres);
			if (count == 0) {
			    sql = "INSERT INTO IF_INTERFACE_TIME (INTERFACE, LAST_TIME, CURRENT_TIME) VALUES (?, SYSDATE, SYSDATE)";
			    new DataHandlerUtil().updateByPrepareSQL(conn, sql, wheres);
			}
			conn.commit();
		} catch (Exception e) {
			TDSLogger.println(interfaceName + " update interface start time fail\n" + e);
		}
	}

	public static void interfaceStart(String interfaceName) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			interfaceStart(conn, interfaceName);
			DBConnection.commit(conn);
		} catch (Exception e) {
			DBConnection.rollback(conn);
		} finally {
			if (conn != null)
				DBConnection.close(conn);
		}
	}

	public static void interfaceEnd(Connection conn, String interfaceName) throws Exception {
		String sql = "UPDATE IF_INTERFACE_TIME SET LAST_TIME = CURRENT_TIME WHERE INTERFACE = ? ";
		Object[] wheres = new Object[1];
		wheres[0] = interfaceName;
		try {
			new DataHandlerUtil().updateByPrepareSQL(conn, sql, wheres);
			DBConnection.commit(conn);
		} catch (Exception e) {
			TDSLogger.println(interfaceName + " update interface end time fail\n" + e);
		}
	}

	public static void interfaceEnd(String interfaceName) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			interfaceEnd(conn, interfaceName);
			conn.commit();
		} catch (Exception e) {
			DBConnection.rollback(conn);
		} finally {
			DBConnection.close(conn);
		}
	}
}