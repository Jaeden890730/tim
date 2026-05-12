package com.mxic.oiplus.eif;
//FLAG : C : 新增 / D : 刪除 / U : 修改



import java.io.BufferedInputStream;
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

public class EifSAPTIM03 {
	private static final int max_commit = 64;
	private DataHandlerUtil util = new DataHandlerUtil();;
    //SAP will send data into /EIFDATA/TOTDS/EIFSAPTIM03/";
	private static final String eifName = "EIFSAPTIM03";
	private LogWriter log;

	public boolean insert_TABLE(Connection conn, HashMap colValue) {

		String sql;
		Object[] obj;
		boolean result = false;
		sql =

			"INSERT INTO TIM.TF_COMPONENT_PRODUCT(PROD_BODY, MASK_OPTION, BACKEND_OPTION, EPN, COM_PROD_BODY, COM_MASK_OPTION, COMPONENT_NO, COMPONENT_QTY, COM_BACKEND_OPTION, RECEIVE_DATE) \n" +
			" VALUES (?,?,?,?,?,?,?,?,?,SYSDATE)";

		obj = new Object[] {colValue.get("PROD_BODY"),
				            colValue.get("MASK_OPTION"),
				            colValue.get("BACKEND_OPTION"),
				            colValue.get("EPN"),
				            colValue.get("COM_PROD_BODY"),
				            colValue.get("COM_MASK_OPTION"),
				            colValue.get("COMPONENT_NO"),
				            colValue.get("COMPONENT_QTY"),
				            colValue.get("COM_BACKEND_OPTION")
				            };

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

        "UPDATE TIM.TF_COMPONENT_PRODUCT SET  COM_BACKEND_OPTION = ?, UPDATE_DATE = SYSDATE \n"
        		+ "WHERE PROD_BODY = ? \n"
            	+ "AND MASK_OPTION = ? \n"
            	+ "AND BACKEND_OPTION = ? \n"
            	+ "AND EPN = ? \n"
            	+ "AND COM_PROD_BODY = ? \n"
            	+ "AND COM_MASK_OPTION = ? \n"
            	+ "AND COMPONENT_NO = ? \n"
            	+ "AND COMPONENT_QTY = ? \n";

        obj = new Object[] {colValue.get("COM_BACKEND_OPTION") ,
        		            colValue.get("PROD_BODY") ,
                            colValue.get("MASK_OPTION") ,
                            colValue.get("BACKEND_OPTION") ,
                            colValue.get("EPN") ,
                            colValue.get("COM_PROD_BODY") ,
                            colValue.get("COM_MASK_OPTION") ,
                            colValue.get("COMPONENT_NO") ,
                            colValue.get("COMPONENT_QTY")};

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
        sql ="DELETE FROM TIM.TF_COMPONENT_PRODUCT  \n"
        	+ "WHERE PROD_BODY = ? \n"
        	+ "AND MASK_OPTION = ? \n"
        	+ "AND BACKEND_OPTION = ? \n"
        	+ "AND EPN = ? \n"
        	+ "AND COM_PROD_BODY = ? \n"
        	+ "AND COM_MASK_OPTION = ? \n"
        	+ "AND COMPONENT_NO = ? \n"
        	+ "AND COMPONENT_QTY = ? \n";
        obj = new Object[] { colValue.get("PROD_BODY").toString(),
        		colValue.get("MASK_OPTION").toString(),
        		colValue.get("BACKEND_OPTION").toString(),
        		colValue.get("EPN").toString(),
        		colValue.get("COM_PROD_BODY").toString(),
        		colValue.get("COM_MASK_OPTION").toString(),
        		colValue.get("COMPONENT_NO").toString(),
        		colValue.get("COMPONENT_QTY").toString()
        		};

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
        String sql = "SELECT COUNT(1) AS CNT FROM TIM.TF_COMPONENT_PRODUCT \n"
        		+ "WHERE PROD_BODY = ? \n"
            	+ "AND MASK_OPTION = ? \n"
            	+ "AND BACKEND_OPTION = ? \n"
            	+ "AND EPN = ? \n"
            	+ "AND COM_PROD_BODY = ? \n"
            	+ "AND COM_MASK_OPTION = ? \n"
            	+ "AND COMPONENT_NO = ? \n"
            	+ "AND COMPONENT_QTY = ? \n";
        try {
            HashMap[] rs = util.getDataBySql(conn, sql, new Object[] { values.get("PROD_BODY").toString(),
            		values.get("MASK_OPTION").toString(),
            		values.get("BACKEND_OPTION").toString(),
            		values.get("EPN").toString(),
            		values.get("COM_PROD_BODY").toString(),
            		values.get("COM_MASK_OPTION").toString(),
            		values.get("COMPONENT_NO").toString(),
            		values.get("COMPONENT_QTY").toString() });
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
		int flag = getEachColValue(line,colValue);
		if(flag > 0){
			//if(is_TABLE_Exists(conn, colValue))	{
			if(colValue.get("FLAG").equals("U"))	{
				result = update_TABLE(conn, colValue);
				log.WriterToLog("Update Data : "+ line + "\n");
			}else if(colValue.get("FLAG").equals("C"))	{
				result = insert_TABLE(conn, colValue);
				log.WriterToLog("Insert Data : "+ line + "\n");
			}else if(colValue.get("FLAG").equals("D"))	{	
				result = delete_TABLE(conn, colValue);
				log.WriterToLog("Delete Data : "+ line + "\n");
			}
			if (!result)
	    		TDSLogger.println("Insert/Update/Delete Error:"+colValue.get("FLAG")+","+
			            colValue.get("PROD_BODY")+","+
			            colValue.get("MASK_OPTION")+","+
			            colValue.get("BACKEND_OPTION")+","+
			            colValue.get("EPN") +","+
			            colValue.get("COM_PROD_BODY") +","+
			            colValue.get("COM_MASK_OPTION") +","+
			            colValue.get("COMPONENT_NO") +","+
			            colValue.get("COMPONENT_QTY") +","+
			            colValue.get("COM_BACKEND_OPTION"));
			    log.WriterToLog("Error:"+ line + "\n");
		}else{
			TDSLogger.println("Data Format Error : "+ line + ")\n");
		}
	}
	

    public EifSAPTIM03(String arg[]) {
        boolean result = true;
        String line = "";

        TDSLogger.println("======================================================");
        TDSLogger.println("EifSAPTIM03 run at " + DateUtil.getNow());
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
        //String filepath ="D:\\EIFDATA\\TOTDS\\EIFSAPTIM03\\202507311313175578.comp";
        try {
            line = "";
            File file = new File(filepath);
            is = new FileInputStream(file);
            //BufferedReader bf = new BufferedReader(new InputStreamReader( new FileInputStream(file), "Big5"));
            //20150525isr = new InputStreamReader(is,"UTF-8");
            isr = new InputStreamReader(is,"Big5");
            br = new BufferedReader(isr);
          //Added by HouYu for #218963
			formattedBr = SapEncoding.formatInput(br, eifName);
            
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
            
            //Modify by HouYu for #218963
            //line = br.readLine();
        	line = formattedBr.readLine();
              
            while (line != null) {
                readAndPut(conn, line);
                //Modify by HouYu for #218963
                //line = br.readLine();
        		line = formattedBr.readLine();
                conn.commit();

            }
        } catch (Exception ex) {
            TDSLogger.println(ex);
            DBConnection.rollback(conn);
            SendMail.send("FAB_PEIS", "PEISAdmin", "SAP to TIM (Component Product Data) interface error(EifSapTim03)", ex.toString()+"\n"+line);
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
                SendMail.send("FAB_PEIS", "PEISAdmin", "SAP to TIM (Component Product Data) interface error(EifSapTim03)", e.toString()+"\n"+line);
            }
        }
    }
    private int getEachColValue(String mqString, HashMap colValue){
        int result = -1;
            if(mqString.length()>=35){
            	//Modify by HouYu for #218963
            	/*colValue.put("FLAG", mqString.substring(0,1).trim());//FLAG(1)
    			colValue.put("PROD_BODY", mqString.substring(1,5).trim());//PROD_BODY(4)
    			colValue.put("MASK_OPTION", mqString.substring(5,6).trim());//MASK_OPTION(1)
    			colValue.put("BACKEND_OPTION", mqString.substring(6,7).trim());//BACKEND_OPTION(1)
    			colValue.put("EPN", mqString.substring(7,25).trim());//EPN(18)
    			colValue.put("COM_PROD_BODY", mqString.substring(25,29).trim());//COM_PROD_BODY(4)
    			colValue.put("COM_MASK_OPTION", mqString.substring(29,30).trim());//COM_MASK_OPTION(1)
    			colValue.put("COMPONENT_NO", mqString.substring(30,31).trim());//COMPONENT_NO(1)
    			colValue.put("COMPONENT_QTY", mqString.substring(31,33).trim());//COMPONENT_QTY(2)
    			colValue.put("COM_BACKEND_OPTION", mqString.substring(33,34).trim());//COM_BACKEND_OPTION(1)*/
            	String[] tempStrings = mqString.split("\\t");
            	int minLength = 10; //colValues
            	if (tempStrings.length < minLength)
            		tempStrings = StringUtil.copySplitArray(tempStrings, minLength);
				colValue.put("FLAG", tempStrings[0].trim());//FLAG(1)
				colValue.put("PROD_BODY", tempStrings[1].trim());//PROD_BODY(4)
				colValue.put("MASK_OPTION", tempStrings[2].trim());//MASK_OPTION(1)
				colValue.put("BACKEND_OPTION", tempStrings[3].trim());//BACKEND_OPTION(1)
				colValue.put("EPN", tempStrings[4].trim());//EPN(18)
				colValue.put("COM_PROD_BODY", tempStrings[5].trim());//COM_PROD_BODY(4)
				colValue.put("COM_MASK_OPTION", tempStrings[6].trim());//COM_MASK_OPTION(1)
				colValue.put("COMPONENT_NO", tempStrings[7].trim());//COMPONENT_NO(1)
				colValue.put("COMPONENT_QTY", tempStrings[8].trim());//COMPONENT_QTY(2)
				colValue.put("COM_BACKEND_OPTION", tempStrings[9].trim());//COM_BACKEND_OPTION(1)
				result = 1;
                return result;
            }else{ 
            	return -1;
            }
      }
    public static void main(String arg[]) {
        EifSAPTIM03 eif = new EifSAPTIM03(arg);
        eif = null;
    }
}