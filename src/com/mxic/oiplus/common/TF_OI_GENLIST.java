package com.mxic.oiplus.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.DateUtil;
import com.mxic.oiplus.util.TDSLogger;

public class TF_OI_GENLIST {
	public Timestamp start_time = null;
	public Timestamp end_time = null;
	Connection conn = null;


	public TF_OI_GENLIST() {
		setStart_time();
	}

	public int insertData(String user_id, String sid, String product_code, String version) {
		int succ = -1;
		try {
			conn = DBConnection.getConnection();
			end_time = new Timestamp(System.currentTimeMillis());
			String sql = "insert into tim.TF_OI_GENLIST(user_id, sid, product_body, version, start_time, end_time, cost) values (?,?,?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user_id);
			ps.setString(2, sid);
			ps.setString(3, product_code);
			ps.setString(4, version);
			ps.setTimestamp(5, this.getStart_time());
			ps.setTimestamp(6, end_time);
			ps.setLong(7, DateUtil.getDiffSecond(end_time, start_time));
			succ = ps.executeUpdate();
			ps.close();

		} catch (Exception ex) {
			DBConnection.close(conn);
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
		} finally {
			try {
			} catch (Exception e) {
			}
			DBConnection.close(conn);
			return succ;
		}
	}

	public Timestamp getStart_time() {
		return start_time;
	}

	public void setStart_time() {
		this.start_time = new Timestamp(System.currentTimeMillis());
	}

}
