package com.mxic.oiplus.eif;

import java.sql.*;
import java.util.HashMap;
import java.io.*;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.*;
import com.mxic.tdsplus.util.DataHandlerUtil;
import com.mxic.tdsplus.util.StringUtil;

public class EifTIMsNOVA02 extends SQLStatement {
    private Connection connection = null;
    IF_TF_YIELD[] tpr = null;

    private static String eifName = "EifTIMsNOVA02";
    private static String eifPath = "/EIFDATA/FROMTDS/EIFPEISDSSD02/";
    //private static String eifPath = "D:\\PEIS_DATA\\eif\\EIFPEISDSSD02\\";

    public EifTIMsNOVA02() {
    }

    public void processEifTIMsNOVA02(int i) {
        try {
            connection = DBConnection.getConnection();
            if (EIFService.updateInterfaceTime(eifName, "CURRENT_TIME") == 1) {
                selectWSSPCData(i);
                selectFTSPCData(i);
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

    /*把计0ボΤ跋琿肚癳玥碞琌场Ω癳筁*/

    private void selectWSSPCData(int ii) {
        DataHandlerUtil dataUtil = new DataHandlerUtil();
        try {
            String sql =
			"SELECT b.PRODUCT_CODE, b.TEST_MODE, B.BRAND, Decode(REPLACE(b.ITEM,'AEB SPC ',''),'Yield','0','AEB D05+D06','5566','D05+D06','5566','AEB D07','-7777',REPLACE(REPLACE(b.ITEM,'AEBSPCBin',''),'BIN','')) BIN_NO, \n"+
            "DECODE(b.ITEM, 'Yield', LOWER_LIMIT,'AEB SPC Yield', LOWER_LIMIT, UPPER_LIMIT) LIMIT \n" +
			"FROM TF_INFORMATION A, TF_YIELD_DEFINITION B \n";
            if(ii == 0) {
                sql +=  ", if_interface_time c\n" ; 
            }
            if(ii == 1) {
            	sql +=  ", tf_current_version_vw d\n" ; 
            }
             sql += "WHERE A.SID = B.SID \n" +
            		 "AND A.STATUS = 'R' \n" + 
            		 "AND A.BRAND = 'MX' \n" + 
            		 "AND B.BRAND in ('All', 'AEB') \n" +
            		 "AND ((B.ITEM like '%Bin%') OR (B.ITEM like '%BIN%') OR (B.ITEM like '%Yield%') OR (B.ITEM like '%D05%') OR (B.ITEM = 'AEB D07'))\n"+
            		 "AND B.ACTION in ('SPC Control', 'SPC Control Pass', 'SPC Hold','SPC Dgrade') \n" + 
            		 "AND B.FACILITY = 0 \n";
            if(ii == 0) {
                   sql += "   and c.interface = '"+eifName+"' \n" ;
                   sql += "   and a.log_time > c.last_time and a.log_time <= c.current_time \n" ;
            }
            if(ii == 1) {
            	   sql += "   and a.sid = d.sid \n" ;
            }
            TDSLogger.println(sql);        
            HashMap[] rs = new DataHandlerUtil().getDataBySql(connection, sql);
            //tpr = (IF_TF_YIELD[]) dataUtil.getData(connection, sql, IF_TF_YIELD.class, null).toArray(new IF_TF_YIELD[0]);
            if(rs!=null && rs.length>0){
    			WriterFile wFile = new WriterFile(eifPath+File.separator+"ws"+File.separator+"ws_bin_yield_spc.csv", true);
    			wFile.Writeln("PROD_ID, TEST_MODE, TESTER_TYPE, BRAND, BIN_NAME, BIN_NO, BIN_FLAG, LOW_LIMIT, UP_LIMIT,");
    			for(int i=0;i<rs.length;i++){
    				String item_type = (String)rs[i].get("ITEM_TYPE");
    				String bin_type = (String)rs[i].get("BIN_TYPE");
    				wFile.Write(StringUtil.formatNull(rs[i].get("PRODUCT_CODE"))+",");
    				wFile.Write(StringUtil.formatNull(rs[i].get("TEST_MODE"))+",");
    				wFile.Write(",");
    				wFile.Write(rs[i].get("BRAND")+ ",");
    				if ("0".equals(rs[i].get("BIN_NO").toString())) {  // for yield
    					wFile.Write("Yield,");
    					wFile.Write("0,");
    					wFile.Write("1,");
    					wFile.Write(StringUtil.formatNull(rs[i].get("LIMIT"))+",");
    				} else if ("5566".equals(rs[i].get("BIN_NO").toString())) {  // D05+D06
    					wFile.Write("D05D06,");
    					wFile.Write("5566,");
    					wFile.Write("0,");
    					wFile.Write("," + StringUtil.formatNull(rs[i].get("LIMIT")));
    				} else if ("-7777".equals(rs[i].get("BIN_NO").toString())) {  // D07
    					wFile.Write("D07,");
    					wFile.Write("-7777,");
    					wFile.Write("0,");
    					wFile.Write("," + StringUtil.formatNull(rs[i].get("LIMIT")));
    				} else {  // for bin
    					String[] bindata = getBinName(rs[i].get("PRODUCT_CODE").toString(),rs[i].get("TEST_MODE").toString(),rs[i].get("BIN_NO").toString());
    					wFile.Write(bindata[1] + ",");
    					wFile.Write(bindata[0] + ",");
    					wFile.Write(getPassFlag(rs[i].get("PRODUCT_CODE").toString(),rs[i].get("TEST_MODE").toString(),rs[i].get("BIN_NO").toString())+ ",");
    					wFile.Write("," + StringUtil.formatNull(rs[i].get("LIMIT")));
    				}
    				wFile.Writeln("");
    			}
    			wFile.close();
    		}
        } catch (Exception e) {
            TDSLogger.println(e);
        } finally {
        }
    }
    private String[] getBinName(String productCode, String TestMode, String BinNo) throws Exception{
		String[] result = new String[2];
		StringBuffer bin_no = new StringBuffer();
		StringBuffer bin_name = new StringBuffer();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT 1 FLAG, W.BIN_NO, W.SHORT_NAME \n");
		sql.append("FROM BA_BIN_WS W \n");
		sql.append("WHERE W.PRODUCT_CODE = ? \n");
		sql.append("AND W.TEST_MODE = ? \n");
		sql.append("AND W.VERSION = 'PA' \n");
		sql.append("AND W.BIN_NO IN ('"+(BinNo!=null?BinNo.replace("+", "','"):"0")+"') \n");
		sql.append("ORDER BY FLAG, BIN_NO ");
		Object[] wheres = null;
		wheres = new Object[2];
		wheres[0] = productCode;
		wheres[1] = TestMode;
		//wheres[2] = (BinNo!=null?"'"+BinNo.replace("+", "','")+"'":"'0'");
		TDSLogger.println(sql);
		HashMap[] rs = new DataHandlerUtil().getDataBySql(connection, sql.toString(), wheres);

		if ((rs!= null) && (rs.length > 0)) {
			for (int i=0; i<rs.length; i++) {
				bin_no.append(rs[i].get("BIN_NO") + "+");
				bin_name.append(rs[i].get("SHORT_NAME") + "+");
			}
		}
		if(bin_no.length() > 0)
			bin_no.deleteCharAt(bin_no.length() - 1);
		if (bin_name.length() > 0)
			bin_name.deleteCharAt(bin_name.length() - 1);
		result[0] = bin_no.toString();
		result[1] = bin_name.toString();

		return result;
	}
    private int getPassFlag(String productCode, String TestMode, String BinNo) throws Exception{
    	if (BinNo.indexOf("+")>=0)
			return -1;
    	StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT W.BIN_NO, W.SHORT_NAME, W.PASS_FLAG \n");
		sql.append("FROM BA_BIN_WS W \n");
		sql.append("WHERE W.PRODUCT_CODE = ? \n");
		sql.append("AND W.TEST_MODE = ?  \n");
		sql.append("AND W.BIN_NO IN (?) \n");
		sql.append("AND W.VERSION = 'PA' \n");
		sql.append("ORDER BY BIN_NO ");

		Object[] wheres = null;
		wheres = new Object[3];
		wheres[0] = productCode;
		wheres[1] = TestMode;
		wheres[2] = BinNo;

		HashMap[] rs = new DataHandlerUtil().getDataBySql(connection, sql.toString(), wheres);

		if ((rs!= null) && (rs.length > 0)) {
			if (rs.length > 1) //merge bin or single bin but exist more than 1 data
				return -1;
			else // single bin
				return Integer.parseInt((String)rs[0].get("PASS_FLAG"));
		} else
			return -1;

	}
    /*把计0ボΤ跋琿肚癳玥碞琌场Ω癳筁*/

    private void selectFTSPCData(int ii) {
        DataHandlerUtil dataUtil = new DataHandlerUtil();
        try {
            String sql =
			"SELECT b.PRODUCT_CODE, b.TEST_MODE, B.BRAND, Decode(REPLACE(b.ITEM,'AEB SPC ',''),'Yield','0',REPLACE(REPLACE(b.ITEM,'AEBSPCBin',''),'BIN','')) BIN_NO, \n"+//AEBRetentionBINぃノъ
            "DECODE(b.ITEM, 'Yield', UPPER_LIMIT,'AEB SPC Yield', UPPER_LIMIT, LOWER_LIMIT) LIMIT \n" +
			"FROM TF_INFORMATION A, TF_YIELD_DEFINITION B \n";
            if(ii == 0) {
                sql +=  ", if_interface_time c\n" ; 
            }
            if(ii == 1) {
            	sql +=  ", tf_current_version_vw d\n" ; 
            }
             sql += "WHERE A.SID = B.SID \n" +
            		 "AND A.STATUS = 'R' \n" + 
            		 "AND A.BRAND = 'MX' \n" + 
            		 "AND B.BRAND in ('All', 'AEB') \n" +
            		 "AND ((B.ITEM like '%Bin%') OR (B.ITEM like '%BIN%') OR (B.ITEM like '%Yield%'))\n"+
            		 "AND B.ITEM NOT LIKE '%AEBRetention%' \n" +
            		 "AND B.ACTION in ('SPC Control', 'SPC Control Pass', 'SPC Hold','SPC Dgrade') \n" + 
            		 "AND B.FACILITY = 1 \n";
            if(ii == 0) {
                   sql += "   and c.interface = '"+eifName+"' \n" ;
                   sql += "   and a.log_time > c.last_time and a.log_time <= c.current_time \n" ;
            }
            if(ii == 1) {
            	   sql += "   and a.sid = d.sid \n" ;
            }
            TDSLogger.println(sql);        
            HashMap[] rs = new DataHandlerUtil().getDataBySql(connection, sql);
            //tpr = (IF_TF_YIELD[]) dataUtil.getData(connection, sql, IF_TF_YIELD.class, null).toArray(new IF_TF_YIELD[0]);
            if(rs!=null && rs.length>0){
    			WriterFile wFile = new WriterFile(eifPath+File.separator+"ft"+File.separator+"ft_bin_yield_spc.csv", true);
    			wFile.Writeln("PROD_ID, TEST_MODE, TESTER_TYPE, BRAND, BIN_NAME, BIN_NO, BIN_FLAG, LOW_LIMIT, UP_LIMIT,");
    			for(int i=0;i<rs.length;i++){
    				String item_type = (String)rs[i].get("ITEM_TYPE");
    				String bin_type = (String)rs[i].get("BIN_TYPE");
    				wFile.Write(StringUtil.formatNull(rs[i].get("PRODUCT_CODE"))+",");
    				wFile.Write(StringUtil.formatNull(rs[i].get("TEST_MODE"))+",");
    				wFile.Write(",");
    				wFile.Write(rs[i].get("BRAND")+ ",");
    				if ("0".equals(rs[i].get("BIN_NO").toString())) {  // for yield
    					wFile.Write("Yield,");
    					wFile.Write("0,");
    					wFile.Write("1,");
    					wFile.Write(StringUtil.formatNull(rs[i].get("LIMIT"))+",");
    				} else {  // for bin
    					String[] bindata = getBinNameFT(rs[i].get("PRODUCT_CODE").toString(),rs[i].get("TEST_MODE").toString(),rs[i].get("BIN_NO").toString());
    					wFile.Write(bindata[1] + ",");
    					wFile.Write(bindata[0] + ",");
    					wFile.Write(getPassFlagFT(rs[i].get("PRODUCT_CODE").toString(),rs[i].get("TEST_MODE").toString(),rs[i].get("BIN_NO").toString())+ ",");
    					wFile.Write("," + StringUtil.formatNull(rs[i].get("LIMIT")));
    				}
    				wFile.Writeln("");
    			}
    			wFile.close();
    		}
        } catch (Exception e) {
            TDSLogger.println(e);
        } finally {
        }
    }
    private String[] getBinNameFT(String productCode, String TestMode, String BinNo) throws Exception{
		String[] result = new String[2];
		StringBuffer bin_no = new StringBuffer();
		StringBuffer bin_name = new StringBuffer();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT 1 FLAG, W.BIN_NO, W.SHORT_NAME \n");
		sql.append("FROM BA_BIN_FT W \n");
		sql.append("WHERE W.PRODUCT_CODE = ? \n");
		sql.append("AND W.TEST_MODE = ? \n");
		sql.append("AND W.VERSION = 'PA' \n");
		sql.append("AND W.BIN_NO IN ('"+(BinNo!=null?BinNo.replace("+", "','"):"0")+"') \n");
		sql.append("ORDER BY FLAG, BIN_NO ");
		Object[] wheres = null;
		wheres = new Object[2];
		wheres[0] = productCode;
		wheres[1] = TestMode;
		//wheres[2] = (BinNo!=null?"'"+BinNo.replace("+", "','")+"'":"'0'");
		TDSLogger.println(sql);
		HashMap[] rs = new DataHandlerUtil().getDataBySql(connection, sql.toString(), wheres);

		if ((rs!= null) && (rs.length > 0)) {
			for (int i=0; i<rs.length; i++) {
				bin_no.append(rs[i].get("BIN_NO") + "+");
				bin_name.append(rs[i].get("SHORT_NAME") + "+");
			}
		}
		if(bin_no.length() > 0)
			bin_no.deleteCharAt(bin_no.length() - 1);
		if (bin_name.length() > 0)
			bin_name.deleteCharAt(bin_name.length() - 1);
		result[0] = bin_no.toString();
		result[1] = bin_name.toString();

		return result;
	}
    private int getPassFlagFT(String productCode, String TestMode, String BinNo) throws Exception{
    	if (BinNo.indexOf("+")>=0)
			return -1;
    	StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT W.BIN_NO, W.SHORT_NAME, W.PASS_FLAG \n");
		sql.append("FROM BA_BIN_FT W \n");
		sql.append("WHERE W.PRODUCT_CODE = ? \n");
		sql.append("AND W.TEST_MODE = ?  \n");
		sql.append("AND W.BIN_NO IN (?) \n");
		sql.append("AND W.VERSION = 'PA' \n");
		sql.append("ORDER BY BIN_NO ");

		Object[] wheres = null;
		wheres = new Object[3];
		wheres[0] = productCode;
		wheres[1] = TestMode;
		wheres[2] = BinNo;

		HashMap[] rs = new DataHandlerUtil().getDataBySql(connection, sql.toString(), wheres);

		if ((rs!= null) && (rs.length > 0)) {
			if (rs.length > 1) //merge bin or single bin but exist more than 1 data
				return -1;
			else // single bin
				return Integer.parseInt((String)rs[0].get("PASS_FLAG"));
		} else
			return -1;

	}



    // Main procedure
    public static void main(String args[]) {
    	EifTIMsNOVA02 sap = new EifTIMsNOVA02();

        try {
            sap.processEifTIMsNOVA02(args.length);
        } catch (Exception e) {
            TDSLogger.println(e);
        }
    }
}
