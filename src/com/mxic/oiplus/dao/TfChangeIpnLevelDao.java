package com.mxic.oiplus.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.GPRSDB;
import com.mxic.oiplus.util.TDSLogger;

public class TfChangeIpnLevelDao {
	
	public static TfChangeIpnLevelBean[] getList(){
		//TfChangeIpnLevelBean[] list = null;
		String sql = "SELECT * FROM TF_CHANGEIPN_LEVEL ORDER BY PRIORITY";
		//TDSLogger.println(sql);		
		Connection conn = null;
		List<TfChangeIpnLevelBean> list = new ArrayList<TfChangeIpnLevelBean>();
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection();
			rs = GPRSDB.qryRSBySql(conn, sql.toString(), new Object[]{});
			while(rs.next()){				
				TfChangeIpnLevelBean bean = (TfChangeIpnLevelBean) GPRSDB.RStoObjectBean(rs, TfChangeIpnLevelBean.class);
				list.add(bean);
			}
		}catch(Exception e){	
			TDSLogger.println(e);
		}finally{
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					TDSLogger.println(e);
				}
			DBConnection.close(conn);
		}
		return (TfChangeIpnLevelBean[]) list.toArray(new TfChangeIpnLevelBean[list.size()]);
	}	
		
}
