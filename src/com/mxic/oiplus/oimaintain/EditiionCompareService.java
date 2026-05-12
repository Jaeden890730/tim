package com.mxic.oiplus.oimaintain;

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
  
  public static boolean isBomMcpDiff(Connection conn, String sid,
	          String sid2,
	          String status,
	          int flag) {
	EditiionCompareActionForm[] result =
	EditiionCompareService.CompareBomMcp(conn, sid, sid2, status, flag, 0);
	
	if ((result != null) && (result.length > 0))
		return true;
	else
		return false;
  }  
  
  // 該功能包含檢查WS/FT/PCB三項
  public static String ComparePDR2BRAND(String sid, String prodbody, String brand, String version) {
      Connection conn = null;;
      String s = "";
      String s_tmp = "";
      
      try {
          conn = DBConnection.getConnection();
          s_tmp =  EditiionCompareService.CompareWSPDR2BRAND(conn, sid, prodbody, brand, version);
          if(s_tmp.length() > 0) {
              s += "WS:" + s_tmp;
          }
         
          s_tmp =  EditiionCompareService.CompareFTPDR2BRAND(conn, sid, prodbody, brand, version);
          if(s_tmp.length() > 0) {
              s += "FT:" + s_tmp;
          }
          
          
          s_tmp = EditiionCompareService.CompareAVIPDR2BRAND(conn, sid, prodbody, brand, version);
          if(s_tmp.length() > 0) {
              s += "AVI:" + s_tmp;
          }
         
          return s;

      } catch (Exception e) {
          e.printStackTrace();
      } finally {
          DBConnection.close(conn);
          conn = null;
      }
      return "";
  }

  public static String CompareWSPDR2BRAND(Connection conn, String sid, String prodbody, String brand, String version) {

      String sid_pre = OiMaintainService.getPreviousVersionSid(conn, sid);
      String sid_otherbrand = OiMaintainService.getSidRelease(prodbody, brand.equals("MX") ? "KH" : "MX");
      if (sid_otherbrand == null) {
          return "";
      }
      WSProductRouteDefinitionForm[] result = EditiionCompareService.CompareWSPDR(conn, sid, sid_pre, "A", null, 1);
      ArrayList sids_cur = new ArrayList();
      String sids_otherbrand_s = "";
      ArrayList sids_otherbrand = new ArrayList();

      if (result != null && result.length > 0 && result[0] != null) {

          for (int i = 0; i < result.length; i++) {
              sids_cur.add(result[i].getPgm_id());
          }

          try {

              WsTestBean[] wtb = OiMaintainService.SelectAllFromAll(sid_otherbrand, conn, null, null, null, "ws");

              if (wtb != null && wtb.length > 0 && wtb[0] != null) {
                  for (int j = 0; j < wtb.length; j++) {
                      if (sids_cur.contains(wtb[j].getPgm_id())) {
                          sids_otherbrand.add(wtb[j].getPgm_id());
                          sids_otherbrand_s += wtb[j].getPgm_id() + ",";
                      }
                  }
              }

          } catch (Exception e) {
              e.printStackTrace();
          } finally {

          }
      }
      return sids_otherbrand_s;

  }

  public static String CompareFTPDR2BRAND(Connection conn, String sid, String prodbody, String brand, String version) {

      String sid_pre = OiMaintainService.getPreviousVersionSid(conn, sid);
      String sid_otherbrand = OiMaintainService.getSidRelease(prodbody, brand.equals("MX") ? "KH" : "MX");
      if (sid_otherbrand == null) {
          return "";
      }
      FTProductRouteDefinitionForm[] result = EditiionCompareService.CompareFTPDR(conn, sid, sid_pre, "A", null, 1);
      ArrayList sids_cur = new ArrayList();
      String sids_otherbrand_s = "";
      ArrayList sids_otherbrand = new ArrayList();

      if (result != null && result.length > 0 && result[0] != null) {

          for (int i = 0; i < result.length; i++) {
              if(!sids_cur.contains(result[i].getPgm_id()))
                  sids_cur.add(result[i].getPgm_id());
          }

          try {

              WsTestBean[] wtb = OiMaintainService.SelectAllFromAll(sid_otherbrand, conn, null, null, null, "ft");

              if (wtb != null && wtb.length > 0 && wtb[0] != null) {
                  for (int j = 0; j < wtb.length; j++) {
                      if (sids_cur.contains(wtb[j].getPgm_id())) {
                          sids_otherbrand.add(wtb[j].getPgm_id());
                          sids_otherbrand_s += wtb[j].getPgm_id() + ",";
                      }
                  }
              }

          } catch (Exception e) {
              e.printStackTrace();
          } finally {

          }
      }
      return sids_otherbrand_s;

  }
  public static String CompareAVIPDR2BRAND(Connection conn, String sid, String prodbody, String brand, String version) {

      String sid_pre = OiMaintainService.getPreviousVersionSid(conn, sid);
      String sid_otherbrand = OiMaintainService.getSidRelease(prodbody, brand.equals("MX") ? "KH" : "MX");
      if (sid_otherbrand == null) {
          return "";
      }
      FTProductRouteDefinitionForm[] result = EditiionCompareService.CompareAVIPDR(conn, sid, sid_pre, "A", null, 1);
      ArrayList sids_cur = new ArrayList();
      String sids_otherbrand_s = "";
      ArrayList sids_otherbrand = new ArrayList();

      if (result != null && result.length > 0 && result[0] != null) {

          for (int i = 0; i < result.length; i++) {
              sids_cur.add(result[i].getPgm_id());
          }

          try {

              WsTestBean[] wtb = OiMaintainService.SelectAllFromAll(sid_otherbrand, conn, null, null, null, "pbc");

              if (wtb != null && wtb.length > 0 && wtb[0] != null) {
                  for (int j = 0; j < wtb.length; j++) {
                      if (sids_cur.contains(wtb[j].getPgm_id())) {
                          sids_otherbrand.add(wtb[j].getPgm_id());
                          sids_otherbrand_s += wtb[j].getPgm_id() + ",";
                      }
                  }
              }

          } catch (Exception e) {
              e.printStackTrace();
          } finally {

          }
      }
      return sids_otherbrand_s;

  }

  public static WSProductRouteDefinitionForm[] CompareWSPDR(Connection conn, 
		                                                    String sid,
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
        sqlStmt.append("call TF_COMPARE_WS_PRODUCT_ROUTE ( " + sid + ", '" + status + "', " + sid2 + ", '" + site + "') ");
      else
        sqlStmt.append("call TF_COMPARE_WS_PRODUCT_ROUTE ( " + sid + ", '" + status + "', " + sid2 + ", null) ");
      CallableStatement stmt = conn.prepareCall(sqlStmt.toString());
      stmt.execute();

      sqlStmt = new StringBuffer();

      //sqlStmt.append("SELECT ORD, TAG, PRODUCT_BODY, MASK_OPTION, DB_WITH_CODE, SORT_ROUTE_CODE, ");
      sqlStmt.append("SELECT distinct ORD, TAG, PRODUCT_BODY, MASK_OPTION, DB_WITH_CODE, ");
      sqlStmt.append("WS_ROUTE, TEST_MODE, TESTER, SITE, PGM_ID, PROGRAM_NAME, TEMPERATURE, ");
      sqlStmt.append("NVL(TF_COMMENT, '') TF_COMMENT, NVL(TF_WS_COMMENT, '') TF_WS_COMMENT, ROUTE_TYPE, SALES_FORM, WS_ROUTE_ADD, HW_CONFIGURE, PGM_SPECIAL_CONTROL,  ");
      sqlStmt.append("ROUTE_TYPE_FLAG, WS_ROUTE_FLAG, WS_ROUTE_ADD_FLAG, TEST_MODE_FLAG, TESTER_FLAG, SITE_FLAG, PGM_ID_FLAG,PROGRAM_NAME_FLAG,  ");
      sqlStmt.append("TEMPERATURE_FLAG, TF_COMMENT_FLAG, TF_WS_COMMENT_FLAG, HW_CONFIGURE_FLAG, PGM_SPECIAL_CONTROL_FLAG  ");
      sqlStmt.append("FROM TF_CMP_03 ORDER BY ord,product_body,mask_option,db_with_code,ws_route,ws_route_add,route_type desc,test_mode,tester,site,TAG  ");

      PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        WSProductRouteDefinitionForm ws = new WSProductRouteDefinitionForm();
        ws.setOrd(rs.getInt("ORD"));
        ws.setTag(rs.getString("TAG"));
        ws.setProductBody(rs.getString("PRODUCT_BODY"));
        ws.setMaskOption(rs.getString("MASK_OPTION"));
        ws.setDbWithCode(rs.getString("DB_WITH_CODE"));
        //ws.setSortRouteCode(rs.getString("SORT_ROUTE_CODE"));
        ws.setWsRoute(rs.getString("WS_ROUTE"));
        ws.setTestMode(rs.getString("TEST_MODE"));
        ws.setTester(rs.getString("TESTER"));
        ws.setSite(rs.getString("SITE"));
        ws.setPgm_id(rs.getString("PGM_ID"));
        ws.setProgramName(rs.getString("PROGRAM_NAME"));
        ws.setTemperature(rs.getString("TEMPERATURE"));
        ws.setTfComment(rs.getString("TF_COMMENT"));
        ws.setRoute_type(rs.getString("ROUTE_TYPE"));
        ws.setTfWSComment(rs.getString("TF_WS_COMMENT"));
        ws.setSales_form(rs.getString("SALES_FORM"));
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
    
    ArrayList tmp2 = new ArrayList();
    try {
      //conn = DBConnection.getConnection();
      String productType = OiMaintainService.getProductType(conn, sid);
      if ((site != null) && (site.length() > 0))
    	if (productType.equals("MROM")) { // mrom always direct to NVM ??? 20090205, not fixed, temp solution
          sqlStmt.append("call TF_COMPARE_FT_PRODUCT_ROUTE_M ( " + sid + ", '" + status + "', " + sid2 + ", '" + site + "') ");
          sqlStmt2.append("call TF_COMPARE_PBC_PRODUCT_ROUTE_M ( " + sid + ", '" + status + "', " + sid2 + ", '" + site + "') ");
    	}
    	else {
    	  sqlStmt.append("call TF_COMPARE_FT_PRODUCT_ROUTE ( " + sid + ", '" + status + "', " + sid2 + ", '" + site + "') ");
    	  sqlStmt2.append("call TF_COMPARE_PBC_PRODUCT_ROUTE ( " + sid + ", '" + status + "', " + sid2 + ", '" + site + "') ");
    	}
      else
      	if (productType.equals("MROM")) {
          sqlStmt.append("call TF_COMPARE_FT_PRODUCT_ROUTE_M ( " + sid + ", '" + status + "', " + sid2 + ", null) ");
          sqlStmt2.append("call TF_COMPARE_PBC_PRODUCT_ROUTE_M ( " + sid + ", '" + status + "', " + sid2 + ", null) ");
      	}
      	else {
          sqlStmt.append("call TF_COMPARE_FT_PRODUCT_ROUTE ( " + sid + ", '" + status + "', " + sid2 + ", null) ");
          sqlStmt2.append("call TF_COMPARE_PBC_PRODUCT_ROUTE ( " + sid + ", '" + status + "', " + sid2 + ", null) ");
      	}

      CallableStatement stmt = conn.prepareCall(sqlStmt.toString());
      stmt.execute();
      stmt = conn.prepareCall(sqlStmt2.toString());
      stmt.execute();

      sqlStmt = new StringBuffer();
      //sqlStmt.append("SELECT ORD, TAG, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
      sqlStmt.append("SELECT distinct ORD, TAG, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
      //sqlStmt.append("BACKEND_OPTION, FG_WITH_CODE, FT_ROUTE_CODE, FT_ROUTE, TEST_MODE, ");
      sqlStmt.append("BACKEND_OPTION, FG_WITH_CODE, FT_ROUTE, TEST_MODE, ");
      sqlStmt.append("TESTER, SITE,PGM_ID, PROGRAM_NAME, I_GRADE, C_GRADE,W_GRADE,Y_GRADE,J_GRADE,K_GRADE,L_GRADE,N_GRADE,B_GRADE,E_GRADE, S_GRADE, BODY_SIZE, TF_COMMENT, TF_FT_COMMENT, ROUTE_TYPE, ACTUAL_FILE, PGM_SPECIAL_CONTROL, SALES_FORM, FT_ROUTE_ADD, HW_CONFIGURE, ");
      sqlStmt.append("ROUTE_TYPE_FLAG, FT_ROUTE_FLAG, FT_ROUTE_ADD_FLAG, TEST_MODE_FLAG, PACKAGE_CODE_FLAG, PGM_ID_FLAG, PACKAGE_NAME_FLAG, PIN_COUNT_FLAG, I_GRADE_FLAG, C_GRADE_FLAG, W_GRADE_FLAG, Y_GRADE_FLAG,J_GRADE_FLAG, K_GRADE_FLAG,L_GRADE_FLAG,N_GRADE_FLAG,B_GRADE_FLAG,E_GRADE_FLAG, S_GRADE_FLAG, BODY_SIZE_FLAG, TESTER_FLAG, SITE_FLAG, PROGRAM_NAME_FLAG,  ");
      sqlStmt.append("ACTUAL_FILE_FLAG, PGM_SPECIAL_CONTROL_FLAG, HW_CONFIGURE_FLAG, TF_COMMENT_FLAG, TF_FT_COMMENT_FLAG   ");
      sqlStmt.append("FROM TF_CMP_13 ");
      sqlStmt.append("UNION ");
	  //sqlStmt.append("SELECT ORD, TAG, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
      sqlStmt.append("SELECT distinct ORD, TAG, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
	  //sqlStmt.append("BACKEND_OPTION, FG_WITH_CODE, FT_ROUTE_CODE, FT_ROUTE, TEST_MODE, ");
      sqlStmt.append("BACKEND_OPTION, FG_WITH_CODE, FT_ROUTE, TEST_MODE, ");
	  sqlStmt.append("TESTER, SITE,PGM_ID, PROGRAM_NAME, I_GRADE, C_GRADE,W_GRADE,Y_GRADE,J_GRADE,K_GRADE,L_GRADE,N_GRADE,B_GRADE,E_GRADE, S_GRADE, BODY_SIZE, TF_COMMENT, TF_FT_COMMENT, ROUTE_TYPE, ACTUAL_FILE, PGM_SPECIAL_CONTROL, SALES_FORM, FT_ROUTE_ADD, HW_CONFIGURE, ");
	  sqlStmt.append("ROUTE_TYPE_FLAG, FT_ROUTE_FLAG, FT_ROUTE_ADD_FLAG, TEST_MODE_FLAG, PACKAGE_CODE_FLAG, PGM_ID_FLAG,  PACKAGE_NAME_FLAG, PIN_COUNT_FLAG, I_GRADE_FLAG, C_GRADE_FLAG,W_GRADE_FLAG, Y_GRADE_FLAG,J_GRADE_FLAG, K_GRADE_FLAG,L_GRADE_FLAG,N_GRADE_FLAG,B_GRADE_FLAG,E_GRADE_FLAG, S_GRADE_FLAG, BODY_SIZE_FLAG, TESTER_FLAG, SITE_FLAG, PROGRAM_NAME_FLAG,  ");
      sqlStmt.append("ACTUAL_FILE_FLAG, PGM_SPECIAL_CONTROL_FLAG, HW_CONFIGURE_FLAG, TF_COMMENT_FLAG, TF_FT_COMMENT_FLAG   ");
      sqlStmt.append("FROM TF_CMP_23 ");
      sqlStmt.append("ORDER BY ord,product_body,backend_option,package_code,pin_count,fg_with_code,ft_route,ft_route_add,route_type desc,test_mode,tester,site,TAG ");

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
        ft.setFgWithCode(rs.getString("FG_WITH_CODE"));
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
        ft.setW_Grade(rs.getString("W_GRADE"));
        ft.setY_Grade(rs.getString("Y_GRADE"));
        ft.setJ_Grade(rs.getString("J_GRADE"));
        ft.setK_Grade(rs.getString("K_GRADE"));
        ft.setL_Grade(rs.getString("L_GRADE"));
        ft.setN_Grade(rs.getString("N_GRADE"));
        ft.setB_Grade(rs.getString("B_GRADE"));
        ft.setE_Grade(rs.getString("E_GRADE"));
        ft.setS_Grade(rs.getString("S_GRADE"));
        ft.setBodySize(rs.getString("BODY_SIZE"));
        ft.setRoute_type(rs.getString("ROUTE_TYPE"));
        ft.setTfComment(rs.getString("TF_COMMENT"));
        ft.setFtComment(rs.getString("TF_FT_COMMENT"));
		ft.setSales_form(rs.getString("SALES_FORM"));
        ft.setFtRouteAdd(rs.getString("FT_ROUTE_ADD"));
        ft.setHw_configure(rs.getString("HW_CONFIGURE"));
        ft.setRoute_type_flag(rs.getInt("ROUTE_TYPE_FLAG"));
        ft.setFt_route_flag(rs.getInt("FT_ROUTE_FLAG"));
        ft.setFt_route_add_flag(rs.getInt("FT_ROUTE_ADD_FLAG"));
        ft.setTest_mode_flag(rs.getInt("TEST_MODE_FLAG"));
        ft.setPackage_code_flag(rs.getInt("PACKAGE_CODE_FLAG"));
        ft.setPackage_name_flag(rs.getInt("PACKAGE_NAME_FLAG"));
        ft.setPin_count_flag(rs.getInt("PIN_COUNT_FLAG"));
        ft.setI_grade_flag(rs.getInt("I_GRADE_FLAG"));
        ft.setC_grade_flag(rs.getInt("C_GRADE_FLAG"));
        ft.setW_grade_flag(rs.getInt("W_GRADE_FLAG"));
        ft.setY_grade_flag(rs.getInt("Y_GRADE_FLAG"));
        ft.setJ_grade_flag(rs.getInt("J_GRADE_FLAG"));
        ft.setK_grade_flag(rs.getInt("K_GRADE_FLAG"));
        ft.setL_grade_flag(rs.getInt("L_GRADE_FLAG"));
        ft.setN_grade_flag(rs.getInt("N_GRADE_FLAG"));
        ft.setB_grade_flag(rs.getInt("B_GRADE_FLAG"));
        ft.setE_grade_flag(rs.getInt("E_GRADE_FLAG"));
        ft.setS_grade_flag(rs.getInt("S_GRADE_FLAG"));
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

  public static FTProductRouteDefinitionForm[] CompareFTPDRMcp(Connection conn, String sid,
          String sid2,
          String status,
          String site,
          int compareType) {

		StringBuffer sqlStmt = new StringBuffer();
		StringBuffer sqlStmt2 = new StringBuffer();
		//Connection conn = null;
		
		ArrayList tmp2 = new ArrayList();
		try {
			//conn = DBConnection.getConnection();
			//String productType = OiMaintainService.getProductType(conn, sid);
			if ((site != null) && (site.length() > 0)){
				sqlStmt.append("call TF_COMPARE_FT_PROD_ROUTE_MCP ( " + sid + ", '" + status + "', " + sid2 + ", '" + site + "') ");
				sqlStmt2.append("call TF_COMPARE_PBC_PROD_ROUTE_MCP ( " + sid + ", '" + status + "', " + sid2 + ", '" + site + "') ");
			}else{
				sqlStmt.append("call TF_COMPARE_FT_PROD_ROUTE_MCP ( " + sid + ", '" + status + "', " + sid2 + ", null) ");
				sqlStmt2.append("call TF_COMPARE_PBC_PROD_ROUTE_MCP ( " + sid + ", '" + status + "', " + sid2 + ", null) ");				
			}
			
			CallableStatement stmt = conn.prepareCall(sqlStmt.toString());
			stmt.execute();
			stmt = conn.prepareCall(sqlStmt2.toString());
			stmt.execute();
			
			sqlStmt = new StringBuffer();
			//sqlStmt.append("SELECT ORD, TAG, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
			sqlStmt.append("SELECT distinct ORD, TAG, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
			//sqlStmt.append("BACKEND_OPTION, FG_WITH_CODE, FT_ROUTE_CODE, FT_ROUTE, TEST_MODE, ");
			sqlStmt.append("BACKEND_OPTION, FG_WITH_CODE, FT_ROUTE, TEST_MODE, ");
			sqlStmt.append("TESTER, SITE,PGM_ID, PROGRAM_NAME, I_GRADE, C_GRADE,W_GRADE,Y_GRADE,J_GRADE,K_GRADE,L_GRADE,N_GRADE,B_GRADE,E_GRADE, S_GRADE, BODY_SIZE, TF_COMMENT, TF_FT_COMMENT, ROUTE_TYPE, ACTUAL_FILE, PGM_SPECIAL_CONTROL, SALES_FORM, FT_ROUTE_ADD, HW_CONFIGURE, ");
			sqlStmt.append("ROUTE_TYPE_FLAG, FT_ROUTE_FLAG, FT_ROUTE_ADD_FLAG, TEST_MODE_FLAG, PACKAGE_CODE_FLAG, PGM_ID_FLAG, PACKAGE_NAME_FLAG, PIN_COUNT_FLAG, I_GRADE_FLAG, C_GRADE_FLAG, W_GRADE_FLAG, Y_GRADE_FLAG,J_GRADE_FLAG, K_GRADE_FLAG,L_GRADE_FLAG,N_GRADE_FLAG,B_GRADE_FLAG,E_GRADE_FLAG, S_GRADE_FLAG, BODY_SIZE_FLAG, TESTER_FLAG, SITE_FLAG, PROGRAM_NAME_FLAG,  ");
			sqlStmt.append("ACTUAL_FILE_FLAG, PGM_SPECIAL_CONTROL_FLAG, HW_CONFIGURE_FLAG, TF_COMMENT_FLAG, TF_FT_COMMENT_FLAG   ");
			sqlStmt.append("FROM TF_CMP_13 ");
			sqlStmt.append("UNION ");
			//sqlStmt.append("SELECT ORD, TAG, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
			sqlStmt.append("SELECT distinct ORD, TAG, PRODUCT_BODY, PACKAGE_CODE, PACKAGE_NAME, PIN_COUNT, ");
			//sqlStmt.append("BACKEND_OPTION, FG_WITH_CODE, FT_ROUTE_CODE, FT_ROUTE, TEST_MODE, ");
			sqlStmt.append("BACKEND_OPTION, FG_WITH_CODE, FT_ROUTE, TEST_MODE, ");
			sqlStmt.append("TESTER, SITE,PGM_ID, PROGRAM_NAME, I_GRADE, C_GRADE,W_GRADE,Y_GRADE,J_GRADE,K_GRADE,L_GRADE,N_GRADE,B_GRADE,E_GRADE, S_GRADE, BODY_SIZE, TF_COMMENT, TF_FT_COMMENT, ROUTE_TYPE, ACTUAL_FILE, PGM_SPECIAL_CONTROL, SALES_FORM, FT_ROUTE_ADD, HW_CONFIGURE, ");
			sqlStmt.append("ROUTE_TYPE_FLAG, FT_ROUTE_FLAG, FT_ROUTE_ADD_FLAG, TEST_MODE_FLAG, PACKAGE_CODE_FLAG, PGM_ID_FLAG,  PACKAGE_NAME_FLAG, PIN_COUNT_FLAG, I_GRADE_FLAG, C_GRADE_FLAG,W_GRADE_FLAG, Y_GRADE_FLAG,J_GRADE_FLAG, K_GRADE_FLAG,L_GRADE_FLAG,N_GRADE_FLAG,B_GRADE_FLAG,E_GRADE_FLAG, S_GRADE_FLAG, BODY_SIZE_FLAG, TESTER_FLAG, SITE_FLAG, PROGRAM_NAME_FLAG,  ");
			sqlStmt.append("ACTUAL_FILE_FLAG, PGM_SPECIAL_CONTROL_FLAG, HW_CONFIGURE_FLAG, TF_COMMENT_FLAG, TF_FT_COMMENT_FLAG   ");
			sqlStmt.append("FROM TF_CMP_23 ");
			sqlStmt.append("ORDER BY ord,product_body,backend_option,package_code,pin_count,fg_with_code,ft_route,ft_route_add,route_type desc,test_mode,tester,site,TAG ");
			
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
				ft.setFgWithCode(rs.getString("FG_WITH_CODE"));
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
				ft.setW_Grade(rs.getString("W_GRADE"));
				ft.setY_Grade(rs.getString("Y_GRADE"));
				ft.setJ_Grade(rs.getString("J_GRADE"));
				ft.setK_Grade(rs.getString("K_GRADE"));
				ft.setL_Grade(rs.getString("L_GRADE"));
				ft.setN_Grade(rs.getString("N_GRADE"));
				ft.setB_Grade(rs.getString("B_GRADE"));
				ft.setE_Grade(rs.getString("E_GRADE"));
				ft.setS_Grade(rs.getString("S_GRADE"));
				ft.setBodySize(rs.getString("BODY_SIZE"));
				ft.setRoute_type(rs.getString("ROUTE_TYPE"));
				ft.setTfComment(rs.getString("TF_COMMENT"));
				ft.setFtComment(rs.getString("TF_FT_COMMENT"));
				ft.setSales_form(rs.getString("SALES_FORM"));
				ft.setFtRouteAdd(rs.getString("FT_ROUTE_ADD"));
				ft.setHw_configure(rs.getString("HW_CONFIGURE"));
				ft.setRoute_type_flag(rs.getInt("ROUTE_TYPE_FLAG"));
				ft.setFt_route_flag(rs.getInt("FT_ROUTE_FLAG"));
				ft.setFt_route_add_flag(rs.getInt("FT_ROUTE_ADD_FLAG"));
				ft.setTest_mode_flag(rs.getInt("TEST_MODE_FLAG"));
				ft.setPackage_code_flag(rs.getInt("PACKAGE_CODE_FLAG"));
				ft.setPackage_name_flag(rs.getInt("PACKAGE_NAME_FLAG"));
				ft.setPin_count_flag(rs.getInt("PIN_COUNT_FLAG"));
				ft.setI_grade_flag(rs.getInt("I_GRADE_FLAG"));
				ft.setC_grade_flag(rs.getInt("C_GRADE_FLAG"));
				ft.setW_grade_flag(rs.getInt("W_GRADE_FLAG"));
				ft.setY_grade_flag(rs.getInt("Y_GRADE_FLAG"));
				ft.setJ_grade_flag(rs.getInt("J_GRADE_FLAG"));
				ft.setK_grade_flag(rs.getInt("K_GRADE_FLAG"));
				ft.setL_grade_flag(rs.getInt("L_GRADE_FLAG"));
				ft.setN_grade_flag(rs.getInt("N_GRADE_FLAG"));
				ft.setB_grade_flag(rs.getInt("B_GRADE_FLAG"));
				ft.setE_grade_flag(rs.getInt("E_GRADE_FLAG"));
				ft.setS_grade_flag(rs.getInt("S_GRADE_FLAG"));
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
  
  public static FTProductRouteDefinitionForm[] CompareAVIPDR(Connection conn, String sid,
		  String sid2,
		  String status,
		  String site,
		  int compareType) {

	  StringBuffer sqlStmt = new StringBuffer();
	  //Connection conn = null;
	  //String productType = OiMaintainService.getProductType(sid);

	  ArrayList tmp2 = new ArrayList();
	  try {
		  //conn = DBConnection.getConnection();
		  if ((site != null) && (site.length() > 0))
				  sqlStmt.append("call TF_COMPARE_AVI ( " + sid + ", '" + status + "', " + sid2 + ", '" + site + "') ");
		  else
				  sqlStmt.append("call TF_COMPARE_AVI ( " + sid + ", '" + status + "', " + sid2 + ", null) ");

		  CallableStatement stmt = conn.prepareCall(sqlStmt.toString());
		  stmt.execute();

		  sqlStmt = new StringBuffer();
		  sqlStmt.append("SELECT TAG, SITE FROM TF_CMP_23 ORDER BY site,TAG DESC ");

		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();
		  while (rs.next()) {
			  FTProductRouteDefinitionForm ft = new FTProductRouteDefinitionForm();
			  ft.setTag(rs.getString("TAG"));
			  ft.setSite(rs.getString("SITE"));
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

  public static TFIMBasicActionForm[] CompareBA (Connection conn, String sid,
                                                 String sid2,
                                                 String status,
                                                 int flag,
                                                 int compareType) {

    StringBuffer sqlStmt = new StringBuffer();
    //Connection conn = null;
    String table1 = null;
    String table2 = null;
    String type_flag = null;
    int tester_flag = 0;
	int options_flag = 0;
	int grade_flag = 0;
	int good_bin_flag = 0;
	int ib_bin_flag = 0;
	int bin_type_str_flag = 0;
	int inkless_grade_flag = 0;
	int ktd_bin_flag_flag = 0;
	int ipn_action_str_flag = 0;
	int epn_speed_flag = 0;
	int test_speed_flag = 0;
	int down_grade_flag = 0;
	int remark_flag = 0;

    ArrayList tmp2 = new ArrayList();
    if (!status.equals("P") && !status.equals("A")) {
      table1 = "TF_BASIC_INFO_VW";
      table2 = "TF_BASIC_INFO_VW";
    } else {
      if (flag == 0) {/* 以前一版為主找出後一版之不同處 */
        table1 = "TF_BASIC_INFO_VW";
        table2 = "TF_BASIC_INFO_TX_VW";
      } else if (flag == 1) { /* 由後一版找出前一版與其不同之處 */
        table1 = "TF_BASIC_INFO_TX_VW";
        table2 = "TF_BASIC_INFO_VW";
      } else { /* 被 update 的資料之處 */
          table1 = "TF_BASIC_INFO_TX_VW";
          table2 = "TF_BASIC_INFO_VW";  
      }
    }
    if (flag == 0){ 
		type_flag = "remove";
		tester_flag = 0;
		options_flag = 0;
		grade_flag = 0;
		good_bin_flag = 0;
		ib_bin_flag = 0;
		bin_type_str_flag = 0;
		inkless_grade_flag = 0;
		ktd_bin_flag_flag = 0;
		ipn_action_str_flag = 0;
		epn_speed_flag = 0;
		test_speed_flag = 0;
		down_grade_flag = 0;
		remark_flag = 0;
	}else if (flag == 1){ 
		type_flag = "insert";
		tester_flag = 1;
		options_flag = 1;
		grade_flag = 1;
		good_bin_flag = 1;
		ib_bin_flag = 1;
		bin_type_str_flag = 1;
		inkless_grade_flag = 1;
		ktd_bin_flag_flag = 1;
		ipn_action_str_flag = 1;
		epn_speed_flag = 1;
		test_speed_flag = 1;
		down_grade_flag = 1;
		remark_flag = 1;
	}else {
		tester_flag = 0;
		options_flag = 0;
		grade_flag = 0;
		good_bin_flag = 0;
		ib_bin_flag = 0;
		bin_type_str_flag = 0;
		inkless_grade_flag = 0;
		ktd_bin_flag_flag = 0;
		ipn_action_str_flag = 0;
		epn_speed_flag = 0;
		test_speed_flag = 0;
		down_grade_flag = 0;
		remark_flag = 0;
	}

    try {
      //conn = DBConnection.getConnection();
      if (flag == 0 || flag == 1){
	      sqlStmt.append("SELECT '" + type_flag + "' TYPE_FLAG, SID, PRODUCT_BODY, BRAND, VERSION, TESTER, OPTIONS, GRADE, \n");
	      sqlStmt.append("GOOD_BIN, IB_BIN, BIN_TYPE_STR, INKLESS_GRADE,KTD_BIN_FLAG, IPN_ACTION_STR, EPN_SPEED, TEST_SPEED, DOWN_GRADE, FAIL_BIN, REMARK, AUTO_SHIP_YIELD, STOP_TEST_YIELD, AUTO_SCRAP_YIELD, MRB_YIELD, SAMPLE_YIELD, \n");
	      sqlStmt.append(tester_flag + " TESTER_FLAG, " +  options_flag + " OPTIONS_FLAG, " + grade_flag + " GRADE_FLAG, " + good_bin_flag + " GOOD_BIN_FLAG, "+ ib_bin_flag + " IB_BIN_FLAG, " + bin_type_str_flag + " BIN_TYPE_STR_FLAG, " + inkless_grade_flag + " INKLESS_GRADE_FLAG, "+ ktd_bin_flag_flag + " KTD_BIN_FLAG_FLAG,\n");
	      sqlStmt.append(ipn_action_str_flag + " IPN_ACTION_STR_FLAG, " +  epn_speed_flag + " EPN_SPEED_FLAG, " + test_speed_flag + " TEST_SPEED_FLAG, " + down_grade_flag + " DOWN_GRADE_FLAG, " + remark_flag + " REMARK_FLAG \n");
	      sqlStmt.append("FROM " + table1 + " T1 \n");
	      sqlStmt.append("WHERE SID = " + sid + " \n");
	      sqlStmt.append("AND NOT EXISTS (SELECT 1 FROM " + table2 + " T2 \n");
	      sqlStmt.append("WHERE T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND \n");
	      sqlStmt.append("AND SID = " + sid2 + " \n");
	      sqlStmt.append("AND T2.OPTIONS = T1.OPTIONS AND T2.GRADE = T1.GRADE \n");
	      sqlStmt.append("AND T2.TESTER = T1.TESTER AND NVL(T2.GOOD_BIN, ' ') = NVL(T1.GOOD_BIN, ' ') AND NVL(T2.IB_BIN, ' ') = NVL(T1.IB_BIN, ' ') \n");
	      //sqlStmt.append("AND T2.BIN_TYPE = T1.BIN_TYPE AND T1.INKLESS_GRADE = T2.INKLESS_GRADE AND T2.IPN_ACTION = T1.IPN_ACTION ");
	      //sqlStmt.append("AND NVL(T2.EPN_SPEED, ' ') = NVL(T1.EPN_SPEED, ' ') AND NVL(T2.TEST_SPEED, 0) = NVL(T1.TEST_SPEED, 0) ");
	      //sqlStmt.append("AND NVL(T2.DOWN_GRADE, ' ') = NVL(T1.DOWN_GRADE, ' ') AND NVL(T2.REMARK, ' ') = NVL(T1.REMARK, ' ') ");
	      //sqlStmt.append("AND NVL(T2.FAIL_BIN, ' ') = NVL(T1.FAIL_BIN, ' ')  ");
	      //sqlStmt.append("AND NVL(T2.AUTO_SHIP_YIELD, 'NA') = NVL(T1.AUTO_SHIP_YIELD, 'NA') ");
	      //sqlStmt.append("AND NVL(T2.STOP_TEST_YIELD, 'NA') = NVL(T1.STOP_TEST_YIELD, 'NA') ");
	      //sqlStmt.append("AND NVL(T2.AUTO_SCRAP_YIELD, 'NA') = NVL(T1.AUTO_SCRAP_YIELD, 'NA') ");
	      //sqlStmt.append("AND NVL(T2.MRB_YIELD, 'NA') = NVL(T1.MRB_YIELD, 'NA') ");
	      //sqlStmt.append("AND NVL(T2.SAMPLE_YIELD, 'NA') = NVL(T1.SAMPLE_YIELD, 'NA')) ");
	      sqlStmt.append(")");
      }else{
    	  sqlStmt.append("SELECT 'old' TYPE_FLAG, T2.SID, T2.PRODUCT_BODY, T2.BRAND, T2.VERSION, T2.TESTER, T2.OPTIONS, T2.GRADE, \n");
	      sqlStmt.append("T2.GOOD_BIN,T2.IB_BIN, T2.BIN_TYPE_STR, T2.INKLESS_GRADE,T2.KTD_BIN_FLAG, T2.IPN_ACTION_STR, T2.EPN_SPEED, T2.TEST_SPEED, T2.DOWN_GRADE, T2.FAIL_BIN, T2.REMARK, T2.AUTO_SHIP_YIELD, T2.STOP_TEST_YIELD, T2.AUTO_SCRAP_YIELD, T2.MRB_YIELD, T2.SAMPLE_YIELD, \n");
	      sqlStmt.append(tester_flag + " TESTER_FLAG, " +  options_flag + " OPTIONS_FLAG, " + grade_flag + " GRADE_FLAG, " + good_bin_flag + " GOOD_BIN_FLAG, " + ib_bin_flag + " IB_BIN_FLAG, " + bin_type_str_flag + " BIN_TYPE_STR_FLAG, " + inkless_grade_flag + " INKLESS_GRADE_FLAG, " + ktd_bin_flag_flag + " KTD_BIN_FLAG_FLAG,\n");
	      sqlStmt.append(ipn_action_str_flag + " IPN_ACTION_STR_FLAG, " +  epn_speed_flag + " EPN_SPEED_FLAG, " + test_speed_flag + " TEST_SPEED_FLAG, " + down_grade_flag + " DOWN_GRADE_FLAG, " + remark_flag + " REMARK_FLAG \n");
	      sqlStmt.append("FROM " + table1 + " T1, " + table2 + " T2 \n");
	      sqlStmt.append("WHERE T1.SID = " + sid + " \n");
	      sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND \n");
	      sqlStmt.append("AND T2.SID = " + sid2 + " \n");
	      sqlStmt.append("AND T2.OPTIONS = T1.OPTIONS AND T2.GRADE = T1.GRADE \n");
	      sqlStmt.append("AND T2.TESTER = T1.TESTER AND NVL(T2.GOOD_BIN, ' ') = NVL(T1.GOOD_BIN, ' ') AND NVL(T2.IB_BIN, ' ') = NVL(T1.IB_BIN, ' ')\n");
	      sqlStmt.append("AND (T2.BIN_TYPE != T1.BIN_TYPE OR T1.INKLESS_GRADE != T2.INKLESS_GRADE OR NVL(T2.KTD_BIN_FLAG, ' ') != NVL(T1.KTD_BIN_FLAG, ' ') OR T2.IPN_ACTION != T1.IPN_ACTION \n");
	      sqlStmt.append(" OR NVL(T2.EPN_SPEED, ' ') != NVL(T1.EPN_SPEED, ' ') OR NVL(T2.TEST_SPEED, 0) != NVL(T1.TEST_SPEED, 0) \n");
	      sqlStmt.append(" OR NVL(T2.DOWN_GRADE, ' ') != NVL(T1.DOWN_GRADE, ' ') OR NVL(T2.REMARK, ' ') != NVL(T1.REMARK, ' ') \n");
	      //sqlStmt.append(" OR NVL(T2.FAIL_BIN, ' ') = NVL(T1.FAIL_BIN, ' ')  ");
	      //sqlStmt.append(" OR NVL(T2.AUTO_SHIP_YIELD, 'NA') = NVL(T1.AUTO_SHIP_YIELD, 'NA') ");
	      //sqlStmt.append(" OR NVL(T2.STOP_TEST_YIELD, 'NA') = NVL(T1.STOP_TEST_YIELD, 'NA') ");
	      //sqlStmt.append(" OR NVL(T2.AUTO_SCRAP_YIELD, 'NA') = NVL(T1.AUTO_SCRAP_YIELD, 'NA') ");
	      //sqlStmt.append(" OR NVL(T2.MRB_YIELD, 'NA') = NVL(T1.MRB_YIELD, 'NA') ");
	      //sqlStmt.append(" OR NVL(T2.SAMPLE_YIELD, 'NA') = NVL(T1.SAMPLE_YIELD, 'NA')) ");
	      sqlStmt.append(")");
	      sqlStmt.append("UNION \n");
	      sqlStmt.append("SELECT 'update' TYPE_FLAG, T1.SID, T1.PRODUCT_BODY, T1.BRAND, T1.VERSION, T1.TESTER, T1.OPTIONS, T1.GRADE, \n");
	      sqlStmt.append("T1.GOOD_BIN, T1.IB_BIN, T1.BIN_TYPE_STR, T1.INKLESS_GRADE,T1.KTD_BIN_FLAG, T1.IPN_ACTION_STR, T1.EPN_SPEED, T1.TEST_SPEED, T1.DOWN_GRADE, T1.FAIL_BIN, T1.REMARK, T1.AUTO_SHIP_YIELD, T1.STOP_TEST_YIELD, T1.AUTO_SCRAP_YIELD, T1.MRB_YIELD, T1.SAMPLE_YIELD, \n");
	      sqlStmt.append("decode(nvl(T2.TESTER, '#'),nvl(T1.TESTER, '#'),0,1) TESTER_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.OPTIONS, '#'),nvl(T1.OPTIONS, '#'),0,1)  OPTIONS_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.GRADE, '#'),nvl(T1.GRADE, '#'),0,1)  GRADE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.GOOD_BIN, '#'),nvl(T1.GOOD_BIN, '#'),0,1) GOOD_BIN_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.IB_BIN, '#'),nvl(T1.IB_BIN, '#'),0,1) IB_BIN_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.BIN_TYPE_STR, '#'),nvl(T1.BIN_TYPE_STR, '#'),0,1)  BIN_TYPE_STR_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.INKLESS_GRADE, '#'),nvl(T1.INKLESS_GRADE, '#'),0,1)  INKLESS_GRADE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.KTD_BIN_FLAG, '#'),nvl(T1.KTD_BIN_FLAG, '#'),0,1)  KTD_BIN_FLAG_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.IPN_ACTION_STR, '#'),nvl(T1.IPN_ACTION_STR, '#'),0,1) IPN_ACTION_STR_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.EPN_SPEED, '#'),nvl(T1.EPN_SPEED, '#'),0,1) EPN_SPEED_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.TEST_SPEED, -999),nvl(T1.TEST_SPEED, -999),0,1) TEST_SPEED_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.DOWN_GRADE, '#'),nvl(T1.DOWN_GRADE, '#'),0,1) DOWN_GRADE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.REMARK, '#'),nvl(T1.REMARK, '#'),0,1) REMARK_FLAG \n");
	      sqlStmt.append("FROM " + table1 + " T1, " + table2 + " T2 \n");
	      sqlStmt.append("WHERE T1.SID = " + sid + " \n");
	      sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND \n");
	      sqlStmt.append("AND T2.SID = " + sid2 + " \n");
	      sqlStmt.append("AND T2.OPTIONS = T1.OPTIONS AND T2.GRADE = T1.GRADE \n");
	      sqlStmt.append("AND T2.TESTER = T1.TESTER AND NVL(T2.GOOD_BIN, ' ') = NVL(T1.GOOD_BIN, ' ') AND NVL(T2.IB_BIN, ' ') = NVL(T1.IB_BIN, ' ') \n");
	      sqlStmt.append("AND (T2.BIN_TYPE != T1.BIN_TYPE OR T1.INKLESS_GRADE != T2.INKLESS_GRADE OR NVL(T2.KTD_BIN_FLAG, ' ') != NVL(T1.KTD_BIN_FLAG, ' ') OR T2.IPN_ACTION != T1.IPN_ACTION \n");
	      sqlStmt.append(" OR NVL(T2.EPN_SPEED, ' ') != NVL(T1.EPN_SPEED, ' ') OR NVL(T2.TEST_SPEED, 0) != NVL(T1.TEST_SPEED, 0) \n");
	      sqlStmt.append(" OR NVL(T2.DOWN_GRADE, ' ') != NVL(T1.DOWN_GRADE, ' ') OR NVL(T2.REMARK, ' ') != NVL(T1.REMARK, ' ') \n");
	      //sqlStmt.append(" OR NVL(T2.FAIL_BIN, ' ') = NVL(T1.FAIL_BIN, ' ')  ");
	      //sqlStmt.append(" OR NVL(T2.AUTO_SHIP_YIELD, 'NA') = NVL(T1.AUTO_SHIP_YIELD, 'NA') ");
	      //sqlStmt.append(" OR NVL(T2.STOP_TEST_YIELD, 'NA') = NVL(T1.STOP_TEST_YIELD, 'NA') ");
	      //sqlStmt.append(" OR NVL(T2.AUTO_SCRAP_YIELD, 'NA') = NVL(T1.AUTO_SCRAP_YIELD, 'NA') ");
	      //sqlStmt.append(" OR NVL(T2.MRB_YIELD, 'NA') = NVL(T1.MRB_YIELD, 'NA') ");
	      //sqlStmt.append(" OR NVL(T2.SAMPLE_YIELD, 'NA') = NVL(T1.SAMPLE_YIELD, 'NA')) ");
	      sqlStmt.append(")");
    	  
      }
      PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        TFIMBasicActionForm baInfo = new TFIMBasicActionForm();
        baInfo.setSid(Integer.parseInt(sid));
        baInfo.setPd_body(rs.getString("PRODUCT_BODY"));
        baInfo.setBrand(rs.getString("BRAND"));
        baInfo.setTester(rs.getString("TESTER"));
        baInfo.setOptions(rs.getString("OPTIONS"));
        baInfo.setGrade(rs.getString("GRADE"));
        baInfo.setGood_bin(rs.getString("GOOD_BIN"));
        baInfo.setIb_bin(rs.getString("IB_BIN"));
        baInfo.setBin_type_str(rs.getString("BIN_TYPE_STR"));
        baInfo.setInkless_grade(rs.getString("INKLESS_GRADE"));
        baInfo.setKtd_bin_flag(rs.getString("KTD_BIN_FLAG"));
        baInfo.setIpn_action_str(rs.getString("IPN_ACTION_STR"));
        baInfo.setEpn_speed(rs.getString("EPN_SPEED"));
        baInfo.setTest_speed(rs.getString("TEST_SPEED"));
        baInfo.setDown_grade(rs.getString("DOWN_GRADE"));
        baInfo.setFail_bin(rs.getString("FAIL_BIN"));
        baInfo.setRemark(rs.getString("REMARK"));
        baInfo.setAuto_ship_yield(rs.getString("AUTO_SHIP_YIELD"));
        baInfo.setStop_test_yield(rs.getString("STOP_TEST_YIELD"));
        baInfo.setAuto_scrap_yield(rs.getString("AUTO_SCRAP_YIELD"));
        baInfo.setMrb_yield(rs.getString("MRB_YIELD"));
        baInfo.setSample_yield(rs.getString("SAMPLE_YIELD"));
        baInfo.setType_flag(rs.getString("TYPE_FLAG"));
        baInfo.setTester_flag(rs.getInt("TESTER_FLAG"));
        baInfo.setOptions_flag(rs.getInt("OPTIONS_FLAG"));
        baInfo.setGrade_flag(rs.getInt("GRADE_FLAG"));
        baInfo.setGood_bin_flag(rs.getInt("GOOD_BIN_FLAG"));
        baInfo.setIb_bin_flag(rs.getInt("IB_BIN_FLAG"));
        baInfo.setBin_type_str_flag(rs.getInt("BIN_TYPE_STR_FLAG"));
        baInfo.setInkless_grade_flag(rs.getInt("INKLESS_GRADE_FLAG"));
        baInfo.setKtd_bin_flag_flag(rs.getInt("KTD_BIN_FLAG_FLAG"));
        baInfo.setIpn_action_str_flag(rs.getInt("IPN_ACTION_STR_FLAG"));
        baInfo.setEpn_speed_flag(rs.getInt("EPN_SPEED_FLAG"));
        baInfo.setTest_speed_flag(rs.getInt("TEST_SPEED_FLAG"));
        baInfo.setDown_grade_flag(rs.getInt("DOWN_GRADE_FLAG"));
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

  public static YieldDefinitionActionForm[] CompareYield(Connection conn, String sid,
		  													String sid2,
		  													String status,
		  													int compareType) {

	  StringBuffer sqlStmt = new StringBuffer();
	  //Connection conn = null;
	  //String productType = OiMaintainService.getProductType(sid);

	  ArrayList tmp2 = new ArrayList();
	  try {
		  //conn = DBConnection.getConnection();
		  sqlStmt.append("call tf_compare_yield_definition ( " + sid+ ", " + sid2 + ", '" + status + "') ");

		  CallableStatement stmt = conn.prepareCall(sqlStmt.toString());
		  stmt.execute();

		  sqlStmt = new StringBuffer();
		  sqlStmt.append("SELECT * FROM tf_cmp_yield WHERE sid IN (?,?) ORDER BY ord, yid, type ");

		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ps.setString(1, sid);
		  ps.setString(2, sid2);
		  ResultSet rs = ps.executeQuery();
		  while (rs.next()) {
			  YieldDefinitionActionForm ft = new YieldDefinitionActionForm();
			  ft.setSid(rs.getInt("SID"));
			  ft.setYid(rs.getInt("YID"));
			  ft.setSeq(rs.getInt("SEQ"));
			  ft.setType(rs.getString("type"));
			  ft.setProduct_code(rs.getString("product_code"));
			  ft.setTest_mode(rs.getString("test_mode"));
			  ft.setAuto_ship(rs.getString("auto_ship"));
			  ft.setHold_pe(rs.getString("hold_pe"));
			  ft.setHold_bin(rs.getString("hold_bin"));
			  ft.setHold_bin_cri(rs.getString("hold_bin_cri"));
			  ft.setAuto_scrap(rs.getString("auto_scrap"));
			  ft.setStop(rs.getString("stop"));
			  ft.setMrb(rs.getString("mrb"));
			  ft.setSampling_yield(rs.getString("sampling_yield"));
			  ft.setNotes(rs.getString("notes"));			  
			  ft.setProduct_code_flag(Integer.parseInt(rs.getString("product_code_flag")));
			  ft.setTest_mode_flag(Integer.parseInt(rs.getString("test_mode_flag")));
			  ft.setAuto_ship_flag(Integer.parseInt(rs.getString("auto_ship_flag")));
			  ft.setHold_pe_flag(Integer.parseInt(rs.getString("hold_pe_flag")));
			  ft.setHold_bin_flag(Integer.parseInt(rs.getString("hold_bin_flag")));
			  ft.setHold_bin_cri_flag(Integer.parseInt(rs.getString("hold_bin_cri_flag")));
			  ft.setAuto_scrap_flag(Integer.parseInt(rs.getString("auto_scrap_flag")));
			  ft.setStop_flag(Integer.parseInt(rs.getString("stop_flag")));
			  ft.setMrb_flag(Integer.parseInt(rs.getString("mrb_flag")));
			  ft.setSampling_yield_flag(Integer.parseInt(rs.getString("sampling_yield_flag")));
			  ft.setNotes_flag(Integer.parseInt(rs.getString("notes_flag")));			  
			  tmp2.add(ft);
			  if (compareType == 0)
				  break;
		  }
		  return (YieldDefinitionActionForm[]) tmp2.toArray(new YieldDefinitionActionForm[0]);
	  } catch (Exception e) {
		  e.printStackTrace();
	  } finally {
		  //DBConnection.close(conn);
		  //conn = null;
	  }
	  return null;
  }

  public static YieldDefBean[] CompareYield(Connection conn, String sid,
										   String sid2,
										   String proc_type,
										   String status,
										   int compareType,
										   String hold_dgrade_flag) {

	  StringBuffer sqlStmt = new StringBuffer();
	  //Connection conn = null;

	  ArrayList tmp2 = new ArrayList();
	  try {
		  //conn = DBConnection.getConnection();
		  String productType = OiMaintainService.getProductType(conn, sid);
		  if(proc_type.equals("WS") && productType.equals("NVM"))
			  sqlStmt.append("call tf_compare_yield_def_ByKey ( " + sid+ ", " + sid2 + ", " +
				  	     (proc_type.equals("WS")?0:1)+",'" + status + "') ");
		  else
			  sqlStmt.append("call tf_compare_yield_def ( " + sid+ ", " + sid2 + ", " +
					  	     (proc_type.equals("WS")?0:1)+",'" + status + "') ");
		  
		  CallableStatement stmt = conn.prepareCall(sqlStmt.toString());
		  stmt.execute();

		  sqlStmt = new StringBuffer();
		  if(proc_type.equals("WS") && productType.equals("NVM") && hold_dgrade_flag.equals("Hold")){
		        sqlStmt.append("SELECT 1 as seqnum,type,sid,yid,seq,product_code,brand,test_mode,\n" +
		  		"item,lower_limit,flag1,upper_limit,flag2,\n" +
		  		"item_type,item_mode2,item_bins2,action,dg_action,by_lot_dg,change_ipn,\n" +
		  		"route_name,start_step,remark,\n" +
		  		"product_code_flag,brand_flag,test_mode_flag,\n" +
		  		"item_flag,action_flag,change_ipn_flag,route_name_flag,\n" +
		  		"start_step_flag,remark_flag,dg_action_flag,by_lot_dg_flag,ord\n" +
		        "FROM tf_cmp_yield_def WHERE sid IN (?,?)\n " +
		        "and (action not like 'Dgrade%' and action !='Follow Hold Criteria' and action not like 'Change IPN%')" +
		        //20130320"ORDER BY seqnum, ord, yid, type");
		        "ORDER BY seqnum, product_code, brand, test_mode, item, ORD, TYPE, cast(nvl(lower_limit,0) as int),cast(nvl(upper_limit,0) as int)");
		  }else if(proc_type.equals("WS") && productType.equals("NVM") && hold_dgrade_flag.equals("Dgrade")){     
		        //"union\n" +
			    sqlStmt.append("SELECT 2 as seqnum,type,sid,yid,seq,product_code,brand,test_mode,\n" +
		  		"item,lower_limit,flag1,upper_limit,flag2,\n" +
		  		"item_type,item_mode2,item_bins2,action,dg_action,by_lot_dg,change_ipn,\n" +
		  		"route_name,start_step,remark,\n" +
		  		"product_code_flag,brand_flag,test_mode_flag,\n" +
		  		"item_flag,action_flag,change_ipn_flag,route_name_flag,\n" +
		  		"start_step_flag,remark_flag,dg_action_flag,by_lot_dg_flag,ord\n" +
		  		"FROM tf_cmp_yield_def WHERE sid IN (?,?)\n" +
		  		"and (action like 'Dgrade%' or action ='Follow Hold Criteria' or action like 'Change IPN%')" +
		  		//20130320"ORDER BY seqnum, ord, yid, type");
		  		"ORDER BY seqnum, product_code, brand, test_mode, item, ORD, TYPE, cast(nvl(lower_limit,0) as int),cast(nvl(upper_limit,0) as int)");
		  
		  }else{
			  sqlStmt.append("SELECT type,sid,yid,seq,product_code,brand,test_mode,\n" +
				  		"item,lower_limit,flag1,upper_limit,flag2,\n" +
				  		"item_type,item_mode2,item_bins2,action,dg_action,by_lot_dg,change_ipn,\n" +
				  		"route_name,start_step,remark,\n" +
				  		"product_code_flag,brand_flag,test_mode_flag,\n" +
				  		"item_flag,action_flag,change_ipn_flag,route_name_flag,\n" +
				  		"start_step_flag,remark_flag,dg_action_flag,by_lot_dg_flag \n" +
				  		//"FROM tf_cmp_yield_def WHERE sid IN (?,?) ORDER BY ord, yid, type");
				  		"FROM tf_cmp_yield_def WHERE sid IN (?,?) ORDER BY type,product_code, brand, test_mode, item, ord,  cast(nvl(lower_limit,0) as int),cast(nvl(upper_limit,0) as int)");

					 
		  }
		  TDSLogger.println(sqlStmt.toString());
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ps.setString(1, sid);
		  ps.setString(2, sid2);
		  ResultSet rs = ps.executeQuery();
		  while (rs.next()) {
			  YieldDefBean ft = new YieldDefBean();
			  ft.setType(rs.getString("TYPE"));
			  ft.setSid(rs.getInt("SID"));
			  ft.setYid(rs.getInt("YID"));
			  ft.setSeq(rs.getInt("SEQ"));
			  ft.setProduct_code(rs.getString("product_code"));
			  ft.setBrands(rs.getString("brand"));
			  ft.setTest_mode(rs.getString("test_mode"));
			  ft.setItem(rs.getString("item"));
			  ft.setLower_limit(rs.getString("lower_limit"));
			  ft.setFlag1(rs.getString("flag1"));
			  ft.setUpper_limit(rs.getString("upper_limit"));
			  ft.setFlag2(rs.getString("flag2"));
			  ft.setItem_type(rs.getInt("item_type"));
			  ft.setItem_mode2(rs.getString("item_mode2"));
			  ft.setItem_bins2(rs.getString("item_bins2"));
			  ft.setAction(rs.getString("action"));
			  ft.setDg_action(rs.getString("dg_action") == null ? "":rs.getString("dg_action"));
			  ft.setBy_lot_dg(rs.getString("by_lot_dg"));
			  ft.setChange_ipn(rs.getString("change_ipn"));
			  ft.setRoute_name(rs.getString("route_name"));
			  ft.setStart_step(rs.getString("start_step"));
			  ft.setRemark(rs.getString("remark"));
			  ft.setProduct_code_flag(Integer.parseInt(rs.getString("product_code_flag")));
			  ft.setBrand_flag(Integer.parseInt(rs.getString("brand_flag")));
			  ft.setTest_mode_flag(Integer.parseInt(rs.getString("test_mode_flag")));
			  ft.setItem_flag(Integer.parseInt(rs.getString("item_flag")));
			  ft.setAction_flag(Integer.parseInt(rs.getString("action_flag")));
			  ft.setChange_ipn_flag(Integer.parseInt(rs.getString("change_ipn_flag")));
			  ft.setRoute_name_flag(Integer.parseInt(rs.getString("route_name_flag")));
			  ft.setStart_step_flag(Integer.parseInt(rs.getString("start_step_flag")));
			  ft.setRemark_flag(Integer.parseInt(rs.getString("remark_flag")));
			  ft.setDg_action_flag(rs.getString("dg_action_flag") == null ? 0:Integer.parseInt(rs.getString("dg_action_flag")));
			  ft.setBy_lot_dg_flag(rs.getString("by_lot_dg_flag") == null ? 0:Integer.parseInt(rs.getString("by_lot_dg_flag")));
		  
			  tmp2.add(ft);
			  if (compareType == 0)
				  break;
		  }
          /*USE FOR HOLD/ACTION CRITERIA, INCLUDE OF NVM ken*/
          if(!productType.equals("NVM")) {
              return YieldDefService.GetYeildDef(conn, (YieldDefBean[]) tmp2.toArray(new YieldDefBean[0]),hold_dgrade_flag);
          } else {
		  return (YieldDefBean[]) tmp2.toArray(new YieldDefBean[0]);
          }
	  } catch (Exception e) {
		  e.printStackTrace();
	  } finally {
		  //DBConnection.close(conn);
		  //conn = null;
	  }
	  return null;
  }

  public static EditiionCompareActionForm[] ComparePDR(Connection conn, String sid,
                                                       String sid2,
                                                       String status,
                                                       int flag,
                                                       int compareType) {
    StringBuffer sqlStmt = new StringBuffer();
    //Connection conn = null;
    String table1 = null;
    String table2 = null;
    String type_flag = null;
	int route_name_flag = 0;
	int step_seq_flag = 0;
	int step_name_flag = 0;
	int test_time_flag = 0;
	int time_unit_flag = 0;
	int temperature_flag = 0;
	int sampling_test_flag = 0;
	int sampling_cond_flag = 0;
	int rework_step_flag = 0;
	int test_time2_flag = 0;
	int time_unit2_flag = 0;
	int remark_flag = 0;

    ArrayList tmp2 = new ArrayList();
    if (!status.equals("P") && !status.equals("A")) {
      table1 = "TF_PRODUCT_ROUTE";
      table2 = "TF_PRODUCT_ROUTE";
    } else {
      if (flag == 0) {/* 以前一版為主找出後一版之不同處 */
        table1 = "TF_PRODUCT_ROUTE";
        table2 = "TF_PRODUCT_ROUTE_TX";
      } else if (flag == 1){ /* 由後一版找出前一版與其不同之處 */
        table1 = "TF_PRODUCT_ROUTE_TX";
        table2 = "TF_PRODUCT_ROUTE";
      } else { /* 被 update 的資料之處 */
  		table1 = "TF_PRODUCT_ROUTE_TX";
  		table2 = "TF_PRODUCT_ROUTE";
      }
    }
    if (flag == 0){ 
		type_flag = "remove";
		route_name_flag = 0;
		step_seq_flag = 0;
		step_name_flag = 0;
		test_time_flag = 0;
		time_unit_flag = 0;
		temperature_flag = 0;
		sampling_test_flag = 0;
		sampling_cond_flag = 0;
		rework_step_flag = 0;
		test_time2_flag = 0;
		time_unit2_flag = 0;
		remark_flag = 0;
	}else if (flag == 1){ 
		type_flag = "insert";
		route_name_flag = 1;
		step_seq_flag = 1;
		step_name_flag = 1;
		test_time_flag = 1;
		time_unit_flag = 1;
		temperature_flag = 1;
		sampling_test_flag = 1;
		sampling_cond_flag = 1;
		rework_step_flag = 1;
		test_time2_flag = 1;
		time_unit2_flag = 1;
		remark_flag = 1;
	}else{
		route_name_flag = 0;
		step_seq_flag = 0;
		step_name_flag = 0;
		test_time_flag = 0;
		time_unit_flag = 0;
		temperature_flag = 0;
		sampling_test_flag = 0;
		sampling_cond_flag = 0;
		rework_step_flag = 0;
		test_time2_flag = 0;
		time_unit2_flag = 0;
		remark_flag = 0;
	}

    try {
      //conn = DBConnection.getConnection();
      if (flag == 0 || flag == 1){
	      sqlStmt.append("SELECT '" + type_flag + "' TYPE_FLAG, SID, PRODUCT_BODY, BRAND, VERSION, ROUTE_NAME, STEP_SEQ, \n");
	      sqlStmt.append("STEP_NAME, TEST_TIME, TIME_UNIT, TEMPERATURE, SAMPLING_TEST, SAMPLING_COND, REWORK_STEP, TEST_TIME2, TIME_UNIT2, REMARK, \n");
	      sqlStmt.append(route_name_flag + " ROUTE_NAME_FLAG, " +  step_seq_flag + " STEP_SEQ_FLAG, " + step_name_flag + " STEP_NAME_FLAG, " + test_time_flag + " TEST_TIME_FLAG, " + time_unit_flag + " TIME_UNIT_FLAG, " + temperature_flag + " TEMPERATURE_FLAG, \n");
	      sqlStmt.append(sampling_test_flag + " SAMPLING_TEST_FLAG, " + sampling_cond_flag + " SAMPLING_COND_FLAG, " +  rework_step_flag + " REWORK_STEP_FLAG, " + test_time2_flag + " TEST_TIME2_FLAG, " + time_unit2_flag + " TIME_UNIT2_FLAG, " + remark_flag + " REMARK_FLAG \n");
	      sqlStmt.append("FROM " + table1 + " T1 \n");
	      sqlStmt.append("WHERE SID = " + sid + " \n");
	      sqlStmt.append("AND NOT EXISTS (SELECT 1 FROM " + table2 + " T2 \n");
	      sqlStmt.append("WHERE T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND \n");
	      sqlStmt.append("AND SID = " + sid2 + " AND T2.ROUTE_NAME = T1.ROUTE_NAME \n");
	      sqlStmt.append("AND T2.STEP_SEQ = T1.STEP_SEQ AND T2.STEP_NAME = T1.STEP_NAME \n");
	      //sqlStmt.append("AND NVL(T2.TEST_TIME, -999) = NVL(T1.TEST_TIME, -999) AND NVL(T2.TIME_UNIT, ' ') = NVL(T1.TIME_UNIT, ' ') \n");
	      //sqlStmt.append("AND NVL(T2.TEMPERATURE, ' ') = NVL(T1.TEMPERATURE, ' ') AND NVL(T2.SAMPLING_TEST, ' ') = NVL(T1.SAMPLING_TEST, ' ') AND NVL(T2.REMARK, ' ') = NVL(T1.REMARK, ' ')\n");
	      //sqlStmt.append("AND NVL(T2.REWORK_STEP, ' ') = NVL(T1.REWORK_STEP, ' ') \n");
	      //sqlStmt.append("AND NVL(T2.TEST_TIME2, -999) = NVL(T1.TEST_TIME2, -999) AND NVL(T2.TIME_UNIT2, ' ') = NVL(T1.TIME_UNIT2, ' ')) \n");
	      sqlStmt.append(")");
	      sqlStmt.append("ORDER BY ROUTE_NAME, STEP_SEQ");
      }else{
    	  sqlStmt.append("SELECT 'old' TYPE_FLAG, T2.SID, T2.PRODUCT_BODY, T2.BRAND, T2.VERSION, T2.ROUTE_NAME, T2.STEP_SEQ, \n");
	      sqlStmt.append("T2.STEP_NAME, T2.TEST_TIME, T2.TIME_UNIT, T2.TEMPERATURE, T2.SAMPLING_TEST, T2.SAMPLING_COND, T2.REWORK_STEP, T2.TEST_TIME2, T2.TIME_UNIT2, T2.REMARK, \n");
	      sqlStmt.append(route_name_flag + " ROUTE_NAME_FLAG, " +  step_seq_flag + " STEP_SEQ_FLAG, " + step_name_flag + " STEP_NAME_FLAG, " + test_time_flag + " TEST_TIME_FLAG, " + time_unit_flag + " TIME_UNIT_FLAG, " + temperature_flag + " TEMPERATURE_FLAG, \n");
	      sqlStmt.append(sampling_test_flag + " SAMPLING_TEST_FLAG, " + sampling_cond_flag + " SAMPLING_COND_FLAG, " +  rework_step_flag + " REWORK_STEP_FLAG, " + test_time2_flag + " TEST_TIME2_FLAG, " + time_unit2_flag + " TIME_UNIT2_FLAG, " + remark_flag + " REMARK_FLAG \n");
	      sqlStmt.append("FROM " + table1 + " T1," + table2 + " T2 \n");
	      sqlStmt.append("WHERE T1.SID = " + sid + " \n");
	      sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND \n");
	      sqlStmt.append("AND T2.SID = " + sid2 + " AND T2.ROUTE_NAME = T1.ROUTE_NAME \n");
	      sqlStmt.append("AND T2.STEP_SEQ = T1.STEP_SEQ AND T2.STEP_NAME = T1.STEP_NAME \n");
	      sqlStmt.append("AND (NVL(T2.TEST_TIME, -999) != NVL(T1.TEST_TIME, -999) OR NVL(T2.TIME_UNIT, ' ') != NVL(T1.TIME_UNIT, ' ') \n");
	      sqlStmt.append(" OR NVL(T2.TEMPERATURE, ' ') != NVL(T1.TEMPERATURE, ' ') OR NVL(T2.SAMPLING_TEST, ' ') != NVL(T1.SAMPLING_TEST, ' ') OR NVL(T2.SAMPLING_COND, ' ') != NVL(T1.SAMPLING_COND, ' ') OR NVL(T2.REMARK, ' ') != NVL(T1.REMARK, ' ')\n");
	      sqlStmt.append(" OR NVL(T2.REWORK_STEP, ' ') != NVL(T1.REWORK_STEP, ' ') \n");
	      sqlStmt.append(" OR NVL(T2.TEST_TIME2, -999) != NVL(T1.TEST_TIME2, -999) OR NVL(T2.TIME_UNIT2, ' ') != NVL(T1.TIME_UNIT2, ' ')) \n");
	      sqlStmt.append("UNION \n");
	      sqlStmt.append("SELECT 'update' TYPE_FLAG, T1.SID, T1.PRODUCT_BODY, T1.BRAND, T1.VERSION, T1.ROUTE_NAME, T1.STEP_SEQ, \n");
	      sqlStmt.append("T1.STEP_NAME, T1.TEST_TIME, T1.TIME_UNIT, T1.TEMPERATURE, T1.SAMPLING_TEST, T1.SAMPLING_COND, T1.REWORK_STEP, T1.TEST_TIME2, T1.TIME_UNIT2, T1.REMARK, \n");
	      sqlStmt.append("decode(nvl(T2.ROUTE_NAME, '#'),nvl(T1.ROUTE_NAME, '#'),0,1)  ROUTE_NAME_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.STEP_SEQ, 0),nvl(T1.STEP_SEQ, 0),0,1) STEP_SEQ_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.STEP_NAME, '#'),nvl(T1.STEP_NAME, '#'),0,1) STEP_NAME_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.TEST_TIME, 0),nvl(T1.TEST_TIME, 0),0,1) TEST_TIME_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.TIME_UNIT, '#'),nvl(T1.TIME_UNIT, '#'),0,1) TIME_UNIT_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.TEMPERATURE, '#'),nvl(T1.TEMPERATURE, '#'),0,1) TEMPERATURE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.SAMPLING_TEST, '#'),nvl(T1.SAMPLING_TEST, '#'),0,1) SAMPLING_TEST_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.SAMPLING_COND, '#'),nvl(T1.SAMPLING_COND, '#'),0,1) SAMPLING_COND_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.REWORK_STEP, '#'),nvl(T1.REWORK_STEP, '#'),0,1) REWORK_STEP_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.TEST_TIME2, 0),nvl(T1.TEST_TIME2, 0),0,1) TEST_TIME2_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.TIME_UNIT2, '#'),nvl(T1.TIME_UNIT2, '#'),0,1) TIME_UNIT2_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.REMARK, '#'),nvl(T1.REMARK, '#'),0,1) REMARK_FLAG \n");
	      sqlStmt.append("FROM " + table1 + " T1," + table2 + " T2 \n");
	      sqlStmt.append("WHERE T1.SID = " + sid + " \n");
	      sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND \n");
	      sqlStmt.append("AND T2.SID = " + sid2 + " AND T2.ROUTE_NAME = T1.ROUTE_NAME \n");
	      sqlStmt.append("AND T2.STEP_SEQ = T1.STEP_SEQ AND T2.STEP_NAME = T1.STEP_NAME \n");
	      sqlStmt.append("AND (NVL(T2.TEST_TIME, -999) != NVL(T1.TEST_TIME, -999) OR NVL(T2.TIME_UNIT, ' ') != NVL(T1.TIME_UNIT, ' ') \n");
	      sqlStmt.append(" OR NVL(T2.TEMPERATURE, ' ') != NVL(T1.TEMPERATURE, ' ') OR NVL(T2.SAMPLING_TEST, ' ') != NVL(T1.SAMPLING_TEST, ' ') OR NVL(T2.SAMPLING_COND, ' ') != NVL(T1.SAMPLING_COND, ' ') OR NVL(T2.REMARK, ' ') != NVL(T1.REMARK, ' ')\n");
	      sqlStmt.append(" OR NVL(T2.REWORK_STEP, ' ') != NVL(T1.REWORK_STEP, ' ') \n");
	      sqlStmt.append(" OR NVL(T2.TEST_TIME2, -999) != NVL(T1.TEST_TIME2, -999) OR NVL(T2.TIME_UNIT2, ' ') != NVL(T1.TIME_UNIT2, ' ')) \n");
	      sqlStmt.append("ORDER BY ROUTE_NAME, STEP_SEQ, TYPE_FLAG");
    	  
      }
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
        bean.setRework_step(rs.getString("REWORK_STEP"));
        bean.setTest_time2(rs.getInt("TEST_TIME2"));
        bean.setTime_unit2(rs.getString("TIME_UNIT2"));
        bean.setRemark(rs.getString("REMARK"));
        bean.setType_flag(rs.getString("TYPE_FLAG"));
        bean.setRoute_name_flag(rs.getInt("ROUTE_NAME_FLAG"));
        bean.setStep_seq_flag(rs.getInt("STEP_SEQ_FLAG"));
        bean.setStep_name_flag(rs.getInt("STEP_NAME_FLAG"));
        bean.setTest_time_flag(rs.getInt("TEST_TIME_FLAG"));
        bean.setTime_unit_flag(rs.getInt("TIME_UNIT_FLAG"));
        bean.setTemperature_flag(rs.getInt("TEMPERATURE_FLAG"));
        bean.setSampling_test_flag(rs.getInt("SAMPLING_TEST_FLAG"));
        bean.setSampling_cond_flag(rs.getInt("SAMPLING_COND_FLAG"));
        bean.setRework_step_flag(rs.getInt("REWORK_STEP_FLAG"));
        bean.setTest_time2_flag(rs.getInt("TEST_TIME2_FLAG"));
        bean.setTime_unit2_flag(rs.getInt("TIME_UNIT2_FLAG"));
        bean.setRemark_flag(rs.getInt("REMARK_FLAG"));

        tmp2.add(bean);
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
  
  public static EditiionCompareActionForm[] ComparePWL(Connection conn, String sid,
          String sid2,
          String status,
          int flag,
          int compareType) {
	StringBuffer sqlStmt = new StringBuffer();
	//Connection conn = null;
	String table1 = null;
	String table2 = null;
	String type_flag = null;
	int revise_priority_flag = 0;
	int wafer_level_flag = 0;
	
	ArrayList tmp2 = new ArrayList();
	if (!status.equals("P") && !status.equals("A")) {
		table1 = "TF_PROD_WAFERLEVEL";
		table2 = "TF_PROD_WAFERLEVEL";
	} else {
	if (flag == 0) {/* 以前一版為主找出後一版之不同處 */
		table1 = "TF_PROD_WAFERLEVEL";
		table2 = "TF_PROD_WAFERLEVEL_TX";
	} else if (flag == 1) { /* 由後一版找出前一版與其不同之處 */
		table1 = "TF_PROD_WAFERLEVEL_TX";
		table2 = "TF_PROD_WAFERLEVEL";
	} else { /* 被 update 的資料之處 */
		table1 = "TF_PROD_WAFERLEVEL_TX";
		table2 = "TF_PROD_WAFERLEVEL";
	}
	}
	if (flag == 0){ 
		type_flag = "remove";
		revise_priority_flag = 0;
		wafer_level_flag = 0;
	}else if (flag == 1){ 
		type_flag = "insert";
		revise_priority_flag = 1;
		wafer_level_flag = 1;
	}	
	
	try {
		//conn = DBConnection.getConnection();
		if (flag == 0 || flag == 1){
			sqlStmt.append("SELECT '" + type_flag + "' TYPE_FLAG, T1.SID, T1.PRODUCT_BODY, T1.BRAND, T1.VERSION, T1.WAFER_LEVEL, T1.WAFER_BRAND, \n");
			sqlStmt.append("T1.BIZTYPE, T1.WAFER_GRADE, T1.APPLY_TYPE, T1.ORI_PRIORITY, T1.REVISE_PRIORITY, " + revise_priority_flag + " REVISE_PRIORITY_FLAG, " + wafer_level_flag + " WAFER_LEVEL_FLAG \n");
			sqlStmt.append("FROM " + table1 + " T1 \n");
			sqlStmt.append("WHERE SID = " + sid + " \n");
			sqlStmt.append("AND CHECKED_FLAG = 'Y' \n");
			sqlStmt.append("AND NOT EXISTS (SELECT 1 FROM " + table2 + " T2 \n");
			sqlStmt.append("WHERE T2.CHECKED_FLAG = 'Y' AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND \n");
			sqlStmt.append("AND SID = " + sid2 + " AND NVL(T2.WAFER_LEVEL, ' ') = NVL(T1.WAFER_LEVEL, ' ') \n");
			sqlStmt.append("AND NVL(T2.WAFER_BRAND, ' ') = NVL(T1.WAFER_BRAND, ' ') AND NVL(T2.BIZTYPE, ' ') = NVL(T1.BIZTYPE, ' ') \n");
			sqlStmt.append("AND NVL(T2.WAFER_GRADE, ' ') = NVL(T2.WAFER_GRADE, ' ') AND NVL(T2.APPLY_TYPE, ' ') = NVL(T1.APPLY_TYPE, ' ') \n");
			//sqlStmt.append("AND NVL(T2.REVISE_PRIORITY, 0) = NVL(T1.REVISE_PRIORITY, 0)  \n");
			sqlStmt.append(")  \n");
			sqlStmt.append("ORDER BY REVISE_PRIORITY ");
		}else{
			sqlStmt.append("SELECT 'old' TYPE_FLAG, T2.SID, T2.PRODUCT_BODY, T2.BRAND, T2.VERSION, T2.WAFER_LEVEL, T2.WAFER_BRAND,  \n");
			sqlStmt.append("T2.BIZTYPE, T2.WAFER_GRADE, T2.APPLY_TYPE, T2.ORI_PRIORITY, T2.REVISE_PRIORITY,  \n");
			sqlStmt.append("0 REVISE_PRIORITY_FLAG, 0 WAFER_LEVEL_FLAG \n");
			sqlStmt.append("FROM " + table1 + " T1, " + table2 + " T2  \n");
			sqlStmt.append("WHERE T1.SID = " + sid + " \n");
			sqlStmt.append("AND T1.CHECKED_FLAG = 'Y'  \n");
			sqlStmt.append("AND T2.CHECKED_FLAG = 'Y'  \n");
			sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY  \n");
			sqlStmt.append("AND T2.BRAND = T1.BRAND  \n");
			sqlStmt.append("AND T2.SID = " + sid2 + " \n");
			sqlStmt.append("AND NVL(T2.WAFER_LEVEL, '#') = NVL(T1.WAFER_LEVEL, '#') \n");
			sqlStmt.append("AND NVL(T2.REVISE_PRIORITY, 0) != NVL(T1.REVISE_PRIORITY, 0) \n");
			sqlStmt.append("UNION \n");
			sqlStmt.append("SELECT 'update' TYPE_FLAG, T1.SID, T1.PRODUCT_BODY, T1.BRAND, T1.VERSION, T1.WAFER_LEVEL, T1.WAFER_BRAND, \n");
			sqlStmt.append("T1.BIZTYPE, T1.WAFER_GRADE, T1.APPLY_TYPE, T1.ORI_PRIORITY, T1.REVISE_PRIORITY,  \n");
			sqlStmt.append("decode(nvl(T2.REVISE_PRIORITY, 0),nvl(T1.REVISE_PRIORITY, 0),0,1) REVISE_PRIORITY_FLAG, \n");
			sqlStmt.append("0 WAFER_LEVEL_FLAG \n");
			sqlStmt.append("FROM " + table1 + " T1, " + table2 + " T2  \n");
			sqlStmt.append("WHERE T1.SID = " + sid + " \n");
			sqlStmt.append("AND T1.CHECKED_FLAG = 'Y'  \n");
			sqlStmt.append("AND T2.CHECKED_FLAG = 'Y'  \n");
			sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY  \n");
			sqlStmt.append("AND T2.BRAND = T1.BRAND  \n");
			sqlStmt.append("AND T2.SID = " + sid2 + " \n");
			sqlStmt.append("AND NVL(T2.WAFER_LEVEL, '#') = NVL(T1.WAFER_LEVEL, '#')  \n");
			sqlStmt.append("AND NVL(T2.REVISE_PRIORITY, 0) != NVL(T1.REVISE_PRIORITY, 0) \n");
			sqlStmt.append("ORDER BY WAFER_LEVEL, TYPE_FLAG \n");
		}
			
		PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			EditiionCompareActionForm bean = new EditiionCompareActionForm();
			bean.setSid(sid);
			bean.setProductbody(rs.getString("PRODUCT_BODY"));
			bean.setBrand(rs.getString("BRAND"));
			bean.setWafer_level(rs.getString("WAFER_LEVEL"));
			bean.setWafer_brand(rs.getString("WAFER_BRAND"));
			bean.setBiztype(rs.getString("BIZTYPE"));
			bean.setWafer_grade(rs.getString("WAFER_GRADE"));
			bean.setApply_type(rs.getString("APPLY_TYPE"));
			bean.setOri_priority(rs.getString("ORI_PRIORITY"));
			bean.setRevise_priority(rs.getString("REVISE_PRIORITY"));
			bean.setRownum(getPriority(sid,table1,rs.getString("WAFER_LEVEL")));
			bean.setType_flag(rs.getString("TYPE_FLAG"));
			bean.setRevise_priority_flag(rs.getInt("REVISE_PRIORITY_FLAG"));
			bean.setWafer_level_flag(rs.getInt("WAFER_LEVEL_FLAG"));
						
			tmp2.add(bean);
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
  public static String getPriority ( String sid, String table1, String wafer_level) {
	  Connection conn = null;
	  String priority = "";
	  StringBuffer sqlStmt1 = new StringBuffer();
		
	  try {
	    conn = DBConnection.getConnection();
	    sqlStmt1.append("SELECT AA.WAFER_LEVEL, AA.REVISE_PRIORITY, ROWNUM FROM \n");
		sqlStmt1.append(" (SELECT WAFER_LEVEL, REVISE_PRIORITY \n");
		sqlStmt1.append("FROM " + table1 + " T1 \n");
		sqlStmt1.append("WHERE SID = " + sid + " \n");
		sqlStmt1.append("AND CHECKED_FLAG = 'Y' \n");
		sqlStmt1.append("ORDER BY REVISE_PRIORITY ) AA \n");
		PreparedStatement ps1 = conn.prepareStatement(sqlStmt1.toString());
		ResultSet rs1 = ps1.executeQuery();
		while (rs1.next()) {
			if(rs1.getString("WAFER_LEVEL").equals(wafer_level)){
				priority = rs1.getString("ROWNUM");
			}
		}
	  } catch (Exception e) {
			e.printStackTrace();
	  } finally {
		    DBConnection.close(conn);
		   conn = null;
	  }
	  
	  return priority;
	  
  }

  /*****************************************************************
   * 取得己生效 之 Document 不同處
   *****************************************************************/
  public static EditiionCompareActionForm[] SearchReleasedDOC(String sid,
                                                              String status,
                                                              String pd_body,
                                                              String brand,
                                                              String version,
                                                              String docType,
                                                              Connection conn) {
    StringBuffer SelSQL = new StringBuffer();
   // Connection conn = null;
    File sourceFile = null;
    File destinationFile = null;
    String path = TDSResource.getProperties("TIMPdf").getValue("jpg.path") + File.separator;
    String dlPath = TDSResource.getProperties("TIMPdf").getValue("jpg_dl.path") + File.separator;
    int version_be = Integer.parseInt(version) - 1;

    try {
      ArrayList tmp2 = new ArrayList();
    //  conn = DBConnection.getConnection();

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
     // DBConnection.close(conn);
     // conn = null;
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
                                                                String docType,
                                                                Connection conn) {
    StringBuffer SelSQL = new StringBuffer();
   // Connection conn = null;
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
      //conn = DBConnection.getConnection();

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
          bean.setTag_old("1");
          bean.setTag_new("0");
          bean.setFile_new(txdlPath + version_t0[1].toString());
          bean.setFile_old("");
          bean.setPath_new(txPath + version_t0[1].toString());
          bean.setPath_old("");
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
      //DBConnection.close(conn);
     // conn = null;
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

        //名稱相同，可能 FILE 有重新 UPLOAD
        if (version_t0[0].equals("Y") && version_t1[0].equals("Y")) {
          sourceFile = new File(txPath + version_t0[1].toString());
          destinationFile = new File(txPath + version_t1[1].toString());
          if (FileUtil.Compare(sourceFile, destinationFile) != 0)
            flag = true;
        // 前版沒有，這版有帶進來，表示改 FULL NAME 變更
        } else if (version_t0[0].equals("Y") &&
                   version_t1[0].equals("NULL") &&
                   version_1[0].equals("NULL")) {
          flag = true;
        // 前版有，後來被刪掉，又加了一個同名的，要比較 FILE 是否相同
        } else if (version_t0[0].equals("NULL") &&
                   version_t1[0].equals("Y") &&
                   version_1[0].equals("Y")) {
          sourceFile = new File(releasePath + version_1[1].toString());
          destinationFile = new File(txPath + version_t1[1].toString());
          if (FileUtil.Compare(sourceFile, destinationFile) != 0)
            flag = true;
        // 本版新增
        } else if (version_t0[0].equals("NULL") &&
                   version_t1[0].equals("Y") &&
                   version_1[0].equals("NULL")) {
          flag = true;
        // 本版被刪掉
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

  public static boolean isWSPDRDiff(Connection conn,
		                            String sid,
                                    String sid2,
                                    String status,
                                    String site
                                    ) {

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

  public static boolean isFTPDRMcpDiff(Connection conn, String sid,
          String sid2,
          String status,
          String site) {

	FTProductRouteDefinitionForm[] result =
	EditiionCompareService.CompareFTPDRMcp(conn, sid, sid2, status, site, 0);
	
	if ((result != null) && (result.length > 0))
		return true;
	else
		return false;
  }  
  
  public static boolean isAVIDiff(Connection conn, String sid,
		  String sid2,
		  String status,
		  String site) {

	  FTProductRouteDefinitionForm[] result =
		  EditiionCompareService.CompareAVIPDR(conn, sid, sid2, status, site, 0);

	  if ((result != null) && (result.length > 0))
		  return true;
	  else
		  return false;
  }

  public static boolean isBADiff(Connection conn, String sid,
                                 String sid2,
                                 String status,
                                 int flag) {

    TFIMBasicActionForm[] result =
        EditiionCompareService.CompareBA(conn, sid, sid2, status, flag, 0);
    if ((result != null) && (result.length > 0))
      return true;
    else
      return false;
  }

  public static boolean isYieldDiff(Connection conn, String sid,
		  String sid2,
		  String status) {

	  YieldDefinitionActionForm[] result =
		  EditiionCompareService.CompareYield(conn, sid, sid2, status, 0);

	  if ((result != null) && (result.length > 0))
		  return true;
	  else
		  return false;
  }

  // for WS/FT Yield Compare, 20090518
  public static boolean isYieldDiff(Connection conn, String sid,
									String sid2,
									String proc_type,
		  							String status,
		  							String hold_grade_flag) {

	  YieldDefBean[] result =
		  EditiionCompareService.CompareYield(conn, sid, sid2, proc_type, status, 0, hold_grade_flag);

	  if ((result != null) && (result.length > 0))
		  return true;
	  else
		  return false;
  }

  public static boolean isBACommentDiff(Connection conn, String sid,
                                        String sid2,
                                        String status) {
    String oldComment = "";
    String newComment = "";
    
    if (sid != null) newComment = TFIMBasicService.getBAComment(conn, Integer.parseInt(sid),status);
    if (sid2 != null) oldComment = TFIMBasicService.getBAComment(conn, Integer.parseInt(sid2),"R");
    if (oldComment.equals(newComment))
      return false;
    return true;
  }

  public static boolean isPDRDiff(Connection conn, String sid,
                                  String sid2,
                                  String status,
                                  int flag) {

    EditiionCompareActionForm[] result =
        EditiionCompareService.ComparePDR(conn, sid, sid2, status, flag, 0);

    if ((result != null) && (result.length > 0))
      return true;
    else
      return false;
  }
  public static boolean isPWLiff(Connection conn, String sid,
          String sid2,
          String status,
          int flag) {

	EditiionCompareActionForm[] result =
	EditiionCompareService.ComparePWL(conn, sid, sid2, status, flag, 0);
	
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
    int backend_option_flag = 0;
	int fg_with_code_flag = 0;
	int pin_count_flag = 0;
	int package_type_flag = 0;
	int ft_route_code_flag = 0;
	int ft_route_flag = 0;
	int ft_route_add_flag = 0;
	int ft_route_add_flag2 = 0;
	int ft_route_add_flag3 = 0;
	int tf_comment_flag = 0;
	int mcp_flag_flag = 0;
	int mask_option_flag = 0;
	int db_with_code_flag = 0;
	int sort_route_code_flag = 0;
	int ws_route_flag = 0;
	int ws_route_add_flag = 0;
	int tf_ws_comment_flag = 0;
	int quality_level_flag = 0;
	int quality_level_comment_flag = 0;
	
	int ft_special_control_flag = 0;
	int ws_special_control_flag = 0;
	int avi_flag = 0;
	int ink_flag = 0;

    ArrayList tmp2 = new ArrayList();
    if (!status.equals("P") && !status.equals("A")) {
      table1 = "TF_BOM_ROUTE";
      table2 = "TF_BOM_ROUTE";
    } else {
      if (flag == 0) {/* 以前一版為主找出後一版之不同處 */
        table1 = "TF_BOM_ROUTE";
        table2 = "TF_BOM_ROUTE_TX";
      } else if (flag == 1) { /* 由後一版找出前一版與其不同之處 */
        table1 = "TF_BOM_ROUTE_TX";
        table2 = "TF_BOM_ROUTE";
      } else { /* 被 update 的資料之處 */
        table1 = "TF_BOM_ROUTE_TX";
        table2 = "TF_BOM_ROUTE";
      }
    }
    if (flag == 0){ 
		type_flag = "remove";
		backend_option_flag = 0;
		fg_with_code_flag = 0;
		pin_count_flag = 0;
		package_type_flag = 0;
		ft_route_code_flag = 0;
		ft_route_flag = 0;
		ft_route_add_flag = 0;
		ft_route_add_flag2 = 0;
		ft_route_add_flag3 = 0;
		tf_comment_flag = 0;
		mcp_flag_flag = 0;
		mask_option_flag = 0;
		db_with_code_flag = 0;
		sort_route_code_flag = 0;
		ws_route_flag = 0;
		ws_route_add_flag = 0;
		tf_ws_comment_flag = 0;
		quality_level_flag = 0;
		quality_level_comment_flag = 0;
		ft_special_control_flag = 0;
		ws_special_control_flag = 0;
		avi_flag = 0;
		ink_flag = 0;
		
	}else if (flag == 1){ 
		type_flag = "insert";
		backend_option_flag = 1;
		fg_with_code_flag = 1;
		pin_count_flag = 1;
		package_type_flag = 1;
		ft_route_code_flag = 1;
		ft_route_flag = 1;
		ft_route_add_flag = 1;
		ft_route_add_flag2 = 1;
		ft_route_add_flag3 = 1;
		tf_comment_flag = 1;
		mcp_flag_flag = 1;
		mask_option_flag = 1;
		db_with_code_flag = 1;
		sort_route_code_flag = 1;
		ws_route_flag = 1;
		ws_route_add_flag = 1;
		tf_ws_comment_flag = 1;
	    quality_level_flag = 1;
	    quality_level_comment_flag = 1;
	    
	    ft_special_control_flag = 1;
	    ws_special_control_flag = 1;
	    avi_flag = 1;
	    ink_flag = 1;

	}else{
		backend_option_flag = 0;
		fg_with_code_flag = 0;
		pin_count_flag = 0;
		package_type_flag = 0;
		ft_route_code_flag = 0;
		ft_route_flag = 0;
		ft_route_add_flag = 0;
		ft_route_add_flag2 = 0;
		ft_route_add_flag3 = 0;
		tf_comment_flag = 0;
		mcp_flag_flag = 0;
		mask_option_flag = 0;
		db_with_code_flag = 0;
		sort_route_code_flag = 0;
		ws_route_flag = 0;
		ws_route_add_flag = 0;
		tf_ws_comment_flag = 0;
	    quality_level_flag = 0;
	    quality_level_comment_flag = 0;
        ft_special_control_flag = 0;
        ws_special_control_flag = 0;
        avi_flag = 0;
        ink_flag = 0;

	}

    try {
      //conn = DBConnection.getConnection();
      if (flag == 0 || flag == 1){
	      sqlStmt.append("SELECT '" + type_flag + "' TYPE_FLAG, SID, PRODUCT_BODY, BRAND, VERSION, BACKEND_OPTION, \n");
	      sqlStmt.append("FG_WITH_CODE, PIN_COUNT, PACKAGE_TYPE, FT_ROUTE, MCP_FLAG, MASK_OPTION, SORT_ROUTE_CODE, \n");
	      sqlStmt.append("DB_WITH_CODE, WS_ROUTE, WS_ROUTE_ADD, TF_COMMENT, FT_ROUTE_ADD,FT_ROUTE_ADD2,FT_ROUTE_ADD3, \n");
	      sqlStmt.append("FT_ROUTE_CODE, TF_WS_COMMENT,QUALITY_LEVEL,QUALITY_LEVEL_COMMENT,ENDURANCE, WSSPECIALCONTROL,tf_check_step('AVI',ws_route) avi,DECODE(tf_check_step('INK',WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',WS_ROUTE)) ink, \n");
	      sqlStmt.append(backend_option_flag + " BACKEND_OPTION_FLAG, " +  fg_with_code_flag + " FG_WITH_CODE_FLAG, " + pin_count_flag + " PIN_COUNT_FLAG, " + package_type_flag + " PACKAGE_TYPE_FLAG, " + ft_route_code_flag + " FT_ROUTE_CODE_FLAG, " + ft_route_flag + " FT_ROUTE_FLAG, \n");
	      sqlStmt.append(ft_route_add_flag + " FT_ROUTE_ADD_FLAG, "+ft_route_add_flag2 + " FT_ROUTE_ADD_FLAG2, "+ft_route_add_flag3 + " FT_ROUTE_ADD_FLAG3, " +  tf_comment_flag + " TF_COMMENT_FLAG, " + mcp_flag_flag + " MCP_FLAG_FLAG, " + mask_option_flag + " MASK_OPTION_FLAG, " + db_with_code_flag + " DB_WITH_CODE_FLAG, " + sort_route_code_flag + " SORT_ROUTE_CODE_FLAG, \n");
	      sqlStmt.append(ws_route_flag + " WS_ROUTE_FLAG, " +  ws_route_add_flag + " WS_ROUTE_ADD_FLAG, " + tf_ws_comment_flag + " TF_WS_COMMENT_FLAG, " + quality_level_flag + " QUALITY_LEVEL_FLAG, " + quality_level_comment_flag + " QUALITY_LEVEL_COMMENT_FLAG, \n");
	      sqlStmt.append(ft_special_control_flag + " FT_SPECIAL_CONTROL_FLAG, " +  ws_special_control_flag + " WS_SPECIAL_CONTROL_FLAG, " + avi_flag + " AVI_FLAG, " + ink_flag + " INK_FLAG  \n");
	      sqlStmt.append("FROM " + table1 + " T1 \n");
	      sqlStmt.append("WHERE SID = " + sid + " \n");
	      sqlStmt.append("AND T1.TAG != 2 \n");
	      sqlStmt.append("AND NOT EXISTS (SELECT 1 FROM " + table2 + " T2 \n");
	      sqlStmt.append("WHERE T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND \n");
	      sqlStmt.append("AND T2.TAG != 2 \n");
	      sqlStmt.append("AND SID = " + sid2 + " AND NVL(T2.BACKEND_OPTION, ' ') = NVL(T1.BACKEND_OPTION, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FG_WITH_CODE, ' ') = NVL(T1.FG_WITH_CODE, ' ') AND T2.PIN_COUNT = T1.PIN_COUNT \n");
	      sqlStmt.append("AND NVL(T2.PACKAGE_TYPE, ' ') = NVL(T1.PACKAGE_TYPE, ' ') AND NVL(T2.FT_ROUTE, ' ') = NVL(T1.FT_ROUTE, ' ') \n");
	      sqlStmt.append("AND NVL(T2.MCP_FLAG, ' ') = NVL(T1.MCP_FLAG, ' ') AND T2.MASK_OPTION = T1.MASK_OPTION AND T2.SORT_ROUTE_CODE = T1.SORT_ROUTE_CODE \n");
	      sqlStmt.append("AND T2.DB_WITH_CODE = T1.DB_WITH_CODE AND NVL(T2.WS_ROUTE, ' ') = NVL(T1.WS_ROUTE, ' ') \n");
	      sqlStmt.append("AND NVL(T2.WS_ROUTE_ADD, ' ') = NVL(T1.WS_ROUTE_ADD, ' ')  \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD, ' ') = NVL(T1.FT_ROUTE_ADD, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD2, ' ') = NVL(T1.FT_ROUTE_ADD2, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD3, ' ') = NVL(T1.FT_ROUTE_ADD3, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_CODE, ' ') = NVL(T1.FT_ROUTE_CODE, ' ') \n");
	      //sqlStmt.append("AND NVL(T2.TF_COMMENT, ' ') = NVL(T1.TF_COMMENT, ' ') AND NVL(T2.TF_WS_COMMENT, ' ') = NVL(T1.TF_WS_COMMENT, ' ')");
	      //sqlStmt.append("AND NVL(T2.QUALITY_LEVEL, ' ') = NVL(T1.QUALITY_LEVEL, ' ') \n");
	      sqlStmt.append("AND ((T1.BRAND='MX' AND NVL(T2.QUALITY_LEVEL, ' ') = NVL(T1.QUALITY_LEVEL, ' ')) OR T1.BRAND='KH') \n");
	      //sqlStmt.append("AND NVL(T2.QUALITY_LEVEL_COMMENT, ' ') = NVL(T1.QUALITY_LEVEL_COMMENT, ' ') \n");
	      sqlStmt.append(")");
      }else{
    	  sqlStmt.append("SELECT  'old' TYPE_FLAG, T2.SID, T2.PRODUCT_BODY, T2.BRAND, T2.VERSION, T2.BACKEND_OPTION, \n");
	      sqlStmt.append("T2.FG_WITH_CODE, T2.PIN_COUNT, T2.PACKAGE_TYPE, T2.FT_ROUTE, T2.MCP_FLAG, T2.MASK_OPTION, T2.SORT_ROUTE_CODE, \n");
	      sqlStmt.append("T2.DB_WITH_CODE, T2.WS_ROUTE, T2.WS_ROUTE_ADD, T2.TF_COMMENT, T2.FT_ROUTE_ADD,T2.FT_ROUTE_ADD2,T2.FT_ROUTE_ADD3, \n");
	      sqlStmt.append("T2.FT_ROUTE_CODE, T2.TF_WS_COMMENT,T2.QUALITY_LEVEL,T2.QUALITY_LEVEL_COMMENT,T2.ENDURANCE,T2.WSSPECIALCONTROL,tf_check_step('AVI',T2.ws_route) avi,DECODE(tf_check_step('INK',T2.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T2.WS_ROUTE)) ink, \n");
	      sqlStmt.append(backend_option_flag + " BACKEND_OPTION_FLAG, " +  fg_with_code_flag + " FG_WITH_CODE_FLAG, " + pin_count_flag + " PIN_COUNT_FLAG, " + package_type_flag + " PACKAGE_TYPE_FLAG, " + ft_route_code_flag + " FT_ROUTE_CODE_FLAG, " + ft_route_flag + " FT_ROUTE_FLAG, \n");
	      sqlStmt.append(ft_route_add_flag + " FT_ROUTE_ADD_FLAG, " +ft_route_add_flag2 + " FT_ROUTE_ADD_FLAG2, "+ft_route_add_flag3 + " FT_ROUTE_ADD_FLAG3, "+  tf_comment_flag + " TF_COMMENT_FLAG, " + mcp_flag_flag + " MCP_FLAG_FLAG, " + mask_option_flag + " MASK_OPTION_FLAG, " + db_with_code_flag + " DB_WITH_CODE_FLAG, " + sort_route_code_flag + " SORT_ROUTE_CODE_FLAG, \n");
	      sqlStmt.append(ws_route_flag + " WS_ROUTE_FLAG, " +  ws_route_add_flag + " WS_ROUTE_ADD_FLAG, " + tf_ws_comment_flag + " TF_WS_COMMENT_FLAG , " + quality_level_flag + " QUALITY_LEVEL_FLAG, " + quality_level_comment_flag + " QUALITY_LEVEL_COMMENT_FLAG, \n");
	      sqlStmt.append(ft_special_control_flag + " FT_SPECIAL_CONTROL_FLAG, " +  ws_special_control_flag + " WS_SPECIAL_CONTROL_FLAG, " + avi_flag + " AVI_FLAG, " + ink_flag + " INK_FLAG  \n");
	      sqlStmt.append("FROM " + table1 + " T1, " + table2 + " T2 \n");
	      sqlStmt.append("WHERE T1.SID = " + sid + " \n");
	      sqlStmt.append("AND T1.TAG != 2 \n");
	      sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND \n");
	      sqlStmt.append("AND T2.TAG != 2 \n");
	      sqlStmt.append("AND T2.SID = " + sid2 + " AND NVL(T2.BACKEND_OPTION, ' ') = NVL(T1.BACKEND_OPTION, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FG_WITH_CODE, ' ') = NVL(T1.FG_WITH_CODE, ' ') AND T2.PIN_COUNT = T1.PIN_COUNT \n");
	      sqlStmt.append("AND NVL(T2.PACKAGE_TYPE, ' ') = NVL(T1.PACKAGE_TYPE, ' ') AND NVL(T2.FT_ROUTE, ' ') = NVL(T1.FT_ROUTE, ' ') \n");
	      sqlStmt.append("AND NVL(T2.MCP_FLAG, ' ') = NVL(T1.MCP_FLAG, ' ') AND T2.MASK_OPTION = T1.MASK_OPTION AND T2.SORT_ROUTE_CODE = T1.SORT_ROUTE_CODE \n");
	      sqlStmt.append("AND T2.DB_WITH_CODE = T1.DB_WITH_CODE AND NVL(T2.WS_ROUTE, ' ') = NVL(T1.WS_ROUTE, ' ') \n");
	      sqlStmt.append("AND NVL(T2.WS_ROUTE_ADD, ' ') = NVL(T1.WS_ROUTE_ADD, ' ')  \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD, ' ') = NVL(T1.FT_ROUTE_ADD, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD2, ' ') = NVL(T1.FT_ROUTE_ADD2, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD3, ' ') = NVL(T1.FT_ROUTE_ADD3, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_CODE, ' ') = NVL(T1.FT_ROUTE_CODE, ' ') \n");
	      sqlStmt.append("AND (  \n");
          sqlStmt.append(" NVL(T2.TF_COMMENT, ' ') != NVL(T1.TF_COMMENT, ' ') \n");
          sqlStmt.append(" OR NVL(T2.TF_WS_COMMENT, ' ') != NVL(T1.TF_WS_COMMENT, ' ') \n");
          //sqlStmt.append(" OR NVL(T2.QUALITY_LEVEL_COMMENT, ' ') != NVL(T1.QUALITY_LEVEL_COMMENT, ' ') \n");
          sqlStmt.append("OR (T1.BRAND='MX' AND NVL(T2.QUALITY_LEVEL_COMMENT, ' ') != NVL(T1.QUALITY_LEVEL_COMMENT, ' '))  \n");
          sqlStmt.append(" OR NVL(T2.ENDURANCE, ' ') != NVL(T1.ENDURANCE, ' ') \n");
          sqlStmt.append(" OR NVL(T2.WSSPECIALCONTROL, ' ') != NVL(T1.WSSPECIALCONTROL, ' ') \n");
          sqlStmt.append(" OR NVL(tf_check_step('AVI',T2.ws_route), ' ') != NVL(tf_check_step('AVI',T1.ws_route), ' ') \n");
          sqlStmt.append(" OR NVL(DECODE(tf_check_step('INK',T2.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T2.WS_ROUTE)), ' ') != NVL(DECODE(tf_check_step('INK',T1.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T1.WS_ROUTE)), ' ') \n");
          sqlStmt.append(") \n");
          sqlStmt.append("AND ((T1.BRAND='MX' AND NVL(T2.QUALITY_LEVEL, ' ') = NVL(T1.QUALITY_LEVEL, ' ')) OR T1.BRAND='KH') \n");//20170109
	      sqlStmt.append("UNION \n");
	      sqlStmt.append("SELECT  'update' TYPE_FLAG, T1.SID, T1.PRODUCT_BODY, T1.BRAND, T1.VERSION, T1.BACKEND_OPTION, \n");
	      sqlStmt.append("T1.FG_WITH_CODE, T1.PIN_COUNT, T1.PACKAGE_TYPE, T1.FT_ROUTE, T1.MCP_FLAG, T1.MASK_OPTION, T1.SORT_ROUTE_CODE, \n");
	      sqlStmt.append("T1.DB_WITH_CODE, T1.WS_ROUTE, T1.WS_ROUTE_ADD, T1.TF_COMMENT, T1.FT_ROUTE_ADD,T1.FT_ROUTE_ADD2,T1.FT_ROUTE_ADD3, \n");
	      sqlStmt.append("T1.FT_ROUTE_CODE, T1.TF_WS_COMMENT,T1.QUALITY_LEVEL,T1.QUALITY_LEVEL_COMMENT, \n");
	      sqlStmt.append("T1.ENDURANCE,T1.WSSPECIALCONTROL, tf_check_step('AVI',T1.ws_route) avi,DECODE(tf_check_step('INK',T1.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T1.WS_ROUTE)) ink,");
	      sqlStmt.append("decode(nvl(T2.BACKEND_OPTION, '#'),nvl(T1.BACKEND_OPTION, '#'),0,1) BACKEND_OPTION_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.FG_WITH_CODE, '#'),nvl(T1.FG_WITH_CODE, '#'),0,1) FG_WITH_CODE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.PIN_COUNT, -999),nvl(T1.PIN_COUNT, -999),0,1) PIN_COUNT_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.PACKAGE_TYPE, '#'),nvl(T1.PACKAGE_TYPE, '#'),0,1) PACKAGE_TYPE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.FT_ROUTE_CODE, '#'),nvl(T1.FT_ROUTE_CODE, '#'),0,1) FT_ROUTE_CODE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.FT_ROUTE, '#'),nvl(T1.FT_ROUTE, '#'),0,1) FT_ROUTE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.FT_ROUTE_ADD, '#'),nvl(T1.FT_ROUTE_ADD, '#'),0,1) FT_ROUTE_ADD_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.FT_ROUTE_ADD2, '#'),nvl(T1.FT_ROUTE_ADD2, '#'),0,1) FT_ROUTE_ADD_FLAG2, \n");
	      sqlStmt.append("decode(nvl(T2.FT_ROUTE_ADD3, '#'),nvl(T1.FT_ROUTE_ADD3, '#'),0,1) FT_ROUTE_ADD_FLAG3, \n");
	      sqlStmt.append("decode(nvl(T2.TF_COMMENT, '#'),nvl(T1.TF_COMMENT, '#'),0,1) TF_COMMENT_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.MCP_FLAG, '#'),nvl(T1.MCP_FLAG, '#'),0,1) MCP_FLAG_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.MASK_OPTION, '#'),nvl(T1.MASK_OPTION, '#'),0,1) MASK_OPTION_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.DB_WITH_CODE, '#'),nvl(T1.DB_WITH_CODE, '#'),0,1) DB_WITH_CODE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.SORT_ROUTE_CODE, '#'),nvl(T1.SORT_ROUTE_CODE, '#'),0,1) SORT_ROUTE_CODE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.WS_ROUTE, '#'),nvl(T1.WS_ROUTE, '#'),0,1) WS_ROUTE_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.WS_ROUTE_ADD, '#'),nvl(T1.WS_ROUTE_ADD, '#'),0,1) WS_ROUTE_ADD_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.TF_WS_COMMENT, '#'),nvl(T1.TF_WS_COMMENT, '#'),0,1) TF_WS_COMMENT_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.QUALITY_LEVEL, '#'),nvl(T1.QUALITY_LEVEL, '#'),0,1) QUALITY_LEVEL_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.QUALITY_LEVEL_COMMENT, '#'),nvl(T1.QUALITY_LEVEL_COMMENT, '#'),0,1) QUALITY_LEVEL_COMMENT_FLAG, \n");
	      
	      sqlStmt.append("decode(nvl(T2.ENDURANCE, '#'),nvl(T1.ENDURANCE, '#'),0,1) FT_SPECIAL_CONTROL_FLAG, \n");
	      sqlStmt.append("decode(nvl(T2.WSSPECIALCONTROL, '#'),nvl(T1.WSSPECIALCONTROL, '#'),0,1) WS_SPECIAL_CONTROL_FLAG, \n");
	      sqlStmt.append("decode(nvl(tf_check_step('AVI',T2.ws_route), '#'),nvl(tf_check_step('AVI',T1.ws_route), '#'),0,1) AVI_FLAG, \n");
	      sqlStmt.append("decode(nvl(DECODE(tf_check_step('INK',T2.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T2.WS_ROUTE)), '#'),nvl(DECODE(tf_check_step('INK',T1.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T1.WS_ROUTE)), '#'),0,1) INK_FLAG \n");
	      
	      
	      sqlStmt.append("FROM " + table1 + " T1, " + table2 + " T2 \n");
	      sqlStmt.append("WHERE T1.SID = " + sid + " \n");
	      sqlStmt.append("AND T1.TAG != 2 \n");
	      sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND \n");
	      sqlStmt.append("AND T2.TAG != 2 \n");
	      sqlStmt.append("AND T2.SID = " + sid2 + " AND NVL(T2.BACKEND_OPTION, ' ') = NVL(T1.BACKEND_OPTION, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FG_WITH_CODE, ' ') = NVL(T1.FG_WITH_CODE, ' ') AND T2.PIN_COUNT = T1.PIN_COUNT \n");
	      sqlStmt.append("AND NVL(T2.PACKAGE_TYPE, ' ') = NVL(T1.PACKAGE_TYPE, ' ') AND NVL(T2.FT_ROUTE, ' ') = NVL(T1.FT_ROUTE, ' ') \n");
	      sqlStmt.append("AND NVL(T2.MCP_FLAG, ' ') = NVL(T1.MCP_FLAG, ' ') AND T2.MASK_OPTION = T1.MASK_OPTION AND T2.SORT_ROUTE_CODE = T1.SORT_ROUTE_CODE \n");
	      sqlStmt.append("AND T2.DB_WITH_CODE = T1.DB_WITH_CODE AND NVL(T2.WS_ROUTE, ' ') = NVL(T1.WS_ROUTE, ' ') \n");
	      sqlStmt.append("AND NVL(T2.WS_ROUTE_ADD, ' ') = NVL(T1.WS_ROUTE_ADD, ' ')  \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD, ' ') = NVL(T1.FT_ROUTE_ADD, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD2, ' ') = NVL(T1.FT_ROUTE_ADD2, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD3, ' ') = NVL(T1.FT_ROUTE_ADD3, ' ') \n");
	      sqlStmt.append("AND NVL(T2.FT_ROUTE_CODE, ' ') = NVL(T1.FT_ROUTE_CODE, ' ') \n");
	      sqlStmt.append("AND ( \n");
	      sqlStmt.append(" NVL(T2.TF_COMMENT, ' ') != NVL(T1.TF_COMMENT, ' ') \n");
	      sqlStmt.append(" OR NVL(T2.TF_WS_COMMENT, ' ') != NVL(T1.TF_WS_COMMENT, ' ') \n");
	      //sqlStmt.append(" OR NVL(T2.QUALITY_LEVEL_COMMENT, ' ') != NVL(T1.QUALITY_LEVEL_COMMENT, ' ') \n");
	      sqlStmt.append("OR (T1.BRAND='MX' AND NVL(T2.QUALITY_LEVEL_COMMENT, ' ') != NVL(T1.QUALITY_LEVEL_COMMENT, ' '))  \n");
	      sqlStmt.append(" OR NVL(T2.ENDURANCE, ' ') != NVL(T1.ENDURANCE, ' ')  \n");
          sqlStmt.append(" OR NVL(T2.WSSPECIALCONTROL, ' ') != NVL(T1.WSSPECIALCONTROL, ' ')  \n");
          sqlStmt.append(" OR NVL(tf_check_step('AVI',T2.ws_route), ' ') != NVL(tf_check_step('AVI',T1.ws_route), ' ')  \n");
          sqlStmt.append(" OR NVL(DECODE(tf_check_step('INK',T2.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T2.WS_ROUTE)), ' ') != NVL(DECODE(tf_check_step('INK',T1.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T1.WS_ROUTE)), ' ')  \n");
	      sqlStmt.append(") \n");

	      //sqlStmt.append("AND NVL(T2.QUALITY_LEVEL, ' ') = NVL(T1.QUALITY_LEVEL, ' ')  \n");
	      sqlStmt.append("AND ((T1.BRAND='MX' AND NVL(T2.QUALITY_LEVEL, ' ') = NVL(T1.QUALITY_LEVEL, ' ')) OR T1.BRAND='KH') \n");
    	  
	      
      }
      TDSLogger.println(sqlStmt.toString());
      PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        EditiionCompareActionForm bom = new EditiionCompareActionForm();
        bom.setSid(rs.getString("SID"));
        bom.setProductbody(rs.getString("PRODUCT_BODY"));
        bom.setBrand(rs.getString("BRAND"));
        bom.setVersion(rs.getString("VERSION"));
        bom.setBeoption(rs.getString("BACKEND_OPTION"));
        bom.setFgwithcode(rs.getString("FG_WITH_CODE"));
        bom.setPincount(rs.getString("PIN_COUNT"));
        bom.setPkgtype(rs.getString("PACKAGE_TYPE"));
        bom.setFtroute(rs.getString("FT_ROUTE"));
        bom.setMcp_flag(rs.getString("MCP_FLAG"));
        bom.setMaskopt(rs.getString("MASK_OPTION"));
        bom.setSortroutecode(rs.getString("SORT_ROUTE_CODE"));
        bom.setDbwithcode(rs.getString("DB_WITH_CODE"));
        bom.setWsroute(rs.getString("WS_ROUTE"));
        bom.setWsaddroute(rs.getString("WS_ROUTE_ADD"));
        bom.setComment(rs.getString("TF_COMMENT"));
        bom.setFt_route_add(rs.getString("FT_ROUTE_ADD"));
        bom.setFt_route_add2(rs.getString("FT_ROUTE_ADD2"));
        bom.setFt_route_add3(rs.getString("FT_ROUTE_ADD3"));
        bom.setFt_route_code(rs.getString("FT_ROUTE_CODE"));
        bom.setWscomment(rs.getString("TF_WS_COMMENT"));
        bom.setQuality_level(rs.getString("QUALITY_LEVEL"));
        bom.setQuality_level_comment(rs.getString("QUALITY_LEVEL_COMMENT"));
        
        bom.setFt_special_control(rs.getString("ENDURANCE"));
        bom.setWs_special_control(rs.getString("WSSPECIALCONTROL"));
        bom.setAvi(rs.getString("avi"));
        bom.setInk(rs.getString("ink"));
        
        bom.setType_flag(rs.getString("TYPE_FLAG"));
        bom.setBackend_option_flag(rs.getInt("BACKEND_OPTION_FLAG"));
        bom.setFg_with_code_flag(rs.getInt("FG_WITH_CODE_FLAG"));
        bom.setPin_count_flag(rs.getInt("PIN_COUNT_FLAG"));
        bom.setPackage_type_flag(rs.getInt("PACKAGE_TYPE_FLAG"));
        bom.setFt_route_code_flag(rs.getInt("FT_ROUTE_CODE_FLAG"));
        bom.setFt_route_flag(rs.getInt("FT_ROUTE_FLAG"));
        bom.setFt_route_add_flag(rs.getInt("FT_ROUTE_ADD_FLAG"));
        bom.setFt_route_add_flag2(rs.getInt("FT_ROUTE_ADD_FLAG2"));
        bom.setFt_route_add_flag3(rs.getInt("FT_ROUTE_ADD_FLAG3"));
        bom.setTf_comment_flag(rs.getInt("TF_COMMENT_FLAG"));
        bom.setMcp_flag_flag(rs.getInt("MCP_FLAG_FLAG"));
        bom.setMask_option_flag(rs.getInt("MASK_OPTION_FLAG"));
        bom.setDb_with_code_flag(rs.getInt("DB_WITH_CODE_FLAG"));
        bom.setSort_route_code_flag(rs.getInt("SORT_ROUTE_CODE_FLAG"));
        bom.setWs_route_flag(rs.getInt("WS_ROUTE_FLAG"));
        bom.setWs_route_add_flag(rs.getInt("WS_ROUTE_ADD_FLAG"));
        bom.setTf_ws_comment_flag(rs.getInt("TF_WS_COMMENT_FLAG"));
        bom.setQuality_level_flag(rs.getInt("QUALITY_LEVEL_FLAG"));
        bom.setQuality_level_comment_flag(rs.getInt("QUALITY_LEVEL_COMMENT_FLAG"));
        
        bom.setFt_special_control_flag(rs.getInt("FT_SPECIAL_CONTROL_FLAG"));
        bom.setWs_special_control_flag(rs.getInt("WS_SPECIAL_CONTROL_FLAG"));
        bom.setAvi_flag(rs.getInt("AVI_FLAG"));
        bom.setInk_flag(rs.getInt("INK_FLAG"));
        
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

  public static EditiionCompareActionForm[] CompareBomMcp(Connection conn,
			String sid, String sid2, String status, int flag, int compareType) {

		StringBuffer sqlStmt = new StringBuffer();
		// Connection conn = null;
		String table1 = null;
		String table2 = null;
		String type_flag = null;
		int backend_option_flag = 0;
		int fg_with_code_flag = 0;
		int pin_count_flag = 0;
		int package_type_flag = 0;
		int ft_route_code_flag = 0;
		int ft_route_flag = 0;
		int ft_route_add_flag = 0;
		int ft_route_add_flag2 = 0;
		int ft_route_add_flag3 = 0;
		int tf_comment_flag = 0;
		//int mask_option_flag = 0;
		int db_with_code_flag = 0;
		int sort_route_code_flag = 0;
		int ws_route_flag = 0;
		int ws_route_add_flag = 0;
		int tf_ws_comment_flag = 0;
		int quality_level_flag = 0;
		int quality_level_comment_flag = 0;
		int component_no_flag = 0;
		int com_prod_body_flag = 0;
		int com_mask_option_flag = 0;
		int com_backend_option_flag = 0;

		int ft_special_control_flag = 0;
		int ws_special_control_flag = 0;
		int avi_flag = 0;
		int ink_flag = 0;

		ArrayList tmp2 = new ArrayList();
		if (!status.equals("P") && !status.equals("A")) {
			table1 = "TF_BOM_ROUTE_MCP";
			table2 = "TF_BOM_ROUTE_MCP";
		} else {
			if (flag == 0) {/* 以前一版為主找出後一版之不同處 */
				table1 = "TF_BOM_ROUTE_MCP";
				table2 = "TF_BOM_ROUTE_MCP_TX";
			} else if (flag == 1) { /* 由後一版找出前一版與其不同之處 */
				table1 = "TF_BOM_ROUTE_MCP_TX";
				table2 = "TF_BOM_ROUTE_MCP";
			} else { /* 被 update 的資料之處 */
				table1 = "TF_BOM_ROUTE_MCP_TX";
				table2 = "TF_BOM_ROUTE_MCP";
			}
		}
		if (flag == 0) {
			type_flag = "remove";
			backend_option_flag = 0;
			fg_with_code_flag = 0;
			pin_count_flag = 0;
			package_type_flag = 0;
			ft_route_code_flag = 0;
			ft_route_flag = 0;
			ft_route_add_flag = 0;
			ft_route_add_flag2 = 0;
			ft_route_add_flag3 = 0;
			tf_comment_flag = 0;
			//mask_option_flag = 0;
			db_with_code_flag = 0;
			sort_route_code_flag = 0;
			ws_route_flag = 0;
			ws_route_add_flag = 0;
			tf_ws_comment_flag = 0;
			quality_level_flag = 0;
			quality_level_comment_flag = 0;
			component_no_flag = 0;
			com_prod_body_flag = 0;
			com_mask_option_flag = 0;
			com_backend_option_flag = 0;
			ft_special_control_flag = 0;
			ws_special_control_flag = 0;
			avi_flag = 0;
			ink_flag = 0;

		} else if (flag == 1) {
			type_flag = "insert";
			backend_option_flag = 1;
			fg_with_code_flag = 1;
			pin_count_flag = 1;
			package_type_flag = 1;
			ft_route_code_flag = 1;
			ft_route_flag = 1;
			ft_route_add_flag = 1;
			ft_route_add_flag2 = 1;
			ft_route_add_flag3 = 1;
			tf_comment_flag = 1;
			//mask_option_flag = 1;
			db_with_code_flag = 1;
			sort_route_code_flag = 1;
			ws_route_flag = 1;
			ws_route_add_flag = 1;
			tf_ws_comment_flag = 1;
			quality_level_flag = 1;
			quality_level_comment_flag = 1;
			component_no_flag = 1;
			com_prod_body_flag = 1;
			com_mask_option_flag = 1;
			com_backend_option_flag = 1;

			ft_special_control_flag = 1;
			ws_special_control_flag = 1;
			avi_flag = 1;
			ink_flag = 1;

		} else {
			backend_option_flag = 0;
			fg_with_code_flag = 0;
			pin_count_flag = 0;
			package_type_flag = 0;
			ft_route_code_flag = 0;
			ft_route_flag = 0;
			ft_route_add_flag = 0;
			ft_route_add_flag2 = 0;
			ft_route_add_flag3 = 0;
			tf_comment_flag = 0;
			//mask_option_flag = 0;
			db_with_code_flag = 0;
			sort_route_code_flag = 0;
			ws_route_flag = 0;
			ws_route_add_flag = 0;
			tf_ws_comment_flag = 0;
			quality_level_flag = 0;
			quality_level_comment_flag = 0;
			component_no_flag = 0;
			com_prod_body_flag = 0;
			com_mask_option_flag = 0;
			com_backend_option_flag = 0;
			ft_special_control_flag = 0;
			ws_special_control_flag = 0;
			avi_flag = 0;
			ink_flag = 0;
		}

		try {
			// conn = DBConnection.getConnection();
			if (flag == 0 || flag == 1) {
				sqlStmt.append("SELECT '" + type_flag + "' TYPE_FLAG, SID, PRODUCT_BODY, BRAND, VERSION, BACKEND_OPTION, \n");
				sqlStmt.append("FG_WITH_CODE, PIN_COUNT, PACKAGE_TYPE, FT_ROUTE, SORT_ROUTE_CODE, \n");
				sqlStmt.append("DB_WITH_CODE, WS_ROUTE, WS_ROUTE_ADD, TF_COMMENT, FT_ROUTE_ADD,FT_ROUTE_ADD2,FT_ROUTE_ADD3, \n");
				sqlStmt.append("FT_ROUTE_CODE, TF_WS_COMMENT,QUALITY_LEVEL,QUALITY_LEVEL_COMMENT,");
				sqlStmt.append("COMPONENT_NO, COM_PROD_BODY,COM_MASK_OPTION,COM_BACKEND_OPTION,");
				sqlStmt.append("ENDURANCE, WSSPECIALCONTROL,tf_check_step('AVI',ws_route) avi,DECODE(tf_check_step('INK',WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',WS_ROUTE)) ink, \n");
				sqlStmt.append(backend_option_flag + " BACKEND_OPTION_FLAG, "
						+ fg_with_code_flag + " FG_WITH_CODE_FLAG, "
						+ pin_count_flag + " PIN_COUNT_FLAG, "
						+ package_type_flag + " PACKAGE_TYPE_FLAG, "
						+ ft_route_code_flag + " FT_ROUTE_CODE_FLAG, "
						+ ft_route_flag + " FT_ROUTE_FLAG, \n");
				sqlStmt.append(ft_route_add_flag + " FT_ROUTE_ADD_FLAG, "
						+ ft_route_add_flag2 + " FT_ROUTE_ADD_FLAG2, "
						+ ft_route_add_flag3 + " FT_ROUTE_ADD_FLAG3, "
						+ tf_comment_flag + " TF_COMMENT_FLAG, "
						//+ mask_option_flag + " MASK_OPTION_FLAG, "
						+ db_with_code_flag + " DB_WITH_CODE_FLAG, "
						+ sort_route_code_flag + " SORT_ROUTE_CODE_FLAG, \n");
				sqlStmt.append(ws_route_flag + " WS_ROUTE_FLAG, "
						+ ws_route_add_flag + " WS_ROUTE_ADD_FLAG, "
						+ tf_ws_comment_flag + " TF_WS_COMMENT_FLAG, "
						+ quality_level_flag + " QUALITY_LEVEL_FLAG, "
						+ quality_level_comment_flag + " QUALITY_LEVEL_COMMENT_FLAG, \n");
				sqlStmt.append(component_no_flag + " COMPONENT_NO_FLAG, "
						+ com_prod_body_flag + " COM_PROD_BODY_FLAG, "
						+ com_mask_option_flag + " COM_MASK_OPTION_FLAG, "
						+ com_backend_option_flag + " COM_BACKEND_OPTION_FLAG, \n");
				sqlStmt.append(ft_special_control_flag + " FT_SPECIAL_CONTROL_FLAG, "
						+ ws_special_control_flag + " WS_SPECIAL_CONTROL_FLAG, " 
						+ avi_flag + " AVI_FLAG, " 
						+ ink_flag + " INK_FLAG  \n");
				sqlStmt.append("FROM " + table1 + " T1 \n");
				sqlStmt.append("WHERE SID = " + sid + " \n");
				sqlStmt.append("AND T1.TAG != 2 \n");
				sqlStmt.append("AND NOT EXISTS (SELECT 1 FROM " + table2 + " T2 \n");
				sqlStmt.append("WHERE T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND \n");
				sqlStmt.append("AND T2.TAG != 2 \n");
				sqlStmt.append("AND SID = " + sid2 + " AND T2.BACKEND_OPTION = T1.BACKEND_OPTION \n");
				sqlStmt.append("AND T2.FG_WITH_CODE = T1.FG_WITH_CODE AND T2.PIN_COUNT = T1.PIN_COUNT \n");
				sqlStmt.append("AND T2.PACKAGE_TYPE = T1.PACKAGE_TYPE AND T2.FT_ROUTE = T1.FT_ROUTE \n");
				sqlStmt.append("AND NVL(T2.SORT_ROUTE_CODE, ' ') = NVL(T1.SORT_ROUTE_CODE, ' ') \n");
				sqlStmt.append("AND NVL(T2.DB_WITH_CODE, ' ') = NVL(T1.DB_WITH_CODE, ' ') AND NVL(T2.WS_ROUTE, ' ') = NVL(T1.WS_ROUTE, ' ') \n");				
				sqlStmt.append("AND NVL(T2.WS_ROUTE_ADD, ' ') = NVL(T1.WS_ROUTE_ADD, ' ')  \n");
				sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD, ' ') = NVL(T1.FT_ROUTE_ADD, ' ') \n");
				sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD2, ' ') = NVL(T1.FT_ROUTE_ADD2, ' ') \n");
				sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD3, ' ') = NVL(T1.FT_ROUTE_ADD3, ' ') \n");
				sqlStmt.append("AND NVL(T2.FT_ROUTE_CODE, ' ') = NVL(T1.FT_ROUTE_CODE, ' ') \n");
				// sqlStmt.append("AND NVL(T2.TF_COMMENT, ' ') = NVL(T1.TF_COMMENT, ' ') AND NVL(T2.TF_WS_COMMENT, ' ') = NVL(T1.TF_WS_COMMENT, ' ')");
				//sqlStmt.append("AND NVL(T2.QUALITY_LEVEL, ' ') = NVL(T1.QUALITY_LEVEL, ' ') \n");
				sqlStmt.append("AND ((T1.BRAND='MX' AND NVL(T2.QUALITY_LEVEL, ' ') = NVL(T1.QUALITY_LEVEL, ' ')) OR T1.BRAND='KH') \n");
				
				// sqlStmt.append("AND NVL(T2.QUALITY_LEVEL_COMMENT, ' ') = NVL(T1.QUALITY_LEVEL_COMMENT, ' ') \n");
				sqlStmt.append("AND NVL(T2.COMPONENT_NO, -1) = NVL(T1.COMPONENT_NO, -1) \n");
				sqlStmt.append("AND NVL(T2.COM_PROD_BODY, ' ') = NVL(T1.COM_PROD_BODY, ' ') \n");
				sqlStmt.append("AND NVL(T2.COM_MASK_OPTION, ' ') = NVL(T1.COM_MASK_OPTION, ' ') \n");
				sqlStmt.append("AND NVL(T2.COM_BACKEND_OPTION, ' ') = NVL(T1.COM_BACKEND_OPTION, ' ') \n");
				sqlStmt.append(")\n");
			} else {
				sqlStmt.append("SELECT  'old' TYPE_FLAG, T2.SID, T2.PRODUCT_BODY, T2.BRAND, T2.VERSION, T2.BACKEND_OPTION, \n");
				sqlStmt.append("T2.FG_WITH_CODE, T2.PIN_COUNT, T2.PACKAGE_TYPE, T2.FT_ROUTE, T2.SORT_ROUTE_CODE, \n");
				sqlStmt.append("T2.DB_WITH_CODE, T2.WS_ROUTE, T2.WS_ROUTE_ADD, T2.TF_COMMENT, T2.FT_ROUTE_ADD,T2.FT_ROUTE_ADD2,T2.FT_ROUTE_ADD3, \n");
				sqlStmt.append("T2.FT_ROUTE_CODE, T2.TF_WS_COMMENT,T2.QUALITY_LEVEL,T2.QUALITY_LEVEL_COMMENT,");
				sqlStmt.append("T2.COMPONENT_NO,T2.COM_PROD_BODY,T2.COM_MASK_OPTION,T2.COM_BACKEND_OPTION,");				
				sqlStmt.append("T2.ENDURANCE,T2.WSSPECIALCONTROL,tf_check_step('AVI',T2.ws_route) avi,DECODE(tf_check_step('INK',T2.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T2.WS_ROUTE)) ink, \n");
				sqlStmt.append(backend_option_flag + " BACKEND_OPTION_FLAG, "
						+ fg_with_code_flag + " FG_WITH_CODE_FLAG, "
						+ pin_count_flag + " PIN_COUNT_FLAG, "
						+ package_type_flag + " PACKAGE_TYPE_FLAG, "
						+ ft_route_code_flag + " FT_ROUTE_CODE_FLAG, "
						+ ft_route_flag + " FT_ROUTE_FLAG, \n");
				sqlStmt.append(ft_route_add_flag + " FT_ROUTE_ADD_FLAG, "
						+ ft_route_add_flag2 + " FT_ROUTE_ADD_FLAG2, "
						+ ft_route_add_flag3 + " FT_ROUTE_ADD_FLAG3, "
						+ tf_comment_flag + " TF_COMMENT_FLAG, "
						//+ mask_option_flag + " MASK_OPTION_FLAG, "
						+ db_with_code_flag + " DB_WITH_CODE_FLAG, "
						+ sort_route_code_flag + " SORT_ROUTE_CODE_FLAG, \n");
				sqlStmt.append(ws_route_flag + " WS_ROUTE_FLAG, "
						+ ws_route_add_flag + " WS_ROUTE_ADD_FLAG, "
						+ tf_ws_comment_flag + " TF_WS_COMMENT_FLAG , "
						+ quality_level_flag + " QUALITY_LEVEL_FLAG, "
						+ quality_level_comment_flag + " QUALITY_LEVEL_COMMENT_FLAG, \n");
				sqlStmt.append(component_no_flag + " COMPONENT_NO_FLAG, "
						+ com_prod_body_flag + " COM_PROD_BODY_FLAG, "
						+ com_mask_option_flag + " COM_MASK_OPTION_FLAG, "
						+ com_backend_option_flag + " COM_BACKEND_OPTION_FLAG, \n");				
				sqlStmt.append(ft_special_control_flag	+ " FT_SPECIAL_CONTROL_FLAG, "
						+ ws_special_control_flag + " WS_SPECIAL_CONTROL_FLAG, " 
						+ avi_flag + " AVI_FLAG, " + ink_flag + " INK_FLAG  \n");
				sqlStmt.append("FROM " + table1 + " T1, " + table2 + " T2 \n");
				sqlStmt.append("WHERE T1.SID = " + sid + " \n");
				sqlStmt.append("AND T1.TAG != 2 \n");
				sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND \n");
				sqlStmt.append("AND T2.TAG != 2 \n");
				sqlStmt.append("AND T2.SID = " + sid2 + " AND T2.BACKEND_OPTION = T1.BACKEND_OPTION \n");
				sqlStmt.append("AND T2.FG_WITH_CODE = T1.FG_WITH_CODE AND T2.PIN_COUNT = T1.PIN_COUNT \n");
				sqlStmt.append("AND T2.PACKAGE_TYPE = T1.PACKAGE_TYPE AND T2.FT_ROUTE = T1.FT_ROUTE \n");
				sqlStmt.append("AND NVL(T2.SORT_ROUTE_CODE, ' ') = NVL(T1.SORT_ROUTE_CODE, ' ') \n");
				sqlStmt.append("AND NVL(T2.DB_WITH_CODE, ' ') = NVL(T1.DB_WITH_CODE, ' ') AND NVL(T2.WS_ROUTE, ' ') = NVL(T1.WS_ROUTE, ' ') \n");
				sqlStmt.append("AND NVL(T2.WS_ROUTE_ADD, ' ') = NVL(T1.WS_ROUTE_ADD, ' ')  \n");
				sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD, ' ') = NVL(T1.FT_ROUTE_ADD, ' ') \n");
				sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD2, ' ') = NVL(T1.FT_ROUTE_ADD2, ' ') \n");
				sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD3, ' ') = NVL(T1.FT_ROUTE_ADD3, ' ') \n");
				sqlStmt.append("AND NVL(T2.FT_ROUTE_CODE, ' ') = NVL(T1.FT_ROUTE_CODE, ' ') \n");
				sqlStmt.append("AND NVL(T2.COMPONENT_NO, -1) = NVL(T1.COMPONENT_NO, -1) \n");
				sqlStmt.append("AND NVL(T2.COM_PROD_BODY, ' ') = NVL(T1.COM_PROD_BODY, ' ') \n");
				sqlStmt.append("AND NVL(T2.COM_MASK_OPTION, ' ') = NVL(T1.COM_MASK_OPTION, ' ') \n");
				sqlStmt.append("AND (  \n");
				sqlStmt.append(" NVL(T2.TF_COMMENT, ' ') != NVL(T1.TF_COMMENT, ' ') \n");
				sqlStmt.append(" OR NVL(T2.TF_WS_COMMENT, ' ') != NVL(T1.TF_WS_COMMENT, ' ') \n");
				//sqlStmt.append(" OR NVL(T2.QUALITY_LEVEL_COMMENT, ' ') != NVL(T1.QUALITY_LEVEL_COMMENT, ' ') \n");
				sqlStmt.append("OR (T1.BRAND='MX' AND NVL(T2.QUALITY_LEVEL_COMMENT, ' ') != NVL(T1.QUALITY_LEVEL_COMMENT, ' '))  \n");
				//sqlStmt.append(" OR NVL(T2.COMPONENT_NO, ' ') != NVL(T1.COMPONENT_NO, ' ') \n");
				//sqlStmt.append(" OR NVL(T2.COM_PROD_BODY, ' ') != NVL(T1.COM_PROD_BODY, ' ') \n");
				//sqlStmt.append(" OR NVL(T2.COM_MASK_OPTION, ' ') != NVL(T1.COM_MASK_OPTION, ' ') \n");
				sqlStmt.append(" OR NVL(T2.COM_BACKEND_OPTION, ' ') != NVL(T1.COM_BACKEND_OPTION, ' ') \n");
				sqlStmt.append(" OR NVL(T2.ENDURANCE, ' ') != NVL(T1.ENDURANCE, ' ') \n");
				sqlStmt.append(" OR NVL(T2.WSSPECIALCONTROL, ' ') != NVL(T1.WSSPECIALCONTROL, ' ') \n");
				sqlStmt.append(" OR NVL(tf_check_step('AVI',T2.ws_route), ' ') != NVL(tf_check_step('AVI',T1.ws_route), ' ') \n");
				sqlStmt.append(" OR NVL(DECODE(tf_check_step('INK',T2.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T2.WS_ROUTE)), ' ') != NVL(DECODE(tf_check_step('INK',T1.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T1.WS_ROUTE)), ' ') \n");
				sqlStmt.append(") \n");
				sqlStmt.append("UNION \n");
				sqlStmt.append("SELECT  'update' TYPE_FLAG, T1.SID, T1.PRODUCT_BODY, T1.BRAND, T1.VERSION, T1.BACKEND_OPTION, \n");
				sqlStmt.append("T1.FG_WITH_CODE, T1.PIN_COUNT, T1.PACKAGE_TYPE, T1.FT_ROUTE, T1.SORT_ROUTE_CODE, \n");
				sqlStmt.append("T1.DB_WITH_CODE, T1.WS_ROUTE, T1.WS_ROUTE_ADD, T1.TF_COMMENT, T1.FT_ROUTE_ADD,T1.FT_ROUTE_ADD2,T1.FT_ROUTE_ADD3, \n");
				sqlStmt.append("T1.FT_ROUTE_CODE, T1.TF_WS_COMMENT,T1.QUALITY_LEVEL,T1.QUALITY_LEVEL_COMMENT, \n");
				sqlStmt.append("T2.COMPONENT_NO,T2.COM_PROD_BODY,T2.COM_MASK_OPTION,T2.COM_BACKEND_OPTION, \n");
				sqlStmt.append("T1.ENDURANCE,T1.WSSPECIALCONTROL, tf_check_step('AVI',T1.ws_route) avi,DECODE(tf_check_step('INK',T1.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T1.WS_ROUTE)) ink,");
				sqlStmt.append("decode(nvl(T2.BACKEND_OPTION, '#'),nvl(T1.BACKEND_OPTION, '#'),0,1) BACKEND_OPTION_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.FG_WITH_CODE, '#'),nvl(T1.FG_WITH_CODE, '#'),0,1) FG_WITH_CODE_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.PIN_COUNT, -999),nvl(T1.PIN_COUNT, -999),0,1) PIN_COUNT_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.PACKAGE_TYPE, '#'),nvl(T1.PACKAGE_TYPE, '#'),0,1) PACKAGE_TYPE_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.FT_ROUTE_CODE, '#'),nvl(T1.FT_ROUTE_CODE, '#'),0,1) FT_ROUTE_CODE_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.FT_ROUTE, '#'),nvl(T1.FT_ROUTE, '#'),0,1) FT_ROUTE_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.FT_ROUTE_ADD, '#'),nvl(T1.FT_ROUTE_ADD, '#'),0,1) FT_ROUTE_ADD_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.FT_ROUTE_ADD2, '#'),nvl(T1.FT_ROUTE_ADD2, '#'),0,1) FT_ROUTE_ADD_FLAG2, \n");
				sqlStmt.append("decode(nvl(T2.FT_ROUTE_ADD3, '#'),nvl(T1.FT_ROUTE_ADD3, '#'),0,1) FT_ROUTE_ADD_FLAG3, \n");
				sqlStmt.append("decode(nvl(T2.TF_COMMENT, '#'),nvl(T1.TF_COMMENT, '#'),0,1) TF_COMMENT_FLAG, \n");
				//sqlStmt.append("decode(nvl(T2.MASK_OPTION, '#'),nvl(T1.MASK_OPTION, '#'),0,1) MASK_OPTION_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.DB_WITH_CODE, '#'),nvl(T1.DB_WITH_CODE, '#'),0,1) DB_WITH_CODE_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.SORT_ROUTE_CODE, '#'),nvl(T1.SORT_ROUTE_CODE, '#'),0,1) SORT_ROUTE_CODE_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.WS_ROUTE, '#'),nvl(T1.WS_ROUTE, '#'),0,1) WS_ROUTE_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.WS_ROUTE_ADD, '#'),nvl(T1.WS_ROUTE_ADD, '#'),0,1) WS_ROUTE_ADD_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.TF_WS_COMMENT, '#'),nvl(T1.TF_WS_COMMENT, '#'),0,1) TF_WS_COMMENT_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.QUALITY_LEVEL, '#'),nvl(T1.QUALITY_LEVEL, '#'),0,1) QUALITY_LEVEL_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.QUALITY_LEVEL_COMMENT, '#'),nvl(T1.QUALITY_LEVEL_COMMENT, '#'),0,1) QUALITY_LEVEL_COMMENT_FLAG, \n");

				sqlStmt.append("decode(nvl(T2.COMPONENT_NO, '-1'),nvl(T1.COMPONENT_NO, '-1'),0,1) COMPONENT_NO_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.COM_PROD_BODY, '#'),nvl(T1.COM_PROD_BODY, '#'),0,1) COM_PROD_BODY_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.COM_MASK_OPTION, '#'),nvl(T1.COM_MASK_OPTION, '#'),0,1) COM_MASK_OPTION_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.COM_BACKEND_OPTION, '#'),nvl(T1.COM_BACKEND_OPTION, '#'),0,1) COM_BACKEND_OPTION_FLAG, \n");

				sqlStmt.append("decode(nvl(T2.ENDURANCE, '#'),nvl(T1.ENDURANCE, '#'),0,1) FT_SPECIAL_CONTROL_FLAG, \n");
				sqlStmt.append("decode(nvl(T2.WSSPECIALCONTROL, '#'),nvl(T1.WSSPECIALCONTROL, '#'),0,1) WS_SPECIAL_CONTROL_FLAG, \n");
				sqlStmt.append("decode(nvl(tf_check_step('AVI',T2.ws_route), '#'),nvl(tf_check_step('AVI',T1.ws_route), '#'),0,1) AVI_FLAG, \n");
				sqlStmt.append("decode(nvl(DECODE(tf_check_step('INK',T2.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T2.WS_ROUTE)), '#'),nvl(DECODE(tf_check_step('INK',T1.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T1.WS_ROUTE)), '#'),0,1) INK_FLAG \n");

				sqlStmt.append("FROM " + table1 + " T1, " + table2 + " T2 \n");
				sqlStmt.append("WHERE T1.SID = " + sid + " \n");
				sqlStmt.append("AND T1.TAG != 2 \n");
				sqlStmt.append("AND T2.PRODUCT_BODY = T1.PRODUCT_BODY AND T2.BRAND = T1.BRAND \n");
				sqlStmt.append("AND T2.TAG != 2 \n");
				sqlStmt.append("AND T2.SID = " + sid2 + " AND T2.BACKEND_OPTION = T1.BACKEND_OPTION \n");
				sqlStmt.append("AND T2.FG_WITH_CODE = T1.FG_WITH_CODE AND T2.PIN_COUNT = T1.PIN_COUNT \n");
				sqlStmt.append("AND T2.PACKAGE_TYPE = T1.PACKAGE_TYPE AND T2.FT_ROUTE = T1.FT_ROUTE \n");
				sqlStmt.append("AND NVL(T2.SORT_ROUTE_CODE, ' ') = NVL(T1.SORT_ROUTE_CODE, ' ') \n");
				sqlStmt.append("AND NVL(T2.DB_WITH_CODE, ' ')= NVL(T1.DB_WITH_CODE, ' ') AND NVL(T2.WS_ROUTE, ' ') = NVL(T1.WS_ROUTE, ' ') \n");
				sqlStmt.append("AND NVL(T2.WS_ROUTE_ADD, ' ') = NVL(T1.WS_ROUTE_ADD, ' ')  \n");
				sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD, ' ') = NVL(T1.FT_ROUTE_ADD, ' ') \n");
				sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD2, ' ') = NVL(T1.FT_ROUTE_ADD2, ' ') \n");
				sqlStmt.append("AND NVL(T2.FT_ROUTE_ADD3, ' ') = NVL(T1.FT_ROUTE_ADD3, ' ') \n");
				sqlStmt.append("AND NVL(T2.FT_ROUTE_CODE, ' ') = NVL(T1.FT_ROUTE_CODE, ' ') \n");
				sqlStmt.append("AND NVL(T2.COMPONENT_NO, -1) = NVL(T1.COMPONENT_NO, -1) \n");
				sqlStmt.append("AND NVL(T2.COM_PROD_BODY, ' ') = NVL(T1.COM_PROD_BODY, ' ') \n");
				sqlStmt.append("AND NVL(T2.COM_MASK_OPTION, ' ') = NVL(T1.COM_MASK_OPTION, ' ') \n");
				sqlStmt.append("AND ( \n");
				sqlStmt.append(" NVL(T2.TF_COMMENT, ' ') != NVL(T1.TF_COMMENT, ' ') \n");
				sqlStmt.append(" OR NVL(T2.TF_WS_COMMENT, ' ') != NVL(T1.TF_WS_COMMENT, ' ') \n");
				//sqlStmt.append(" OR NVL(T2.QUALITY_LEVEL_COMMENT, ' ') != NVL(T1.QUALITY_LEVEL_COMMENT, ' ') \n");
				sqlStmt.append("OR (T1.BRAND='MX' AND NVL(T2.QUALITY_LEVEL_COMMENT, ' ') != NVL(T1.QUALITY_LEVEL_COMMENT, ' '))  \n");

				//sqlStmt.append(" OR NVL(T2.COMPONENT_NO, ' ') != NVL(T1.COMPONENT_NO, ' ') \n");
				//sqlStmt.append(" OR NVL(T2.COM_PROD_BODY, ' ') != NVL(T1.COM_PROD_BODY, ' ') \n");
				//sqlStmt.append(" OR NVL(T2.COM_MASK_OPTION, ' ') != NVL(T1.COM_MASK_OPTION, ' ') \n");
				sqlStmt.append(" OR NVL(T2.COM_BACKEND_OPTION, ' ') != NVL(T1.COM_BACKEND_OPTION, ' ') \n");				
				
				sqlStmt.append(" OR NVL(T2.ENDURANCE, ' ') != NVL(T1.ENDURANCE, ' ')  \n");
				sqlStmt.append(" OR NVL(T2.WSSPECIALCONTROL, ' ') != NVL(T1.WSSPECIALCONTROL, ' ')  \n");
				sqlStmt.append(" OR NVL(tf_check_step('AVI',T2.ws_route), ' ') != NVL(tf_check_step('AVI',T1.ws_route), ' ')  \n");
				sqlStmt.append(" OR NVL(DECODE(tf_check_step('INK',T2.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T2.WS_ROUTE)), ' ') != NVL(DECODE(tf_check_step('INK',T1.WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',T1.WS_ROUTE)), ' ')  \n");
				sqlStmt.append(") \n");

				//sqlStmt.append("AND NVL(T2.QUALITY_LEVEL, ' ') = NVL(T1.QUALITY_LEVEL, ' ')  \n");
				sqlStmt.append("AND ((T1.BRAND='MX' AND NVL(T2.QUALITY_LEVEL, ' ') = NVL(T1.QUALITY_LEVEL, ' ')) OR T1.BRAND='KH') \n");

			}
			sqlStmt.append("ORDER BY FT_ROUTE_CODE, COMPONENT_NO");
			TDSLogger.println(sqlStmt.toString());
			PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				EditiionCompareActionForm bom = new EditiionCompareActionForm();
				bom.setSid(rs.getString("SID"));
				bom.setProductbody(rs.getString("PRODUCT_BODY"));
				bom.setBrand(rs.getString("BRAND"));
				bom.setVersion(rs.getString("VERSION"));
				bom.setBeoption(rs.getString("BACKEND_OPTION"));
				bom.setFgwithcode(rs.getString("FG_WITH_CODE"));
				bom.setPincount(rs.getString("PIN_COUNT"));
				bom.setPkgtype(rs.getString("PACKAGE_TYPE"));
				bom.setFtroute(rs.getString("FT_ROUTE"));
				//bom.setMaskopt(rs.getString("MASK_OPTION"));
				bom.setSortroutecode(rs.getString("SORT_ROUTE_CODE"));
				bom.setDbwithcode(rs.getString("DB_WITH_CODE"));
				bom.setWsroute(rs.getString("WS_ROUTE"));
				bom.setWsaddroute(rs.getString("WS_ROUTE_ADD"));
				bom.setComment(rs.getString("TF_COMMENT"));
				bom.setFt_route_add(rs.getString("FT_ROUTE_ADD"));
				bom.setFt_route_add2(rs.getString("FT_ROUTE_ADD2"));
				bom.setFt_route_add3(rs.getString("FT_ROUTE_ADD3"));
				bom.setFt_route_code(rs.getString("FT_ROUTE_CODE"));
				bom.setWscomment(rs.getString("TF_WS_COMMENT"));
				bom.setQuality_level(rs.getString("QUALITY_LEVEL"));
				bom.setQuality_level_comment(rs.getString("QUALITY_LEVEL_COMMENT"));
				bom.setComponent_no(rs.getString("COMPONENT_NO"));
				bom.setCom_prod_body(rs.getString("COM_PROD_BODY"));
				bom.setCom_mask_option(rs.getString("COM_MASK_OPTION"));
				bom.setCom_backend_option(rs.getString("COM_BACKEND_OPTION"));

				bom.setFt_special_control(rs.getString("ENDURANCE"));
				bom.setWs_special_control(rs.getString("WSSPECIALCONTROL"));
				bom.setAvi(rs.getString("avi"));
				bom.setInk(rs.getString("ink"));

				bom.setType_flag(rs.getString("TYPE_FLAG"));
				bom.setBackend_option_flag(rs.getInt("BACKEND_OPTION_FLAG"));
				bom.setFg_with_code_flag(rs.getInt("FG_WITH_CODE_FLAG"));
				bom.setPin_count_flag(rs.getInt("PIN_COUNT_FLAG"));
				bom.setPackage_type_flag(rs.getInt("PACKAGE_TYPE_FLAG"));
				bom.setFt_route_code_flag(rs.getInt("FT_ROUTE_CODE_FLAG"));
				bom.setFt_route_flag(rs.getInt("FT_ROUTE_FLAG"));
				bom.setFt_route_add_flag(rs.getInt("FT_ROUTE_ADD_FLAG"));
				bom.setFt_route_add_flag2(rs.getInt("FT_ROUTE_ADD_FLAG2"));
				bom.setFt_route_add_flag3(rs.getInt("FT_ROUTE_ADD_FLAG3"));
				bom.setTf_comment_flag(rs.getInt("TF_COMMENT_FLAG"));
				//bom.setMask_option_flag(rs.getInt("MASK_OPTION_FLAG"));
				bom.setDb_with_code_flag(rs.getInt("DB_WITH_CODE_FLAG"));
				bom.setSort_route_code_flag(rs.getInt("SORT_ROUTE_CODE_FLAG"));
				bom.setWs_route_flag(rs.getInt("WS_ROUTE_FLAG"));
				bom.setWs_route_add_flag(rs.getInt("WS_ROUTE_ADD_FLAG"));
				bom.setTf_ws_comment_flag(rs.getInt("TF_WS_COMMENT_FLAG"));
				bom.setQuality_level_flag(rs.getInt("QUALITY_LEVEL_FLAG"));
				bom.setQuality_level_comment_flag(rs.getInt("QUALITY_LEVEL_COMMENT_FLAG"));
				bom.setComponent_no_flag(rs.getInt("COMPONENT_NO_FLAG"));
				bom.setCom_prod_body_flag(rs.getInt("COM_PROD_BODY_FLAG"));
				bom.setCom_mask_option_flag(rs.getInt("COM_MASK_OPTION_FLAG"));
				bom.setCom_backend_option_flag(rs.getInt("COM_BACKEND_OPTION_FLAG"));
				
				bom.setFt_special_control_flag(rs.getInt("FT_SPECIAL_CONTROL_FLAG"));
				bom.setWs_special_control_flag(rs.getInt("WS_SPECIAL_CONTROL_FLAG"));
				bom.setAvi_flag(rs.getInt("AVI_FLAG"));
				bom.setInk_flag(rs.getInt("INK_FLAG"));

				tmp2.add(bom);
				if (compareType == 0)
					break;
			}
			return (EditiionCompareActionForm[]) tmp2
					.toArray(new EditiionCompareActionForm[0]);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// DBConnection.close(conn);
			// conn = null;
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