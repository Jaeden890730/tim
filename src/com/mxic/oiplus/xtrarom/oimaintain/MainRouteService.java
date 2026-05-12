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

import java.sql.*;
import java.util.*;

import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;
public class MainRouteService {

	public MainRouteService() {
	}

	//check if any data of the given product body and route_type in tf_main_route_xrom_tx
	public static boolean CheckMainRoute_TX_Exist(String sid,
			String pro_b,
                        String version,
                        String route_type) throws Exception {

		StringBuffer sql = new StringBuffer();
		Connection conn=null;
		try {
			conn=DBConnection.getConnection();
			HashMap whereStem = new HashMap();
			whereStem.put("sid", sid);
                        whereStem.put("version", version);
                        whereStem.put("route_type", route_type);
			sql.append("select * from tf_main_route_xrom_tx ");
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

	//check if any earlier version data of the given product body and route_type in tf_main_route_xrom
	public static boolean CheckMainRoute_Exist(String sid,
			String pro_b,
                        String version,
                        String route_type) throws Exception {

		StringBuffer sql = new StringBuffer();
		Connection conn = null;
                String maxV = String.valueOf(Integer.parseInt(version)-1);
		try {
			conn=DBConnection.getConnection();
			HashMap whereStem = new HashMap();
			whereStem.put("product_body", pro_b);
                        whereStem.put("version", maxV);
                        whereStem.put("route_type", route_type);
			sql.append("select * from tf_main_route_xrom ");
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

	//copy the data from tf_main_route_xrom to tf_main_route_xrom_tx
	public static boolean MainRouteToMainRouteTx(Connection conn,
			String pro_b,
                        String version,
			String sid,
                        String route_type) {

		StringBuffer Sql = new StringBuffer();
                String maxV=String.valueOf(Integer.parseInt(version)-1);
		try {
			Sql.append("Insert into tf_main_route_xrom_tx ");
			Sql.append("(sid, tag, product_body, version, route_type, main_route, map_route, remark) ");
			Sql.append("select " + sid + ",'0',product_body," + version + "," + route_type+ ","  );
			Sql.append("main_route,map_route,remark ");
			Sql.append("FROM tf_main_route_xrom where product_body='" + pro_b + "' ");
                        Sql.append("and version='" + maxV + "' and route_type=" + route_type );
                        TDSLogger.println(Sql.toString());
			PreparedStatement ps = conn.prepareStatement(Sql.toString());
			ps.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			DBConnection.rollback(conn);
		}
		return false;
	}

	//get the data of the given product body and route_type from tf_main_route_xrom_tx
	public static MainRouteSubBean[] SelectAllFromMainRoute(String sid, Connection conn, String pro_b, String version, String route_type) {

		StringBuffer SelSQL = new StringBuffer();
		try {
			ArrayList tmp = new ArrayList();
			HashMap whereStem = new HashMap();
			whereStem.put("sid", sid);
                        whereStem.put("version", version);
                        whereStem.put("route_type", route_type);
			SelSQL.append("SELECT * FROM tf_main_route_xrom_tx ");
			SelSQL.append(SQLStem.getWhereStmt(whereStem));
			SelSQL.append("order by tag ");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				MainRouteSubBean bean = new MainRouteSubBean();
				bean.setProduct_body(pro_b);
                                bean.setVersion(version);
				bean.setRoute_type(Integer.parseInt(route_type));
				bean.setSid(Integer.parseInt(sid));
				bean.setTag(rs.getString("tag"));
				bean.setMain_route(rs.getString("main_route"));
				bean.setMap_route(rs.getString("map_route"));
				bean.setRemark(rs.getString("remark"));
				tmp.add(bean);
			}
			return (MainRouteSubBean[]) tmp.toArray(new MainRouteSubBean[0]);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
        //get the data of the given product body and route_type from tf_main_route_xrom_tx
        public static MainRouteReBean[] SelectAllFromMainRoute_re(String sid, Connection conn, String pro_b, String version, String route_type) {

                StringBuffer SelSQL = new StringBuffer();
                try {
                        ArrayList tmp = new ArrayList();
                        HashMap whereStem = new HashMap();
                        whereStem.put("sid", sid);
                        whereStem.put("version", version);
                        whereStem.put("route_type", route_type);
                        SelSQL.append("SELECT * FROM tf_main_route_xrom_tx ");
                        SelSQL.append(SQLStem.getWhereStmt(whereStem));
                        SelSQL.append("order by tag ");
                        PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                                MainRouteReBean bean = new MainRouteReBean();
                                bean.setProduct_body(pro_b);
                                bean.setVersion(version);
                                bean.setRoute_type(Integer.parseInt(route_type));
                                bean.setSid(Integer.parseInt(sid));
                                bean.setTag(rs.getString("tag"));
                                bean.setMain_route(rs.getString("main_route"));
                                bean.setMap_route(rs.getString("map_route"));
                                bean.setRemark(rs.getString("remark"));
                                tmp.add(bean);
                        }
                        return (MainRouteReBean[]) tmp.toArray(new MainRouteReBean[0]);
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
                conn_exit = DBConnection.getConnection();
                InsertSQL = "Insert into tf_test_parameter_ft_tx " +
                "(sid,tag,pgm_id,product_body,brand,version,test_type,backend_option," +
                "pin_count,package_type,body_size,tester,site,program_name,actual_file) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
                PreparedStatement ps_insert = conn_exit.prepareStatement(InsertSQL);

				while (rs.next()) {
					if (rs.getInt("total_count") == 0) {
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
				}
			}
			return flag;
		} catch (Exception ex) {
			ex.printStackTrace();
			DBConnection.rollback(conn);
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			flag = false;
		} finally {
			DBConnection.close(conn);
            DBConnection.close(conn_exit);
            conn_exit = null;
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



	public static void update_data(String[] sid,
			String[] pd_body,
			String[] version,
			String[] route_type,
			String[] main_route,
			String[] map_route,
			String[] remark,
			String flag,
                        String re_sub) {
		String InsSQL = null;
		String InsSQL1 = null;
		Connection conn = null;

		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
                        InsSQL = "update tf_main_route_xrom_tx set remark=? " +
			"where sid = ? and product_body=? and version=? and route_type=? " +
			"and main_route=? and map_route=?  ";
			PreparedStatement ps2 = conn.prepareStatement(InsSQL);
			if (main_route != null) {
				for (int i = 0; i < main_route.length; i++) {
					ps2.setString(1, StringUtil.Utf8ToBig5(remark[i]));
					ps2.setInt(2, Integer.parseInt(sid[i]));
                                        ps2.setString(3, pd_body[i]);
                                        ps2.setString(4, version[i]);
					ps2.setInt(5, Integer.parseInt(route_type[i]));
                                        ps2.setString(6, main_route[i]);
					ps2.setString(7, map_route[i]);

					ps2.executeUpdate();
				}
			}
			if (flag.equals("submit_cmd")) {
                                if(re_sub.equals("0")){//route_type[0].equals("0")
     				   InsSQL1 = "update tf_information set TF_MAIN_SUB = ? " +
				   "where sid = ? ";
                                }else if (re_sub.equals("1")){//route_type[0].equals("1")
                                  InsSQL1 = "update tf_information set TF_MAIN_REWORK = ? " +
				   "where sid = ? ";
                                }
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
			String route_type, String version,
			String pd_body) {
		Connection conn = null;
		String main_route = "";
		String map_route = "";
		String remark = "";
		String sql = null;

		try {
			String record_id_str[] = record_id.split(",");
			main_route = record_id_str[0];
			map_route = record_id_str[1];

            if (record_id_str.length <= 2)
              remark = "";
            else
              remark = record_id_str[2];

			conn = DBConnection.getConnection();
			sql = "delete from tf_main_route_xrom_tx " +
			"where sid = ? and product_body=? and route_type=? and version=? " +
			"and main_route=? and map_route=? " ;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, sid);
			ps.setString(2, pd_body);
			ps.setString(3, route_type);
			ps.setString(4, version);
			ps.setString(5, main_route);
			ps.setString(6, map_route);
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
			sql = "delete from tf_main_route_xrom_tx " +
			"where sid = ? and route_type = '0' ";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, sid);
			TDSLogger.println(sql.toString());
			TDSLogger.println("reset_tx");
			ps.executeUpdate();

                        InsSQL1 = "update tf_information set TF_MAIN_SUB = ? " +
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
        public static void reset_tx_re(int sid) {

                Connection conn = null;
                String sql = null;
                String InsSQL1 = null;

                try {
                        conn = DBConnection.getConnection();
                        conn.setAutoCommit(false);
                        sql = "delete from tf_main_route_xrom_tx " +
                        "where sid = ? and route_type = '1' ";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setInt(1, sid);
                        TDSLogger.println(sql.toString());
                        TDSLogger.println("reset_tx");
                        ps.executeUpdate();

                        InsSQL1 = "update tf_information set TF_MAIN_REWORK = ? " +
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


	public static String toString(String pd_body, String version, String route_type,
			String main_route, String map_route, String remark) {
		StringBuffer str = new StringBuffer();
		str.append("Product Body = " + pd_body + ",");
		str.append("Version = " + version + ",");
		str.append("Route Type = " + route_type + ",");
		str.append("Main route = " + main_route + ",");
		str.append("Map route = " + map_route + ",");
		str.append("Remark = " + remark + "'");

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
