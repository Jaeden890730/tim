package com.mxic.oiplus.eif;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.mxic.oiplus.util.GPRSDB;
import com.mxic.oiplus.util.StringUtil;
import com.mxic.tdsplus.resource.DBConnection;
import com.mxic.tdsplus.ba.BADAO;
import com.mxic.tdsplus.util.DataHandlerUtil;
import com.mxic.tdsplus.util.DateUtil;
import com.mxic.tdsplus.util.SapEncoding;
import com.mxic.tdsplus.util.SendMail;
import com.mxic.tdsplus.util.TDSLogger;

public class EifSAPTIM01 {
	private static final int max_commit = 64;
	private DataHandlerUtil util = new DataHandlerUtil();;
    //SAP will send data into /EIFDATA/TOTDS/EIFSAPTIM01/";
	private static final String eifName = "EIFSAPTIM01";
	private LogWriter log;

	public boolean insert_TABLE(Connection conn, HashMap colValue) {

		String sql;
		Object[] obj;
		boolean result = false;
		sql =

			"INSERT INTO TIM.TF_QUALITY_LEVEL(PRODUCT_BODY, QUALITY_LEVEL, QUALITY_LEVEL_COMMENT, TURNON, RECEIVE_TIME) \n" +
			" VALUES (?,?,?,?,SYSDATE)";

		obj = new Object[] {colValue.get("PRODUCT_BODY"),
				            colValue.get("QUALITY_LEVEL"),
				            colValue.get("QUALITY_LEVEL_COMMENT"),
				            colValue.get("TURNON")};

		try {
		    GPRSDB.execDML(conn, sql.toString(), obj);
			result = true;
		
		} catch (Exception e) {
			result = false;
			TDSLogger.println(e);
		} 
		return result;
	}
	
    public boolean update_TABLE(Connection conn, HashMap colValue) {

        String sql;
        Object[] obj;
        boolean result = false;
        sql =

        "UPDATE TIM.TF_QUALITY_LEVEL SET  QUALITY_LEVEL = ?, QUALITY_LEVEL_COMMENT = ?, TURNON = ?, RECEIVE_TIME = SYSDATE \n"
                + " WHERE PRODUCT_BODY = ?  ";

        obj = new Object[] {colValue.get("QUALITY_LEVEL") ,
                            colValue.get("QUALITY_LEVEL_COMMENT") ,
                            colValue.get("TURNON") ,
                            colValue.get("PRODUCT_BODY")};

        try {
            GPRSDB.execDML(conn, sql.toString(), obj);
            result = true;

        } catch (Exception e) {
            result = false;
            TDSLogger.println(e);
        }
        return result;
    }
	
    public boolean delete_TABLE(Connection conn, HashMap colValue) {

        String sql;
        Object[] obj;
        boolean result = false;
        sql ="DELETE FROM TIM.TF_QUALITY_LEVEL  WHERE PRODUCT_BODY = ? ";
        obj = new Object[] { colValue.get("PRODUCT_BODY").toString() };

        try {
            GPRSDB.execDML(conn, sql.toString(), obj);
            result = true;

        } catch (Exception e) {
            result = false;
            TDSLogger.println(e);
        }
        return result;
    }
	
    public boolean is_TABLE_Exists(Connection conn, HashMap values) {
        boolean result = false;
        String sql = "SELECT COUNT(1) AS CNT FROM TIM.TF_QUALITY_LEVEL WHERE PRODUCT_BODY = ?  ";
        try {
            HashMap[] rs = util.getDataBySql(conn, sql, new Object[] { values.get("PRODUCT_BODY") });
            if (rs != null && rs.length > 0) {
                if (rs[0].get("CNT").equals("0")) {
                    result = false;
                } else {
                    result = true;
                }
            } else {
                result = false;
            }
        } catch (Exception e) {
            TDSLogger.println(e);
            result = false;
        }
        return result;
    }
	

	
	private boolean readAndPut(Connection conn, String line)
	{
		boolean	result;
		HashMap colValue = new HashMap();
		//Modify by HouYu for #218963
		/*colValue.put("STATUS", line.substring(0, 1).trim());//1(STATUS),C:Create, U:Update, D:Delete
		colValue.put("PRODUCT_BODY", line.substring(1, 5).trim());//4(PRODBODY),6705
		colValue.put("QUALITY_LEVEL", line.substring(5, 7).trim());//5(QLEVAL),Q2
		colValue.put("QUALITY_LEVEL_COMMENT", line.substring(7,67).trim());//60(DESCRIPTION),1000 tppm < Q2 <= 2000 tppm
		colValue.put("TURNON", line.substring(67, 68).trim());//1(TURNON),Y*/
		String[] tempStrings = line.split("\\t");
    	int minLength = 5; //colValues
    	if (tempStrings.length < minLength)
    		tempStrings = StringUtil.copySplitArray(tempStrings, minLength);
		colValue.put("STATUS", tempStrings[0].trim());//1(STATUS),C:Create, U:Update, D:Delete
		colValue.put("PRODUCT_BODY", tempStrings[1].trim());//4(PRODBODY),6705
		colValue.put("QUALITY_LEVEL", tempStrings[2].trim());//5(QLEVAL),Q2
		colValue.put("QUALITY_LEVEL_COMMENT", tempStrings[3].trim());//60(DESCRIPTION),1000 tppm < Q2 <= 2000 tppm
		colValue.put("TURNON", tempStrings[4].trim());//1(TURNON),Y
		
		if(colValue.get("STATUS").toString().equals("C"))
			result = insert_TABLE(conn, colValue);
		else if(colValue.get("STATUS").toString().equals("U")){
			if(is_TABLE_Exists(conn, colValue))			
			   result = update_TABLE(conn, colValue);
			else{
			   TDSLogger.println("No Data To Update : "+ line + "\n");
			   log.WriterToLog("No Data To Update : "+ line + "\n");
			   result = false;
			}   
		}else if(colValue.get("STATUS").toString().equals("D")){
			if(is_TABLE_Exists(conn, colValue))			
			   result = delete_TABLE(conn, colValue);
			else{
			   TDSLogger.println("No Data To Delete : "+ line + "\n");	
			   log.WriterToLog("No Data To Delete : "+ line + "\n");	
			   result = false;
			}   
		}else{
			TDSLogger.println("Format Error : "+ line + "\n");
			log.WriterToLog("Format Error : "+ line + "\n");
			return false;
					
		}		
			
				
		if (!result)
    		TDSLogger.println("Error:"+colValue.get("PRODUCT_BODY")+","+
		            colValue.get("QUALITY_LEVEL")+","+
		            colValue.get("QUALITY_LEVEL_COMMENT")+","+
		            colValue.get("TURNON") );
		    log.WriterToLog("Error:"+ line + "\n");
		return result;
	}
	

    public EifSAPTIM01(String arg[]) {
        boolean result = true;

        TDSLogger.println("======================================================");
        TDSLogger.println("EifSAPTIM01 run at " + DateUtil.getNow());
        log = new LogWriter(eifName);
        log.Println("----------START----------" + DateUtil.getNow());

        int argv = arg.length;
        if (argv != 1) {
            TDSLogger.println("No arguments !!");
            log.Println("No arguments !!");
            return;
        }

        FileInputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        BufferedReader formattedBr = null;
        Connection conn = null;

        String filepath = arg[0];
        //String filepath ="D:\\EIFDATA\\TOTDS\\EIFSAPTIM01\\202507311312325577.qlev";
        try {
            String line = "";
            File file = new File(filepath);
            is = new FileInputStream(file);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            //Added by HouYu for #218963
			formattedBr = SapEncoding.formatInput(br, eifName);
			
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            //Modify by HouYu for #218963
            //line = br.readLine();
        	line = formattedBr.readLine();

            while (line != null) {
                result = readAndPut(conn, line) && result;
              //Modify by HouYu for #218963
                //line = br.readLine();
        		line = formattedBr.readLine();
                conn.commit();

            }
        } catch (Exception ex) {
            TDSLogger.println(ex);
            DBConnection.rollback(conn);
            SendMail.send("fab_peis", "PEISAdmin", "SAP to TIM (Quality Level) interface error", ex.toString());
        } finally {
            try {
                log.Println("----------E N D----------" + DateUtil.getNow());
                log.Println("");
                log.close();
                br.close();
                isr.close();
                is.close();
        		formattedBr.close();
                br = null;
                isr = null;
                is = null;
        		formattedBr=null;
                conn.commit();
                DBConnection.close(conn);
            } catch (Exception e) {
                TDSLogger.println(e);
                SendMail.send("fab_peis", "PEISAdmin", "SAP to TIM (Quality Level) interface error", e.toString());
            }
        }
    }
	
    public static void main(String arg[]) {
        EifSAPTIM01 eif = new EifSAPTIM01(arg);
        eif = null;
    }
}