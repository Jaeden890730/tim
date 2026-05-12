package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;

/*****************************************************************
* 差異只列新增或刪除，修改視為刪舊資料，增新資料
*****************************************************************/

public class EditiionCompareService {
  public EditiionCompareService() {
  }

  public static boolean isBomDiff(Connection conn, String sid,
                                  String sid2,
                                  String status,
                                  int flag) {
    EditiionCompareActionForm[] result =
        EditiionCompareService.CompareBom(conn, sid, sid2, status, flag, 0);

    if ((result != null) && (result.length > 0))
      return true;
    else
      return false;
  }

  public static WSProductRouteDefinitionForm[] CompareWSPDR(Connection conn, String sid,
                                                            String sid2,
                                                            String status,
                                                            String site,
                                                            int compareType) {

    StringBuffer sqlStmt = new StringBuffer();
    //Connection conn = null;

    ArrayList tmp2 = new ArrayList();
    try {
      //conn = DBConnection.getConnection();
      if ((site != null) && (site.length() > 0))
        sqlStmt.append("call TF_COMPARE_WS_PRODUCT_ROUTE_X ( " + sid + ", '" + status + "', " + sid2 + ", '" + site + "') ");
      else
        sqlStmt.append("call TF_COMPARE_WS_PRODUCT_ROUTE_X ( " + sid + ", '" + status + "', " + sid2 + ", null) ");
      CallableStatement stmt = conn.prepareCall(sqlStmt.toString());
      stmt.execute();

      sqlStmt = new StringBuffer();

      //sqlStmt.append("SELECT ORD, TAG,route_type_seq, PRODUCT_BODY, BODY_VERSION, MASK_OPTION, MASK_OPTION_REV, CODE_NO, SORT_ROUTE_CODE, ");
      sqlStmt.append("SELECT distinct ORD, TAG,route_type_seq, PRODUCT_BODY, BODY_VERSION, MASK_OPTION, MASK_OPTION_REV, CODE_NO, ");
      sqlStmt.append("WS_ROUTE, TEST_MODE, TESTER, SITE, PGM_ID,PROGRAM_NAME, TEMPERATURE, ");
      sqlStmt.append("NVL(TF_COMMENT, '') FT_COMMENT, NVL(WS_COMMENT, '') WS_COMMENT, ROUTE_TYPE, WS_ROUTE_ADD, HW_CONFIGURE, PGM_SPECIAL_CONTROL, ");
      sqlStmt.append("ROUTE_TYPE_FLAG, WS_ROUTE_FLAG, WS_ROUTE_ADD_FLAG, TEST_MODE_FLAG, TESTER_FLAG, SITE_FLAG, PROGRAM_NAME_FLAG,  ");
      sqlStmt.append("TEMPERATURE_FLAG, TF_COMMENT_FLAG, TF_WS_COMMENT_FLAG, HW_CONFIGURE_FLAG, PGM_SPECIAL_CONTROL_FLAG, MASK_OPTION_FLAG,PGM_ID_FLAG  ");
      sqlStmt.append("FROM TF_CMP_03_X ORDER BY ord,product_body,body_version,mask_option,mask_option_rev,ws_route,route_type_seq,ws_route_add,test_mode,tester,site,TAG ");

      TDSLogger.println(sqlStmt.toString());
      PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        WSProductRouteDefinitionForm ws = new WSProductRouteDefinitionForm();
        ws.setOrd(rs.getInt("ORD"));
        ws.setTag(rs.getString("TAG"));
        ws.setProductBody(rs.getString("PRODUCT_BODY"));
        ws.setBodyVersion(rs.getString("BODY_VERSION"));
        ws.setMaskOption(rs.getString("MASK_OPTION"));
        ws.setMaskOptionRev(rs.getString("MASK_OPTION_REV"));
        ws.setCodeNo(rs.getString("CODE_NO"));
        //ws.setSortRouteCode(rs.getString("SORT_ROUTE_CODE"));
        ws.setWsRoute(rs.getString("WS_ROUTE"));
        ws.setTestMode(rs.getString("TEST_MODE"));
        ws.setTester(rs.getString("TESTER"));
        ws.setSite(rs.getString("SITE"));
        ws.setPgm_id(rs.getString("PGM_ID"));
        ws.setProgramName(rs.getString("PROGRAM_NAME"));
        ws.setTemperature(rs.getString("TEMPERATURE"));
        ws.setTfComment(rs.getString("FT_COMMENT"));
        ws.setRoute_type(rs.getString("ROUTE_TYPE"));
        ws.setWsComment(rs.getString("WS_COMMENT"));
        ws.setWsRouteAdd(rs.getString("WS_ROUTE_ADD"));
        ws.setHw_configure(rs.getString("HW_CONFIGURE"));
        ws.setPgm_special_control(rs.getString("PGM_SPECIAL_CONTROL"));
        ws.setRoute_type_flag(rs.getInt("ROUTE_TYPE_FLAG"));
        ws.setWs_route_flag(rs.getInt("WS_ROUTE_FLAG"));
        ws.setWs_route_add_flag(rs.getInt("WS_ROUTE_ADD_FLAG"));
        ws.setTest_mode_flag(rs.getInt("TEST_MODE_FLAG"));
        ws.setTester_flag(rs.getInt("TESTER_FLAG"));
        ws.setSite_flag(rs.getInt("SITE_FLAG"));
        ws.setProgram_name_flag(rs.getInt("PROGRAM_NAME_FLAG"));
        ws.setTemperature_flag(rs.getInt("TEMPERATURE_FLAG"));
        ws.setTf_comment_flag(rs.getInt("TF_COMMENT_FLAG"));
        ws.setTf_ws_comment_flag(rs.getInt("TF_WS_COMMENT_FLAG"));
        ws.setHw_configure_flag(rs.getInt("HW_CONFIGURE_FLAG"));
        ws.setPgm_special_control_flag(rs.getInt("PGM_SPECIAL_CONTROL_FLAG"));
        ws.setMask_option_flag(rs.getInt("MASK_OPTION_FLAG"));
        ws.setPgm_id_flag(rs.getInt("PGM_ID_FLAG"));
        tmp2.add(ws);
        if (compareType == 0)
          break;
      }
      return (WSProductRouteDefinitionForm[]) tmp2.toArray(new WSProductRouteDefinitionForm[0]);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //DBConnection.close(conn);
      //conn = null;
    }
    return null;
  }

  public static FTProductRouteDefinitionForm[] CompareFTPDR(Connection conn, String sid,
                                                            String sid2,
                                                            String status,
                                                            String site,
                                                            int compareType) {

    StringBuffer sqlStmt = new StringBuffer();
    StringBuffer sqlStmt2 = new StringBuffer();
    //Connection conn = null;
    String productType = OiMaintainService.getProductType(sid);

    ArrayList tmp2 = new ArrayList();
    try {
      //conn = DBConnection.getConnection();
      if ((site != null) && (site.length() > 0)){
          sqlStmt.append("call TF_COMPARE_FT_PROD_ROUTE_X ( " + sid + ", '" + status + "', " + sid2 + ", '" + site + "') ");
          sqlStmt2.append("call TF_COMPARE_PBC_PROD_ROUTE_X ( " + sid + ", '" + status + "', " + sid2 + ", '" + site + "') ");
      }else{
      	  sqlStmt.append("call TF_COMPARE_FT_PROD_ROUTE_X ( " + sid + ", '" + status + "', " + sid2 + ", null) ");
          sqlStmt2.append("call TF_COMPARE_PBC_PROD_ROUTE_X ( " + sid + ", '" + status + "', " + sid2 + ", null) ");
      }

      CallableStatement stmt = conn.prepareCall(sqlStmt.toString());
      stmt.execute();
      stmt = conn.prepareCall(sqlStmt2.toString());
      stmt.execute();

      sqlStmt = new StringBuffer();
      //sqlStmt.append("SELECT ORD, TAG, route_type_seq, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
      sqlStmt.append("SELECT distinct ORD, TAG, route_type_seq, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
      //sqlStmt.append("BACKEND_OPTION, BODY_VERSION, BACKEND_REV, CODE_NO, FT_ROUTE_CODE, FT_ROUTE, TEST_MODE, ");
      sqlStmt.append("BACKEND_OPTION, BODY_VERSION, BACKEND_REV, CODE_NO, FT_ROUTE, TEST_MODE, ");
      sqlStmt.append("TESTER, SITE, PGM_ID, PROGRAM_NAME, I_GRADE, C_GRADE, BODY_SIZE, TF_COMMENT, FT_COMMENT, ROUTE_TYPE, ACTUAL_FILE, PGM_SPECIAL_CONTROL, ROUTE_TYPE_X, FT_ROUTE_ADD, HW_CONFIGURE, ");
      sqlStmt.append("ROUTE_TYPE_FLAG, FT_ROUTE_FLAG, FT_ROUTE_ADD_FLAG, TEST_MODE_FLAG, PACKAGE_CODE_FLAG, PACKAGE_NAME_FLAG, PIN_COUNT_FLAG, CODE_NO_FLAG, C_GRADE_FLAG, BODY_SIZE_FLAG, TESTER_FLAG, SITE_FLAG, PGM_ID_FLAG,PROGRAM_NAME_FLAG,  ");
      sqlStmt.append("ACTUAL_FILE_FLAG, PGM_SPECIAL_CONTROL_FLAG, HW_CONFIGURE_FLAG, TF_COMMENT_FLAG, TF_FT_COMMENT_FLAG   ");
      sqlStmt.append("FROM TF_CMP_13_X ");
      sqlStmt.append("UNION ");
	  //sqlStmt.append("SELECT ORD, TAG, route_type_seq, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
      sqlStmt.append("SELECT distinct ORD, TAG, route_type_seq, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
	  //sqlStmt.append("BACKEND_OPTION, BODY_VERSION, BACKEND_REV, CODE_NO, FT_ROUTE_CODE, FT_ROUTE, TEST_MODE, ");
      sqlStmt.append("BACKEND_OPTION, BODY_VERSION, BACKEND_REV, CODE_NO, FT_ROUTE, TEST_MODE, ");
	  sqlStmt.append("TESTER, SITE,PGM_ID, PROGRAM_NAME, I_GRADE, C_GRADE, BODY_SIZE, TF_COMMENT, FT_COMMENT, ROUTE_TYPE, ACTUAL_FILE, PGM_SPECIAL_CONTROL, ROUTE_TYPE_X, FT_ROUTE_ADD, HW_CONFIGURE, ");
	  sqlStmt.append("ROUTE_TYPE_FLAG, FT_ROUTE_FLAG, FT_ROUTE_ADD_FLAG, TEST_MODE_FLAG, PACKAGE_CODE_FLAG, PACKAGE_NAME_FLAG, PIN_COUNT_FLAG, CODE_NO_FLAG, C_GRADE_FLAG, BODY_SIZE_FLAG, TESTER_FLAG, SITE_FLAG, PGM_ID_FLAG,PROGRAM_NAME_FLAG,  ");
      sqlStmt.append("ACTUAL_FILE_FLAG, PGM_SPECIAL_CONTROL_FLAG, HW_CONFIGURE_FLAG, TF_COMMENT_FLAG, TF_FT_COMMENT_FLAG   ");
	  sqlStmt.append("FROM TF_CMP_23_X ");
      sqlStmt.append("ORDER BY ord,product_body,backend_option,package_code,pin_count,body_version,ft_route,route_type_seq,ft_route_add,test_mode,tester,site,TAG ");
      TDSLogger.println(sqlStmt.toString());
      PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        FTProductRouteDefinitionForm ft = new FTProductRouteDefinitionForm();
        ft.setOrd(rs.getInt("ORD"));
        ft.setTag(rs.getString("TAG"));
        ft.setProductBody(rs.getString("PRODUCT_BODY"));
        ft.setPackageCode(rs.getString("PACKAGE_CODE"));
        ft.setPackageName(rs.getString("PACKAGE_NAME"));
        ft.setPinCount(rs.getString("PIN_COUNT"));
        ft.setBackendOption(rs.getString("BACKEND_OPTION"));
        ft.setBodyVersion(rs.getString("BODY_VERSION"));
        ft.setMaskOptionRev(rs.getString("BACKEND_REV"));
        ft.setCodeNo(rs.getString("CODE_NO"));
        //ft.setFtRouteCode(rs.getString("FT_ROUTE_CODE"));
        ft.setFtRoute(rs.getString("FT_ROUTE"));
        ft.setTestMode(rs.getString("TEST_MODE"));
        ft.setTester(rs.getString("TESTER"));
        ft.setSite(rs.getString("SITE"));
        ft.setPgm_id(rs.getString("PGM_ID"));
        ft.setProgramName(rs.getString("PROGRAM_NAME"));
        ft.setActual_file(rs.getString("ACTUAL_FILE"));
        ft.setPgm_special_control(rs.getString("PGM_SPECIAL_CONTROL"));
        ft.setI_Grade(rs.getString("I_GRADE"));
        ft.setC_Grade(rs.getString("C_GRADE"));
        ft.setBodySize(rs.getString("BODY_SIZE"));
        ft.setRoute_type(rs.getString("ROUTE_TYPE"));
        ft.setTfComment(rs.getString("TF_COMMENT"));
        ft.setFtComment(rs.getString("FT_COMMENT"));
	    ft.setRoute_type_x(rs.getString("ROUTE_TYPE_X"));
        ft.setFtRouteAdd(rs.getString("FT_ROUTE_ADD"));
        ft.setHw_configure(rs.getString("HW_CONFIGURE"));
        ft.setRoute_type_flag(rs.getInt("ROUTE_TYPE_FLAG"));
        ft.setFt_route_flag(rs.getInt("FT_ROUTE_FLAG"));
        ft.setFt_route_add_flag(rs.getInt("FT_ROUTE_ADD_FLAG"));
        ft.setTest_mode_flag(rs.getInt("TEST_MODE_FLAG"));
        ft.setPackage_code_flag(rs.getInt("PACKAGE_CODE_FLAG"));
        ft.setPackage_name_flag(rs.getInt("PACKAGE_NAME_FLAG"));
        ft.setPin_count_flag(rs.getInt("PIN_COUNT_FLAG"));
        ft.setCode_no_flag(rs.getInt("CODE_NO_FLAG"));
        ft.setC_grade_flag(rs.getInt("C_GRADE_FLAG"));
        ft.setBody_size_flag(rs.getInt("BODY_SIZE_FLAG"));
        ft.setTester_flag(rs.getInt("TESTER_FLAG"));
        ft.setSite_flag(rs.getInt("SITE_FLAG"));
        ft.setProgram_name_flag(rs.getInt("PROGRAM_NAME_FLAG"));
        ft.setActual_file_flag(rs.getInt("ACTUAL_FILE_FLAG"));
        ft.setPgm_special_control_flag(rs.getInt("PGM_SPECIAL_CONTROL_FLAG"));
        ft.setHw_configure_flag(rs.getInt("HW_CONFIGURE_FLAG"));
        ft.setTf_comment_flag(rs.getInt("TF_COMMENT_FLAG"));
        ft.setTf_ft_comment_flag(rs.getInt("TF_FT_COMMENT_FLAG"));
        ft.setPgm_id_flag(rs.getInt("PGM_ID_FLAG"));
        tmp2.add(ft);
        if (compareType == 0)
          break;
      }
      return (FTProductRouteDefinitionForm[]) tmp2.toArray(new FTProductRouteDefinitionForm[0]);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //DBConnection.close(conn);
      //conn = null;
    }
    return null;
  }

  public static FTProductReRouteDefinitionForm[] CompareFTPDR_RE(Connection conn, String sid,
                                                            String sid2,
                                                            String status,
                                                            String site,
                                                            int compareType) {

    StringBuffer sqlStmt = new StringBuffer();
    StringBuffer sqlStmt2 = new StringBuffer();
    //Connection conn = null;
    String productType = OiMaintainService.getProductType(sid);

    ArrayList tmp2 = new ArrayList();
    try {
      //conn = DBConnection.getConnection();
      if ((site != null) && (site.length() > 0)){
          sqlStmt.append("call TF_COMPARE_FT_PROD_RROUTE_X ( " + sid + ", '" + status + "', " + sid2 + ", '" + site + "') ");
          sqlStmt2.append("call TF_COMPARE_PBC_PROD_RROUTE_X ( " + sid + ", '" + status + "', " + sid2 + ", '" + site + "') ");
      }else{
          sqlStmt.append("call TF_COMPARE_FT_PROD_RROUTE_X ( " + sid + ", '" + status + "', " + sid2 + ", null) ");
          sqlStmt2.append("call TF_COMPARE_PBC_PROD_RROUTE_X ( " + sid + ", '" + status + "', " + sid2 + ", null) ");
      }

      CallableStatement stmt = conn.prepareCall(sqlStmt.toString());
      stmt.execute();
      stmt = conn.prepareCall(sqlStmt2.toString());
      stmt.execute();

      sqlStmt = new StringBuffer();
      //sqlStmt.append("SELECT ORD, TAG, route_type_seq, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
      sqlStmt.append("SELECT distinct ORD, TAG, route_type_seq, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
      //sqlStmt.append("BACKEND_OPTION, BODY_VERSION, MASK_OPTION, MASK_OPTION_REV, CODE_NO, RECYCLE_CODE, FT_ROUTE_CODE, FT_ROUTE, TEST_MODE, ");
      sqlStmt.append("BACKEND_OPTION, BODY_VERSION, MASK_OPTION, MASK_OPTION_REV, CODE_NO, RECYCLE_CODE, FT_ROUTE, TEST_MODE, ");
      sqlStmt.append("TESTER, SITE, PGM_ID, PROGRAM_NAME, I_GRADE, C_GRADE, BODY_SIZE, TF_COMMENT, FT_COMMENT, ROUTE_TYPE, ACTUAL_FILE, PGM_SPECIAL_CONTROL, ROUTE_TYPE_X, FT_ROUTE_ADD, HW_CONFIGURE, ");
      sqlStmt.append("ROUTE_TYPE_FLAG, FT_ROUTE_FLAG, FT_ROUTE_ADD_FLAG, TEST_MODE_FLAG, PACKAGE_CODE_FLAG, PACKAGE_NAME_FLAG, PIN_COUNT_FLAG, RECYLE_CODE_FLAG, C_GRADE_FLAG, BODY_SIZE_FLAG, TESTER_FLAG, SITE_FLAG, PGM_ID_FLAG,PROGRAM_NAME_FLAG,  ");
      sqlStmt.append("ACTUAL_FILE_FLAG, PGM_SPECIAL_CONTROL_FLAG, HW_CONFIGURE_FLAG, TF_COMMENT_FLAG, TF_FT_COMMENT_FLAG   ");
      sqlStmt.append("FROM TF_CMP_13_RE_X ");
      sqlStmt.append("UNION ");
      //sqlStmt.append("SELECT ORD, TAG, route_type_seq, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
      sqlStmt.append("SELECT distinct ORD, TAG, route_type_seq, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
      //sqlStmt.append("BACKEND_OPTION, BODY_VERSION, MASK_OPTION, MASK_OPTION_REV, CODE_NO, RECYCLE_CODE, FT_ROUTE_CODE, FT_ROUTE, TEST_MODE, ");
      sqlStmt.append("BACKEND_OPTION, BODY_VERSION, MASK_OPTION, MASK_OPTION_REV, CODE_NO, RECYCLE_CODE, FT_ROUTE, TEST_MODE, ");
      sqlStmt.append("TESTER, SITE, PGM_ID, PROGRAM_NAME, I_GRADE, C_GRADE, BODY_SIZE, TF_COMMENT, FT_COMMENT, ROUTE_TYPE, ACTUAL_FILE, PGM_SPECIAL_CONTROL, ROUTE_TYPE_X, FT_ROUTE_ADD, HW_CONFIGURE, ");
      sqlStmt.append("ROUTE_TYPE_FLAG, FT_ROUTE_FLAG, FT_ROUTE_ADD_FLAG, TEST_MODE_FLAG, PACKAGE_CODE_FLAG, PACKAGE_NAME_FLAG, PIN_COUNT_FLAG, RECYLE_CODE_FLAG, C_GRADE_FLAG, BODY_SIZE_FLAG, TESTER_FLAG, SITE_FLAG, PGM_ID_FLAG,PROGRAM_NAME_FLAG,  ");
      sqlStmt.append("ACTUAL_FILE_FLAG, PGM_SPECIAL_CONTROL_FLAG, HW_CONFIGURE_FLAG, TF_COMMENT_FLAG, TF_FT_COMMENT_FLAG   ");
      sqlStmt.append("FROM TF_CMP_23_RE_X ");
      sqlStmt.append("ORDER BY ord,product_body,backend_option,body_version,package_code,pin_count,mask_option,mask_option_rev,ft_route,route_type_seq,ft_route_add,test_mode,tester,site,TAG ");
      TDSLogger.println(sqlStmt.toString());
      PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        FTProductReRouteDefinitionForm ft = new FTProductReRouteDefinitionForm();
        ft.setOrd(rs.getInt("ORD"));
        ft.setTag(rs.getString("TAG"));
        ft.setProductBody(rs.getString("PRODUCT_BODY"));
        ft.setPackageCode(rs.getString("PACKAGE_CODE"));
        ft.setPackageName(rs.getString("PACKAGE_NAME"));
        ft.setPinCount(rs.getString("PIN_COUNT"));
        ft.setBackendOption(rs.getString("BACKEND_OPTION"));
        ft.setBodyVersion(rs.getString("BODY_VERSION"));
        ft.setMaskOption(rs.getString("MASK_OPTION"));
        ft.setMaskOptionRev(rs.getString("MASK_OPTION_REV"));
        ft.setCodeNo(rs.getString("CODE_NO"));
        ft.setRecycleCode(rs.getString("RECYCLE_CODE"));
        //ft.setFtRouteCode(rs.getString("FT_ROUTE_CODE"));
        ft.setFtRoute(rs.getString("FT_ROUTE"));
        ft.setTestMode(rs.getString("TEST_MODE"));
        ft.setTester(rs.getString("TESTER"));
        ft.setSite(rs.getString("SITE"));
        ft.setPgm_id(rs.getString("PGM_ID"));
        ft.setProgramName(rs.getString("PROGRAM_NAME"));
        ft.setActual_file(rs.getString("ACTUAL_FILE"));
        ft.setPgm_special_control(rs.getString("PGM_SPECIAL_CONTROL"));
        ft.setI_Grade(rs.getString("I_GRADE"));
        ft.setC_Grade(rs.getString("C_GRADE"));
        ft.setBodySize(rs.getString("BODY_SIZE"));
        ft.setRoute_type(rs.getString("ROUTE_TYPE"));
        ft.setTfComment(rs.getString("TF_COMMENT"));
        ft.setFtComment(rs.getString("FT_COMMENT"));
        ft.setRoute_type_x(rs.getString("ROUTE_TYPE_X"));
        ft.setFtRouteAdd(rs.getString("FT_ROUTE_ADD"));
        ft.setHw_configure(rs.getString("HW_CONFIGURE"));
        ft.setRoute_type_flag(rs.getInt("ROUTE_TYPE_FLAG"));
        ft.setFt_route_flag(rs.getInt("FT_ROUTE_FLAG"));
        ft.setFt_route_add_flag(rs.getInt("FT_ROUTE_ADD_FLAG"));
        ft.setTest_mode_flag(rs.getInt("TEST_MODE_FLAG"));
        ft.setPackage_code_flag(rs.getInt("PACKAGE_CODE_FLAG"));
        ft.setPackage_name_flag(rs.getInt("PACKAGE_NAME_FLAG"));
        ft.setPin_count_flag(rs.getInt("PIN_COUNT_FLAG"));
        ft.setRecycle_code_flag(rs.getInt("RECYLE_CODE_FLAG"));
        ft.setC_grade_flag(rs.getInt("C_GRADE_FLAG"));
        ft.setBody_size_flag(rs.getInt("BODY_SIZE_FLAG"));
        ft.setTester_flag(rs.getInt("TESTER_FLAG"));
        ft.setSite_flag(rs.getInt("SITE_FLAG"));
        ft.setProgram_name_flag(rs.getInt("PROGRAM_NAME_FLAG"));
        ft.setActual_file_flag(rs.getInt("ACTUAL_FILE_FLAG"));
        ft.setPgm_special_control_flag(rs.getInt("PGM_SPECIAL_CONTROL_FLAG"));
        ft.setHw_configure_flag(rs.getInt("HW_CONFIGURE_FLAG"));
        ft.setTf_comment_flag(rs.getInt("TF_COMMENT_FLAG"));
        ft.setTf_ft_comment_flag(rs.getInt("TF_FT_COMMENT_FLAG"));
        ft.setPgm_id_flag(rs.getInt("PGM_ID_FLAG"));
        tmp2.add(ft);
        if (compareType == 0)
          break;
      }
      return (FTProductReRouteDefinitionForm[]) tmp2.toArray(new FTProductReRouteDefinitionForm[0]);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //DBConnection.close(conn);
      //conn = null;
    }
    return null;
  }

  public static TFIMBasicActionForm[] CompareBA (String sid,
                                                 String sid2,
                                                 String status,
                                                 int flag,
                                                 int compareType) {

    StringBuffer sqlStmt = new StringBuffer();
    Connection conn = null;
    String table1 = null;
    String table2 = null;

    ArrayList tmp2 = new ArrayList();
    if (!status.equals("P") && !status.equals("A")) {
      table1 = "TF_BASIC_INFO";
      table2 = "TF_BASIC_INFO";
    } else {
      if (flag == 0) {/* 以前一版為主找出後一版之不同處 */
        table1 = "TF_BASIC_INFO";
        table2 = "TF_BASIC_INFO_TX";
      } else { /* 由後一版找出前一版與其不同之處 */
        table1 = "TF_BASIC_INFO_TX";
        table2 = "TF_BASIC_INFO";
      }
    }

    try {
      conn = DBConnection.getConnection();
      sqlStmt.append("SELECT SID, PRODUCT_BODY, BRAND, VERSION, TESTER, ");
      sqlStmt.append("GOOD_BIN, FAIL_BIN, REMARK, AUTO_SHIP_YIELD, STOP_TEST_YIELD, AUTO_SCRAP_YIELD, MRB_YIELD, SAMPLE_YIELD ");
      sqlStmt.append("FROM " + table1 + " T1 ");
      sqlStmt.append("WHERE SID = " + sid + " ");
      sqlStmt.append("AND NOT EXISTS (SELECT 1 FROM " + table2 + " T2 ");
      sqlStmt.append("WHERE T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND ");
      sqlStmt.append("AND SID = " + sid2 + " ");
      sqlStmt.append("AND T2.TESTER = T1.TESTER AND NVL(T2.GOOD_BIN, ' ') = NVL(T1.GOOD_BIN, ' ') ");
      sqlStmt.append("AND NVL(T2.FAIL_BIN, ' ') = NVL(T1.FAIL_BIN, ' ') AND NVL(T2.REMARK, ' ') = NVL(T1.REMARK, ' ') ");
      sqlStmt.append("AND NVL(T2.AUTO_SHIP_YIELD, 'NA') = NVL(T1.AUTO_SHIP_YIELD, 'NA') ");
      sqlStmt.append("AND NVL(T2.STOP_TEST_YIELD, 'NA') = NVL(T1.STOP_TEST_YIELD, 'NA') ");
      sqlStmt.append("AND NVL(T2.AUTO_SCRAP_YIELD, 'NA') = NVL(T1.AUTO_SCRAP_YIELD, 'NA') ");
      sqlStmt.append("AND NVL(T2.MRB_YIELD, 'NA') = NVL(T1.MRB_YIELD, 'NA') ");
      sqlStmt.append("AND NVL(T2.SAMPLE_YIELD, 'NA') = NVL(T1.SAMPLE_YIELD, 'NA')) ");

      PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        TFIMBasicActionForm baInfo = new TFIMBasicActionForm();
        baInfo.setSid(Integer.parseInt(sid));
        baInfo.setPd_body(rs.getString("PRODUCT_BODY"));
        baInfo.setBrand(rs.getString("BRAND"));
        baInfo.setTester(rs.getString("TESTER"));
        baInfo.setGood_bin(rs.getString("GOOD_BIN"));
        baInfo.setFail_bin(rs.getString("FAIL_BIN"));
        baInfo.setRemark(rs.getString("REMARK"));
        baInfo.setAuto_ship_yield(rs.getString("AUTO_SHIP_YIELD"));
        baInfo.setStop_test_yield(rs.getString("STOP_TEST_YIELD"));
        baInfo.setAuto_scrap_yield(rs.getString("AUTO_SCRAP_YIELD"));
        baInfo.setMrb_yield(rs.getString("MRB_YIELD"));
        baInfo.setSample_yield(rs.getString("SAMPLE_YIELD"));
        tmp2.add(baInfo);
        if (compareType == 0)
          break;
      }
      return (TFIMBasicActionForm[]) tmp2.toArray(new TFIMBasicActionForm[0]);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
  public static TFIMBasicActionForm[] CompareMainRouteSub (Connection conn, String sid,
                                                 String sid2,
                                                 String status,
                                                 int flag,
                                                 int compareType) {

    StringBuffer sqlStmt = new StringBuffer();
    //Connection conn = null;
    String table1 = null;
    String table2 = null;
    String type_flag = null;
    int main_route_flag = 0;
    int map_route_flag = 0;
    int remark_flag = 0;

    ArrayList tmp2 = new ArrayList();
    if (!status.equals("P") && !status.equals("A")) {
      table1 = "TF_MAIN_ROUTE_XROM";
      table2 = "TF_MAIN_ROUTE_XROM";
    } else {
      if (flag == 0) {/* 以前一版為主找出後一版之不同處 */
        table1 = "TF_MAIN_ROUTE_XROM";
        table2 = "TF_MAIN_ROUTE_XROM_TX";
      } else if (flag == 1) { /* 由後一版找出前一版與其不同之處 */
        table1 = "TF_MAIN_ROUTE_XROM_TX";
        table2 = "TF_MAIN_ROUTE_XROM";
      } else { /* 被 update 的資料之處 */
        table1 = "TF_MAIN_ROUTE_XROM_TX";
        table2 = "TF_MAIN_ROUTE_XROM";  
      }
    }
    if (flag == 0){ 
		type_flag = "remove";
		main_route_flag = 0;
	    map_route_flag = 0;
	    remark_flag = 0;
	}else if (flag == 1){ 
		type_flag = "insert";
		main_route_flag = 1;
	    map_route_flag = 1;
	    remark_flag = 1;
	}else{
		main_route_flag = 0;
	    map_route_flag = 0;
	    remark_flag = 0;
	}

    try {
      //conn = DBConnection.getConnection();
      if (flag == 0 || flag == 1){
	      sqlStmt.append("SELECT '" + type_flag + "' TYPE_FLAG, SID, PRODUCT_BODY, VERSION, MAIN_ROUTE, MAP_ROUTE, REMARK, \n");
	      sqlStmt.append(main_route_flag + " MAIN_ROUTE_FLAG, " + map_route_flag + " MAP_ROUTE_FLAG, " + remark_flag + " REMARK_FLAG \n");
	      sqlStmt.append("FROM " + table1 + " T1 \n");
	      sqlStmt.append("WHERE SID = " + sid + " AND ROUTE_TYPE = 0 \n");
	      sqlStmt.append("AND NOT EXISTS (SELECT 1 FROM " + table2 + " T2 \n");
	      sqlStmt.append("WHERE T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.ROUTE_TYPE = 0 \n");
	      sqlStmt.append("AND SID = " + sid2 + " \n");
	      sqlStmt.append(" AND NVL(T2.MAIN_ROUTE, ' ') = NVL(T1.MAIN_ROUTE, ' ') \n");
	      sqlStmt.append("AND NVL(T2.MAP_ROUTE, ' ') = NVL(T1.MAP_ROUTE, ' ') \n");
	      //sqlStmt.append("AND NVL(T2.REMARK, ' ') = NVL(T1.REMARK, ' ')) ");
	      sqlStmt.append(")");
      }else{
    	  sqlStmt.append("SELECT 'old' TYPE_FLAG, T2.SID, T2.PRODUCT_BODY, T2.VERSION, T2.MAIN_ROUTE, T2.MAP_ROUTE, T2.REMARK, \n");
	      sqlStmt.append(main_route_flag + " MAIN_ROUTE_FLAG, " + map_route_flag + " MAP_ROUTE_FLAG, " + remark_flag + " REMARK_FLAG \n");
	      sqlStmt.append("FROM " + table1 + " T1, " + table2 + " T2 \n");
	      sqlStmt.append("WHERE T1.SID = " + sid + " AND T1.ROUTE_TYPE = 0 \n");
	      sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.ROUTE_TYPE = 0 \n");
	      sqlStmt.append("AND T2.SID = " + sid2 + " \n");
	      sqlStmt.append(" AND NVL(T2.MAIN_ROUTE, ' ') = NVL(T1.MAIN_ROUTE, ' ') \n");
	      sqlStmt.append("AND NVL(T2.MAP_ROUTE, ' ') = NVL(T1.MAP_ROUTE, ' ') \n");
	      sqlStmt.append("AND NVL(T2.REMARK, ' ') != NVL(T1.REMARK, ' ') ");
	      sqlStmt.append("UNION \n");
	      sqlStmt.append("SELECT 'update' TYPE_FLAG, T1.SID, T1.PRODUCT_BODY, T1.VERSION, T1.MAIN_ROUTE, T1.MAP_ROUTE, T1.REMARK, \n");
	      sqlStmt.append("decode(nvl(T2.MAIN_ROUTE, '#'),nvl(T1.MAIN_ROUTE, '#'),0,1) MAIN_ROUTE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.MAP_ROUTE, '#'),nvl(T1.MAP_ROUTE, '#'),0,1) MAP_ROUTE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.REMARK, '#'),nvl(T1.REMARK, '#'),0,1) REMARK_FLAG \n");
	      sqlStmt.append("FROM " + table1 + " T1, " + table2 + " T2 \n");
	      sqlStmt.append("WHERE T1.SID = " + sid + " AND T1.ROUTE_TYPE = 0 \n");
	      sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.ROUTE_TYPE = 0 \n");
	      sqlStmt.append("AND T2.SID = " + sid2 + " \n");
	      sqlStmt.append(" AND NVL(T2.MAIN_ROUTE, ' ') = NVL(T1.MAIN_ROUTE, ' ') \n");
	      sqlStmt.append("AND NVL(T2.MAP_ROUTE, ' ') = NVL(T1.MAP_ROUTE, ' ') \n");
	      sqlStmt.append("AND NVL(T2.REMARK, ' ') != NVL(T1.REMARK, ' ') ");
    	  
      }
      TDSLogger.println(sqlStmt.toString());
      PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        TFIMBasicActionForm baInfo = new TFIMBasicActionForm();
        baInfo.setSid(Integer.parseInt(sid));
        baInfo.setPd_body(rs.getString("PRODUCT_BODY"));
        baInfo.setMain_route(rs.getString("MAIN_ROUTE"));
        baInfo.setMap_route(rs.getString("MAP_ROUTE"));
        baInfo.setRemark(rs.getString("REMARK"));
        baInfo.setType_flag(rs.getString("TYPE_FLAG"));
        baInfo.setMain_route_flag(rs.getInt("MAIN_ROUTE_FLAG"));
        baInfo.setMap_route_flag(rs.getInt("MAP_ROUTE_FLAG"));
        baInfo.setRemark_flag(rs.getInt("REMARK_FLAG"));
        tmp2.add(baInfo);
        if (compareType == 0)
          break;
      }
      return (TFIMBasicActionForm[]) tmp2.toArray(new TFIMBasicActionForm[0]);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //DBConnection.close(conn);
      //conn = null;
    }
    return null;
  }
  public static TFIMBasicActionForm[] CompareMainRouteRework (Connection conn, String sid,
                                                   String sid2,
                                                   String status,
                                                   int flag,
                                                   int compareType) {

      StringBuffer sqlStmt = new StringBuffer();
      //Connection conn = null;
      String table1 = null;
      String table2 = null;
      String type_flag = null;
      int main_route_flag = 0;
      int map_route_flag = 0;
      int remark_flag = 0;

      ArrayList tmp2 = new ArrayList();
      if (!status.equals("P") && !status.equals("A")) {
        table1 = "TF_MAIN_ROUTE_XROM";
        table2 = "TF_MAIN_ROUTE_XROM";
      } else {
        if (flag == 0) {/* 以前一版為主找出後一版之不同處 */
          table1 = "TF_MAIN_ROUTE_XROM";
          table2 = "TF_MAIN_ROUTE_XROM_TX";
        } else if (flag == 1) { /* 由後一版找出前一版與其不同之處 */
          table1 = "TF_MAIN_ROUTE_XROM_TX";
          table2 = "TF_MAIN_ROUTE_XROM";
        } else { /* 被 update 的資料之處 */
          table1 = "TF_MAIN_ROUTE_XROM_TX";
          table2 = "TF_MAIN_ROUTE_XROM";  
        }
      }
      if (flag == 0){ 
  		type_flag = "remove";
  		main_route_flag = 0;
  	    map_route_flag = 0;
  	    remark_flag = 0;
  	  }else if (flag == 1){ 
  		type_flag = "insert";
  		main_route_flag = 1;
  	    map_route_flag = 1;
  	    remark_flag = 1;
  	  }else{
  		main_route_flag = 0;
  	    map_route_flag = 0;
  	    remark_flag = 0;
  	  }

      try {
        //conn = DBConnection.getConnection();
        if (flag == 0 || flag == 1){
  	      sqlStmt.append("SELECT '" + type_flag + "' TYPE_FLAG, SID, PRODUCT_BODY, VERSION, MAIN_ROUTE, MAP_ROUTE, REMARK, \n");
  	      sqlStmt.append(main_route_flag + " MAIN_ROUTE_FLAG, " + map_route_flag + " MAP_ROUTE_FLAG, " + remark_flag + " REMARK_FLAG \n");
  	      sqlStmt.append("FROM " + table1 + " T1 \n");
  	      sqlStmt.append("WHERE SID = " + sid + " AND ROUTE_TYPE = 1 \n");
  	      sqlStmt.append("AND NOT EXISTS (SELECT 1 FROM " + table2 + " T2 \n");
  	      sqlStmt.append("WHERE T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.ROUTE_TYPE = 1 \n");
  	      sqlStmt.append("AND SID = " + sid2 + " \n");
  	      sqlStmt.append(" AND NVL(T2.MAIN_ROUTE, ' ') = NVL(T1.MAIN_ROUTE, ' ') \n");
  	      sqlStmt.append("AND NVL(T2.MAP_ROUTE, ' ') = NVL(T1.MAP_ROUTE, ' ') \n");
  	      //sqlStmt.append("AND NVL(T2.REMARK, ' ') = NVL(T1.REMARK, ' ')) ");
  	      sqlStmt.append(")");
        }else{
      	  sqlStmt.append("SELECT 'old' TYPE_FLAG, T2.SID, T2.PRODUCT_BODY, T2.VERSION, T2.MAIN_ROUTE, T2.MAP_ROUTE, T2.REMARK, \n");
  	      sqlStmt.append(main_route_flag + " MAIN_ROUTE_FLAG, " + map_route_flag + " MAP_ROUTE_FLAG, " + remark_flag + " REMARK_FLAG \n");
  	      sqlStmt.append("FROM " + table1 + " T1, " + table2 + " T2 \n");
  	      sqlStmt.append("WHERE T1.SID = " + sid + " AND T1.ROUTE_TYPE = 1 \n");
  	      sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.ROUTE_TYPE = 1 \n");
  	      sqlStmt.append("AND T2.SID = " + sid2 + " \n");
  	      sqlStmt.append(" AND NVL(T2.MAIN_ROUTE, ' ') = NVL(T1.MAIN_ROUTE, ' ') \n");
  	      sqlStmt.append("AND NVL(T2.MAP_ROUTE, ' ') = NVL(T1.MAP_ROUTE, ' ') \n");
  	      sqlStmt.append("AND NVL(T2.REMARK, ' ') != NVL(T1.REMARK, ' ') ");
  	      sqlStmt.append("UNION \n");
  	      sqlStmt.append("SELECT 'update' TYPE_FLAG, T1.SID, T1.PRODUCT_BODY, T1.VERSION, T1.MAIN_ROUTE, T1.MAP_ROUTE, T1.REMARK, \n");
  	      sqlStmt.append("decode(nvl(T2.MAIN_ROUTE, '#'),nvl(T1.MAIN_ROUTE, '#'),0,1) MAIN_ROUTE_FLAG, \n");
  	      sqlStmt.append("decode(nvl(T2.MAP_ROUTE, '#'),nvl(T1.MAP_ROUTE, '#'),0,1) MAP_ROUTE_FLAG, \n");
  	      sqlStmt.append("decode(nvl(T2.REMARK, '#'),nvl(T1.REMARK, '#'),0,1) REMARK_FLAG \n");
  	      sqlStmt.append("FROM " + table1 + " T1, " + table2 + " T2 \n");
  	      sqlStmt.append("WHERE T1.SID = " + sid + " AND T1.ROUTE_TYPE = 1 \n");
  	      sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.ROUTE_TYPE = 1 \n");
  	      sqlStmt.append("AND T2.SID = " + sid2 + " \n");
  	      sqlStmt.append(" AND NVL(T2.MAIN_ROUTE, ' ') = NVL(T1.MAIN_ROUTE, ' ') \n");
  	      sqlStmt.append("AND NVL(T2.MAP_ROUTE, ' ') = NVL(T1.MAP_ROUTE, ' ') \n");
  	      sqlStmt.append("AND NVL(T2.REMARK, ' ') != NVL(T1.REMARK, ' ') ");
      	  
        }
        TDSLogger.println(sqlStmt.toString());
        PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          TFIMBasicActionForm baInfo = new TFIMBasicActionForm();
          baInfo.setSid(Integer.parseInt(sid));
          baInfo.setPd_body(rs.getString("PRODUCT_BODY"));
          baInfo.setMain_route(rs.getString("MAIN_ROUTE"));
          baInfo.setMap_route(rs.getString("MAP_ROUTE"));
          baInfo.setRemark(rs.getString("REMARK"));
          baInfo.setType_flag(rs.getString("TYPE_FLAG"));
          baInfo.setMain_route_flag(rs.getInt("MAIN_ROUTE_FLAG"));
          baInfo.setMap_route_flag(rs.getInt("MAP_ROUTE_FLAG"));
          baInfo.setRemark_flag(rs.getInt("REMARK_FLAG"));
          tmp2.add(baInfo);
          if (compareType == 0)
            break;
        }
        return (TFIMBasicActionForm[]) tmp2.toArray(new TFIMBasicActionForm[0]);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        //DBConnection.close(conn);
        //conn = null;
      }
      return null;
    }


  public static EditiionCompareActionForm[] ComparePDR(String sid,
                                                       String sid2,
                                                       String status,
                                                       int flag,
                                                       int compareType) {
    StringBuffer sqlStmt = new StringBuffer();
    Connection conn = null;
    String table1 = null;
    String table2 = null;

    ArrayList tmp2 = new ArrayList();
    if (!status.equals("P") && !status.equals("A")) {
      table1 = "TF_PRODUCT_ROUTE";
      table2 = "TF_PRODUCT_ROUTE";
    } else {
      if (flag == 0) {/* 以前一版為主找出後一版之不同處 */
        table1 = "TF_PRODUCT_ROUTE";
        table2 = "TF_PRODUCT_ROUTE_TX";
      } else { /* 由後一版找出前一版與其不同之處 */
        table1 = "TF_PRODUCT_ROUTE_TX";
        table2 = "TF_PRODUCT_ROUTE";
      }
    }

    try {
      conn = DBConnection.getConnection();
      sqlStmt.append("SELECT SID, PRODUCT_BODY, BRAND, VERSION, ROUTE_NAME, STEP_SEQ, ");
      sqlStmt.append("STEP_NAME, TEST_TIME, TIME_UNIT, TEMPERATURE, SAMPLING_TEST, SAMPLING_COND, REMARK ");
      sqlStmt.append("FROM " + table1 + " T1 ");
      sqlStmt.append("WHERE SID = " + sid + " ");
      sqlStmt.append("AND BRAND=' ' ");
      sqlStmt.append("AND NOT EXISTS (SELECT 1 FROM " + table2 + " T2 ");
      sqlStmt.append("WHERE T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND ");
      sqlStmt.append("AND SID = " + sid2 + " AND T2.ROUTE_NAME = T1.ROUTE_NAME ");
      sqlStmt.append("AND T2.STEP_SEQ = T1.STEP_SEQ AND T2.STEP_NAME = T1.STEP_NAME ");
      sqlStmt.append("AND NVL(T2.TEST_TIME, -999) = NVL(T1.TEST_TIME, -999) AND NVL(T2.TIME_UNIT, ' ') = NVL(T1.TIME_UNIT, ' ') ");
      sqlStmt.append("AND NVL(T2.TEMPERATURE, ' ') = NVL(T1.TEMPERATURE, ' ') AND NVL(T2.SAMPLING_TEST, ' ') = NVL(T1.SAMPLING_TEST, ' ') AND NVL(T2.SAMPLING_COND, ' ') = NVL(T1.SAMPLING_COND, ' ') AND NVL(T2.REMARK, ' ') = NVL(T1.REMARK, ' '))");
      TDSLogger.println(sqlStmt.toString());
      PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        EditiionCompareActionForm bean = new EditiionCompareActionForm();
        bean.setSid(sid);
        bean.setProductbody(rs.getString("PRODUCT_BODY"));
        bean.setBrand(rs.getString("BRAND"));
        bean.setRoute_name(rs.getString("ROUTE_NAME"));
        bean.setStep_seq(rs.getInt("STEP_SEQ"));
        bean.setStep_name(rs.getString("STEP_NAME"));
        bean.setTest_time(rs.getInt("TEST_TIME"));
        bean.setTime_unit(rs.getString("TIME_UNIT"));
        bean.setTemperature(rs.getString("TEMPERATURE"));
        bean.setSampling_test(rs.getString("SAMPLING_TEST"));
        bean.setSampling_cond(rs.getString("SAMPLING_COND"));
        bean.setRemark(rs.getString("REMARK"));

        tmp2.add(bean);
        if (compareType == 0)
          break;
      }
      return (EditiionCompareActionForm[]) tmp2.toArray(new EditiionCompareActionForm[0]);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  /*****************************************************************
   * 取得己生效 之 Document 不同處
   *****************************************************************/
  public static EditiionCompareActionForm[] SearchReleasedDOC(String sid,
                                                              String status,
                                                              String pd_body,
                                                              String brand,
                                                              String version,
                                                              String docType) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    File sourceFile = null;
    File destinationFile = null;
    String path = TDSResource.getProperties("TIMPdf").getValue("jpg.path") + File.separator;
    String dlPath = TDSResource.getProperties("TIMPdf").getValue("jpg_dl.path") + File.separator;
    int version_be = Integer.parseInt(version) - 1;

    try {
      ArrayList tmp2 = new ArrayList();
      conn = DBConnection.getConnection();

      SelSQL.append("SELECT seq,doc_name||tf_comment full_name, doc_name FROM tf_document_linkage where " +
                    "product_body='" + pd_body +
                    "' and brand='" + brand +
                    "' and doc_name is not null and " +
                    "version='" + version_be + "'");

      if (!docType.equals(""))
        SelSQL.append(" and doc_type='"+docType+"'");
      SelSQL.append(" union SELECT seq,doc_name||tf_comment full_name, doc_name FROM tf_document_linkage where " +
                    "product_body='" + pd_body +
                    "' and brand='" + brand +
                    "' and version='" + version +
                    "' and doc_name is not null");
      if (!docType.equals(""))
        SelSQL.append(" and doc_type='"+docType+"'");
      SelSQL.append(" order by seq");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
    	TDSLogger.println(rs.getString("full_name"));

        String[] version_0 = CheckReleasedDocument(rs.getString("full_name"),
            pd_body, brand, version, docType, 0).split(",");
        String[] version_1 = CheckReleasedDocument(rs.getString("full_name"),
            pd_body, brand, version, docType, 1).split(",");

        EditiionCompareActionForm bean = new EditiionCompareActionForm();

        if (version_0[0].equals("Y") && version_1[0].equals("Y")) {
          bean.setTest_flow(rs.getString("doc_name"));
          bean.setFile_old(dlPath + version_0[1].toString());
          bean.setFile_new(dlPath + version_1[1].toString());
          bean.setPath_old(path + version_0[1].toString());
          bean.setPath_new(path + version_1[1].toString());
          bean.setTag_old("0");
          bean.setTag_new("0");
          sourceFile = new File(path + version_0[1].toString());
          destinationFile = new File(path + version_1[1].toString());
          if (FileUtil.Compare(sourceFile, destinationFile) != 0)
            tmp2.add(bean);
        } else if (version_0[0].equals("Y") &&
                   version_1[0].equals("NULL")) {
          bean.setTest_flow(rs.getString("doc_name"));
          bean.setFile_old(dlPath + version_0[1].toString());
          bean.setFile_new("");
          bean.setPath_old(path + version_0[1].toString());
          bean.setPath_new("");
          bean.setTag_old("0");
          bean.setTag_new("");
          tmp2.add(bean);
        } else if (version_0[0].equals("NULL") &&
                   version_1[0].equals("Y")) {
          bean.setTest_flow(rs.getString("doc_name"));
          bean.setFile_old("");
          bean.setFile_new(dlPath + version_1[1].toString());
          bean.setPath_old("");
          bean.setPath_new(path + version_1[1].toString());
          bean.setTag_old("");
          bean.setTag_new("0");
          tmp2.add(bean);
        }
      }
      return (EditiionCompareActionForm[]) tmp2.toArray(new EditiionCompareActionForm[0]);
    }
    catch (Exception ex) {
      ex.getStackTrace();
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  /*****************************************************************
   * 取得會簽中/處理中 之 Document 不同處
   *****************************************************************/
  public static EditiionCompareActionForm[] SearchUnreleasedDOC(String sid,
                                                                String status,
                                                                String pd_body,
                                                                String brand,
                                                                String version,
                                                                String docType) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    File sourceFile = null;
    File destinationFile = null;
    TDSProperties pdfProp = TDSResource.getProperties("TIMPdf");
    String releasePath = pdfProp.getValue("jpg.path") + File.separator;
    String txPath = pdfProp.getValue("jpg_tx.path") + File.separator;
    String dlPath = pdfProp.getValue("jpg_dl.dir");
    String txdlPath = pdfProp.getValue("jpg_tx_dl.dir");

    int version_be = Integer.parseInt(version) - 1;

    try {
      ArrayList tmp2 = new ArrayList();
      //HashMap whereStem = new HashMap();
      //whereStem.put("sid", sid);
      conn = DBConnection.getConnection();

      if (status.equals("P") || status.equals("A")) {
        //求得所有doc_name
        SelSQL.append(
            "SELECT seq,doc_name||tf_comment full_name, doc_name FROM tf_document_linkage where product_body='" +
            pd_body + "' and brand='" + brand +
            "' and doc_name is not null and version='" + version_be + "'");

        if (!docType.equals(""))
          SelSQL.append(" and doc_type='"+docType+"'");
        SelSQL.append(" union SELECT seq,doc_name||tf_comment full_name, doc_name FROM tf_document_linkage_tx " +
                      "where product_body='" + pd_body +
                      "' and brand='" + brand +
                      "' and version='" + version +
                      "' and doc_name is not null");
        if (!docType.equals(""))
          SelSQL.append(" and doc_type='"+docType+"'");
        SelSQL.append(" order by seq,doc_name");
      }

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        //a.求version-1中是否有doc_name==>SearchVersion_flag1
        //b.求version中tag=0是否有doc_name==>SearchVersion_flag2_t0
        //c.求version中tag=1是否有doc_name==>SearchVersion_flag3_t1
    	TDSLogger.println(rs.getString("full_name"));

        String[] version_1 =
            CheckReleasedDocument(rs.getString("full_name"),
                                  pd_body, brand, version, docType, 0).split(",");

        String[] version_t0 =
            CheckUnreleasedDocumentWithTag(rs.getString("full_name"),
                                           pd_body, brand, version, docType, 0).split(",");

        String[] version_t1 =
            CheckUnreleasedDocumentWithTag(rs.getString("full_name"),
                                           pd_body, brand, version, docType, 1).split(",");

        EditiionCompareActionForm bean = new EditiionCompareActionForm();

        //0==>show,1==>no show
        if (version_t0[0].equals("Y") && version_t1[0].equals("Y")) {
          bean.setTest_flow(rs.getString("doc_name"));
          bean.setTag_old("0");
          bean.setTag_new("0");
          bean.setFile_old(txdlPath + version_t0[1].toString());
          bean.setFile_new(txdlPath + version_t1[1].toString());
          bean.setPath_old(txPath + version_t0[1].toString());
          bean.setPath_new(txPath + version_t1[1].toString());
          sourceFile = new File(txPath + version_t0[1].toString());
          destinationFile = new File(txPath + version_t1[1].toString());
          if (FileUtil.Compare(sourceFile, destinationFile) != 0)
            tmp2.add(bean);
        } else if (version_t0[0].equals("Y") &&
                   version_t1[0].equals("NULL") &&
                   version_1[0].equals("NULL")) {
          bean.setTest_flow(rs.getString("doc_name"));
          bean.setTag_old("0");
          bean.setTag_new("1");
          bean.setFile_old(txdlPath + version_t0[1].toString());
          bean.setFile_new("");
          bean.setPath_old(txPath + version_t0[1].toString());
          bean.setPath_new("");
          tmp2.add(bean);
        } else if (version_t0[0].equals("NULL") &&
                   version_t1[0].equals("Y") &&
                   version_1[0].equals("Y")) {
          bean.setTest_flow(rs.getString("doc_name"));
          bean.setTag_old("0");
          bean.setTag_new("0");
          bean.setFile_old(dlPath + version_1[1].toString());
          bean.setFile_new(txdlPath + version_t1[1].toString());
          bean.setPath_old(releasePath + version_1[1].toString());
          bean.setPath_new(txPath + version_t1[1].toString());
          sourceFile = new File(releasePath + version_1[1].toString());
          destinationFile = new File(txPath + version_t1[1].toString());
          if (FileUtil.Compare(sourceFile, destinationFile) != 0)
            tmp2.add(bean);
        } else if (version_t0[0].equals("NULL") &&
                   version_t1[0].equals("Y") &&
                   version_1[0].equals("NULL")) {
          bean.setTest_flow(rs.getString("doc_name"));
          bean.setTag_old("1");
          bean.setTag_new("0");
          bean.setFile_old("");
          bean.setFile_new(txdlPath + version_t1[1].toString());
          bean.setPath_old("");
          bean.setPath_new(txPath + version_t1[1].toString());
          tmp2.add(bean);
        } else if (version_t0[0].equals("NULL") &&
                   version_t1[0].equals("NULL") &&
                   version_1[0].equals("Y")) {
          bean.setTest_flow(rs.getString("doc_name"));
          bean.setTag_old("0");
          bean.setTag_new("1");
          bean.setFile_old(dlPath + version_1[1].toString());
          bean.setFile_new("");
          bean.setPath_old(releasePath + version_1[1].toString());
          bean.setPath_new("");
          tmp2.add(bean);
        }
      }
      return (EditiionCompareActionForm[]) tmp2.toArray(new EditiionCompareActionForm[0]);
    }
    catch (Exception ex) {
      ex.getStackTrace();
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  /*****************************************************************
   * 由已生效資料 (TF_DOCUMENT_LINKAGE)，尋找 Test Flow or Yield Definition Document
   * Document Name = doc_name
   * 若 flag == 0 表示找 version - 1 版，
   * 若 flag == 1 表示找 version 這一版
   *****************************************************************/

  public static String CheckReleasedDocument(String doc_name,
                                             String pd_body,
                                             String brand,
                                             String version,
                                             String docType,
                                             int flag) {

    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    String result = "";
    int version_be = 0;
    if (flag == 0)
      version_be = Integer.parseInt(version) - 1;
    else
      version_be = Integer.parseInt(version);

    try {
      conn = DBConnection.getConnection();

      SelSQL.append(
          "SELECT * FROM tf_document_linkage where product_body='" +
          pd_body + "' and brand='" + brand +
          "' and version='" + version_be +
          "' and doc_name||tf_comment='" + doc_name + "'");

      if (!docType.equals(""))
        SelSQL.append(" and doc_type='"+docType+"'");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();
      int count = 0;
      while (rs.next()) {
        count = count + 1;
        result = "Y," + rs.getString("file_name");
      }

      if (count == 0) {
        result = "NULL,NULL";
      }
      return result;
    }
    catch (Exception ex) {
      ex.getStackTrace();
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
    return result;
  }

  /*****************************************************************
   * 由未生效資料 (TF_DOCUMENT_LINKAGE_TX)，尋找 Test Flow or Yield Definition Document
   * Document Name = doc_name
   * 若 flag == 0 表示找 tag = 0 的資料 (copy from 前一版)，
   * 若 flag == 1 表示找 tag = 1 的資料
   * 註：其實 tag == 0 即表示已生效資料，此處應可改變做法
   *****************************************************************/
  public static String CheckUnreleasedDocumentWithTag(String doc_name,
                                              String pd_body,
                                              String brand,
                                              String version,
                                              String docType,
                                              int tag) {

    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    String flag = "";

    try {
      conn = DBConnection.getConnection();

      SelSQL.append(
          "SELECT * FROM tf_document_linkage_tx where product_body='" +
          pd_body + "' and brand='" + brand +
          "' and version='" + version +
          "' and  tag=" + tag + " and doc_name||tf_comment='" + doc_name + "'");

      if (!docType.equals(""))
        SelSQL.append(" and doc_type='"+docType+"'");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();
      int count = 0;
      while (rs.next()) {
        count = count + 1;
        flag = "Y," + rs.getString("file_name");
      }

      if (count == 0) {
        flag = "NULL,NULL";
      }
      return flag;
    }
    catch (Exception ex) {
      ex.getStackTrace();
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
    return flag;
  }

  /*****************************************************************
   *主題:求得FT version-1存在但不存在version中即為刪除~~show藍色
   *****************************************************************/
  public static boolean SearchDOC1(Connection conn, String sid,
                                   String status, String pd_body,
                                   String brand, String version, String docType) {

    StringBuffer SelSQL = new StringBuffer();
    //Connection conn = null;
    boolean flag = false;
    File sourceFile = null;
    File destinationFile = null;
    TDSProperties pdfProp = TDSResource.getProperties("TIMPdf");
    String releasePath = pdfProp.getValue("jpg.path") + File.separator;
    String txPath = pdfProp.getValue("jpg_tx.path") + File.separator;

    int version_be = Integer.parseInt(version) - 1;

    try {
      //HashMap whereStem = new HashMap();
      //whereStem.put("sid", sid);
      //conn = DBConnection.getConnection();
      if (status.equals("P") || status.equals("A")) {
        //求得所有doc_name
        SelSQL.append(
            "SELECT doc_name||tf_comment full_name FROM tf_document_linkage " +
            "where product_body='" + pd_body +
            "' and brand='" + brand +
            "' and version='" + version_be +
            "' and doc_name is not null");
        if (!docType.equals(""))
          SelSQL.append( " and doc_type='"+docType+"'");
        SelSQL.append(" union SELECT doc_name||tf_comment full_name FROM tf_document_linkage_tx a " +
                      "where product_body='" + pd_body +
                      "' and brand='" + brand +
                      "' and version='" + version +
                      "' and doc_type='"+docType+
                      "' and doc_name is not null");
        if (!docType.equals(""))
          SelSQL.append( " and doc_type='"+docType+"'");

        SelSQL.append( "and not exists (select 1 from tf_document_linkage_tx b " +
            "where a.sid = b.sid " +
//先拿掉            "and b.tag = 1 " +
            "and a.doc_type = b.doc_type " +
//            "and a.seq = b.seq " +
            "and a.doc_name = b.doc_name " +
            "and a.tf_comment = b.tf_comment " +
            "and a.rowid != b.rowid) ");

        SelSQL.append(" order by full_name ");
      }

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        //a.求version-1中是否有doc_name==>SearchVersion_flag1
        //b.求version中tag=0是否有doc_name==>SearchVersion_flag2_t0
        //c.求version中tag=1是否有doc_name==>SearchVersion_flag3_t1
        String[] version_1 =
            CheckReleasedDocument(rs.getString("full_name"),
                                  pd_body, brand, version,docType, 0).split(",");
        String[] version_t0 =
            CheckUnreleasedDocumentWithTag(rs.getString("full_name"),
                                           pd_body, brand, version,docType, 0).split(",");
        String[] version_t1 =
            CheckUnreleasedDocumentWithTag(rs.getString("full_name"),
                                           pd_body, brand, version,docType, 1).split(",");

        //0==>show,1==>no show
        if (version_t0[0].equals("Y") && version_t1[0].equals("Y")) {
          sourceFile = new File(txPath + version_t0[1].toString());
          destinationFile = new File(txPath + version_t1[1].toString());
          if (FileUtil.Compare(sourceFile, destinationFile) != 0)
            flag = true;
        } else if (version_t0[0].equals("Y") &&
                   version_t1[0].equals("NULL") &&
                   version_1[0].equals("NULL")) {
          flag = true;

        } else if (version_t0[0].equals("NULL") &&
                   version_t1[0].equals("Y") &&
                   version_1[0].equals("Y")) {
          sourceFile = new File(releasePath + version_1[1].toString());
          destinationFile = new File(txPath + version_t1[1].toString());
          if (FileUtil.Compare(sourceFile, destinationFile) != 0)
            flag = true;
        } else if (version_t0[0].equals("NULL") &&
                   version_t1[0].equals("Y") &&
                   version_1[0].equals("NULL")) {
          flag = true;
        } else if (version_t0[0].equals("NULL") &&
                   version_t1[0].equals("NULL") &&
                   version_1[0].equals("Y")) {
          flag = true;
        }
        if (flag)
          break;
      }
      return flag;
    }
    catch (Exception ex) {
      ex.getStackTrace();
    }
    finally {
      //DBConnection.close(conn);
      //conn = null;
    }
    return flag;
  }

  /*****************************************************************
   * Document: 比較已生效版本之前後版是否有差異
   *****************************************************************/

  public static boolean SearchDOC2(Connection conn, String sid,
                                   String status,
                                   String pd_body,
                                   String brand,
                                   String version,
                                   String docType) {

    StringBuffer SelSQL = new StringBuffer();
    //Connection conn = null;
    boolean flag = false;
    File sourceFile = null;
    File destinationFile = null;
    String path = TDSResource.getProperties("TIMPdf").getValue("jpg.path") + File.separator;

    int version_be = Integer.parseInt(version) - 1;

    try {
      //HashMap whereStem = new HashMap();
      //whereStem.put("sid", sid);
      //conn = DBConnection.getConnection();
      SelSQL.append(
          "SELECT doc_name||tf_comment full_name FROM tf_document_linkage " +
          "where product_body='" + pd_body +
          "' and brand='" + brand +
          "' and version='" + version_be +
          "' and doc_type='"+docType+
          "'  and doc_name is not null " +
          "union SELECT doc_name||tf_comment full_name FROM tf_document_linkage " +
          "where product_body='" + pd_body +
          "' and brand='" + brand +
          "' and version='" + version +
          "' and doc_type='"+docType+
          "' and doc_name is not null order by full_name ");

      //SelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        //a.求version-1中是否有doc_name==>SearchVersion_flag1==>version_0
        //b.求version中是否有doc_name==>SearchVersion_flag1==>version_1

        String[] version_0 =
            CheckReleasedDocument(rs.getString("full_name"),
                                  pd_body, brand, version,docType, 0).split(",");
        String[] version_1 =
            CheckReleasedDocument(rs.getString("full_name"),
                                  pd_body, brand, version,docType, 1).split(",");

        //0==>show,1==>no show
        if (version_0[0].equals("Y") && version_1[0].equals("Y")) {
          sourceFile = new File(path + version_0[1].toString());
          destinationFile = new File(path + version_1[1].toString());
          if (FileUtil.Compare(sourceFile, destinationFile) != 0)
            flag = true;
        } else if (version_0[0].equals("Y") &&
                   version_1[0].equals("NULL")) {
          flag = true;
        } else if (version_0[0].equals("NULL") &&
                   version_1[0].equals("Y")) {
          flag = true;
        }
        if (flag)
          break;
      }
      return flag;
    }
    catch (Exception ex) {
      ex.getStackTrace();
    }
    finally {
      //DBConnection.close(conn);
      //conn = null;
    }
    return flag;
  }

  public static boolean isWSPDRDiff(Connection conn, String sid,
                                    String sid2,
                                    String status,
                                    String site) {

    WSProductRouteDefinitionForm[] result =
        EditiionCompareService.CompareWSPDR(conn, sid, sid2, status, site, 0);

    if ((result != null) && (result.length > 0))
      return true;
    else
      return false;
  }

  public static boolean isFTPDRDiff(Connection conn, String sid,
                                    String sid2,
                                    String status,
                                    String site) {

    FTProductRouteDefinitionForm[] result =
        EditiionCompareService.CompareFTPDR(conn, sid, sid2, status, site, 0);

    if ((result != null) && (result.length > 0))
      return true;
    else
      return false;
  }
  public static boolean isFTPDR_REDiff(Connection conn, String sid,
                                   String sid2,
                                   String status,
                                   String site) {

   FTProductReRouteDefinitionForm[] result =
       EditiionCompareService.CompareFTPDR_RE(conn, sid, sid2, status, site, 0);

   if ((result != null) && (result.length > 0))
     return true;
   else
     return false;
  }

  public static boolean isBADiff(String sid,
                                 String sid2,
                                 String status,
                                 int flag) {

    TFIMBasicActionForm[] result =
        EditiionCompareService.CompareBA(sid, sid2, status, flag, 0);
    if ((result != null) && (result.length > 0))
      return true;
    else
      return false;
  }
  public static boolean isMainRouteSubDiff(Connection conn, String sid,
                                 String sid2,
                                 String status,
                                 int flag) {

    TFIMBasicActionForm[] result =
        EditiionCompareService.CompareMainRouteSub(conn, sid, sid2, status, flag, 0);
    if ((result != null) && (result.length > 0))
      return true;
    else
      return false;
  }

  public static boolean isMainRouteReworkDiff(Connection conn, String sid,
                                 String sid2,
                                 String status,
                                 int flag) {

    TFIMBasicActionForm[] result =
        EditiionCompareService.CompareMainRouteRework(conn, sid, sid2, status, flag, 0);
    if ((result != null) && (result.length > 0))
      return true;
    else
      return false;
  }


  public static boolean isPDRDiff(String sid,
                                  String sid2,
                                  String status,
                                  int flag) {

    EditiionCompareActionForm[] result =
        EditiionCompareService.ComparePDR(sid, sid2, status, flag, 0);

    if ((result != null) && (result.length > 0))
      return true;
    else
      return false;
  }

  public static EditiionCompareActionForm[] CompareBom(Connection conn, String sid,
                                                       String sid2,
                                                       String status,
                                                       int flag,
                                                       int compareType) {

    StringBuffer sqlStmt = new StringBuffer();
    //Connection conn = null;
    String table1 = null;
    String table2 = null;
    String type_flag = null;
    int body_version_flag = 0;
    int mask_option_flag = 0;
    int mask_option_rev_flag = 0;
    int code_no_flag = 0;
    int pin_count_flag = 0;
	int package_code_flag = 0;
	int route_type_flag = 0;
	int ft_route_code_flag = 0;
	int ft_route_flag = 0;
	int ft_route_add_flag = 0;
	int ft_route_add1_flag = 0;
	int ft_route_add2_flag = 0;
	int ft_route_add3_flag = 0;
	int ft_route_add4_flag = 0;
	int ft_route_add5_flag = 0;
	int tf_comment_flag = 0;
	int sort_route_code_flag = 0;
	int ws_route_flag = 0;
	int ws_route_add_flag = 0;
	int ws_route_add1_flag = 0;
	int ws_route_add2_flag = 0;
	int ws_route_add3_flag = 0;
	int ws_route_add4_flag = 0;
	int tf_ws_comment_flag = 0;
	
	ArrayList tmp2 = new ArrayList();
    if (!status.equals("P") && !status.equals("A")) {
      table1 = "TF_BOM_ROUTE_XROM";
      table2 = "TF_BOM_ROUTE_XROM";
    } else {
      if (flag == 0) {/* 以前一版為主找出後一版之不同處 */
        table1 = "TF_BOM_ROUTE_XROM";
        table2 = "TF_BOM_ROUTE_XROM_TX";
      } else if (flag == 1)  { /* 由後一版找出前一版與其不同之處 */
        table1 = "TF_BOM_ROUTE_XROM_TX";
        table2 = "TF_BOM_ROUTE_XROM";
      } else { /* 被 update 的資料之處 */
          table1 = "TF_BOM_ROUTE_XROM_TX";
          table2 = "TF_BOM_ROUTE_XROM";  
      }
    }
    if (flag == 0){ 
		type_flag = "remove";
		body_version_flag = 0;
	    mask_option_flag = 0;
	    mask_option_rev_flag = 0;
	    code_no_flag = 0;
	    pin_count_flag = 0;
		package_code_flag = 0;
		route_type_flag = 0;
		ft_route_code_flag = 0;
		ft_route_flag = 0;
		ft_route_add_flag = 0;
		ft_route_add1_flag = 0;
		ft_route_add2_flag = 0;
		ft_route_add3_flag = 0;
		ft_route_add4_flag = 0;
		ft_route_add5_flag = 0;
		tf_comment_flag = 0;
		sort_route_code_flag = 0;
		ws_route_flag = 0;
		ws_route_add_flag = 0;
		ws_route_add1_flag = 0;
		ws_route_add2_flag = 0;
		ws_route_add3_flag = 0;
		ws_route_add4_flag = 0;
		tf_ws_comment_flag = 0;
	}else if (flag == 1){ 
		type_flag = "insert";
		body_version_flag = 1;
	    mask_option_flag = 1;
	    mask_option_rev_flag = 1;
	    code_no_flag = 1;
	    pin_count_flag = 1;
		package_code_flag = 1;
		route_type_flag = 1;
		ft_route_code_flag = 1;
		ft_route_flag = 1;
		ft_route_add_flag = 1;
		ft_route_add1_flag = 1;
		ft_route_add2_flag = 1;
		ft_route_add3_flag = 1;
		ft_route_add4_flag = 1;
		ft_route_add5_flag = 1;
		tf_comment_flag = 1;
		sort_route_code_flag = 1;
		ws_route_flag = 1;
		ws_route_add_flag = 1;
		ws_route_add1_flag = 1;
		ws_route_add2_flag = 1;
		ws_route_add3_flag = 1;
		ws_route_add4_flag = 1;
		tf_ws_comment_flag = 1;
	}else {
		body_version_flag = 0;
	    mask_option_flag = 0;
	    mask_option_rev_flag = 0;
	    code_no_flag = 0;
	    pin_count_flag = 0;
		package_code_flag = 0;
		route_type_flag = 0;
		ft_route_code_flag = 0;
		ft_route_flag = 0;
		ft_route_add_flag = 0;
		ft_route_add1_flag = 0;
		ft_route_add2_flag = 0;
		ft_route_add3_flag = 0;
		ft_route_add4_flag = 0;
		ft_route_add5_flag = 0;
		tf_comment_flag = 0;
		sort_route_code_flag = 0;
		ws_route_flag = 0;
		ws_route_add_flag = 0;
		ws_route_add1_flag = 0;
		ws_route_add2_flag = 0;
		ws_route_add3_flag = 0;
		ws_route_add4_flag = 0;
		tf_ws_comment_flag = 0;
	}

    try {
      //conn = DBConnection.getConnection();
      if (flag == 0 || flag == 1){
	      sqlStmt.append("SELECT '" + type_flag + "' TYPE_FLAG, SID, PRODUCT_BODY, BODY_VERSION, MASK_OPTION, MASK_OPTION_REV, CODE_NO, \n");
	      sqlStmt.append("PIN_COUNT, PACKAGE_CODE, ROUTE_TYPE, FT_ROUTE, SORT_ROUTE_CODE,   \n");
	      sqlStmt.append("WS_ROUTE, WS_ROUTE_ADD, WS_ROUTE_ADD1, WS_ROUTE_ADD2, WS_ROUTE_ADD3, WS_ROUTE_ADD4, FT_COMMENT, FT_ROUTE_ADD, FT_ROUTE_ADD1,  \n");
	      sqlStmt.append("FT_ROUTE_ADD2, FT_ROUTE_ADD3, FT_ROUTE_ADD4, FT_ROUTE_ADD5, FT_ROUTE_CODE, WS_COMMENT, ");
	      sqlStmt.append(body_version_flag + " BODY_VERSION_FLAG, " +  mask_option_flag + " MASK_OPTION_FLAG, " + mask_option_rev_flag + " MASK_OPTION_REV_FLAG, " + code_no_flag + " CODE_NO_FLAG, " + pin_count_flag + " PIN_COUNT_FLAG, " + package_code_flag + " PACKAGE_CODE_FLAG, \n");
	      sqlStmt.append(route_type_flag + " ROUTE_TYPE_FLAG, " + ft_route_code_flag + " FT_ROUTE_CODE_FLAG, " + ft_route_flag + " FT_ROUTE_FLAG, " + ft_route_add_flag + " FT_ROUTE_ADD_FLAG, " + ft_route_add1_flag + " FT_ROUTE_ADD1_FLAG, \n");
	      sqlStmt.append(ft_route_add2_flag + " FT_ROUTE_ADD2_FLAG, " +  ft_route_add3_flag + " FT_ROUTE_ADD3_FLAG, " + ft_route_add4_flag + " FT_ROUTE_ADD4_FLAG, " + ft_route_add5_flag + " FT_ROUTE_ADD5_FLAG, " + tf_comment_flag + " TF_COMMENT_FLAG, " + sort_route_code_flag + " SORT_ROUTE_CODE_FLAG, \n");
	      sqlStmt.append(ws_route_flag + " WS_ROUTE_FLAG, " +  ws_route_add_flag + " WS_ROUTE_ADD_FLAG, " + ws_route_add1_flag + " WS_ROUTE_ADD1_FLAG, " + ws_route_add2_flag + " WS_ROUTE_ADD2_FLAG, " + ws_route_add3_flag + " WS_ROUTE_ADD3_FLAG, " + ws_route_add4_flag + " WS_ROUTE_ADD4_FLAG, " + tf_ws_comment_flag + " TF_WS_COMMENT_FLAG \n");
	      sqlStmt.append("FROM " + table1 + " T1 \n");
	      sqlStmt.append("WHERE SID = " + sid + " \n");
	      sqlStmt.append("AND T1.TAG != 2 \n");
	      sqlStmt.append("AND NOT EXISTS (SELECT 1 FROM " + table2 + " T2 \n");
	      sqlStmt.append("WHERE T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BODY_VERSION = T1.BODY_VERSION \n");
	      sqlStmt.append("AND T2.TAG != 2 \n");
	      sqlStmt.append("AND SID = " + sid2 + " AND T2.MASK_OPTION = T1.MASK_OPTION   \n");
	      sqlStmt.append("AND T2.MASK_OPTION_REV = T1.MASK_OPTION_REV AND T2.CODE_NO = T1.CODE_NO \n");
	      sqlStmt.append("AND T2.PIN_COUNT = T1.PIN_COUNT AND T2.PACKAGE_CODE = T1.PACKAGE_CODE  AND T2.ROUTE_TYPE = T1.ROUTE_TYPE \n");
	      sqlStmt.append("AND T2.FT_ROUTE = T1.FT_ROUTE  \n");
	      sqlStmt.append("AND NVL(T2.WS_ROUTE, ' ') = NVL(T1.WS_ROUTE, ' ') AND NVL(T2.WS_ROUTE_ADD, ' ') = NVL(T1.WS_ROUTE_ADD, ' ') \n");
	      sqlStmt.append("AND NVL(T2.WS_ROUTE_ADD1, ' ') = NVL(T1.WS_ROUTE_ADD1, ' ') AND NVL(T2.WS_ROUTE_ADD2, ' ') = NVL(T1.WS_ROUTE_ADD2, ' ') \n");
	      sqlStmt.append("AND NVL(T2.WS_ROUTE_ADD3, ' ') = NVL(T1.WS_ROUTE_ADD3, ' ') AND NVL(T2.WS_ROUTE_ADD4, ' ') = NVL(T1.WS_ROUTE_ADD4, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD, ' ') = NVL(T1.FT_ROUTE_ADD, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD1, ' ') = NVL(T1.FT_ROUTE_ADD1, ' ') AND NVL(T2.FT_ROUTE_ADD2, ' ') = NVL(T1.FT_ROUTE_ADD2, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD3, ' ') = NVL(T1.FT_ROUTE_ADD3, ' ') AND NVL(T2.FT_ROUTE_ADD4, ' ') = NVL(T1.FT_ROUTE_ADD4, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD5, ' ') = NVL(T1.FT_ROUTE_ADD5, ' ')  \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_CODE, ' ') = NVL(T1.FT_ROUTE_CODE, ' ') AND T2.SORT_ROUTE_CODE = T1.SORT_ROUTE_CODE \n");
	      //sqlStmt.append("AND NVL(T2.WS_COMMENT, ' ') = NVL(T1.WS_COMMENT, ' ') AND NVL(T2.FT_COMMENT, ' ') = NVL(T1.FT_COMMENT, ' ') \n");
	      sqlStmt.append(")");
      }else{
    	  sqlStmt.append("SELECT  'old' TYPE_FLAG, T2.SID, T2.PRODUCT_BODY, T2.BODY_VERSION, T2.MASK_OPTION, T2.MASK_OPTION_REV, T2.CODE_NO, \n");
	      sqlStmt.append("T2.PIN_COUNT, T2.PACKAGE_CODE, T2.ROUTE_TYPE, T2.FT_ROUTE, T2.SORT_ROUTE_CODE, \n");
	      sqlStmt.append("T2.WS_ROUTE, T2.WS_ROUTE_ADD, T2.WS_ROUTE_ADD1, T2.WS_ROUTE_ADD2, T2.WS_ROUTE_ADD3, T2.WS_ROUTE_ADD4, T2.FT_COMMENT, T2.FT_ROUTE_ADD, T2.FT_ROUTE_ADD1,  \n");
	      sqlStmt.append("T2.FT_ROUTE_ADD2, T2.FT_ROUTE_ADD3, T2.FT_ROUTE_ADD4, T2.FT_ROUTE_ADD5, T2.FT_ROUTE_CODE, T2.WS_COMMENT, \n");
	      sqlStmt.append(body_version_flag + " BODY_VERSION_FLAG, " +  mask_option_flag + " MASK_OPTION_FLAG, " + mask_option_rev_flag + " MASK_OPTION_REV_FLAG, " + code_no_flag + " CODE_NO_FLAG, " + pin_count_flag + " PIN_COUNT_FLAG, " + package_code_flag + " PACKAGE_CODE_FLAG, \n");
	      sqlStmt.append(route_type_flag + " ROUTE_TYPE_FLAG, " + ft_route_code_flag + " FT_ROUTE_CODE_FLAG, " + ft_route_flag + " FT_ROUTE_FLAG, " + ft_route_add_flag + " FT_ROUTE_ADD_FLAG, " + ft_route_add1_flag + " FT_ROUTE_ADD1_FLAG, \n");
	      sqlStmt.append(ft_route_add2_flag + " FT_ROUTE_ADD2_FLAG, " +  ft_route_add3_flag + " FT_ROUTE_ADD3_FLAG, " + ft_route_add4_flag + " FT_ROUTE_ADD4_FLAG, " + ft_route_add5_flag + " FT_ROUTE_ADD5_FLAG, " + tf_comment_flag + " TF_COMMENT_FLAG, " + sort_route_code_flag + " SORT_ROUTE_CODE_FLAG, \n");
	      sqlStmt.append(ws_route_flag + " WS_ROUTE_FLAG, " +  ws_route_add_flag + " WS_ROUTE_ADD_FLAG, " + ws_route_add1_flag + " WS_ROUTE_ADD1_FLAG, " + ws_route_add2_flag + " WS_ROUTE_ADD2_FLAG, " + ws_route_add3_flag + " WS_ROUTE_ADD3_FLAG, " + ws_route_add4_flag + " WS_ROUTE_ADD4_FLAG, " + tf_ws_comment_flag + " TF_WS_COMMENT_FLAG \n");
	      sqlStmt.append("FROM " + table1 + " T1, " + table2 + " T2 \n");
	      sqlStmt.append("WHERE T1.SID = " + sid + " \n");
	      sqlStmt.append("AND T1.TAG != 2 \n");
	      sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BODY_VERSION = T1.BODY_VERSION \n");
	      sqlStmt.append("AND T2.TAG != 2 \n");
	      sqlStmt.append("AND T2.SID = " + sid2 + " AND T2.MASK_OPTION = T1.MASK_OPTION \n");
	      sqlStmt.append("AND T2.MASK_OPTION_REV = T1.MASK_OPTION_REV AND T2.CODE_NO = T1.CODE_NO \n");
	      sqlStmt.append("AND T2.PIN_COUNT = T1.PIN_COUNT AND T2.PACKAGE_CODE = T1.PACKAGE_CODE AND T2.ROUTE_TYPE = T1.ROUTE_TYPE \n");
	      sqlStmt.append("AND T2.FT_ROUTE = T1.FT_ROUTE  \n");
	      sqlStmt.append("AND NVL(T2.WS_ROUTE, ' ') = NVL(T1.WS_ROUTE, ' ') AND NVL(T2.WS_ROUTE_ADD, ' ') = NVL(T1.WS_ROUTE_ADD, ' ') \n");
	      sqlStmt.append("AND NVL(T2.WS_ROUTE_ADD1, ' ') = NVL(T1.WS_ROUTE_ADD1, ' ') AND NVL(T2.WS_ROUTE_ADD2, ' ') = NVL(T1.WS_ROUTE_ADD2, ' ') \n");
	      sqlStmt.append("AND NVL(T2.WS_ROUTE_ADD3, ' ') = NVL(T1.WS_ROUTE_ADD3, ' ') AND NVL(T2.WS_ROUTE_ADD4, ' ') = NVL(T1.WS_ROUTE_ADD4, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD, ' ') = NVL(T1.FT_ROUTE_ADD, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD1, ' ') = NVL(T1.FT_ROUTE_ADD1, ' ') AND NVL(T2.FT_ROUTE_ADD2, ' ') = NVL(T1.FT_ROUTE_ADD2, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD3, ' ') = NVL(T1.FT_ROUTE_ADD3, ' ') AND NVL(T2.FT_ROUTE_ADD4, ' ') = NVL(T1.FT_ROUTE_ADD4, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD5, ' ') = NVL(T1.FT_ROUTE_ADD5, ' ')  \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_CODE, ' ') != NVL(T1.FT_ROUTE_CODE, ' ') AND T2.SORT_ROUTE_CODE != T1.SORT_ROUTE_CODE \n");
	      sqlStmt.append("AND (NVL(T2.WS_COMMENT, ' ') != NVL(T1.WS_COMMENT, ' ') OR NVL(T2.FT_COMMENT, ' ') != NVL(T1.FT_COMMENT, ' ')) \n");
	      sqlStmt.append("UNION \n");
	      sqlStmt.append("SELECT  'update' TYPE_FLAG, T1.SID, T1.PRODUCT_BODY, T1.BODY_VERSION, T1.MASK_OPTION, T1.MASK_OPTION_REV, T1.CODE_NO, \n");
	      sqlStmt.append("T1.PIN_COUNT, T1.PACKAGE_CODE, T1.ROUTE_TYPE, T1.FT_ROUTE, T1.SORT_ROUTE_CODE, \n");
	      sqlStmt.append("T1.WS_ROUTE, T1.WS_ROUTE_ADD, T1.WS_ROUTE_ADD1, T1.WS_ROUTE_ADD2, T1.WS_ROUTE_ADD3, T1.WS_ROUTE_ADD4, T1.FT_COMMENT, T1.FT_ROUTE_ADD, T1.FT_ROUTE_ADD1,  \n");
	      sqlStmt.append("T1.FT_ROUTE_ADD2, T1.FT_ROUTE_ADD3, T1.FT_ROUTE_ADD4, T1.FT_ROUTE_ADD5, T1.FT_ROUTE_CODE, T1.WS_COMMENT, \n");
	      sqlStmt.append("decode(nvl(T2.BODY_VERSION, '#'),nvl(T1.BODY_VERSION, '#'),0,1) BODY_VERSION_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.MASK_OPTION, '#'),nvl(T1.MASK_OPTION, '#'),0,1) MASK_OPTION_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.MASK_OPTION_REV, '#'),nvl(T1.MASK_OPTION_REV, '#'),0,1) MASK_OPTION_REV_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.CODE_NO, '#'),nvl(T1.CODE_NO, '#'),0,1) CODE_NO_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.PIN_COUNT, -999),nvl(T1.PIN_COUNT, -999),0,1) PIN_COUNT_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.PACKAGE_CODE, '#'),nvl(T1.PACKAGE_CODE, '#'),0,1) PACKAGE_CODE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.ROUTE_TYPE, '#'),nvl(T1.ROUTE_TYPE, '#'),0,1) ROUTE_TYPE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.FT_ROUTE_CODE, '#'),nvl(T1.FT_ROUTE_CODE, '#'),0,1) FT_ROUTE_CODE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.FT_ROUTE, '#'),nvl(T1.FT_ROUTE, '#'),0,1) FT_ROUTE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.FT_ROUTE_ADD, '#'),nvl(T1.FT_ROUTE_ADD, '#'),0,1) FT_ROUTE_ADD_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.FT_ROUTE_ADD1, '#'),nvl(T1.FT_ROUTE_ADD1, '#'),0,1) FT_ROUTE_ADD1_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.FT_ROUTE_ADD2, '#'),nvl(T1.FT_ROUTE_ADD2, '#'),0,1) FT_ROUTE_ADD2_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.FT_ROUTE_ADD3, '#'),nvl(T1.FT_ROUTE_ADD3, '#'),0,1) FT_ROUTE_ADD3_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.FT_ROUTE_ADD4, '#'),nvl(T1.FT_ROUTE_ADD4, '#'),0,1) FT_ROUTE_ADD4_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.FT_ROUTE_ADD5, '#'),nvl(T1.FT_ROUTE_ADD5, '#'),0,1) FT_ROUTE_ADD5_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.FT_COMMENT, '#'),nvl(T1.FT_COMMENT, '#'),0,1) TF_COMMENT_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.SORT_ROUTE_CODE, '#'),nvl(T1.SORT_ROUTE_CODE, '#'),0,1) SORT_ROUTE_CODE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.WS_ROUTE, '#'),nvl(T1.WS_ROUTE, '#'),0,1) WS_ROUTE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.WS_ROUTE_ADD, '#'),nvl(T1.WS_ROUTE_ADD, '#'),0,1) WS_ROUTE_ADD_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.WS_ROUTE_ADD1, '#'),nvl(T1.WS_ROUTE_ADD1, '#'),0,1) WS_ROUTE_ADD1_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.WS_ROUTE_ADD2, '#'),nvl(T1.WS_ROUTE_ADD2, '#'),0,1) WS_ROUTE_ADD2_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.WS_ROUTE_ADD3, '#'),nvl(T1.WS_ROUTE_ADD3, '#'),0,1) WS_ROUTE_ADD3_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.WS_ROUTE_ADD4, '#'),nvl(T1.WS_ROUTE_ADD4, '#'),0,1) WS_ROUTE_ADD4_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.WS_COMMENT, '#'),nvl(T1.WS_COMMENT, '#'),0,1) TF_WS_COMMENT_FLAG \n");
	      sqlStmt.append("FROM " + table1 + " T1, " + table2 + " T2 \n");
	      sqlStmt.append("WHERE T1.SID = " + sid + " \n");
	      sqlStmt.append("AND T1.TAG != 2 \n");
	      sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BODY_VERSION = T1.BODY_VERSION \n");
	      sqlStmt.append("AND T2.TAG != 2 \n");
	      sqlStmt.append("AND T2.SID = " + sid2 + " AND T2.MASK_OPTION = T1.MASK_OPTION \n");
	      sqlStmt.append("AND T2.MASK_OPTION_REV = T1.MASK_OPTION_REV AND T2.CODE_NO = T1.CODE_NO \n");
	      sqlStmt.append("AND T2.PIN_COUNT = T1.PIN_COUNT AND T2.PACKAGE_CODE = T1.PACKAGE_CODE AND T2.ROUTE_TYPE = T1.ROUTE_TYPE \n");
	      sqlStmt.append("AND T2.FT_ROUTE = T1.FT_ROUTE  \n");
	      sqlStmt.append("AND NVL(T2.WS_ROUTE, ' ') = NVL(T1.WS_ROUTE, ' ') AND NVL(T2.WS_ROUTE_ADD, ' ') = NVL(T1.WS_ROUTE_ADD, ' ') \n");
	      sqlStmt.append("AND NVL(T2.WS_ROUTE_ADD1, ' ') = NVL(T1.WS_ROUTE_ADD1, ' ') AND NVL(T2.WS_ROUTE_ADD2, ' ') = NVL(T1.WS_ROUTE_ADD2, ' ') \n");
	      sqlStmt.append("AND NVL(T2.WS_ROUTE_ADD3, ' ') = NVL(T1.WS_ROUTE_ADD3, ' ') AND NVL(T2.WS_ROUTE_ADD4, ' ') = NVL(T1.WS_ROUTE_ADD4, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD, ' ') = NVL(T1.FT_ROUTE_ADD, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD1, ' ') = NVL(T1.FT_ROUTE_ADD1, ' ') AND NVL(T2.FT_ROUTE_ADD2, ' ') = NVL(T1.FT_ROUTE_ADD2, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD3, ' ') = NVL(T1.FT_ROUTE_ADD3, ' ') AND NVL(T2.FT_ROUTE_ADD4, ' ') = NVL(T1.FT_ROUTE_ADD4, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD5, ' ') = NVL(T1.FT_ROUTE_ADD5, ' ')  ");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_CODE, ' ') != NVL(T1.FT_ROUTE_CODE, ' ') AND T2.SORT_ROUTE_CODE != T1.SORT_ROUTE_CODE \n");
	      sqlStmt.append("AND (NVL(T2.WS_COMMENT, ' ') != NVL(T1.WS_COMMENT, ' ') OR NVL(T2.FT_COMMENT, ' ') != NVL(T1.FT_COMMENT, ' ')) \n");
    	  
      }

      TDSLogger.println(sqlStmt.toString());
      PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        EditiionCompareActionForm bom = new EditiionCompareActionForm();
        bom.setSid(rs.getString("SID"));
        bom.setProductbody(rs.getString("PRODUCT_BODY"));
        bom.setBody_version(rs.getString("BODY_VERSION"));
        bom.setMaskopt(rs.getString("MASK_OPTION"));
        bom.setMaskopt_rev(rs.getString("MASK_OPTION_REV"));
        bom.setCode_no(rs.getString("CODE_NO"));
        bom.setPincount(rs.getString("PIN_COUNT"));
        bom.setPkgtype(rs.getString("PACKAGE_CODE"));
        bom.setRoute_type(rs.getString("ROUTE_TYPE"));
        bom.setFtroute(rs.getString("FT_ROUTE"));
        bom.setSortroutecode(rs.getString("SORT_ROUTE_CODE"));
        bom.setWsroute(rs.getString("WS_ROUTE"));
        bom.setWsaddroute(rs.getString("WS_ROUTE_ADD"));
        bom.setWsaddroute1(rs.getString("WS_ROUTE_ADD1"));
        bom.setWsaddroute2(rs.getString("WS_ROUTE_ADD2"));
        bom.setWsaddroute3(rs.getString("WS_ROUTE_ADD3"));
        bom.setWsaddroute4(rs.getString("WS_ROUTE_ADD4"));
        bom.setComment(rs.getString("FT_COMMENT"));
        bom.setFt_route_add(rs.getString("FT_ROUTE_ADD"));
        bom.setFt_route_add1(rs.getString("FT_ROUTE_ADD1"));
        bom.setFt_route_add2(rs.getString("FT_ROUTE_ADD2"));
        bom.setFt_route_add3(rs.getString("FT_ROUTE_ADD3"));
        bom.setFt_route_add4(rs.getString("FT_ROUTE_ADD4"));
        bom.setFt_route_add5(rs.getString("FT_ROUTE_ADD5"));
        bom.setFt_route_code(rs.getString("FT_ROUTE_CODE"));
        bom.setWscomment(rs.getString("WS_COMMENT"));
        bom.setType_flag(rs.getString("TYPE_FLAG"));
        bom.setBody_version_flag(rs.getInt("BODY_VERSION_FLAG"));
        bom.setMask_option_flag(rs.getInt("MASK_OPTION_FLAG"));
        bom.setMask_option_rev_flag(rs.getInt("MASK_OPTION_REV_FLAG"));
        bom.setCode_no_flag(rs.getInt("CODE_NO_FLAG"));
        bom.setPin_count_flag(rs.getInt("PIN_COUNT_FLAG"));
        bom.setPackage_code_flag(rs.getInt("PACKAGE_CODE_FLAG"));
        bom.setRoute_type_flag(rs.getInt("ROUTE_TYPE_FLAG"));
        bom.setFt_route_code_flag(rs.getInt("FT_ROUTE_CODE_FLAG"));
        bom.setFt_route_flag(rs.getInt("FT_ROUTE_FLAG"));
        bom.setFt_route_add_flag(rs.getInt("FT_ROUTE_ADD_FLAG"));
        bom.setFt_route_add1_flag(rs.getInt("FT_ROUTE_ADD1_FLAG"));
        bom.setFt_route_add2_flag(rs.getInt("FT_ROUTE_ADD2_FLAG"));
        bom.setFt_route_add3_flag(rs.getInt("FT_ROUTE_ADD3_FLAG"));
        bom.setFt_route_add4_flag(rs.getInt("FT_ROUTE_ADD4_FLAG"));
        bom.setFt_route_add5_flag(rs.getInt("FT_ROUTE_ADD5_FLAG"));
        bom.setTf_comment_flag(rs.getInt("TF_COMMENT_FLAG"));
        bom.setSort_route_code_flag(rs.getInt("SORT_ROUTE_CODE_FLAG"));
        bom.setWs_route_flag(rs.getInt("WS_ROUTE_FLAG"));
        bom.setWs_route_add_flag(rs.getInt("WS_ROUTE_ADD_FLAG"));
        bom.setWs_route_add1_flag(rs.getInt("WS_ROUTE_ADD1_FLAG"));
        bom.setWs_route_add2_flag(rs.getInt("WS_ROUTE_ADD2_FLAG"));
        bom.setWs_route_add3_flag(rs.getInt("WS_ROUTE_ADD3_FLAG"));
        bom.setWs_route_add4_flag(rs.getInt("WS_ROUTE_ADD4_FLAG"));
        bom.setTf_ws_comment_flag(rs.getInt("TF_WS_COMMENT_FLAG"));
        tmp2.add(bom);
        if (compareType == 0)
          break;
      }
      return (EditiionCompareActionForm[]) tmp2.toArray(new EditiionCompareActionForm[0]);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //DBConnection.close(conn);
      //conn = null;
    }
    return null;
  }

  /*****************************************************************
   *主題:求得 生效版本 version-1(flag = 0) or version (flag = 1)
   *****************************************************************/

  public static String SearchVersion_flag1(String doc_name, String pd_body,
                                           String brand, String version, String docType, int flag) {

      StringBuffer SelSQL = new StringBuffer();
      Connection conn = null;
      String result = "";
      int version_be = 0;
      if (flag == 0)
          version_be = Integer.parseInt(version) - 1;
      else
              version_be = Integer.parseInt(version);

      try {
          conn = DBConnection.getConnection();

          SelSQL.append(
              "SELECT * FROM tf_document_linkage where product_body='" +
              pd_body + "' and brand='" + brand +
              "' and version='" + version_be +
              "' and doc_name='" + doc_name + "'");

          if (!docType.equals(""))
              SelSQL.append(" and doc_type='"+docType+"'");

          PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
          ResultSet rs = ps.executeQuery();
          int count = 0;
          while (rs.next()) {
              count = count + 1;
              result = "Y," + rs.getString("file_name");
          }

          if (count == 0) {
              result = "NULL,NULL";
          }
          return result;
      }
      catch (Exception ex) {
          ex.getStackTrace();
      }
      finally {
          DBConnection.close(conn);
      }
      return result;
  }

  /*****************************************************************
   *主題:求得 處理中 version  tag=0 or tag=1
   *****************************************************************/

  public static String SearchVersion_flag2_tx(String doc_name, String pd_body,
                                              String brand, String version, String docType, int tag) {

      StringBuffer SelSQL = new StringBuffer();
      Connection conn = null;
      String flag = "";

      try {
          conn = DBConnection.getConnection();

          SelSQL.append(
              "SELECT * FROM tf_document_linkage_tx where product_body='" +
              pd_body + "' and brand='" + brand +
              "' and version='" + version +
              "' and  tag=" + tag + " and doc_name='" + doc_name + "'");

          if (!docType.equals(""))
              SelSQL.append(" and doc_type='"+docType+"'");

          PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
          ResultSet rs = ps.executeQuery();
          int count = 0;
          while (rs.next()) {
              count = count + 1;
              flag = "Y," + rs.getString("file_name");
          }

          if (count == 0) {
              flag = "NULL,NULL";
          }
          return flag;
      }
      catch (Exception ex) {
          ex.getStackTrace();
      }
      finally {
          DBConnection.close(conn);
      }
      return flag;
  }

  /*****************************************************************
   *主題:求得FT version存在但不存在version-1中即為新增~~show紅色  沒有用
   *****************************************************************/
  public static EditiionCompareActionForm[] SearchDOC(String sid,
                                                      String status, String pd_body,
                                                      String brand, String version, String docType) {
      StringBuffer SelSQL = new StringBuffer();
      Connection conn = null;
      File sourceFile = null;
      File destinationFile = null;
      TDSProperties pdfProp = TDSResource.getProperties("TIMPdf");
      String releasePath = pdfProp.getValue("jpg.path") + File.separator;
      String txPath = pdfProp.getValue("jpg_tx.path") + File.separator;
      String dlPath = pdfProp.getValue("jpg_dl.dir");
      String txdlPath = pdfProp.getValue("jpg_tx_dl.dir");
      int version_be = Integer.parseInt(version) - 1;

      try {
          ArrayList tmp2 = new ArrayList();
          //HashMap whereStem = new HashMap();
          //whereStem.put("sid", sid);
          conn = DBConnection.getConnection();

          if (status.equals("P") || status.equals("A")) {
              //求得所有doc_name
              SelSQL.append(
                  "SELECT doc_name||tf_comment full_name, doc_name FROM tf_document_linkage where product_body='" +
                  pd_body + "' and brand='" + brand +
                  "' and doc_name is not null and version='" + version_be + "'");

              if (!docType.equals(""))
                  SelSQL.append(" and doc_type='"+docType+"'");
              SelSQL.append(" union SELECT doc_name||tf_comment full_name, doc_name FROM tf_document_linkage_tx where product_body='" +
                            pd_body + "' and brand='" + brand +
                            "' and version='" + version +
                            "' and doc_name is not null");
              if (!docType.equals(""))
                  SelSQL.append(" and doc_type='"+docType+"'");
              SelSQL.append(" order by full_name");
          }

          //System.out.println(SelSQL.toString());
          //System.out.println("SelSQL");
          //SelSQL.append(SQLStem.getWhereStmt(whereStem));
          PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
          ResultSet rs = ps.executeQuery();

          while (rs.next()) {
              //a.求version-1中是否有doc_name==>SearchVersion_flag1
              //b.求version中tag=0是否有doc_name==>SearchVersion_flag2_t0
              //c.求version中tag=1是否有doc_name==>SearchVersion_flag3_t1
        	  TDSLogger.println(rs.getString("full_name"));

              String[] version_1 =
                  SearchVersion_flag1(rs.getString("full_name"),
                                      pd_body, brand, version, docType, 0).split(",");

              String[] version_t0 =
                  SearchVersion_flag2_tx(rs.getString("full_name"),
                                         pd_body, brand, version, docType, 0).split(",");

              String[] version_t1 =
                  SearchVersion_flag2_tx(rs.getString("full_name"),
                                         pd_body, brand, version, docType, 1).split(",");

              EditiionCompareActionForm bean = new EditiionCompareActionForm();

              //0==>show,1==>no show
              if (version_t0[0].equals("Y") && version_t1[0].equals("Y")) {
                  bean.setTest_flow(rs.getString("doc_name"));
                  bean.setTag_old("0");
                  bean.setTag_new("0");
                  bean.setFile_old(txdlPath + version_t0[1].toString());
                  bean.setFile_new(txdlPath + version_t1[1].toString());
                  sourceFile = new File(txPath + version_t0[1].toString());
                  destinationFile = new File(txPath + version_t1[1].toString());
                  if (FileUtil.Compare(sourceFile, destinationFile) != 0)
                          tmp2.add(bean);
              } else if (version_t0[0].equals("Y") &&
                         version_t1[0].equals("NULL") &&
                         version_1[0].equals("NULL")) {
                  bean.setTest_flow(rs.getString("doc_name"));
                  bean.setTag_old("1");
                  bean.setTag_new("0");
                  bean.setFile_new(txdlPath + version_t0[1].toString());
                  bean.setFile_old("");
                  tmp2.add(bean);
              } else if (version_t0[0].equals("NULL") &&
                         version_t1[0].equals("Y") &&
                         version_1[0].equals("Y")) {
                  bean.setTest_flow(rs.getString("doc_name"));
                  bean.setTag_old("0");
                  bean.setTag_new("0");
                  bean.setFile_old(dlPath + version_1[1].toString());
                  bean.setFile_new(txdlPath + version_t1[1].toString());
                  sourceFile = new File(releasePath + version_1[1].toString());
                  destinationFile = new File(txPath + version_t1[1].toString());
                  if (FileUtil.Compare(sourceFile, destinationFile) != 0)
                          tmp2.add(bean);
              } else if (version_t0[0].equals("NULL") &&
                         version_t1[0].equals("Y") &&
                         version_1[0].equals("NULL")) {
                  bean.setTest_flow(rs.getString("doc_name"));
                  bean.setTag_old("1");
                  bean.setTag_new("0");
                  bean.setFile_old("");
                  bean.setFile_new(txdlPath + version_t1[1].toString());
                  tmp2.add(bean);
              } else if (version_t0[0].equals("NULL") &&
                         version_t1[0].equals("NULL") &&
                         version_1[0].equals("Y")) {
                  bean.setTest_flow(rs.getString("doc_name"));
                  bean.setTag_old("0");
                  bean.setTag_new("1");
                  bean.setFile_old(dlPath + version_1[1].toString());
                  bean.setFile_new("");
                  tmp2.add(bean);
              }
          }
          return (EditiionCompareActionForm[]) tmp2.toArray(new EditiionCompareActionForm[0]);
      }
      catch (Exception ex) {
          ex.getStackTrace();
      }
      finally {
          DBConnection.close(conn);
      }
      return null;
  }

  public static void main(String[] args) {
    EditiionCompareService editiionCompareService = new EditiionCompareService();
  }
}