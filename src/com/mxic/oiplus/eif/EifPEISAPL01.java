/******************************************************************************************************/
// Author  : Robin Mao
// Date	   : November 14, 2006.
// Purpose : AP_APL 的資料來源
// Flow    : 0. 本 interface 將使用 tim 帳號至 database 存取資料
//           1. 由 tds.pg_test_program 及 tds.pg_plant_release 取得資料
//           2. 過濾條件，以 tds.pg_test_program.approve_date 及 tds.pg_plant_release.release_date
//              位於某時間內的資料
//           3. 取得的資料將 insert into tim.ap_apl
//           2007/04/10 modified for ignore Body Size from TF_EPN_PROD
/******************************************************************************************************/
package com.mxic.oiplus.eif;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.StringUtil;

import com.mxic.oiplus.util.*;

public class EifPEISAPL01 extends SQLStatement
{
  private ResultSet rs = null;
  private static String eifName = "EifAPLSAP01";
//  private static String interval = "1440*365*15";

  public EifPEISAPL01(){
  }

  public void processPEISAPL01()
  {
    Connection conn = null;
    try
    {
      conn = DBConnection.getConnection();
      // get avaliable PIM list
      selectPIMInfo(conn);
      modifyAPL(conn);
    } catch (Exception e){
      TDSLogger.println(e);
    } finally {
      try {
        DBConnection.close(conn);
        conn = null;
      } catch (Exception e){
        TDSLogger.println(e);
      }
    }
  }

  // 取得某一時間區間內 Release 的 PGM 資料
  private void selectPIMInfo(Connection conn)
  {
    boolean result = true;
    String interval = (String) TDSResource.getProperties("EIF").get("from.peis.pim.interval");
    try
    {
      String sql =
          // WS by pg_test_program.LAST_RELEASE_DATE
          "select /*+index (pk_ba_product p)*/ distinct a.process_type,a.product_body,a.mask_option options,'W' pkg_type,'000' pin_count, \n"+
          "'NA' body_size,a.test_mode,a.tester_type,a.LAST_RELEASE_DATE,a.sap_plant_no,a.apl_status,a.apl_no,to_char(a.max_site ) site \n"+
          "from (select distinct 'WS' process_type,\n"+
          "substr(a.product_code,1,4) product_body,\n"+
          "substr(a.product_code,5,1) mask_option,\n"+
          "c.prm2_code pkg_type,\n"+
          "trim(to_char(a.pin_count,'009')) pin_count,\n"+
          "a.test_mode, \n"+
          "a.tester_type, \n"+
          "a.LAST_RELEASE_DATE, \n"+
          "b.sap_plant_no, \n"+
          "'W' apl_status, \n"+
          "null apl_no, \n"+
          "a.max_site \n"+
          "from pg_test_program a, pg_plant_release d, ba_plant b, ba_package_type c \n"+
          "where a.sid= d.pg_sid \n"+
          "and a.program_mode = 'PROD' \n"+
          "and a.program_status in (54,64) \n"+
          "and d.plant_no = b.plant_no \n"+
          "and a.package_type = c.package_type \n"+
          "and a.test_mode like 'S%' \n"+
          "and b.pgm_flag = 1 \n"+
          "and a.LAST_RELEASE_DATE > sysdate-\n"+interval+"/1440 " +
          "and b.sap_plant_no is not null) a, ba_product p \n"+
          "where a.product_body||a.mask_option = p.product_code \n"+
          "and p.facility = 0 \n"+
          "and p.product_type in ('flash','rom','eprom') \n"+
          "union \n"+
          // FT by pg_test_program.aproval_date
          "select distinct a.process_type,a.product_body,a.backend_option options,a.pkg_type,a.pin_count, \n"+
          "'NA' body_size,a.test_mode,a.tester_type,a.LAST_RELEASE_DATE,a.sap_plant_no,a.apl_status,a.apl_no, 'NA' site \n"+
          "from (select distinct 'FT' process_type,\n"+
          "substr(a.product_code,1,4) product_body,\n"+
          "substr(a.product_code,5,1) backend_option,\n"+
          "c.prm2_code pkg_type,\n"+
          "trim(to_char(a.pin_count,'009')) pin_count,\n"+
          "a.test_mode, \n"+
          "a.tester_type, \n"+
          "a.LAST_RELEASE_DATE, \n"+
          "b.sap_plant_no, \n"+
          "'W' apl_status, \n"+
          "null apl_no \n"+
          "from pg_test_program a, pg_plant_release d, ba_plant b, ba_package_type c \n"+
          "where a.sid= d.pg_sid \n"+
          "and a.program_mode = 'PROD' \n"+
          "and a.program_status in (54,64) \n"+
          "and d.plant_no = b.plant_no \n"+
          "and a.package_type = c.package_type \n"+
          "and a.test_mode like 'F%' \n"+
          "and b.pgm_flag = 1 \n"+
          "and a.LAST_RELEASE_DATE > sysdate-\n"+interval+"/1440 " +
          "and b.sap_plant_no is not null) a, ba_product p \n"+
          "where a.product_body||a.backend_option = p.product_code \n"+
          "and p.facility = 1 \n"+
          "and p.product_type in ('flash','rom','eprom') \n"+
          "union \n"+
          // WS by pg_plant_release.release_date
          "select /*+index (pk_ba_product p)*/ distinct a.process_type,a.product_body,a.mask_option options,'W' pkg_type,'000' pin_count, \n"+
          "'NA' body_size,a.test_mode,a.tester_type,a.LAST_RELEASE_DATE,a.sap_plant_no,a.apl_status,a.apl_no,to_char(a.max_site ) site \n"+
          "from (select distinct 'WS' process_type,\n"+
          "substr(a.product_code,1,4) product_body,\n"+
          "substr(a.product_code,5,1) mask_option,\n"+
          "c.prm2_code pkg_type,\n"+
          "trim(to_char(a.pin_count,'009')) pin_count,\n"+
          "a.test_mode, \n"+
          "a.tester_type, \n"+
          "a.LAST_RELEASE_DATE, \n"+
          "b.sap_plant_no, \n"+
          "'W' apl_status, \n"+
          "null apl_no, \n"+
          "a.max_site \n"+
          "from pg_test_program a, pg_plant_release d, ba_plant b, ba_package_type c \n"+
          "where a.sid= d.pg_sid \n"+
          "and a.program_mode = 'PROD' \n"+
          "and a.program_status in (54,64) \n"+
          "and d.plant_no = b.plant_no \n"+
          "and a.package_type = c.package_type \n"+
          "and a.test_mode like 'S%' \n"+
          "and b.pgm_flag = 1 \n"+
          "and d.release_date > sysdate-\n"+interval+"/1440 " +
          "and b.sap_plant_no is not null) a, ba_product p \n"+
          "where a.product_body||a.mask_option = p.product_code \n"+
          "and p.facility = 0 \n"+
          "and p.product_type in ('flash','rom','eprom') \n"+
          "union \n"+
          // FT by pg_plant_release.release_date
          "select distinct a.process_type,a.product_body,a.backend_option options,a.pkg_type,a.pin_count, \n"+
          "'NA' body_size,a.test_mode,a.tester_type,a.LAST_RELEASE_DATE,a.sap_plant_no,a.apl_status,a.apl_no,'NA' site \n"+
          "from (select distinct 'FT' process_type,\n"+
          "substr(a.product_code,1,4) product_body,\n"+
          "substr(a.product_code,5,1) backend_option,\n"+
          "c.prm2_code pkg_type,\n"+
          "trim(to_char(a.pin_count,'009')) pin_count,\n"+
          "a.test_mode, \n"+
          "a.tester_type, \n"+
          "a.LAST_RELEASE_DATE, \n"+
          "b.sap_plant_no, \n"+
          "'W' apl_status, \n"+
          "null apl_no \n"+
          "from pg_test_program a, pg_plant_release d, ba_plant b, ba_package_type c \n"+
          "where a.sid= d.pg_sid \n"+
          "and a.program_mode = 'PROD' \n"+
          "and a.program_status in (54,64) \n"+
          "and d.plant_no = b.plant_no \n"+
          "and a.package_type = c.package_type \n"+
          "and a.test_mode like 'F%' \n"+
          "and b.pgm_flag = 1 \n"+
          "and d.release_date > sysdate-\n"+interval+"/1440 " +
          "and b.sap_plant_no is not null) a, ba_product p \n"+
          "where a.product_body||a.backend_option = p.product_code \n"+
          "and p.facility = 1 \n"+
          "and p.product_type in ('flash','rom','eprom') \n"+
          "union \n"+
          // for program not in PIM system
          "select distinct 'FT' process_type, \n"+
          "a.product_body, \n"+
          "backend_option, \n"+
          "c.prm2_code, \n"+
          "trim(to_char(d.pin_count,'009')) pin_count, \n"+
          "d.body_size, \n"+
          "d.test_type,  \n"+
          "d.tester,  \n"+
          "sysdate,  \n"+
          "b.sap_plant_no,  \n"+
          "'W' apl_status,  \n"+
          "null apl_no,  \n"+
          "'NA' site  \n"+
          "from tf_information a, tf_test_parameter_pbc d, ba_plant b, ba_package_type c  \n"+
          "where a.sid = d.sid and a.status = 'R'\n"+
          "and d.site = b.plant_name \n"+
          "and d.package_type = c.package_type  \n"+
          "and d.test_type like 'F%'  \n"+
          "and b.sap_plant_no is not null \n"+
          "and a.log_time > sysdate-"+interval+"/1440\n"+
          "union \n"+
          "select distinct 'WS', \n"+ 
          "w.product_body, \n"+ 
          "w.backend_option,  \n"+
          "'W',  \n"+
          "'000', \n"+
          "'NA', \n"+
          "w.test_type, \n"+
          "w.tester, \n"+
          "sysdate, \n"+
          "b.sap_plant_no, \n"+
          "'W', \n"+
          "null, \n"+
          "'NA' site  \n"+
          "from tf_test_parameter_pbc w, ba_plant b, tf_information i \n"+
          "where i.status = 'R' and w.site = b.plant_name \n"+
          "and w.sid = i.sid \n" +
          "and i.log_time > sysdate-\n"+interval+"/1440 \n" +
          "and w.test_type like 'S%'";
      TDSLogger.println(sql);
      PreparedStatement ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();

    } catch (Exception e) {
    	TDSLogger.println(e);
    } finally {
    }
  }

  // 將資料置入 AP_APL 中
  private void modifyAPL(Connection conn)
  {
    int i = 0;
    int j = 0;
    int result = 0;
    PreparedStatement pstmt = null;

    try
    {
      TDSLogger.println("--START UPDATE PGM TO APL!!!");
      String sql = "";
      while (rs.next()) {
        if (alreadyExists(conn)) {
          if (updateAPL(conn))
            j++;
        }
        else {
          if (insertAPL(conn))
            i++;
        }
      }
      TDSLogger.println("--SUCCESS insert " + i + " and check " + j + " Apls !!!");
    } catch (Exception e) {
      TDSLogger.println(e);
    } finally {
      try{
      }catch(Exception ex){
        TDSLogger.println(ex);
      }
    }
  }

  private boolean insertAPL(Connection conn)
  {
    int result = 0;
    PreparedStatement pstmt = null;

    try
    {
      TDSLogger.println("--START INSERT PGM TO APL!!!");
      String sql = "";
      sql = "insert into ap_apl select apl_seq.nextval,'" +
          rs.getString("PROCESS_TYPE") + "','" +
          rs.getString("PRODUCT_BODY") + "','" +
          rs.getString("OPTIONS") + "','" +
          rs.getString("PKG_TYPE") + "','" +
          rs.getString("PIN_COUNT") + "',";
      if (StringUtil.formatNull(rs.getString("BODY_SIZE")).equals(""))
        sql = sql + "null";
      else
        sql = sql + "'" + rs.getString("BODY_SIZE") + "'";
      sql = sql + ",'" +
          rs.getString("TEST_MODE") + "','" +
          rs.getString("TESTER_TYPE") + "','" +
          rs.getString("SAP_PLANT_NO") + "','" +
          rs.getString("APL_STATUS") + "'," +
          "null, sysdate, TO_DATE(substr('" +
          rs.getString("LAST_RELEASE_DATE") +
          "',1,19),'yyyy/mm/dd hh24:mi:ss'), ";
      if (StringUtil.formatNull(rs.getString("SITE")).equals(""))
          sql = sql + "'NA',";
      else
          sql = sql + "'" + rs.getString("SITE") + "',";    
      sql = sql + "'NA', 'NA', 'NA', 'NA', '' " +
          "from dual";
      pstmt = conn.prepareStatement(sql);
      result = pstmt.executeUpdate();
      if (result == 1) {
        TDSLogger.println("--SUCCESS " + sql);
      }
      pstmt.close();
    } catch (Exception e) {
      TDSLogger.println(e);
      return false;
    } finally {
      pstmt = null;
    }
    return true;
  }

  // update APL if PIM release date is more recently
  private boolean updateAPL(Connection conn)
  {
    int result = 0;
    PreparedStatement pstmt = null;

    try
    {
      TDSLogger.println("--START UPDATE PGM TO APL!!!");
      String sql = "";
      sql = "UPDATE ap_apl SET PIM_RELEASEDATE = " +
          "TO_DATE(substr('" + rs.getString("LAST_RELEASE_DATE") +
          "',1,19),'yyyy/mm/dd hh24:mi:ss') WHERE " +
          "PROCESS_TYPE = '" + rs.getString("PROCESS_TYPE") +
          "' AND PRODUCT_BODY = '" + rs.getString("PRODUCT_BODY") +
          "' AND OPTIONS = '" + rs.getString("OPTIONS") +
          "' AND PACKAGE_TYPE = '" + rs.getString("PKG_TYPE") +
          "' AND PIN_COUNT = '" + rs.getString("PIN_COUNT") +
          "' AND NVL(BODY_SIZE,'null') = '" + rs.getString("BODY_SIZE") +
          "' AND NVL(SITE,'null') = '" + rs.getString("SITE") +
          "' AND TEST_MODE = '" + rs.getString("TEST_MODE") +
          "' AND TESTER_TYPE = '" + rs.getString("TESTER_TYPE") +
          "' AND TESTER_TYPE = '" + rs.getString("TESTER_TYPE") +
          "' AND PIM_RELEASEDATE < TO_DATE(substr('" +
          rs.getString("LAST_RELEASE_DATE") + "',1,19),'yyyy/mm/dd hh24:mi:ss') ";

      pstmt = conn.prepareStatement(sql);
      result = pstmt.executeUpdate();
      if (result == 1) {
        TDSLogger.println("--SUCCESS " + sql);
      }
      pstmt.close();
    } catch (Exception e) {
      TDSLogger.println(e);
      return false;
    } finally {
      pstmt = null;
    }
    return true;
  }

  // 將資料置入 AP_APL 前，先 check 是否已有此筆資料
  private boolean alreadyExists(Connection conn)
  {
    boolean result = false;
    PreparedStatement pstmt = null;
    int cnt = 0;

    try {
      String sql =
          "select count(*) CNT from ap_apl where " +
          "PROCESS_TYPE = '" + rs.getString("PROCESS_TYPE") +
          "' AND PRODUCT_BODY = '" + rs.getString("PRODUCT_BODY") +
          "' AND OPTIONS = '" + rs.getString("OPTIONS") +
          "' AND PACKAGE_TYPE = '" + rs.getString("PKG_TYPE") +
          "' AND PIN_COUNT = '" + rs.getString("PIN_COUNT") +
          //"' AND NVL(BODY_SIZE,'null') = '" + rs.getString("BODY_SIZE") +
          //"' AND NVL(SITE,'null') = '" + rs.getString("SITE") +
          "' AND TEST_MODE = '" + rs.getString("TEST_MODE") +
          "' AND TESTER_TYPE = '" + rs.getString("TESTER_TYPE") +
          "' AND VENDOR_NO = '" + rs.getString("SAP_PLANT_NO") + "'";
      pstmt = conn.prepareStatement(sql);
      ResultSet q = pstmt.executeQuery();
      while (q.next()) {
        cnt = Integer.parseInt(q.getString("CNT"));
        break;
      }
      if (cnt > 0) {
        result = true;
        TDSLogger.println("Duplicate " + sql);
      }
      pstmt.close();

    } catch (Exception ex) {
      TDSLogger.println(ex);
    } finally {
    }
    return result;
  }
  // Main procedure
  public static void main(String args[])
  {
    EifPEISAPL01 apl = new EifPEISAPL01();

    try	{
      apl.processPEISAPL01();
    } catch(Exception e) {
      TDSLogger.println(e);
    }
  }
}