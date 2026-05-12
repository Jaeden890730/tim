package com.mxic.oiplus.eif;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.mxic.oiplus.oimaintain.OiMaintainService;
import com.mxic.oiplus.pdf.pdfService;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.TDSProperties;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.SafeExec;
import com.mxic.oiplus.util.TDSLogger;

// copy from com.mxic.oiplus.oimaintain.OIReleaseService.java

public class EifOIReleaseService {

  public EifOIReleaseService() {
  }

  // Get OI's SID by Product Body + Brand + Version for certain Status
  public static String getOIsid(Connection conn,
		                        String product_body,
                                String version,
                                String brand,
                                String status) {
      String sql;
      String sid = null;
      //Connection conn = null;

      try {
          //conn = DBConnection.getConnection();
          sql = "select sid from tf_information " +
              "where product_body = '" + product_body + "' " +
              "and version = " + version +
              " and brand = '" + brand + "'";
          if (!status.equals(""))
              sql = sql + " and status = '" + status + "'";

          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();
          if (rs.next()) {
              sid = rs.getString("sid");
          }
          rs.close();
          rs = null;
          }
      catch (Exception ex) {
          ex.printStackTrace();
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return "exception";
      }
      finally {
          //DBConnection.close(conn);
      }
      return sid;
  }

  // get not-processed oi data
  // return Array of "product_body,version,brand,status,caseno"
  public static ArrayList getNonProcessedOI(Connection conn) {
      String sql;
      ArrayList vl = null;
      //Connection conn = null;
      String brand;

      try {
          //conn = DBConnection.getConnection();
//          sql = "select caseno, docno, rev, status from if_tf_coverpage " +
//                " where caseno = 'BZ9999' ";
            sql = "select caseno, docno, rev, status from if_tf_coverpage " +
                  "where processed = 'N' and status != 'EPC' ";

          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();

          while (rs.next()) {
              if (vl == null)
                  vl = new ArrayList();

                  // format 8049K-6615 or 8049-6615
                  String str[] = rs.getString("docno").trim().split("-");
                  if (str == null)
                      continue;
                  if (str.length != 2) // 格式不對
                      continue;
                  if (str[0].length() == 5)
                      if (str[0].charAt(4) == 'K')
                          brand = "KH";
                      else
                          continue;
                  else brand = "MX";
                  vl.add(str[1]+","+rs.getString("rev")+","+brand+","+rs.getString("status")+","+rs.getString("caseno")+","+rs.getString("docno"));
              }
          rs.close();
          rs = null;
          }
      catch (Exception ex) {
          ex.printStackTrace();
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return null;
      }
      finally {
          //DBConnection.close(conn);
      }
      return vl;
  }

  public static ArrayList getOICoverPage(Connection conn,String caseNo, String docNo, String rev) {
      String sql;
      ArrayList vl = null;
      //Connection conn = null;
      String brand;

      try {
          //conn = DBConnection.getConnection();
          sql = "select caseno, docno, rev, status,replace_tecr_no from if_tf_coverpage " +
                "where caseno = '" + caseNo + "' and docno = '" + docNo + "' and rev = " + rev +
                " and processed = 'N' ";

          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();

          while (rs.next()) {
              if (vl == null)
                  vl = new ArrayList();

                  // format 8049K-6615 or 8049-6615
                  String str[] = rs.getString("docno").trim().split("-");
                  if (str == null)
                      continue;
                  if (str[0].length() == 5)
                      if (str[0].charAt(4) == 'K')
                          brand = "KH";
                      else
                          continue;
                  else brand = "MX";
                  //vl.add(str[1]+","+rs.getString("rev")+","+brand+","+rs.getString("status")+","+rs.getString("caseno")+","+rs.getString("docno")+","+rs.getString("replace_tecr_no"));
                  vl.add(str[1]+","+rs.getString("rev")+","+brand+","+rs.getString("status")+","+rs.getString("caseno")+","+rs.getString("docno"));
              }
          rs.close();
          rs = null;
          }
      catch (Exception ex) {
          ex.printStackTrace();
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return null;
      }
      finally {
          //DBConnection.close(conn);
      }
      return vl;
  }
  public static ArrayList getOIByUser(Connection conn,String product_type, String product_body, String brand, String version) {
      String sql;
      ArrayList vl = null;
      //Connection conn = null;

      try {
          //conn = DBConnection.getConnection();
          sql = "select product_type, product_body, brand, version rev, '生效'as status, 'CASENO' as caseno, '8049-'|| product_body  as docno from tf_information " +
                "where product_type = '" + product_type + "' and product_body = '" + product_body + "' and brand = '" + brand + "' " +
                " and version in(" + version + ")";

          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();

          while (rs.next()) {
              if (vl == null)
                  vl = new ArrayList();

                  vl.add(rs.getString("product_body")+","+rs.getString("rev")+","+rs.getString("brand")+","+rs.getString("status").trim()+","+rs.getString("caseno")+","+rs.getString("docno"));
              }
          rs.close();
          rs = null;
          }
      catch (Exception ex) {
          ex.printStackTrace();
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return null;
      }
      finally {
          //DBConnection.close(conn);
      }
      return vl;
  }

  // get not-processed oi data
  // return Array of "product_body,version,brand,status,caseno"
  public static TFCoverPageBean getCoverPage(Connection conn, String productBody, String brand, int version) {
      String sql;
      //Connection conn = null;
      TFCoverPageBean result = null;

      try {
          //conn = DBConnection.getConnection();
          sql = "SELECT PRODUCT_BODY, BRAND, VERSION, APPLICANT, APPROVE_NO, APPROVE_DATE, " +
                "REMARK, SPONSOR FROM TF_COVERPAGE " +
                "WHERE PRODUCT_BODY = '" + productBody + "' AND BRAND = '" + brand +
                "' AND VERSION = " + version;

          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();

          while (rs.next()) {
        	  result = new TFCoverPageBean();
        	  result.setProduct_body(rs.getString("PRODUCT_BODY"));
        	  result.setBrand(rs.getString("BRAND"));
        	  result.setVersion(new Integer(rs.getInt("VERSION")));
        	  result.setApplicant(rs.getString("APPLICANT"));
        	  result.setApprove_no(rs.getString("APPROVE_NO"));
        	  result.setApprove_date(rs.getDate("APPROVE_DATE"));
        	  result.setRemark(rs.getString("REMARK"));
        	  result.setSponsor(rs.getString("SPONSOR"));
          }
          rs.close();
          rs = null;
          }
      catch (Exception ex) {
          ex.printStackTrace();
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return null;
      }
      finally {
         // DBConnection.close(conn);
      }
      return result;
  }

  // Get OI's Vendor List
  public static ArrayList getVendorList(Connection conn,
                                        String sid) {

        StringBuffer sql = new StringBuffer();
        ArrayList vl = null;

        try {
            sql.append(
                "select distinct site from tf_test_parameter_ws a " +
                "where sid = " + sid +
                "union select distinct site from tf_test_parameter_ft a " +
                "where sid = " + sid +
                "union select distinct site from tf_test_parameter_pbc a " +
                "where sid = " + sid);

            TDSLogger.println(sql.toString());
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (vl == null)
                    vl = new ArrayList();
                vl.add(rs.getString("site"));
            }
            rs.close();
            rs = null;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        }
        finally {
        }
        return vl;
  }

  // mark the oi as processed
  public static boolean do_markProcessed(Connection conn, String caseno, String docno, String status) {

      //Connection conn = null;
      try {
          String sql = "update if_tf_coverpage set processed = '" + status + "' " +
              "where caseno = '" + caseno + "' and docno = '" + docno + "'";

          //conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql);
          ps.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
          //DBConnection.close(conn);
      }
      return true;
  }

  // remove table data
  public static boolean delete_data(Connection conn,
                                    String table_name,
                                    String sid,
                                    String product_body,
                                    String brand,
                                    String version) {

      StringBuffer sql_del = new StringBuffer();
      try {
          sql_del.append("delete from "+table_name+" where sid = ? and product_body=? and brand=? and version=? ");
          PreparedStatement ps_del = conn.prepareStatement(sql_del.toString());
          ps_del.setString(1, sid);
          ps_del.setString(2, product_body);
          ps_del.setString(3, brand);
          ps_del.setString(4, version);
          ps_del.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // remove table data
  public static boolean delete_data(Connection conn,
                                    String table_name,
                                    String sid,
                                    String product_body,
                                    String version) {

      StringBuffer sql_del = new StringBuffer();
      try {
          sql_del.append("delete from "+table_name+" where sid = ? and product_body=? and version=? ");
          PreparedStatement ps_del = conn.prepareStatement(sql_del.toString());
          ps_del.setString(1, sid);
          ps_del.setString(2, product_body);
          ps_del.setString(3, version);
          ps_del.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // remove table data
  public static boolean delete_data(Connection conn,
                                    String table_name,
                                    String sid) {

      StringBuffer sql_del = new StringBuffer();
      try {
          sql_del.append("delete from "+table_name+" where sid = ? ");
          PreparedStatement ps_del = conn.prepareStatement(sql_del.toString());
          ps_del.setString(1, sid);
          ps_del.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // move product route from tf_product_route_tx to tf_product_route
  public static boolean product_route_insert(Connection conn,
                                             String sid,
                                             String product_body,
                                             String brand,
                                             String version) {

      StringBuffer sql = new StringBuffer();

      try {
          sql.append(
              "INSERT INTO tf_product_route ( \n" +
              "sid, product_body, brand, version, route_name, step_seq, step_name, \n"+
              "test_time, time_unit, temperature, sampling_test, sampling_cond, remark, rework_step, test_time2, time_unit2, qc_actual_mode,step_def ) \n" +
              "SELECT sid, product_body, brand, version, route_name, step_seq, step_name, \n"+
              "test_time, time_unit, temperature, sampling_test, sampling_cond, remark, rework_step, test_time2, time_unit2, qc_actual_mode, step_def  \n" +
              "FROM tf_product_route_tx where \n" +
              "product_body='" + product_body +
              "' and brand='" + brand +
              "' and version='" + version +
              "' and sid='" + sid + "'");

          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }
//move product route from tf_product_route_tx to tf_product_route
  public static boolean product_waferlevel_insert(Connection conn,
                                             String sid,
                                             String product_body,
                                             String brand,
                                             String version) {

      StringBuffer sql = new StringBuffer();

      try {
          sql.append(
              "INSERT INTO tf_prod_waferlevel ( \n" +
              "sid, product_body, brand, version, wafer_level, wafer_brand, biztype, \n"+
              "wafer_grade, apply_type, ori_priority, revise_priority, checked_flag ) \n" +
              "SELECT sid, product_body, brand, version, wafer_level, wafer_brand, biztype, \n"+
              "wafer_grade, apply_type, ori_priority, revise_priority, checked_flag \n" +
              "FROM tf_prod_waferlevel_tx where \n" +
              "product_body='" + product_body +
              "' and brand='" + brand +
              "' and version='" + version +
              "' and sid='" + sid + "'");

          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // move bom vs route from tf_bom_route_tx to tf_bom_route
  public static boolean bom_route_insert(Connection conn,
                                      String sid,
                                      String product_body,
                                      String brand,
                                      String version) {

      StringBuffer sql = new StringBuffer();
      try {
          sql.append(
              "INSERT INTO tf_bom_route ( " +
              "sid, product_body, brand, version, backend_option, fg_with_code, pin_count, "+
              "package_type, ft_route, ft_route_add, ft_route_add2, ft_route_add3, ft_route_code, tf_comment, " +
              "mask_option, sort_route_code, db_with_code, ws_route, ws_route_add, tf_ws_comment, sales_form, tag, endurance, wsspecialcontrol,quality_level, quality_level_comment, mcp_flag) " +
              "SELECT DISTINCT sid, product_body, brand, version, backend_option, fg_with_code, pin_count, "+//20180718-LAI
              "package_type, ft_route, ft_route_add, ft_route_add2, ft_route_add3, ft_route_code, tf_comment, " +
              "mask_option, sort_route_code, db_with_code, ws_route, ws_route_add, tf_ws_comment, sales_form, " +
              "decode(tag, '2', '2', '0'), endurance, wsspecialcontrol,quality_level,quality_level_comment, mcp_flag " +
              "FROM tf_bom_route_tx where " +
              "product_body='" + product_body +
              "' and brand='" + brand +
              "' and version='" + version +
              "' and sid='" + sid + "'");

          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // move bom vs route from tf_bom_route_mcp_tx to tf_bom_route_mcp
  public static boolean bom_route_mcp_insert(Connection conn,
                                      String sid,
                                      String product_body,
                                      String brand,
                                      String version) {

      StringBuffer sql = new StringBuffer();
      try {
          sql.append(
              "INSERT INTO tf_bom_route_mcp ( " +
              "	sid, product_body, brand, version, backend_option, fg_with_code, pin_count, "+
              "	package_type, ft_route, ft_route_add, ft_route_add2, ft_route_add3, ft_route_code, tf_comment, " +
              "	sort_route_code, db_with_code, ws_route, ws_route_add, tf_ws_comment, sales_form, tag, " +
              "	endurance, wsspecialcontrol, quality_level, quality_level_comment, " +
              "	component_no, com_prod_body, com_mask_option, com_backend_option) " +
              "SELECT DISTINCT sid, product_body, brand, version, backend_option, fg_with_code, pin_count, "+//20180718
              "	package_type, ft_route, ft_route_add, ft_route_add2, ft_route_add3, ft_route_code, tf_comment, " +
              "	sort_route_code, db_with_code, ws_route, ws_route_add, tf_ws_comment, sales_form, " +
              "	decode(tag, '2', '2', '0'), endurance, wsspecialcontrol,quality_level,quality_level_comment, " +
              "	component_no, com_prod_body, com_mask_option, com_backend_option " +
              "FROM tf_bom_route_mcp_tx where " +
              "product_body='" + product_body +
              "' and brand='" + brand +
              "' and version='" + version +
              "' and sid='" + sid + "'");

          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }  
  
  // move bom vs route from tf_bom_route_mrom_tx to tf_bom_route_mrom
  public static boolean bom_route_mrom_insert(Connection conn, String sid) {

      StringBuffer sql = new StringBuffer();
      try {
          sql.append(
              "INSERT INTO tf_bom_route_mrom ( " +
              "sid, product_body, version, sales_form, mask_option, ws_route, ft_route, tag,  " +
              "ft_route_add1, ft_route_add2, ft_comment )  " +
              "SELECT DISTINCT sid, product_body, version, sales_form, mask_option, ws_route, ft_route, " +//20180718
              "decode(tag, '2', '2', '0'), ft_route_add1, ft_route_add2, ft_comment " +
              "FROM tf_bom_route_mrom_tx where " +
              "sid='" + sid + "'");

          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // move bom vs route from tf_bom_route_xrom_tx to tf_bom_route_xrom
  public static boolean bom_route_xrom_insert(Connection conn,
                                      String sid,
                                      String product_body,
                                      String brand,
                                      String version) {

      StringBuffer sql = new StringBuffer();
      try {
          sql.append(
              "INSERT INTO tf_bom_route_xrom ( " +
              "sid, product_body, version, body_version, mask_option, mask_option_rev, code_no, pin_count, "+
              "package_code, route_type, ft_route_code, ft_route, ft_route_add, ft_comment, " +
              "sort_route_code, ws_route, ws_route_add, ws_route_add1, ws_route_add2, ws_route_add3, ws_route_add4, ws_comment, tag, ft_route_add1, ft_route_add2, ft_route_add3, ft_route_add4, ft_route_add5) " +
              "SELECT DISTINCT sid, product_body, version, body_version, mask_option, mask_option_rev, code_no, pin_count, "+//20180718
              "package_code, route_type, ft_route_code, ft_route, ft_route_add, ft_comment, " +
              "sort_route_code, ws_route, ws_route_add, ws_route_add1, ws_route_add2, ws_route_add3, ws_route_add4, ws_comment,  " +
              "decode(tag, '2', '2', '0'), ft_route_add1, ft_route_add2, ft_route_add3, ft_route_add4, ft_route_add5 " +
              "FROM tf_bom_route_xrom_tx where " +
              "product_body='" + product_body +
              //"' and brand='" + brand +
              "' and version='" + version +
              "' and sid='" + sid + "'");

          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // move bom vs route from tf_bom_reroute_xrom_tx to tf_bom_reroute_xrom
  public static boolean bom_reroute_xrom_insert(Connection conn,
                                      String sid,
                                      String product_body,
                                      String brand,
                                      String version) {

      StringBuffer sql = new StringBuffer();
      try {
          sql.append(
              "INSERT INTO tf_bom_reroute_xrom ( " +
              "sid, product_body, version, body_version, mask_option, mask_option_rev, code_no, pin_count, "+
              "package_code, route_type, recycle_code, ft_route, ft_route_add, ft_comment, tag ) " +
              "SELECT sid, product_body, version, body_version, mask_option, mask_option_rev, code_no, pin_count, "+
              "package_code, route_type, recycle_code, ft_route, ft_route_add, ft_comment, " +
              "decode(tag, '2', '2', '0') " +
              "FROM tf_bom_reroute_xrom_tx where " +
              "product_body='" + product_body +
              //"' and brand='" + brand +
              "' and version='" + version +
              "' and sid='" + sid + "'");

          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // set TF_PROD_EPN.STATUS = 'E' for expired TF_BOM_ROUTE
  public static boolean setProdEpnExpire(Connection conn,
                                         String sid,
                                         String product_body,
                                         String brand) {

      StringBuffer sql = new StringBuffer();
      String productType = OiMaintainService.getProductType(conn, sid);
      try {
          if (productType.equals("NVM")) {
            sql.append(
        	  "UPDATE TF_PROD_EPN E SET STATUS = 'E' \n" +
              "WHERE EXISTS ( SELECT 1 FROM TF_BOM_ROUTE_TX B \n" +
              "WHERE B.PRODUCT_BODY = E.PRODUCT_BODY AND B.BRAND = E.BRAND \n" +
              "AND B.MASK_OPTION = E.MASK_OPTION AND B.BACKEND_OPTION = E.BACKEND_OPTION \n" +
              "AND B.PIN_COUNT = E.PIN_COUNT AND B.PACKAGE_TYPE = E.PACKAGE_TYPE \n" +
              "AND B.TAG = 2 \n" +
              "AND B.SID = " + sid + ") \n" +
              "AND NOT EXISTS ( SELECT 1 FROM TF_BOM_ROUTE_TX B \n" +
              "WHERE B.PRODUCT_BODY = E.PRODUCT_BODY AND B.BRAND = E.BRAND \n" +
              "AND B.MASK_OPTION = E.MASK_OPTION AND B.BACKEND_OPTION = E.BACKEND_OPTION \n" +
              "AND B.PIN_COUNT = E.PIN_COUNT AND B.PACKAGE_TYPE = E.PACKAGE_TYPE \n" +
              "AND B.TAG != 2 \n" +
              "AND B.SID = " + sid + ") \n" +
              "AND E.PRODUCT_BODY = '" + product_body + "' \n" +
              "AND E.BRAND = '" + brand + "' \n");
          } else if (productType.equals("XROM")){
            sql.append(
                  "UPDATE TF_PROD_EPN E SET STATUS = 'E' \n" +
                  "WHERE EXISTS ( SELECT 1 FROM TF_BOM_ROUTE_XROM_TX B \n" +
                  "WHERE B.PRODUCT_BODY = E.PRODUCT_BODY AND E.BRAND = 'MX' \n" +
                  "AND B.MASK_OPTION = E.MASK_OPTION \n" +
                  "AND B.PIN_COUNT = E.PIN_COUNT AND B.PACKAGE_CODE = E.PACKAGE_TYPE \n" +
                  "AND B.TAG = 2 \n" +
                  "AND B.SID = " + sid + ") \n" +
                  "AND NOT EXISTS ( SELECT 1 FROM TF_BOM_ROUTE_XROM_TX B \n" +
                  "WHERE B.PRODUCT_BODY = E.PRODUCT_BODY AND E.BRAND = 'MX' \n" +
                  "AND B.MASK_OPTION = E.MASK_OPTION \n" +
                  "AND B.PIN_COUNT = E.PIN_COUNT AND B.PACKAGE_CODE = E.PACKAGE_TYPE \n" +
                  "AND B.TAG != 2 \n" +
                  "AND B.SID = " + sid + ") \n" +
                  "AND E.PRODUCT_BODY = '" + product_body + "' \n");
          } else if (productType.equals("MROM")){
            sql.append(
                  "UPDATE TF_PROD_EPN E SET STATUS = 'E' \n" +
                  "WHERE EXISTS ( SELECT 1 FROM TF_BOM_ROUTE_MROM_TX B \n" +
                  "WHERE B.PRODUCT_BODY = E.PRODUCT_BODY \n" +
                  "AND B.MASK_OPTION = E.MASK_OPTION \n" +
                  "AND B.TAG = 2 \n" +
                  "AND B.SID = " + sid + " )\n" +
                  "AND NOT EXISTS ( SELECT 1 FROM TF_BOM_ROUTE_MROM_TX B \n" +
                  "WHERE B.PRODUCT_BODY = E.PRODUCT_BODY \n" +
                  "AND B.MASK_OPTION = E.MASK_OPTION \n" +
                  "AND B.TAG != 2 \n" +
                  "AND B.SID = " + sid + ") \n" +
                  "AND E.PRODUCT_BODY = '" + product_body + "' \n");
          }  else if (productType.equals("MMS")){
              sql.append(
                      "UPDATE TF_PROD_EPN E SET STATUS = 'E' \n" +
                      "WHERE EXISTS ( SELECT 1 FROM TF_BOM_ROUTE_MMS_TX B \n" +
                      "WHERE B.PRODUCT_BODY = E.PRODUCT_BODY \n" +
                      "AND B.MASK_OPTION = E.MASK_OPTION \n" +
                      "AND B.TAG = 2 \n" +
                      "AND B.SID = " + sid + " )\n" +
                      "AND NOT EXISTS ( SELECT 1 FROM TF_BOM_ROUTE_MMS_TX B \n" +
                      "WHERE B.PRODUCT_BODY = E.PRODUCT_BODY \n" +
                      "AND B.MASK_OPTION = E.MASK_OPTION \n" +
                      "AND B.TAG != 2 \n" +
                      "AND B.SID = " + sid + ") \n" +
                      "AND E.PRODUCT_BODY = '" + product_body + "' \n");
              }


          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // insert Yield Definition table from tf_yield_tx to tf_yield
  public static boolean Yield_insert(Connection conn,
                                  String sid,
                                  String product_body,
                                  String brand,
                                  String version) {
      StringBuffer sql = new StringBuffer();

      try {
          sql.append(
              "INSERT INTO tf_yield ( " +
              "SID, PRODUCT_BODY, BRAND, VERSION, YID, SEQ, PRODUCT_CODE, TEST_MODE, \n"+
              "AUTO_SHIP, HOLD_PE, HOLD_BIN, HOLD_BIN_CRI, AUTO_SCRAP, STOP, MRB, SAMPLING_YIELD, NOTES) \n" +
              "SELECT SID, PRODUCT_BODY, BRAND, VERSION, YID, SEQ, PRODUCT_CODE, TEST_MODE, \n"+
              "AUTO_SHIP, HOLD_PE, HOLD_BIN, HOLD_BIN_CRI, AUTO_SCRAP, STOP, MRB, SAMPLING_YIELD, NOTES \n" +
              "FROM tf_yield_tx where \n" +
              "product_body='" + product_body +
              "' and brand='" + brand +
              "' and version='" + version +
              "' and sid='" + sid + "'");

          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // insert Yield Definition table from tf_yield_definition_tx to tf_yield_definition
  public static boolean Yield_Def_insert(Connection conn,
                                  String sid) {
      StringBuffer sql = new StringBuffer();

      try {
          sql.append(
              "INSERT INTO tf_yield_definition ( " +
              "YID,SID,SEQ,FACILITY,PRODUCT_CODE,BRAND,TEST_MODE,LOWER_LIMIT,\n"+
              "FLAG1,UPPER_LIMIT,FLAG2,ITEM_TYPE,ITEM,ITEM_MODE2,ITEM_BINS2,\n"+
              "ACTION,CHANGE_IPN,ROUTE_NAME,START_STEP,REMARK,DG_ACTION,BY_LOT_DG,DGRADE_SPECIAL_IPN, SNOVA_ID, VERSION, DGRADEPRODCODE, GROUPITEMS_NO, FORM_FACTOR_NAME, MODULE_OPTION ) \n" +
              "SELECT YID,SID,SEQ,FACILITY,PRODUCT_CODE,BRAND,TEST_MODE,LOWER_LIMIT,\n"+
              "FLAG1,UPPER_LIMIT,FLAG2,ITEM_TYPE,ITEM,ITEM_MODE2,ITEM_BINS2,\n"+
              "ACTION,CHANGE_IPN,ROUTE_NAME,START_STEP,REMARK,DG_ACTION,BY_LOT_DG,DGRADE_SPECIAL_IPN, SNOVA_ID, VERSION, DGRADEPRODCODE, GROUPITEMS_NO, FORM_FACTOR_NAME, MODULE_OPTION  \n" +
              "FROM tf_yield_definition_tx where \n" +
              "sid=" + sid);

          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
      }
      catch (Exception ex) {
          DBConnection.rollback(conn);
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // insert Yield GroupItems table from tf_yield_groupitems_tx to tf_yield_groupitems
  public static boolean Yield_GroupItems_insert(Connection conn,
                                  String sid) {
      StringBuffer sql = new StringBuffer();

      try {
          sql.append("INSERT INTO tf_yield_groupitems ( ")
             .append("SID, TYPE, PRODUCT_CODE, PROD_LEVEL, TEST_MODE, VERSION, GROUP_NO, ITEM_NO, ITEM_TYPE, ITEM, RANGE_VALUES)\n")
             .append("SELECT SID, TYPE, PRODUCT_CODE, PROD_LEVEL, TEST_MODE, VERSION, GROUP_NO, ITEM_NO, ITEM_TYPE, ITEM, RANGE_VALUES\n")
             .append("FROM tf_yield_groupitems_tx where sid = ?");

          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.setObject(1, sid);
          ps.executeUpdate();
      }catch (Exception ex) {
          DBConnection.rollback(conn);
          TDSLogger.println(ex.getMessage());
          return false;
      }finally {
    	  
      }
      return true;
  }
  
  // insert WS Test Parameter from tf_test_parameter_ws_tx to tf_test_parameter_ws
  public static boolean WS_insert(Connection conn,
                                  String sid,
                                  String product_body,
                                  String brand,
                                  String version) {
      StringBuffer sql = new StringBuffer();

      try {
          sql.append(
              "INSERT INTO tf_test_parameter_ws ( " +
              "sid, pgm_id, product_body, brand, version, mask_option, test_type, tester, "+
              "site, program_name, tf_comment, temperature, hw_configure, pgm_special_control, one_main_pgm_group_version) " +
              "SELECT sid, pgm_id, product_body, brand, version, mask_option, test_type, tester, "+
              "site, program_name, tf_comment, temperature, hw_configure, pgm_special_control, one_main_pgm_group_version " +
              "FROM tf_test_parameter_ws_tx where " +
              "product_body='" + product_body +
              "' and brand='" + brand +
              "' and version='" + version +
              "' and sid='" + sid + "'");

          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // insert FT Test Parameter from tf_test_parameter_ft_tx to tf_test_parameter_ft
  public static boolean FT_insert(Connection conn,
                                  String sid,
                                  String product_body,
                                  String brand,
                                  String version) {
    StringBuffer sql = new StringBuffer();

    try {
        sql.append(
            "INSERT INTO tf_test_parameter_ft ( " +
            "sid, pgm_id, product_body, brand, version, backend_option, test_type, pin_count, "+
            "package_type, tester, site, program_name, i_grade, c_grade,w_grade,y_grade,j_grade,"
            + "k_grade,l_grade,n_grade,b_grade,e_grade, s_grade, tf_comment, body_size, actual_file, "
            + "pgm_special_control, hw_configure, one_main_pgm_group_version, form_factor_name, module_option) " +
            "SELECT sid, pgm_id, product_body, brand, version, backend_option, test_type, pin_count, "+
            "package_type, tester, site, program_name, i_grade, c_grade,w_grade,y_grade,j_grade,"
            + "k_grade,l_grade,n_grade,b_grade,e_grade, s_grade, tf_comment, body_size, actual_file, "
            + "pgm_special_control, hw_configure, one_main_pgm_group_version, form_factor_name, module_option " +
            "FROM tf_test_parameter_ft_tx where " +
            "product_body='" + product_body +
            "' and brand='" + brand +
            "' and version='" + version +
            "' and sid='" + sid + "'");

        PreparedStatement ps = conn.prepareStatement(sql.toString());
        ps.executeUpdate();
    }
    catch (Exception ex) {
        ex.printStackTrace();
        DBConnection.rollback(conn);
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
        return false;
    }
    finally {
    }
    return true;
  }

  // insert PBC_insert Test Parameter from tf_test_parameter_pbc_tx to tf_test_parameter_pbc
  public static boolean PBC_insert(Connection conn,
                                  String sid,
                                  String product_body,
                                  String brand,
                                  String version) {
    StringBuffer sql = new StringBuffer();

    try {
        sql.append(
            "INSERT INTO tf_test_parameter_pbc ( " +
            "sid, pgm_id, product_body, brand, version, backend_option, test_type, pin_count, "+
            "package_type, tester, site, program_name, i_grade, c_grade,w_grade,y_grade,j_grade,k_grade,l_grade,n_grade,b_grade,e_grade, s_grade, tf_comment, body_size, actual_file, hw_configure) " +
            "SELECT sid, pgm_id, product_body, brand, version, backend_option, test_type, pin_count, "+
            "package_type, tester, site, program_name, i_grade, c_grade,w_grade,y_grade,j_grade,k_grade,l_grade,n_grade,b_grade,e_grade, s_grade, tf_comment, body_size, actual_file, hw_configure " +
            "FROM tf_test_parameter_pbc_tx where " +
            "product_body='" + product_body +
            "' and brand='" + brand +
            "' and version='" + version +
            "' and sid='" + sid + "'");

        PreparedStatement ps = conn.prepareStatement(sql.toString());
        ps.executeUpdate();
    }
    catch (Exception ex) {
        ex.printStackTrace();
        DBConnection.rollback(conn);
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
        return false;
    }
    finally {
    }
    return true;
  }

// insert Main Route from tf_main_route_xrom_tx to tf_main_route_xrom
  public static boolean main_route_insert(Connection conn,
                                  String sid,
                                  String product_body,
                                  String version) {
    StringBuffer sql = new StringBuffer();

    try {
        sql.append(
            "INSERT INTO tf_main_route_xrom ( " +
            "sid, product_body, version, route_type, main_route, map_route, remark)  "+
            "SELECT sid, product_body, version, route_type, main_route, map_route, remark "+
            "FROM tf_main_route_xrom_tx where " +
            "product_body='" + product_body +
            "' and version='" + version +
            "' and sid='" + sid + "'");

        PreparedStatement ps = conn.prepareStatement(sql.toString());
        ps.executeUpdate();
    }
    catch (Exception ex) {
        ex.printStackTrace();
        DBConnection.rollback(conn);
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
        return false;
    }
    finally {
    }
    return true;
  }

  // insert badicdata
  public static boolean Basicdata_insert(Connection conn,
                                         String sid,
                                         String product_body,
                                         String brand,
                                         String version) {
    StringBuffer sql = new StringBuffer();

    try {
        sql.append(
            "INSERT INTO tf_basic_info ( " +
            "sid, product_body, brand, version, "+
            "tester, good_bin, ib_bin, fail_bin, remark, auto_ship_yield, stop_test_yield, " +
            "auto_scrap_yield, mrb_yield, sample_yield, options, grade, " +
            "bin_type, inkless_grade,ktd_bin_flag, ipn_action, epn_speed, test_speed, down_grade) " +
            "SELECT sid, product_body, brand, version, "+
            "tester, good_bin, ib_bin, fail_bin, remark, auto_ship_yield, stop_test_yield, " +
            "auto_scrap_yield, mrb_yield, sample_yield, options, grade, " +
            "bin_type, inkless_grade,ktd_bin_flag, ipn_action, epn_speed, test_speed, down_grade " +
            "FROM tf_basic_info_tx where " +
            "product_body='" + product_body +
            "' and brand='" + brand +
            "' and version='" + version +
            "' and sid='" + sid + "'");

        PreparedStatement ps = conn.prepareStatement(sql.toString());
        ps.executeUpdate();
        
        //MMS TF_BASIC_INFO_MMS
    	String cols = "SID,PRODUCT_BODY,BRAND,VERSION,TESTER,GOOD_BIN,FAIL_BIN,REMARK,AUTO_SHIP_YIELD,STOP_TEST_YIELD,AUTO_SCRAP_YIELD,MRB_YIELD,SAMPLE_YIELD,OPTIONS,GRADE,BIN_TYPE,IPN_ACTION,EPN_SPEED,TEST_SPEED,DOWN_GRADE,INKLESS_GRADE,IB_BIN,KTD_BIN_FLAG,FORM_FACTOR_NAME,MODULE_OPTION";
    	String sqlIns = "insert into TF_BASIC_INFO_MMS (" + cols + ") "
    			+ "select " + cols + " from TF_BASIC_INFO_MMS_TX "
    			+ " where product_body = '" + product_body
    			+ "' and brand = '" + brand
    			+ "' and version = '" + version 
    			+ "' and sid = " + sid;
    	ps = conn.prepareStatement(sqlIns);
    	ps.executeUpdate();

    }
    catch (Exception ex) {
        ex.printStackTrace();
        DBConnection.rollback(conn);
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
        return false;
    }
    finally {
    }
    return true;
  }

  // insert badicdata comments
  public static boolean BasicdataComment_insert(Connection conn, String sid)
  {
    StringBuffer sql = new StringBuffer();

    try {
        sql.append(
            "INSERT INTO TF_BASIC_INFO_COMMENT ( \n" +
            "sid, comments, comments2) \n" +
            "SELECT sid, comments, comments2 \n" +
            "FROM TF_BASIC_INFO_COMMENT_TX where " +
            "sid='" + sid + "'");

        PreparedStatement ps = conn.prepareStatement(sql.toString());
        ps.executeUpdate();
    }
    catch (Exception ex) {
        ex.printStackTrace();
        DBConnection.rollback(conn);
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
        return false;
    }
    finally {
    }
    return true;
  }

  public static boolean do_document_likage(Connection conn,
                               String sid,
                               String product_body,
                               String brand,
                               String version) {

      StringBuffer sql = new StringBuffer();
      String namelist;
      ArrayList releaed_file_list = null;
      ArrayList removed_file_list = null;

      try {
          // Steps :
          // 1. 找出要被刪除的檔案 list
          //    docType+seq 同時存在 tag=0 and teg=1 的資料
          // 2. 將要的資料由 tf_document_linkage_tx copy 至 tf_document_linkage
          //    本次被 update 不置入
          // 3. 刪除 tf_document_linkage_tx 的資料
          // 4. 將檔案 copy 至 release 目錄
          // 5. 將檔案自暫存區移除
/* array list usage
          ArrayList list = new ArrayList();

          String val;
          Iterator i = _data.values().iterator();
          while (i.hasNext())
          {
          val = (String) i.next();
          list.add (val);
          }
          Collection copyList = list;
          ArrayList c = new ArrayList(copyList);

          return c;
          }
*/
          // 1. 找出要被刪除的檔案 list
          sql.append(
              "select file_name from tf_document_linkage_tx a " +
              "where tag = 0 " +
              "and exists (select 1 from tf_document_linkage_tx b " +
              "  where a.sid = b.sid " +
              "    and a.doc_type = b.doc_type " +
              "    and a.seq = b.seq " +
              "    and b.tag = 1) " +
              "and sid = " + sid);

          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ResultSet rs = ps.executeQuery();
          while (rs.next()) {
              if (removed_file_list == null)
                  removed_file_list = new ArrayList();
              removed_file_list.add(rs.getString("file_name"));
//          boolean success = (new File(path + "/File/" +
//                                      rs1.getString("file_name"))).delete();
          }
          rs = null;
          ps = null;

          sql.delete(0,sql.length());
          sql.append(
              "delete from tf_document_linkage_tx a " +
              "where tag = 0 " +
              "and exists (select 1 from tf_document_linkage_tx b " +
              "  where a.sid = b.sid " +
              "    and a.doc_type = b.doc_type " +
              "    and a.seq = b.seq " +
              "    and b.tag = 1) " +
              "and sid = " + sid);

          ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
          ps = null;

          // 2. 將要的資料由 tf_document_linkage_tx copy 至 tf_document_linkage
          sql.delete(0,sql.length());
          sql.append(
              "INSERT INTO tf_document_linkage ( " +
              "sid, product_body, brand, version, "+
              "doc_type, seq, doc_name, file_name, tf_comment) " +
              "SELECT sid, product_body, brand, version, "+
              "doc_type, seq, doc_name, " +
              "product_body||'_'||brand||'_'||version||'_'||doc_type||'_'||trim(to_char(seq,'000'))||'.png', " +
              "tf_comment " +
              "FROM tf_document_linkage_tx where " +
              "product_body='" + product_body +
              "' and brand='" + brand +
              "' and version='" + version +
              "' and sid='" + sid + "'");

          ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
          ps = null;

          // 3. 刪除 tf_document_linkage_tx 的資料
          sql.delete(0,sql.length());
          sql.append(
              "select file_name, " +
              "product_body||'_'||brand||'_'||version||'_'||doc_type||'_'||trim(to_char(seq,'000'))||'.png' target_name " +
              "from tf_document_linkage_tx " +
              "where sid = " + sid);

          ps = conn.prepareStatement(sql.toString());
          rs = ps.executeQuery();
          while (rs.next()) {
              if (releaed_file_list == null)
                  releaed_file_list = new ArrayList();
              releaed_file_list.add(rs.getString("file_name")+","+rs.getString("target_name"));
          }
          rs = null;
          ps = null;

          if (!delete_data(conn, "tf_document_linkage_tx", sid, product_body, brand, version))
              return false;

          // 4. 將檔案 copy 至 release 目錄
          TDSProperties pdfProp = TDSResource.getProperties("TIMPdf");
          String releasePath = pdfProp.getValue("jpg.path") + File.separator;
          String txPath = pdfProp.getValue("jpg_tx.path") + File.separator;
          if (releaed_file_list != null){
              Iterator i = releaed_file_list.iterator();
              while (i.hasNext()){
                  namelist = (String) i.next();
                  String[] filenames = namelist.split(",");
                  File file2 = new File(txPath + filenames[0]);
                  File file3 = new File(releasePath + filenames[1]);
                  if (!copy(file2, file3))
                      return false;
                  file2 = null;
                  file3 = null;
              }
          }

          // 5. 將檔案自暫存區移除
          String file = null;
          try {
            if (releaed_file_list != null){
                  Iterator i = releaed_file_list.iterator();
                  while (i.hasNext()){
                      namelist = (String) i.next();
                      String[] filenames = namelist.split(",");
                      file = txPath + filenames[0];
                      File file2 = new File(file);
                      file2.delete();
                      file2 = null;
                   }
            }

            if (removed_file_list != null){
                Iterator i = removed_file_list.iterator();
                while (i.hasNext()){
                    namelist = (String) i.next();
                    String[] filenames = namelist.split(",");
                    file = txPath + filenames[0];
                    File file2 = new File(file);
                    file2.delete();
                    file2 = null;
                 }
             }
        } catch (Exception e) {
                        TDSLogger.println("WARNING: delete " + file + " fail : " + e);
	}
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // insert badicdata
  public static boolean Wip_insert(Connection conn, String sid) {
    StringBuffer sql = new StringBuffer();

    try {
        sql.append(
            "INSERT INTO tf_wip ( sid, optionlist, ctrl_type, pgname1, \n"+
            "pgname2, pgname3, pgname4, pgname5, p1, p2, p3, p4, p5 ) \n" +
            "SELECT sid, optionlist, ctrl_type, pgname1, pgname2, pgname3, pgname4, pgname5, p1, p2, p3, p4, p5 \n"+
            "FROM tf_wip_tx where sid=" + sid );

        PreparedStatement ps = conn.prepareStatement(sql.toString());
        ps.executeUpdate();
    }
    catch (Exception ex) {
        ex.printStackTrace();
        DBConnection.rollback(conn);
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
        return false;
    }
    finally {
    }
    return true;
  }

  // Set OI to releas status
  public static boolean coverpage_insert(Connection conn,
                                         String product_body,
                                         String brand,
                                         String version,
                                         String caseno) {

      String InsSQL = null;
      String docNo = null;
      int status = 0;
      if (brand.equals("KH"))
    	  docNo = "8049K-" + product_body;
      else
    	  docNo = "8049-" + product_body;
      try {
          InsSQL = "insert into tf_coverpage (product_body,brand,version,applicant,approve_no,approve_date,remark,sponsor,replace_tecr_no) " +
              "select '" + product_body + "','" + brand + "'," + version +
              ",applicant,caseno,effectivedate,changedesc,sponsor,replace_tecr_no from if_tf_coverpage where " +
              "caseno = '" + caseno + "' and docno = '" + docNo + "' and rev = '" + version + "' " +
              "and status != 'EPC'";
          PreparedStatement ps = conn.prepareStatement(InsSQL);
          status = ps.executeUpdate();
      }
      catch (Exception ex) {
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
   	  return (status > 0);
  }

  // Set OI to desired status
  public static boolean set_oi_status(Connection conn,
                                 String sid,
                                 String status) {

      String InsSQL1 = null;
      String InsertSQL = null;
      try {
          InsSQL1 = "update tf_information set status = ? where sid = ? ";
          PreparedStatement ps = conn.prepareStatement(InsSQL1);
          ps.setString(1, status);
          ps.setInt(2, Integer.parseInt(sid));
          //TDSLogger.println(ps.toString());
          ps.executeUpdate();
      }
      catch (Exception ex) {
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  public static boolean copy(File src, File dst) {

      String seq_str = "";
      String file_name = "";
      try {
          InputStream in = new FileInputStream(src);
          OutputStream out = new FileOutputStream(dst);
          // Transfer bytes from in to out
          byte[] buf = new byte[1024];
          int len;
          while ( (len = in.read(buf)) > 0) {
              out.write(buf, 0, len);
          }
          in.close();
          out.close();
      }
      catch (Exception ex) {
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // Move the PDF to release directory
  public static boolean move_original_pdf(String product_body,
                                          String brand,
                                          String version,
                                          String path) {

      String org_pdffile = product_body+brand+"V"+version+"_TX.pdf";
      String dest_pdffile = product_body+brand+"V"+version+".pdf";

      File file1 = new File(path + "/File/" + org_pdffile);
      File file2 = new File(path + "/Release/" + dest_pdffile);
      if (!copy(file1, file2))
          return false;

      org_pdffile = product_body+brand+"V"+version+"Difference_TX.pdf";
      dest_pdffile = product_body+brand+"V"+version+"Difference.pdf";
      file1 = new File(path + "/File/" + org_pdffile);
      file2 = new File(path + "/Release/" + dest_pdffile);
      if (!copy(file1, file2))
          return false;

      return true;
  }


	public static boolean ftp_to_dcc(String product_body, String brand, String version, String tmpmaillist) {
		boolean flag = true;
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			flag = ftp_to_dcc(conn, product_body, brand, version, tmpmaillist);
		} catch (Exception ex) {
			ex.printStackTrace();
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return false;
		} finally {
			DBConnection.close(conn);
		}
		return flag;

	}

  public static boolean ftp_to_dcc(Connection conn, String product_body,
                                   String brand,
                                   String version,
                                   String tmpmaillist) {
    String release_path = TDSResource.getProperties("TIMPdf").getValue("pdf.dir");
    String script_name = TDSResource.getProperties("TIMPdf").getValue("pdf_script");
    boolean flag = true;

    TFCoverPageBean coverPage = EifOIReleaseService.getCoverPage(conn, product_body, brand, Integer.parseInt(version));
    String mailList = null;
    if(coverPage == null)
        return false;
    if (coverPage.getSponsor() != null) {
    	mailList = coverPage.getSponsor() + "@mxic.com.tw";//twsmtp01.mxic.com.tw
    } else
    	mailList = "";
    if(tmpmaillist != null)
        mailList = tmpmaillist + "@mxic.com.tw";
    String command = script_name + " " + release_path + " " + product_body + " " + brand + " " + version + " " + mailList;
    TDSLogger.println(command);
    flag = Exec(command);

    return flag;
  }

  public static boolean Exec(String command){
    try {
      int returnValue;
      StringBuffer bf1 = new StringBuffer();
      StringBuffer bf2 = new StringBuffer();

      SafeExec se = new SafeExec(command);
      returnValue = se.perform(bf1, bf2);
      if (returnValue == 0){
        return true;
      } else {
        return false;
      }
    } catch (Exception e){
    	TDSLogger.println(e);
      return false;
    }
  }

  public static boolean deletePDFFile(Connection conn, String product_body, String brand, String version) {

	  String oiFile = pdfService.getFileName(conn, product_body, brand, version, null, false, "_tx");
	  String coverPageFile = pdfService.getFileName(conn, product_body, brand, version, null, true, "_tx");
      String txPath = TDSResource.getProperties("TIMPdf").getValue("pdf.dir") + File.separator;

	  try {
		File file = new File(txPath + oiFile);
		file.delete();
		file = new File(txPath + coverPageFile);
        file.delete();
	  }
	  catch (Exception ex) {
		TDSLogger.println(ex.getMessage());
		//return false;
	  }
	  return true;
  }
  
  public static ArrayList<HashMap<String,String>> getEffectMcpList(Connection conn, String scpSid, String productBody){
	  String sql = 
			  "select a1.* from tf_bom_route a , tf_bom_route_mcp a1, tf_cosign_version_vw b \n"+//tf_current_version_vw
			  "where a.sid = " + scpSid + " \n"+
			  "and a.tag=2 \n"+
			  "and a1.sid = b.sid \n"+
			  "and a.sort_route_code = a1.sort_route_code \n"+
			  "and a.product_body = a1.com_prod_body \n"+
			  "and a.brand = a1.brand \n"+
			  "and a.mask_option = a1.com_mask_option \n";
		  /*20160318"select a1.*\n" +
		  "from (select a.*\n" + 
		  "  from tf_bom_route_mcp a, tf_current_version_vw b\n" + 
		  "  where a.product_body = b.product_body and a.brand = b.brand and a.version = b.version and a.tag !=2\n" + 
		  "  ) a1,\n" + 
		  "  (select a.product_body, a.mask_option, a.sort_route_code,\n" + 
		  "          a.ws_route, a.wsspecialcontrol, nvl(a.ws_route_add, '') ws_route_add, a.tf_ws_comment\n" + 
		  "  from tf_bom_route a\n" + 
		  "  where a.sid = " + scpSid + " and a.mcp_flag in ('MCP', 'SCP_MCP')\n" + 
		  "  minus\n" + 
		  "  select a.com_prod_body, a.com_mask_option, a.sort_route_code,\n" + 
		  "         a.ws_route, a.wsspecialcontrol, nvl(a.ws_route_add, '') ws_route_add, a.tf_ws_comment\n" + 
		  "  from tf_bom_route_mcp a, tf_current_version_vw b\n" + 
		  "  where a.product_body = b.product_body and a.brand = b.brand and a.version = b.version and a.tag !=2\n" + 
		  "     and a.sort_route_code is not null\n" +
		  "  union\n" +
		  "  select a.com_prod_body, a.com_mask_option, a.sort_route_code,\n" + 
		  "         a.ws_route, a.wsspecialcontrol, a.ws_route_add, a.tf_ws_comment\n" + 
		  "  from tf_bom_route_mcp a, tf_current_version_vw b\n" + 
		  "  where a.product_body = b.product_body and a.brand = b.brand and a.version = b.version and a.tag !=2\n" + 
		  "    and a.com_prod_body = '" + productBody + "'\n" + 
		  "    and not exists (\n" + 
		  "			select a1.product_body, a1.mask_option, a1.sort_route_code\n" + 
		  "			from tf_bom_route a1\n" + 
		  "			where a1.sid = " + scpSid + " and a1.mcp_flag in ('MCP', 'SCP_MCP')\n" + 
		  "			  and a1.product_body = a.com_prod_body and a1.mask_option = a.com_mask_option\n" + 
		  "			  and a1.sort_route_code = a.sort_route_code\n" + 
		  "			)) b1\n" +		  
		  "where a1.com_prod_body = b1.product_body and a1.com_mask_option = b1.mask_option\n" + 
		  "    and a1.sort_route_code = b1.sort_route_code";
		  */
		 /* 
		  "select a.product_body, a.mask_option, a.sort_route_code,\n" +
		  "a.ws_route, a.wsspecialcontrol, nvl(a.ws_route_add, '') ws_route_add, a.tf_ws_comment\n" + 
		  "from tf_bom_route a\n" + 
		  "where a.sid = " + scpSid + " and a.mcp_flag in ('MCP', 'SCP_MCP')\n" + 
		  "minus\n" + 
		  "select a.com_prod_body, a.com_mask_option, a.sort_route_code,\n" + 
		  "a.ws_route, a.wsspecialcontrol, nvl(a.ws_route_add, 'a') ws_route_add, a.tf_ws_comment\n" + 
		  "from tf_bom_route_mcp_tx a, tf_current_version_vw b\n" + 
		  "where a.product_body = b.product_body and a.brand = b.brand and a.version = b.version\n" + 
		  "   and a.sort_route_code is not null";
		*/
			  
			  
	  ArrayList<HashMap<String,String>> data=new ArrayList<HashMap<String,String>>();
	  //Connection con = null;
	  try {
		 // con = DBConnection.getConnection();
		  Statement st=conn.createStatement();
		  ResultSet rs=st.executeQuery(sql);
	        
		  while(rs.next()){
			  HashMap<String,String> hm = new HashMap<String,String>();
			  hm.put("SID", rs.getString("SID"));
			  hm.put("PRODUCT_BODY", rs.getString("PRODUCT_BODY"));
			  hm.put("BRAND", rs.getString("BRAND"));
			  hm.put("BACKEND_OPTION", rs.getString("BACKEND_OPTION"));
			  hm.put("COM_PROD_BODY", rs.getString("COM_PROD_BODY"));
			  hm.put("COM_MASK_OPTION", rs.getString("COM_MASK_OPTION"));			  
			  //hm.put("SORT_ROUTE_CODE", rs.getString("SORT_ROUTE_CODE")==null?"":rs.getString("SORT_ROUTE_CODE"));
			  hm.put("WS_ROUTE", rs.getString("WS_ROUTE")==null?"":rs.getString("WS_ROUTE"));
			  data.add(hm);
		  }
		  if(st!=null)
			  st.close();
		  if(rs!=null)
			  rs.close();
	  }	catch (Exception ex) {
		  TDSLogger.println(ex);
	  } finally {
		 // DBConnection.close(con);
	  }
	  return data;
  }  
  // move bom vs route from tf_bom_route_tx to tf_bom_route
  public static boolean bom_route_mms_insert(Connection conn,
                                      String sid,
                                      String product_body,
                                      String brand,
                                      String version) {

      StringBuffer sql = new StringBuffer();
      try {
          sql.append(
              "INSERT INTO tf_bom_route_mms ( " +
              "sid, product_body, brand, version, backend_option, fg_with_code, pin_count, "+
              "package_type, ft_route, ft_route_add, ft_route_add2, ft_route_add3, ft_route_code, tf_comment, " +
              "mask_option, sort_route_code, db_with_code, ws_route, ws_route_add, tf_ws_comment, sales_form, " +
              "tag, endurance, wsspecialcontrol,quality_level, quality_level_comment, mcp_flag, form_factor_name, module_option) " +
              "SELECT DISTINCT sid, product_body, brand, version, backend_option, fg_with_code, pin_count, "+//20180718-LAI
              "package_type, ft_route, ft_route_add, ft_route_add2, ft_route_add3, ft_route_code, tf_comment, " +
              "mask_option, sort_route_code, db_with_code, ws_route, ws_route_add, tf_ws_comment, sales_form, " +
              "decode(tag, '2', '2', '0'), endurance, wsspecialcontrol,quality_level,quality_level_comment, mcp_flag " +
              ", form_factor_name, module_option " +
              "FROM tf_bom_route_mms_tx where " +
              "product_body='" + product_body +
              "' and brand='" + brand +
              "' and version='" + version +
              "' and sid='" + sid + "'");

          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

}
