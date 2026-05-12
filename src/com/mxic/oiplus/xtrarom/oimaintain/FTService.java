/*****************************************************************
 *主題:FTTest相關程式
 *作者:Candy
 *日期:2005/10/27
 *****************************************************************/

package com.mxic.oiplus.xtrarom.oimaintain;

//import com.mxic.oiplus.rs.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.mxic.oiplus.oimaintain.OiMaintainService;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.oiplus.xtrarom.pdf.*;

public class FTService {

  public FTService() {
  }

  /*****************************************************************
   *主題:取得sid之基本資料
   *****************************************************************/
  public static FTTestActionForm getInfo(int sid) {
    Connection conn = null;

    try {
      String sql = "SELECT product_body,brand,version,status,product_type " +
          "FROM tf_information where sid='" + sid + "'  ";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      FTTestActionForm bean = null;

      while (rs.next()) {
        bean = new FTTestActionForm();
        bean.setSid(sid);
        bean.setPd_body(rs.getString("product_body"));
        bean.setBrand(rs.getString("brand"));
        bean.setVersion(rs.getString("version"));
        bean.setStatus(rs.getString("status"));
        break;
      }
      return bean;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  /*****************************************************************
   *主題:get第一版資料
   * ****注意：Device_Size尚未得解喔~~
   * ****注意：buy_off_mapping之release資料~~尚未得解喔~~
   * ****注意buy-off資料~~尚未得解喔~~
   *****************************************************************/

  public static FTTestActionForm[] getInitialInfo(int sid) {
    Connection conn = null;
    boolean flag = true;
    String product = "";
    String product_body = "";
    String brand = "";
    int version = 0;
    boolean check_tx_boolean = true;
    boolean check_first_boolean = true;

    try {
      String sql = null;
      product = getInfo1(sid);
      String product1[] = product.split(",");
      product_body = product1[0];
      brand = product1[1];
      version = Integer.parseInt(product1[2]);
      check_tx_boolean = check_tx(sid);
      if (check_tx_boolean) {
        //_tx中無資料
        check_first_boolean = check_first(product_body, brand, version - 1);
        if (check_first_boolean) {
          ArrayList tmp = new ArrayList();
          return (FTTestActionForm[]) tmp.toArray(new FTTestActionForm[0]);
        } else {
          //為第二版資料,取得sid version-1的資料,讀出資料存入tf_test_parameter_ft_tx,tag=0
          int version_two = version - 1;
          sql = "Insert into tf_test_parameter_ft_tx " +
              "(sid,tag,pgm_id,product_body,brand,version,test_type,backend_option," +
              "pin_count,package_type,body_size,tester,site,program_name,i_grade,c_grade," +
              "tf_comment,actual_file,pgm_special_control,one_main_pgm_group_version,hw_configure) " +
              "select " + sid + ",'0',NVL(pgm_id, 0),product_body,brand," + version +
              ",test_type,backend_option,pin_count,package_type,body_size,tester,site," +
              "program_name,i_grade,c_grade,tf_comment,actual_file,pgm_special_control,one_main_pgm_group_version,hw_configure " +
              "FROM tf_test_parameter_ft where product_body='" + product_body +
              "' and brand='" + brand + "' and version ='" + version_two + "'";
          conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql);
          ps.executeUpdate();
          DBConnection.close(conn);
        }
      }
      //tx中有資料
      sql =   "SELECT DISTINCT A.SID,\n" +
              "       GET_WSFT_OI_PGMID_VENDOR_LIST(A.SID, A.PGM_ID, A.BODY_SIZE, 'tag', 'TF_TEST_PARAMETER_FT_TX') TAG,\n" + 
              "       A.PGM_ID,\n" + 
              "       A.PRODUCT_BODY,\n" + 
              "       A.BRAND,\n" + 
              "       A.VERSION,\n" + 
              "       A.TEST_TYPE,\n" + 
              "       A.BACKEND_OPTION,\n" + 
              "       A.PIN_COUNT,\n" + 
              "       A.PACKAGE_TYPE,\n" + 
              "       A.TESTER,\n" + 
              "       GET_WSFT_OI_PGMID_VENDOR_LIST(A.SID, A.PGM_ID, A.BODY_SIZE, 'site', 'TF_TEST_PARAMETER_FT_TX') SITE,\n" + 
              "       A.PROGRAM_NAME,\n" + 
              "       NVL(A.I_GRADE," +
              //"			  DECODE((SELECT VERSION FROM PG_TEST_PROGRAM WHERE PROGRAM_MODE ='PROD'\n" + 
              //"                      AND PROGRAM_ID = A.PGM_ID),'A',\n" +
              "           DECODE((SELECT BB.PREVIOUS_PROGRAM_ID\n" +
              "                                  FROM PG_TEST_PROGRAM AA, PG_PREVIOUS_PROGRAM BB\n" + 
              "                                 WHERE AA.PROGRAM_MODE = BB.PROGRAM_MODE\n" + 
              "                                   AND AA.PROGRAM_ID = BB.PROGRAM_ID\n" + 
              "                                   AND AA.PROGRAM_ID = A.PGM_ID\n" + 
              "                                   AND BB.CATEGORY IN (1,2)),null,\n" +
              "                  (SELECT B.TEMP FROM GPRS_BA_PRODUCT A, GPRS_BA_PGM_TEMP B, PG_TEST_PROGRAM C\n" + 
              "                    WHERE A.PROCESS = B.PROCESS AND A.FUNCTION = B.FUNCTION\n" + 
              "                      AND A.TYPE = B.TYPE AND A.FAMILY_CODE = B.FAMILY_CODE\n" + 
              "                      AND C.PROGRAM_MODE='PROD'\n" + 
              "                      AND C.PROGRAM_ID = A.PGM_ID\n" + 
              "                      AND C.TEST_MODE = B.TEST_MODE\n" + 
              "                      AND A.PRODUCT_CODE = C.PRODUCT_CODE)," +
              "                  (SELECT DISTINCT I_GRADE FROM TF_TEST_PARAMETER_FT B, PG_PREVIOUS_PROGRAM C  WHERE B.PRODUCT_BODY = A.PRODUCT_BODY AND B.BRAND = A.BRAND AND B.VERSION = A.VERSION -1  AND A.PGM_ID = C.PROGRAM_ID AND C.CATEGORY IN (1,2) AND C.PREVIOUS_PROGRAM_ID = B.PGM_ID AND C.PROGRAM_MODE = 'PROD' AND C.PREVIOUS_PROGRAM_MODE = 'PROD'))) AS I_GRADE,\n" + 
              "       NVL(A.C_GRADE," +
              //"			  DECODE((SELECT VERSION FROM PG_TEST_PROGRAM WHERE PROGRAM_MODE ='PROD'\n" + 
              //"                      AND PROGRAM_ID = A.PGM_ID),'A',\n" +
              "           DECODE((SELECT BB.PREVIOUS_PROGRAM_ID\n" +
              "                                  FROM PG_TEST_PROGRAM AA, PG_PREVIOUS_PROGRAM BB\n" + 
              "                                 WHERE AA.PROGRAM_MODE = BB.PROGRAM_MODE\n" + 
              "                                   AND AA.PROGRAM_ID = BB.PROGRAM_ID\n" + 
              "                                   AND AA.PROGRAM_ID = A.PGM_ID\n" + 
              "                                   AND BB.CATEGORY IN (1,2)),null,\n" +
              "                  (SELECT B.TEMP FROM GPRS_BA_PRODUCT A, GPRS_BA_PGM_TEMP B, PG_TEST_PROGRAM C\n" + 
              "                    WHERE A.PROCESS = B.PROCESS AND A.FUNCTION = B.FUNCTION\n" + 
              "                      AND A.TYPE = B.TYPE AND A.FAMILY_CODE = B.FAMILY_CODE\n" + 
              "                      AND C.PROGRAM_MODE='PROD'\n" + 
              "                      AND C.PROGRAM_ID = A.PGM_ID\n" + 
              "                      AND C.TEST_MODE = B.TEST_MODE\n" + 
              "                      AND A.PRODUCT_CODE = C.PRODUCT_CODE)," +
              "                  (SELECT DISTINCT C_GRADE FROM TF_TEST_PARAMETER_FT B, PG_PREVIOUS_PROGRAM C  WHERE B.PRODUCT_BODY = A.PRODUCT_BODY AND B.BRAND = A.BRAND AND B.VERSION = A.VERSION -1  AND A.PGM_ID = C.PROGRAM_ID AND C.CATEGORY IN (1,2) AND C.PREVIOUS_PROGRAM_ID = B.PGM_ID AND C.PROGRAM_MODE = 'PROD' AND C.PREVIOUS_PROGRAM_MODE = 'PROD'))) AS C_GRADE,\n" + 
              "       NVL(A.W_GRADE,(SELECT DISTINCT W_GRADE FROM TF_TEST_PARAMETER_FT B, PG_PREVIOUS_PROGRAM C  WHERE B.PRODUCT_BODY = A.PRODUCT_BODY AND B.BRAND = A.BRAND AND B.VERSION = A.VERSION -1  AND A.PGM_ID = C.PROGRAM_ID AND C.CATEGORY IN (1,2) AND C.PREVIOUS_PROGRAM_ID = B.PGM_ID AND C.PROGRAM_MODE = 'PROD' AND C.PREVIOUS_PROGRAM_MODE = 'PROD')) AS W_GRADE,\n" + 
              "       NVL(A.Y_GRADE,(SELECT DISTINCT Y_GRADE FROM TF_TEST_PARAMETER_FT B, PG_PREVIOUS_PROGRAM C  WHERE B.PRODUCT_BODY = A.PRODUCT_BODY AND B.BRAND = A.BRAND AND B.VERSION = A.VERSION -1  AND A.PGM_ID = C.PROGRAM_ID AND C.CATEGORY IN (1,2) AND C.PREVIOUS_PROGRAM_ID = B.PGM_ID AND C.PROGRAM_MODE = 'PROD' AND C.PREVIOUS_PROGRAM_MODE = 'PROD')) AS Y_GRADE,\n" + 
              "       A.TF_COMMENT,\n" + 
              "       A.BODY_SIZE,\n" + 
              "       A.ACTUAL_FILE,\n" + 
              "       A.PGM_SPECIAL_CONTROL,\n" + 
              "       A.ONE_MAIN_PGM_GROUP_VERSION,\n" + 
              "       NVL(A.S_GRADE,(SELECT DISTINCT S_GRADE FROM TF_TEST_PARAMETER_FT B, PG_PREVIOUS_PROGRAM C  WHERE B.PRODUCT_BODY = A.PRODUCT_BODY AND B.BRAND = A.BRAND AND B.VERSION = A.VERSION -1  AND A.PGM_ID = C.PROGRAM_ID AND C.CATEGORY IN (1,2) AND C.PREVIOUS_PROGRAM_ID = B.PGM_ID AND C.PROGRAM_MODE = 'PROD' AND C.PREVIOUS_PROGRAM_MODE = 'PROD')) AS S_GRADE,\n" + 
              "       NVL(A.HW_CONFIGURE,(SELECT DISTINCT HW_CONFIGURE FROM TF_TEST_PARAMETER_FT B, PG_PREVIOUS_PROGRAM C  WHERE B.PRODUCT_BODY = A.PRODUCT_BODY AND B.BRAND = A.BRAND AND B.VERSION = A.VERSION -1  AND A.PGM_ID = C.PROGRAM_ID AND C.CATEGORY IN (1,2) AND C.PREVIOUS_PROGRAM_ID = B.PGM_ID AND C.PROGRAM_MODE = 'PROD' AND C.PREVIOUS_PROGRAM_MODE = 'PROD')) AS HW_CONFIGURE,\n" + 
              "       NVL(A.J_GRADE,(SELECT DISTINCT J_GRADE FROM TF_TEST_PARAMETER_FT B, PG_PREVIOUS_PROGRAM C  WHERE B.PRODUCT_BODY = A.PRODUCT_BODY AND B.BRAND = A.BRAND AND B.VERSION = A.VERSION -1  AND A.PGM_ID = C.PROGRAM_ID AND C.CATEGORY IN (1,2) AND C.PREVIOUS_PROGRAM_ID = B.PGM_ID AND C.PROGRAM_MODE = 'PROD' AND C.PREVIOUS_PROGRAM_MODE = 'PROD')) AS J_GRADE,\n" + 
              "       NVL(A.K_GRADE,(SELECT DISTINCT K_GRADE FROM TF_TEST_PARAMETER_FT B, PG_PREVIOUS_PROGRAM C  WHERE B.PRODUCT_BODY = A.PRODUCT_BODY AND B.BRAND = A.BRAND AND B.VERSION = A.VERSION -1  AND A.PGM_ID = C.PROGRAM_ID AND C.CATEGORY IN (1,2) AND C.PREVIOUS_PROGRAM_ID = B.PGM_ID AND C.PROGRAM_MODE = 'PROD' AND C.PREVIOUS_PROGRAM_MODE = 'PROD')) AS K_GRADE,\n" + 
              "       NVL(A.L_GRADE,(SELECT DISTINCT L_GRADE FROM TF_TEST_PARAMETER_FT B, PG_PREVIOUS_PROGRAM C  WHERE B.PRODUCT_BODY = A.PRODUCT_BODY AND B.BRAND = A.BRAND AND B.VERSION = A.VERSION -1  AND A.PGM_ID = C.PROGRAM_ID AND C.CATEGORY IN (1,2) AND C.PREVIOUS_PROGRAM_ID = B.PGM_ID AND C.PROGRAM_MODE = 'PROD' AND C.PREVIOUS_PROGRAM_MODE = 'PROD')) AS L_GRADE,\n" + 
              "       NVL(A.N_GRADE,(SELECT DISTINCT N_GRADE FROM TF_TEST_PARAMETER_FT B, PG_PREVIOUS_PROGRAM C  WHERE B.PRODUCT_BODY = A.PRODUCT_BODY AND B.BRAND = A.BRAND AND B.VERSION = A.VERSION -1  AND A.PGM_ID = C.PROGRAM_ID AND C.CATEGORY IN (1,2) AND C.PREVIOUS_PROGRAM_ID = B.PGM_ID AND C.PROGRAM_MODE = 'PROD' AND C.PREVIOUS_PROGRAM_MODE = 'PROD')) AS N_GRADE\n" + 
              "  FROM TF_TEST_PARAMETER_FT_TX A\n" + 
              " WHERE A.PRODUCT_BODY = '"+product_body+"'\n" + 
              "       AND A.BRAND = '"+brand+"'\n" + 
              "       AND A.VERSION = '"+version+"'\n" + 
              " ORDER BY TAG ";
      conn = null;
      conn = DBConnection.getConnection();
      TDSLogger.println(sql);
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      //String productType = OiMaintainService.getProductType(Integer.toString(sid));

      while (rs.next()) {
        FTTestActionForm bean = new FTTestActionForm();
        bean.setSid(sid);
        bean.setTag(rs.getString("tag"));
        bean.setPg_id(rs.getInt("PGM_ID"));
        bean.setBe_opt(rs.getString("backend_option"));
        bean.setTest_mode(rs.getString("TEST_TYPE"));
        bean.setPin_count(rs.getInt("PIN_COUNT"));
        bean.setPg_type(rs.getString("PACKAGE_TYPE"));
        //bean.setDevice_size("尚無資料");
        bean.setBody_size(rs.getString("body_size"));
        bean.setTester(rs.getString("tester"));
        bean.setSite(rs.getString("site"));
        bean.setPg_name(rs.getString("PROGRAM_NAME"));
        bean.setActual_file(rs.getString("actual_file"));
        bean.setPgm_special_control(rs.getString("pgm_special_control"));
        bean.setOne_main_pgm_group_version(rs.getString("one_main_pgm_group_version"));
        bean.setI_grade(rs.getString("i_grade"));
        bean.setC_grade(rs.getString("c_grade"));
        bean.setNotes(rs.getString("tf_comment"));
        bean.setHw_configure(rs.getString("hw_configure"));
/*
        if (productType.equals("ASM")) {
        	Vector temperatureList = null;
        	temperatureList = OiMaintainService.getTemperatureFromProductRoute(product_body, brand, Integer.toString(version), bean.getTest_mode());
        	if (bean.getI_grade() != null && (!bean.getI_grade().equals(" "))) {
        		if (!temperatureList.contains(bean.getI_grade()))
        			temperatureList.add(bean.getI_grade());
        	}
        	bean.setTemperatureList(temperatureList);
        }
*/
        tmp.add(bean);
      }
      //DBConnection.close(conn);
      return (FTTestActionForm[]) tmp.toArray(new FTTestActionForm[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
      if(conn != null)
    	  DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
      return null;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  /*****************************************************************
   *主題:取出product_body,brand,version
   *****************************************************************/
  public static String getInfo1(int sid) {
    Connection conn = null;
    boolean flag = true;
    String product = "";

    try {
      String sql = "SELECT product_body,brand,version " +
          " FROM tf_information where sid='" + sid + "'";
      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      while (rs.next()) {
        product = rs.getString("product_body") + "," +
            rs.getString("brand") + "," + rs.getString("version");
      }
      return product;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
      return null;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  /*****************************************************************
   *主題:檢查FTTest是否為第一版==>New,version已減過1
   *****************************************************************/
  public static boolean check_first(String product_body,
                                    String brand,
                                    int version) {

    Connection conn_check_first = null;
    boolean flag = true;

    try {
      String sql_check_first =
          "SELECT count(*) as total_count FROM tf_test_parameter_ft " +
          "where product_body='" + product_body +
          "' and brand='" + brand +
          "' and version='" + version + "'";

      conn_check_first = DBConnection.getConnection();
      PreparedStatement ps_check_first =
          conn_check_first.prepareStatement(sql_check_first.toString());
      ResultSet rs_leave_day = ps_check_first.executeQuery();

      while (rs_leave_day.next()) {
        if (rs_leave_day.getString("total_count").equals("0") == true) {
          //tf_test_parameter_ft中有資料表示不為第一版
          flag = true;
        } else {
          flag = false;
        }
      }
      return flag;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn_check_first);
      conn_check_first = null;
      return flag;
    }
  }

  public static boolean check_tx(int sid) {
    Connection conn_check_first = null;
    boolean flag = true;

    try {
      String sql_check_first =
          "SELECT count(*) as total_count FROM tf_test_parameter_ft_tx " +
          "where sid='" + sid + "'";
      conn_check_first = DBConnection.getConnection();
      PreparedStatement ps_check_first =
          conn_check_first.prepareStatement(sql_check_first.toString());
      ResultSet rs_leave_day = ps_check_first.executeQuery();

      while (rs_leave_day.next()) {
        if (rs_leave_day.getString("total_count").equals("0") == true) {
          //tf_test_parameter_ft_tx中已有資料,直接存取tf_test_parameter_ft_tx中的資料即可
          flag = true;
        } else {
          flag = false;
        }
      }
      return flag;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn_check_first);
      conn_check_first = null;
      return flag;
    }
  }

  /*****************************************************************
   *主題:取得FTTAdd之test_mode
   *****************************************************************/
  public static FTTestAddActionForm[] gettest_mode(String brand,
                                                   String pd_body,
                                                   String pgmflag) {
    Connection conn = null;
    boolean flag = true;
    String pgm_no = "";

    try {
      if(pgmflag == null || pgmflag.equals("buyoff"))
      	  pgm_no = "'53'";
      else if(pgmflag.equals("release"))
      	  pgm_no = "'54','64'";	
      String sql = "select distinct f.test_mode from " +
          "(SELECT distinct a.program_id,c.body_size as body_size, " +
          "c.backend_option as be_opt,a.test_mode,a.pin_count,a.program_name," +
          "e.plant_name,a.tester_type,a.package_type,a.version,a.actual_file,DECODE(A.DPAT_OPTION,'Y','DPAT_Option=Y;','')||DECODE(A.QT_MERGE_OPTION,'Y','QT_MERGE_OPTION=Y;','')||DECODE(A.FT_ITEM_SAMPLE_OPTION,'Y','FT_ITEM_SAMPLE_OPTION=Y;','')||DECODE(A.SSS_OPTION,'Y','Load sss file=Y;','') pgm_special_control, a.one_main_pgm_group_version " +
          "from pg_test_program a, ba_package_type b, tf_prod_epn c,pg_plant_release d, " +
          "ba_plant e " +
          "where a.product_code like '"+ pd_body +"%' and program_mode = 'PROD' " +
          "and a.test_mode like 'F%' " +
          "and a.package_type = b.package_type and b.prm2_code = c.package_type " +
          "and substr(a.product_code,1,4) = c.product_body " +
          "and substr(a.product_code,5,1) = c.backend_option " +
          "and a.program_status in ("+pgm_no+") and a.sid = d.pg_sid " +
          "and d.plant_no = e.plant_no ) f ";
      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      TDSLogger.println(sql);
      while (rs.next()) {
        FTTestAddActionForm bean = new FTTestAddActionForm();
        bean.setTest_mode_str(rs.getString("test_mode"));
        tmp.add(bean);
      }
      if (tmp.isEmpty()) {
        return (FTTestAddActionForm[])null;
      } else {
        return (FTTestAddActionForm[]) tmp.toArray(new FTTestAddActionForm[0]);
      }
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
      return null;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
  }


  public static FTTestAddActionForm[] gettest_modeInfo(String[] test_mode,
                                                       String brand,
                                                       String pd_body,
                                                       int sid,
                                                       String version,
                                                       String pgmflag) {
    Connection conn = null;
    boolean flag = true;
    String pgm_no = "";
    try {
      if(pgmflag == null || pgmflag.equals("buyoff"))
        	  pgm_no = "'53'";
      else if(pgmflag.equals("release"))
        	  pgm_no = "'54','64'";	
      StringBuffer sql = new StringBuffer();
      String test_mode_str = "";
      for (int j = 0; j < test_mode.length; j++) {
        if (j == 0) {
          test_mode_str = "f.test_mode='" + test_mode[j] + "'";
        } else {
          test_mode_str = test_mode_str + " or f.test_mode='" + test_mode[j] + "'";
        }
      }
      conn = DBConnection.getConnection();
      
      StringBuffer sql_pta = new StringBuffer();
      sql_pta.append(
			  "        SELECT DISTINCT E.APP_ID APP_ID,\n" + 
			  "                        E.SEQ_NO SEQ_NO,\n" + 
			  "                        PRODUCT_CODE,\n" + 
			  "                        PACKAGE_TYPE,\n" + 
			  "                        PIN_COUNT,\n" + 
			  "                        PLANT_NAME,\n" + 
			  "                        WIRE_VERSION,\n" + 
			  "                        TESTER_TYPE,\n" + 
			  "                        BODYSIZE,\n" + 
			  "                        E1.STATUS\n" + 
			  "          FROM TIM.PTA_ACCESSORY_APP_DETAIL E, TIM.PTA_ACCESSORY_NEW_APP E1, ba_plant b\n" + 
			  "         WHERE E.APP_ID = E1.APP_ID  and e.plant_no = b.plant_no AND E1.SEQ_NO = E.SEQ_NO \n" + 
			  " AND  PRODUCT_CODE like '" + pd_body + "%'\n");
      
		PreparedStatement ps_pta = conn.prepareStatement(sql_pta.toString());
		ResultSet rs_pta = ps_pta.executeQuery();
		HashMap hh_pta = new HashMap();
		String k = "", v = "",v1 = "";
		while (rs_pta.next()) {
			k = (rs_pta.getString("PRODUCT_CODE") + '~' + rs_pta.getString("PACKAGE_TYPE") + '~' + rs_pta.getString("PIN_COUNT") + '~' + rs_pta.getString("PLANT_NAME") + '~' + rs_pta.getString("WIRE_VERSION") + '~' + rs_pta.getString("TESTER_TYPE") + '~' + rs_pta.getString("BODYSIZE"));
			v = (rs_pta.getString("APP_ID") + '-' + rs_pta.getString("SEQ_NO")+ "("+rs_pta.getString("STATUS")+")");
			if(hh_pta.containsKey(k)){
				v1="~"+hh_pta.get(k)+"";
			}else {
				v1="";
			}
			hh_pta.put(k, (v+v1));
		}
		
      sql.append("SELECT f.* FROM (SELECT distinct a.program_id,c.body_size," +
                 "c.backend_option as be_opt,a.test_mode,a.pin_count,a.program_name," +
                 "e.plant_name,a.tester_type,a.package_type,a.version,a.actual_file,DECODE(A.DPAT_OPTION,'Y','DPAT_Option=Y;','')||DECODE(A.QT_MERGE_OPTION,'Y','QT_MERGE_OPTION=Y;','')||DECODE(A.FT_ITEM_SAMPLE_OPTION,'Y','FT_ITEM_SAMPLE_OPTION=Y;','')||DECODE(A.SSS_OPTION,'Y','Load sss file=Y;','') pgm_special_control, a.one_main_pgm_group_version, a.os_version " +
                 ",GET_8049_ONE_MAIN_PGM_NUM(a.one_main_pgm_group_version, '" + pgmflag + "') one_main_pgm_num " +
                 "from pg_test_program a, ba_package_type b, tf_prod_epn c," +
                 "pg_plant_release d, ba_plant e " +
                 "where a.product_code like '" + pd_body + "%' " +
                 "and program_mode = 'PROD' and a.package_type = b.package_type " +
                 "and b.prm2_code = c.package_type " +
                 "and substr(a.product_code,1,4) = c.product_body " +
                 "and substr(a.product_code,5,1) = c.backend_option " +
                 "and a.program_status in ("+pgm_no+") and a.sid = d.pg_sid " +
                 "and d.plant_no = e.plant_no) f where " + test_mode_str);

      
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      TDSLogger.println(sql);
      while (rs.next()) {
        StringBuffer sql_check = new StringBuffer();
        sql_check.append(
            "SELECT count(*) as total_count FROM tf_test_parameter_ft_tx where sid='" +
            sid + "' and pgm_id='" + rs.getInt("PROGRAM_ID") +
            "' and product_body='" + pd_body +
            "' and brand='" + brand +
            "' and version='" + version +
            "' and test_type='" + rs.getString("test_mode") +
            "' and backend_option='" + rs.getString("be_opt") +
            "' and pin_count='" + rs.getString("pin_count") +
            "' and package_type='" + rs.getString("package_type") +
            "' and body_size='" + rs.getString("body_size") +
            "' and tester='" + rs.getString("tester_type") +
            "' and site='" + rs.getString("plant_name") +
            "' and program_name='" + rs.getString("program_name") +
            "' and actual_file='" + rs.getString("actual_file") + "' ");

        PreparedStatement ps_check = conn.prepareStatement(sql_check.toString());
        ResultSet rs_check = ps_check.executeQuery();

        while (rs_check.next()) {
          if (rs_check.getInt("total_count") == 0) {
            FTTestAddActionForm bean = new FTTestAddActionForm();
            bean.setPg_id(rs.getInt("PROGRAM_ID"));
            bean.setBe_opt(rs.getString("be_opt"));
            bean.setTest_mode_str(rs.getString("TEST_MODE"));
            bean.setPin_count(rs.getInt("PIN_COUNT"));
            bean.setPg_type(rs.getString("PACKAGE_TYPE"));
            bean.setBody_size(rs.getString("body_size"));
            bean.setTester(rs.getString("TESTER_TYPE"));
            bean.setSite(rs.getString("plant_name"));
            bean.setPg_name(rs.getString("PROGRAM_NAME"));
            bean.setActual_file(rs.getString("actual_file"));
            bean.setPgm_special_control(rs.getString("pgm_special_control"));
            bean.setOne_main_pgm_group_version(rs.getString("one_main_pgm_group_version"));
            bean.setOne_main_pgm_num(rs.getString("one_main_pgm_num"));
            bean.setOs_version("");
            if("PK2".equals(bean.getTester()))
            	bean.setOs_version("PK2 OS Version:"+rs.getString("os_version"));            	
            
			k = pd_body + bean.getBe_opt() + "~" + 
					bean.getPg_type() + "~" + 
					bean.getPin_count() + "~" + 
					bean.getSite() + "~" + 
					bean.getPg_name().substring(bean.getPg_name().length() - 1) + "~" + 
					bean.getTester() + "~" + 
					bean.getBody_size();
					
			if (hh_pta.get(k) == null || hh_pta.get(k).equals("")){
				bean.setPta_status("No PTA Info.");
			} else{
				bean.setPta_status(hh_pta.get(k)+"");
			}
					
            tmp.add(bean);
          } else {
          }
        }
      }
      return (FTTestAddActionForm[]) tmp.toArray(new FTTestAddActionForm[0]);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
      return null;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  /********************************************************************
   *主題:get Add資料
   *********************************************************************/

  public static boolean insertadd(int sid, String pd_body,
                                  String brand, String version,
                                  String[] record_id) {

    int pg_id = 0;
    String test_type = "";
    String be_opt = "";
    int pin_count = 0;
    String pg_type = "";
    String body_size = "";
    String tester = "";
    String site = "";
    String pg_name = "";
    String actual_file = "";
    String pgm_special_control = "";
    String one_main_pgm_group_version = "";
    String os_version = "";
    String i_grade = null;
    String c_grade = null;
    String hw_configure = null;
    String comment = null;
    Connection conn = null;
    Connection conn_exit = null;
    boolean flag = true;
    String InsertSQL = null;
    try {
     conn = DBConnection.getConnection();
     conn_exit = DBConnection.getConnection();
     InsertSQL = "Insert into tf_test_parameter_ft_tx " +
         "(sid,tag,pgm_id,product_body,brand,version,test_type,backend_option," +
         "pin_count,package_type,body_size,tester,site,program_name,actual_file,pgm_special_control,one_main_pgm_group_version,i_grade,c_grade,hw_configure,tf_comment) " +
         "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
     PreparedStatement ps_insert = conn_exit.prepareStatement(InsertSQL);

     for (int i = 0; i < record_id.length; i++) {
        String record_id_str[] = record_id[i].split(",",-1);
        pg_id = Integer.parseInt(record_id_str[0]);
        test_type = record_id_str[1];
        be_opt = record_id_str[2];
        pin_count = Integer.parseInt(record_id_str[3]);
        pg_type = record_id_str[4];
        body_size = record_id_str[5];
        tester = record_id_str[6];
        site = record_id_str[7];
        pg_name = record_id_str[8];
        actual_file = record_id_str[9];
        pgm_special_control = record_id_str[10];
        one_main_pgm_group_version = record_id_str[11];
        if(record_id_str.length>12)
        os_version = record_id_str[12];

        StringBuffer sql = new StringBuffer();
        sql.append(
            "SELECT count(*) as total_count FROM tf_test_parameter_ft_tx where sid='" + sid +
            "' and pgm_id='" + pg_id +
            "' and product_body='" + pd_body +
            "' and brand='" + brand +
            "' and version='" + version +
            "' and test_type='" + test_type +
            "' and backend_option='" + be_opt +
            "' and pin_count='" + pin_count +
            "' and package_type='" + pg_type +
            "' and body_size='" + body_size +
            "' and  tester='" + tester +
            "' and site='" + site +
            "' and actual_file ='" + actual_file +
            "' and program_name='" + pg_name + "' ");

        PreparedStatement ps = conn.prepareStatement(sql.toString());
        ResultSet rs = ps.executeQuery();
        ArrayList tmp = new ArrayList();

        while (rs.next()) {
          if (rs.getInt("total_count") == 0) {
        	  StringBuffer sql1 = new StringBuffer();
        	  sql1.append(
                  "SELECT i_grade, c_grade, hw_configure, tf_comment FROM tf_test_parameter_ft_tx where sid='" + sid +
                  "' and pgm_id='" + pg_id +
                  "' and product_body='" + pd_body +
                  "' and brand='" + brand +
                  "' and version='" + version +
                  "' and test_type='" + test_type +
                  "' and backend_option='" + be_opt +
                  "' and pin_count='" + pin_count +
                  "' and package_type='" + pg_type +
                  "' and body_size='" + body_size +
                  "' and tester='" + tester +
                  "' and actual_file ='" + actual_file +
                  "' and program_name='" + pg_name + "' ");

              PreparedStatement ps1 = conn.prepareStatement(sql1.toString());
              ResultSet rs1 = ps1.executeQuery();

              while (rs1.next()) {
            	  i_grade = rs1.getString("i_grade");
            	  c_grade = rs1.getString("c_grade");
            	  hw_configure = rs1.getString("hw_configure");
            	  comment = rs1.getString("tf_comment");
              }
        	String previous_program_id = OiMaintainService.GetProviousProgramId(String.valueOf(pg_id));
        	String previous_hw_configure = OiMaintainService.GetHWConfigureBySidProgramId("tf_test_parameter_ft_tx",String.valueOf(sid),previous_program_id);  
            ps_insert.setInt(1, sid);
            ps_insert.setString(2, "1");
            ps_insert.setInt(3, pg_id);
            ps_insert.setString(4, pd_body);
            ps_insert.setString(5, brand);
            ps_insert.setInt(6, Integer.parseInt(version));
            if (test_type == null || test_type.equals("")) {
              ps_insert.setString(7, " ");
            } else {
              ps_insert.setString(7, test_type);
            }
            if (be_opt == null || be_opt.equals("")) {
              ps_insert.setString(8, " ");
            } else {
              ps_insert.setString(8, be_opt);
            }
            if (String.valueOf(pin_count) == null || String.valueOf(pin_count).equals("")) {
              ps_insert.setInt(9, 0);
            } else {
              ps_insert.setInt(9, pin_count);
            }
            if (pg_type == null || pg_type.equals("")) {
              ps_insert.setString(10, " ");
            } else {
              ps_insert.setString(10, pg_type);
            }
            if (body_size == null || body_size.equals("")) {
              ps_insert.setString(11, " ");
            } else {
              ps_insert.setString(11, body_size);
            }
            if (tester == null || tester.equals("")) {
              ps_insert.setString(12, " ");
            } else {
              ps_insert.setString(12, tester);
            }
            if (site == null || site.equals("")) {
              ps_insert.setString(13, " ");
            } else {
              ps_insert.setString(13, site);
            }
            if (pg_name == null || pg_name.equals("")) {
              ps_insert.setString(14, " ");
            } else {
              ps_insert.setString(14, pg_name);
            }
            if (actual_file == null || actual_file.equals("")) {
              ps_insert.setString(15, " ");
            } else {
              ps_insert.setString(15, actual_file);
            }
            if (pgm_special_control == null || pgm_special_control.equals("")) {
                ps_insert.setString(16, " ");
            } else {
                ps_insert.setString(16, pgm_special_control);
            }
            if (one_main_pgm_group_version == null || one_main_pgm_group_version.equals("")) {
                ps_insert.setString(17, " ");
            } else {
                ps_insert.setString(17, one_main_pgm_group_version);
            }
            ps_insert.setString(18, i_grade);
            ps_insert.setString(19, c_grade);
            if(hw_configure == null){
            	if (previous_hw_configure == null || previous_hw_configure.equals("")) {
            		ps_insert.setString(20, "");
            	} else {
            		ps_insert.setString(20, previous_hw_configure);
            	}
            }else{
            	ps_insert.setString(20, hw_configure);
            }	
            if(comment == null){
	            if (os_version == null || os_version.equals("")) {
	                ps_insert.setString(21, "");
	              } else {
	                ps_insert.setString(21, os_version);
	              }
            }else{
            	ps_insert.setString(21, comment);
            }
  
            TDSLogger.println(InsertSQL.toString());
            ps_insert.executeUpdate();
            //DBConnection.commit(conn_exit);
          } else {
            //已存在相同資料,回到上一頁
          }
          //DBConnection.commit(conn);
        }
        rs.close();
        ps.close();
        rs = null;
        ps = null;
      }
      ps_insert.close();
      ps_insert = null;
      return flag;
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn);
      DBConnection.close(conn_exit);
      conn_exit = null;
      conn = null;
      return flag;
    }
  }


  //select d.* from  (SELECT right(left(a.product_code,5),1)as be_opt,a.*,b.plant_no,c.plant_name,e.status as program_status FROM pg_test_program a,pg_plant_release b,ba_plant c,buy_off_mapping e where (a.program_id=b.pg_sid and b.plant_no=c.plant_no and a.program_status=e.program_status and e.status='release')) d where d.program_id='2' and program_mode='ENG' and be_opt='6' and test_mode='aa' and version='99' and program_name='aa' and tester_type='bb' and package_type='dd' and pin_count='12' and plant_name<>'kkk'
  //select distinct d.plant_no,d.plant_name,d.program_status from  (SELECT right(left(a.product_code,5),1)as be_opt,a.*,b.plant_no,c.plant_name,e.status as program_status FROM pg_test_program a,pg_plant_release b,ba_plant c,buy_off_mapping e where (a.program_id=b.pg_sid and b.plant_no=c.plant_no and a.program_status=e.program_status and e.status='release')) d where d.plant_name<>'TTT'
  /*****************************************************************
   *主題:取得FTTAdd之test_mode
   *****************************************************************/
  public static FTTestActionForm[] getvendor(String product_body, String record_id) {
    Connection conn = null;
    boolean flag = true;
    String be_opt = "";
    String test_mode = "";
    int pin_count = 0;
    String pg_type = "";
    String tester = "";
    String pg_name = "";
    String actual_file = "";
    String pgm_special_control = "";
    String one_main_pgm_group_version = "";
    String body_size = "";
    String i_grade = "";
    String c_grade = "";
    String site = "";
    String hw_configure = "";
    String comment = "";

    try {
      String record_id_str[] = record_id.split(",");
      be_opt = record_id_str[0];
      test_mode = record_id_str[1];
      pin_count = Integer.parseInt(record_id_str[2]);
      pg_type = record_id_str[3];
      tester = record_id_str[4];
      pg_name = record_id_str[5];
      site = record_id_str[6];
      body_size = record_id_str[7];
      actual_file = record_id_str[8];
      pgm_special_control = record_id_str[9];
      one_main_pgm_group_version = record_id_str[10];
      if (record_id_str.length > 12)
    	  i_grade = record_id_str[12];
      if (record_id_str.length > 13)
    	  c_grade = record_id_str[13];
      if (record_id_str.length > 14)
	      hw_configure = record_id_str[14];
      if (record_id_str.length > 15)
    	  comment = record_id_str[15];
      StringBuffer sql = new StringBuffer();

      sql.append(
          "select distinct b.plant_no,c.plant_name,a.program_status " +
          "FROM pg_test_program a,pg_plant_release b,ba_plant c " +
          "where a.sid=b.pg_sid and b.plant_no=c.plant_no " +
          "and (a.program_status='54' or a.program_status='64') " +
          "and program_mode='PROD' and a.product_code = '" + product_body + be_opt +
          "' and test_mode='" + test_mode +
          "' and program_name='" + pg_name +
          "' and actual_file='" + actual_file +
          "' and package_type='" + pg_type +
          "' and pin_count='" + pin_count +
          "' and tester_type='" + tester +
          "' and plant_name not in ('" + site.replaceAll(";", "','") + "')");

      // where  be_opt='6' and test_mode='aa' and program_name='aa' and tester_type='bb' and package_type='dd' and pin_count='12' and plant_name<>'kkkk'
      TDSLogger.println(sql.toString());
      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      while (rs.next()) {
        FTTestActionForm bean = new FTTestActionForm();
        bean.setSite_str(rs.getString("plant_name"));
        TDSLogger.println(rs.getString("plant_name"));
        bean.setPg_type(pg_type);
        bean.setBe_opt(be_opt);
        bean.setTest_mode(test_mode);
        bean.setPin_count(pin_count);
        bean.setTester(tester);
        bean.setPg_name(pg_name);
        bean.setBody_size(body_size);
        bean.setI_grade(i_grade);
        bean.setC_grade(c_grade);
        bean.setActual_file(actual_file);
        bean.setPgm_special_control(pgm_special_control);
        bean.setOne_main_pgm_group_version(one_main_pgm_group_version);
        bean.setHw_configure(hw_configure);
        bean.setComment(comment);
        tmp.add(bean);
      }
      if (tmp.isEmpty()) {
        return null;
      } else {
        return (FTTestActionForm[]) tmp.toArray(new FTTestActionForm[0]);
      }
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex);
//      TDSLogger.println(ex.getMessage());
      flag = false;
      // return null;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  /*****************************************************************
  *主題:取得 OI Name List for all vendors
  *****************************************************************/
 public static FTTestActionForm[] getVendorOIList(int sid) {
   Connection conn = null;

   try {
     String sql = "select distinct site, product_body, brand, version, lower(b.tim_short_name) sname from tf_test_parameter_ws_tx a, ba_plant b " +
               "\n where a.site = b.plant_name and sid = " + sid +
               "\n union select distinct site, product_body, brand, version, lower(b.tim_short_name) sname from tf_test_parameter_ft_tx a, ba_plant b " +
               "\n where a.site = b.plant_name and sid = " + sid +
               "\n union select distinct site, product_body, brand, version, lower(b.tim_short_name) sname from tf_test_parameter_pbc_tx a, ba_plant b " +
               "\n where a.site = b.plant_name and sid = " + sid;

     conn = DBConnection.getConnection();
     PreparedStatement ps = conn.prepareStatement(sql);
     ResultSet rs = ps.executeQuery();
     ArrayList tmp = new ArrayList();
     FTTestActionForm bean = null;
     while (rs.next()) {
       bean = new FTTestActionForm();
       bean.setPlant_name(rs.getString("SITE"));
       bean.setActual_file(pdfService.getFileName(rs.getString("PRODUCT_BODY"),
                                      rs.getString("BRAND"),
                                      rs.getString("VERSION"),
                                      rs.getString("SITE"),
                                      false,
                                      "_tx"));
       tmp.add(bean);
     }
     return (FTTestActionForm[]) tmp.toArray(new FTTestActionForm[0]);
   } catch (Exception ex) {
     ex.fillInStackTrace();
     TDSLogger.println(ex.getMessage());
     return null;
   } finally {
     DBConnection.close(conn);
     conn = null;
   }
 }

public static FTTestActionForm[] getVendorList(int sid) {
    Connection conn = null;

    try {
      String sql = "select distinct site, product_body, brand, version, lower(b.tim_short_name) sname from tf_test_parameter_ws_tx a, ba_plant b " +
                "\n where a.site = b.plant_name and sid = " + sid +
                "\n union select distinct site, product_body, brand, version, lower(b.tim_short_name) sname from tf_test_parameter_ft_tx a, ba_plant b " +
                "\n where a.site = b.plant_name and sid = " + sid +
                "\n union select distinct site, product_body, brand, version, lower(b.tim_short_name) sname from tf_test_parameter_pbc_tx a, ba_plant b " +
                "\n where a.site = b.plant_name and sid = " + sid;

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      FTTestActionForm bean = null;
      while (rs.next()) {
        if (!rs.getString("site").equals("TEST2")) {
          bean = new FTTestActionForm();
          bean.setSid(sid);
          bean.setPd_body(rs.getString("product_body"));
          bean.setBrand(rs.getString("brand"));
          bean.setVersion(rs.getString("version"));
          bean.setPlant_name(rs.getString("site"));
          bean.setComment(rs.getString("sname")); // set short TIM name in Comment fieid
          tmp.add(bean);
        }
      }
      return (FTTestActionForm[]) tmp.toArray(new FTTestActionForm[0]);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  public static FTTestVendorActionForm[] getvendor_Info(
      String[] site_data,
      String be_opt,
      String test_mode,
      int pin_count,
      String pg_type,
      String tester,
      String pg_name,
      String actual_file,
      String pgm_special_control,
      String one_main_pgm_group_version,
      int sid,
      String pd_body,
      String brand,
      String version,
      String body_size,
      String i_grade,
      String c_grade,
      String hw_configure,
      String comment) {

    String InsertSQL = null;
    Connection conn = null;
    Connection conn1 = null;
    Connection conn_check = null;
    boolean flag = true;

    try {
      StringBuffer sql = new StringBuffer();
      String site_data_str = "";
      for (int j = 0; j < site_data.length; j++) {
        if (j == 0) {
          site_data_str = "d.plant_name='" + site_data[j] + "'";
        } else {
          site_data_str = site_data_str + " or d.plant_name='" + site_data[j] + "'";
        }
      }
      TDSLogger.println(site_data_str);
      sql.append("select * from (SELECT distinct substr(a.product_code,5,1)as be_opt," +
                 "a.*,b.plant_no,c.plant_name " +
                 "FROM pg_test_program a,pg_plant_release b,ba_plant c " +
                 "where (a.sid=b.pg_sid and b.plant_no=c.plant_no " +
                 "and (a.program_status='54' or a.program_status='64') " +
                 "and program_mode='PROD')) d " +
                 "where be_opt='" + be_opt +
                 "' and test_mode='" + test_mode +
                 "' and program_name='" + pg_name +
                 "' and actual_file='" + actual_file +
                 "' and test_mode='" + test_mode +
                 "' and package_type='" + pg_type +
                 "' and pin_count='" + pin_count +
                 "' and  (" + site_data_str + ")");

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      conn_check = DBConnection.getConnection();
      conn1 = DBConnection.getConnection();
      //StringBuffer InsertSQL = new StringBuffer();
      InsertSQL = "Insert into tf_test_parameter_ft_tx " +
          "(sid,tag,pgm_id,product_body,brand,version,test_type,backend_option," +
          "pin_count,package_type,body_size,tester,site,program_name,i_grade," +
          "c_grade,tf_comment,actual_file,pgm_special_control,one_main_pgm_group_version,hw_configure) " +
          "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
      PreparedStatement ps_insert = conn1.prepareStatement(InsertSQL);

      while (rs.next()) {
        StringBuffer sql_check = new StringBuffer();
        sql_check.append(
            "SELECT count(*) as total_count FROM tf_test_parameter_ft_tx " +
            "where sid='" + sid +
            "' and pgm_id='" + rs.getInt("PROGRAM_ID") +
            "' and product_body='" + pd_body +
            "' and brand='" + brand +
            "' and version='" + version +
            "' and test_type='" + test_mode +
            "' and backend_option='" + be_opt +
            "' and pin_count='" + pin_count +
            "' and package_type='" + pg_type +
            "' and body_size='" + body_size +
            "' and tester='" + tester +
            "' and actual_file='" + actual_file +
            "' and site='" + rs.getString("plant_name") +
            "' and program_name='" + pg_name + "' ");

        PreparedStatement ps_check = conn_check.prepareStatement(sql_check.toString());
        ResultSet rs_check = ps_check.executeQuery();

        while (rs_check.next()) {
          if (rs_check.getInt("total_count") == 0) {
        	  
            //String previous_program_id = OiMaintainService.GetProviousProgramId(String.valueOf(rs.getInt("PROGRAM_ID")));
        	//String previous_hw_configure = "NA"; 
        	ps_insert.setInt(1, sid);
            ps_insert.setString(2, "1");
            if (rs.getString("PROGRAM_ID") == null || rs.getString("PROGRAM_ID").equals("")) {
              ps_insert.setInt(3, 0);
            } else {
              ps_insert.setInt(3, rs.getInt("PROGRAM_ID"));
              //previous_hw_configure = OiMaintainService.GetHWConfigureBySidProgramId("tf_test_parameter_ft_tx",String.valueOf(sid),rs.getString("PROGRAM_ID"));
            }
            ps_insert.setString(4, pd_body);
            ps_insert.setString(5, brand);
            ps_insert.setInt(6, Integer.parseInt(version));
            if (test_mode == null || test_mode.equals("")) {
              ps_insert.setString(7, " ");
            } else {
              ps_insert.setString(7, test_mode);
            }
            if (be_opt == null || be_opt.equals("")) {
              ps_insert.setString(8, " ");
            } else {
              ps_insert.setString(8, be_opt);
            }
            if (String.valueOf(pin_count) == null || String.valueOf(pin_count).equals("")) {
              ps_insert.setInt(9, 0);
            } else {
              ps_insert.setInt(9, pin_count);
            }
            if (pg_type == null || pg_type.equals("")) {
              ps_insert.setString(10," ");
            } else {
              ps_insert.setString(10, pg_type);
            }
            if (body_size == null || body_size.equals("")) {
              ps_insert.setString(11, " ");
            } else {
              ps_insert.setString(11, body_size);
            }
            if (tester == null || tester.equals("")) {
              ps_insert.setString(12, " ");
            } else {
              ps_insert.setString(12, tester);
            }
            if (rs.getString("plant_name") == null || rs.getString("plant_name").equals("")) {
              ps_insert.setString(13, " ");
            } else {
              ps_insert.setString(13, rs.getString("plant_name"));
            }
            if (pg_name == null || pg_name.equals("")) {
              ps_insert.setString(14, " ");
            } else {
              ps_insert.setString(14, pg_name);
            }
            ps_insert.setString(15, i_grade);
            ps_insert.setString(16, c_grade);
            ps_insert.setString(17, comment);
            if (actual_file == null || actual_file.equals("")) {
              ps_insert.setString(18, " ");
            } else {
              ps_insert.setString(18, actual_file);
            }
            if (pgm_special_control == null || pgm_special_control.equals("")) {
                ps_insert.setString(19, " ");
            } else {
                ps_insert.setString(19, pgm_special_control);
            }
            if (one_main_pgm_group_version == null || one_main_pgm_group_version.equals("")) {
                ps_insert.setString(20, " ");
            } else {
                ps_insert.setString(20, one_main_pgm_group_version);
            }
            if (hw_configure == null || hw_configure.equals("")) {
                ps_insert.setString(21, " ");
              } else {
                ps_insert.setString(21, hw_configure);
              }
            ps_insert.executeUpdate();
          }
        }
      }
      return (FTTestVendorActionForm[]) tmp.toArray(new FTTestVendorActionForm[0]);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
      return null;
    } finally {
      DBConnection.close(conn);
      DBConnection.close(conn1);
      DBConnection.close(conn_check);
      conn = null;
      conn1 = null;
      conn_check = null;
    }
  }

  /********************************************************************
   *主題:get Add資料
   *********************************************************************/
  public static boolean update_data(String[] sid,
                                    String[] pd_body,
                                    String[] brand,
                                    String[] version,
                                    String[] test_type,
                                    String[] be_opt,
                                    String[] pin_count,
                                    String[] pg_type,
                                    String[] body_size,
                                    String[] tester,
                                    String[] site,
                                    String[] pg_name,
                                    String[] actual_file,
                                    String[] pgm_special_control,
                                    String[] one_main_pgm_group_version,
                                    String[] pg_id,
                                    String[] i_grade,
                                    String[] c_grade,
                                    String[] comment,
                                    String[] hw_configure,
                                    String flag) {
      StringBuffer InsSQL = new StringBuffer();
      StringBuffer DelSQL = new StringBuffer();
      StringBuffer InsSQL1 = null;
      Connection conn = null;
      int result;

      try {
    	  conn = DBConnection.getConnection();
    	  conn.setAutoCommit(false);
    	  InsSQL.append("update tf_test_parameter_ft_tx set i_grade=?, c_grade=?, tf_comment=?, hw_configure=? ");
    	  InsSQL.append("where sid = ? and product_body=? and brand=? and version=? and test_type=? ");
    	  InsSQL.append("and backend_option=? and pin_count=? and package_type=? and tester=? ");
    	  InsSQL.append("and pgm_id=? and program_name=? ");
    	  PreparedStatement ps2 = null;
    	  int idx = 0;
    	  if (pin_count != null) {
    		  for (int i = 0; i < pin_count.length; i++) {
    			  DelSQL.delete(0, DelSQL.length());
			      DelSQL.append("delete from tf_test_parameter_ft_tx ");
			      DelSQL.append("where sid = ? ");
			      DelSQL.append("and pgm_id = ? ");
			      DelSQL.append("and body_size = ? ");
			      DelSQL.append("and site not in ('" + site[i].trim().replaceAll(";", "','")+"')");
			      PreparedStatement ps0 = conn.prepareStatement(DelSQL.toString());
			      TDSLogger.println(DelSQL.toString());
			      TDSLogger.println(sid[i]+";"+pg_id[i].trim());
			      ps0.setInt(1,Integer.parseInt(sid[i]));
			      ps0.setString(2,pg_id[i]);
			      ps0.setString(3,body_size[i]);
			      //ps0.setString(3,StringUtil.Utf8ToBig5(site[i].trim()));
			      ps0.executeUpdate();
			      
    			  idx = 16;
        		  InsSQL1 = new StringBuffer(InsSQL.toString());
            	  if ((body_size[i] != null) && (body_size[i].length() > 0))
            		  InsSQL1.append("and body_size=? ");
            	  else
            		  InsSQL1.append("and body_size is null ");
            	  if ((actual_file[i] != null) && (actual_file[i].length() > 0))
            		  InsSQL1.append("and actual_file=? ");
            	  else
            		  InsSQL1.append("and actual_file is null ");
            	  ps2 = conn.prepareStatement(InsSQL1.toString());

    			  ps2.setString(1, c_grade[i]);
    			  ps2.setString(2, c_grade[i]);
    			  ps2.setString(3, comment[i].trim());
    			  ps2.setString(4, hw_configure[i].trim());
    			  ps2.setInt(5, Integer.parseInt(sid[i]));
    			  ps2.setString(6, pd_body[i]);
    			  ps2.setString(7, brand[i]);
    			  ps2.setString(8, version[i]);
    			  ps2.setString(9, test_type[i]);
    			  ps2.setString(10, be_opt[i]);
    			  ps2.setString(11, pin_count[i]);
    			  ps2.setString(12, pg_type[i]);
    			  ps2.setString(13, tester[i]);
    			  //ps2.setString(14, site[i]);
    			  ps2.setString(14, pg_id[i]);
    			  ps2.setString(15, pg_name[i]);
    			  if ((body_size[i] != null) && (body_size[i].length() > 0)) {
    				  ps2.setString(idx, body_size[i]);
    				  idx++;
    			  }
    			  if ((actual_file[i] != null) && (actual_file[i].length() > 0)) {
    				  ps2.setString(idx, actual_file[i]);
    			  }

    			  result = ps2.executeUpdate();
    		  }
    	  }
    	  if (flag.equals("submit_cmd")) {
    		  InsSQL1 = new StringBuffer("update tf_information set TF_TEST_PARAMETER_FT = ? ,TF_BASIC_INFORMATION = ? where sid = ? ");
    		  PreparedStatement ps3 = conn.prepareStatement(InsSQL1.toString());
    		  ps3.setString(1, "Y");
    		  ps3.setString(2, "N");
    		  ps3.setInt(3, Integer.parseInt(sid[0]));
    		  ps3.executeUpdate();
    	  }
    	  conn.commit();
      } catch (Exception ex) {
    	  ex.printStackTrace();
      } finally {
    	  DBConnection.close(conn);
    	  conn = null;
      }
      return false;
  }

  /*****************************************************************
   *主題:刪除row資料
   *****************************************************************/
  public static boolean delete_row(String record_list, int sid,
                                   String brand, String version,
                                   String pd_body) {
    Connection conn = null;
    boolean flag = true;
    String be_opt = "";
    String test_mode = "";
    int pin_count = 0;
    String pg_type = "";
    String tester = "";
    String pg_name = "";
    String actual_file = "";
    String pgm_special_control = "";
    String one_main_pgm_group_version = "";
    String site = "";
    String body_size = "";
    int pg_id = 0;
    String sql = null;

    try {
      conn = DBConnection.getConnection();
      sql = "delete from tf_test_parameter_ft_tx " +
          "where sid = ? and product_body=? and brand=? and version=? " +
          "and test_type=? and backend_option=? and pin_count=? " +
          "and package_type=? and body_size=? and tester=? " +
          "and pgm_id=? and program_name=? and NVL(actual_file, '1234') = NVL(?, '1234')";
      PreparedStatement ps = conn.prepareStatement(sql);
      String record_id_str[] = record_list.split(",");

      for (int i = 1; i < record_id_str.length; i++) {
        be_opt = record_id_str[i++];
        test_mode = record_id_str[i++];
        pin_count = Integer.parseInt(record_id_str[i++]);
        pg_type = record_id_str[i++];
        tester = record_id_str[i++];
        pg_name = record_id_str[i++];
        site = record_id_str[i++];
        body_size = record_id_str[i++];
        actual_file = record_id_str[i++];
        pgm_special_control = record_id_str[i++];
        one_main_pgm_group_version = record_id_str[i++];
        pg_id = Integer.parseInt(record_id_str[i++]);
        i++;
        i++; // skip temperature (hw_configure)
        i++; // skip comment (comment)

        ps.setInt(1, sid);
        ps.setString(2, pd_body);
        ps.setString(3, brand);
        ps.setString(4, version);
        ps.setString(5, test_mode);
        ps.setString(6, be_opt);
        ps.setInt(7, pin_count);
        ps.setString(8, pg_type);
        ps.setString(9, body_size);
        ps.setString(10, tester);
        //ps.setString(11, site);
        ps.setInt(11, pg_id);
        ps.setString(12, pg_name);
        ps.setString(13, actual_file);
        ps.executeUpdate();
      }
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      DBConnection.rollback(conn);
      flag = false;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  /*****************************************************************
   *主題:reset資料,即delete tf_test_parameter_ft_tx中之相資關資料
   ******************************************************************/
  public static boolean reset_tx(int sid) {

    Connection conn = null;
    boolean flag = true;
    String sql = null;
    String InsSQL1 = null;

    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      sql = "delete from tf_test_parameter_ft_tx " +
          "where sid = ? ";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, sid);
      TDSLogger.println(sql.toString());
      TDSLogger.println("reset_tx");
      ps.executeUpdate();

      InsSQL1 = "update tf_information set TF_TEST_PARAMETER_FT = ? " +
          "where sid = ? ";

      PreparedStatement ps3 = conn.prepareStatement(InsSQL1);
      ps3.setString(1, "N");
      ps3.setInt(2, sid);
      ps3.executeUpdate();
      conn.commit();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  /*****************************************************************
   *主題:取得sid之基本資料
   *****************************************************************/
  public static FTTestActionForm[] getPGMInfo(int sid) {
    Connection conn = null;
    boolean flag = true;

    try {
      String sql = "SELECT distinct a.pgm_id " +
          "FROM tf_test_parameter_ws_tx a,pg_test_program b " +
          "where a.sid='" + sid + "' and a.pgm_id=b.program_id and b.program_status ='53' " +
          "union SELECT distinct a.pgm_id " +
          "FROM tf_test_parameter_ft_tx a,pg_test_program b " +
          "where a.sid='" + sid + "'  and a.pgm_id=b.program_id and b.program_status ='53' ";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      String pgm_id = "";
      int i = 0;
      while (rs.next()) {
        FTTestActionForm bean = new FTTestActionForm();
        bean.setPg_id(rs.getInt("pgm_id"));
        tmp.add(bean);
      }
      return (FTTestActionForm[]) tmp.toArray(new FTTestActionForm[0]);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
      return null;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  public static boolean exit_conform(int sid) {
    boolean flag = false;
    String InsSQL1 = null;
    Connection conn = null;
    Connection conn1 = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      conn = DBConnection.getConnection();
      String SelSQL = "SELECT count(*) as total_count " +
          "FROM tf_information where sid='" + sid + "' " +
          "and TF_PRODUCT_ROUTE='Y' and TF_BOM_ROUTE='Y' and TF_BOM_REROUTE='Y' and TF_TEST_PARAMETER_WS='Y' " +
          "and TF_TEST_PARAMETER_FT='Y' and TF_MAIN_SUB='Y' and TF_MAIN_REWORK='Y'  " +
          "and TF_DOCUMENT_LINKAGE='Y' ";

      PreparedStatement ps = conn.prepareStatement(SelSQL);
      ResultSet rs = ps.executeQuery();
      conn1 = DBConnection.getConnection();
      InsSQL1 = " update tf_information set status = ? where sid = ?  ";
      PreparedStatement ps3 = conn1.prepareStatement(InsSQL1);

      while (rs.next()) {
        if (rs.getString("total_count").equals("0") == true) {
          flag = false;
        } else {
          flag = true;
          ps3.setString(1, "A");
          ps3.setInt(2, sid);
          ps3.executeUpdate();
        }
      }
      return flag;
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn1);
      DBConnection.close(conn);
      conn = null;
      conn1 = null;
    }
    return flag;
  }

  public static boolean had_had(int sid) {
    boolean flag = false;
    StringBuffer SelSQL = new StringBuffer();
    String InsSQL1 = null;
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();

      TDSLogger.println("sid");
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT count(*) as total_count FROM tf_information where sid='" +
                    sid + "'  and status='A' ");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        if (rs.getString("total_count").equals("0") == true) {
          flag = false;
        } else {
          flag = true;
        }
      }
      return flag;
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return flag;
  }

  public static boolean tf_final_exit(int sid) {
    Connection conn = null;
    boolean flag=false;
    String InsSQL3 = null;

    try {
      conn = DBConnection.getConnection();
      StringBuffer sql = new StringBuffer();

      //***記得有少找出option及tester的條件喔~~
      sql.append("SELECT count(*) as total_count FROM tf_information where sid='" + sid +
                 "' and  TF_PRODUCT_ROUTE='Y' and TF_BOM_ROUTE='Y' and TF_BOM_REROUTE='Y' " +
                 "and TF_TEST_PARAMETER_WS='Y' and TF_TEST_PARAMETER_FT='Y' " +
                 "and TF_YIELD_WS='Y' and TF_YIELD_FT='Y' " +
                 "and TF_DOCUMENT_LINKAGE='Y' and TF_MAIN_SUB='Y' and TF_MAIN_REWORK='Y' " +
                 "and TF_TEST_PARAMETER_PBC='Y' and TF_WIP_CONTROL='Y' ");

      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      //StringBuffer InsertSQL3 = new StringBuffer();
      while (rs.next()) {
        if (rs.getString("total_count").equals("0") == true) {
        } else {
          flag=true;
        }
      }
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn);
      conn = null;
      return flag;
    }
  }

  public static boolean status_apply(int sid) {
    Connection conn = null;
    boolean flag=false;
    String InsSQL3 = null;

    try {
      conn = DBConnection.getConnection();
      StringBuffer sql = new StringBuffer();
      //***記得有少找出option及tester的條件喔~~
      sql.append("SELECT count(*) as total_count FROM tf_information where sid='" + sid +
                 "' and  status='A' ");

      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      //StringBuffer InsertSQL3 = new StringBuffer();
      while (rs.next()) {
        if (rs.getString("total_count").equals("0") == true) {
          flag=true;
        } else {
          flag=false;
        }
      }
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn);
      conn = null;
      return flag;
    }
  }

  public static void main(String[] args) {
    FTService fTService = new FTService();
  }
}
