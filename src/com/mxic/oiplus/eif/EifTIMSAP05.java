package com.mxic.oiplus.eif;

import java.sql.*;
import java.util.LinkedHashMap;
import java.io.*;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.*;
import com.mxic.oiplus.resource.*;
import com.mxic.tdsplus.util.SapEncoding;

public class EifTIMSAP05 extends SQLStatement {
    private Connection connection = null;
    IF_TF_YIELD_DEFINITION[] tpr = null;

    private static String eifName = "EifTIMSAP05";
    private static String eifPath = "/EIFDATA/FROMTDS/EIFTIMSAP05/";
    //private static String eifPath = "D:\\EIFDATA\\FROMTDS\\EIFTIMSAP05\\";

    public EifTIMSAP05() {
    }

    public void processEifTIMSAP05(int i) {
        try {
            connection = DBConnection.getConnection();
            if (EIFService.updateInterfaceTime(eifName, "CURRENT_TIME") == 1) {
                boolean flag = selectPRODUCTROUTEData(i);
                if (flag) {
                    output();
                } else
                    TDSLogger.println("Nothing to do !!");
                EIFService.updateInterfaceTime(eifName, "LAST_TIME");
            }
        } catch (Exception e) {
            TDSLogger.println(e);
        } finally {
            try {
                connection.close();
                connection = null;
            } catch (Exception e) {
            }
        }
    }

    /*參數0表示有區段傳送，否則就是全部一次送過去*/

    private boolean selectPRODUCTROUTEData(int i) {
        String interval = (String) TDSResource.getProperties("EIF").get("to.sap1.cron.interval");
        DataHandlerUtil dataUtil = new DataHandlerUtil();
        try {
            String sql =

                    "SELECT DISTINCT\n" +
                    "       DECODE(A.FACILITY, 0, 'WS', 1, 'FT') STAGE,\n" + 
                    "       B.BRAND BRAND,\n" + 
                    "       B.VERSION VERSION,\n" + 
                    "       A.PRODUCT_CODE PRODUCT_CODE,A.ROUTE_NAME,\n" + 
                    "       DECODE(INSTR(A.TEST_MODE,'/',1),0,A.TEST_MODE, SUBSTR(A.TEST_MODE,0,INSTR(A.TEST_MODE,'/',1)-1)) TEST_MODE,\n" + 
                    "       A.ACTION ACTION,\n" + 
                    "       DECODE(A.FACILITY, 0, A.DGRADE_SPECIAL_IPN, 1, A.DG_ACTION) DG_ACTION,\n" + 
                    "       DECODE(A.BY_LOT_DG, 'By lot Dgrade', 'Y') ALL_LOT_DGRADE,\n" + 
                    "       A.DGRADEPRODCODE DGRADEPRODCODE,\n" + 
                    "       to_char(C.LOG_TIME,'YYYYMMDD') RELEASE_DATE\n" + 
                    "\n" + 
                    "  FROM TF_YIELD_DEFINITION A, TF_CURRENT_VERSION_VW B, TF_INFORMATION C " ;
            if(i == 0) {
                sql +=  ", if_interface_time D\n" ; 
            }
             sql += " WHERE SUBSTR(A.PRODUCT_CODE, 0, 4) = B.PRODUCT_BODY\n" + 
                    "   AND A.SID = B.SID\n" + 
                    "   AND SUBSTR(A.PRODUCT_CODE, 0, 4) = C.PRODUCT_BODY\n" + 
                    "   AND B.BRAND = C.BRAND\n" + 
                    "   AND B.VERSION = C.VERSION\n" + 
                    "   AND A.SID = C.SID\n" + 
                    "   AND C.PRODUCT_TYPE = 'NVM'\n" + 
                    "   AND (A.ACTION LIKE '%Dgrade%' OR A.ACTION LIKE 'Change IPN%')\n" ;
            if(i == 0) {
                   sql += "   and d.interface = '"+eifName+"' \n" ;
                   sql += "   and c.log_time > d.last_time and c.log_time <= d.current_time \n" ;
            }
                    
            tpr = (IF_TF_YIELD_DEFINITION[]) dataUtil.getData(connection, sql, IF_TF_YIELD_DEFINITION.class, null).toArray(new IF_TF_YIELD_DEFINITION[0]);
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

    /*private void output() {
        FileOutputStream os = null;
        PrintWriter pr = null;
        try {
            os = new FileOutputStream(eifPath + "TIMSAP05-" + (String) DateUtil.Timestamp2String(new java.util.Date(), "yyyyMMddkkmm"));
            pr = new PrintWriter(os);

            StringBuffer output = new StringBuffer();

            for (int i = 0; i < tpr.length; i++) {
                output.delete(0, output.length());
                output.append(output_stiring(tpr[i].STAGE, 2));
                output.append(output_stiring(tpr[i].BRAND, 2));
                output.append(output_stiring(tpr[i].VERSION+"", 3));
                output.append(output_stiring(tpr[i].PRODUCT_CODE, 5));
                output.append(output_stiring(tpr[i].getTEST_MODE(), 6));
                output.append(output_stiring(tpr[i].ACTION, 20));
                output.append(output_stiring(tpr[i].DG_ACTION, 100));
                output.append(output_stiring(tpr[i].ALL_LOT_DGRADE, 2));
                output.append(output_stiring(tpr[i].RELEASE_DATE+"", 8));
                output.append(output_stiring(tpr[i].ROUTE_NAME+"", 12));
                output.append(output_stiring(tpr[i].DGRADEPRODCODE+"", 5));
                pr.println(output.toString());
                TDSLogger.println("EifTIMSAP05-SNDMSG--------");
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
    }*/
    
    //#217143
    private void output() {
		FileOutputStream os = null;
		PrintWriter pr = null;
		try {
			os = new FileOutputStream(eifPath
					+ "TIMSAP05-"
					+ (String) DateUtil.Timestamp2String(new java.util.Date(),
							"yyyyMMddkkmm"));
			pr = new PrintWriter(os);

			StringBuffer output = new StringBuffer();

			for (int i = 0; i < tpr.length; i++) {
				LinkedHashMap<String, String> info = new LinkedHashMap<String, String>();
				output.delete(0, output.length());
				info.put("STAGE", tpr[i].STAGE);
				info.put("BRAND", tpr[i].BRAND);
				info.put("VERSION", tpr[i].VERSION + "");
				info.put("PRODUCT_CODE", tpr[i].PRODUCT_CODE);
				info.put("TEST_MODE", tpr[i].getTEST_MODE());
				info.put("ACTION", tpr[i].ACTION);
				info.put("DG_ACTION", tpr[i].DG_ACTION);
				info.put("ALL_LOT_DGRADE", tpr[i].ALL_LOT_DGRADE);
				info.put("RELEASE_DATE", tpr[i].RELEASE_DATE + "");
				info.put("ROUTE_NAME", tpr[i].ROUTE_NAME + "");
				info.put("DGRADEPRODCODE", tpr[i].DGRADEPRODCODE + "");

				output = SapEncoding.formatOutput(info, eifName);
				pr.print(output.toString());
				TDSLogger.println("EifTIMSAP05-SNDMSG--------");
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
            return value.trim() + padding(length - value.trim().length());
    }*/

    // Main procedure
    public static void main(String args[]) {
        EifTIMSAP05 sap = new EifTIMSAP05();

        try {
            sap.processEifTIMSAP05(args.length);
        } catch (Exception e) {
            TDSLogger.println(e);
        }
    }
}
