package com.mxic.oiplus.oimaintain;

import oracle.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
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
      conn = DBConnection.getConnection();
      String lastSid = OiMaintainService.getPreviousVersionSid(conn, ""+sid);
      check_tx_boolean = check_tx(sid);

      if (check_tx_boolean) {
        //_tx中無資料
        check_first_boolean = check_first(product_body, brand, version - 1);
        if (!check_first_boolean) {
          //為第二版資料,取得sid version-1的資料,讀出資料存入tf_test_parameter_ft_tx,tag=0
          int version_two = version - 1;
          sql.append("INSERT INTO TF_BASIC_INFO_tx " +
                     "(sid,tag,product_body,brand,version,tester,good_bin,ib_bin,fail_bin,remark,auto_ship_yield,stop_test_yield,auto_scrap_yield,mrb_yield,sample_yield,OPTIONS,GRADE,BIN_TYPE,INKLESS_GRADE,KTD_BIN_FLAG, IPN_ACTION,EPN_SPEED,TEST_SPEED,DOWN_GRADE) ");
          sql.append("SELECT " + sid + ",0,product_body,brand," + version +
                     ",tester,good_bin,ib_bin,fail_bin,remark,auto_ship_yield,stop_test_yield,auto_scrap_yield,mrb_yield,sample_yield,OPTIONS,GRADE,BIN_TYPE,INKLESS_GRADE,KTD_BIN_FLAG, IPN_ACTION,EPN_SPEED,TEST_SPEED,DOWN_GRADE ");
          sql.append("FROM TF_BASIC_INFO WHERE product_body='" + product_body +
                     "' and brand='" + brand + "' and version ='" + version_two + "'");
          
          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
          ps.close();
          ps=null;
          sql.delete(0,sql.length());
          sql.append("INSERT INTO TF_BASIC_INFO_COMMENT_TX (sid,comments,comments2)\n ");
          sql.append("SELECT " + sid + ",a.comments, a.comments2 \n");
          sql.append("FROM TF_BASIC_INFO_COMMENT a\n");
          sql.append("WHERE a.sid =" +lastSid);
          ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
          ps.close();
          ps=null;

          
        }
      }
      DBConnection.close(conn);
      sql = new StringBuffer();
      sql.append(
          "SELECT * FROM TF_BASIC_INFO_tx_vw a where " +
          "product_body='" +product_body +
          "' and brand='" + brand +
          "' and version ='" + version + "' Order by tester,options,grade,good_bin");
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
		bean.setIb_bin(rs.getString("ib_bin"));
        bean.setRemark(rs.getString("remark"));
        bean.setAuto_ship_yield(rs.getString("auto_ship_yield"));
        bean.setStop_test_yield(rs.getString("stop_test_yield"));
        bean.setAuto_scrap_yield(rs.getString("auto_scrap_yield"));
        bean.setMrb_yield(rs.getString("mrb_yield"));
        bean.setOptions(rs.getString("options"));
        bean.setGrade(rs.getString("grade"));
        bean.setBin_type(rs.getString("bin_type"));
        bean.setBin_type_str(rs.getString("bin_type_str"));
        bean.setInkless_grade(rs.getString("inkless_grade"));
        bean.setKtd_bin_flag(rs.getString("ktd_bin_flag"));
        bean.setIpn_action(rs.getString("ipn_action"));
        bean.setIpn_action_str(rs.getString("ipn_action_str"));
        bean.setEpn_speed(rs.getString("epn_speed"));
        bean.setTest_speed(rs.getString("test_speed"));
        bean.setDown_grade(rs.getString("down_grade"));
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
      //DBConnection.close(conn);
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
   * Get Comments of Basic Information
   *****************************************************************/
  public static String getBAComment(Connection conn, int sid, String status) {
    //Connection conn = null;
    String comments = "";
    String table = "_TX";
    
    if (status.equals("R"))
    	table = "";
    
    try {
      String sql = "SELECT comments2 FROM TF_BASIC_INFO_COMMENT"+table+" where sid=" + sid;
//      sql += "\n union SELECT comments2 FROM TF_BASIC_INFO_COMMENT_tx where sid=" + sid;

      //conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
    	  java.sql.Clob notes = rs.getClob("comments2");
    	  if (notes != null) {
    		  int len = (int) notes.length();
    		  comments = notes.getSubString(1, len);
    	  }
//    	  comments = rs.getString("comments");
      }
      //DBConnection.close(conn);
      return comments;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return "";
    } finally {
      //DBConnection.close(conn);
    }
  }
  /*****************************************************************
   * Get Comments of Basic Information
   *****************************************************************/
  public static String getBAComment(int sid, String status) {
    Connection conn = null;
    String comments = "";
    String table = "_TX";
    
    if (status.equals("R"))
    	table = "";
    
    try {
      conn = DBConnection.getConnection();
      comments = getBAComment(conn, sid, status);
      return comments;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return "";
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
      //DBConnection.close(conn_check_first);
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
      //DBConnection.close(conn_check_first);
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
   *主題: Insert Basic Information 資料
   *********************************************************************/
  public static boolean insertadd(int sid,
                                  String pd_body,
                                  String brand,
                                  String version,
                                  String tester,
                                  String options,
                                  String grade,
                                  String good_bin,
								  String ib_bin,
                                  String bin_type,
                                  String inkless_grade,
                                  String ktd_bin_flag,
                                  String ipn_action,
                                  String epn_speed,
                                  String test_speed,
                                  String down_grade,
                                  String fail_bin,
                                  String remark,
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
          "' and tester='" + tester +
          "' and good_bin='" + good_bin +
          "' and options='" + options +
          "' and inkless_grade='" + inkless_grade +
          "' and ktd_bin_flag='" + ktd_bin_flag +
          "' and grade='" + grade +"'" );
      /*
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
          "auto_ship_yield,stop_test_yield,auto_scrap_yield,mrb_yield,sample_yield," +
          "options,grade,bin_type,inkless_grade,ipn_action,epn_speed,test_speed,down_grade,ib_bin,ktd_bin_flag) " +
          "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
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
          ps_insert.setString(15, options);
          ps_insert.setString(16, grade);
          ps_insert.setString(17, bin_type);
          ps_insert.setString(18, inkless_grade);
          ps_insert.setString(19, ipn_action);
          ps_insert.setString(20, epn_speed);
          ps_insert.setString(21, test_speed);
          ps_insert.setString(22, down_grade);
		  ps_insert.setString(23, ib_bin);
		  ps_insert.setString(24, ktd_bin_flag);
          //TDSLogger.println(ps_insert.toString());
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

  /********************************************************************
   *主題: Update Basic Information 資料
   *********************************************************************/
  public static boolean update_row(int sid,
                                  String pd_body,
                                  String brand,
                                  String version,
                                  String tester,
                                  String options,
                                  String grade,
                                  String good_bin,
								  String ib_bin,
                                  String bin_type,
                                  String inkless_grade,
                                  String ktd_bin_flag,
                                  String ipn_action,
                                  String epn_speed,
                                  String test_speed,
                                  String down_grade,
                                  String fail_bin,
                                  String remark,
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
      InsertSQL1 = "update TF_BASIC_INFO_tx " +
          "set tag=?,fail_bin=?,remark=?,auto_ship_yield=?,stop_test_yield=?,auto_scrap_yield=?,mrb_yield=?"+
          ",sample_yield=?,bin_type=?,inkless_grade=?,ipn_action=?,epn_speed=?,test_speed=?,down_grade=?, ktd_bin_flag=?" +
          "where sid=? and product_body=? and brand=? and version=? and tester=? and " +
          "good_bin=? and options=? and grade=? ";
          //"good_bin=? and options=? and grade=? and COALESCE(ib_bin,'BIN0')=?";

      PreparedStatement ps_insert = conn.prepareStatement(InsertSQL1);
      ps_insert.setString(1, "1");
      ps_insert.setString(2, fail_bin);
      ps_insert.setString(3, remark);
      ps_insert.setString(4, auto_ship_yield);
      ps_insert.setString(5, stop_test_yield);
      ps_insert.setString(6, auto_scrap_yield);
      ps_insert.setString(7, mrb_yield);
      ps_insert.setString(8, sample_yield);
      ps_insert.setString(9, bin_type);
      ps_insert.setString(10, inkless_grade);
      ps_insert.setString(11, ipn_action);
      ps_insert.setString(12, epn_speed);
      ps_insert.setString(13, test_speed);
      ps_insert.setString(14, down_grade);

      ps_insert.setString(15, ktd_bin_flag);
      
      
      ps_insert.setInt(16, sid);
      ps_insert.setString(17, pd_body);
      ps_insert.setString(18, brand);
      ps_insert.setInt(19, Integer.parseInt(version));
      ps_insert.setString(20, tester);
      ps_insert.setString(21, good_bin);
      ps_insert.setString(22, options);
      ps_insert.setString(23, grade);
      
	  /*if(ib_bin == "")
          ps_insert.setString(23, "BIN0");
      else
          ps_insert.setString(23, ib_bin);
      */    
      //TDSLogger.println(ps_insert.toString());
      ps_insert.executeUpdate();
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
    String attr[] = record_id.split(",");
    if (attr.length != 4)
      return false;

    try {
      conn = DBConnection.getConnection();
      sql = "delete from TF_BASIC_INFO_tx where " +
          "sid=? and product_body=? and brand=? and version=? and tester=? and options=? and grade=? and good_bin=?  ";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, sid);
      ps.setString(2, pd_body);
      ps.setString(3, brand);
      ps.setString(4, version);
      ps.setString(5, attr[0]);
      ps.setString(6, attr[1]);
      ps.setString(7, attr[2]);
      ps.setString(8, attr[3]);
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
                                    String[] options,
                                    String[] grade,
                                    String[] good_bin,
                                    String[] remark,
/*
                                    String[] auto_ship_yield,
                                    String[] stop_test_yield,
                                    String[] auto_scrap_yield,
                                    String[] mrb_yield,
                                    String[] sample_yield,
*/
                                    String comments,
                                    String flag) {

    String sql = null;
    Connection conn = null;
    try {
      conn = DBConnection.getOracleConnection();
      conn.setAutoCommit(false);

      // 是否已有 COMMENTS
      sql = "select count(*) cnt from TF_BASIC_INFO_COMMENT_TX where sid = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, Integer.parseInt(sid[0]));
      ResultSet rs = ps.executeQuery();
      int cnt = 0;
      while (rs.next()) {
        cnt = Integer.parseInt(rs.getString("cnt"));
      }
      ps.close();
      ps = null;
      //lai-add-start-20130829[都先刪掉, 以免留舊料刪不掉]
      sql = "delete from TF_BASIC_INFO_COMMENT_TX where sid=?";
	  ps = conn.prepareStatement(sql);
	  ps.setInt(1, Integer.parseInt(sid[0]));
	  rs = ps.executeQuery();
	  ps.close();
	  ps = null;
	  //lai-add-end-20130829
      // 沒有->先 insert 一筆 (如果有 comments)
      //if (cnt == 0 && !comments.equals("")) {//20130829-lai-mark
	  if (!comments.equals("")) {
    	  sql = "insert into TF_BASIC_INFO_COMMENT_TX (sid,COMMENTS,COMMENTS2) values(?,null,empty_clob())";
          ps = conn.prepareStatement(sql);
          ps.setInt(1, Integer.parseInt(sid[0]));
          ps.executeUpdate();
          ps.close();
          ps = null;
      }

	  //空白值要另外處理
      if (comments.equals("")) {
    	  /*lai-mark-20130829 if (cnt > 0) { // 已有 comment, 本次被刪除
    		  sql = "delete from TF_BASIC_INFO_COMMENT_TX where sid=?";
    		  ps = conn.prepareStatement(sql);
    		  ps.setInt(1, Integer.parseInt(sid[0]));
    		  rs = ps.executeQuery();
    		  ps.close();
    		  ps = null;
    	  }*/
      } else {
    	  //取出 clob for update
    	  /*20130829-lai-mark sql = "select comments2 from TF_BASIC_INFO_COMMENT_TX where sid=? for update";
    	  ps = conn.prepareStatement(sql);
    	  ps.setInt(1, Integer.parseInt(sid[0]));
    	  rs = ps.executeQuery();
    	  rs.next();
    	  TDSLogger.println("--- CLOB Class ----\n"+rs.getClob(1).getClass()); 
          */
    	  sql = "select comments2 from TF_BASIC_INFO_COMMENT_TX where sid=? for update";
    	  ps = conn.prepareStatement(sql);
    	  ps.setInt(1, Integer.parseInt(sid[0]));
    	  rs = ps.executeQuery();
    	  rs.next();
    	  TDSLogger.println("--- CLOB Class ----\n"+rs.getClob(1).getClass()); 
    	  CLOB clob = (oracle.sql.CLOB) rs.getClob(1);
    	  if (clob != null) {
    		  java.io.Writer wr = clob.getCharacterOutputStream(); 
   	  	  	wr.write(comments);  
   	  	  	wr.flush();  
   	  	  	wr.close();
    	  }
    	  ps.close();
    	  ps = null;
      }

      if (flag.equals("submit_cmd")) {
        sql = "update tf_information set TF_BASIC_INFORMATION = ? " +
            "where sid = ? and product_body=? and brand=? and version=? ";
        ps = conn.prepareStatement(sql);
        ps.setString(1, "Y");
        ps.setInt(2, Integer.parseInt(sid[0]));
        ps.setString(3, pd_body[0]);
        ps.setString(4, brand[0]);
        ps.setString(5, version[0]);
        ps.executeUpdate();
        ps.close();
        ps = null;
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
   *主題:reset資料,即delete tf_basic_info_tx 中之相資關資料
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
      //TDSLogger.println(ps.toString());
      ps.executeUpdate();
      ps.close();
      ps = null;

      sql = "delete from TF_BASIC_INFO_COMMENT_TX " +
          "where sid = ?  ";
      ps = conn.prepareStatement(sql);
      ps.setInt(1, sid);
      ps.executeUpdate();
      ps.close();
      ps = null;

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
