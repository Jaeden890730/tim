package com.mxic.oiplus.xtrarom.oisearch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mxic.oiplus.xtrarom.oimaintain.PBCTestParameterBean;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.oiplus.oimaintain.ProTestRouteBean;;

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

  public static ProTestRouteBean[] OIQueryStep1(String productname,
                                                  String brand,
                                                  String version) {

    Connection conn = null;
    try {
      //9i¥Îªk
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
        bean.setTesttime(rs.getString("test_time"));
        bean.setTimeunit(rs.getString("time_unit"));
        bean.setTemperature(rs.getString("temperature"));
        bean.setSamplingtest(rs.getString("sampling_test"));
        bean.setReworkstep(rs.getString("rework_step"));
        bean.setTesttime2(rs.getString("test_time2"));
        bean.setTimeunit2(rs.getString("time_unit2"));
        bean.setRemark(rs.getString("remark"));
        bean.setQcactualmode(rs.getString("qc_actual_mode"));
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
      String sql = "select a.*, b.route_cat from tf_bom_route_xrom a, tf_route_master b\n"+"" +
      		"where product_body = '" +productname +
      		"'\nand version = '" + version +
      		"'\nand tag != 2\n"+
      		"and a.ft_route = b.route_name(+)\n"+"" +
      		"order by ft_route_code, sort_route_code, ws_route, ws_route_add, ws_route_add1";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      while (rs.next()) {
        TFBomRouteBean bean = new TFBomRouteBean();
        bean.setSid(rs.getString("sid"));
        bean.setProduct_body(rs.getString("Product_body"));
        bean.setVersion(rs.getString("version"));
        bean.setBody_version(rs.getString("body_version"));
        bean.setCode_no(rs.getString("code_no"));
        bean.setPin_count(rs.getString("pin_count"));
        bean.setPackage_code(rs.getString("package_code"));
        bean.setFt_route_code(rs.getString("ft_route_code"));
        bean.setRoute_type(rs.getString("route_type"));
        bean.setFt_route(rs.getString("ft_route"));
        bean.setMask_option(rs.getString("mask_option"));
        bean.setMask_option_rev(rs.getString("mask_option_rev"));
        bean.setSort_route_code(rs.getString("sort_route_code"));
        bean.setWs_route(rs.getString("ws_route"));
        bean.setWs_route_add(rs.getString("ws_route_add"));
        bean.setWs_route_add1(rs.getString("ws_route_add1"));
        bean.setWs_route_add2(rs.getString("ws_route_add2"));
        bean.setWs_route_add3(rs.getString("ws_route_add3"));
        bean.setWs_route_add4(rs.getString("ws_route_add4"));
        bean.setFt_comment(rs.getString("ft_comment"));
        bean.setWs_comment(rs.getString("ws_comment"));
        bean.setFtAddroute(rs.getString("ft_route_add"));
        bean.setFtAddroute1(rs.getString("ft_route_add1"));
        bean.setFtAddroute2(rs.getString("ft_route_add2"));
        bean.setFtAddroute3(rs.getString("ft_route_add3"));
        bean.setFtAddroute4(rs.getString("ft_route_add4"));
        bean.setFtAddroute5(rs.getString("ft_route_add5"));
        bean.setRoute_cat(rs.getString("route_cat"));
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
  public static TFBomReRouteBean[] OIQueryStep3(String productname,
                                              String brand,
                                              String version) {

    Connection conn = null;
    try {
      String sql = "select * from tf_bom_reroute_xrom where product_body = '" +
          productname +
          "'  and version = '" +
          version +
          "' and tag != 2 order by ft_route, ft_route_add ";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      while (rs.next()) {
        TFBomReRouteBean bean = new TFBomReRouteBean();
        bean.setSid(rs.getString("sid"));
        bean.setProduct_body(rs.getString("Product_body"));
        bean.setVersion(rs.getString("version"));
        bean.setBody_version(rs.getString("body_version"));
        bean.setMask_option(rs.getString("mask_option"));
        bean.setMask_option_rev (rs.getString("mask_option_rev"));
        bean.setCode_no(rs.getString("code_no"));
        bean.setPin_count(rs.getString("pin_count"));
        bean.setPackage_code(rs.getString("package_code"));
        bean.setFt_route(rs.getString("ft_route"));
        if (rs.getString("route_type").equals("0"))
          bean.setRoute_type("erase code");
        else if (rs.getString("route_type").equals("1"))
          bean.setRoute_type("boot code");
        else if (rs.getString("route_type").equals("2"))
          bean.setRoute_type("erase code + boot code");
        else
          bean.setRoute_type("repair");
        bean.setRecycle_code(rs.getString("recycle_code"));
        bean.setFt_comment(rs.getString("ft_comment"));
        bean.setFtAddroute(rs.getString("ft_route_add"));
        tmp.add(bean);
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if (tmp.isEmpty()) {
        return (TFBomReRouteBean[])null;
      } else {
        return (TFBomReRouteBean[]) tmp.toArray(new TFBomReRouteBean[0]);
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


  public static TFTestParameterWSBean[] OIQueryStep4(String productname,
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

  public static TFTestParameterFTBean[] OIQueryStep5(String productname,
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
        bean.setI_grade(rs.getString("i_grade"));
        bean.setC_grade(rs.getString("c_grade"));
        bean.setTf_comment(rs.getString("tf_comment"));
        bean.setHw_configure(rs.getString("hw_configure"));
        bean.setOne_main_pgm_group_version(rs.getString("one_main_pgm_group_version"));
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
  public static PBCTestParameterBean[] OIQueryStep6(String productname,
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
  public static TFMainRouteSubBean[] OIQueryStep8(String productname,
                                                   String brand,
                                                   String version) {

    Connection conn = null;
    try {
      String sql =
          "select * from tf_main_route_xrom where product_body = '" +
          productname + "' and version = '" +
          version + "' and route_type='0' order by main_route, map_route";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      while (rs.next()) {
        TFMainRouteSubBean bean = new TFMainRouteSubBean();
        bean.setSid(rs.getString("sid"));
        bean.setProduct_body(rs.getString("Product_body"));
        //bean.setBrand(rs.getString("brand"));
        bean.setVersion(rs.getString("version"));
        bean.setRoute_type(rs.getString("route_type"));
        bean.setMain_route(rs.getString("main_route"));
        bean.setMap_route(rs.getString("map_route"));
        bean.setRemark(rs.getString("remark"));
        tmp.add(bean);
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if (tmp.isEmpty()) {
        return (TFMainRouteSubBean[])null;
      } else {
        return (TFMainRouteSubBean[]) tmp.toArray(new
            TFMainRouteSubBean[0]);
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
  public static TFMainRouteReBean[] OIQueryStep9(String productname,
                                                   String brand,
                                                   String version) {

    Connection conn = null;
    try {
      String sql =
          "select * from tf_main_route_xrom where product_body = '" +
          productname + "' and version = '" +
          version + "' and route_type='1' order by main_route, map_route";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      while (rs.next()) {
        TFMainRouteReBean bean = new TFMainRouteReBean();
        bean.setSid(rs.getString("sid"));
        bean.setProduct_body(rs.getString("Product_body"));
        //bean.setBrand(rs.getString("brand"));
        bean.setVersion(rs.getString("version"));
        bean.setRoute_type(rs.getString("route_type"));
        bean.setMain_route(rs.getString("main_route"));
        bean.setMap_route(rs.getString("map_route"));
        bean.setRemark(rs.getString("remark"));
        tmp.add(bean);
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if (tmp.isEmpty()) {
        return (TFMainRouteReBean[])null;
      } else {
        return (TFMainRouteReBean[]) tmp.toArray(new
            TFMainRouteReBean[0]);
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

  public static TestFlowBean[] OIQueryStep10(String productname,
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
  public static TFBomRouteBean[] OIQueryStep14(String productname,
          String brand,
          String version) {

    Connection conn = null;
    try {
    	String sql = "select 1 as seq, a.ws_group_key as group_key, a.product_body, a.mask_option, a.db_with_code, a.ws_route, '' as backend_option, '' as fg_with_code, '' as pin_count, '' as package_type, '' as  ft_route \n" +
                     "from if_tf_bom_route a \n" +
                     "where product_body = '" + productname + "'\n" +
                     //" and a.brand = '" + brand + "'\n" +
                     " and a.version = '" +  version + "'\n" +
                     " and a.rectype != 'E' \n" + 
                     " and a.ws_group_key is not null \n" +
                     " union " +
                     "select 2 as seq, ft_group_key as group_key,  a.product_body, mask_option, '' as db_with_code, '' as ws_route, a.backend_option, a.fg_with_code,  to_char(a.pin_count) pin_count, a.package_type, a.ft_route \n" +
                     "from if_tf_bom_route a \n" +
                     "where a.product_body = '" + productname + "'\n" +
//                   " and a.brand = '" + brand + "'\n" +
                     " and a.version = '" +  version + "'\n" +
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
