/*****************************************************************
 *主題:FTTest相關程式
 *作者:Candy
 *日期:2005/10/27
 *****************************************************************/

package com.mxic.oiplus.xtrarom.oimaintain;

//import com.mxic.oiplus.rs.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.SQLStem;
import com.mxic.oiplus.util.TDSLogger;

public class PBCService {

	public PBCService() {
	}

	//check if any data of the given product body and brand in tf_test_parameter_pbc_tx
	public static boolean CheckPBC_TX_Exist(String sid) throws Exception {

		StringBuffer sql = new StringBuffer();
		Connection conn=null;
		try {
			conn=DBConnection.getConnection();
			HashMap whereStem = new HashMap();
			whereStem.put("sid", sid);
			sql.append("select * from tf_test_parameter_pbc_tx ");
			sql.append(SQLStem.getWhereStmt(whereStem));
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ResultSet rs = ps1.executeQuery();
			while(rs.next()){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return false;
	}

    public static boolean CheckPBC_Exist(String sid,
            String pro_b,
            String brand,
            String version) throws Exception {

        StringBuffer sql = new StringBuffer();
        Connection conn = null;
        String maxV = String.valueOf(Integer.parseInt(version)-1);
        try {
            conn=DBConnection.getConnection();
            HashMap whereStem = new HashMap();
            whereStem.put("product_body", pro_b);
            whereStem.put("brand", brand);
            whereStem.put("version",maxV);
            sql.append("select * from tf_test_parameter_pbc ");
            sql.append(SQLStem.getWhereStmt(whereStem));
            PreparedStatement ps1 = conn.prepareStatement(sql.toString());
            ResultSet rs=ps1.executeQuery();
            while(rs.next()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn);
            conn = null;
        }
        return false;
    }

	//copy the data from tf_test_parameter_pbc to tf_test_parameter_pbc_tx
	public static boolean PBCToPBCTx(Connection conn,
			String pro_b,
			String br,
			String sid,
			String version) {

		StringBuffer Sql = new StringBuffer();
		String maxV=String.valueOf(Integer.parseInt(version)-1);
		try {
			Sql.append("Insert into tf_test_parameter_pbc_tx ");
			Sql.append("(sid,tag,pgm_id,product_body,brand,version,test_type,backend_option,");
			Sql.append("pin_count,package_type,body_size,tester,site,program_name,i_grade,c_grade,");
			Sql.append("tf_comment,actual_file,hw_configure) ");
			Sql.append("select " + sid + ",'0',NVL(pgm_id, 0),product_body,brand," + version );
			Sql.append(",test_type,backend_option,pin_count,package_type,body_size,tester,site,");
			Sql.append("program_name,i_grade,c_grade,tf_comment,actual_file,hw_configure ");
			Sql.append("FROM tf_test_parameter_pbc where product_body='" + pro_b );
			Sql.append("' and brand='" + br + "' and version ='" + maxV + "'");

			PreparedStatement ps = conn.prepareStatement(Sql.toString());
			ps.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			DBConnection.rollback(conn);
		}
		return false;
	}

	//get the data of the given product body and brand from tf_test_parameter_ws_tx
	public static PBCTestParameterBean[] SelectAllFromPBC(String sid, Connection conn, String pro_b, String br, String version) {

		StringBuffer SelSQL = new StringBuffer();
		try {
			ArrayList tmp = new ArrayList();
			HashMap whereStem = new HashMap();
			whereStem.put("sid", sid);
			SelSQL.append("SELECT * FROM tf_test_parameter_pbc_tx ");
			SelSQL.append(SQLStem.getWhereStmt(whereStem));
			SelSQL.append("order by tag ");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PBCTestParameterBean bean = new PBCTestParameterBean();
				bean.setProduct_body(pro_b);
				bean.setBrand(br);
				bean.setVersion(version);
				bean.setSid(Integer.parseInt(sid));
				bean.setTag(rs.getString("tag"));
				bean.setPgm_id(rs.getInt("PGM_ID"));
				bean.setBackend_option(rs.getString("backend_option"));
				bean.setTest_type(rs.getString("TEST_TYPE"));
				bean.setPin_count(rs.getInt("PIN_COUNT"));
				bean.setPackage_type(rs.getString("PACKAGE_TYPE"));
				//bean.setDevice_size("尚無資料");
				bean.setBody_size(rs.getString("body_size"));
				bean.setTester(rs.getString("tester"));
				bean.setSite(rs.getString("site"));
				bean.setProgram_name(rs.getString("PROGRAM_NAME"));
				bean.setActual_file(rs.getString("actual_file"));
				bean.setI_grade(rs.getString("i_grade"));
				bean.setC_grade(rs.getString("c_grade"));
				bean.setTf_comment(rs.getString("tf_comment"));
				bean.setHw_configure(rs.getString("hw_configure"));
				tmp.add(bean);
			}
			return (PBCTestParameterBean[]) tmp.toArray(new PBCTestParameterBean[0]);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/********************************************************************
	 *主題:get Add資料
	 *********************************************************************/

	public static boolean insertadd(int sid, String pd_body,
			String brand, String version,
			String[] record_id) {

		int pg_id = 0;
		String test_type = "";
		String be_opt = "";
		int pin_count = 0;
		String pg_type = "";
		String body_size = "";
		String tester = "";
		String site = "";
		String pg_name = "";
		String actual_file = "";
		Connection conn = null;
		Connection conn_exit = null;
		boolean flag = true;
		String InsertSQL = null;
		try {
			for (int i = 0; i < record_id.length; i++) {
				String record_id_str[] = record_id[i].split(",");
				pg_id = Integer.parseInt(record_id_str[0]);
				test_type = record_id_str[1];
				be_opt = record_id_str[2];
				pin_count = Integer.parseInt(record_id_str[3]);
				pg_type = record_id_str[4];
				body_size = record_id_str[5];
				tester = record_id_str[6];
				site = record_id_str[7];
				pg_name = record_id_str[8];
				actual_file = record_id_str[9];
				conn = DBConnection.getConnection();

				StringBuffer sql = new StringBuffer();
				sql.append(
						"SELECT count(*) as total_count FROM tf_test_parameter_ft_tx where sid='" + sid +
						"' and pgm_id='" + pg_id +
						"' and product_body='" + pd_body +
						"' and brand='" + brand +
						"' and version='" + version +
						"' and test_type='" + test_type +
						"' and backend_option='" + be_opt +
						"' and pin_count='" + pin_count +
						"' and package_type='" + pg_type +
						"' and body_size='" + body_size +
						"' and  tester='" + tester +
						"' and site='" + site +
						"' and actual_file ='" + actual_file +
						"' and program_name='" + pg_name + "' ");

				PreparedStatement ps = conn.prepareStatement(sql.toString());
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					conn_exit = DBConnection.getConnection();
					if (rs.getInt("total_count") == 0) {
						InsertSQL = "Insert into tf_test_parameter_ft_tx " +
						"(sid,tag,pgm_id,product_body,brand,version,test_type,backend_option," +
						"pin_count,package_type,body_size,tester,site,program_name,actual_file) " +
						"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
						PreparedStatement ps_insert = conn_exit.prepareStatement(InsertSQL);
						ps_insert.setInt(1, sid);
						ps_insert.setString(2, "1");
						ps_insert.setInt(3, pg_id);
						ps_insert.setString(4, pd_body);
						ps_insert.setString(5, brand);
						ps_insert.setInt(6, Integer.parseInt(version));
						if (test_type == null || test_type.equals("")) {
							ps_insert.setString(7, " ");
						} else {
							ps_insert.setString(7, test_type);
						}
						if (be_opt == null || be_opt.equals("")) {
							ps_insert.setString(8, " ");
						} else {
							ps_insert.setString(8, be_opt);
						}
						if (String.valueOf(pin_count) == null || String.valueOf(pin_count).equals("")) {
							ps_insert.setInt(9, 0);
						} else {
							ps_insert.setInt(9, pin_count);
						}
						if (pg_type == null || pg_type.equals("")) {
							ps_insert.setString(10, " ");
						} else {
							ps_insert.setString(10, pg_type);
						}
						if (body_size == null || body_size.equals("")) {
							ps_insert.setString(11, " ");
						} else {
							ps_insert.setString(11, body_size);
						}
						if (tester == null || tester.equals("")) {
							ps_insert.setString(12, " ");
						} else {
							ps_insert.setString(12, tester);
						}
						if (site == null || site.equals("")) {
							ps_insert.setString(13, " ");
						} else {
							ps_insert.setString(13, site);
						}
						if (pg_name == null || pg_name.equals("")) {
							ps_insert.setString(14, " ");
						} else {
							ps_insert.setString(14, pg_name);
						}
						if (actual_file == null || actual_file.equals("")) {
							ps_insert.setString(15, " ");
						} else {
							ps_insert.setString(15, actual_file);
						}
						TDSLogger.println(InsertSQL.toString());
						ps_insert.executeUpdate();
						//DBConnection.commit(conn_exit);
					} else {
						//已存在相同資料,回到上一頁
					}
					//DBConnection.commit(conn);
					DBConnection.close(conn_exit);
					conn_exit = null;
				}
			}
			DBConnection.close(conn);
			return flag;
		} catch (Exception ex) {
			ex.printStackTrace();
			DBConnection.rollback(conn);
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			flag = false;
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return flag;
	}

	/*****************************************************************
	 *主題:取得PBCAdd之test_mode
	 *****************************************************************/
	public static PBCTestParameterForm[] getvendor(String record_id) {
		Connection conn = null;
		String be_opt = "";
		String test_mode = "";
		int pin_count = 0;
		String pg_type = "";
		String tester = "";
		String pg_name = "";
		String actual_file = "";
		String body_size = "";
		String i_grade = "";
		String c_grade = "";
		String site = "";
		String pgm_id = "";
		String hw_configure = "";

		try {
			String record_id_str[] = record_id.split(",");
			be_opt = record_id_str[0];
			test_mode = record_id_str[1];
			pin_count = Integer.parseInt(record_id_str[2]);
			pg_type = record_id_str[3];
			tester = record_id_str[4];
			pg_name = record_id_str[5];
			site = record_id_str[6];
			body_size = record_id_str[7];
			actual_file = record_id_str[8];
			i_grade = record_id_str[10];
			c_grade = record_id_str[11];
			pgm_id = record_id_str[12];
			hw_configure = record_id_str[13];

			StringBuffer sql = new StringBuffer();

			sql.append("select distinct plant_name from ba_plant ");
			sql.append("where plant_name != '" + site + "' and cycling_flag = 1 order by plant_name");
			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			ArrayList tmp = new ArrayList();
			while (rs.next()) {
				PBCTestParameterForm bean = new PBCTestParameterForm();
				bean.setSite_str(rs.getString("plant_name"));
/*
				TDSLogger.println(rs.getString("plant_name"));
*/
				bean.setPackage_type(pg_type);
				bean.setBackend_option(be_opt);
				bean.setTest_type(test_mode);
				bean.setPin_count(pin_count);
				bean.setTester(tester);
				bean.setProgram_name(pg_name);
				bean.setBody_size(body_size);
				bean.setI_grade(i_grade);
				bean.setC_grade(c_grade);
				bean.setActual_file(actual_file);
				bean.setPgm_id(pgm_id);
				bean.setHw_configure(hw_configure);
				tmp.add(bean);
			}
			if (tmp.isEmpty()) {
				return null;
			} else {
				return (PBCTestParameterForm[]) tmp.toArray(new PBCTestParameterForm[0]);
			}
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			// return null;
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return null;
	}

	public static String addvendor_Info(
			String[] site_data,
			String be_opt,
			String test_mode,
			int pin_count,
			String pg_type,
			String tester,
			String pg_name,
			String actual_file,
			int sid,
			String pd_body,
			String brand,
			String version,
			String body_size,
			String i_grade,
			String c_grade,
			String pgm_id,
			String hw_configure) {

		String InsertSQL = null;
		Connection conn = null;
		String site_data_str = "";
		Vector sites = new Vector();
		StringBuffer message = new StringBuffer();

		for (int j = 0; j < site_data.length; j++) {
			if (j == 0) {
				site_data_str = "site='" + site_data[j] + "'";
			} else {
				site_data_str = site_data_str + " or site='" + site_data[j] + "'";
			}
			sites.add(site_data[j]);
		}

		try {
			conn = DBConnection.getConnection();
			StringBuffer sql_check = new StringBuffer();
			sql_check.append(
					"SELECT DISTINCT site FROM tf_test_parameter_pbc_tx " +
					"where sid='" + sid +
					"' and pgm_id='" + pgm_id +
					"' and product_body='" + pd_body +
					"' and brand='" + brand +
					"' and version='" + version +
					"' and test_type='" + test_mode +
					"' and backend_option='" + be_opt +
					"' and pin_count='" + pin_count +
					"' and package_type='" + pg_type +
					"' and body_size='" + body_size +
					"' and tester='" + tester +
					"' and actual_file='" + actual_file +
					"' and program_name='" + pg_name +
					"' and (" + site_data_str + ")");

			PreparedStatement ps_check = conn.prepareStatement(sql_check.toString());
			ResultSet rs_check = ps_check.executeQuery();
			while (rs_check.next()) {
				sites.remove(rs_check.getString("SITE"));
			}
		} catch (Exception e) {
			message.append("SELECT DATA FAIL!");
			return message.toString();
		}
		String site = null;
		for (int i=0; i<sites.size(); i++) {
			try {
				site = (String)sites.get(i);
				//StringBuffer InsertSQL = new StringBuffer();
				InsertSQL = "Insert into tf_test_parameter_pbc_tx " +
				"(sid,tag,pgm_id,product_body,brand,version,test_type,backend_option," +
				"pin_count,package_type,body_size,tester,site,program_name,i_grade," +
				"c_grade,tf_comment,actual_file,hw_configure) " +
				"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
				PreparedStatement ps_insert = conn.prepareStatement(InsertSQL);
				ps_insert.setInt(1, sid);
				ps_insert.setString(2, "1");
				if (pgm_id == null || pgm_id.equals("")) {
					ps_insert.setInt(3, 0);
				} else {
					ps_insert.setInt(3, Integer.parseInt(pgm_id));
				}
				ps_insert.setString(4, pd_body);
				ps_insert.setString(5, brand);
				ps_insert.setInt(6, Integer.parseInt(version));
				if (test_mode == null || test_mode.equals("")) {
					ps_insert.setString(7, " ");
				} else {
					ps_insert.setString(7, test_mode);
				}
				if (be_opt == null || be_opt.equals("")) {
					ps_insert.setString(8, " ");
				} else {
					ps_insert.setString(8, be_opt);
				}
				if (String.valueOf(pin_count) == null || String.valueOf(pin_count).equals("")) {
					ps_insert.setInt(9, 0);
				} else {
					ps_insert.setInt(9, pin_count);
				}
				if (pg_type == null || pg_type.equals("")) {
					ps_insert.setString(10," ");
				} else {
					ps_insert.setString(10, pg_type);
				}
				if (body_size == null || body_size.equals("")) {
					ps_insert.setString(11, " ");
				} else {
					ps_insert.setString(11, body_size);
				}
				if (tester == null || tester.equals("")) {
					ps_insert.setString(12, " ");
				} else {
					ps_insert.setString(12, tester);
				}
				if (site == null || site.equals("")) {
					ps_insert.setString(13, " ");
				} else {
					ps_insert.setString(13, site);
				}
				if (pg_name == null || pg_name.equals("")) {
					ps_insert.setString(14, " ");
				} else {
					ps_insert.setString(14, pg_name);
				}
				ps_insert.setString(15, i_grade);
				ps_insert.setString(16, c_grade);
				ps_insert.setString(17, " ");
				if (actual_file == null || actual_file.equals("")) {
					ps_insert.setString(18, " ");
				} else {
					ps_insert.setString(18, actual_file);
				}
				if (hw_configure == null || hw_configure.equals("")) {
					ps_insert.setString(19, " ");
				} else {
					ps_insert.setString(19, hw_configure);
				}
				ps_insert.executeUpdate();
			} catch (Exception ex) {
				ex.fillInStackTrace();
				TDSLogger.println(ex.getMessage());
				message.append("Insert Fail: " + toString(pgm_id, pd_body, brand, version, test_mode, be_opt,
								Integer.toString(pin_count), pg_type, body_size, tester, site, pg_name, actual_file, i_grade, c_grade, hw_configure) + "\n");
			}
		}
		DBConnection.close(conn);
		return message.toString();
	}

	public static void update_data(String[] sid,
			String[] pd_body,
			String[] brand,
			String[] version,
			String[] test_type,
			String[] be_opt,
			String[] pin_count,
			String[] pg_type,
			String[] body_size,
			String[] tester,
			String[] site,
			String[] pg_name,
			String[] actual_file,
			String[] pg_id,
			String[] i_grade,
			String[] c_grade,
			String[] comment,
			String flag,
			String[] hw_configure) {
		String InsSQL = null;
		String InsSQL1 = null;
		Connection conn = null;

		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
                        InsSQL = "update tf_test_parameter_pbc_tx set i_grade=?, c_grade=?, tf_comment=?, hw_configure=? " +
			"where sid = ? and product_body=? and brand=? and version=? and test_type=? " +
			"and backend_option=? and pin_count=? and package_type=? and body_size=? " +
			"and tester=? and site=? and pgm_id=? and program_name=? and nvl(actual_file,'NULL')=? ";
			PreparedStatement ps2 = conn.prepareStatement(InsSQL);
			if (pin_count != null) {
				for (int i = 0; i < pin_count.length; i++) {
					ps2.setString(1, c_grade[i]);
					ps2.setString(2, c_grade[i]);
					ps2.setString(3, comment[i].trim());
					ps2.setString(4, hw_configure[i]);
					ps2.setInt(5, Integer.parseInt(sid[i]));
					ps2.setString(6, pd_body[i]);
					ps2.setString(7, brand[i]);
					ps2.setString(8, version[i]);
					ps2.setString(9, test_type[i]);
					ps2.setString(10, be_opt[i]);
					ps2.setString(11, pin_count[i]);
					ps2.setString(12, pg_type[i]);
					ps2.setString(13, body_size[i]);
					ps2.setString(14, tester[i]);
					ps2.setString(15, site[i]);
					ps2.setString(16, pg_id[i]);
					ps2.setString(17, pg_name[i]);
                                        if (actual_file[i].equals(""))
                                          ps2.setString(18, "NULL");
                                        else
                                          ps2.setString(18, actual_file[i]);

					ps2.executeUpdate();
				}
			}
			if (flag.equals("submit_cmd")) {
				InsSQL1 = "update tf_information set TF_TEST_PARAMETER_PBC = ? " +
				"where sid = ? ";
				PreparedStatement ps3 = conn.prepareStatement(InsSQL1);
				ps3.setString(1, "Y");
				ps3.setInt(2, Integer.parseInt(sid[0]));
				ps3.executeUpdate();
			}
			conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
	}

	public static void delete_row(String record_id, int sid,
			String brand, String version,
			String pd_body) {
		Connection conn = null;
		String be_opt = "";
		String test_mode = "";
		int pin_count = 0;
		String pg_type = "";
		String tester = "";
		String pg_name = "";
		String actual_file = "";
		String site = "";
		String body_size = "";
		int pg_id = 0;
		String sql = null;

		try {
			String record_id_str[] = record_id.split(",");
			be_opt = record_id_str[0];
			test_mode = record_id_str[1];
			pin_count = Integer.parseInt(record_id_str[2]);
			pg_type = record_id_str[3];
			tester = record_id_str[4];
			pg_name = record_id_str[5];
			site = record_id_str[6];
			body_size = record_id_str[7];
			actual_file = record_id_str[8];
			pg_id = Integer.parseInt(record_id_str[9]);
			conn = DBConnection.getConnection();
			sql = "delete from tf_test_parameter_pbc_tx " +
			"where sid = ? and product_body=? and brand=? and version=? " +
			"and test_type=? and backend_option=? and pin_count=? " +
			"and package_type=? and body_size=? and tester=? and site=? " +
			"and pgm_id=? and program_name=? and actual_file=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, sid);
			ps.setString(2, pd_body);
			ps.setString(3, brand);
			ps.setString(4, version);
			ps.setString(5, test_mode);
			ps.setString(6, be_opt);
			ps.setInt(7, pin_count);
			ps.setString(8, pg_type);
			ps.setString(9, body_size);
			ps.setString(10, tester);
			ps.setString(11, site);
			ps.setInt(12, pg_id);
			ps.setString(13, pg_name);
			ps.setString(14, actual_file);
			TDSLogger.println(sql.toString());
			TDSLogger.println("delete_row");
			ps.executeUpdate();
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			DBConnection.rollback(conn);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
	}

	public static void reset_tx(int sid) {

		Connection conn = null;
		String sql = null;
		String InsSQL1 = null;

		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			sql = "delete from tf_test_parameter_pbc_tx " +
			"where sid = ? ";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, sid);
			//TDSLogger.println(ps.toString());
			TDSLogger.println("reset_tx");
			ps.executeUpdate();

			InsSQL1 = "update tf_information set TF_TEST_PARAMETER_PBC = ? " +
			"where sid = ? ";

			PreparedStatement ps3 = conn.prepareStatement(InsSQL1);
			ps3.setString(1, "N");
			ps3.setInt(2, sid);
			ps3.executeUpdate();
			conn.commit();
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			DBConnection.rollback(conn);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
	}

	public static String toString(String pgm_id, String pd_body, String brand, String version, String test_mode,
			String be_opt, String pin_count, String pg_type, String body_size, String tester, String site,
			String pg_name, String actual_file, String i_grade, String c_grade, String hw_configure) {
		StringBuffer str = new StringBuffer();
		str.append("PGM ID = " + pgm_id + ",");
		str.append("Product Body = " + pd_body + ",");
		str.append("Brand = " + brand + ",");
		str.append("Version = " + version + ",");
		str.append("Test Type = " + test_mode + ",");
		str.append("Backend Option = " + be_opt + ",");
		str.append("Pin Count = " + pin_count + ",");
		str.append("Package Type = " + pg_type + ",");
		str.append("Body Size = " + body_size + ",");
		str.append("tester = " + tester + ",");
		str.append("site = " + site + ",");
		str.append("Program Name = " + pg_name + ",");
		str.append("Actual Name = " + actual_file + ",");
		str.append("I Grade = '" + i_grade + "',");
		str.append("C Grade = '" + c_grade + "'");
		str.append("HW Configure = '" + hw_configure + "'");

		return str.toString();
	}

    public static String[] getTesterType() {
        Connection conn = null;

        try {
            StringBuffer sql = new StringBuffer();

            sql.append("SELECT DISTINCT DESCRIP FROM BA_DESCRIPTION_LIST ");
            sql.append("WHERE TAG = 403");
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            ArrayList tmp = new ArrayList();
            while (rs.next()) {
                String testerType = rs.getString("DESCRIP");
                tmp.add(testerType);
            }
            if (tmp.isEmpty()) {
                return null;
            } else {
                return (String[]) tmp.toArray(new String[0]);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            // return null;
        } finally {
            DBConnection.close(conn);
            conn = null;
        }
        return null;
    }

	public static String[] getProductData(String productBody, String brand, int fieldNo) {
		String[] fieldName = {"BACKEND_OPTION", "PIN_COUNT", "PACKAGE_TYPE", "BODY_SIZE"};
		Connection conn = null;

		try {
			StringBuffer sql = new StringBuffer();

			if (fieldNo != 2) {
				sql.append("SELECT DISTINCT " + fieldName[fieldNo] + " FROM TF_PROD_EPN ");
				sql.append("WHERE PRODUCT_BODY = '" + productBody + "' AND BRAND = '" + brand + "'");
			} else if (fieldNo == 2) {
				sql.append("SELECT DISTINCT B.PACKAGE_TYPE FROM BA_PACKAGE_TYPE B, TF_PROD_EPN T ");
				sql.append("WHERE T.PRODUCT_BODY = '" + productBody + "' AND T.BRAND = '" + brand + "' ");
				sql.append("AND B.PRM2_CODE = T.PACKAGE_TYPE AND T.PACKAGE_TYPE != 'W'");
			} else return null;

			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			ArrayList tmp = new ArrayList();
			while (rs.next()) {
				String testerType = rs.getString(fieldName[fieldNo]);
				tmp.add(testerType);
			}
			if (tmp.isEmpty()) {
				return null;
			} else {
				return (String[]) tmp.toArray(new String[0]);
			}
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			// return null;
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return null;
	}

	public static String[] getVendor() {
		Connection conn = null;
		try {
			StringBuffer sql = new StringBuffer();

			sql.append("select distinct plant_name from ba_plant ");
			sql.append("where cycling_flag = 1 order by plant_name");
			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			ArrayList tmp = new ArrayList();
			while (rs.next()) {
				String plant = rs.getString("plant_name");
				tmp.add(plant);
			}
			if (tmp.isEmpty()) {
				return null;
			} else {
				return (String[]) tmp.toArray(new String[0]);
			}
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			// return null;
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return null;
	}

}
