package com.mxic.oiplus.eif;
//每天傳全部資料過來,所以table必須全砍全新增



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.mxic.oiplus.util.GPRSDB;
import com.mxic.tdsplus.resource.DBConnection;
import com.mxic.tdsplus.ba.BADAO;
import com.mxic.tdsplus.util.DataHandlerUtil;
import com.mxic.tdsplus.util.DateUtil;
import com.mxic.tdsplus.util.SendMail;
import com.mxic.tdsplus.util.TDSLogger;


public class EifSAPTIM02 {
	private static final int max_commit = 64;
	private DataHandlerUtil util = new DataHandlerUtil();;
    //SAP will send data into /EIFDATA/TOTDS/EIFSAPTIM02/";
	private static final String eifName = "EIFSAPTIM02";
	private LogWriter log;

	public boolean insert_TABLE(Connection conn, HashMap colValue) {

		String sql;
		Object[] obj;
		boolean result = false;
		sql =

			"INSERT INTO TIM.TG_CUSTOMER(GROUP_NO, GROUP_NAME, ENGLISH_FULL_NAME, LOCAL_FULL_NAME, SAP_CUST_CLASS_NO, RECEIVE_TIME) \n" +
			" VALUES (?,?,?,?,?,SYSDATE)";

		obj = new Object[] {colValue.get("GROUP_NO"),
				            colValue.get("GROUP_NAME"),
				            colValue.get("ENGLISH_FULL_NAME"),
				            colValue.get("LOCAL_FULL_NAME"),
				            colValue.get("SAP_CUST_CLASS_NO")};

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

        "UPDATE TIM.TG_CUSTOMER SET  GROUP_NAME = ?, ENGLISH_FULL_NAME = ?, LOCAL_FULL_NAME = ?, SAP_CUST_CLASS_NO = ?, RECEIVE_TIME = SYSDATE \n"
                + " WHERE GROUP_NO = ?  ";

        obj = new Object[] {colValue.get("GROUP_NAME") ,
                            colValue.get("ENGLISH_FULL_NAME") ,
                            colValue.get("LOCAL_FULL_NAME") ,
                            colValue.get("SAP_CUST_CLASS_NO") ,
                            colValue.get("GROUP_NO")};

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
        sql ="DELETE FROM TIM.TG_CUSTOMER  WHERE GROUP_NO = ? ";
        obj = new Object[] { colValue.get("GROUP_NO").toString() };

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
        String sql = "SELECT COUNT(1) AS CNT FROM TIM.TG_CUSTOMER WHERE GROUP_NO = ?  ";
        try {
            HashMap[] rs = util.getDataBySql(conn, sql, new Object[] { values.get("GROUP_NO") });
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
	

	
	private void readAndPut(Connection conn, String line)
	{
		boolean	result = false;
		HashMap colValue = new HashMap();
		String[] tmpValue = line.split("\t", -1);
		if(tmpValue.length==5 && tmpValue[0]!=null && !tmpValue[0].equals("") && tmpValue[1]!=null && !tmpValue[1].equals("")){
			colValue.put("GROUP_NO", tmpValue[0]);//GROUP_NO
			colValue.put("GROUP_NAME", tmpValue[1]);//GROUP_NAME
			colValue.put("ENGLISH_FULL_NAME", tmpValue[2]);//ENGLISH_FULL_NAME
			colValue.put("LOCAL_FULL_NAME", tmpValue[3]);//LOCAL_FULL_NAME
			colValue.put("SAP_CUST_CLASS_NO", tmpValue[4]);//SAP_CUST_CLASS_NO
			
			if(is_TABLE_Exists(conn, colValue))	{		
				result = update_TABLE(conn, colValue);
				log.WriterToLog("Update Data : "+ line + "\n");
			}else{
				result = insert_TABLE(conn, colValue);
				log.WriterToLog("Insert Data : "+ line + "\n");
			}	   
			if (!result)
	    		TDSLogger.println("Insert/Update Error:"+colValue.get("GROUP_NO")+","+
			            colValue.get("GROUP_NAME")+","+
			            colValue.get("ENGLISH_FULL_NAME")+","+
			            colValue.get("LOCAL_FULL_NAME") +","+
			            colValue.get("SAP_CUST_CLASS_NO"));
			    log.WriterToLog("Error:"+ line + "\n");
		}else{
			TDSLogger.println("Data Format Error : "+ line + ")\n");
		}
	}
	

    public EifSAPTIM02(String arg[]) {
        boolean result = true;
        String line = "";

        TDSLogger.println("======================================================");
        TDSLogger.println("EifSAPTIM02 run at " + DateUtil.getNow());
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
        Connection conn = null;

        String filepath = arg[0];
         //String filepath
         //="D:\\pg-20150417091031.txt";
        try {
            line = "";
            File file = new File(filepath);
            is = new FileInputStream(file);
            //BufferedReader bf = new BufferedReader(new InputStreamReader( new FileInputStream(file), "Big5"));
            //20150525isr = new InputStreamReader(is,"UTF-8");
            isr = new InputStreamReader(is,"Big5");
            br = new BufferedReader(isr);
            
            /*判斷檔案是什麼格式
            String charset = "Big5";  
            byte[] first3Bytes = new byte[3];  
            BufferedInputStream bis = null;  
            bis = new BufferedInputStream(new FileInputStream(file));  
            bis.mark(0);  
            int read = bis.read(first3Bytes, 0, 3);  
            if (read == -1) {  
                charset ="fail";  
            }  
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {  
                charset = "UTF-16LE";  
                // checked = true;  
            } else if (first3Bytes[0] == (byte) 0xFE  
                    && first3Bytes[1] == (byte) 0xFF) {  
                charset = "UTF-16BE";  
                // checked = true;  
            } else if (first3Bytes[0] == (byte) 0xEF  
                    && first3Bytes[1] == (byte) 0xBB  
                    && first3Bytes[2] == (byte) 0xBF) {  
                charset = "UTF-8";  
                // checked = true;  
            }  
            */
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
           
            line = br.readLine();
              
            while (line != null) {
                readAndPut(conn, line);
                line = br.readLine();
                conn.commit();

            }
        } catch (Exception ex) {
            TDSLogger.println(ex);
            DBConnection.rollback(conn);
            SendMail.send("sophialai", "PEISAdmin", "SAP to TIM (Customer Data) interface error(EifSapTim02)", ex.toString()+"\n"+line);
        } finally {
            try {
                log.Println("----------E N D----------" + DateUtil.getNow());
                log.Println("");
                log.close();
                br.close();
                isr.close();
                is.close();
                br = null;
                isr = null;
                is = null;
                conn.commit();
                DBConnection.close(conn);
            } catch (Exception e) {
                TDSLogger.println(e);
                SendMail.send("sophialai", "PEISAdmin", "SAP to TIM (Customer Data) interface error(EifSapTim02)", e.toString()+"\n"+line);
            }
        }
    }
	
    public static void main(String arg[]) {
        EifSAPTIM02 eif = new EifSAPTIM02(arg);
        eif = null;
    }
}