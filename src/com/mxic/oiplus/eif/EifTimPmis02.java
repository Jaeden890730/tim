/******************************************************************************************************/
//	Author	: 	Robin Mao
//	Date	:	March 21, 2008.
/******************************************************************************************************/
package com.mxic.oiplus.eif;

import java.sql.*;
import java.util.*;
import java.io.*;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.*;

public class EifTimPmis02 extends SQLStatement
{
  private Object[] whereConditions;
  private StringBuffer queryBuffer;
  private Connection conn = null;
  private IF_TF_BOM_ROUTE[] ifbomroute = null;
  private int[] ifbom_seq = null;

  private static String eifName = "EIFTIMPMIS02";
  private static String eifPath = "/EIFDATA/FROMTDS/EIFTIMPMIS02/";
  //private static String eifPath = "d:\\Sophia\\TIM\\EIFDATA\\FROMTDS\\EIFTIMPMIS02\\";

  public EifTimPmis02(){
  }

  public void process()
  {
    try
    {
      conn = DBConnection.getConnection();
      TDSLogger.println(eifName);

      boolean flag = generateDataList();
      //boolean flag = true;
      if (flag){
        output();
      }

      //output_ftTestTime();
    } catch (Exception e){
      TDSLogger.println(e);
    } finally {
      try {
        DBConnection.close(conn);
        conn = null;
      } catch (Exception e){
      }
    }
  }

  // Get all BOM data that was not transferred to PRM II
  private boolean generateDataList()
  {
    String sqlStmt = null;
    int result = 0;
    try
    {
      sqlStmt = "call TIM_PMIS_02()";
      CallableStatement stmt = conn.prepareCall(sqlStmt);
      result = stmt.executeUpdate();
      stmt.close();
    } catch (Exception e) {
      TDSLogger.println(e);
      return false;
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

  private String output_stiring(String value, int length) {
    if (value == null)
      return padding(length);
    else
      return value.trim()+padding(length-value.trim().length());
  }

  private void output()
  {
    FileOutputStream os = null;
    PrintWriter pr = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try
    {
      os = new FileOutputStream(eifPath+"TIMPMIS02-"+(String)DateUtil.Timestamp2String(new java.util.Date(),"yyyyMMdd"));
      pr = new PrintWriter(os);

      queryBuffer = new StringBuffer("select a.product_group_key, a.site, a.product_type, a.product_body, a.mask_option, a.db_with_code, \n");
      queryBuffer.append("a.tester_type, a.ws_route, a.test_route,\n");
      queryBuffer.append("a.step_name, nvl(avg(a.test_time),0) test_time, a.max_site, decode(a.sampling_test,null,' ', a.sampling_test) sampling_test, \n");
      queryBuffer.append("decode(b.apl_status, 'R', 'Release', 'W', 'Wait', ' ') apl_status \n");
      queryBuffer.append("from if_pmis02_03 a, ap_apl b, ba_plant c \n");
      queryBuffer.append("where a.product_body = b.product_body \n");
      queryBuffer.append("and a.mask_option = b.options \n");
      queryBuffer.append("and b.process_type = 'WS' \n");
      queryBuffer.append("and b.package_type = 'W' \n");
      queryBuffer.append("and b.pin_count = '000' \n");
      queryBuffer.append("and b.body_size = 'NA' \n");
      queryBuffer.append("and a.step_name = substr(b.test_mode,1,1)|| 'ORT' || substr(b.test_mode,2) \n");
      queryBuffer.append("and a.plant_no = c.plant_no \n");
      queryBuffer.append("and c.sap_plant_no = b.vendor_no \n");
      queryBuffer.append("group by a.product_group_key, a.site, a.product_type, a.product_body, a.mask_option, \n");
      queryBuffer.append("a.db_with_code, a.tester_type, a.ws_route, a.test_route, a.step_name, \n");
      queryBuffer.append("a.max_site,  sampling_test, b.apl_status \n");
      
      TDSLogger.println(queryBuffer.toString());

      ps = conn.prepareStatement(queryBuffer.toString());
      rs = ps.executeQuery();

      StringBuffer output = new StringBuffer();

      while (rs.next()) {
        output.delete(0,output.length());
        output.append(rs.getString("PRODUCT_GROUP_KEY").trim()+padding(40-rs.getString("PRODUCT_GROUP_KEY").trim().length()));
        output.append(rs.getString("PRODUCT_BODY").trim());
        output.append(rs.getString("MASK_OPTION").trim());
        output.append(rs.getString("DB_WITH_CODE").trim()+padding(1-rs.getString("DB_WITH_CODE").trim().length()));
        output.append(rs.getString("WS_ROUTE").trim()+padding(12-rs.getString("WS_ROUTE").trim().length()));
        output.append(rs.getString("TEST_ROUTE").trim()+padding(255-rs.getString("TEST_ROUTE").trim().length()));
        output.append(rs.getString("STEP_NAME").trim()+padding(6-rs.getString("STEP_NAME").trim().length()));
        output.append(rs.getString("TESTER_TYPE").trim()+padding(15-rs.getString("TESTER_TYPE").trim().length()));
        output.append(rs.getString("SITE").trim()+padding(15-rs.getString("SITE").trim().length()));
        output.append(rs.getString("APL_STATUS").trim()+padding(10-rs.getString("APL_STATUS").trim().length()));
        output.append(rs.getString("MAX_SITE").trim()+padding(5-rs.getString("MAX_SITE").trim().length()));
        output.append(rs.getString("SAMPLING_TEST").trim()+padding(5-rs.getString("SAMPLING_TEST").trim().length()));
        output.append(rs.getString("TEST_TIME").trim()+padding(6-rs.getString("TEST_TIME").trim().length()));
        //output.append(rs.getString("PRODUCT_TYPE").trim()+padding(5-rs.getString("PRODUCT_TYPE").trim().length()));
        //output.append(rs.getString("PROGRAM_ID").trim()+padding(10-rs.getString("PROGRAM_ID").trim().length()));
        //output.append(rs.getString("BODY_SIZE").trim()+padding(40-rs.getString("BODY_SIZE").trim().length()));
        
        pr.println(output.toString());
        //TDSLogger.println(output.toString());
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

  public void output_ftTestTime() {
	  FileOutputStream os = null;
	  PrintWriter pr = null;

	  queryBuffer = new StringBuffer("SELECT T.*, P.PLANT_NAME FROM SE_SAP_INDEX_TIME_FT T, BA_PLANT P ");
	  queryBuffer.append("WHERE T.SAP_PLANT_NO = P.SAP_PLANT_NO ORDER BY T.SAP_PLANT_NO");
	  try {
		  os = new FileOutputStream(eifPath+"TIMPMIS02-FTTIME-"+DateUtil.Timestamp2String(new java.util.Date(),"yyyyMMdd"));
		  pr = new PrintWriter(os);
		  HashMap[] rs = new DataHandlerUtil().getDataBySql(conn, queryBuffer.toString());
		  if (rs != null && rs.length > 0) {
			  for (int i=0; i<rs.length; i++) {
				  pr.print(rs[i].get("PLANT_NAME") + ",");
				  pr.print(rs[i].get("TESTER_TYPE") + ",");
				  pr.print(rs[i].get("PIN_COUNT") + ",");
				  pr.print(rs[i].get("PKG_CODE") + ",");
				  pr.print(rs[i].get("SITE") + ",");
				  pr.print(rs[i].get("CYCLE_TIME") + ",");
				  pr.println(rs[i].get("INDEX_TIME") + ",");
			  }
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
  // Main procedure
  public static void main(String args[])
  {
    EifTimPmis02 tim = new EifTimPmis02();

    try	{
      tim.process();
    } catch(Exception e) {
      TDSLogger.println(e);
    }
  }
}