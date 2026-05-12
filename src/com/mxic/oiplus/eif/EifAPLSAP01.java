/******************************************************************************************************/
//	Author	: 	Robin Mao
//	Date	:	May 19, 2006.
//      Modify  :       April 09, 2007, Remove body size
/******************************************************************************************************/
package com.mxic.oiplus.eif;

import java.sql.*;
import java.util.*;
import java.io.*;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.*;
import com.mxic.oiplus.apl.bean.APLSearch;
import com.mxic.oiplus.apl.bean.APL.AP_APL;
import com.mxic.oiplus.apl.bean.APL;
import com.mxic.oiplus.apl.APLUtil;
import com.mxic.oiplus.resource.*;
import com.mxic.tdsplus.util.SapEncoding;

public class EifAPLSAP01 extends SQLStatement
{
  private Connection connection = null;
  AP_APL[] apl = null;

  private static String eifName = "EifAPLSAP01";
  private static String eifPath = "/EIFDATA/FROMTDS/EIFAPLSAP01/";
  //private static String eifPath = "d:\\PEIS_DATA\\EIFDATA\\FROMTDS\\EIFAPLSAP01\\";

  public EifAPLSAP01(){
  }

  public void processEifAPLSAP01()
  {
    try
    {
      connection = DBConnection.getConnection();
      if (EIFService.updateInterfaceTime("EIFAPLSAP01", "CURRENT_TIME") == 1) {
	      boolean flag = selectAPLData();
	      if (flag){
	        output();
	      } else
	        TDSLogger.println( "Nothing to do !!" );
	      EIFService.updateInterfaceTime("EIFAPLSAP01", "LAST_TIME");
      }    
    } catch (Exception e){
      TDSLogger.println(e);
    } finally {
      try {
        connection.close();
        connection = null;
      } catch (Exception e){
      }
    }
  }

  // Get all APL data
  private boolean selectAPLData()
  {
    String interval = (String)TDSResource.getProperties("EIF").get("to.sap.cron.interval");
    
    try
    {
      String sql =
          "select  "+
          "'WS' process_type,a.product_body,a.options,a.vendor_no,a.package_type, a.pin_count, a.body_size, "+
          //"apl_is_released('WS',a.product_body,a.options,a.vendor_no,null,null,null) APL_STATUS "+
          "a.apl_status, substr(a.test_mode,1,1)||'ORT'||substr(a.test_mode,2) test_mode, a.tester_type, "+
          "a.site, 'NA' ink, 'NA' carrier_type, 'NA' marking_spec_no, 'NA' marking_spec_version, a.isexists8049 "+
          "from ap_apl a, if_interface_time e "+
          "where substr(a.test_mode,1,1) = 'S' and a.process_type = 'WS' "+
          "and e.interface = 'EIFAPLSAP01' \n" +
    	  "and a.log_time between e.last_time and e.current_time \n" + //lai-20070308-15/24*60 ";
          //20111130"and a.log_time > sysdate - "+ interval +" /1440 "+
          //"and a.log_time > sysdate - 200 "+ //interval +" /1440 "
          //"group by a.product_body,a.options,a.vendor_no "+
          "union "+
          "select 'FT' process_type, a.product_body,a.options,a.vendor_no,a.package_type, "+
          "a.pin_count, a.body_size, "+
          //"apl_is_released('FT',a.product_body,a.options,a.vendor_no,a.package_type,a.pin_count,null) APL_STATUS "+
          "a.apl_status, a.test_mode, a.tester_type, "+
          "'NA' site, 'NA' ink, 'NA' carrier_type, 'NA' marking_spec_no, 'NA' marking_spec_version, a.isexists8049 "+
          "from ap_apl a, if_interface_time e "+
          "where substr(a.test_mode,1,1) in ('F','O') and a.process_type = 'FT' "+
          "and e.interface = 'EIFAPLSAP01' \n" +
    	  "and a.log_time between e.last_time and e.current_time \n" + //lai-20070308-15/24*60 ";
          //20111130"and a.log_time > sysdate - "+ interval +" /1440 "+
          //"and a.log_time > sysdate - 200 "+ //interval +" /1440 "
          //"group by a.product_body,a.options,a.vendor_no,a.package_type, a.pin_count"+
          "union "+
          "select 'AVI' process_type, a.product_body,a.options,a.vendor_no,a.package_type, "+
          "a.pin_count, 'NA' body_size, "+
          "a.apl_status, 'NA' test_mode, a.tester_type, "+
          "'NA' site, a.ink, 'NA' carrier_type, 'NA' marking_spec_no, 'NA' marking_spec_version, a.isexists8049 "+
          "from ap_apl a, if_interface_time e "+
          "where a.process_type = 'AVI' "+
          "and e.interface = 'EIFAPLSAP01' \n" +
    	  "and a.log_time between e.last_time and e.current_time \n" + //lai-20070308-15/24*60 ";
          //20111130"and a.log_time > sysdate - "+ interval +" /1440 "+
          "union "+
          "select 'FVI' process_type, a.product_body,a.options,a.vendor_no,a.package_type, "+
          "a.pin_count, a.body_size, "+
          "a.apl_status, 'NA' test_mode, a.tester_type, "+
          "'NA' site, 'NA' ink, substr(a.carrier_type,1,2) carrier_type, 'NA' marking_spec_no, 'NA' marking_spec_version, a.isexists8049 "+
          "from ap_apl a, if_interface_time e "+
          "where a.process_type = 'FVI' "+
          "and e.interface = 'EIFAPLSAP01' \n" +
    	  "and a.log_time between e.last_time and e.current_time \n" + //lai-20070308-15/24*60 ";
          //20111130"and a.log_time > sysdate - "+ interval +" /1440 "+
          "union "+
          "select 'MARK' process_type, a.product_body,a.options,a.vendor_no,a.package_type, "+
          "a.pin_count, a.body_size, "+
          "a.apl_status, 'NA' test_mode, a.tester_type, "+
          "'NA' site, 'NA' ink, 'NA' carrier_type, a.marking_spec_no, a.marking_spec_version, a.isexists8049 "+
          "from ap_apl a, if_interface_time e "+
          "where a.process_type = 'MARK' "+
          "and e.interface = 'EIFAPLSAP01' \n" +
    	  "and a.log_time between e.last_time and e.current_time \n" + //lai-20070308-15/24*60 ";
          //20111130"and a.log_time > sysdate - "+ interval +" /1440 "+
          " ";
        TDSLogger.println(sql);
//      APLSearch search = new APLSearch("data", "query");
//      HashMap queryItems = new HashMap();
//      queryItems.put("1","1");
//      result = search.execute(connection, queryItems, null);
      apl = (AP_APL[]) APLUtil.getData(connection,sql,AP_APL.class,null).toArray(new AP_APL[0]);
      if (apl.length == 0)
        return false;
    } catch (Exception e) {
      TDSLogger.println(e);
    } finally {
    }
    return true;
  }

  // generate a 'blank' string with length = 'width'
  private String padding(int width) {
    String str = "";

    for (int i = 0; i < width; i++)
      str = str + " ";

    return str;
  }

  private void output()
  {
    FileOutputStream os = null;
    PrintWriter pr = null;
    try
    {
      os = new FileOutputStream(eifPath+"APL01-"+(String)DateUtil.Timestamp2String(new java.util.Date(),"yyyyMMddHHmm"));
      pr = new PrintWriter(os);

      String reasonCode = "";
      StringBuffer output = new StringBuffer();
      int CCode = 0;

      for ( int i = 0; i < apl.length; i++ ) {
    	  
    	  pr.write(genStr(apl[i],eifName));

//        pr.println(output.toString());
        TDSLogger.println("EifAPLSAP01-SNDMSG--------");
        TDSLogger.println(output.toString());

        TDSLogger.println("--SUCCESS!!!");
        TDSLogger.println("--SUCCESS Update Trans Flag!!!");
      }
    } catch (Exception e) {
      TDSLogger.println(e);
    } finally {
      try{
        if(pr!=null)
          pr.close();
        if(os!=null)
          os.close();
        pr=null;
        os=null;
      }catch(Exception ex){
        TDSLogger.println(ex);
      }
    }
  }
  public String formatString(String buf, int length) {
      String result = "";
      String space = "";
      for(int i=0; i<length; i++){
          space +=" ";
      }
      result = buf + space ;
      return result.substring(0, length);
}
  
  
  
  private static String genStr(AP_APL apl,String eifName){
  	
  	StringBuffer result = new StringBuffer();
  	
  	LinkedHashMap<String,String> info = new LinkedHashMap<String, String>();
  	info.put("PROCESS_TYPE", safeToString(apl.PROCESS_TYPE));
  	info.put("PRODUCT_BODY", safeToString(apl.PRODUCT_BODY));
  	info.put("OPTIONS", safeToString(apl.OPTIONS));
  	info.put("VENDOR_NO", safeToString(apl.VENDOR_NO));
  	
    if (apl.PROCESS_TYPE.equals("WS")) {
    	info.put("PACKAGE_TYPE", null);
    	info.put("PIN_COUNT", null);
    	info.put("BODY_SIZE", null);
    }else{
    	info.put("PACKAGE_TYPE", safeToString(apl.PACKAGE_TYPE));
    	info.put("PIN_COUNT", safeToString(apl.PIN_COUNT));
    	info.put("BODY_SIZE", safeToString(apl.BODY_SIZE));
    }
      	
  	info.put("APL_STATUS", safeToString(apl.APL_STATUS));
  	info.put("TEST_MODE", safeToString(apl.TEST_MODE));
  	info.put("TESTER_TYPE", safeToString(apl.TESTER_TYPE));
  	info.put("SITE", safeToString(apl.SITE));
  	info.put("INK", safeToString(apl.INK));
  	info.put("CARRIER_TYPE", safeToString(apl.CARRIER_TYPE));
  	info.put("MARKING_SPEC_NO", safeToString(apl.MARKING_SPEC_NO));
  	info.put("MARKING_SPEC_VERSION", safeToString(apl.MARKING_SPEC_VERSION));
  	info.put("ISEXISTS8049", safeToString(apl.ISEXISTS8049));
  	
  	if("".equals(safeToString(apl.ISEXISTS8049))){
  		info.put("ISEXISTS8049", "null");
  	}
  	
  	result = SapEncoding.formatOutput(info, eifName);
  	
  	
//        StringBuffer res = new StringBuffer();
//             
//        res.append(rpad(StringUtil.formatNull(hm.get("SID")).toString(), 10));    
//        res.append(rpad(StringUtil.formatNull(hm.get("SAP_PLANT_NO")).toString(), 10));
//        res.append(rpad(StringUtil.formatNull(hm.get("PROGRAM_MODE")).toString(), 6));
//        res.append(rpad(StringUtil.formatNull(hm.get("PROGRAM_ID")).toString(), 10));
//        res.append(rpad(StringUtil.formatNull(hm.get("PRODUCT_CODE")).toString(), 10));
//        res.append(rpad(StringUtil.formatNull(hm.get("TEST_MODE")).toString(), 4));
//        res.append(rpad(StringUtil.formatNull(hm.get("PROGRAM_NAME")).toString(), 30));
//        res.append(rpad(StringUtil.formatNull(hm.get("TESTER_TYPE")).toString(), 15));
//        res.append(rpad(StringUtil.formatNull(hm.get("PRM2_CODE")).toString(), 2));
//        res.append(rpad(StringUtil.formatNull(hm.get("PIN_COUNT")).toString(), 6));
//        res.append(rpad(StringUtil.formatNull(hm.get("MAX_SITE")).toString(), 6));
//        res.append(rpad(StringUtil.formatNull(hm.get("TEST_TIME")).toString(), 9));
//        res.append(rpad(StringUtil.formatNull(hm.get("PROGRAM_STATUS")).toString(), 2));
//        res.append(rpad(StringUtil.formatNull(hm.get("APROVAL_DATE")).toString(), 19));
//        res.append(rpad(StringUtil.formatNull(hm.get("EXPIRE_DATE")).toString(), 19));
//        res.append(rpad(StringUtil.formatNull(hm.get("BE_APROVAL_DATE")).toString(), 19));
//	      res.append("\n");
	      
      return result.toString();
    }
  private static String safeToString(Object value) {
	    return value == null ? "" : value.toString();
	}
  // Main procedure
  public static void main(String args[])
  {
    EifAPLSAP01 sap = new EifAPLSAP01();

    try	{
      sap.processEifAPLSAP01();
    } catch(Exception e) {
      TDSLogger.println(e);
    }
  }
}
