/******************************************************************************************************/
//	Author	: 	Robin Mao
//	Date	:	Mar 15, 2007.
//	Purpose	:	1. Create Speed BIN file (BIN Type=1) for PRM2
//                      2. Create Down Grade BIN file (BIN Type=2) for SAP
//      Modify  :       May 11, 2007.
//                      for Transfer Down Grade BIN to SAP
//	Remarks	:	Copy from com.mxic.tdsplus.eif
/******************************************************************************************************/
package com.mxic.oiplus.eif;

import java.sql.*;
import java.util.LinkedHashMap;
import java.io.*;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.*;
import com.mxic.tdsplus.util.SapEncoding;

public class EifTIMPRMII02 extends SQLStatement
{
  private StringBuffer queryBuffer;
  private Connection conn = null;
  private ResultSet rs = null;
  private static String PRMIIeifName = "EIFTIMPRMII02";
  private static String SAPeifName = "EIFTIMSAP02";
  private static String eifPRMIIPath = "/EIFDATA/FROMTDS/EIFTIMPRMII02/";
  private static String eifSAPPath = "/EIFDATA/FROMTDS/EIFTIMSAP02/";
	//private static String eifPRMIIPath = "D:\\EIFDATA\\FROMTDS\\EIFTIMPRMII02\\";
	//private static String eifSAPPath = "D:\\EIFDATA\\FROMTDS\\EIFTIMSAP02\\";
  private LogWriter log=null;

  public EifTIMPRMII02(){
  }

  public void processEifTIMPRMII02()
  {
    try
    {
      log = new LogWriter(PRMIIeifName);
      conn = DBConnection.getConnection();
      log.Println(PRMIIeifName);
      boolean flag = selectBasicDataforPRMII(conn);
      if (flag){
        outputPRMII();
      } else
        log.Println( "Nothing to do !!" );
    } catch (Exception e){
      TDSLogger.println(e);
      log.Println( "Select Basic Data for PRMII Exception !!" );
    } finally {
      try {
        DBConnection.close(conn);
        conn = null;
      } catch (Exception e){
      }
    }
    log.close();
    log = null;
    try
    {
      log = new LogWriter(SAPeifName);
      conn = DBConnection.getConnection();
      log.Println(SAPeifName);
      boolean flag = selectBasicDataforSAP(conn);
      if (flag){
        outputSAP();
      } else
        log.Println( "Nothing to do !!" );
    } catch (Exception e){
      TDSLogger.println(e);
      log.Println( "Select Basic Data for SAP Exception !!" );
    } finally {
      try {
        DBConnection.close(conn);
        conn = null;
      } catch (Exception e){
      }
    }
    log.close();
    log = null;
  }

  // Get all Basic data that was not transferred to PRM II
  private boolean selectBasicDataforPRMII(Connection conn)
  {
    boolean result = false;
    try
    {
      queryBuffer = new StringBuffer("SELECT SEQ,ACTION,TO_CHAR(LOG_DATE,'yyyymmddhh24miss') LOG_DATE, ");
      queryBuffer.append("PRODUCT_BODY,ROUTE_NAME,TESTER,OPTIONS,GRADE,GOOD_BIN,EPN_SPEED,KTD_BIN_FLAG FROM IF_TF_BASIC_INFO ");
      //queryBuffer.append(" WHERE TRANS = 'N' AND BIN_TYPE = 1 ORDER BY SEQ");
      queryBuffer.append(" WHERE TRANS = 'N' AND( BIN_TYPE = 1 OR KTD_BIN_FLAG IS NOT NULL) ORDER BY SEQ");

      PreparedStatement ps = conn.prepareStatement(queryBuffer.toString());
      rs = ps.executeQuery();
      // If there is data, isBeforeFirst() will return true
      result = rs.isBeforeFirst();
    } catch (Exception e) {
      TDSLogger.println(e);
      log.Println("Exception while selectBasicDataforPRMII() !" );
      return result;
    } finally {
    }
    return result;
  }

  /*private void outputPRMII()
  {
    FileOutputStream os=null;
    PrintWriter pr=null;
    try
    {
      os = new FileOutputStream(eifPRMIIPath+"TIM02-"+(String)DateUtil.Timestamp2String(new java.util.Date(),"yyyyMMddkkmm"));
      pr = new PrintWriter(os);

      String reasonCode = "";
      StringBuffer output = new StringBuffer();
      int CCode = 0;
      log.Println("EifTIMPRMII02-PRMII-SNDMSG--------");
      while (rs.next())
      {
        output.delete(0,output.length());
        output.append(rs.getString("SEQ").trim()+padding(10-rs.getString("SEQ").trim().length()));
        output.append(rs.getString("ACTION"));
//        output.append(rs.getString("LOG_DATE"));
        output.append(rs.getString("PRODUCT_BODY"));
        output.append(rs.getString("ROUTE_NAME")+padding(12-rs.getString("ROUTE_NAME").length()));
        output.append(rs.getString("TESTER")+padding(6-rs.getString("TESTER").length()));  //長度由 5 改為 6 , test mode 碼數擴增專案
        output.append(rs.getString("OPTIONS"));
        output.append(rs.getString("GRADE"));
        output.append(rs.getString("GOOD_BIN"));
        if(rs.getString("EPN_SPEED") == null || "null".equals(rs.getString("EPN_SPEED"))){
        	output.append("   ");	
        }else {
        	output.append(rs.getString("EPN_SPEED")+padding(3-rs.getString("EPN_SPEED").length()));	
        }
        
        if(rs.getString("KTD_BIN_FLAG") == null || "null".equals(rs.getString("KTD_BIN_FLAG"))){
        	output.append("    ");	
        }else {
        output.append(rs.getString("KTD_BIN_FLAG")+padding(4-rs.getString("KTD_BIN_FLAG").length()));
        }
        pr.println(output.toString());
        setTrans(rs.getString("SEQ"));
        log.Println(rs.getString("SEQ"));
      }
      log.Println("--SUCCESS!!!");
    } catch (Exception e) {
      TDSLogger.println(e);
      log.Println("Exception while outputPRMII() inner !");
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
        log.Println("Exception while outputPRMII() outer !");
      }
    }
  }*/

  //#217139
  private void outputPRMII() {
		FileOutputStream os = null;
		PrintWriter pr = null;
		try {
			os = new FileOutputStream(eifPRMIIPath
					+ "TIM02-"
					+ (String) DateUtil.Timestamp2String(new java.util.Date(),
							"yyyyMMddkkmm"));
			pr = new PrintWriter(os);

			StringBuffer output = new StringBuffer();
			log.Println("EifTIMPRMII02-PRMII-SNDMSG--------");
			while (rs.next()) {
				LinkedHashMap<String, String> info = new LinkedHashMap<String, String>();
				info.put("SEQ", rs.getString("SEQ").trim());
				info.put("ACTION", rs.getString("ACTION"));
				info.put("PRODUCT_BODY", rs.getString("PRODUCT_BODY"));
				info.put("ROUTE_NAME", rs.getString("ROUTE_NAME"));
				info.put("TESTER", rs.getString("TESTER"));
				info.put("OPTIONS", rs.getString("OPTIONS"));
				info.put("GRADE", rs.getString("GRADE"));
				info.put("GOOD_BIN", rs.getString("GOOD_BIN"));
				if (rs.getString("EPN_SPEED") == null
						|| "null".equals(rs.getString("EPN_SPEED"))) {
				} else {
					info.put("EPN_SPEED", rs.getString("EPN_SPEED"));
				}

				if (rs.getString("KTD_BIN_FLAG") == null
						|| "null".equals(rs.getString("KTD_BIN_FLAG"))) {
					info.put("KTD_BIN_FLAG", "");
				} else {
					info.put("KTD_BIN_FLAG", rs.getString("KTD_BIN_FLAG"));
				}
				output = SapEncoding.formatOutput(info, PRMIIeifName);
				pr.print(output.toString());
				setTrans(rs.getString("SEQ"));
				log.Println(rs.getString("SEQ"));
			}
			log.Println("--SUCCESS!!!");
		} catch (Exception e) {
			TDSLogger.println(e);
			log.Println("Exception while outputPRMII() inner !");
		} finally {
			try {
				if (pr != null)
					pr.close();
				if (os != null)
					os.close();
				pr = null;
				os = null;
			} catch (Exception ex) {
				TDSLogger.println(ex);
				log.Println("Exception while outputPRMII() outer !");
			}
		}
	}
  
  // Get all Basic data that was not transferred to SAP
  private boolean selectBasicDataforSAP(Connection conn)
  {
    boolean result = false;
    try
    {
      queryBuffer = new StringBuffer("SELECT SEQ, LOG_DATE, PRODUCT_BODY,OPTIONS,ROUTE_NAME,TESTER,GOOD_BIN ");
      queryBuffer.append("FROM (SELECT max(seq) seq, max(TO_CHAR(LOG_DATE,'yyyymmddhh24miss')) LOG_DATE, ");
      queryBuffer.append("PRODUCT_BODY,OPTIONS,ROUTE_NAME,TESTER,GOOD_BIN,KTD_BIN_FLAG FROM IF_TF_BASIC_INFO ");
      queryBuffer.append("WHERE TRANS = 'N' AND BIN_TYPE = 2 ");
      queryBuffer.append("group by PRODUCT_BODY,OPTIONS,ROUTE_NAME,TESTER,GOOD_BIN,KTD_BIN_FLAG) ORDER BY SEQ");

      PreparedStatement ps = conn.prepareStatement(queryBuffer.toString());
      rs = ps.executeQuery();
      // If there is data, isBeforeFirst() will return true
      result = rs.isBeforeFirst();
    } catch (Exception e) {
      TDSLogger.println(e);
      log.Println("Exception while selectBasicDataforSAP() !" );
      return result;
    } finally {
    }
    return result;
  }

  /*private void outputSAP()
 {
   FileOutputStream os=null;
   PrintWriter pr=null;
   try
   {
     os = new FileOutputStream(eifSAPPath+"TIM03-"+(String)DateUtil.Timestamp2String(new java.util.Date(),"yyyyMMddkkmm"));
     pr = new PrintWriter(os);

     String reasonCode = "";
     StringBuffer output = new StringBuffer();
     int CCode = 0;
     log.Println("EifTIMPRMII02-SAP-SNDMSG--------");
     while (rs.next())
     {
       output.delete(0,output.length());
       output.append(rs.getString("SEQ").trim()+padding(10-rs.getString("SEQ").trim().length()));
       output.append(rs.getString("PRODUCT_BODY"));
       output.append(rs.getString("OPTIONS"));
       output.append(rs.getString("ROUTE_NAME")+padding(5-rs.getString("ROUTE_NAME").length()));
       output.append(rs.getString("TESTER")+padding(6-rs.getString("TESTER").length()));  //長度由 5 改為 6 , test mode 碼數擴增專案
       output.append(rs.getString("GOOD_BIN"));
       pr.println(output.toString());
       setTrans(rs.getString("SEQ"));
       log.Println(rs.getString("SEQ"));
     }
     log.Println("--SUCCESS!!!");
   } catch (Exception e) {
     TDSLogger.println(e);
     log.Println("Exception while outputSAP() inner !");
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
       log.Println("Exception while outputPRMII() outer !");
     }
   }
 }*/

  private void outputSAP() {
		FileOutputStream os = null;
		PrintWriter pr = null;
		try {
			os = new FileOutputStream(eifSAPPath
					+ "TIM03-"
					+ (String) DateUtil.Timestamp2String(new java.util.Date(),
							"yyyyMMddkkmm"));
			pr = new PrintWriter(os);

			String reasonCode = "";
			StringBuffer output = new StringBuffer();
			int CCode = 0;
			log.Println("EifTIMPRMII02-SAP-SNDMSG--------");
			while (rs.next()) {
				LinkedHashMap<String, String> info = new LinkedHashMap<String, String>();
				output.delete(0, output.length());
				info.put("SEQ", rs.getString("SEQ").trim());
				info.put("PRODUCT_BODY", rs.getString("PRODUCT_BODY"));
				info.put("OPTIONS", rs.getString("OPTIONS"));
				info.put("ROUTE_NAME", rs.getString("ROUTE_NAME"));
				info.put("TESTER", rs.getString("TESTER"));
				info.put("GOOD_BIN", rs.getString("GOOD_BIN"));
				output = SapEncoding.formatOutput(info, SAPeifName);
				pr.print(output.toString());
				setTrans(rs.getString("SEQ"));
				log.Println(rs.getString("SEQ"));
			}
			log.Println("--SUCCESS!!!");
		} catch (Exception e) {
			TDSLogger.println(e);
			log.Println("Exception while outputSAP() inner !");
		} finally {
			try {
				if (pr != null)
					pr.close();
				if (os != null)
					os.close();
				pr = null;
				os = null;
			} catch (Exception ex) {
				TDSLogger.println(ex);
				log.Println("Exception while outputPRMII() outer !");
			}
		}
	}
  
 // Set Trans = 'Y' to record that this data identified by seq was transferred
  private void setTrans(String seq)
  {
//if (1==1) return;
    PreparedStatement stmt = null;
    String sqlstr = "update if_tf_basic_info set trans = 'Y' where seq = ?";
    try {
      stmt = conn.prepareStatement(sqlstr);
      stmt.setInt(1,Integer.parseInt(seq));
      int succ = stmt.executeUpdate();
    } catch(Exception e){
      TDSLogger.println(e);
      log.Println("Exception while setTrans() !");
    } finally {
      try {
        stmt.close();
      } catch (Exception e) {
        TDSLogger.println(e);
        log.Println("Exception while setTrans() stmt.close()!");
      }
    }
  }

  // generate a 'blank' string with length = 'width'
  /*private String padding(int width) {
    String str = "";

    for (int i = 0; i < width; i++)
      str = str + " ";

    return str;
  }*/

  // Main procedure
  public static void main(String args[])
  {
    EifTIMPRMII02 prm = new EifTIMPRMII02();

    try	{
      prm.processEifTIMPRMII02();
    } catch(Exception e) {
      TDSLogger.println(e);
    }
  }
}
