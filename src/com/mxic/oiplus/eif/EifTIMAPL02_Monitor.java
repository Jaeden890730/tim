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
import com.mxic.tdsplus.util.TDSLogger;



public class EifTIMAPL02_Monitor extends SQLStatement
{
  private ResultSet rs = null;
  private static String eifName = "EifTIMAPL02_Monitor";
  private static final String eifFileDateFormat = "yyyyMMddHHmm";
  String exec_date= new SimpleDateFormat(eifFileDateFormat).format(new java.util.Date());
//  private static String interval = "1440*365*15";

  public EifTIMAPL02_Monitor(){
  }

  public void processTIMAPL02_Monitor(String facility)
  {
    Connection conn = null;
    int i = 0;
    int j = 0;
    try
    {
      conn = DBConnection.getConnection();
      if (EIFService.updateInterfaceTime("EIFTIMAPL02_Monitor", "CURRENT_TIME") == 1) {
    	if(facility.equals("") || facility.equals("FT")){  
	      // get avaliable TIM FT(廠外) list
	      selectTIMData(conn);
	      boolean Flag = true;
	      String message = "";
	      while (rs.next()) {
	    	  if (!alreadyExists(conn)) {
	    		  message += AlarmMailBody();
	    		  Flag = false;
	    	  }
	      }
	      if(!Flag)
	        SendMail.send("sophialai@mxic.com.tw", "sophialai@mxic.com.tw", "EIFTIMAPL02_Monitor_Error", message, "EifTIMAPL02_Monitor_Error", null);
        }  
	    EIFService.updateInterfaceTime("EIFTIMAPL02_Monitor", "LAST_TIME");
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
  private String AlarmMailBody()
  {
	  String message ="";
	  try {
		  message = message + "PROCESS_TYPE = " + rs.getString("PROCESS_TYPE")+ "\n";
		  message = message + "PRODUCT_BODY = " + rs.getString("PRODUCT_BODY")+ "\n";
		  message = message + "OPTIONS = " + rs.getString("OPTIONS")+ "\n";
		  message = message + "PACKAGE_TYPE = " + rs.getString("PACKAGE_TYPE")+ "\n";
		  message = message + "PIN_COUNT = " + rs.getString("PIN_COUNT")+ "\n";
		  message = message + "TEST_MODE = " + rs.getString("TEST_MODE")+ "\n";
		  message = message + "TESTER_TYPE = " + rs.getString("TESTER_TYPE")+ "\n";
		  message = message + "SAP_PLANT_NO = " + rs.getString("SAP_PLANT_NO")+ "\n";
		  message = message + "BODY_SIZE = " + rs.getString("BODY_SIZE")+ "\n";
		  message = message + "SITE = " + rs.getString("SITE")+ "\n";
		  message = message + "CARRIER_TYPE = " + rs.getString("CARRIER_TYPE")+ "\n";
		  message = message + "MARKING_SPEC_NO = " + rs.getString("MARKING_SPEC_NO")+ "\n";
		  message = message + "MARKING_SPEC_VERSION = " + rs.getString("MARKING_SPEC_VERSION")+ "\n";
		  message = message + "INK = " + rs.getString("INK")+ "\n";
		  message = message + "---------------------------------------------------\n";
	  } catch (Exception ex) {
	      TDSLogger.println(ex);
	  } finally {
	  }
    return message;
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
          "' AND APL_STATUS IN ('H','R') ";//('W','R')
      TDSLogger.println(sql);
      pstmt = conn.prepareStatement(sql);
      ResultSet q = pstmt.executeQuery();
      while (q.next()) {
        cnt = Integer.parseInt(q.getString("CNT"));
        break;
      }
      if (cnt > 0) {
        result = true;
        TDSLogger.println("EifTIMAPL02_Monitor Find data " + sql);
      }
      pstmt.close();

    } catch (Exception ex) {
      TDSLogger.println(ex);
    } finally {
    }
    return result;
  }
//取得某一時間區間內 TIM-FT(NVM/MROM/XROM) 廠外Release 的資料
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
    	  "and c.plant_no != 0 \n" + //not include mxic 
    	  "and d.package_type = b.package_type \n" +
    	  "and b.test_type like 'F%' \n" +
    	  "and c.sap_plant_no is not null \n"+
    	  "and e.interface = 'EIFTIMAPL02_Monitor' \n" +
    	  "and a.log_time between e.last_time -(1/24) and e.current_time -(1/24) \n" + //lai-20070308-15/24*60 ";
    	  //"and a.log_time between e.last_time -(3000) and e.current_time -(1/24) \n" + //lai-20070308-15/24*60 ";
    	  "union \n"+
          // for program not in PIM system
          "select distinct 'FT' process_type, a.product_body, b.backend_option, d.prm2_code package_type, \n"+
          "trim(to_char(b.pin_count,'009')) pin_count, b.body_size, b.test_type test_mode, b.tester tester_type, c.sap_plant_no,'"+ft_app_id+"' app_id, 'NA' site, 'NA' carrier_type, 'NA' marking_spec_no, 'NA' marking_spec_version, 'NA' ink  \n"+//
          "from tf_information a, tf_test_parameter_pbc b, ba_plant c, ba_package_type d, if_interface_time e \n"+
          "where a.sid = b.sid \n" + 
          "and a.status = 'R' \n"+
          "and b.site = c.plant_name \n"+
          "and c.plant_no != 0 \n" + //not include mxic 
          "and d.package_type = b.package_type \n"+
          "and b.test_type like 'F%' \n"+
          "and c.sap_plant_no is not null \n"+
          "and e.interface = 'EIFTIMAPL02_Monitor' \n" +
          "and a.log_time between e.last_time -(1/24) and e.current_time -(1/24) \n" + //lai-20070308-15/24*60 ";
          //"and a.log_time between e.last_time -(3000) and e.current_time -(1/24) \n" + //lai-20070308-15/24*60 ";
          "order by product_body ";
     TDSLogger.println("tim-ft sql="+sql);
     PreparedStatement ps = conn.prepareStatement(sql);
     rs = ps.executeQuery();
     

   } catch (Exception e) {
     TDSLogger.println(e);
   } finally {
   }
 }
  
  // Main procedure
  public static void main(String args[])
  {
	EifTIMAPL02_Monitor apl = new EifTIMAPL02_Monitor();
    try	{
   		apl.processTIMAPL02_Monitor("FT");
    } catch(Exception e) {
      TDSLogger.println(e);
    }
  }
}