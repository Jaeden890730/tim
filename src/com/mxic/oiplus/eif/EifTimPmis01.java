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

public class EifTimPmis01 extends SQLStatement
{
  private Object[] whereConditions;
  private StringBuffer queryBuffer;
  private Connection conn = null;
  private IF_TF_BOM_ROUTE[] ifbomroute = null;
  private int[] ifbom_seq = null;

  private static String eifName = "EIFTIMPMIS01";
  private static String eifPath = "/EIFDATA/FROMTDS/EIFTIMPMIS01/";
//  private static String eifPath = "D:\\temp\\";

  public EifTimPmis01(){
  }

  public void process()
  {
    try
    {
      conn = DBConnection.getConnection();
      TDSLogger.println(eifName);

      boolean flag = generateDataList();
      if (flag){
        output();
      }

      output_ftTestTime();
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
      sqlStmt = "call TIM_PMIS_01()";
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
      os = new FileOutputStream(eifPath+"TIMPMIS01-"+(String)DateUtil.Timestamp2String(new java.util.Date(),"yyyyMMdd"));
      pr = new PrintWriter(os);

      queryBuffer = new StringBuffer("select a.program_id, a.site, a.product_type, a.product_body, a.backend_option,\n");
      queryBuffer.append("a.pin_count, a.prm2_code, a.tester_type, NVL(a.body_size, ' ') body_size, a.ft_route, a.test_route,\n");
      queryBuffer.append("a.step_name, a.test_time, a.max_site\n");
      queryBuffer.append("from if_pmis01_03 a\n");

      ps = conn.prepareStatement(queryBuffer.toString());
      rs = ps.executeQuery();

      StringBuffer output = new StringBuffer();

      while (rs.next()) {
        output.delete(0,output.length());
        output.append(rs.getString("PROGRAM_ID").trim()+padding(10-rs.getString("PROGRAM_ID").trim().length()));
        output.append(rs.getString("SITE").trim()+padding(15-rs.getString("SITE").trim().length()));
        output.append(rs.getString("PRODUCT_TYPE").trim()+padding(5-rs.getString("PRODUCT_TYPE").trim().length()));
        output.append(rs.getString("PRODUCT_BODY").trim());
        output.append(rs.getString("BACKEND_OPTION").trim());
        output.append(rs.getString("PIN_COUNT").trim()+padding(3-rs.getString("PIN_COUNT").trim().length()));
        output.append(rs.getString("PRM2_CODE").trim()+padding(2-rs.getString("PRM2_CODE").trim().length()));
        output.append(rs.getString("TESTER_TYPE").trim()+padding(15-rs.getString("TESTER_TYPE").trim().length()));
        output.append(rs.getString("BODY_SIZE").trim()+padding(40-rs.getString("BODY_SIZE").trim().length()));
        output.append(rs.getString("FT_ROUTE").trim()+padding(12-rs.getString("FT_ROUTE").trim().length()));
        output.append(rs.getString("TEST_ROUTE").trim()+padding(255-rs.getString("TEST_ROUTE").trim().length()));
        output.append(rs.getString("STEP_NAME").trim()+padding(6-rs.getString("STEP_NAME").trim().length()));
        output.append(rs.getString("TEST_TIME").trim()+padding(6-rs.getString("TEST_TIME").trim().length()));
        output.append(rs.getString("MAX_SITE").trim()+padding(5-rs.getString("MAX_SITE").trim().length()));
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
		  os = new FileOutputStream(eifPath+"TIMPMIS01-FTTIME-"+DateUtil.Timestamp2String(new java.util.Date(),"yyyyMMdd"));
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
    EifTimPmis01 tim = new EifTimPmis01();

    try	{
      tim.process();
    } catch(Exception e) {
      TDSLogger.println(e);
    }
  }
}