/******************************************************************************************************/
// Author  : Robin Mao
// Date	   : Feburary 12, 2007.
// Purpose : AP_APL 的資料來源 (NVM 資料來自 SEM，ASM 請參考 EifPEISAPL01.java
// Flow    : 0. 本 interface 將使用 tds 帳號至 database 存取資料
//           1. 由 tds.tf_test_parameter_ws, tds.tf_test_parameter_ft 及 tds.tf_test_parameter_pbc 取得資料
//           2. 過濾條件，以 tf_information.tatus = 'R' & tf_information.log_time 位於某時間內的資料
//           3. 取得的資料將 insert into tim.ap_apl
//              - 若 APL 已存在，update pim_releasedate only
/******************************************************************************************************/
package com.mxic.oiplus.eif;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import com.mxic.oiplus.util.*;
import com.mxic.oiplus.au.User;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.oimaintain.OiMaintainService;



public class EifTIMAPL02 extends SQLStatement
{
  private ResultSet rs = null;
  private ResultSet rs1 = null;
  private ResultSet rs2 = null;
  private ResultSet rs2_body = null;
  private ResultSet rs_e8049ReleaseData = null;
  private static String eifName = "EifTIMAPL02";
  private static final String eifFileDateFormat = "yyyyMMddHHmm";
  String exec_date= new SimpleDateFormat(eifFileDateFormat).format(new java.util.Date());
//  private static String interval = "1440*365*15";
  String mailto = "";
  String ws_mailbody = "";
  String ft_mailbody = "";

  public EifTIMAPL02(){
  }

  public void processTIMAPL02(String facility)
  {
    Connection conn = null;
    int i = 0;
    int j = 0;
    try
    {
      conn = DBConnection.getConnection();
      if (EIFService.updateInterfaceTime("EIFTIMAPL02", "CURRENT_TIME") == 1) {
    	if(facility.equals("") || facility.equals("FT")){  
	      // get avaliable TIM FT(廠外) list
    	  //新增廠內資料 20171129
	      selectTIMData(conn);
	      while (rs.next()) {
	    	  if (!alreadyExists(conn)) {
	    		  insertAPL_PIMNoData(conn);
	    	  }
	    	  deleteAPL_NAData(conn, "FT");
	    	  selectAPLData(conn);
	      }
	      selecte8049ReleaseData(conn);
	      while (rs_e8049ReleaseData.next()){
	    	  String brand = "";
	    	  if(rs_e8049ReleaseData.getString("BRAND").equals("MX")){
	    		  brand = "KH";
	    	  }else{
	    		  brand = "MX";
	    	  }
	    	  String OtherOI_sid = OiMaintainService.getSidRelease(rs_e8049ReleaseData.getString("PRODUCT_BODY"),brand);
	    	  updateAPLIsExistsE8049(conn, OtherOI_sid);
	      }
        }  
        if(facility.equals("") || facility.equals("WS")){
	      // get avaliable SEM-WS (廠外) list
	      selectSEMWSData(conn);
	      while (rs.next()) {
	    	  if (!alreadyExists(conn)) {
	    		  insertAPL_PIMNoData(conn);
	    	  }		  
	    	  deleteAPL_NAData(conn, "WS");
	    	  selectAPLData(conn);
	      }
        } 
        if(facility.equals("") || facility.equals("AVI")){
	     // get avaliable SEM-AVI (廠外) list
	      selectSEMAVIData(conn);
	      while (rs.next()) {
	    	  if (!alreadyExists(conn)) {
	    		  insertAPL_PIMNoData(conn);
	    	  }		  
	    	  selectAPLData(conn);
	      }
        }
        if(facility.equals("") || facility.equals("FVI")){
	   // get avaliable SEM-FVI (廠外) list
	      selectSEMFVIData(conn);
	      while (rs.next()) {
	    	  if (!alreadyExists(conn)) {
	    		  insertAPL_PIMNoData(conn);
	    	  }		  
	    	  selectAPLData(conn);
	      }
        }
        if(facility.equals("") || facility.equals("MARK")){
	   // get avaliable SEM-MARK (廠外) list
	      selectSEMMARKData(conn);
	      while (rs.next()) {
	    	  if (!alreadyExists(conn)) {
	    		  insertAPL_PIMNoData(conn);
	    	  }		  
	    	  selectAPLData(conn);
	      }
        }  
	      //SEND MAIL
	      sendmail(conn, exec_date);
	      EIFService.updateInterfaceTime("EIFTIMAPL02", "LAST_TIME");
      }    
     
    } catch (Exception e){
    	TDSLogger.println(e);
    } finally {
      try {
        conn.close();
        conn = null;
      } catch (Exception e){
        TDSLogger.println(e);
      }
    }
  }
  private boolean insertAPL_PIMNoData(Connection conn)
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
          rs.getString("PACKAGE_TYPE") + "','" +
          rs.getString("PIN_COUNT") + "',";
      if (StringUtil.formatNull(rs.getString("BODY_SIZE")).equals(""))
        sql = sql + "'NA','";//null
      else
        sql = sql + "'" + rs.getString("BODY_SIZE") + "','";
      sql = sql + 
          rs.getString("TEST_MODE") + "','" +
          rs.getString("TESTER_TYPE") + "','" +
          rs.getString("SAP_PLANT_NO") + "','W','" +
          rs.getString("APP_ID") + "'," +
          " sysdate, sysdate," ;
      if (StringUtil.formatNull(rs.getString("SITE")).equals(""))
          sql = sql + "'NA',";//null
      else
          sql = sql + "'" + rs.getString("SITE") + "',";
      if (StringUtil.formatNull(rs.getString("CARRIER_TYPE")).equals(""))
          sql = sql + "'NA',";
      else
          sql = sql + "'" + rs.getString("CARRIER_TYPE") + "',";    
      if (StringUtil.formatNull(rs.getString("MARKING_SPEC_NO")).equals(""))
          sql = sql + "'NA',";
      else
          sql = sql + "'" + rs.getString("MARKING_SPEC_NO") + "',";    
      if (StringUtil.formatNull(rs.getString("MARKING_SPEC_VERSION")).equals(""))
          sql = sql + "'NA',";
      else
          sql = sql + "'" + rs.getString("MARKING_SPEC_VERSION") + "',";    
      if (StringUtil.formatNull(rs.getString("INK")).equals(""))
          sql = sql + "'NA'";
      else
          sql = sql + "'" + rs.getString("INK") + "'";  
      
      sql = sql + ", '' ";
      //sql = sql + "null, null, null, null " +
      sql = sql + " from dual";
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
  
  private boolean deleteAPL_NAData(Connection conn, String process_type)
  {
    int result = 0;
    PreparedStatement pstmt = null;

    try
    {
      TDSLogger.println("--START Delete PGM NA TO APL!!!");
      String sql = "";
      sql = "delete from ap_apl \n" +
            "where process_type = '" + rs.getString("PROCESS_TYPE") + "' \n" +
            "  and product_body = '" + rs.getString("PRODUCT_BODY") + "' \n" +
            "  and options = '" + rs.getString("OPTIONS") + "' \n" +
            "  and package_type = '" + rs.getString("PACKAGE_TYPE") + "' \n" +
            "  and pin_count = '" +rs.getString("PIN_COUNT") + "' \n" +
            "  and test_mode = '" + rs.getString("TEST_MODE") + "' \n" +
            "  and tester_type = '" + rs.getString("TESTER_TYPE") + "' \n" +
            "  and vendor_no = '" + rs.getString("SAP_PLANT_NO") + "' \n" +
            "  and apl_status = 'W' ";
      if (process_type.equals("WS"))
    	  sql = sql + "  and site = 'NA' \n";
      if (process_type.equals("FT"))
    	  sql = sql + "  and body_size = 'NA' \n";
      TDSLogger.println(sql);
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
//將資料置入 AP_APL 前，先 check 是否已有此筆資料
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
          "' AND PACKAGE_TYPE = '" + rs.getString("PACKAGE_TYPE") +
          "' AND PIN_COUNT = '" + rs.getString("PIN_COUNT") +
          "' AND NVL(BODY_SIZE,'NA') = '" + rs.getString("BODY_SIZE") + //"' AND NVL(BODY_SIZE,'null') = '" + rs.getString("BODY_SIZE") +
          "' AND NVL(SITE,'NA') = '" + rs.getString("SITE") + //"' AND NVL(SITE,'null') = '" + rs.getString("SITE") +
          "' AND TEST_MODE = '" + rs.getString("TEST_MODE") +
          "' AND TESTER_TYPE = '" + rs.getString("TESTER_TYPE") +
          "' AND NVL(CARRIER_TYPE,'NA') = '" + rs.getString("CARRIER_TYPE") + //"' AND NVL(CARRIER_TYPE,'null') = '" + rs.getString("CARRIER_TYPE") +
          "' AND NVL(MARKING_SPEC_NO,'NA') = '" + rs.getString("MARKING_SPEC_NO") + //"' AND NVL(MARKING_SPEC_NO,'null') = '" + rs.getString("MARKING_SPEC_NO") +
          "' AND NVL(MARKING_SPEC_VERSION,'NA') = '" + rs.getString("MARKING_SPEC_VERSION") + //"' AND NVL(MARKING_SPEC_VERSION,'null') = '" + rs.getString("MARKING_SPEC_VERSION") +
          "' AND NVL(INK,'NA') = '" + rs.getString("INK") + //"' AND NVL(INK,'null') = '" + rs.getString("INK") +
          "' AND VENDOR_NO = '" + rs.getString("SAP_PLANT_NO") + 
          "' AND APL_STATUS IN ('W','R') ";
      TDSLogger.println(sql);
      pstmt = conn.prepareStatement(sql);
      ResultSet q = pstmt.executeQuery();
      while (q.next()) {
        cnt = Integer.parseInt(q.getString("CNT"));
        break;
      }
      if (cnt > 0) {
        result = true;
        TDSLogger.println("Find data " + sql);
      }
      pstmt.close();

    } catch (Exception ex) {
      TDSLogger.println(ex);
    } finally {
    }
    return result;
  }
//取得某一時間區間內 TIM-FT(NVM/MROM/XROM) 廠外Release 的資料
  //新增廠內資料 20171129
  private void selectTIMData(Connection conn)
  {
    boolean result = true;
    try
    {
      String ft_app_id="TIM-FT_"+exec_date;
      String sql =
    	  "select distinct 'FT' process_type, a.product_body, b.backend_option options, d.prm2_code package_type, \n" +
    	  "trim(to_char(b.pin_count,'009')) pin_count, b.body_size, b.test_type test_mode, b.tester tester_type, c.sap_plant_no,'"+ft_app_id+"' app_id, 'NA' site, 'NA' carrier_type, 'NA' marking_spec_no, 'NA' marking_spec_version, 'NA' ink \n" +//
    	  "from tf_information a, tf_test_parameter_ft b, ba_plant c, ba_package_type d, if_interface_time e \n" +
    	  "where a.sid = b.sid \n" + 
    	  "and a.status = 'R' \n" + 
    	  "and b.site = c.plant_name \n" + 
    	  //"and c.plant_no != 0 \n" + //not include mxic 
    	  "and d.package_type = b.package_type \n" +
    	  "and b.test_type like 'F%' \n" +
    	  "and c.sap_plant_no is not null \n"+
    	  "and e.interface = 'EIFTIMAPL02' \n" +
    	  "and a.log_time between e.last_time and e.current_time \n" + //lai-20070308-15/24*60 ";
    	  "union \n"+
          // for program not in PIM system
          "select distinct 'FT' process_type, a.product_body, b.backend_option, d.prm2_code package_type, \n"+
          "trim(to_char(b.pin_count,'009')) pin_count, b.body_size, b.test_type test_mode, b.tester tester_type, c.sap_plant_no,'"+ft_app_id+"' app_id, 'NA' site, 'NA' carrier_type, 'NA' marking_spec_no, 'NA' marking_spec_version, 'NA' ink  \n"+//
          "from tf_information a, tf_test_parameter_pbc b, ba_plant c, ba_package_type d, if_interface_time e \n"+
          "where a.sid = b.sid \n" + 
          "and a.status = 'R' \n"+
          "and b.site = c.plant_name \n"+
          //"and c.plant_no != 0 \n" + //not include mxic 
          "and d.package_type = b.package_type \n"+
          "and b.test_type like 'F%' \n"+
          "and c.sap_plant_no is not null \n"+
          "and e.interface = 'EIFTIMAPL02' \n" +
          "and a.log_time between e.last_time and e.current_time \n" + //lai-20070308-15/24*60 ";
          "order by product_body ";
     TDSLogger.println("tim-ft sql="+sql);
     PreparedStatement ps = conn.prepareStatement(sql);
     rs = ps.executeQuery();
     

   } catch (Exception e) {
     TDSLogger.println(e);
   } finally {
   }
 }
  private void selecte8049ReleaseData(Connection conn)
  {
    boolean result = true;
    try
    {
      String sql =
    		  "select * from tf_information a, if_interface_time e\n" +
    		  "where a.status = 'R'\n" +
    		  //"and a.brand = 'MX'\n" +
    		  "and e.interface = 'EIFTIMAPL02'\n" + 
    		  "and a.log_time between e.last_time and e.current_time\n" +
    		  "order by a.log_time asc";

     TDSLogger.println("e8049ReleaseData sql="+sql);
     PreparedStatement ps = conn.prepareStatement(sql);
     rs_e8049ReleaseData = ps.executeQuery();
     

   } catch (Exception e) {
     TDSLogger.println(e);
   } finally {
   }
 } 
  private boolean updateAPLIsExistsE8049(Connection conn, String OtherOI_sid)
  {
    int result = 0;
    PreparedStatement pstmt = null;

    try
    {
      TDSLogger.println("--START updateAPLIsExistsE8049(Remove)!!!");
      String sql = "";
      sql = "update tim.ap_apl aa set aa.isexists8049 ='Remove', LOG_TIME = sysdate\n" +
    		"where aa.product_body = '" + rs_e8049ReleaseData.getString("PRODUCT_BODY") + "'\n" +
			"and aa.process_type='FT'\n" + 
    		"and ( aa.isexists8049 IS NULL OR aa.isexists8049 != 'Remove')\n" +
			"and not exists (select 1 from tf_information a, tf_test_parameter_ft b, ba_plant c, ba_package_type d\n" + 
			"                 where a.sid = b.sid\n" + 
			"					and a.sid = '" + rs_e8049ReleaseData.getString("SID") + "'\n" +
			"                   and a.status ='R'\n" + 
			"                   and b.site = c.plant_name\n" + 
			"                   and d.package_type = b.package_type\n" + 
			"                   and b.test_type like 'F%'\n" + 
			"                   and c.sap_plant_no is not null\n" + 
			"                   and aa.product_body = a.product_body\n" + 
			"                   and aa.options = b.backend_option\n" + 
			"                   and aa.package_type = d.prm2_code\n" + 
			"                   and to_char(aa.pin_count, '000') = to_char(b.pin_count, '000')\n" + 
			"                   and aa.test_mode = b.test_type\n" + 
			"                   and aa.tester_type = b.tester\n" + 
			"                   and aa.Vendor_No = C.SAP_PLANT_NO\n" + 
			"                 union\n" + 
			"                 select 1 from tf_information a, tf_test_parameter_pbc b, ba_plant c, ba_package_type d\n" + 
			"                 where a.sid = b.sid\n" + 
			"					and a.sid = '" + rs_e8049ReleaseData.getString("SID") + "'\n" +
			"                   and a.status ='R'\n" + 
			"                   and b.site = c.plant_name\n" + 
			"                   and d.package_type = b.package_type\n" + 
			"                   and b.test_type like 'F%'\n" + 
			"                   and c.sap_plant_no is not null\n" + 
			"                   and aa.product_body = a.product_body\n" + 
			"                   and aa.options = b.backend_option\n" + 
			"                   and aa.package_type = d.prm2_code\n" + 
			"                   and to_char(aa.pin_count, '000') = to_char(b.pin_count, '000')\n" + 
			"                   and aa.test_mode = b.test_type\n" + 
			"                   and aa.tester_type = b.tester\n" + 
			"                   and aa.Vendor_No = C.SAP_PLANT_NO\n" + 
			"                  )" +
			"and not exists (select 1 from tf_information a, tf_test_parameter_ft b, ba_plant c, ba_package_type d\n" + 
			"                 where a.sid = b.sid\n" + 
			"					and a.sid = '" + OtherOI_sid + "'\n" +
			"                   and a.status ='R'\n" + 
			"                   and b.site = c.plant_name\n" + 
			"                   and d.package_type = b.package_type\n" + 
			"                   and b.test_type like 'F%'\n" + 
			"                   and c.sap_plant_no is not null\n" + 
			"                   and aa.product_body = a.product_body\n" + 
			"                   and aa.options = b.backend_option\n" + 
			"                   and aa.package_type = d.prm2_code\n" + 
			"                   and to_char(aa.pin_count, '000') = to_char(b.pin_count, '000')\n" + 
			"                   and aa.test_mode = b.test_type\n" + 
			"                   and aa.tester_type = b.tester\n" + 
			"                   and aa.Vendor_No = C.SAP_PLANT_NO\n" + 
			"                 union\n" + 
			"                 select 1 from tf_information a, tf_test_parameter_pbc b, ba_plant c, ba_package_type d\n" + 
			"                 where a.sid = b.sid\n" + 
			"					and a.sid = '" + OtherOI_sid + "'\n" +
			"                   and a.status ='R'\n" + 
			"                   and b.site = c.plant_name\n" + 
			"                   and d.package_type = b.package_type\n" + 
			"                   and b.test_type like 'F%'\n" + 
			"                   and c.sap_plant_no is not null\n" + 
			"                   and aa.product_body = a.product_body\n" + 
			"                   and aa.options = b.backend_option\n" + 
			"                   and aa.package_type = d.prm2_code\n" + 
			"                   and to_char(aa.pin_count, '000') = to_char(b.pin_count, '000')\n" + 
			"                   and aa.test_mode = b.test_type\n" + 
			"                   and aa.tester_type = b.tester\n" + 
			"                   and aa.Vendor_No = C.SAP_PLANT_NO\n" + 
			"                  )";

          
      TDSLogger.println(sql);
      pstmt = conn.prepareStatement(sql);
      result = pstmt.executeUpdate();
      if (result == 1) {
    	TDSLogger.println("--updateAPLIsExistsE8049(Remove) SUCCESS " + sql);
      }
      
      TDSLogger.println("--START updateAPLIsExistsE8049(Space)!!!");
      sql = "update tim.ap_apl aa set aa.isexists8049 ='', LOG_TIME = sysdate\n" +
    		"where aa.product_body = '" + rs_e8049ReleaseData.getString("PRODUCT_BODY") + "'\n" +
			"and aa.process_type='FT'\n" + 
    		"and aa.isexists8049 = 'Remove'\n" +
			"and (exists (select 1 from tf_information a, tf_test_parameter_ft b, ba_plant c, ba_package_type d\n" + 
			"                 where a.sid = b.sid\n" + 
			"					and a.sid = '" + rs_e8049ReleaseData.getString("SID") + "'\n" +
			"                   and a.status ='R'\n" + 
			"                   and b.site = c.plant_name\n" + 
			"                   and d.package_type = b.package_type\n" + 
			"                   and b.test_type like 'F%'\n" + 
			"                   and c.sap_plant_no is not null\n" + 
			"                   and aa.product_body = a.product_body\n" + 
			"                   and aa.options = b.backend_option\n" + 
			"                   and aa.package_type = d.prm2_code\n" + 
			"                   and to_char(aa.pin_count, '000') = to_char(b.pin_count, '000')\n" + 
			"                   and aa.test_mode = b.test_type\n" + 
			"                   and aa.tester_type = b.tester\n" + 
			"                   and aa.Vendor_No = C.SAP_PLANT_NO\n" + 
			"                 union\n" + 
			"                 select 1 from tf_information a, tf_test_parameter_pbc b, ba_plant c, ba_package_type d\n" + 
			"                 where a.sid = b.sid\n" + 
			"					and a.sid = '" + rs_e8049ReleaseData.getString("SID") + "'\n" +
			"                   and a.status ='R'\n" + 
			"                   and b.site = c.plant_name\n" + 
			"                   and d.package_type = b.package_type\n" + 
			"                   and b.test_type like 'F%'\n" + 
			"                   and c.sap_plant_no is not null\n" + 
			"                   and aa.product_body = a.product_body\n" + 
			"                   and aa.options = b.backend_option\n" + 
			"                   and aa.package_type = d.prm2_code\n" + 
			"                   and to_char(aa.pin_count, '000') = to_char(b.pin_count, '000')\n" + 
			"                   and aa.test_mode = b.test_type\n" + 
			"                   and aa.tester_type = b.tester\n" + 
			"                   and aa.Vendor_No = C.SAP_PLANT_NO\n" + 
			"                  )\n" +
			"or exists (select 1 from tf_information a, tf_test_parameter_ft b, ba_plant c, ba_package_type d\n" + 
			"                 where a.sid = b.sid\n" + 
			"					and a.sid = '" + OtherOI_sid + "'\n" +
			"                   and a.status ='R'\n" + 
			"                   and b.site = c.plant_name\n" + 
			"                   and d.package_type = b.package_type\n" + 
			"                   and b.test_type like 'F%'\n" + 
			"                   and c.sap_plant_no is not null\n" + 
			"                   and aa.product_body = a.product_body\n" + 
			"                   and aa.options = b.backend_option\n" + 
			"                   and aa.package_type = d.prm2_code\n" + 
			"                   and to_char(aa.pin_count, '000') = to_char(b.pin_count, '000')\n" + 
			"                   and aa.test_mode = b.test_type\n" + 
			"                   and aa.tester_type = b.tester\n" + 
			"                   and aa.Vendor_No = C.SAP_PLANT_NO\n" + 
			"                 union\n" + 
			"                 select 1 from tf_information a, tf_test_parameter_pbc b, ba_plant c, ba_package_type d\n" + 
			"                 where a.sid = b.sid\n" + 
			"					and a.sid = '" + OtherOI_sid + "'\n" +
			"                   and a.status ='R'\n" + 
			"                   and b.site = c.plant_name\n" + 
			"                   and d.package_type = b.package_type\n" + 
			"                   and b.test_type like 'F%'\n" + 
			"                   and c.sap_plant_no is not null\n" + 
			"                   and aa.product_body = a.product_body\n" + 
			"                   and aa.options = b.backend_option\n" + 
			"                   and aa.package_type = d.prm2_code\n" + 
			"                   and to_char(aa.pin_count, '000') = to_char(b.pin_count, '000')\n" + 
			"                   and aa.test_mode = b.test_type\n" + 
			"                   and aa.tester_type = b.tester\n" + 
			"                   and aa.Vendor_No = C.SAP_PLANT_NO\n" + 
			"                  ))";

          
      TDSLogger.println(sql);
      pstmt = conn.prepareStatement(sql);
      result = pstmt.executeUpdate();
      if (result == 1) {
    	TDSLogger.println("--updateAPLIsExistsE8049(Space) SUCCESS " + sql);
      }
    } catch (Exception e) {
      TDSLogger.println(e);
      return false;
    } finally {
      pstmt = null;
    }
    return true;
  } 
//取得某一時間區間內 SEM-WS 已結案(廠外)的資料
  private void selectSEMWSData(Connection conn)
  {
    boolean result = true;
    try
    {
      String sql =
    	  "select distinct 'WS' process_type, A.PRODUCT_BODY, A.OPTIONS, 'W' package_type, '000' pin_count, \n" +
    	  "'NA' body_size, b.test_mode, b.tester_type, c.sap_plant_no, B.site_no site, a.app_id, 'NA' carrier_type, 'NA' marking_spec_no, 'NA' marking_spec_version, 'NA' ink \n" +//
    	  "from ts_wsapl a, ts_wsapl_detail b, ba_plant c, if_interface_time e, ts_event t \n" +
    	  "where a.app_id = b.app_id \n" + 
    	  "and a.status = 7 \n" + //已結案
    	  "and b.plant_name = c.plant_name \n" + 
    	  "and c.plant_no != 0 \n" + //not include mxic 
    	  "and c.sap_plant_no is not null \n"+
    	  "and e.interface = 'EIFTIMAPL02' \n" +
    	  "and t.app_id = a.app_id \n" +
    	  "and t.status = '確認部級主管送結案' \n" +
    	  //"and a.close_date between e.last_time and e.current_time \n" + //lai-20070308-15/24*60 ";
    	  "and t.event_date between e.last_time and e.current_time \n" + //lai-20070308-15/24*60 ";
    	  "order by a.product_body ";
     TDSLogger.println("sem-ws sql="+sql);
     PreparedStatement ps = conn.prepareStatement(sql);
     rs = ps.executeQuery();
     

   } catch (Exception e) {
     TDSLogger.println(e);
   } finally {
   }
 }
  
//取得某一時間區間內 SEM-AVI 已結案(廠外)的資料
  private void selectSEMAVIData(Connection conn)
  {
    boolean result = true;
    try
    {
      String sql =
	    	  "select distinct 'AVI' process_type, A.PRODUCT_BODY, A.OPTIONS, 'NA' package_type, '000' pin_count, \n" +
	    	  "'NA' body_size, 'NA' test_mode, b.tester_type, c.sap_plant_no, 'NA' site, a.app_id, 'NA' carrier_type, 'NA' marking_spec_no, 'NA' marking_spec_version, a.ink \n" +//, '' site, '' carrier_type, '' marking_spec_no, '' marking_spec_version, '' ink
	    	  "from ts_aviapl a, ts_aviapl_detail b, ba_plant c, if_interface_time e, ts_event t \n" +
	    	  "where a.app_id = b.app_id \n" + 
	    	  "and a.status = 7 \n" + //已結案
	    	  "and a.plant_name_f = c.plant_name \n" + 
	    	  "and c.plant_no != 0 \n" + //not include mxic 
	    	  "and c.sap_plant_no is not null \n"+
	    	  "and e.interface = 'EIFTIMAPL02' \n" +
	    	  "and t.app_id = a.app_id \n" +
	    	  "and t.status = '確認部級主管送結案' \n" +
	    	  //"and a.close_date between e.last_time and e.current_time \n" + //lai-20070308-15/24*60 ";
	    	  "and t.event_date between e.last_time and e.current_time \n" + //lai-20070308-15/24*60 ";
	    	  "order by a.product_body ";
     TDSLogger.println("sem-avi sql="+sql);
     PreparedStatement ps = conn.prepareStatement(sql);
     rs = ps.executeQuery();
     

   } catch (Exception e) {
     TDSLogger.println(e);
   } finally {
   }
 }
//取得某一時間區間內 SEM-FVI 已結案(廠外)的資料
  private void selectSEMFVIData(Connection conn)
  {
    boolean result = true;
    try
    {
      String sql =
    	  "select distinct 'FVI' process_type, 'NA' PRODUCT_BODY, 'NA' OPTIONS, a.package_type, trim(to_char(a.pin_count,'009')) pin_count, \n" +
    	  "a.body_size, 'NA' test_mode, b.tester_type, c.sap_plant_no, 'NA' site, a.app_id, b.carrier_type, 'NA' marking_spec_no, 'NA' marking_spec_version, 'NA' ink \n" +//, '' site, '' carrier_type, '' marking_spec_no, '' marking_spec_version, '' ink
    	  "from ts_fviapl a, ts_fviapl_detail b, ba_plant c, if_interface_time e, ts_event t \n" +
    	  "where a.app_id = b.app_id \n" + 
    	  "and a.status = 7 \n" + //已結案
    	  "and b.plant_name = c.plant_name \n" + 
    	  "and c.plant_no != 0 \n" + //not include mxic 
    	  "and c.sap_plant_no is not null \n"+
    	  "and e.interface = 'EIFTIMAPL02' \n" +
    	  "and t.app_id = a.app_id \n" +
    	  "and t.status = '確認部級主管送結案' \n" +
    	  //"and a.close_date between e.last_time and e.current_time \n";
    	  "and t.event_date between e.last_time and e.current_time \n";
     TDSLogger.println("sem-fvi sql="+sql);
     PreparedStatement ps = conn.prepareStatement(sql);
     rs = ps.executeQuery();
     

   } catch (Exception e) {
     TDSLogger.println(e);
   } finally {
   }
 }
//取得某一時間區間內 SEM-MARK 已結案(廠外)的資料
  private void selectSEMMARKData(Connection conn)
  {
    boolean result = true;
    try
    {
      String sql =
    	  "select distinct 'MARK' process_type, 'NA' PRODUCT_BODY,'NA' OPTIONS, a.package_type,  \n" +
    	  "trim(to_char(a.pin_count,'009')) pin_count, b.body_size, 'NA' test_mode, b.tester_type, c.sap_plant_no, 'NA' site, a.app_id, 'NA' carrier_type, a.marking_spec_no, a.marking_spec_ver marking_spec_version, 'NA' ink \n" +//, '' site, '' carrier_type, '' marking_spec_no, '' marking_spec_version, '' ink
    	  "from ts_markapl a, ts_markapl_detail b, ba_plant c, if_interface_time e, ts_event t  \n" +
          "where a.app_id = b.app_id \n" +
    	  "and a.status = 7 \n" + //已結案
    	  "and b.plant_name = c.plant_name \n" + 
    	  "and c.plant_no != 0 \n" + //not include mxic 
    	  "and c.sap_plant_no is not null \n" + 
    	  "and e.interface = 'EIFTIMAPL02' \n" + 
    	  "and t.app_id = a.app_id \n" +
    	  "and t.status = '確認部級主管送結案' \n" +
    	  //"and a.close_date between e.last_time and e.current_time";
    	  "and t.event_date between e.last_time and e.current_time";

     TDSLogger.println("sem-mark sql="+sql);
     PreparedStatement ps = conn.prepareStatement(sql);
     rs = ps.executeQuery();
     

   } catch (Exception e) {
     TDSLogger.println(e);
   } finally {
   }
 }
  private void selectAPLData(Connection conn)
  {
    PreparedStatement pstmt = null;
    String sql = "";

    

    try {
    	 //String[] test_mode = rs.getString("TEST_MODE").split(";");
    	 //String[] tester_type = rs.getString("TESTER_TYPE").split(";");
         //for(int ii=0; ii<test_mode.length; ii++ ){
        	 sql =
           	    "select  SID, "+
           	          "  PROCESS_TYPE, "+ 
           	          "  PRODUCT_BODY, "+ 
           	          "  OPTIONS, "+
           	          "  PACKAGE_TYPE, "+
           	          "  PIN_COUNT, "+
           	          "  BODY_SIZE, "+
           	          "  TEST_MODE, "+
           	          "  TESTER_TYPE, "+
             	      "  VENDOR_NO, "+
             	      "  SITE, "+
             	      "  CARRIER_TYPE, "+
             	      "  MARKING_SPEC_NO, "+
             	      "  MARKING_SPEC_VERSION, "+
             	      "  INK, "+
             	      "  APL_STATUS, "+
             	      " '" + rs.getString("APP_ID") + "' APL_NO "+
                   "from TIM.AP_APL a \n"+
                   "where a.PROCESS_TYPE = ? \n"+
                   "and a.PRODUCT_BODY = ? \n"+
                   "and a.OPTIONS = ? \n"+
                   "and a.PACKAGE_TYPE = ? \n"+
                   "and a.PIN_COUNT = ? \n"+
                   "and a.TEST_MODE = ?  \n"+
                   "and a.TESTER_TYPE = ? \n"+
                   "and a.VENDOR_NO = ? \n"+
                   "and a.BODY_SIZE = ? \n"+
                   "and a.SITE = ? \n"+
        	       "and a.CARRIER_TYPE = ? \n"+
        	       "and a.MARKING_SPEC_NO = ? \n"+
        	       "and a.MARKING_SPEC_VERSION = ? \n"+
        	       "and a.INK = ? \n"+
        	       "and a.APL_STATUS = 'W' \n";
         
                   TDSLogger.println("sql = " + sql);
            
                   pstmt = conn.prepareStatement(sql);
                   pstmt.setString(1, rs.getString("PROCESS_TYPE"));
                   pstmt.setString(2, rs.getString("PRODUCT_BODY"));
                   pstmt.setString(3, rs.getString("OPTIONS"));
                   pstmt.setString(4, rs.getString("PACKAGE_TYPE"));
                   pstmt.setString(5, rs.getString("PIN_COUNT"));
                   pstmt.setString(6, rs.getString("TEST_MODE"));//test_mode[ii]);
                   pstmt.setString(7, rs.getString("TESTER_TYPE"));//tester_type[ii]);	
                   pstmt.setString(8, rs.getString("SAP_PLANT_NO"));
                   pstmt.setString(9, rs.getString("BODY_SIZE"));
                   pstmt.setString(10, rs.getString("SITE"));
                   pstmt.setString(11, rs.getString("CARRIER_TYPE"));
                   pstmt.setString(12, rs.getString("MARKING_SPEC_NO"));
                   pstmt.setString(13, rs.getString("MARKING_SPEC_VERSION"));
                   pstmt.setString(14, rs.getString("INK"));
                   TDSLogger.println("PROCESS_TYPE = " + rs.getString("PROCESS_TYPE"));
                   TDSLogger.println("PRODUCT_BODY = " + rs.getString("PRODUCT_BODY"));
                   TDSLogger.println("OPTIONS = " + rs.getString("OPTIONS"));
                   TDSLogger.println("PACKAGE_TYPE = " + rs.getString("PACKAGE_TYPE"));
                   TDSLogger.println("PIN_COUNT = " + rs.getString("PIN_COUNT"));
                   TDSLogger.println("TEST_MODE = " + rs.getString("TEST_MODE"));//test_mode[ii]);
                   TDSLogger.println("TEST_MODE = " + rs.getString("TESTER_TYPE"));//tester_type[ii]);
                   TDSLogger.println("SAP_PLANT_NO = " + rs.getString("SAP_PLANT_NO"));
                   TDSLogger.println("BODY_SIZE = " + rs.getString("BODY_SIZE"));
                   TDSLogger.println("SITE = " + rs.getString("SITE"));
                   TDSLogger.println("CARRIER_TYPE = " + rs.getString("CARRIER_TYPE"));
                   TDSLogger.println("MARKING_SPEC_NO = " + rs.getString("MARKING_SPEC_NO"));
                   TDSLogger.println("MARKING_SPEC_VERSION = " + rs.getString("MARKING_SPEC_VERSION"));
                   TDSLogger.println("INK = " + rs.getString("INK"));
          
           rs1 = pstmt.executeQuery();
           if(rs1 !=null){
        	   while(rs1.next()){
             	    boolean result = modifyAPL(conn);
             	    if(!result){
             	    	TDSLogger.println("Update Fail ");
             	    }else{
             	    	TDSLogger.println("Update Success ");
             	    }
        	   }
        	   
      	   }
           pstmt.close();
        //}
 
    } catch (Exception ex) {
      TDSLogger.println(ex);
    } finally {
    }
  }
  
//just update APP_STATUS to "C" (Completed)
  // AP_APP_MASTER's trigger BF_UPDATE_AP_APP_MASTER() will change AP_APL status
  //     and move data to history
  //
  // Step 1: Set AP_APP_MASTER STATUS to C
  // Step 2: BF_UPDATE_AP_APP_MASTER will insert History Data
  // Step 3: Mail Notice to related users
  private boolean sendmail( Connection conn, String exec_date) {
    boolean result = false;
    ArrayList wheres2 = new ArrayList();
    String sql = "";
    PreparedStatement pstmt = null;
    PreparedStatement pstmt_body = null;
    
    try {
      
    	sql =
       	    "select  distinct SEM_NO, PROCESS_TYPE "+
               "from TIM.AP_APL_SEM  \n"+
               "where EXEC_DATE = ? \n";
     
        TDSLogger.println("sql = " + sql);
        
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, exec_date);
              
        rs2 = pstmt.executeQuery();
        if(rs2 !=null){
    	       while(rs2.next()){
    	    	      HashMap[] users = getNoticeList(conn, rs2.getString("PROCESS_TYPE"));
    	    	      mailto =  (String) TDSResource.getProperties("APL").get("mail_to");
    	    	      for (int i = 0; i < users.length; i++) {
    	    	          mailto = mailto + "," + users[i].get("EMAIL");
    	    	      }

    	    	      TDSLogger.println(rs2.getString("SEM_NO") + " Mail to : " + mailto);

    	    	      String mailfrom = (String) TDSResource.getProperties("APL").get("mail_to");
    	    	      
    	    	      String sql_body =
    	    	       	    "select  * "+
    	    	               "from TIM.AP_APL_SEM APL, BA_PLANT PLANT  \n"+
    	    	               "where SEM_NO = ? \n"+
    	    	               "and APL.VENDOR_NO = PLANT.SAP_PLANT_NO \n";
    	    	               //"and PLANT.PGM_FLAG = 1 \n"
    	    	      TDSLogger.println("sql_body = " + sql_body);
    	    	        
    	    	      pstmt_body = conn.prepareStatement(sql_body);
    	    	      pstmt_body.setString(1, rs2.getString("SEM_NO"));
    	    	      rs2_body = pstmt_body.executeQuery();
    	    	      String mailbody = "";
    	    	      String mailtitle = mailtitle_desc(rs2.getString("PROCESS_TYPE"));
    	    	                    
    	    	      String mailbcontent = "";
    	    	      
    	    	      while(rs2_body.next()){
    	    	    	  mailbcontent = mailbcontent + mailbcontent_desc(rs2.getString("PROCESS_TYPE"));
    	    	      }

    	    	          String mailsubject = "APL 結案通知 ("+rs2.getString("PROCESS_TYPE")+")：SEM_NO=" + rs2.getString("SEM_NO");
    	    	          mailbody =  "<FONT FACE=\"Courier New\">" +mailtitle + mailbcontent + "</FONT><br>" ;
    	    	    	             
    	    	          if(rs2.getString("PROCESS_TYPE").equals("FT"))
    	    	              mailbody = mailbody + "Information = <BR>\r\n" +
	    	    	          "http://" + (String) TDSResource.getProperties("APL").get("server_ip_port").toString().trim() +
    	    	              "APLShow.do?timftNo=" + rs2.getString("SEM_NO");
    	    	          else if(rs2.getString("PROCESS_TYPE").equals("WS"))
    	    	              mailbody = mailbody + "Information = <BR>\r\n" +
	    	    	          "http://" + (String) TDSResource.getProperties("APL").get("server_ip_port").toString().trim() +
    	    	              "APLShow.do?semwsNo=" + rs2.getString("SEM_NO");
    	    	          else if(rs2.getString("PROCESS_TYPE").equals("AVI"))
    	    	              mailbody = mailbody + "Information = <BR>\r\n" +
	    	    	          "http://" + (String) TDSResource.getProperties("APL").get("server_ip_port").toString().trim() + 
    	    	              "APLShow.do?semaviNo=" + rs2.getString("SEM_NO");
    	    	          if(rs2.getString("PROCESS_TYPE").equals("FVI"))
    	    	              mailbody = mailbody + "Information = <BR>\r\n" +
	    	    	          "http://" + (String) TDSResource.getProperties("APL").get("server_ip_port").toString().trim() + 
	    	    	          "APLShow.do?semfviNo=" + rs2.getString("SEM_NO");
    	    	          if(rs2.getString("PROCESS_TYPE").equals("MARK"))
    	    	              mailbody = mailbody + "Information = <BR>\r\n" +
	    	    	          "http://" + (String) TDSResource.getProperties("APL").get("server_ip_port").toString().trim() + 
	    	    	          "APLShow.do?semmarkNo=" + rs2.getString("SEM_NO");
    	    	     
    	    	          //SendMail.send(mailto, mailfrom, mailsubject, mailbody,
    	    	          //              "APL 結案通知：APP_NO=" + rs2.getString("SEM_NO"), null);
    	    	          SendMail.sendHtml(mailto, mailfrom, mailsubject, mailbody);
         	      
    	       }

  	    }
       pstmt.close();

      
    }
    catch (Exception ex) {
      DBConnection.rollback(conn);
      ex.printStackTrace();
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
    }
    finally {
      result = true;
    }
    return result;
  }
  
  
  
//used in release process
  public HashMap[] getNoticeList(Connection conn, String process_type) throws Exception {
    StringBuffer sql = new StringBuffer(256);
    PreparedStatement pstmt = null;
    HashMap[] user =null;
    
    sql.append("SELECT USR.USER_ID, USR.USER_NAME, USR.EMPLOYEE_NO, USR.REAL_NAME, ");
    sql.append("USR.DEPT_ID, DEP.DEPT_NAME, NOTICE.EMAIL ");
    sql.append(",NOTICE.WS_INHOUSE,NOTICE.FT_INHOUSE,NOTICE.WS_SUBCON,NOTICE.FT_SUBCON ");
    sql.append("FROM AU_USER_ACCOUNT USR, AU_USER_DEPARTMENT DEP, TIM.AP_NOTICE NOTICE ");
    sql.append("WHERE USR.DEPT_ID <> 'DUMMY' AND USR.DEPT_ID = DEP.DEPT_ID AND ");
    sql.append("USR.EMPLOYEE_NO = TRIM(NOTICE.EMPLOYEE_NO)  ");
    if (process_type.equals("WS"))
    	sql.append("AND NOTICE.WS_SUBCON = 'Y' ");
    if (process_type.equals("FT"))
    	sql.append("AND NOTICE.FT_SUBCON = 'Y' ");
    if (process_type.equals("AVI"))
    	sql.append("AND NOTICE.AVI = 'Y' ");
    if (process_type.equals("FVI"))
    	sql.append("AND NOTICE.FVI = 'Y' ");
    if (process_type.equals("MARK"))
    	sql.append("AND NOTICE.MARK = 'Y' ");

    sql.append(" ORDER BY NOTICE.EMPLOYEE_NO ");

    TDSLogger.println("sql = " + sql.toString());
    user= GPRSDB.qryHashMapBySql(conn, sql.toString(), new Object[]{});

    return user;
  }
  

  // 將資料置入 AP_APL_DETAIL / AP_APP_SEM 中
  private boolean modifyAPL(Connection conn)
  {
    int j = 0;
    try
    {
      TDSLogger.println("--START UPDATE TIM TO APL!!!");
      if (updateAPL(conn)){
    	  if(insertAP_APP_DETAIL(conn)){
    		  insertAP_APP_SEM(conn);  
            j++;  
            return true;
    	  }
      } 	  
        
      TDSLogger.println("--SUCCESS Update/Insert " + j + " Apls !!!");
    } catch (Exception e) {
    	TDSLogger.println(e);
    	return false;
    } finally {
      try{
      }catch(Exception ex){
    	  TDSLogger.println(ex);
      }
    }
    return true;
  }

  

  // update APL 
  private boolean updateAPL(Connection conn)
  {
    int result = 0;
    PreparedStatement pstmt = null;

    try
    {
      TDSLogger.println("--START UPDATE AP_APL!!!");
      String sql = "";
      sql = "UPDATE TIM.AP_APL SET " +
          " APL_STATUS = 'R' , "+
          " APL_NO = '" + rs1.getString("APL_NO") + "' , " +
          " LOG_TIME = sysdate " +
          "WHERE " +
          "  PROCESS_TYPE = '" + rs1.getString("PROCESS_TYPE") +
          "' AND PRODUCT_BODY = '" + rs1.getString("PRODUCT_BODY") +
          "' AND OPTIONS = '" + rs1.getString("OPTIONS") +
          "' AND PACKAGE_TYPE = '" + rs1.getString("PACKAGE_TYPE") +
          "' AND PIN_COUNT = '" + rs1.getString("PIN_COUNT") +
          "' AND TEST_MODE = '" + rs1.getString("TEST_MODE") +
          "' AND TESTER_TYPE = '" + rs1.getString("TESTER_TYPE") +
          "' AND BODY_SIZE = '" + rs1.getString("BODY_SIZE") +
          "' AND SITE = '" + rs1.getString("SITE") +
          "' AND CARRIER_TYPE = '" + rs1.getString("CARRIER_TYPE") +
          "' AND MARKING_SPEC_NO = '" + rs1.getString("MARKING_SPEC_NO") +
          "' AND MARKING_SPEC_VERSION = '" + rs1.getString("MARKING_SPEC_VERSION") +
          "' AND INK = '" + rs1.getString("INK") +
          "' AND VENDOR_NO = '" + rs1.getString("VENDOR_NO") +
          "' AND APL_STATUS = '" + rs1.getString("APL_STATUS") + "'";
          

      pstmt = conn.prepareStatement(sql);
      result = pstmt.executeUpdate();
      if (result == 1) {
    	TDSLogger.println("--SUCCESS " + sql);
      }
    } catch (Exception e) {
      TDSLogger.println(e);
      return false;
    } finally {
      pstmt = null;
    }
    return true;
  }
  
//insert AP_APP_DETAIL 
  private boolean insertAP_APP_DETAIL(Connection conn)
  {
    int result = 0;
    PreparedStatement pstmt = null;

    try
    {
      TDSLogger.println("--START INSERT AP_APP_DETAIL!!!");
      String sql = "";
      sql = "INSERT into TIM.AP_APP_DETAIL VALUES ( " +
          " '" + rs1.getString("APL_NO") + "', " +
          " '" + rs1.getString("SID") + "' )";
          

      pstmt = conn.prepareStatement(sql);
      result = pstmt.executeUpdate();
      if (result == 1) {
    	TDSLogger.println("--SUCCESS " + sql);
      }
    } catch (Exception e) {
      TDSLogger.println(e);
      return false;
    } finally {
      pstmt = null;
    }
    return true;
  }
  
//insert AP_APP_SEM
  private boolean insertAP_APP_SEM(Connection conn)
  {
    int result = 0;
    PreparedStatement pstmt = null;

    try
    {
      TDSLogger.println("--START INSERT AP_APP_SEM!!!");
      String sql = "";
      sql = "insert into TIM.ap_apl_sem (SID, PROCESS_TYPE, PRODUCT_BODY, OPTIONS, " +
          "PACKAGE_TYPE, PIN_COUNT, BODY_SIZE, TEST_MODE, TESTER_TYPE, VENDOR_NO, SEM_NO, LOG_TIME, EXEC_DATE, SITE, CARRIER_TYPE, MARKING_SPEC_NO, MARKING_SPEC_VERSION, INK) " +
          "select TIM.apl_sem_seq.nextval,'" +
          rs1.getString("PROCESS_TYPE") + "','" +
          rs1.getString("PRODUCT_BODY") + "','" +
          rs1.getString("OPTIONS") + "','" +
          rs1.getString("PACKAGE_TYPE") + "','" +
          rs1.getString("PIN_COUNT") + "',";
      if (StringUtil.formatNull(rs1.getString("BODY_SIZE")).equals(""))
        sql = sql + "'NA','";//null
      else
        sql = sql + "'" + rs1.getString("BODY_SIZE") + "','";
      sql = sql + 
          rs1.getString("TEST_MODE") + "','" +
          rs1.getString("TESTER_TYPE") + "','" +
          rs1.getString("VENDOR_NO") + "','" +
          rs1.getString("APL_NO") + "',"+
          "sysdate, '" + exec_date + "', ";
      if (StringUtil.formatNull(rs1.getString("SITE")).equals(""))    
    	  sql = sql + "'NA',";
      else
    	  sql = sql + "'" + rs1.getString("SITE") + "',";
      if (StringUtil.formatNull(rs1.getString("CARRIER_TYPE")).equals(""))    
    	  sql = sql + "'NA',";
      else
    	  sql = sql + "'" + rs1.getString("CARRIER_TYPE") + "',";
      if (StringUtil.formatNull(rs1.getString("MARKING_SPEC_NO")).equals(""))    
    	  sql = sql + "'NA',";
      else
    	  sql = sql + "'" + rs1.getString("MARKING_SPEC_NO") + "',";
      if (StringUtil.formatNull(rs1.getString("MARKING_SPEC_VERSION")).equals(""))    
    	  sql = sql + "'NA',";
      else
    	  sql = sql + "'" + rs1.getString("MARKING_SPEC_VERSION") + "',";
      if (StringUtil.formatNull(rs1.getString("INK")).equals(""))    
    	  sql = sql + "'NA'";
      else
    	  sql = sql + "'" + rs1.getString("INK") + "' ";
      sql = sql + " from dual";
      TDSLogger.println("-- " + sql);
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
  // generate a 'blank' string with length = 'width'
  private String padding(int width) {
    String str = "";

    for (int i = 0; i < width; i++)
      str = str + "&nbsp;";

    return str;
  }
  
  private String mailtitle_desc(String proctype) {
	  String mailtitledesc = "";
	  if (proctype.equals("WS")){
 	     mailtitledesc = "ProductBody" +padding(13-"ProductBody".length()) +//13
                      "Option" +padding(8-"Option".length()) +//13
                      "PackageCode" +padding(13-"PackageCode".length()) +//13
                      "PinCount" +padding(10-"PinCount".length()) +//13
                      "TestMode" +padding(10-"TestMode".length()) +//13
                      "TesterType" +padding(13-"TesterType".length()) +//13
                      "Site" +padding(13-"Site".length()) +//13
                      "VendorName<BR>\r\n";
   }else if (proctype.equals("FT")){
  	     mailtitledesc = "ProductBody" +padding(13-"ProductBody".length()) +//13
                      "Option" +padding(8-"Option".length()) +//13
                      "PackageCode" +padding(13-"PackageCode".length()) +//13
                      "PinCount" +padding(10-"PinCount".length()) +//13
                      "TestMode" +padding(10-"TestMode".length()) +//13
                      "TesterType" +padding(13-"TesterType".length()) +//13
                      "BodySize" +padding(30-"BodySize".length()) +//13
                      "VendorName<BR>\r\n";
   }else if (proctype.equals("AVI")){
   	     mailtitledesc = "ProductBody" +padding(13-"ProductBody".length()) +//13
                      "Option" +padding(8-"Option".length()) +//13
                      "TesterType" +padding(13-"TesterType".length()) +//13
                      "Ink" +padding(13-"Ink".length()) +//13
                      "VendorName<BR>\r\n";   
   }else if (proctype.equals("FVI")){
    	 mailtitledesc = "PackageCode" +padding(13-"PackageCode".length()) +//13
                      "PinCount" +padding(10-"PinCount".length()) +//13
                      "TesterType" +padding(13-"TesterType".length()) +//13
                      "BodySize" +padding(30-"BodySize".length()) +//30
                      "CarrierType" +padding(13-"CarrierType".length()) +//13
                      "CarrierTypeDesc" +padding(20-"CarrierTypeDesc".length()) +//13
                      "VendorName<BR>\r\n";    
   }else if (proctype.equals("MARK")){
	     mailtitledesc = "PackageCode" +padding(13-"PackageCode".length()) +//13
                      "PinCount" +padding(10-"PinCount".length()) +//13
                      "TesterType" +padding(13-"TesterType".length()) +//13
                      "BodySize" +padding(30-"BodySize".length()) +//13
                      "MarkingSpecNo" +padding(18-"MarkingSpecNo".length()) +//18
                      "MarkingSpecVer" +padding(18-"MarkingSpecVer".length()) +//18
                      "VendorName<BR>\r\n";         
   }     
   return mailtitledesc;
  }
private String mailbcontent_desc(String proctype) {
  String mailbcontentdesc = "";
  try	{	
	  if(proctype.equals("WS")){
		  mailbcontentdesc = 
	      rs2_body.getString("PRODUCT_BODY") +padding(13-rs2_body.getString("PRODUCT_BODY").trim().length()) +//13
	      rs2_body.getString("OPTIONS") +padding(8-rs2_body.getString("OPTIONS").trim().length()) +//8
	      rs2_body.getString("PACKAGE_TYPE") +padding(13-rs2_body.getString("PACKAGE_TYPE").trim().length()) +//13
	      rs2_body.getString("PIN_COUNT") +padding(10-rs2_body.getString("PIN_COUNT").trim().length()) +//10
	      rs2_body.getString("TEST_MODE") +padding(10-rs2_body.getString("TEST_MODE").trim().length()) +//10
	      rs2_body.getString("TESTER_TYPE") +padding(13-rs2_body.getString("TESTER_TYPE").trim().length()) +
	      rs2_body.getString("SITE") +padding(13-rs2_body.getString("SITE").trim().length()) +
	      rs2_body.getString("PLANT_NAME") +padding(13-rs2_body.getString("PLANT_NAME").trim().length()) + " <BR>\r\n";
	  }else if(proctype.equals("FT")){
		  mailbcontentdesc = 
	      rs2_body.getString("PRODUCT_BODY") +padding(13-rs2_body.getString("PRODUCT_BODY").trim().length()) +//13
	      rs2_body.getString("OPTIONS") +padding(8-rs2_body.getString("OPTIONS").trim().length()) +//8
	      rs2_body.getString("PACKAGE_TYPE") +padding(13-rs2_body.getString("PACKAGE_TYPE").trim().length()) +//13
	      rs2_body.getString("PIN_COUNT") +padding(10-rs2_body.getString("PIN_COUNT").trim().length()) +//10
	      rs2_body.getString("TEST_MODE") +padding(10-rs2_body.getString("TEST_MODE").trim().length()) +//10
	      rs2_body.getString("TESTER_TYPE") +padding(13-rs2_body.getString("TESTER_TYPE").trim().length()) +
	      rs2_body.getString("BODY_SIZE") +padding(30-rs2_body.getString("BODY_SIZE").trim().length()) +
	      rs2_body.getString("PLANT_NAME") +padding(13-rs2_body.getString("PLANT_NAME").trim().length()) + " <BR>\r\n";
	  }else if(proctype.equals("AVI")){
		  mailbcontentdesc = 
	      rs2_body.getString("PRODUCT_BODY") +padding(13-rs2_body.getString("PRODUCT_BODY").trim().length()) +//13
	      rs2_body.getString("OPTIONS") +padding(8-rs2_body.getString("OPTIONS").trim().length()) +//8
	      rs2_body.getString("TESTER_TYPE") +padding(13-rs2_body.getString("TESTER_TYPE").trim().length()) +
	      rs2_body.getString("INK") +padding(13-rs2_body.getString("INK").trim().length()) +
	      rs2_body.getString("PLANT_NAME") +padding(13-rs2_body.getString("PLANT_NAME").trim().length()) + " <BR>\r\n";
	  }else if(proctype.equals("FVI")){
		  mailbcontentdesc =  
	      rs2_body.getString("PACKAGE_TYPE") +padding(13-rs2_body.getString("PACKAGE_TYPE").trim().length()) +//13
	      rs2_body.getString("PIN_COUNT") +padding(10-rs2_body.getString("PIN_COUNT").trim().length()) +//10
	      rs2_body.getString("TESTER_TYPE") +padding(13-rs2_body.getString("TESTER_TYPE").trim().length()) +
	      rs2_body.getString("BODY_SIZE") +padding(30-rs2_body.getString("BODY_SIZE").trim().length()) +
	      rs2_body.getString("CARRIER_TYPE").substring(0,2) +padding(13-rs2_body.getString("CARRIER_TYPE").substring(0,2) .trim().length()) +
	      rs2_body.getString("CARRIER_TYPE").substring(3) +padding(20-rs2_body.getString("CARRIER_TYPE").substring(3).trim().length()) +
	      rs2_body.getString("PLANT_NAME") +padding(13-rs2_body.getString("PLANT_NAME").trim().length()) + " <BR>\r\n";
	  }else if(proctype.equals("MARK")){
		  mailbcontentdesc = 
	      rs2_body.getString("PACKAGE_TYPE") +padding(13-rs2_body.getString("PACKAGE_TYPE").trim().length()) +//13
	      rs2_body.getString("PIN_COUNT") +padding(10-rs2_body.getString("PIN_COUNT").trim().length()) +//10
	      rs2_body.getString("TESTER_TYPE") +padding(13-rs2_body.getString("TESTER_TYPE").trim().length()) +
	      rs2_body.getString("BODY_SIZE") +padding(30-rs2_body.getString("BODY_SIZE").trim().length()) +
	      rs2_body.getString("MARKING_SPEC_NO") +padding(18-rs2_body.getString("MARKING_SPEC_NO").trim().length()) +
	      rs2_body.getString("MARKING_SPEC_VERSION") +padding(18-rs2_body.getString("MARKING_SPEC_VERSION").trim().length()) +
	      rs2_body.getString("PLANT_NAME") +padding(13-rs2_body.getString("PLANT_NAME").trim().length()) + " <BR>\r\n";
	  }	
  } catch(Exception e) {
      TDSLogger.println(e);
  }	
  return mailbcontentdesc;
}
  
  
  // Main procedure
  public static void main(String args[])
  {
    EifTIMAPL02 apl = new EifTIMAPL02();
    try	{
    	if (args.length == 0) {
    		apl.processTIMAPL02("");
    	}else if (args.length == 1) {
   		    apl.processTIMAPL02(args[0]);
    	} 
    } catch(Exception e) {
      TDSLogger.println(e);
    }
  }
}