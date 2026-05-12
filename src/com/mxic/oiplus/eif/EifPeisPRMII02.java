/******************************************************************************************************/
//	Author	: 	Robin Mao
//	Date	:	May 19, 2006.
//              2009/04/07, Robin, For FT Special Control info
//			    2009/09/21, Robin, For PRM3 trigger to invoke auto IPN assign process,
//						    Add "ending mark" XXXXXXXXXX
//  Record length : 875
/******************************************************************************************************/
package com.mxic.oiplus.eif;

import java.sql.*;
import java.util.LinkedHashMap;
import java.io.*;

//import com.mxic.oiplus.eif.lotend.*;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.*;
import com.mxic.tdsplus.util.SapEncoding;

public class EifPeisPRMII02 extends SQLStatement
{
  private Object[] whereConditions;
  private StringBuffer queryBuffer;
  private Connection conn = null;
  private IF_TF_BOM_ROUTE[] ifbomroute = null;
//  private int[] ifbom_seq = null;

  private static String eifName = "EIFPEISPRMII02";
  private static String eifPath = "/EIFDATA/FROMTDS/EIFPEISPRMII02/";
  //private static String eifPath = "D:\\EIFDATA\\FROMTDS\\EIFPEISPRMII02\\";

  private static int recordLength = 875;

  public EifPeisPRMII02(){
  }

  public void processPEISPRMII02()
  {
    try
    {
      conn = DBConnection.getConnection();
      TDSLogger.println(eifName);
      boolean flag = selectBOMData();
      if (flag){
        output();
      } else
        TDSLogger.println( "Nothing to do !!" );
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
  private boolean selectBOMData()
  {
    DataHandlerUtil dataUtil = new DataHandlerUtil();
    try
    {
      whereConditions = null;

      queryBuffer = new StringBuffer("SELECT SEQ,RECTYPE,ACTION,TO_CHAR(LOG_DATE,'yyyymmddhh24miss') LOG_DATE, \n");
      queryBuffer.append("PRODUCT_BODY,BRAND,BACKEND_OPTION,NVL(FG_WITH_CODE,' ') FG_WITH_CODE,\n");
      queryBuffer.append("TRIM(TO_CHAR(PIN_COUNT, '000')) PIN_COUNT,\n");
      queryBuffer.append("PACKAGE_TYPE,\n");
      queryBuffer.append("NVL(FT_ROUTE_CODE,' ') FT_ROUTE_CODE, \n");
      queryBuffer.append("FT_ROUTE,NVL(FT_ROUTE_ADD,' ') FT_ROUTE_ADD,\n");
      queryBuffer.append("NVL(TF_COMMENT,' ') TF_COMMENT,\n");
      //queryBuffer.append("MASK_OPTION,\n");
      queryBuffer.append("NVL(SORT_ROUTE_CODE,' ') SORT_ROUTE_CODE, \n");
      queryBuffer.append("NVL(DB_WITH_CODE,' ') DB_WITH_CODE,\n");
      queryBuffer.append("WS_ROUTE, NVL(WS_ROUTE_ADD,' ') WS_ROUTE_ADD,\n");
      queryBuffer.append("NVL(TF_WS_COMMENT,' ') TF_WS_COMMENT,\n");
      queryBuffer.append("GET_8049OI_STEP_LIST(WS_ROUTE) WS_STEP,\n");
      queryBuffer.append("GET_8049OI_STEP_LIST(WS_ROUTE_ADD) WS_ADD_STEP,\n");
      queryBuffer.append("GET_8049OI_STEP_LIST(FT_ROUTE) FT_STEP,\n");
      queryBuffer.append("GET_8049OI_STEP_LIST(FT_ROUTE_ADD) FT_ADD_STEP ,\n");
      queryBuffer.append("A.SALES_FORM,\n");
      queryBuffer.append("A.BODY_REV,\n");
      //queryBuffer.append("A.MASK_OPTION_REV,\n");
      queryBuffer.append("A.CODE_NO,\n");
      queryBuffer.append("B.ROUTE_CAT ROUTE_TYPE,\n");
      queryBuffer.append("A.WS_ROUTE_ADD1,\n");
      queryBuffer.append("A.FT_ROUTE_ADD1,\n");
      queryBuffer.append("A.FT_ROUTE_ADD2,\n");
      queryBuffer.append("GET_8049OI_STEP_LIST(WS_ROUTE_ADD1) WS_ROUTE_ADD1_STEP ,\n");
      queryBuffer.append("GET_8049OI_STEP_LIST(FT_ROUTE_ADD1) FT_ROUTE_ADD1_STEP ,\n");
      queryBuffer.append("GET_8049OI_STEP_LIST(FT_ROUTE_ADD2) FT_ROUTE_ADD2_STEP ,\n");
      queryBuffer.append("A.PRODUCT_TYPE,\n");
      queryBuffer.append("A.ENDURANCE,\n");
      queryBuffer.append("A.WSSPECIALCONTROL,\n");
      queryBuffer.append("TF_CHECK_STEP('AVI',A.WS_ROUTE) AVI,\n");
      queryBuffer.append("DECODE(TF_CHECK_STEP('INK',A.WS_ROUTE),'Y','Y',TF_CHECK_STEP('INK_MAP',A.WS_ROUTE)) INK,\n");
      queryBuffer.append("A.FT_GROUP_KEY,\n");
      queryBuffer.append("A.QUALITY_LEVEL,\n");
      queryBuffer.append("A.QUALITY_LEVEL_COMMENT,\n");      
      queryBuffer.append("A.COMPONENT_NO,\n");
      queryBuffer.append("A.COM_PROD_BODY,\n");
      queryBuffer.append("A.COM_MASK_OPTION,\n");
      queryBuffer.append("A.COM_BACKEND_OPTION,\n");      
      queryBuffer.append("A.WS_GROUP_KEY\n");
      queryBuffer.append("FROM IF_TF_BOM_ROUTE_MCP A, TF_ROUTE_MASTER B\n");
      queryBuffer.append(" WHERE TRANS = 'N'\n");
      queryBuffer.append(" AND A.FT_ROUTE = B.ROUTE_NAME(+) \n");
//      queryBuffer.append(" and a.product_body in ('5201','5164','6614') and a.version in (75,21,29,10) and a.version is not null \n");
      queryBuffer.append(" ORDER BY SEQ");
//      queryBuffer.append(" WHERE PRODUCT_TYPE != 'NVM' AND A.FT_ROUTE = B.ROUTE_NAME(+) AND SEQ>= 33281 ORDER BY SEQ");

      ifbomroute = (IF_TF_BOM_ROUTE[]) dataUtil.getData(conn, queryBuffer.toString(), IF_TF_BOM_ROUTE.class, whereConditions).toArray(new IF_TF_BOM_ROUTE[0]);

      if (ifbomroute==null || ifbomroute.length < 1)
        return false;
    } catch (Exception e) {
      TDSLogger.println(e);
    } finally {
    }
    return true;
  }

  // generate a 'blank' string with length = 'width'
  /*private String padding(int width) {
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
  }*/

  private int string_length(String string) {
	  String encoding = System.getProperty("file.encoding");
	  int len = string.getBytes().length - string.length();

	  if (len == 0)
		  len = string.length();
	  else if (encoding.equals("UTF-8"))
		  len = string.length() + len / 2;
	  else 
		  len = string.length() + len;
	  return len;
  }
  
  /*private void output()
  {
    FileOutputStream os=null;
    PrintWriter pr=null;
    try
    {
      os = new FileOutputStream(eifPath+"TIM02-"+(String)DateUtil.Timestamp2String(new java.util.Date(),"yyyyMMddkkmm"));
      //pr = new PrintWriter(os);
      pr = new PrintWriter(new OutputStreamWriter(os, "BIG5"));


      String reasonCode = "";
      StringBuffer output = new StringBuffer();
      int CCode = 0;

      for (int i = 0; i < ifbomroute.length; i++) {
        int comment_length = 0;
        output.delete(0,output.length());
        output.append(ifbomroute[i].SEQ.toString().trim()+padding(10-ifbomroute[i].SEQ.toString().trim().length()));
        output.append(ifbomroute[i].RECTYPE);
        output.append(ifbomroute[i].ACTION);
        output.append(ifbomroute[i].LOG_DATE);
        output.append(ifbomroute[i].PRODUCT_BODY);
        output.append(output_stiring(ifbomroute[i].BRAND,2));
        output.append(output_stiring(ifbomroute[i].BACKEND_OPTION,1));
        output.append(output_stiring(ifbomroute[i].FG_WITH_CODE,1));
        output.append(output_stiring(ifbomroute[i].PIN_COUNT,3));
        output.append(output_stiring(ifbomroute[i].PACKAGE_TYPE,2));
        output.append(output_stiring(ifbomroute[i].FT_ROUTE_CODE,2));
        output.append(ifbomroute[i].FT_ROUTE.trim()+padding(12-ifbomroute[i].FT_ROUTE.trim().length()));
        output.append(ifbomroute[i].FT_ROUTE_ADD.trim()+padding(12-ifbomroute[i].FT_ROUTE_ADD.trim().length()));
        if (ifbomroute[i].TF_COMMENT.trim().equals("")){
          comment_length = 0;
          output.append(ifbomroute[i].TF_COMMENT.trim()+padding(64-comment_length));
        }else{
          //20150126comment_length = ifbomroute[i].TF_COMMENT.trim().length();
          //comment_length = string_length(ifbomroute[i].TF_COMMENT.trim());
// Available for Tru64
//          String comments = StringUtil.unicodeToBig5(ifbomroute[i].TF_COMMENT.trim());
//            String comments = StringUtil.Utf8ToBig5_new(ifbomroute[i].TF_COMMENT.trim());
          String comments = ifbomroute[i].TF_COMMENT.trim();
        String encoding = System.getProperty("file.encoding");
//        String comments1 = new String(comments.getBytes(encoding), "Big5");
//          comment_length = new String(comments.getBytes(encoding), "BIG5").getBytes("BIG5").length;
//          TDSLogger.println("comment_length="+comment_length);
          comment_length = new String(comments).getBytes("BIG5").length;
          TDSLogger.println("comment_length="+comment_length);
          TDSLogger.println("comments="+comments.length());
          
          output.append(comments+padding(64-comment_length));
// Available for PC and AIX
        }  
        //20150126output.append(ifbomroute[i].TF_COMMENT.trim()+padding(64-comment_length));

        //output.append(output_stiring(ifbomroute[i].MASK_OPTION,1));
        output.append(output_stiring(ifbomroute[i].SORT_ROUTE_CODE,2));
        output.append(output_stiring(ifbomroute[i].DB_WITH_CODE,1));
        output.append(output_stiring(ifbomroute[i].WS_ROUTE,12));
        output.append(output_stiring(ifbomroute[i].WS_ROUTE_ADD,12));
        if (ifbomroute[i].TF_WS_COMMENT.trim().equals("")){
          comment_length = 0;
          output.append(ifbomroute[i].TF_WS_COMMENT.trim()+padding(64-comment_length));
        }else{
          //20150126comment_length = ifbomroute[i].TF_WS_COMMENT.trim().length();
          comment_length = string_length(ifbomroute[i].TF_WS_COMMENT.trim());
// Available for Tru64
//          String comments = StringUtil.unicodeToBig5(ifbomroute[i].TF_WS_COMMENT.trim());
//          String comments = StringUtil.Utf8ToBig5_new(ifbomroute[i].TF_WS_COMMENT.trim());
          String comments = ifbomroute[i].TF_WS_COMMENT.trim();
        String encoding = System.getProperty("file.encoding");
//        String comments1 = new String(comments.getBytes(encoding), "Big5");
//          comment_length = new String(comments.getBytes(encoding), "BIG5").getBytes("BIG5").length;
//          TDSLogger.println("comment_length="+comment_length);
          comment_length = new String(comments).getBytes("BIG5").length;
          TDSLogger.println("comment_length="+comment_length);
          TDSLogger.println("comments="+comments.length());
          output.append(comments+padding(64-comment_length));

// Available for PC and AIX
        }  
        //20150126output.append(ifbomroute[i].TF_WS_COMMENT.trim()+padding(64-comment_length));

        if (ifbomroute[i].PRODUCT_TYPE.trim().toUpperCase().equals("NVM"))
          output.append("  ");
        else
          output.append(output_stiring(ifbomroute[i].SALES_FORM,2));
        output.append(output_stiring(ifbomroute[i].WS_STEP,255));  //パ 80 эΘ 255, test mode 絏计耎糤盡
        output.append(output_stiring(ifbomroute[i].WS_ADD_STEP,255));  //パ 80 эΘ 255, test mode 絏计耎糤盡
        output.append(output_stiring(ifbomroute[i].FT_STEP,255));  //パ 80 эΘ 255, test mode 絏计耎糤盡
        output.append(output_stiring(ifbomroute[i].FT_ADD_STEP,255));  //パ 80 эΘ 255, test mode 絏计耎糤盡
        //output.append(output_stiring(ifbomroute[i].BODY_REV,1));
        //output.append(output_stiring(ifbomroute[i].MASK_OPTION_REV,1));
        //output.append(output_stiring(ifbomroute[i].CODE_NO,4));
        //output.append(output_stiring(ifbomroute[i].ROUTE_TYPE,32));
        output.append(output_stiring(ifbomroute[i].WS_ROUTE_ADD1,12));
        output.append(output_stiring(ifbomroute[i].FT_ROUTE_ADD1,12));
        output.append(output_stiring(ifbomroute[i].FT_ROUTE_ADD2,12));
        output.append(output_stiring(ifbomroute[i].WS_ROUTE_ADD1_STEP,255));  //パ 80 эΘ 255, test mode 絏计耎糤盡
        output.append(output_stiring(ifbomroute[i].FT_ROUTE_ADD1_STEP,255));  //パ 80 эΘ 255, test mode 絏计耎糤盡
        output.append(output_stiring(ifbomroute[i].FT_ROUTE_ADD2_STEP,255));  //パ 80 эΘ 255, test mode 絏计耎糤盡
        output.append(output_stiring(ifbomroute[i].PRODUCT_TYPE.trim(),4));
        output.append(output_stiring(ifbomroute[i].ENDURANCE,12));
        output.append(output_stiring(ifbomroute[i].AVI,1));
        output.append(output_stiring(ifbomroute[i].INK,1));
        output.append(output_stiring(ifbomroute[i].FT_GROUP_KEY,40));
        output.append(output_stiring(ifbomroute[i].WS_GROUP_KEY,40));
        output.append(output_stiring(ifbomroute[i].WSSPECIALCONTROL,12));
        output.append(output_stiring(ifbomroute[i].QUALITY_LEVEL,2));
        output.append(output_stiring(ifbomroute[i].QUALITY_LEVEL_COMMENT,128));
        
        output.append(output_stiring(ifbomroute[i].COMPONENT_NO.toString(),1));
        output.append(output_stiring(ifbomroute[i].COM_PROD_BODY,4));
        output.append(output_stiring(ifbomroute[i].COM_MASK_OPTION,1));
        output.append(output_stiring(ifbomroute[i].COM_BACKEND_OPTION,1));

        pr.write(output.toString()+ "\r\n");
        TDSLogger.println("EifPeisPRMII02-SNDMSG--------");
        TDSLogger.println(output.toString());

        TDSLogger.println("--SUCCESS!!!");
        setTrans(ifbomroute[i].SEQ.toString().trim());
        TDSLogger.println("--SUCCESS Update Trans Flag!!!");
      }
      // file ending mark
      if (ifbomroute.length > 0)
    	  pr.write("XXXXXXXXXX"+padding(recordLength - 10)+ "\r\n");
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
  }*/

  	//#217143
  	private void output() {
		FileOutputStream os = null;
		PrintWriter pr = null;
		try {
			os = new FileOutputStream(eifPath
					+ "TIM02-"
					+ (String) DateUtil.Timestamp2String(new java.util.Date(),
							"yyyyMMddkkmm"));
			// pr = new PrintWriter(os);
			pr = new PrintWriter(new OutputStreamWriter(os, "BIG5"));

			String reasonCode = "";
			StringBuffer output = new StringBuffer();
			int CCode = 0;

			for (int i = 0; i < ifbomroute.length; i++) {
				LinkedHashMap<String, String> info = new LinkedHashMap<String, String>();
				int comment_length = 0;
				output.delete(0, output.length());
				info.put("SEQ", ifbomroute[i].SEQ.toString().trim());
				info.put("RECTYPE",ifbomroute[i].RECTYPE);
				info.put("ACTION", ifbomroute[i].ACTION);
				info.put("LOG_DATE", ifbomroute[i].LOG_DATE);
				info.put("PRODUCT_BODY", ifbomroute[i].PRODUCT_BODY);
				info.put("BRAND", ifbomroute[i].BRAND);
				info.put("BACKEND_OPTION", ifbomroute[i].BACKEND_OPTION);
				info.put("FG_WITH_CODE", ifbomroute[i].FG_WITH_CODE);
				info.put("PIN_COUNT", ifbomroute[i].PIN_COUNT);
				info.put("PACKAGE_TYPE", ifbomroute[i].PACKAGE_TYPE);
				info.put("FT_ROUTE_CODE", ifbomroute[i].FT_ROUTE_CODE);
				info.put("FT_ROUTE", ifbomroute[i].FT_ROUTE.trim());
				info.put("FT_ROUTE_ADD", ifbomroute[i].FT_ROUTE_ADD.trim());
				if (ifbomroute[i].TF_COMMENT.trim().equals("")) {
					comment_length = 0;
					info.put("TF_COMMENT", ifbomroute[i].TF_COMMENT.trim());
				} else {
					String comments = ifbomroute[i].TF_COMMENT.trim();
					String encoding = System.getProperty("file.encoding");
					comment_length = new String(comments).getBytes("BIG5").length;
					TDSLogger.println("comment_length=" + comment_length);
					TDSLogger.println("comments=" + comments.length());

					info.put("TF_COMMENT", comments);
				}
				info.put("SORT_ROUTE_CODE", ifbomroute[i].SORT_ROUTE_CODE);
				info.put("DB_WITH_CODE", ifbomroute[i].DB_WITH_CODE);
				info.put("WS_ROUTE", ifbomroute[i].WS_ROUTE);
				info.put("WS_ROUTE_ADD", ifbomroute[i].WS_ROUTE_ADD);
				if (ifbomroute[i].TF_WS_COMMENT.trim().equals("")) {
					comment_length = 0;
					info.put("TF_WS_COMMENT", ifbomroute[i].TF_WS_COMMENT.trim());
				} else {
					comment_length = string_length(ifbomroute[i].TF_WS_COMMENT
							.trim());
					String comments = ifbomroute[i].TF_WS_COMMENT.trim();
					String encoding = System.getProperty("file.encoding");
					comment_length = new String(comments).getBytes("BIG5").length;
					TDSLogger.println("comment_length=" + comment_length);
					TDSLogger.println("comments=" + comments.length());
					info.put("TF_WS_COMMENT", comments);
				}
				if (ifbomroute[i].PRODUCT_TYPE.trim().toUpperCase()
						.equals("NVM"))
					info.put("SALES_FORM","");
				else
					info.put("SALES_FORM", ifbomroute[i].SALES_FORM);
				info.put("WS_STEP", ifbomroute[i].WS_STEP);
				info.put("WS_ADD_STEP", ifbomroute[i].WS_ADD_STEP);
				info.put("FT_STEP", ifbomroute[i].FT_STEP);
				info.put("FT_ADD_STEP", ifbomroute[i].FT_ADD_STEP);
				info.put("WS_ROUTE_ADD1", ifbomroute[i].WS_ROUTE_ADD1);
				info.put("FT_ROUTE_ADD1", ifbomroute[i].FT_ROUTE_ADD1);
				info.put("FT_ROUTE_ADD2", ifbomroute[i].FT_ROUTE_ADD2);
				info.put("WS_ROUTE_ADD1_STEP", ifbomroute[i].WS_ROUTE_ADD1_STEP);
				info.put("FT_ROUTE_ADD1_STEP", ifbomroute[i].FT_ROUTE_ADD1_STEP);
				info.put("FT_ROUTE_ADD2_STEP", ifbomroute[i].FT_ROUTE_ADD2_STEP);
				info.put("PRODUCT_TYPE", ifbomroute[i].PRODUCT_TYPE.trim());
				info.put("ENDURANCE", ifbomroute[i].ENDURANCE);
				info.put("AVI", ifbomroute[i].AVI);
				info.put("INK", ifbomroute[i].INK);
				info.put("FT_GROUP_KEY", ifbomroute[i].FT_GROUP_KEY);
				info.put("WS_GROUP_KEY", ifbomroute[i].WS_GROUP_KEY);
				info.put("WSSPECIALCONTROL", ifbomroute[i].WSSPECIALCONTROL);
				info.put("QUALITY_LEVEL", ifbomroute[i].QUALITY_LEVEL);
				info.put("QUALITY_LEVEL_COMMENT", ifbomroute[i].QUALITY_LEVEL_COMMENT);
				info.put("COMPONENT_NO", ifbomroute[i].COMPONENT_NO.toString());
				info.put("COM_PROD_BODY", ifbomroute[i].COM_PROD_BODY);
				info.put("COM_MASK_OPTION", ifbomroute[i].COM_MASK_OPTION);
				info.put("COM_BACKEND_OPTION", ifbomroute[i].COM_BACKEND_OPTION);

				output = SapEncoding.formatOutput(info, eifName);
				pr.write(output.toString());
				TDSLogger.println("EifPeisPRMII02-SNDMSG--------");
				TDSLogger.println(output.toString());

				TDSLogger.println("--SUCCESS!!!");
				setTrans(ifbomroute[i].SEQ.toString().trim());
				TDSLogger.println("--SUCCESS Update Trans Flag!!!");
			}
			// file ending mark
			if (ifbomroute.length > 0)
				pr.write("XXXXXXXXXX");
		} catch (Exception e) {
			TDSLogger.println(e);
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
			}
		}
	}
  
  // Set Trans = 'Y' to record that this data identified by seq was transferred
  private void setTrans(String seq)
  {
    PreparedStatement stmt = null;
    String sqlstr = "update if_tf_bom_route_mcp set trans = 'Y' where seq = ?";
    try {
      stmt = conn.prepareStatement(sqlstr);
      stmt.setInt(1,Integer.parseInt(seq));
      int succ = stmt.executeUpdate();
    } catch(Exception e){
      TDSLogger.println(e);
    } finally {
      try {
        stmt.close();
        stmt = null;
      } catch (Exception e) {
        TDSLogger.println(e);
      }
    }
  }

  // Main procedure
  public static void main(String args[])
  {
    EifPeisPRMII02 prm = new EifPeisPRMII02();

    try	{
      prm.processPEISPRMII02();
    } catch(Exception e) {
      TDSLogger.println(e);
    }
  }
}