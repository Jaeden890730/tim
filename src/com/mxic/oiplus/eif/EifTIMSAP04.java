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
import com.mxic.oiplus.oimaintain.*;
import com.mxic.oiplus.resource.*;
import com.mxic.tdsplus.util.SapEncoding;

public class EifTIMSAP04 extends SQLStatement
{
  private Connection connection = null;
  TF_PRODUCT_ROUTE[] tpr = null;

  private static String eifName = "EifTIMSAP04";
  private static String eifPath = "/EIFDATA/FROMTDS/EIFTIMSAP04/";
  //private static String eifPath = "D:\\EIFDATA\\FROMTDS\\EIFTIMSAP04\\";

  public EifTIMSAP04(){
  }

    public void processEifTIMSAP04(int s) {
        try {
      connection = DBConnection.getConnection();
            if (EIFService.updateInterfaceTime(eifName, "CURRENT_TIME") == 1) {
                boolean flag = selectPRODUCTROUTEData(s);
      if (flag){
        output();
      } else
        TDSLogger.println( "Nothing to do !!" );
                EIFService.updateInterfaceTime(eifName, "LAST_TIME");
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
  private boolean selectPRODUCTROUTEData(int s)
  {
    String interval = (String)TDSResource.getProperties("EIF").get("to.sap1.cron.interval");
    DataHandlerUtil dataUtil = new DataHandlerUtil();
    try
    {
      String sql =
    	  //NVM-MROM-XROM WS / FT (main route / substitution route)
          "select  distinct "+
          "a.sid, decode(substr(route_name,2,1),'W','WS','P','FT','Q','FT','FT') as process_type, a.product_body,a.brand,a.route_name,a.step_seq, a.step_name,a.test_time,a.time_unit,a.temperature,nvl(a.sampling_test,'N')sampling_test,a.step_def "+
          "from TF_PRODUCT_ROUTE a, tf_current_version_vw b, tf_information c " ;
          if(s == 0)
               sql+=   ", if_interface_time d ";
          sql += "where a.product_body = b.product_body "+
          "and a.brand = b.brand "+
          "and a.version = b.version "+
          "and a.sid = b.sid "+
          "and a.product_body = c.product_body "+
          "and a.brand = c.brand "+
          "and a.version = c.version "+
          "and a.sid = c.sid ";
          if(s == 0) {
              sql += "and d.interface = '"+eifName+"' \n" ;
              sql += "and c.log_time > d.last_time and c.log_time <= d.current_time \n" ;
          }
          sql += "order by product_body, brand, route_name, step_seq ";

      tpr = (TF_PRODUCT_ROUTE[]) dataUtil.getData(connection,sql,TF_PRODUCT_ROUTE.class,null).toArray(new TF_PRODUCT_ROUTE[0]);
      StringBuffer sid = new StringBuffer();
      
      if(tpr!= null && tpr.length > 0 ) {
          for (int i =0 ; i < tpr.length; i++) {
              if(sid.toString().indexOf(tpr[i].SID.intValue()+"")== -1) {
                  sid.append(tpr[i].SID);
                  sid.append(",");
              }
          }
         
          sql = "SELECT COUNT(A.YID) COUNT ,A.SID\n" +
                          "  FROM TF_YIELD_DEFINITION A\n" + 
                          " WHERE A.SID in ('" + sid.toString().replaceAll(",", "','")+"')\n" + 
                          "   AND A.FACILITY = 0\n" + 
                          "   AND A.ACTION LIKE '%Dgrade%'\n" + 
                          "   AND TEST_MODE = 'Ship' GROUP BY A.SID ";

          
          /*tpr1 = (IF_TF_YIELD_DEFINITION[]) dataUtil.getData(connection, sql, IF_TF_YIELD_DEFINITION.class, null).toArray(new IF_TF_YIELD_DEFINITION[0]);
          
          if(tpr1 != null && tpr1.length > 0 ) {
              ChangeArrayList = new HashMap();
              for(int i = 0 ; i < tpr1.length; i++) {
                  ChangeArrayList.put(tpr1[i].SID, tpr1[i].COUNT);
              }
          }*/
      }
      

      if (tpr.length == 0)
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
  }*/

  /*private void output()
  {
    FileOutputStream os = null;
    PrintWriter pr = null;
    try
    {
      os = new FileOutputStream(eifPath+"TIMSAP04-"+(String)DateUtil.Timestamp2String(new java.util.Date(),"yyyyMMddkkmm"));
      pr = new PrintWriter(os);

      StringBuffer output = new StringBuffer();

      for ( int i = 0; i < tpr.length; i++ ) {
          if(!tpr[i].PROCESS_TYPE.equals("WS"))
              continue;
         
          if(tpr[i].STEP_NAME.equals("AVI")) {  /*AVI 抓取前道電測的STEP_DEF*//*
              int j = i; 
              while (!tpr[j].STEP_NAME.startsWith("SORT")) {
                 j--;
              }
              tpr[i].STEP_DEF = tpr[j].STEP_DEF;
          }
      }

      for ( int i = 0; i < tpr.length; i++ ) {
        output.delete(0,output.length());
        output.append(tpr[i].PROCESS_TYPE);
        output.append(tpr[i].PRODUCT_BODY);
        output.append(tpr[i].BRAND);
        output.append(output_stiring(tpr[i].ROUTE_NAME,12));
        output.append(output_stiring(tpr[i].STEP_SEQ.toString(),3));
        output.append(output_stiring(tpr[i].STEP_NAME,32));
        if(StringUtil.formatNull(tpr[i].TEST_TIME).equals(""))
           output.append(padding(4));
        else        		
           output.append(output_stiring(tpr[i].TEST_TIME.toString(),4));
        output.append(output_stiring(tpr[i].TIME_UNIT,6));
        output.append(output_stiring(tpr[i].TEMPERATURE,12));
        output.append(output_stiring(tpr[i].SAMPLING_TEST,1));
        output.append(output_stiring(tpr[i].STEP_DEF,20));
        
        pr.println(output.toString());
        */
        
        /*倘若WS Downgrade Criteria有定義Ship資訊，且該產品有定義AVI step，則需要再傳送STEP資訊過去，資訊內容目前仿造AVI內容，須和SAP再討論
        if(tpr[i].STEP_NAME.equals("AVI") && ChangeArrayList!= null && ChangeArrayList.containsKey(tpr[i].SID) && Integer.parseInt(ChangeArrayList.get(tpr[i].SID)+"") > 0) {
            output.delete(0,output.length());
            output.append(tpr[i].PROCESS_TYPE);
            output.append(tpr[i].PRODUCT_BODY);
            output.append(tpr[i].BRAND);
            output.append(output_stiring(tpr[i].ROUTE_NAME,12));
            output.append(output_stiring(tpr[i].STEP_SEQ.toString(),3));
            output.append(output_stiring("Ship",32));
            if(StringUtil.formatNull(tpr[i].TEST_TIME).equals(""))
               output.append(padding(4));
            else                
               output.append(output_stiring(tpr[i].TEST_TIME.toString(),4));
            output.append(output_stiring(tpr[i].TIME_UNIT,6));
            output.append(output_stiring(tpr[i].TEMPERATURE,12));
            output.append(output_stiring(tpr[i].SAMPLING_TEST,1));
            output.append(output_stiring(tpr[i].STEP_DEF,20));
        
        pr.println(output.toString());
        }
        */
        
        
        
        
        /*TDSLogger.println("EifTIMSAP04-SNDMSG--------");
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
  }*/
  
  // #217145
  	private void output() {
		FileOutputStream os = null;
		PrintWriter pr = null;
		try {
			os = new FileOutputStream(eifPath
					+ "TIMSAP04-"
					+ (String) DateUtil.Timestamp2String(new java.util.Date(),
							"yyyyMMddkkmm"));
			pr = new PrintWriter(os);

			StringBuffer output = new StringBuffer();

			for (int i = 0; i < tpr.length; i++) {
				if (!tpr[i].PROCESS_TYPE.equals("WS"))
					continue;

				if (tpr[i].STEP_NAME.equals("AVI")) { /* AVI 抓取前道電測的STEP_DEF */
					int j = i;
					while (!tpr[j].STEP_NAME.startsWith("SORT")) {
						j--;
					}
					tpr[i].STEP_DEF = tpr[j].STEP_DEF;
				}
			}

			for (int i = 0; i < tpr.length; i++) {
				LinkedHashMap<String, String> info = new LinkedHashMap<String, String>();
				output.delete(0, output.length());
				info.put("PROCESS_TYPE", tpr[i].PROCESS_TYPE);
				info.put("PRODUCT_BODY", tpr[i].PRODUCT_BODY);
				info.put("BRAND", tpr[i].BRAND);
				info.put("ROUTE_NAME", tpr[i].ROUTE_NAME);
				info.put("STEP_SEQ", tpr[i].STEP_SEQ.toString());
				info.put("STEP_NAME", tpr[i].STEP_NAME);
				if (StringUtil.formatNull(tpr[i].TEST_TIME).equals(""))
					info.put("TEST_TIME", "");
				else
					info.put("TEST_TIME", tpr[i].TEST_TIME.toString());
				info.put("TIME_UNIT", tpr[i].TIME_UNIT);
				info.put("TEMPERATURE", tpr[i].TEMPERATURE);
				info.put("SAMPLING_TEST", tpr[i].SAMPLING_TEST);
				info.put("STEP_DEF", tpr[i].STEP_DEF);

				output = SapEncoding.formatOutput(info, eifName);
				pr.print(output.toString());

				TDSLogger.println("EifTIMSAP04-SNDMSG--------");
				TDSLogger.println(output.toString());

				TDSLogger.println("--SUCCESS!!!");
				TDSLogger.println("--SUCCESS Update Trans Flag!!!");
			}
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
  
  /*private String output_stiring(String value, int length) {
	    if (value == null)
	      return padding(length);
	    else
	      return value.trim()+padding(length-value.trim().length());
	  }*/

  // Main procedure
  public static void main(String args[])
  {
	EifTIMSAP04 sap = new EifTIMSAP04();

    try	{
         sap.processEifTIMSAP04(args.length);

      
    } catch(Exception e) {
      TDSLogger.println(e);
    }
  }
}
