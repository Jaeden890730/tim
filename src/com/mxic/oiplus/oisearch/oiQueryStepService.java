package com.mxic.oiplus.oisearch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mxic.oiplus.oimaintain.PBCTestParameterBean;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.SQLStem;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.oiplus.oimaintain.OiMaintainService;
import com.mxic.oiplus.oimaintain.WIPActionForm;
import com.mxic.oiplus.oimaintain.WIPCtrlBean;
import com.mxic.oiplus.oimaintain.YieldDefinitionActionForm;
import com.mxic.oiplus.oimaintain.ProTestRouteBean;
import com.mxic.oiplus.oimaintain.YieldDefBean;
import com.mxic.oiplus.oimaintain.ProWaferlevelBean;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class oiQueryStepService {
  public oiQueryStepService() {
  }

  
  public static ProWaferlevelBean[] OIQueryStep0(String productname,
          String brand,
          String version) {

	  Connection conn = null;
	try {
		//9i用法
		/*String sql = "select * from tf_product_route a left join " +
		"(SELECT route_name," +
		"count( * ) i FROM tf_product_route where " +
		"product_body = '" + productname +
		"' group by route_name) b " +
		"on a.route_name = b.route_name " +
		"where product_body = '" + productname +
		"' and brand = '" + brand + "' and version = '" +
		version + "' order by a.route_name, a.step_seq";
		*/
		String sql = "select a.* from tf_prod_waferlevel a " +
		"where a.product_body='" +
		productname + "' and a.brand = '" + brand + "' and a.version = '" +
		version + "' order by a.revise_priority";
		
		conn = DBConnection.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		ArrayList tmp = new ArrayList();
		String j = "";
		while (rs.next()) {
			ProWaferlevelBean bean = new ProWaferlevelBean();
			bean.setSid(rs.getString("SID"));
			bean.setProductbody(rs.getString("PRODUCT_BODY"));
			bean.setBrand(rs.getString("BRAND"));
			bean.setVersion(rs.getString("VERSION"));
			bean.setWafer_level(rs.getString("wafer_level"));
			bean.setWafer_brand(rs.getString("wafer_brand"));
			bean.setBiztype(rs.getString("biztype"));
			bean.setWafer_grade(rs.getString("wafer_grade"));
			bean.setApply_type(rs.getString("apply_type"));
			bean.setOri_priority(rs.getString("ori_priority"));
			bean.setRevise_priority(rs.getString("revise_priority"));
			bean.setChecked_flag(rs.getString("checked_flag"));
			tmp.add(bean);
		}
		ps.clearParameters();
		ps.close();
		rs.close();
		if (tmp.isEmpty()) {
			return (ProWaferlevelBean[])null;
		}
		else {
			return (ProWaferlevelBean[]) tmp.toArray(new ProWaferlevelBean[0]);
		}
	}
	catch (Exception ex) {
		ex.fillInStackTrace();
		TDSLogger.println(ex.getMessage());
		return null;
	}
	finally {
		DBConnection.close(conn);
		conn = null;
	}
}  
  public static ProTestRouteBean[] OIQueryStep1(String productname,
                                                  String brand,
                                                  String version) {

    Connection conn = null;
    try {
      //9i用法
      /*String sql = "select * from tf_product_route a left join " +
                   "(SELECT route_name," +
                   "count( * ) i FROM tf_product_route where " +
                   "product_body = '" + productname +
                   "' group by route_name) b " +
                   "on a.route_name = b.route_name " +
                   "where product_body = '" + productname +
                   "' and brand = '" + brand + "' and version = '" +
                   version + "' order by a.route_name, a.step_seq";
       */
      String sql = "select a.*,b.* from tf_product_route a ,(SELECT route_name,count( * ) i " +
          "FROM tf_product_route where product_body='" +
          productname + "' and brand = '" + brand + "' and version = '" +
          version + "' group by route_name) b "
          + "where a.route_name=b.route_name(+) and a.product_body='" +
          productname + "' and a.brand = '" + brand + "' and a.version = '" +
          version + "' order by a.route_name, a.step_seq";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      String j = "";
      while (rs.next()) {
    	  ProTestRouteBean bean = new ProTestRouteBean();
        if (j.equals(rs.getString("route_name"))) {
          bean.setCount("0");
        } else {
          bean.setCount(rs.getString("i"));
          j = rs.getString("route_name");
        }

        bean.setSid(rs.getString("SID"));
        bean.setProductbody(rs.getString("PRODUCT_BODY"));
        bean.setBrand(rs.getString("BRAND"));
        bean.setVersion(rs.getString("VERSION"));
        bean.setRoutename(rs.getString("route_name"));
        bean.setStepseq(rs.getString("step_seq"));
        bean.setStepname(rs.getString("step_name"));
        bean.setStep_def(rs.getString("step_def"));
        bean.setQcactualmode(rs.getString("qc_actual_mode"));
        bean.setTesttime(rs.getString("test_time"));
        bean.setTimeunit(rs.getString("time_unit"));
        bean.setTemperature(rs.getString("temperature"));
        bean.setSamplingtest(rs.getString("sampling_test"));
        bean.setSamplingcond(rs.getString("sampling_cond"));
        bean.setReworkstep(rs.getString("rework_step"));
        bean.setTesttime2(rs.getString("test_time2"));
        bean.setTimeunit2(rs.getString("time_unit2"));
        bean.setRemark(rs.getString("remark"));
        tmp.add(bean);
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if (tmp.isEmpty()) {
        return (ProTestRouteBean[])null;
      }
      else {
        return (ProTestRouteBean[]) tmp.toArray(new ProTestRouteBean[0]);
      }
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
  }
  

  public static TFBomRouteBean[] OIQueryStep2(String productname,
                                              String brand,
                                              String version) {

    Connection conn = null;
    try {
      String sql = "select a.*,tf_check_step('AVI',ws_route) avi,DECODE(tf_check_step('INK',WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',WS_ROUTE)) ink \n" +//tf_check_step('INK',ws_route)
    	  "from tf_bom_route a \n" +
    	  "where product_body = '" + productname +
          "' and brand = '" + brand + "' and version = '" +  version +
          "' and tag != 2 order by ft_route_code, sort_route_code, ws_route, ws_route_add";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      while (rs.next()) {
        TFBomRouteBean bean = new TFBomRouteBean();
        bean.setSid(rs.getString("sid"));
        bean.setProduct_body(rs.getString("Product_body"));
        bean.setBrand(rs.getString("brand"));
        bean.setVersion(rs.getString("version"));
        bean.setBackend_option(rs.getString("backend_option"));
        bean.setFg_with_code(rs.getString("fg_with_code"));
        bean.setPin_count(rs.getString("pin_count"));
        bean.setPackage_type(rs.getString("package_type"));
        bean.setFt_route_code(rs.getString("ft_route_code"));
        bean.setFt_route(rs.getString("ft_route"));
        bean.setMask_option(rs.getString("mask_option"));
        bean.setSort_route_code(rs.getString("sort_route_code"));
        bean.setDb_with_code(rs.getString("db_with_code"));
        bean.setWs_route(rs.getString("ws_route"));
        bean.setWs_route_add(rs.getString("ws_route_add"));
        bean.setTf_comment(rs.getString("tf_comment"));
        bean.setTf_ws_comment(rs.getString("tf_ws_comment"));
        bean.setFtAddroute(rs.getString("ft_route_add"));
        bean.setFtAddroute2(rs.getString("ft_route_add2"));
        bean.setFtAddroute3(rs.getString("ft_route_add3"));
        bean.setSales_form(rs.getString("sales_form"));
        bean.setEndurance(rs.getString("endurance"));
        bean.setAvi(rs.getString("avi"));
        bean.setInk(rs.getString("ink"));
        bean.setWsspecialcontrol(rs.getString("wsspecialcontrol"));
        bean.setQuality_level(rs.getString("quality_level"));
        bean.setQuality_level_comment(rs.getString("quality_level_comment"));
        bean.setMcp_flag(rs.getString("mcp_flag"));
        tmp.add(bean);
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if (tmp.isEmpty()) {
        return (TFBomRouteBean[])null;
      } else {
        return (TFBomRouteBean[]) tmp.toArray(new TFBomRouteBean[0]);
      }
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
  }
  
  public static TFBomRouteBean[] OIQueryStep2_1(String productname,
          String brand,
          String version) {

		Connection conn = null;
		try {
		String sql = "select a.*,tf_check_step('AVI',ws_route) avi,DECODE(tf_check_step('INK',WS_ROUTE),'Y','Y',tf_check_step('INK_MAP',WS_ROUTE)) ink \n" +//tf_check_step('INK',ws_route)
		"from tf_bom_route_mcp a \n" +
		"where product_body = '" + productname +
		"' and brand = '" + brand + "' and version = '" +  version +
		"' and tag != 2 order by ft_route_code, sort_route_code, ws_route, ws_route_add";
		
		conn = DBConnection.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		ArrayList tmp = new ArrayList();
		
		while (rs.next()) {
		TFBomRouteBean bean = new TFBomRouteBean();
		bean.setSid(rs.getString("sid"));
		bean.setProduct_body(rs.getString("Product_body"));
		bean.setBrand(rs.getString("brand"));
		bean.setVersion(rs.getString("version"));
		bean.setBackend_option(rs.getString("backend_option"));
		bean.setFg_with_code(rs.getString("fg_with_code"));
		bean.setPin_count(rs.getString("pin_count"));
		bean.setPackage_type(rs.getString("package_type"));
		bean.setFt_route_code(rs.getString("ft_route_code"));
		bean.setFt_route(rs.getString("ft_route"));
		//bean.setMask_option(rs.getString("mask_option"));
		bean.setSort_route_code(rs.getString("sort_route_code"));
		bean.setDb_with_code(rs.getString("db_with_code"));
		bean.setWs_route(rs.getString("ws_route"));
		bean.setWs_route_add(rs.getString("ws_route_add"));
		bean.setTf_comment(rs.getString("tf_comment"));
		bean.setTf_ws_comment(rs.getString("tf_ws_comment"));
		bean.setFtAddroute(rs.getString("ft_route_add"));
		bean.setFtAddroute2(rs.getString("ft_route_add2"));
		bean.setFtAddroute3(rs.getString("ft_route_add3"));
		bean.setSales_form(rs.getString("sales_form"));
		bean.setEndurance(rs.getString("endurance"));
		bean.setAvi(rs.getString("avi"));
		bean.setInk(rs.getString("ink"));
		bean.setWsspecialcontrol(rs.getString("wsspecialcontrol"));
		bean.setQuality_level(rs.getString("quality_level"));
		bean.setQuality_level_comment(rs.getString("quality_level_comment"));
		
		bean.setComponent_no(rs.getString("component_no"));
		bean.setCom_prod_body(rs.getString("com_prod_body"));
		bean.setCom_mask_option(rs.getString("com_mask_option"));
		bean.setCom_backend_option(rs.getString("com_backend_option"));
		
		tmp.add(bean);
		}
		ps.clearParameters();
		ps.close();
		rs.close();
		if (tmp.isEmpty()) {
		return (TFBomRouteBean[])null;
		} else {
		return (TFBomRouteBean[]) tmp.toArray(new TFBomRouteBean[0]);
		}
	}
	catch (Exception ex) {
		ex.fillInStackTrace();
		TDSLogger.println(ex.getMessage());
		return null;
	}
	finally {
		DBConnection.close(conn);
		conn = null;
	}
  }  

  public static TFTestParameterWSBean[] OIQueryStep3(String productname,
                                                     String brand,
                                                     String version) {

    Connection conn = null;
    try {
      String sql =
          "select * from tf_test_parameter_ws where product_body = '" +
          productname + "' and brand = '" + brand +
          "' and version = '" +
          version + "' order by pgm_id";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      while (rs.next()) {
        TFTestParameterWSBean bean = new TFTestParameterWSBean();
        bean.setSid(rs.getString("sid"));
        bean.setPgm_id(rs.getString("pgm_id"));
        bean.setProduct_body(rs.getString("Product_body"));
        bean.setBrand(rs.getString("brand"));
        bean.setVersion(rs.getString("version"));
        bean.setMask_option(rs.getString("mask_option"));
        bean.setTest_type(rs.getString("test_type"));
        bean.setTester(rs.getString("tester"));
        bean.setSite(rs.getString("site"));
        bean.setProgram_name(rs.getString("program_name"));
        bean.setPgm_special_control(rs.getString("pgm_special_control"));
        bean.setOne_main_pgm_group_version(rs.getString("one_main_pgm_group_version"));
        bean.setTemp(rs.getString("temperature"));
        bean.setTf_comment(rs.getString("tf_comment"));
        bean.setHw_configure(rs.getString("hw_configure"));
        tmp.add(bean);
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if (tmp.isEmpty()) {
        return (TFTestParameterWSBean[])null;
      } else {
        return (TFTestParameterWSBean[]) tmp.toArray(new
            TFTestParameterWSBean[0]);
      }
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  public static TFTestParameterFTBean[] OIQueryStep4(String productname,
                                                     String brand,
                                                     String version) {

    Connection conn = null;
    try {
      String sql =
          "select * from tf_test_parameter_ft where product_body = '" +
          productname + "' and brand = '" + brand +
          "' and version = '" +
          version + "' order by pgm_id";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      while (rs.next()) {
        TFTestParameterFTBean bean = new TFTestParameterFTBean();
        bean.setSid(rs.getString("sid"));
        bean.setPgm_id(rs.getString("pgm_id"));
        bean.setProduct_body(rs.getString("Product_body"));
        bean.setBrand(rs.getString("brand"));
        bean.setVersion(rs.getString("version"));
        bean.setTest_type(rs.getString("test_type"));
        bean.setBackend_option(rs.getString("backend_option"));
        bean.setPin_count(rs.getString("pin_count"));
        bean.setPackage_type(rs.getString("package_type"));
        bean.setBody_size(rs.getString("body_size"));
        bean.setTester(rs.getString("tester"));
        bean.setSite(rs.getString("site"));
        bean.setProgram_name(rs.getString("program_name"));
        bean.setActual_file(rs.getString("actual_file"));
        bean.setPgm_special_control(rs.getString("pgm_special_control"));
        bean.setOne_main_pgm_group_version(rs.getString("one_main_pgm_group_version"));
        bean.setI_grade(rs.getString("i_grade"));
        bean.setC_grade(rs.getString("c_grade"));
        bean.setW_grade(rs.getString("w_grade"));
        bean.setY_grade(rs.getString("y_grade"));
        bean.setJ_grade(rs.getString("j_grade"));
        bean.setK_grade(rs.getString("k_grade"));
        bean.setL_grade(rs.getString("l_grade"));
        bean.setN_grade(rs.getString("n_grade"));
        bean.setB_grade(rs.getString("b_grade"));
        bean.setE_grade(rs.getString("e_grade"));
        bean.setS_grade(rs.getString("s_grade"));
        bean.setTf_comment(rs.getString("tf_comment"));
        bean.setHw_configure(rs.getString("hw_configure"));
        tmp.add(bean);
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if (tmp.isEmpty()) {
        return (TFTestParameterFTBean[])null;
      } else {
        return (TFTestParameterFTBean[]) tmp.toArray(new
            TFTestParameterFTBean[0]);
      }
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
  }
  public static PBCTestParameterBean[] OIQueryStep5(String productname,
		  String brand,
		  String version) {

	  Connection conn = null;
	  try {
		  String sql =
			  "select * from tf_test_parameter_pbc where product_body = '" +
			  productname + "' and brand = '" + brand +
			  "' and version = '" +
			  version + "' order by pgm_id";

		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sql);
		  ResultSet rs = ps.executeQuery();
		  ArrayList tmp = new ArrayList();

		  while (rs.next()) {
			  PBCTestParameterBean bean = new PBCTestParameterBean();
			  bean.setSid(Integer.parseInt(rs.getString("sid")));
			  bean.setPgm_id(Integer.parseInt(rs.getString("pgm_id")));
			  bean.setProduct_body(rs.getString("Product_body"));
			  bean.setBrand(rs.getString("brand"));
			  bean.setVersion(rs.getString("version"));
			  bean.setTest_type(rs.getString("test_type"));
			  bean.setBackend_option(rs.getString("backend_option"));
			  bean.setPin_count(Integer.parseInt(rs.getString("pin_count")));
			  bean.setPackage_type(rs.getString("package_type"));
			  bean.setBody_size(rs.getString("body_size"));
			  bean.setTester(rs.getString("tester"));
			  bean.setSite(rs.getString("site"));
			  bean.setProgram_name(rs.getString("program_name"));
			  bean.setActual_file(rs.getString("actual_file"));
			  bean.setI_grade(rs.getString("i_grade"));
			  bean.setC_grade(rs.getString("c_grade"));
	          bean.setW_grade(rs.getString("w_grade"));
	          bean.setY_grade(rs.getString("y_grade"));
	          bean.setW_grade(rs.getString("j_grade"));
	          bean.setY_grade(rs.getString("k_grade"));
	          bean.setL_grade(rs.getString("l_grade"));
	          bean.setN_grade(rs.getString("n_grade"));
	          bean.setB_grade(rs.getString("b_grade"));
	          bean.setE_grade(rs.getString("e_grade"));
			  bean.setS_grade(rs.getString("s_grade"));
			  bean.setTf_comment(rs.getString("tf_comment"));
			  bean.setHw_configure(rs.getString("hw_configure"));
			  tmp.add(bean);
		  }
		  ps.clearParameters();
		  ps.close();
		  rs.close();
		  if (tmp.isEmpty()) {
			  return null;
		  } else {
			  return (PBCTestParameterBean[]) tmp.toArray(new PBCTestParameterBean[0]);
		  }
	  }
	  catch (Exception ex) {
		  ex.fillInStackTrace();
		  TDSLogger.println(ex.getMessage());
		  return null;
	  }
	  finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
  }

  public static TFBasicInformationBean[] OIQueryStep6(String productname,
                                                             String brand,
                                                             String version) {
   Connection conn = null;
    try {
      String sql =
          "select * from TF_BASIC_INFO_vw where product_body = '" +
          productname + "' and brand = '" + brand +
          "' and version = '" +
          version + "'";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      while (rs.next()) {
        TFBasicInformationBean bean = new TFBasicInformationBean();
        bean.setSid(rs.getString("sid"));
        bean.setProduct_body(rs.getString("Product_body"));
        bean.setBrand(rs.getString("brand"));
        bean.setVersion(rs.getString("version"));
        bean.setTester(rs.getString("tester"));
        bean.setOptions(rs.getString("options"));
        bean.setGrade(rs.getString("grade"));
        bean.setGood_bin(rs.getString("good_bin"));
        bean.setIb_bin(rs.getString("ib_bin"));
        bean.setIpn_action_str(rs.getString("ipn_action_str"));
        bean.setIpn_action(rs.getString("ipn_action"));
        bean.setBin_type(rs.getString("bin_type"));
        bean.setBin_type_str(rs.getString("bin_type_str"));
        bean.setInkless_grade(rs.getString("inkless_grade"));
        bean.setKtd_bin_flag(rs.getString("ktd_bin_flag"));
        bean.setEpn_speed(rs.getString("epn_speed"));
        bean.setTest_speed(rs.getString("test_speed"));
        bean.setDown_grade(rs.getString("down_grade"));
        bean.setFail_bin(rs.getString("fail_bin"));
        bean.setRemark(rs.getString("remark"));
        tmp.add(bean);
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if (tmp.isEmpty()) {
        return (TFBasicInformationBean[])null;
      } else {
        return (TFBasicInformationBean[]) tmp.toArray(new
            TFBasicInformationBean[0]);
      }
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
  }
  public static String OIQueryStep61(String productname,
                                     String brand,
                                     String version) {
   Connection conn = null;
   String Comments = "";
    try {
      String sql =
          "select a.* from TF_BASIC_INFO_COMMENT a, tf_information b \n" +
          "where product_body = '" + productname + "' and brand = '" + brand +
          "' and version = '" +  version + "'" +
          "and a.sid = b.sid";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        Comments = rs.getString("COMMENTS");
        if (Comments == null) Comments = "";
      }
      ps.clearParameters();
      ps.close();
      rs.close();
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return "";
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
    return Comments;
  }

  public static YieldDefinitionBean[] OIQueryStep7(String productname,
                                                   String brand,
                                                   String version) {

    Connection conn = null;
    try {
      String sql =
          "select * from tf_document_linkage where product_body = '" +
          productname + "' and brand = '" + brand +
          "' and version = '" +
          version + "' and doc_type='Y' order by doc_name";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      while (rs.next()) {
        YieldDefinitionBean bean = new YieldDefinitionBean();
        bean.setSid(rs.getString("sid"));
        bean.setProduct_body(rs.getString("Product_body"));
        bean.setBrand(rs.getString("brand"));
        bean.setVersion(rs.getString("version"));
        bean.setFile_name(rs.getString("file_name"));
        bean.setDoc_name(rs.getString("doc_name"));
        bean.setComment(rs.getString("tf_comment"));
        tmp.add(bean);
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if (tmp.isEmpty()) {
        return (YieldDefinitionBean[])null;
      } else {
        return (YieldDefinitionBean[]) tmp.toArray(new
            YieldDefinitionBean[0]);
      }
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  public static YieldDefinitionActionForm[] OIQueryStep71(String productname,
          												  String brand,
          												  String version) {

	  Connection conn = null;
	  try {
		  String sql =
			  "select * from tf_yield where product_body = '" +
			  productname + "' and brand = '" + brand +
			  "' and version = "+version+" order by seq";

		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sql);
		  ResultSet rs = ps.executeQuery();
		  ArrayList tmp = new ArrayList();
		  
		  while (rs.next()) {
			  YieldDefinitionActionForm bean = new YieldDefinitionActionForm();
			  bean.setPd_body(productname);
			  bean.setBrand(brand);
			  bean.setVersion(""+version);
			  bean.setProduct_code(rs.getString("product_code"));
			  bean.setTest_mode(rs.getString("test_mode"));
			  bean.setAuto_ship(rs.getString("auto_ship"));
			  bean.setHold_pe(rs.getString("hold_pe"));
			  bean.setHold_bin(rs.getString("hold_bin"));
			  bean.setHold_bin_cri(rs.getString("hold_bin_cri"));
			  bean.setAuto_scrap(rs.getString("auto_scrap"));
			  bean.setStop(rs.getString("stop"));
			  bean.setMrb(rs.getString("mrb"));
			  bean.setSampling_yield(rs.getString("sampling_yield"));
			  bean.setNotes(rs.getString("notes"));			  
			  tmp.add(bean);
		  }
		  ps.clearParameters();
		  ps.close();
		  rs.close();
		  if (tmp.isEmpty()) {
			  return (YieldDefinitionActionForm[])null;
		  } else {
			  return (YieldDefinitionActionForm[]) tmp.toArray(new
					  YieldDefinitionActionForm[0]);
		  }
	  }
	  catch (Exception ex) {
		  ex.fillInStackTrace();
		  TDSLogger.println(ex.getMessage());
		  return null;
	  }
	  finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
  }

  public static TestFlowBean[] OIQueryStep8(String productname,
                                            String brand,
                                            String version) {

    Connection conn = null;

    try {
      String sql = "select * from tf_document_linkage where product_body = '" +
          productname + "' and brand = '" + brand +
          "' and version = '" +
          version + "' and doc_type='T' order by doc_name";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      ArrayList tmp = new ArrayList();
      while (rs.next()) {
        TestFlowBean bean = new TestFlowBean();
        bean.setSid(rs.getString("sid"));
        bean.setProduct_body(rs.getString("Product_body"));
        bean.setBrand(rs.getString("brand"));
        bean.setVersion(rs.getString("version"));
        bean.setFile_name(rs.getString("file_name"));
        bean.setDoc_name(rs.getString("doc_name"));
        bean.setComment(rs.getString("tf_comment"));
        tmp.add(bean);
      }  ps.clearParameters();
      ps.close();
      rs.close();
      if (tmp.isEmpty()) {
        return (TestFlowBean[])null;
      } else {
        return (TestFlowBean[]) tmp.toArray(new
            TestFlowBean[0]);
      }
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
  }
  public static WIPActionForm[] OIQueryStep91(String product_body,
                                            String brand,
                                            String version) {
  Connection conn = null;

  try {
    String sql =
        "select distinct mask_option, decode(instr(b.optionlist,a.mask_option),0,'',null,'','checked') tag \n" +
        "from (select distinct sid,mask_option \n" +
        "from tf_test_parameter_ws \n" +
        "where product_body = '"+product_body+
        "' and brand = '"+brand+"' and version = "+version+") a, \n" +
        "(select distinct a.sid,optionlist \n" +
        "from tf_wip a, tf_information b \n" +
        "where a.sid = b.sid and b.product_body = '"+product_body+
        "' and brand='"+brand+"' and version="+version+") b\n" +
        "where a.sid = b.sid (+)";

    conn = null;
    conn = DBConnection.getConnection();
    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();
    ArrayList tmp = new ArrayList();

    while (rs.next()) {
      WIPActionForm bean = new WIPActionForm();
      bean.setOption_list(rs.getString("MASK_OPTION"));
      bean.setTag(rs.getString("TAG"));
      tmp.add(bean);
    }
    return (WIPActionForm[]) tmp.toArray(new WIPActionForm[0]);
  } catch (Exception ex) {
    ex.printStackTrace();
    DBConnection.rollback(conn);
    ex.fillInStackTrace();
    TDSLogger.println(ex.getMessage());
    return null;
  } finally {
    DBConnection.close(conn);
    conn = null;
  }

  }
  public static WIPCtrlBean[] OIQueryStep92(String product_body,
                                            String brand,
                                            String version) {
    Connection conn = null;

    try {
      String sql =
          "select distinct \n" +
          "decode(ctrl_type,'0','checked',null,'checked','') M0, \n" +
          "decode(ctrl_type,'1','checked','') M1, \n" +
          "decode(ctrl_type,'2-1','checked','') M21, \n" +
          "decode(ctrl_type,'2-2','checked','') M22, \n" +
          "decode(ctrl_type,'2-3','checked','') M23, \n" +
          "decode(ctrl_type,'2-4-1','checked','') M241, \n" +
          "decode(ctrl_type,'3-1','checked','') M31, \n" +
          "decode(ctrl_type,'2-4-1',pgname1,'') PGNAME1 \n" +
          "from tf_wip a, tf_information b\n" +
          " where b.sid = a.sid (+) \n" +
          " and b.product_body = '" + product_body +
          "' \n and b.brand = '" + brand +
          "' \n and b.version = " + version;

      conn = null;
      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      while (rs.next()) {
        WIPCtrlBean bean = new WIPCtrlBean();
        bean.setM0(rs.getString("M0"));
        bean.setM1(rs.getString("M1"));
        bean.setM21(rs.getString("M21"));
        bean.setM22(rs.getString("M22"));
        bean.setM23(rs.getString("M23"));
        bean.setM241(rs.getString("M241"));
        bean.setM31(rs.getString("M31"));
        bean.setSteps(rs.getString("PGNAME1"));
        tmp.add(bean);
      }
      return (WIPCtrlBean[]) tmp.toArray(new WIPCtrlBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  public static WIPActionForm[] OIQueryStep93(String product_body,
                                            String brand,
                                            String version) {
  Connection conn = null;
  boolean flag = true;

  try {
    String sql = "SELECT optionlist,ctrl_type,pgname1,pgname2,pgname3,pgname4,pgname5, \n" +
        " decode(p1,1,'readonly','') p1, \n" +
        " decode(p2,1,'readonly','') p2, \n" +
        " decode(p3,1,'readonly','') p3, \n" +
        " decode(p4,1,'readonly','') p4, \n" +
        " decode(p5,1,'readonly','') p5 \n" +
        " FROM tf_wip a, tf_information b \n" +
        " where a.sid=b.sid \n" +
        " and b.product_body = '" + product_body +
        "' \n and b.brand = '" + brand +
        "' \n and b.version = " + version +
        " \n and ctrl_type = '3-1' order by pgname1 ";
    conn = DBConnection.getConnection();
    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();
    ArrayList tmp = new ArrayList();

    while (rs.next()) {
      WIPActionForm bean = new WIPActionForm();
      bean.setOption_list(rs.getString("OPTIONLIST"));
      bean.setCtrl_type(rs.getString("CTRL_TYPE"));
      bean.setPgname1(rs.getString("PGNAME1"));
      bean.setPgname2(rs.getString("PGNAME2"));
      bean.setPgname3(rs.getString("PGNAME3"));
      bean.setPgname4(rs.getString("PGNAME4"));
      bean.setPgname5(rs.getString("PGNAME5"));
      bean.setP1(rs.getString("P1"));
      bean.setP2(rs.getString("P2"));
      bean.setP3(rs.getString("P3"));
      bean.setP4(rs.getString("P4"));
      bean.setP5(rs.getString("P5"));
      tmp.add(bean);
    }
    return (WIPActionForm[]) tmp.toArray(new WIPActionForm[0]);
  } catch (Exception ex) {
    ex.printStackTrace();
    DBConnection.rollback(conn);
    ex.fillInStackTrace();
    TDSLogger.println(ex.getMessage());
    return null;
  } finally {
    DBConnection.close(conn);
    conn = null;
  }
}
  
	public static YieldDefBean[] OIQueryStep12(String productname,
											   String brand,
								               String version,
								               String proc_type) {
		Connection conn = null;
		try {
			String producttype = OiMaintainService.getProductType(productname, brand, version);
			String sql = "";
			if(producttype.equals("NVM") && proc_type.equals("WS")){
				sql =

					"with t1 as (\n" +
					"select rownum as num,\n" + 
					"sid,yid,seq,facility,product_code,brand,test_mode,\n" + 
					"lower_limit,flag1,upper_limit,flag2,item,\n" + 
					"item_type,item_mode2,item_bins2,action,dg_action,change_ipn,\n" + 
					"route_name,start_step,remark,by_lot_dg,DGRADE_SPECIAL_IPN, actionseq\n" + 
					"from (\n" + 
					"SELECT a.sid,a.yid,a.seq,a.facility,a.product_code,a.brand,a.test_mode,\n" + 
					"lower_limit,flag1,upper_limit,flag2,a.item,a.snova_id, a.version,\n" + 
					"a.item_type,a.item_mode2,a.item_bins2,a.action,a.dg_action,a.change_ipn,\n" + 
					"a.route_name,a.start_step,a.remark,a.by_lot_dg, a.DGRADE_SPECIAL_IPN, \n" + 
					"b.id+10 actionseq FROM tf_yield_definition a, tf_description b, tf_information c\n" + 
					"where a.sid = c.sid" +
					"\nand c.product_body = ?" +
					"\nand c.brand = ?" +
					"\nand c.version = ?" +
					"\nand a.facility = ?" +
					"\nand a.action=b.description\n" + 
					"and b.tag=441\n" + 
					"and substr(a.action,1, 6) !='Dgrade'\n" + 
					"and a.action !='Follow Hold Criteria'\n" + 
					"order by a.product_code,a.brand,a.test_mode,a.item,cast(nvl(lower_limit,0) as int),cast(nvl(upper_limit,0) as int)\n" + 
					")\n" + 
					")\n" + 
					", t2 as (\n" + 
					"select rownum+10000 as num,\n" + 
					"sid,yid,seq,facility,product_code,brand,test_mode,\n" + 
					"lower_limit,flag1,upper_limit,flag2,item,\n" + 
					"item_type,item_mode2,item_bins2,action,dg_action,change_ipn,\n" + 
					"route_name,start_step,remark,by_lot_dg,DGRADE_SPECIAL_IPN,actionseq\n" + 
					"from(\n" + 
					"SELECT a.sid,a.yid,a.seq,a.facility,a.product_code,a.brand,a.test_mode,\n" + 
					"lower_limit,flag1,upper_limit,flag2,a.item,a.snova_id, a.version,\n" + 
					"a.item_type,a.item_mode2,a.item_bins2,a.action,a.dg_action,a.change_ipn,\n" + 
					"a.route_name,a.start_step,"
					//+ "a.remark,"
					+ "(a.remark||decode(nvl(a.dgradeprodcode,' '),' ','',';Dgrade different ProdCode:'||a.dgradeprodcode)) remark,"
					+ "by_lot_dg,DGRADE_SPECIAL_IPN,\n" + 
					"b.id+10 actionseq FROM tf_yield_definition a, tf_description b, tf_information c\n" + 
					"where a.sid = c.sid" +
					"\nand c.product_body = ?" +
					"\nand c.brand = ?" +
					"\nand c.version = ?" +
					"\nand a.facility = ?" +
					"\nand a.action=b.description\n" + 
					"and b.tag=441\n" + 
					"and ((substr(a.action,1, 6) ='Dgrade') or (a.action ='Follow Hold Criteria'))\n" + 
					"order by a.product_code,a.brand,a.test_mode,a.item,cast(nvl(lower_limit,0) as int),cast(nvl(upper_limit,0) as int)\n" + 
					")\n" + 
					")\n" + 
					"select * from t1\n" + 
					"union\n" + 
					"select * from t2";

					
			}else{
				sql =
					"SELECT a.sid,a.yid,a.seq,a.facility,a.product_code,a.brand,a.test_mode,\n" +
					"item,a.snova_id, a.version,lower_limit,flag1,upper_limit,flag2,\n" +
				  	"a.item_type,a.item_mode2,a.item_bins2,a.action,a.change_ipn,a.by_lot_dg, a.DGRADE_SPECIAL_IPN,a.dg_action,\n" +
				  	"a.route_name,a.start_step,a.remark\n" +
					"FROM tf_yield_definition a, tf_information b\n" +
					"where b.product_body = ?" +
					"\nand b.brand = ?" +
					"\nand b.version = ?" +
					"\nand a.sid = b.sid" +
					"\nand a.facility = ?" +
					"\norder by seq";
			}
			TDSLogger.println(sql);
			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, productname);
			ps.setString(2, brand);
			ps.setString(3, version);
			ps.setInt(4, (proc_type.equals("WS")?0:1));
			if(producttype.equals("NVM") && proc_type.equals("WS")){
				ps.setString(5, productname);
				ps.setString(6, brand);
				ps.setString(7, version);
				ps.setInt(8, (proc_type.equals("WS")?0:1));
			}
			ResultSet rs = ps.executeQuery();
			ArrayList tmp = new ArrayList();

			while (rs.next()) {
				YieldDefBean bean = new YieldDefBean();
				bean.setProduct_code(rs.getString("product_code"));
				bean.setBrands(rs.getString("brand"));
				bean.setTest_mode(rs.getString("test_mode"));
				bean.setLower_limit(rs.getString("lower_limit"));
				bean.setFlag1(rs.getString("flag1"));
				if(rs.getString("item")!=null && rs.getString("item").startsWith("AEBRetentionBIN"))
					bean.setItem(rs.getString("item")+"-"+rs.getString("snova_id")+"-V"+rs.getString("version"));
				else	
					bean.setItem(rs.getString("item"));
				bean.setItem_type(Integer.parseInt(rs.getString("item_type")));
				bean.setFlag2(rs.getString("flag2"));
				bean.setUpper_limit(rs.getString("upper_limit"));
				bean.setItem_mode2(rs.getString("item_mode2"));
				bean.setItem_bins2(rs.getString("item_bins2"));
				bean.setAction(rs.getString("action"));
				bean.setChange_ipn(rs.getString("change_ipn"));
				bean.setRoute_name(rs.getString("route_name"));
				bean.setStart_step(rs.getString("start_step"));
				bean.setRemark(rs.getString("remark"));
                bean.setBy_lot_dg(rs.getString("by_lot_dg")== null?"":rs.getString("by_lot_dg"));
                bean.setDgrade_special_ipn(rs.getString("dgrade_special_ipn")== null?"":rs.getString("dgrade_special_ipn"));
                bean.setDg_action(rs.getString("dg_action")== null?"":rs.getString("dg_action"));
				tmp.add(bean);
			}
			ps.clearParameters();
			ps.close();
			rs.close();
			return (YieldDefBean[]) tmp.toArray(new YieldDefBean[0]);
/*
			if (tmp.isEmpty()) {
				return (YieldDefBean[])null;
			} else {
				return (YieldDefBean[]) tmp.toArray(new YieldDefBean[0]);
			}
*/			
		}
		catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return null;
		}
		finally {
			DBConnection.close(conn);
			conn = null;	
		}
	}
	public static TFBomRouteBean[] OIQueryStep14(String productname,
            String brand,
            String version) {

        Connection conn = null;
        try {
            String sql = "select 1 as seq, a.ws_group_key as group_key, a.product_body, a.mask_option, a.db_with_code, a.ws_route, '' as backend_option, '' as fg_with_code, '' as pin_count, '' as package_type, '' as  ft_route \n" +
                         "from if_tf_bom_route a \n" +
                         "where product_body = '" + productname + "'\n" +
                         " and a.brand = '" + brand + "' and a.version = '" +  version + "'\n" +
                         " and a.rectype != 'E' \n" + 
                         " and a.ws_group_key is not null \n" +
                         " union " +
                         "select 2 as seq, ft_group_key as group_key,  a.product_body, '' as mask_option, '' as db_with_code, '' as ws_route, a.backend_option, a.fg_with_code,  to_char(a.pin_count) pin_count, a.package_type, a.ft_route \n" +
                         "from if_tf_bom_route a \n" +
                         "where a.product_body = '" + productname + "'\n" +
                         " and a.brand = '" + brand + "' and a.version = '" +  version + "'\n" +
                         " and a.rectype != 'E' \n" + 
                         " and a.ft_group_key is not null \n" +
                         " order by seq, group_key, mask_option, db_with_code, ws_route, backend_option, fg_with_code, pin_count, package_type, ft_route ";

            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ArrayList tmp = new ArrayList();

            while (rs.next()) {
                TFBomRouteBean bean = new TFBomRouteBean();
                bean.setGroup_key(rs.getString("group_key"));
                bean.setProduct_body(rs.getString("Product_body"));
                bean.setMask_option(rs.getString("mask_option"));
                bean.setDb_with_code(rs.getString("db_with_code"));
                bean.setWs_route(rs.getString("ws_route"));
                bean.setBackend_option(rs.getString("backend_option"));
                bean.setFg_with_code(rs.getString("fg_with_code"));
                bean.setPin_count(rs.getString("pin_count"));
                bean.setPackage_type(rs.getString("package_type"));
                bean.setFt_route(rs.getString("ft_route"));
                tmp.add(bean);
            }
            ps.clearParameters();
            ps.close();
            rs.close();
            if (tmp.isEmpty()) {
                return (TFBomRouteBean[])null;
            } else {
                return (TFBomRouteBean[]) tmp.toArray(new TFBomRouteBean[0]);
            }
        }catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        }finally {
            DBConnection.close(conn);
            conn = null;
        }
  }
	
	
}
