package com.mxic.oiplus.oisearch;

import com.mxic.oiplus.resource.*;
//import com.mxic.oiplus.rs.*;

import java.sql.*;
import java.util.*;
import java.lang.*;
import com.mxic.oiplus.util.StringUtil.*;
import com.mxic.oiplus.util.*;
import java.util.Date;
import java.text.*;
import java.io.*;
import javax.swing.*;

public class TestRouteAddService {
  public TestRouteAddService() {
  }

  /*****************************************************************
   *主題:reset資料,即delete tf_route_master中之相資關資料
   ******************************************************************/
  public static String file_name(String route_name) {

    Connection conn = null;
    boolean flag = true;
    String file_name = "";
    try {
      StringBuffer sql = new StringBuffer();
      //若有讀到則復蓋file_name...,若無讀到直接新增
      sql.append("SELECT testflow_filename FROM tf_route_master where route_name='" + route_name );

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      while (rs.next()) {
        file_name = rs.getString("testflow_filename");
      }
//      DBConnection.close(conn);
      return file_name;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
      return null;
    } finally {
      DBConnection.close(conn);
    }
  }

  /*****************************************************************
   *主題:reset資料,即delete tf_route_master中之相資關資料
   ******************************************************************/
  public static boolean update_file_name(String route_name, String testflow_filename, String remark, String route_cat, String user) {

    Connection conn = null;
    Connection conn1 = null;
    Connection conn2 = null;
    boolean flag = true;
    String InsSQL3 = null;
    try {
        conn2 = DBConnection.getConnection();
        InsSQL3 = "update tf_route_master set testflow_filename = ?, remark = ?, route_cat = ?, chart_log_time = sysdate where route_name='" + route_name + "'";
        PreparedStatement ps_insert3 = conn2.prepareStatement(InsSQL3);
        ps_insert3.setString(1, testflow_filename);
        ps_insert3.setString(2, remark);
        ps_insert3.setString(3, route_cat);
        ps_insert3.executeUpdate();
        ps_insert3.close();
        ps_insert3 = null;


        conn = DBConnection.getConnection();
        InsSQL3 = "Insert into tf_testflow_upload_log (ROUTE_NAME,ACTION,USER_NAME,LOG_TIME) values (?,?,?,sysdate) ";
        PreparedStatement ps_insert2 = conn2.prepareStatement(InsSQL3);
        ps_insert2.setString(1, route_name);
        ps_insert2.setString(2, "Upload");
        ps_insert2.setString(3, user);
        ps_insert2.executeUpdate();
        ps_insert2.close();
        ps_insert2 = null;

//      DBConnection.close(conn2);
//      DBConnection.close(conn);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn2);
      DBConnection.close(conn);
      return flag;
    }
  }
  
  public static boolean update_file_name(String route_name, String file_type, String testflow_filename, String remark, String route_cat, String user) {

	    Connection conn = null;
	    Connection conn2 = null;
	    boolean flag = true;
	    String InsSQL3 = null;
	    try {
	        conn2 = DBConnection.getConnection();
	        if(file_type.equals("png"))
	        	InsSQL3 = "update tf_route_master set testflow_filename = ?, remark = ?, route_cat = ?, chart_log_time = sysdate where route_name='" + route_name + "'";
	        else if(file_type.equals("xls"))
	        	InsSQL3 = "update tf_route_master set stdexcflow_filename = ?, remark = ?, route_cat = ?, chart_log_time = sysdate where route_name='" + route_name + "'";
	        PreparedStatement ps_insert3 = conn2.prepareStatement(InsSQL3);
	        ps_insert3.setString(1, testflow_filename);
	        ps_insert3.setString(2, remark);
	        ps_insert3.setString(3, route_cat);
	        ps_insert3.executeUpdate();
	        ps_insert3.close();
	        ps_insert3 = null;


	        conn = DBConnection.getConnection();
	        InsSQL3 = "Insert into tf_testflow_upload_log (ROUTE_NAME,ACTION,USER_NAME,LOG_TIME) values (?,?,?,sysdate) ";
	        PreparedStatement ps_insert2 = conn2.prepareStatement(InsSQL3);
	        ps_insert2.setString(1, route_name);
	        ps_insert2.setString(2, "Upload");
	        ps_insert2.setString(3, user);
	        ps_insert2.executeUpdate();
	        ps_insert2.close();
	        ps_insert2 = null;

	    } catch (Exception ex) {
	      ex.fillInStackTrace();
	      TDSLogger.println(ex.getMessage());
	      flag = false;
	    } finally {
	      DBConnection.close(conn2);
	      DBConnection.close(conn);
	    }
	      return flag;
	  }

  
  public static boolean delete_file_name(String route_name, String file_type, String testflow_filename, String remark, String route_cat, String user) {
	    Connection conn = null;
	    Connection conn2 = null;
	    boolean flag = true;
	    String InsSQL3 = null;
	    try {
	        conn2 = DBConnection.getConnection();
	        if(file_type.equals("png"))
	        	InsSQL3 = "update tf_route_master set testflow_filename = ?, remark = ?, route_cat = ?, chart_log_time = sysdate where route_name='" + route_name + "'";
	        else if(file_type.equals("xls"))
	        	InsSQL3 = "update tf_route_master set stdexcflow_filename = ?, remark = ?, route_cat = ?, chart_log_time = sysdate where route_name='" + route_name + "'";
	        PreparedStatement ps_insert3 = conn2.prepareStatement(InsSQL3);
	        ps_insert3.setString(1, testflow_filename);
	        ps_insert3.setString(2, remark);
	        ps_insert3.setString(3, route_cat);
	        ps_insert3.executeUpdate();
	        ps_insert3.close();
	        ps_insert3 = null;


	        conn = DBConnection.getConnection();
	        InsSQL3 = "Insert into tf_testflow_upload_log (ROUTE_NAME,ACTION,USER_NAME,LOG_TIME) values (?,?,?,sysdate) ";
	        PreparedStatement ps_insert2 = conn2.prepareStatement(InsSQL3);
	        ps_insert2.setString(1, route_name);
	        ps_insert2.setString(2, "Delete");
	        ps_insert2.setString(3, user);
	        ps_insert2.executeUpdate();
	        ps_insert2.close();
	        ps_insert2 = null;

	    } catch (Exception ex) {
	      ex.fillInStackTrace();
	      TDSLogger.println(ex.getMessage());
	      flag = false;
	    } finally {
	      DBConnection.close(conn2);
	      DBConnection.close(conn);
	    }
	      return flag;
	  }
  
  public static boolean update_remark_route_cat(String route_name, String remark, String route_cat, String user) {

    Connection conn = null;
    Connection conn1 = null;
    Connection conn2 = null;
    boolean flag = true;
    String InsSQL3 = null;
    try {
        conn2 = DBConnection.getConnection();
        InsSQL3 = "update tf_route_master set remark = ? , route_cat = ? where route_name='" + route_name + "'";
        PreparedStatement ps_insert3 = conn2.prepareStatement(InsSQL3);
        ps_insert3.setString(1, remark);
        ps_insert3.setString(2, route_cat);
        ps_insert3.executeUpdate();
        ps_insert3.close();
        ps_insert3 = null;


//      DBConnection.close(conn2);
//      DBConnection.close(conn);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn2);
      DBConnection.close(conn);
      return flag;
    }
  }

  public static boolean update_remark(String route_name, String remark, String user) {

    Connection conn = null;
    Connection conn1 = null;
    Connection conn2 = null;
    boolean flag = true;
    String InsSQL3 = null;
    try {
        conn2 = DBConnection.getConnection();
        InsSQL3 = "update tf_route_master set remark = ?  where route_name='" + route_name + "'";
        PreparedStatement ps_insert3 = conn2.prepareStatement(InsSQL3);
        ps_insert3.setString(1, remark);
        ps_insert3.executeUpdate();
        ps_insert3.close();
        ps_insert3 = null;


//      DBConnection.close(conn2);
//      DBConnection.close(conn);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn2);
      DBConnection.close(conn);
      return flag;
    }
  }

  public static boolean update_route_cat(String route_name, String route_cat, String user) {

    Connection conn = null;
    Connection conn1 = null;
    Connection conn2 = null;
    boolean flag = true;
    String InsSQL3 = null;
    try {
        conn2 = DBConnection.getConnection();
        InsSQL3 = "update tf_route_master set route_cat = ?  where route_name='" + route_name + "'";
        PreparedStatement ps_insert3 = conn2.prepareStatement(InsSQL3);
        ps_insert3.setString(1, route_cat);
        ps_insert3.executeUpdate();
        ps_insert3.close();
        ps_insert3 = null;


//      DBConnection.close(conn2);
//      DBConnection.close(conn);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn2);
      DBConnection.close(conn);
      return flag;
    }
  }



  public static TestRouteAddActionForm[] getbean(int sid) {
    ArrayList tmp = new ArrayList();
    TestRouteAddActionForm bean = new TestRouteAddActionForm();
    bean.setSid(sid);
    tmp.add(bean);
    return (TestRouteAddActionForm[]) tmp.toArray(new TestRouteAddActionForm[0]);
  }


  public static void main(String[] args) {
    TestRouteAddActionForm TestRouteAddService = new TestRouteAddActionForm();
  }
}
