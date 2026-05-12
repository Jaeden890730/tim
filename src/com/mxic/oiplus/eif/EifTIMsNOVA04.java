package com.mxic.oiplus.eif;

import java.sql.*;
import java.util.HashMap;
import java.io.*;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.*;
import com.mxic.oiplus.resource.*;
import com.mxic.tdsplus.util.TDSLogger;

public class EifTIMsNOVA04 extends SQLStatement {
    private Connection connection = null;
    IF_TF_YIELD[] tpr = null;

    private static String eifName = "EifTIMsNOVA04";
    private static String eifPath = "/EIFDATA/FROMTDS/EIFTIMSNOVA04/";
    //private static String eifPath = "d:\\Sophia\\PEIS\\EIFDATA\\FROMTDS\\EIFTIMSNOVA04\\";
    //private static String eifPath = "d:\\";
    public EifTIMsNOVA04() {
    }

    public void processEifTIMsNOVA04(int i) {
        try {
            connection = DBConnection.getConnection();
            if (EIFService.updateInterfaceTime(eifName, "CURRENT_TIME") == 1) {
                boolean flag = SELECTKGDProductData();
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
    
    
    private boolean SELECTKGDProductData() {
        DataHandlerUtil dataUtil = new DataHandlerUtil();
        try {
            String sql =

			"SELECT distinct b.product_body || B.MASK_OPTION as product_code, decode(b.wsspecialcontrol,'NA','KGD',b.wsspecialcontrol) action, c.step_name test_mode, TO_CHAR(A.LOG_TIME,'YYYY-MM-DD HH24:MI:SS') RELEASE_TIME\n" + 
			"FROM TF_INFORMATION A, tf_bom_route b, tf_product_route c, if_interface_time d\n" + 
			"WHERE A.STATUS = 'R'\n" + 
			"and a.brand = 'MX'\n" + 
			"and a.sid = b.sid\n" + 
			"and a.sid = c.sid\n" + 
			"and b.ws_route = c.route_name\n" + 
			"and (c.step_name like 'SORT%' or c.step_name like 'AVI%')\n" + 
			"and b.wsspecialcontrol in ('NA','KGD_AEB')\n" + 
			"and d.interface = 'EifTIMsNOVA04'\n" + 
			"and a.log_time > d.last_time and a.log_time <= d.current_time\n" + 
			"and c.route_name in (select cc.route_name from tf_product_route cc\n" + 
			"             where cc.sid = a.sid\n" + 
			"             and cc.step_name in ('AVI')\n" + 
			"             and cc.route_name = c.route_name)\n" + 
            "union all \n" +
            "SELECT distinct b.product_body || B.BACKEND_OPTION as product_code, 'NA' action , c.step_name test_mode, TO_CHAR(A.LOG_TIME,'YYYY-MM-DD HH24:MI:SS') RELEASE_TIME\n" + 
            "FROM TF_INFORMATION A, tf_bom_route b, tf_product_route c, if_interface_time d\n" + 
            "WHERE A.STATUS = 'R'\n" + 
            "and a.brand = 'MX'\n" + 
            "and a.sid = b.sid\n" + 
            "and a.sid = c.sid\n" + 
            "and (b.ft_route = c.route_name)\n" + 
            "and ( c.step_name like 'FT%')\n" + 
            "and d.interface = 'EifTIMsNOVA04'\n" + 
            "and a.log_time > d.last_time and a.log_time <= d.current_time\n" +
            "union all \n" +
            "SELECT distinct b.product_body || B.BACKEND_OPTION as product_code, 'NA' action , c.step_name test_mode, TO_CHAR(A.LOG_TIME,'YYYY-MM-DD HH24:MI:SS') RELEASE_TIME\n" + 
            "FROM TF_INFORMATION A, tf_bom_route_mcp b, tf_product_route c, if_interface_time d\n" + 
            "WHERE A.STATUS = 'R'\n" + 
            "and a.brand = 'MX'\n" + 
            "and a.sid = b.sid\n" + 
            "and a.sid = c.sid\n" + 
            "and (b.ft_route = c.route_name)\n" + 
            "and ( c.step_name like 'FT%')\n" + 
            "and d.interface = 'EifTIMsNOVA04'\n" + 
            "and a.log_time > d.last_time and a.log_time <= d.current_time";


            TDSLogger.println(sql);        
            tpr = (IF_TF_YIELD[]) dataUtil.getData(connection, sql, IF_TF_YIELD.class, null).toArray(new IF_TF_YIELD[0]);
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
            WriterFile wFile = new WriterFile(eifPath + "TIMSNOVAPEIS04-" + (String) DateUtil.Timestamp2String(new java.util.Date(), "yyyyMMddkkmm") + ".csv", false);
			wFile.Writeln("PRODUCT_CODE,TEST_MODE,FLAG,RELEASE_TIME");
            for (int i = 0; i < tpr.length; i++) {
				wFile.Write(tpr[i].PRODUCT_CODE+",");
				wFile.Write(tpr[i].TEST_MODE+",");
				wFile.Write(tpr[i].ACTION+",");
				wFile.Write(tpr[i].RELEASE_TIME);
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
    	EifTIMsNOVA04 sap = new EifTIMsNOVA04();

        try {
            sap.processEifTIMsNOVA04(args.length);
        } catch (Exception e) {
            TDSLogger.println(e);
        }
    }
}
