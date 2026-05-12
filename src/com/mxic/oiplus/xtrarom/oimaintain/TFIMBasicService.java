package com.mxic.oiplus.xtrarom.oimaintain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.TDSLogger;

public class TFIMBasicService {
  public TFIMBasicService() {
  }

  /*****************************************************************
   *主題:取得sid之基本資料
   *****************************************************************/
  public static TFIMBasicActionForm[] getInfo(int sid) {
    Connection conn = null;
    try {
      StringBuffer sql = new StringBuffer();
      sql.append(
          "SELECT product_body,brand,version FROM tf_information where sid='" + sid + "'");
      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      while (rs.next()) {
        TFIMBasicActionForm bean = new TFIMBasicActionForm();
        bean.setPd_body(rs.getString("product_body"));
        bean.setBrand(rs.getString("brand"));
        bean.setVersion(rs.getString("version"));
        tmp.add(bean);
      }

      return (TFIMBasicActionForm[]) tmp.toArray(new TFIMBasicActionForm[0]);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    } finally {
      DBConnection.close(conn);
    }
  }

  /*****************************************************************
   *主題:get第一版資料
   *****************************************************************/
  public static TFIMBasicActionForm[] getInitialInfo(int sid) {
    Connection conn = null;
    String product = "";
    String product_body = "";
    String brand = "";
    int version = 0;
    boolean check_tx_boolean = true;
    boolean check_first_boolean = true;

    try {
      StringBuffer sql = new StringBuffer();
      product = getInfo1(sid);
      String product1[] = product.split(",");
      product_body = product1[0];
      brand = product1[1];
      version = Integer.parseInt(product1[2]);
      check_tx_boolean = check_tx(sid);
      if (check_tx_boolean) {
        //_tx中無資料
        check_first_boolean = check_first(product_body, brand, version - 1);
        if (!check_first_boolean) {
          //為第二版資料,取得sid version-1的資料,讀出資料存入tf_test_parameter_ft_tx,tag=0
          int version_two = version - 1;
          sql.append("INSERT INTO TF_BASIC_INFO_tx " +
                     "(sid,tag,product_body,brand,version,tester,good_bin,fail_bin,remark,auto_ship_yield,stop_test_yield,auto_scrap_yield,mrb_yield,sample_yield) ");
          sql.append("SELECT " + sid + ",0,product_body,brand," + version +
                     ",tester,good_bin,fail_bin,remark,auto_ship_yield,stop_test_yield,auto_scrap_yield,mrb_yield,sample_yield ");
          sql.append("FROM TF_BASIC_INFO WHERE product_body='" + product_body +
                     "' and brand='" + brand + "' and version ='" + version_two + "'");
          conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
          DBConnection.close(conn);
        }
      }
      sql = new StringBuffer();
      sql.append(
          "SELECT * FROM TF_BASIC_INFO_tx a where " +
          "product_body='" +product_body +
          "' and brand='" + brand +
          "' and version ='" + version + "'");
      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      while (rs.next()) {
        TFIMBasicActionForm bean = new TFIMBasicActionForm();
        bean.setSid(sid);
        bean.setTag(rs.getString("tag"));
        bean.setPd_body(product_body);
        bean.setBrand(brand);
        bean.setTester(rs.getString("tester"));
        bean.setFail_bin(rs.getString("fail_bin"));
        bean.setGood_bin(rs.getString("good_bin"));
        bean.setRemark(rs.getString("remark"));
        bean.setAuto_ship_yield(rs.getString("auto_ship_yield"));
        bean.setStop_test_yield(rs.getString("stop_test_yield"));
        bean.setAuto_scrap_yield(rs.getString("auto_scrap_yield"));
        bean.setMrb_yield(rs.getString("mrb_yield"));
        bean.setSample_yield(rs.getString("sample_yield"));
        tmp.add(bean);
      }
      return (TFIMBasicActionForm[]) tmp.toArray(new TFIMBasicActionForm[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    } finally {
      // DBConnection.commit(conn1);
      DBConnection.close(conn);
    }
  }

  /*****************************************************************
   *主題:取出product_body,brand,version
   *****************************************************************/
  public static String getInfo1(int sid) {
    Connection conn = null;
    String product = "";
    try {
      String sql = "SELECT product_body,brand,version FROM tf_information where sid='" + sid + "'";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        product = rs.getString("product_body") + "," +
            rs.getString("brand") + "," +
            rs.getString("version");
      }
      return product;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    } finally {
      DBConnection.close(conn);
    }
  }

  /*****************************************************************
   *主題:TF_BASIC_INFO_TX中是否已經有值
   *****************************************************************/
  public static boolean check_tx(int sid) {
    Connection conn_check_first = null;
    boolean flag = true;
    try {
      String sql_check_first =
          "SELECT count(*) as total_count FROM TF_BASIC_INFO_tx where sid='" +
          sid + "'";
      conn_check_first = DBConnection.getConnection();
      PreparedStatement ps_check_first =
          conn_check_first.prepareStatement(sql_check_first.toString());
      ResultSet rs_leave_day = ps_check_first.executeQuery();
      while (rs_leave_day.next()) {
        if (rs_leave_day.getString("total_count").equals("0") == true) {
          //TF_BASIC_INFO_tx中已有資料,直接存取TF_BASIC_INFO_tx中的資料即可
          flag = true;
        } else {
          flag = false;
        }
      }
      return flag;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn_check_first);
    }
    return flag;
  }

  /*****************************************************************
   *主題:此version已減過一了~
   *****************************************************************/
  public static boolean check_first(String product_body,
                                    String brand,
                                    int version) {
    Connection conn_check_first = null;
    boolean flag = true;
    try {
      String sql_check_first =
          "SELECT count(*) as total_count FROM TF_BASIC_INFO where " +
          "product_body='" + product_body +
          "' and brand='" + brand +
          "' and version='" + version + "'";
      conn_check_first = DBConnection.getConnection();
      PreparedStatement ps_check_first =
          conn_check_first.prepareStatement(sql_check_first.toString());
      ResultSet rs_leave_day = ps_check_first.executeQuery();

      while (rs_leave_day.next()) {
        if (rs_leave_day.getString("total_count").equals("0") == true) {
          //TF_BASIC_INFO中有資料表示不為第一版
          flag = true;
        } else {
          flag = false;
        }
      }
      return flag;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn_check_first);
    }
    return flag;
  }

  /********************************************************************
   *主題:get Add資料
   *********************************************************************/
  public static boolean insertadd(int sid,
                                  String pd_body,
                                  String brand,
                                  String version,
                                  String tester,
                                  String remark,
                                  String good_bin,
                                  String fail_bin,
                                  String auto_ship_yield,
                                  String stop_test_yield,
                                  String auto_scrap_yield,
                                  String mrb_yield,
                                  String sample_yield) {
    Connection conn = null;
    boolean flag = true;
    String InsertSQL1 = null;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      StringBuffer sql = new StringBuffer();
      sql.append(
          "SELECT count(*) as total_count FROM TF_BASIC_INFO_tx where sid='" + sid +
          "' and product_body='" + pd_body +
          "' and brand='" + brand +
          "' and version='" + version +
          "' and tester='" + tester + "'");
/*
          "' and good_bin='" + good_bin +
          "' and fail_bin='" + fail_bin +
          "' and remark='" + remark +
          "' and auto_ship_yield='" + auto_ship_yield +
          "' and stop_test_yield='" + stop_test_yield +
          "' and auto_scrap_yield='" + auto_scrap_yield +
          "' and mrb_yield='" + mrb_yield +
          "' and sample_yield='" + sample_yield + "'"
          );
*/
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      InsertSQL1 = "Insert into TF_BASIC_INFO_tx " +
          "(sid,tag,product_body,brand,version,tester,good_bin,fail_bin,remark," +
          "auto_ship_yield,stop_test_yield,auto_scrap_yield,mrb_yield,sample_yield) " +
          "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
      PreparedStatement ps_insert = conn.prepareStatement(InsertSQL1);

      while (rs.next()) {
        TDSLogger.println(rs.getInt("total_count"));
        TDSLogger.println("TB");
        if (rs.getInt("total_count") == 0) {
          ps_insert.setInt(1, sid);
          ps_insert.setString(2, "1");
          ps_insert.setString(3, pd_body);
          ps_insert.setString(4, brand);
          ps_insert.setInt(5, Integer.parseInt(version));
          ps_insert.setString(6, tester);
          ps_insert.setString(7, good_bin);
          ps_insert.setString(8, fail_bin);
          ps_insert.setString(9, remark);
          ps_insert.setString(10, auto_ship_yield);
          ps_insert.setString(11, stop_test_yield);
          ps_insert.setString(12, auto_scrap_yield);
          ps_insert.setString(13, mrb_yield);
          ps_insert.setString(14, sample_yield);
          TDSLogger.println(InsertSQL1.toString());
          ps_insert.executeUpdate();
        } else {
        }
      }
      conn.commit();
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn);
    }
    return flag;
  }

  /*****************************************************************
   *主題:刪除row資料
   *****************************************************************/
  public static boolean delete_row(String record_id,
                                   int sid,
                                   String brand,
                                   String version,
                                   String pd_body) {
    Connection conn = null;
    String sql = null;
    try {
      conn = DBConnection.getConnection();
      sql = "delete from TF_BASIC_INFO_tx where " +
          "sid=? and product_body=? and brand=? and version=? and tester=?  ";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, sid);
      ps.setString(2, pd_body);
      ps.setString(3, brand);
      ps.setString(4, version);
      ps.setString(5, record_id);
      ps.executeUpdate();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
    } finally {
      DBConnection.close(conn);
    }
    return false;
  }

  /********************************************************************
   *主題:get Add資料
   *********************************************************************/
  public static boolean update_data(String[] sid,
                                    String[] pd_body,
                                    String[] brand,
                                    String[] version,
                                    String[] tester,
                                    String[] good_bin,
                                    String[] fail_bin,
                                    String[] remark,
                                    String[] auto_ship_yield,
                                    String[] stop_test_yield,
                                    String[] auto_scrap_yield,
                                    String[] mrb_yield,
                                    String[] sample_yield,
                                    String[] tester_be,
                                    String flag) {

    String InsSQL = null;
    String InsSQL1 = null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      InsSQL = "update TF_BASIC_INFO_tx " +
          "set tester=?, good_bin=?, fail_bin=?, remark=?, auto_ship_yield=?, stop_test_yield=?, auto_scrap_yield=?, mrb_yield=?, sample_yield=? " +
          "where sid = ? and product_body=? and brand=? and version=? and tester=? "
          ;
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
      for (int i = 0; i < Math.min(pd_body.length,tester.length); i++) {
        ps2.setString(1, tester[i]);
        ps2.setString(2, good_bin[i]);
        ps2.setString(3, fail_bin[i]);
        ps2.setString(4, remark[i]);
        ps2.setString(5, auto_ship_yield[i]);
        ps2.setString(6, stop_test_yield[i]);
        ps2.setString(7, auto_scrap_yield[i]);
        ps2.setString(8, mrb_yield[i]);
        ps2.setString(9, sample_yield[i]);
        ps2.setInt(10, Integer.parseInt(sid[i]));
        ps2.setString(11, pd_body[i]);
        ps2.setString(12, brand[i]);
        ps2.setString(13, version[i]);
        ps2.setString(14, tester_be[i]);
        ps2.executeUpdate();
      }
      if (flag.equals("submit_cmd")) {
        InsSQL1 = "update tf_information set TF_BASIC_INFORMATION = ? " +
            "where sid = ? and product_body=? and brand=? and version=? ";
        PreparedStatement ps3 = conn.prepareStatement(InsSQL1);
        ps3.setString(1, "Y");
        ps3.setInt(2, Integer.parseInt(sid[0]));
        ps3.setString(3, pd_body[0]);
        ps3.setString(4, brand[0]);
        ps3.setString(5, version[0]);
        ps3.executeUpdate();
      }
      conn.commit();
    } catch (Exception ex) {
      DBConnection.rollback(conn);
      ex.printStackTrace();
      return false;
    } finally {
      DBConnection.close(conn);
    }
    return true; // orig: flase
  }

  /*****************************************************************
   *主題:reset資料,即delete tf_test_parameter_ft_tx中之相資關資料
   ******************************************************************/
  public static boolean reset_tx(int sid,
                                 String brand,
                                 String version,
                                 String pd_body) {

    Connection conn = null;
    String sql = null;
    String InsSQL1 = null;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      sql = "delete from TF_BASIC_INFO_tx " +
          "where sid = ? and product_body=? and brand=? and version=? ";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, sid);
      ps.setString(2, pd_body);
      ps.setString(3, brand);
      ps.setString(4, version);
      TDSLogger.println(sql.toString());
      ps.executeUpdate();

      InsSQL1 = "update tf_information set TF_BASIC_INFORMATION = ? " +
          "where sid = ? and product_body=? and brand=? and version=? ";
      PreparedStatement ps3 = conn.prepareStatement(InsSQL1);
      ps3.setString(1, "N");
      ps3.setInt(2, sid);
      ps3.setString(3, pd_body);
      ps3.setString(4, brand);
      ps3.setString(5, version);
      TDSLogger.println(ps3.toString());
      ps3.executeUpdate();
      conn.commit();
    } catch (Exception ex) {
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
    } finally {
      DBConnection.close(conn);
    }
    return false;
  }

  public static void main(String[] args) {
//    TFIMBasicService tFIMBasicService = new TFIMBasicService();
  }
}
