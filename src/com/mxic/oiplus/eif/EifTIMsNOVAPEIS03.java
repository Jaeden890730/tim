package com.mxic.oiplus.eif;

import java.sql.*;
import java.util.HashMap;
import java.io.*;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.*;
import com.mxic.oiplus.resource.*;
import com.mxic.tdsplus.util.TDSLogger;

public class EifTIMsNOVAPEIS03 extends SQLStatement {
    private Connection connection = null;
    IF_TF_YIELD[] tpr = null;
    IF_TF_YIELD[] tpr_delete = null;
    IF_TF_YIELD[] tpr_1 = null;

    private static String eifName = "EifTIMsNOVAPEIS03";
    private static String eifPath = "/EIFDATA/FROMTDS/EIFTIMSNOVA03/";
    //private static String eifPath = "d:\\Sophia\\PEIS\\EIFDATA\\FROMTDS\\EIFTIMSNOVA03\\";

    public EifTIMsNOVAPEIS03() {
    }

    public void processEifTIMsNOVAPEIS03(int i) {
        try {
            connection = DBConnection.getConnection();
            if (EIFService.updateInterfaceTime(eifName, "CURRENT_TIME") == 1) {
                boolean flag = updateAEBRetintionBinData();
                boolean flag_1 = SELECTAEBRetintionBinData();
                if (flag && flag_1) {
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

    private boolean updateAEBRetintionBinData() {
        DataHandlerUtil dataUtil = new DataHandlerUtil();
        try {
        	
        	String sql_delete =
			"SELECT distinct a.product_body \n" +
			"FROM TF_INFORMATION A, if_interface_time c \n" +
            "WHERE A.STATUS = 'R' \n" + 
            "AND A.BRAND = 'MX' \n" +
            "and c.interface = '"+eifName+"' \n" +
            "and a.log_time > c.last_time and a.log_time <= c.current_time \n" ;

            TDSLogger.println(sql_delete);        
            tpr_delete = (IF_TF_YIELD[]) dataUtil.getData(connection, sql_delete, IF_TF_YIELD.class, null).toArray(new IF_TF_YIELD[0]);
            if (tpr_delete.length == 0)
                return false;
            for (int j = 0; j < tpr_delete.length; j++) {
            	delete_BA_FT_AEB_RETENTION_NEWEST(tpr_delete[j].PRODUCT_BODY);
            }
                    
            String sql =
			"SELECT distinct a.sid, a.product_body, b.test_mode, replace(b.item,'AEBRetentionBIN','') bin_no, b.snova_id, b.version \n" +
			"FROM TF_INFORMATION A, TF_YIELD_DEFINITION B, if_interface_time c \n" +
            "WHERE A.SID = B.SID \n" +
            "AND A.STATUS = 'R' \n" + 
            "AND A.BRAND = 'MX' \n" +
            "AND b.snova_id is not null \n" + 
            "AND b.version is not null \n" + 
            "AND b.item like 'AEBRetentionBIN%' \n" +
            "and c.interface = '"+eifName+"' \n" +
            "and a.log_time > c.last_time and a.log_time <= c.current_time \n" ;

            TDSLogger.println(sql);        
            tpr = (IF_TF_YIELD[]) dataUtil.getData(connection, sql, IF_TF_YIELD.class, null).toArray(new IF_TF_YIELD[0]);
            if (tpr.length == 0){
            	output_NoData();
                return false;
            }    
            for (int j = 0; j < tpr.length; j++) {
            	update_BA_FT_AEB_RETENTION(tpr[j].PRODUCT_BODY, tpr[j].TEST_MODE, tpr[j].BIN_NO, tpr[j].SNOVA_ID, tpr[j].VERSION );
            	insert_BA_FT_AEB_RETENTION_NEWEST(tpr[j].PRODUCT_BODY, tpr[j].TEST_MODE, tpr[j].BIN_NO, tpr[j].SNOVA_ID, tpr[j].VERSION );
			}
        } catch (Exception e) {
            TDSLogger.println(e);
        } finally {
        }
        return true;
    }
    private void update_BA_FT_AEB_RETENTION(String product_body, String test_mode, String bin_no, Number snova_id, Number version)
    {
      PreparedStatement stmt = null;
      String sqlstr = "update ba_ft_aeb_retention set version = ? \n" +
                      "where product_body = ? \n" +
    		          "  and test_mode = ? \n" +
                      "  and bin_no = ? \n " +
    		          "  and snova_id = ? \n";
      try {
        stmt = connection.prepareStatement(sqlstr);
        stmt.setInt(1,version.intValue());
        stmt.setString(2,product_body);
        stmt.setString(3,test_mode);
        stmt.setInt(4, Integer.parseInt(bin_no));
        stmt.setInt(5,snova_id.intValue());
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
    
    public boolean delete_BA_FT_AEB_RETENTION_NEWEST(String product_body)
    {
        String sql;
        Object[] obj;
        boolean result = false;
        PreparedStatement stmt = null;
        sql ="DELETE FROM BA_FT_AEB_RETENTION_NEWEST \n" + 
        	 "where product_body = ? \n";
		     //"  and test_mode = ? \n";
             //"  and bin_no = ? \n ";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1,product_body);
            //stmt.setString(2,test_mode);
            //stmt.setInt(3,Integer.parseInt(bin_no));
            stmt.executeUpdate();
            return true;
          } catch(Exception e){
            TDSLogger.println(e);
            return false;
          } finally {
            try {
              stmt.close();
              stmt = null;
            } catch (Exception e) {
              TDSLogger.println(e);
            }
          }
    }
    
    private boolean insert_BA_FT_AEB_RETENTION_NEWEST(String product_body, String test_mode, String bin_no, Number snova_id, Number version)
    {
      PreparedStatement stmt = null;
      String sqlstr = "insert into ba_ft_aeb_retention_newest(PRODUCT_BODY,TEST_MODE,CP_TEST_MODE,BIN_NAME,BIN_NO,UPDATE_TIME,SNOVA_ID,VERSION) \n" +
                      "select distinct PRODUCT_BODY,TEST_MODE,CP_TEST_MODE,BIN_NAME,BIN_NO,sysdate,SNOVA_ID,VERSION from ba_ft_aeb_retention \n" +
                      "where product_body = ? \n" +
    		          "  and test_mode = ? \n" +
                      "  and bin_no = ? \n " +
    		          "  and snova_id = ? \n";
      try {
        stmt = connection.prepareStatement(sqlstr);
        stmt.setString(1,product_body);
        stmt.setString(2,test_mode);
        stmt.setInt(3,Integer.parseInt(bin_no));
        stmt.setInt(4,snova_id.intValue());
        int succ = stmt.executeUpdate();
        return true;
      } catch(Exception e){
        TDSLogger.println(e);
        return false;
      } finally {
        try {
          stmt.close();
          stmt = null;
        } catch (Exception e) {
          TDSLogger.println(e);
        }
      }
    }
    
    private boolean SELECTAEBRetintionBinData() {
        DataHandlerUtil dataUtil = new DataHandlerUtil();
        try {
            String sql =
			"SELECT distinct a.product_body, a.test_mode, a.cp_test_mode, a.bin_name, to_char(a.bin_no) bin_no, a.snova_id, a.version \n" +
			"FROM ba_ft_aeb_retention_newest A, if_interface_time c \n" +
            "WHERE c.interface = '"+eifName+"' \n" +
            "and a.update_time >= c.current_time \n" ;

            TDSLogger.println(sql);        
            tpr_1 = (IF_TF_YIELD[]) dataUtil.getData(connection, sql, IF_TF_YIELD.class, null).toArray(new IF_TF_YIELD[0]);
            if (tpr_1.length == 0)
                return false;
        } catch (Exception e) {
            TDSLogger.println(e);
        } finally {
        }
        return true;
    }



    private void output() {
        try {
            WriterFile wFile = new WriterFile(eifPath + "TIMSNOVAPEIS03-" + (String) DateUtil.Timestamp2String(new java.util.Date(), "yyyyMMddkkmm") + ".csv", false);
			wFile.Writeln("PRODUCT_BODY,TEST_MODE,CP_TEST_MODE,BIN_NAME,BIN_NO,SNOVA_ID,VERSION");
            for (int i = 0; i < tpr.length; i++) {
				wFile.Write(tpr_1[i].PRODUCT_BODY+",");
				wFile.Write(tpr_1[i].TEST_MODE+",");
				wFile.Write(tpr_1[i].CP_TEST_MODE+",");
				wFile.Write(tpr_1[i].BIN_NAME+",");
				wFile.Write(tpr_1[i].BIN_NO+",");
				wFile.Write(tpr_1[i].SNOVA_ID+",");
				wFile.Write(tpr_1[i].VERSION);
				wFile.Writeln("");
            }
            wFile.close();
            TDSLogger.println("--SUCCESS!!!");
        } catch (Exception e) {
            TDSLogger.println(e);
        } finally {
        }
    }
    
    private void output_NoData() {
        try {
            WriterFile wFile = new WriterFile(eifPath + "TIMSNOVAPEIS03-" + (String) DateUtil.Timestamp2String(new java.util.Date(), "yyyyMMddkkmm") + ".csv", false);
			wFile.Writeln("PRODUCT_BODY,TEST_MODE,CP_TEST_MODE,BIN_NAME,BIN_NO,SNOVA_ID,VERSION");
            for (int i = 0; i < tpr_delete.length; i++) {
				wFile.Write(tpr_delete[i].PRODUCT_BODY+",");
				wFile.Write("NoData"+",");
				wFile.Write(""+",");
				wFile.Write(""+",");
				wFile.Write(""+",");
				wFile.Write(""+",");
				wFile.Write("");
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
    	EifTIMsNOVAPEIS03 sap = new EifTIMsNOVAPEIS03();

        try {
            sap.processEifTIMsNOVAPEIS03(args.length);
        } catch (Exception e) {
            TDSLogger.println(e);
        }
    }
}
