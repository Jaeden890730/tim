package com.mxic.oiplus.eif;

import java.sql.*;
import java.io.*;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.*;
import com.mxic.oiplus.resource.*;

public class EifTIMsNOVA01 extends SQLStatement {
    private Connection connection = null;
    IF_TF_YIELD[] tpr = null;

    private static String eifName = "EifTIMsNOVA01";
    private static String eifPath = "/EIFDATA/FROMTDS/EIFTIMSNOVA01/";
    //private static String eifPath = "d:\\Sophia\\PEIS\\EIFDATA\\FROMTDS\\EIFTIMSNOVA01\\";
    //private static String eifPath = "d:\\";
    public EifTIMsNOVA01() {
    }

    public void processEifTIMsNOVA01(int i) {
        try {
            connection = DBConnection.getConnection();
            if (EIFService.updateInterfaceTime(eifName, "CURRENT_TIME") == 1) {
                boolean flag = selectYieldData(i);
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

    private boolean selectYieldData(int i) {
        DataHandlerUtil dataUtil = new DataHandlerUtil();
        try {
            String sql =
			"SELECT DISTINCT b.PRODUCT_CODE, b.TEST_MODE, B.ACTION,B.ITEM, TO_CHAR (decode(b.action, 'Auto Ship', b.lower_limit, b.Upper_Limit)) yield \n" +
			"FROM TF_INFORMATION A, TF_YIELD_DEFINITION B \n";
            if(i == 0) {
                sql +=  ", if_interface_time c\n" ; 
            }
            if(i == 1) {
            	sql +=  ", tf_current_version_vw d\n" ; 
            }
             sql += "WHERE A.SID = B.SID \n" +
            		 "AND A.STATUS = 'R' \n" + 
            		 "AND A.BRAND='MX' \n" + 
            		 "AND B.ACTION in ('Scrap', 'Auto Ship') \n" +
            		 "AND B.ITEM = 'Yield' \n";
            if(i == 0) {
                   sql += "   and c.interface = '"+eifName+"' \n" ;
                   sql += "   and a.log_time > c.last_time and a.log_time <= c.current_time \n" ;
            }
            if(i == 1) {
            	   sql += "   and a.sid = d.sid \n" ;
            }
            
            String sql1 =
			"union all  SELECT DISTINCT b1.PRODUCT_CODE, b1.TEST_MODE, B1.ACTION,B1.ITEM, TO_CHAR (decode(b1.item, 'Yield', b1.Upper_Limit, b1.lower_limit)) yield \n" +
			"FROM TF_INFORMATION A1, TF_YIELD_DEFINITION B1 \n";
            if(i == 0) {
                sql1 +=  ", if_interface_time c1\n" ; 
            }
            if(i == 1) {
            	sql1 +=  ", tf_current_version_vw d1\n" ; 
            }
             sql1 += "WHERE A1.SID = B1.SID \n" +
            		 "AND A1.STATUS = 'R' \n" + 
            		 "AND A1.BRAND='MX' \n" + 
            		 "AND B1.ACTION like ('OOC%')  \n";
            if(i == 0) {
                   sql1 += "   and c1.interface = '"+eifName+"' \n" ;
                   sql1 += "   and a1.log_time > c1.last_time and a1.log_time <= c1.current_time \n" ;
            }
            if(i == 1) {
            	   sql1 += "   and a1.sid = d1.sid \n" ;
            }

            
            TDSLogger.println(sql+sql1);        
            tpr = (IF_TF_YIELD[]) dataUtil.getData(connection, (sql+sql1), IF_TF_YIELD.class, null).toArray(new IF_TF_YIELD[0]);
            if (tpr.length == 0)
                return false;
        } catch (Exception e) {
            TDSLogger.println(e);
        } finally {
        }
        return true;
    }


    private void output() {
        try {
            WriterFile wFile = new WriterFile(eifPath + "TIMSNOVA01-" + (String) DateUtil.Timestamp2String(new java.util.Date(), "yyyyMMddkkmm") + ".csv", false);
			wFile.Writeln("PRODUCT_CODE,TEST_MODE,ACTION,ITEM,YIELD");
            for (int i = 0; i < tpr.length; i++) {
				wFile.Write(tpr[i].PRODUCT_CODE+",");
				wFile.Write(tpr[i].TEST_MODE+",");
				wFile.Write(tpr[i].ACTION+",");
				wFile.Write(tpr[i].ITEM+",");
				wFile.Write(tpr[i].YIELD);
				wFile.Writeln("");
            }
            wFile.close();
            TDSLogger.println("--SUCCESS!!!");
        } catch (Exception e) {
            TDSLogger.println(e);
        } finally {
        }
    }


    // Main procedure
    public static void main(String args[]) {
    	EifTIMsNOVA01 sap = new EifTIMsNOVA01();

        try {
            sap.processEifTIMsNOVA01(args.length);
        } catch (Exception e) {
            TDSLogger.println(e);
        }
    }
}
