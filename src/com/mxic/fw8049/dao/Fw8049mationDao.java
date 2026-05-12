package com.mxic.fw8049.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mxic.fw8049.action.BaseActionForm;
import com.mxic.fw8049.action.Fw8049MainActionForm;
import com.mxic.fw8049.bean.CompareResultBean;
import com.mxic.fw8049.bean.Fw8049maintainenceBean;
import com.mxic.fw8049.bean.Fw8049mationBean;
import com.mxic.fw8049.bean.FwBasicInfoTxBean;
import com.mxic.oi8040.resource.DBConnection;
import com.mxic.oi8040.util.DBUtil;
import com.mxic.oi8040.util.TDSLogger;
import com.mxic.oiplus.xtrarom.oimaintain.AurthMaintainActionForm;
import com.mxic.tdsplus.util.DataHandlerUtil;

public class Fw8049mationDao {

	public static boolean isEditUser(String sid, String loginUserName) {
		StringBuffer sql = new StringBuffer();
		sql.append("select decode('").append(loginUserName)
				.append("', a.creator, 'Y', a.sponsor_1, 'Y', a.sponsor_2, 'Y', 'N') flag\n")
				.append("from tg_information a\n").append("where a.sid = ?");

		Connection con = null;
		ArrayList al = new ArrayList();
		ResultSet rs = null;
		try {
			con = DBConnection.getConnection();
			HashMap[] hm = DBUtil.qryHashMapBySql(con, sql.toString(), new Object[] { sid });
			if (hm.length > 0) {
				if (hm[0].get("FLAG").toString().equals("Y"))
					return true;
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					TDSLogger.println(e);
				}
			DBConnection.close(con);
		}
		return false;
	}

	public static ArrayList getFwBasicListTx(String sid) {
		StringBuffer sql = new StringBuffer();

		sql.append("select tag, product_code, form_no, reason_details, annotation_issues from fw_basic_info f \n")
				.append("where SID = ? ");

		Connection con = null;
		ResultSet rs = null;
		ArrayList al = new ArrayList();
		try {
			con = DBConnection.getConnection();
			rs = DBUtil.qryRSBySql(con, sql.toString(), new Object[] { sid });
			while (rs.next()) {
				al.add((Fw8049maintainenceBean) DBUtil.RStoObjectBean(rs, Fw8049maintainenceBean.class));
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					TDSLogger.println(e);
				}
			DBConnection.close(con);
		}
		return al;
	}

	public static List<Fw8049MainActionForm> getInfo(String sid) {
	    Connection conn = null;
	    boolean flag = true;

	    try {
	      StringBuffer sql = new StringBuffer();
	      sql.append("SELECT * FROM tf_fw_information where sid='" + sid + "'");

	      conn = DBConnection.getConnection();
	      PreparedStatement ps = conn.prepareStatement(sql.toString());
	      ResultSet rs = ps.executeQuery();
	      List<Fw8049MainActionForm> tmp = new ArrayList();
	      while (rs.next()) {
			Fw8049MainActionForm bean = new Fw8049MainActionForm();
	        bean.setProduct_body(rs.getString("product_body"));            
	        bean.setBrand(rs.getString("brand"));
	        bean.setVersion(rs.getString("version"));
	        bean.setCreator(rs.getString("creator"));
	        bean.setSponsor_1(rs.getString("sponsor_1"));
	        bean.setSponsor_2(rs.getString("sponsor_2"));
	        tmp.add(bean);
	      }

	      return tmp;
	    } catch (Exception ex) {
	      ex.fillInStackTrace();
	      TDSLogger.println(ex.getMessage());
	      flag = false;
	      return null;
	    } finally {
	      DBConnection.close(conn);
	    }
	  }

	public static List<CompareResultBean> getCompareList(String productBody) {

		List<CompareResultBean> resultList = new ArrayList<CompareResultBean>();

		StringBuffer sql = new StringBuffer();
		sql.append(
				"SELECT a.NCL_FORM_NO, a.PROD_BODY, a.BE_OPTION, b.FORMNO, b.reason, b.content, b.pending FROM fwrs_ncl_fw_app a LEFT JOIN fw_basic_info b  \n")
				.append(" ON a.NCL_FORM_NO = b.FORMNO \n").append("WHERE  a.STATUS = ? AND a.PROD_BODY = ?");

		Connection con = null;
		ResultSet rs = null;
		List<CompareResultBean> al = new ArrayList();
		try {
			con = DBConnection.getConnection();
			rs = DBUtil.qryRSBySql(con, sql.toString(), new Object[] { "送EPC會簽中", productBody });
			while (rs.next()) {
				al.add((CompareResultBean) DBUtil.RStoObjectBean(rs, CompareResultBean.class));
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					TDSLogger.println(e);
				}
			DBConnection.close(con);
		}
		return al;
	}

	public static Fw8049mationBean query(String sid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tf_fw_information where sid = ? ");

		Connection con = null;
		ResultSet rs = null;
		try {
			con = DBConnection.getConnection();
			rs = DBUtil.qryRSBySql(con, sql.toString(), new Object[] { sid });
			if (rs.next()) {
				return (Fw8049mationBean) DBUtil.RStoObjectBean(rs, Fw8049mationBean.class);
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					TDSLogger.println(e);
				}
			DBConnection.close(con);
		}
		return null;
	}

	public static List<Fw8049MainActionForm> getAllList() {

		Connection conn = null;

		try {
			conn = DBConnection.getConnection();
			DataHandlerUtil util = new DataHandlerUtil();
			String sql = "select SID, PRODUCT_BODY, BRAND, VERSION, STATUS, CREATOR, CREATETIME, UPDATETIME, decode(STATUS, 'A', '會簽中', 'P', '處理中', 'R', '已生效', 'F', '已失效', STATUS) STATUS_DISPLAY from tf_fw_information where activeflag != 'Y'";
			TDSLogger.println(sql);
			HashMap[] data = util.getDataBySql(conn, sql);
			List<Fw8049MainActionForm> beans = new ArrayList<>();

			if (data != null) {
				for (HashMap<String, Object> map : data) {
					Fw8049MainActionForm bean = new Fw8049MainActionForm();
					bean.setSid((String) map.get("SID"));
					bean.setProduct_body((String) map.get("PRODUCT_BODY"));
					bean.setBrand((String) map.get("BRAND"));
					bean.setVersion((String) map.get("VERSION"));
					bean.setStatus((String) map.get("STATUS"));
					bean.setStatus_display((String) map.get("STATUS_DISPLAY"));
					bean.setCreator((String) map.get("CREATOR"));
					String c = ((String) map.get("CREATETIME"));
					String u = ((String) map.get("UPDATETIME"));
					if (u != null) {
						bean.setShowTime(u);
					} else {
						bean.setShowTime(c);
					}
					beans.add(bean);
				}
			}

			return beans;
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return null;
		} finally {
			DBConnection.close(conn);
		}

	}

	public static List<Fw8049MainActionForm> getList(String status) {

		Connection conn = null;

		try {
			conn = DBConnection.getConnection();
			DataHandlerUtil util = new DataHandlerUtil();
			String sql = "select SID, PRODUCT_BODY, BRAND, VERSION, STATUS, decode(STATUS, 'A', '會簽中', 'P', '處理中', 'R', '已生效', 'F', '已失效', STATUS) STATUS_DISPLAY from tf_fw_information ";
			sql += "where STATUS = '" + status + "' and activeflag != 'Y'";
			TDSLogger.println(sql);
			HashMap[] data = util.getDataBySql(conn, sql);
			List<Fw8049MainActionForm> beans = new ArrayList<>();

			if (data != null) {
				for (HashMap<String, Object> map : data) {
					Fw8049MainActionForm bean = new Fw8049MainActionForm();
					bean.setSid((String) map.get("SID"));
					bean.setProduct_body((String) map.get("PRODUCT_BODY"));
					bean.setBrand((String) map.get("BRAND"));
					bean.setVersion((String) map.get("VERSION"));
					bean.setStatus((String) map.get("STATUS"));
					bean.setStatus((String) map.get("STATUS_DISPLAY"));
					beans.add(bean);
				}
			}

			return beans;
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return null;
		} finally {
			DBConnection.close(conn);
		}

	}

	public static List<Fw8049MainActionForm> getListByOiSearch(String userName, String productBody) {
		Connection conn = null;

		try {
			conn = DBConnection.getConnection();
			DataHandlerUtil util = new DataHandlerUtil();
			String sql = "select SID, PRODUCT_BODY, BRAND, VERSION, STATUS, CREATOR, CREATETIME, UPDATETIME, decode(STATUS, 'A', '會簽中', 'P', '處理中', 'R', '已生效', 'F', '已失效', STATUS) STATUS_DISPLAY from tf_fw_information ";
			sql += "where PRODUCT_BODY = '" + productBody + "'";
			TDSLogger.println(sql);
			HashMap[] data = util.getDataBySql(conn, sql);
			List<Fw8049MainActionForm> beans = new ArrayList<>();

			if (data != null) {
				for (HashMap<String, Object> map : data) {
					Fw8049MainActionForm bean = new Fw8049MainActionForm();
					bean.setSid((String) map.get("SID"));
					bean.setProduct_body((String) map.get("PRODUCT_BODY"));
					bean.setBrand((String) map.get("BRAND"));
					bean.setVersion((String) map.get("VERSION"));
					bean.setStatus((String) map.get("STATUS"));
					bean.setStatus_display((String) map.get("STATUS_DISPLAY"));
					bean.setCreator((String) map.get("CREATOR"));
					String c = ((String) map.get("CREATETIME"));
					String u = ((String) map.get("UPDATETIME"));
					if (u != null) {
						bean.setShowTime(u);
					} else {
						bean.setShowTime(c);
					}
					beans.add(bean);
				}
			}

			return beans;
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return null;
		} finally {
			DBConnection.close(conn);
		}
	}

	public static List<Fw8049MainActionForm> selectList(String sid, String productBody, String brand) {
		Connection conn = null;

		try {
			conn = DBConnection.getConnection();
			DataHandlerUtil util = new DataHandlerUtil();
			String sql = "select SID, PRODUCT_BODY, BRAND, (CAST(version AS NUMBER) + 1) AS version, STATUS, decode(STATUS, 'A', '會簽中', 'P', '處理中', 'R', '已生效', 'F', '已失效', STATUS) STATUS_DISPLAY from tf_fw_information ";
			sql += "where PRODUCT_BODY = '" + productBody + "' and BRAND = '" + brand + "' and ACTIVEFLAG != 'Y'";
			TDSLogger.println(sql);
			HashMap[] data = util.getDataBySql(conn, sql);
			List<Fw8049MainActionForm> beans = new ArrayList<>();

			if (data != null) {
				for (HashMap<String, Object> map : data) {
					Fw8049MainActionForm bean = new Fw8049MainActionForm();
					bean.setSid((String) map.get("SID"));
					bean.setProduct_body((String) map.get("PRODUCT_BODY"));
					bean.setBrand((String) map.get("BRAND"));
					bean.setVersion((String) map.get("VERSION"));
					bean.setStatus((String) map.get("STATUS"));
					bean.setStatus((String) map.get("STATUS_DISPLAY"));
					beans.add(bean);
				}
			}

			return beans;
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return null;
		} finally {
			DBConnection.close(conn);
		}
	}

	public static boolean insert(Connection conn, BaseActionForm fm) throws Exception {
		boolean flag = true;
		try {

			String sql = "insert into tf_fw_information (sid,product_body,brand,version,status,creator,activeflag) values (tf_fw_information_seq.nextval,?,?,?,?,?,?)";
			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, fm.getProduct_body());
			ps.setString(2, fm.getBrand());
			ps.setString(3, fm.getVersion());
			ps.setString(4, "P");
			ps.setString(5, fm.getCreator());
			ps.setString(6, "N");

			ps.execute();
			ps.clearParameters();
			ps.close();

			flag = true;

		} catch (Exception ex) {
			ex.fillInStackTrace();
			DBConnection.rollback(conn);
			TDSLogger.println(ex.getMessage());
			flag = false;
		} finally {
			DBConnection.close(conn);
		}
		return flag;
	}

	public static boolean updateFlag(Connection conn, BaseActionForm fm) throws Exception {
		boolean flag = true;
		try {
 
			String sql = "update tf_fw_information set ACTIVEFLAG = 'Y' where product_body = ? ";
			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, fm.getProduct_body());

			ps.execute();
			ps.clearParameters();
			ps.close();

			flag = true;

		} catch (Exception ex) {
			ex.fillInStackTrace();
			DBConnection.rollback(conn);
			TDSLogger.println(ex.getMessage());
			flag = false;
		} finally {
			DBConnection.close(conn);
		}
		return flag;
	}
	
	public static boolean updateData(Connection conn, BaseActionForm fm) throws Exception {
		boolean flag = true;
		try {
 
			String sql = "update tf_fw_information set sponsor_1=?, sponsor_2=? where sid = ?";
			conn = DBConnection.getConnection();

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, fm.getSponsor_1());
			ps.setString(2, fm.getSponsor_2());
			ps.setString(3, fm.getSid());

			ps.executeUpdate();
			ps.clearParameters();
			ps.close();

			flag = true;

		} catch (Exception ex) {
			ex.fillInStackTrace();
			DBConnection.rollback(conn);
			TDSLogger.println(ex.getMessage());
			flag = false;
		} finally {
			DBConnection.close(conn);
		}
		return flag;
	}

	public static void delete(Connection con, String sid) throws Exception {
		HashMap wheres = new HashMap();
		wheres.put("SID", sid);
		DBUtil.delete(con, "FW_BASIC_INFO", wheres);
	}

	public int mergeFwBasicInfoTx(Connection conn, FwBasicInfoTxBean bean, String userName) throws Exception {

		DataHandlerUtil util = new DataHandlerUtil();
		StringBuffer sql = new StringBuffer();

		sql.append("MERGE INTO FW_BASIC_INFO_TX T ");
		sql.append("USING ( ");
		sql.append("    SELECT ? AS PRODUCTCODE, ");
		sql.append("           ? AS FORMNO, ");
		sql.append("           ? AS REASON, ");
		sql.append("           ? AS CONTENT, ");
		sql.append("           ? AS PENDING, ");
		sql.append("           ? AS USER_ID ");
		sql.append("    FROM DUAL ");
		sql.append(") S ");
		sql.append("ON ( ");
		sql.append("    T.PRODUCTCODE = S.PRODUCTCODE ");
		sql.append("    AND T.FORMNO = S.FORMNO ");
		sql.append(") ");
		sql.append("WHEN MATCHED THEN ");
		sql.append("    UPDATE SET ");
		sql.append("        T.REASON = S.REASON, ");
		sql.append("        T.CONTENT = S.CONTENT, ");
		sql.append("        T.PENDING = S.PENDING, ");
		sql.append("        T.UPDATER = S.USER_ID, ");
		sql.append("        T.UPDATETIME = SYSDATE ");
		sql.append("WHEN NOT MATCHED THEN ");
		sql.append("    INSERT ( ");
		sql.append("        PRODUCTCODE, ");
		sql.append("        FORMNO, ");
		sql.append("        REASON, ");
		sql.append("        CONTENT, ");
		sql.append("        PENDING, ");
		sql.append("        CREATOR, ");
		sql.append("        CREATETIME ");
		sql.append("    ) VALUES ( ");
		sql.append("        S.PRODUCTCODE, ");
		sql.append("        S.FORMNO, ");
		sql.append("        S.REASON, ");
		sql.append("        S.CONTENT, ");
		sql.append("        S.PENDING, ");
		sql.append("        S.USER_ID, ");
		sql.append("        SYSDATE ");
		sql.append("    )");

		Object[] objs = new Object[] { bean.getProductCode(), bean.getFormNo(), bean.getReason(), bean.getContent(),
				bean.getPending(), userName };

		return util.updateByPrepareSQL(conn, sql.toString(), objs);
	}

	public int mergeFwBasicInfo(Connection conn, FwBasicInfoTxBean bean, String userName) throws Exception {

		DataHandlerUtil util = new DataHandlerUtil();
		StringBuffer sql = new StringBuffer();

		sql.append("MERGE INTO FW_BASIC_INFO T ");
		sql.append("USING ( ");
		sql.append("    SELECT ? AS PRODUCTCODE, ");
		sql.append("           ? AS FORMNO, ");
		sql.append("           ? AS REASON, ");
		sql.append("           ? AS CONTENT, ");
		sql.append("           ? AS PENDING, ");
		sql.append("           ? AS USER_ID ");
		sql.append("    FROM DUAL ");
		sql.append(") S ");
		sql.append("ON ( ");
		sql.append("    T.PRODUCTCODE = S.PRODUCTCODE ");
		sql.append("    AND T.FORMNO = S.FORMNO ");
		sql.append(") ");
		sql.append("WHEN MATCHED THEN ");
		sql.append("    UPDATE SET ");
		sql.append("        T.REASON = S.REASON, ");
		sql.append("        T.CONTENT = S.CONTENT, ");
		sql.append("        T.PENDING = S.PENDING, ");
		sql.append("        T.UPDATER = S.USER_ID, ");
		sql.append("        T.UPDATETIME = SYSDATE ");
		sql.append("WHEN NOT MATCHED THEN ");
		sql.append("    INSERT ( ");
		sql.append("        SID, ");
		sql.append("        PRODUCTCODE, ");
		sql.append("        FORMNO, ");
		sql.append("        REASON, ");
		sql.append("        CONTENT, ");
		sql.append("        PENDING, ");
		sql.append("        CREATOR, ");
		sql.append("        CREATETIME ");
		sql.append("    ) VALUES ( ");
		sql.append("        FW_BASIC_INFO_SEQ.NEXTVAL, ");
		sql.append("        S.PRODUCTCODE, ");
		sql.append("        S.FORMNO, ");
		sql.append("        S.REASON, ");
		sql.append("        S.CONTENT, ");
		sql.append("        S.PENDING, ");
		sql.append("        S.USER_ID, ");
		sql.append("        SYSDATE ");
		sql.append("    )");

		Object[] objs = new Object[] { bean.getProductCode(), bean.getFormNo(), bean.getReason(), bean.getContent(),
				bean.getPending(), userName };

		return util.updateByPrepareSQL(conn, sql.toString(), objs);
	}

	public Map<String, FwBasicInfoTxBean> getFwBasicInfoTxByProductCode(Connection conn, String productCode)
			throws Exception {

		DataHandlerUtil util = new DataHandlerUtil();
		Map<String, FwBasicInfoTxBean> dataMap = new HashMap<String, FwBasicInfoTxBean>();

		String sql = "select tx.formno, tx.reason, tx.content, tx.pending from fw_basic_info_tx tx where productcode = ?";

		Object[] objs = new Object[] { productCode };

		HashMap[] rows = util.getDataBySql(conn, sql, objs);

		if (rows == null) {
			return dataMap;
		}

		for (int i = 0; i < rows.length; i++) {
			HashMap row = rows[i];
			String formNo = getString(row, "FORMNO");

			FwBasicInfoTxBean fw = new FwBasicInfoTxBean();

			fw.setFormNo(formNo);
			fw.setReason(getString(row, "REASON"));
			fw.setContent(getString(row, "CONTENT"));
			fw.setPending(getString(row, "PENDING"));

			dataMap.put(formNo, fw);
		}

		return dataMap;

	}

	private String getString(HashMap row, String key) {
		Object value = row.get(key);
		return value == null ? "" : value.toString();
	}
}
