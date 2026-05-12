package com.mxic.oiplus.eif;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.sql.Date;




import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.mxic.oiplus.mrom.pdf.PDFdiffService;
import com.mxic.oiplus.oimaintain.OiMaintainService;
import com.mxic.oiplus.oimaintain.ProTestRouteBeanAF;
import com.mxic.oiplus.pdf.pdfService;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.TDSProperties;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.SafeExec;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.oiplus.util.DateUtil;
import com.mxic.stoptest.util.DBUtil;

// copy from com.mxic.oiplus.oimaintain.OIReleaseService.java

public class EifCATReleaseAllApplyService {

  public EifCATReleaseAllApplyService() {
  }

  // Get OI's SID by Product Body + Brand + Version for certain Status
  public static String getOIsid(String product_body,
                                String brand,
                                String version,
                                String status,
                                String product_type) {
      String sql;
      String sid = null;
      Connection conn = null;

      try {
          conn = DBConnection.getConnection();
          sql = "select sid from tf_information " +
              " where product_body = '" + product_body + "' " +
              "  and version = " + version +
              "  and brand = '" + brand + "'" +
              "  and status = '" + status + "'" +
              "  and product_type = '" + product_type + "'";

          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();
          if (rs.next()) {
              sid = rs.getString("sid");
          }
	      ps.clearParameters();
	      ps.close();
          rs.close();
	      if(ps != null)
	    	  ps = null;
	      if(rs != null)
          rs = null;
          }
      catch (Exception ex) {
          ex.printStackTrace();
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return "exception";
      }
      finally {
          DBConnection.close(conn);
      }
      return sid;
  }

  
  
//get recently oi data (released/applied)
  // return Array of "product_body,brand,version,status,product_type"
  public static ArrayList getRecentlyOI(Connection conn) {
      String sql;
      ArrayList vl = null;
      //Connection conn = null;

      try {
          //conn = DBConnection.getConnection();
          sql = "select sid, product_body, brand, version, status, product_type from tf_information a, if_interface_time e " +
                  " where a.status in ('R') " +
                  //" and product_type = 'NVM' " +
                  //" and product_body = '6615' " +
                  //" and version = 77 " +
                  //" and log_time between to_date('2008-12-14 11:38:00','yyyy-mm-dd hh24:mi:ss') and to_date('2008-12-30 23:00:00','yyyy-mm-dd hh24:mi:ss') " +
                  "and e.interface = 'EifCATReleaseAllApply' \n" +
            	  "and a.log_time between e.last_time and e.current_time \n" +
                  //" and log_time > to_date(to_char(sysdate-0.01,'yyyymmdd hh24mi'),'yyyymmdd hh24mi') " + //lai-20070308-15/24*60 ";
                  " order by a.product_body, a.brand, a.log_time  ";

          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();

          while (rs.next()) {
              if (vl == null)
                  vl = new ArrayList();

              vl.add(rs.getString("sid")+","+rs.getString("product_body")+","+rs.getString("brand")+","+rs.getString("version")+","+rs.getString("status")+","+rs.getString("product_type"));
          }
	      ps.clearParameters();
	      ps.close();
          rs.close();
	      if(ps != null)
	    	  ps = null;
	      if(rs != null)
          rs = null;
          }
      catch (Exception ex) {
          ex.printStackTrace();
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return null;
      }
      finally {
          //DBConnection.close(conn);
      }
      return vl;
  }
  
//get oi data by product_type (released/applied)
  // return Array of "product_body,brand,version,status,product_type"
  public static ArrayList getProductTypeOI(String product_type, Connection conn) {
      String sql;
      ArrayList vl = null;
      //Connection conn = null;

      try {
          //conn = DBConnection.getConnection();
          sql = "select a.sid, a.product_body, a.brand, a.version, a.status, a.product_type " + 
                " from tf_information a, tf_cosign_version_vw b " +
                " where a.status in ('A','R') " +
                "   and a.product_body = b.product_body " +
                "   and a.brand = b.brand " +
                "   and a.version = b.version " +
                "   and a.status = b.status " +
                "   and a.product_type = '" + product_type + "' " +
                " order by a.status desc, a.product_body, a.brand, a.version,  a.product_type ";

          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();

          while (rs.next()) {
              if (vl == null)
                  vl = new ArrayList();

              vl.add(rs.getString("sid")+","+rs.getString("product_body")+","+rs.getString("brand")+","+rs.getString("version")+","+rs.getString("status")+","+rs.getString("product_type"));
          }
	      ps.clearParameters();
	      ps.close();
          rs.close();
	      if(ps != null)
	    	  ps = null;
	      if(rs != null)
          rs = null;
          }
      catch (Exception ex) {
          ex.printStackTrace();
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return null;
      }
      finally {
          //DBConnection.close(conn);
      }
      return vl;
  }
  
  public static ArrayList getProductTypeOICurrent(String product_type, Connection conn) {
      String sql;
      ArrayList vl = null;
      //Connection conn = null;

      try {
          //conn = DBConnection.getConnection();
          sql = "select a.sid, a.product_body, a.brand, a.version, a.status, a.product_type " + 
                " from tf_information a, tf_Current_Version_Vw b " +
                " where a.status in ('R') " +
                "   and a.product_body = b.product_body " +
                "   and a.brand = b.brand " +
                "   and a.version = b.version " +
                "   and a.product_type = '" + product_type + "' " +
                " order by a.product_body, a.brand, a.version,  a.product_type ";
          TDSLogger.println(sql);
          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();

          while (rs.next()) {
              if (vl == null)
                  vl = new ArrayList();

              vl.add(rs.getString("sid")+","+rs.getString("product_body")+","+rs.getString("brand")+","+rs.getString("version")+","+rs.getString("status")+","+rs.getString("product_type"));
          }
	      ps.clearParameters();
	      ps.close();
          rs.close();
	      if(ps != null)
	    	  ps = null;
	      if(rs != null)
          rs = null;
          }
      catch (Exception ex) {
          ex.printStackTrace();
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return null;
      }
      finally {
          //DBConnection.close(conn);
      }
      return vl;
  }
  
	public static ArrayList getProductTypeOICustom(Connection conn) {
		String sql;
		ArrayList vl = null;

		try {
			sql = 
					"SELECT A.SID,\n" +
							"       A.PRODUCT_BODY,\n" + 
							"       A.BRAND,\n" + 
							"       A.VERSION,\n" + 
							"       A.STATUS,\n" + 
							"       C.PRODUCT_TYPE,\n" + 
							"       A.UPDATETIME\n" + 
							"  FROM TF_COSIGN_VERSION_VW A, TIM_WIP_USING1 B, TF_INFORMATION C\n" + 
							" WHERE A.PRODUCT_BODY = B.PRODUCT_TYPE\n" + 
							"   AND A.SID = C.SID";


			TDSLogger.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				if (vl == null)
					vl = new ArrayList();

				vl.add(rs.getString("sid") + "," + rs.getString("product_body") + "," + rs.getString("brand") + "," + rs.getString("version") + "," + rs.getString("status") + "," + rs.getString("product_type"));
			}
		      ps.clearParameters();
		      ps.close();
			rs.close();
		      if(ps != null)
		    	  ps = null;
		      if(rs != null)
			rs = null;
		} catch (Exception ex) {
			ex.printStackTrace();
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return null;
		} finally {

		}
		return vl;
	}
  

  public static ArrayList getRecentlyOIByPara(String product_body, String brand, String version, String product_type, Connection conn) {
      String sql;
      ArrayList vl = null;
      //Connection conn = null;

      try {
          //conn = DBConnection.getConnection();
          sql = "select sid, product_body, brand, version, status, product_type from tf_information " +
                " where status in ('R') " +
                " and product_body= '" + product_body + "' and brand = '" + brand + "' and version = '" + version + "' and product_type = '" + product_type + "'";

          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();

          while (rs.next()) {
              if (vl == null)
                  vl = new ArrayList();

              vl.add(rs.getString("sid")+","+rs.getString("product_body")+","+rs.getString("brand")+","+rs.getString("version")+","+rs.getString("status")+","+rs.getString("product_type"));
          }
	      ps.clearParameters();
	      ps.close();
          rs.close();
	      if(ps != null)
	    	  ps = null;
	      if(rs != null)
          rs = null;
          }
      catch (Exception ex) {
          ex.printStackTrace();
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return null;
      }
      finally {
          //DBConnection.close(conn);
      }
      return vl;
  }
//select NVM WS 最新版或會簽中 TEST1 之 Product Group vs Route data
//insert data to CAT_Route_Pgm
  public static boolean insert_cat_route_pgm_all_ws_NVM(    ProTestRouteBeanAF fm,
                                         String table,
                                         String product_type,
                                         String status,
                                         Connection conn) {

    try {

      String sql = "select distinct " +
          //"'Main' route_type, a.product_body,a.brand,a.version,a.mask_option maskbeoption,a.db_with_code,a.ws_route,a.ws_route_add, '' packagecode, '' packagetype, '' pincount, '' codeno," +
          "'Main' route_type, a.product_body,a.brand,a.version,a.mask_option maskbeoption,a.db_with_code,a.ws_route,a.ws_route_add, '' packagecode, '' packagetype, '' pincount, '' codeno," +
          "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname, '' actualprogramname,c.temperature c_grade, '' i_grade, '' w_grade, '' y_grade,'' j_grade,'' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade, '' s_grade, ' ' bodysize, a.sales_form, c.hw_configure " +
          "from tf_bom_route"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c " +
          "where a.sid = ? " +
          "and trim(ws_route) is not null and ws_route != 'NA' " +
          "and a.ws_route = b.route_name " +
          "and a.sid = c.sid " +
          "and a.mask_option = c.mask_option " +
          "and b.test_mode = c.test_type " +
          "and a.tag != 2 " +
         // "and c.site = 'TEST1' " +
          "union " +
          "select distinct " +
          "'Add.' route_type, a.product_body,a.brand,a.version,a.mask_option maskbeoption,a.db_with_code,a.ws_route,a.ws_route_add, '' packagecode, '' packagetype, '' pincount, '' codeno," +
          "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname, '' actualprogramname,c.temperature c_grade, '' i_grade,'' w_grade, '' y_grade,'' j_grade,'' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade, '' s_grade, ' ' bodysize, a.sales_form, c.hw_configure  " +
          "from tf_bom_route"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c " +
          "where a.sid = ? " +
          "and trim(ws_route_add) is not null and ws_route_add != 'NA' " +
          "and a.ws_route_add = b.route_name " +
          "and a.sid = c.sid " +
          "and a.mask_option = c.mask_option " +
          "and b.test_mode = c.test_type " +
          "and a.tag != 2 " +
         // "and c.site = 'TEST1' " +
          "order by product_body,maskbeoption,db_with_code,ws_route,ws_route_add,route_type desc,testmode ";
      TDSLogger.println(sql);
      TDSLogger.println("paremeter = "+fm.getSid()+ "," +fm.getSid() );
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,String.valueOf(fm.getSid()));
      ps.setString(2,String.valueOf(fm.getSid()));
      ResultSet rs = ps.executeQuery();

      String GroupKey = "";
      
      while(rs.next()){
          GroupKey = "";
          String routename = "";
          GroupKey = getGroupKey(product_type, rs.getString("product_body"), rs.getString("brand"), rs.getString("maskbeoption"), "", "",
    	             null, null, rs.getString("db_with_code"), rs.getString("ws_route"), rs.getString("ws_route_add"), rs.getString("sales_form"), 0);
          String updatetime = getLogTime(fm.getSid());
          if (rs.getString("route_type").equals("Main")) {
          	routename = rs.getString("ws_route");
          }else {
        	routename = rs.getString("ws_route_add");
          }
          /*String temperature = " ";
            if (rs.getString("temperature") != null){
          	temperature = rs.getString("temperature");
          	if (!temperature.equals("ROOM TEMP") && !temperature.equals(""))
          		temperature = temperature + "℃";
          }*/
          boolean result = CATRoutePgmAll_insert(conn, GroupKey, rs.getString("product_body"), rs.getString("brand"), 
        		  rs.getString("version"), status, updatetime, rs.getString("route_type"), 
        		  routename, rs.getString("testmode"), rs.getString("maskbeoption"), rs.getString("packagecode"), 
        		  rs.getString("packagetype"), rs.getString("pincount"), rs.getString("codeno"), rs.getString("c_grade"), 
        		  rs.getString("i_grade"), rs.getString("w_grade"), rs.getString("y_grade"), rs.getString("j_grade"), rs.getString("k_grade"), rs.getString("l_grade"), rs.getString("n_grade"),rs.getString("b_grade"),rs.getString("e_grade"),rs.getString("s_grade"), 
        		  rs.getString("bodysize"), rs.getString("testertype"), 
        		  rs.getString("pgm_id"), rs.getString("programname"), rs.getString("actualprogramname"), product_type, rs.getString("hw_configure"), rs.getString("site"));
          if(result == false)
        	  return false;
        
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if(ps != null)
    	  ps = null;
      if(rs != null)
    	  rs = null;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    } finally {
    }
    return true;
  }
//select NVM WS MCP最新版或會簽中 TEST1 之 Product Group vs Route data
//insert data to CAT_Route_Pgm
  public static boolean insert_cat_route_pgm_all_ws_MCP_NVM(    ProTestRouteBeanAF fm,
                                         String table,
                                         String product_type,
                                         String status,
                                         Connection conn) {

    try {

      String sql = "select distinct " +
          //"'Main' route_type, a.product_body,a.brand,a.version,a.mask_option maskbeoption,a.db_with_code,a.ws_route,a.ws_route_add, '' packagecode, '' packagetype, '' pincount, '' codeno," +
          "'Main' route_type, a.product_body,a.brand,a.version,a.com_mask_option maskbeoption,a.db_with_code,a.ws_route,a.ws_route_add, '' packagecode, '' packagetype, '' pincount, '' codeno," +
          "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname, '' actualprogramname,c.temperature c_grade, '' i_grade, '' w_grade, '' y_grade,'' j_grade,'' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade, '' s_grade, ' ' bodysize, a.sales_form, c.hw_configure " +
          "from tf_bom_route_mcp"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c " +
          "where a.sid = ? " +
          "and trim(ws_route) is not null and ws_route != 'NA' " +
          "and a.ws_route = b.route_name " +
          "and a.sid = c.sid " +
          "and a.com_mask_option = c.mask_option " +
          "and b.test_mode = c.test_type " +
          "and a.tag != 2 " +
        //  "and c.site = 'TEST1' " +
          "union " +
          "select distinct " +
          "'Add.' route_type, a.product_body,a.brand,a.version,a.com_mask_option maskbeoption,a.db_with_code,a.ws_route,a.ws_route_add, '' packagecode, '' packagetype, '' pincount, '' codeno," +
          "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname, '' actualprogramname,c.temperature c_grade, '' i_grade,'' w_grade, '' y_grade,'' j_grade,'' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade, '' s_grade, ' ' bodysize, a.sales_form, c.hw_configure " +
          "from tf_bom_route_mcp"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c " +
          "where a.sid = ? " +
          "and trim(ws_route_add) is not null and ws_route_add != 'NA' " +
          "and a.ws_route_add = b.route_name " +
          "and a.sid = c.sid " +
          "and a.com_mask_option = c.mask_option " +
          "and b.test_mode = c.test_type " +
          "and a.tag != 2 " +
         // "and c.site = 'TEST1' " +
          "order by product_body,maskbeoption,db_with_code,ws_route,ws_route_add,route_type desc,testmode ";
      TDSLogger.println(sql);
      TDSLogger.println("paremeter = "+fm.getSid()+ "," +fm.getSid() );
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,String.valueOf(fm.getSid()));
      ps.setString(2,String.valueOf(fm.getSid()));
      ResultSet rs = ps.executeQuery();

      String GroupKey = "";
      
      while(rs.next()){
          GroupKey = "";
          String routename = "";
          GroupKey = getGroupKey(product_type, rs.getString("product_body"), rs.getString("brand"), rs.getString("maskbeoption"), "", "",
    	             null, null, rs.getString("db_with_code"), rs.getString("ws_route"), rs.getString("ws_route_add"), rs.getString("sales_form"), 0);
          String updatetime = getLogTime(fm.getSid());
          if (rs.getString("route_type").equals("Main")) {
          	routename = rs.getString("ws_route");
          }else {
        	routename = rs.getString("ws_route_add");
          }
          /*String temperature = " ";
            if (rs.getString("temperature") != null){
          	temperature = rs.getString("temperature");
          	if (!temperature.equals("ROOM TEMP") && !temperature.equals(""))
          		temperature = temperature + "℃";
          }*/
          boolean result = CATRoutePgmAll_insert(conn, GroupKey, rs.getString("product_body"), rs.getString("brand"), 
        		  rs.getString("version"), status, updatetime, rs.getString("route_type"), 
        		  routename, rs.getString("testmode"), rs.getString("maskbeoption"), rs.getString("packagecode"), 
        		  rs.getString("packagetype"), rs.getString("pincount"), rs.getString("codeno"), rs.getString("c_grade"), 
        		  rs.getString("i_grade"), rs.getString("w_grade"), rs.getString("y_grade"), rs.getString("j_grade"), rs.getString("k_grade"), rs.getString("l_grade"), rs.getString("n_grade"),rs.getString("b_grade"),rs.getString("e_grade"),rs.getString("s_grade"), 
        		  rs.getString("bodysize"), rs.getString("testertype"), 
        		  rs.getString("pgm_id"), rs.getString("programname"), rs.getString("actualprogramname"), product_type, rs.getString("hw_configure"), rs.getString("site"));
          if(result == false)
        	  return false;
        
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if(ps != null)
    	  ps = null;
      if(rs != null)
    	  rs = null;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    } finally {
    }
    return true;
  }  
  
//select MROM WS 最新版或會簽中 TEST1 之 Product Group vs Route data
//insert data to CAT_Route_Pgm
  public static boolean insert_cat_route_pgm_all_ws_MROM(    ProTestRouteBeanAF fm,
                                         String table,
                                         String product_type,
                                         String status,
                                         Connection conn) {

    try {
    	
      String sql = "select distinct " +
        "'Main' route_type,a.product_body,'MX' brand,a.version,a.mask_option maskbeoption,a.ws_route,'' packagecode, '' packagetype, '' pincount, '' codeno," +
        "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,c.temperature c_grade, '' i_grade,'' w_grade,'' y_grade,'' j_grade,'' k_grade, '' l_grade,'' n_grade,'' b_grade,'' e_grade,'' s_grade, ' ' bodysize,a.sales_form, hw_configure " +
        "from tf_bom_route_mrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c " +
        "where a.sid = ? " +
        "and trim(ws_route) is not null and ws_route != 'NA' " +
        "and a.ws_route = b.route_name " +
        "and a.sid = c.sid " +
        "and a.mask_option = c.mask_option " +
        "and b.test_mode = c.test_type " +
        "and a.tag != 2 " + 
      //  "and c.site = 'TEST1' " +
        "order by product_body,maskbeoption,ws_route,sales_form,testmode,programname ";
   
      TDSLogger.println(sql);
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,String.valueOf(fm.getSid()));
      ResultSet rs = ps.executeQuery();	

      
      String GroupKey = "";
      
      while(rs.next()){
          GroupKey = "";
          GroupKey = getGroupKey(product_type, rs.getString("product_body"), rs.getString("brand"), rs.getString("maskbeoption"), "", "",
    	             null, null, "", rs.getString("ws_route"), "", rs.getString("sales_form"), 0);
          String updatetime = getLogTime(fm.getSid());
          /*String temperature = " ";
            if (rs.getString("temperature") != null){
          	temperature = rs.getString("temperature");
          	if (!temperature.equals("ROOM TEMP") && !temperature.equals(""))
          		temperature = temperature + "℃";
          }*/
          boolean result = CATRoutePgmAll_insert(conn, GroupKey, rs.getString("product_body"), rs.getString("brand"), 
        		  rs.getString("version"), status, updatetime, rs.getString("route_type"), 
        		  rs.getString("ws_route"), rs.getString("testmode"), rs.getString("maskbeoption"), rs.getString("packagecode"), 
        		  rs.getString("packagetype"), rs.getString("pincount"), rs.getString("codeno"), rs.getString("c_grade"), 
        		  rs.getString("i_grade"),rs.getString("w_grade"),rs.getString("y_grade"), rs.getString("j_grade"),rs.getString("k_grade"),rs.getString("l_grade"),rs.getString("n_grade"),rs.getString("b_grade"),rs.getString("e_grade"),rs.getString("s_grade"), rs.getString("bodysize"), rs.getString("testertype"), 
        		  rs.getString("pgm_id"), rs.getString("programname"), rs.getString("actualprogramname"), product_type, rs.getString("hw_configure"), rs.getString("site"));
          if(result == false)
        	  return false;
        
      }
      ps.clearParameters();
      ps.close();
      rs.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    } finally {
    }
    return true;
  }
  
//select XROM WS 最新版或會簽中 TEST1 之 Product Group vs Route data
//insert data to CAT_Route_Pgm
  public static boolean insert_cat_route_pgm_all_ws_XROM(    ProTestRouteBeanAF fm,
                                         String table,
                                         String product_type,
                                         String status,
                                         Connection conn) {

    try {

    	String sql = "select distinct \n" +
        //"'Main' route_type, 1 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,a.ws_route_add,'' packagecode, '' packagetype, '' pincount, a.code_no codeno,\n" +
    	"'Main' route_type, 1 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,a.ws_route_add,'' packagecode, '' packagetype, '' pincount, a.code_no codeno,\n" +
        "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,c.temperature c_grade, '' i_grade,'' w_grade,'' y_grade,'' j_grade,'' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade, '' s_grade,' ' bodysize, hw_configure \n" +
        "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c \n" +
        "where a.sid = ? \n" +
        "and trim(ws_route) is not null and ws_route != 'NA' \n" +
        "and a.ws_route = b.route_name \n" +
        "and a.sid = c.sid \n" +
        //"and c.brand = ' ' " +
        "and a.mask_option = c.mask_option \n" +
        "and b.test_mode = c.test_type \n" +
        "and a.tag != 2 \n" +
        //"and c.site = 'TEST1' \n" +
        "union \n" +
        "select distinct \n" +
        "'Add1' route_type, 2 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,a.ws_route_add,'' packagecode, '' packagetype, '' pincount,a.code_no codeno, \n" +
        "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,c.temperature c_grade, '' i_grade,'' w_grade,'' y_grade, '' j_grade, '' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade,'' s_grade,' ' bodysize, hw_configure  \n" +
        "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c \n" +
        "where a.sid = ? \n" +
        "and trim(ws_route_add) is not null and ws_route_add != 'NA' \n" +
        "and a.ws_route_add = b.route_name \n" +
        "and a.sid = c.sid \n" +
        //"and c.brand = ' ' " +
        "and a.mask_option = c.mask_option \n" +
        "and b.test_mode = c.test_type \n" +
        "and a.tag != 2 \n" +
       // "and c.site = 'TEST1' \n" +
        "union \n" +
        "select distinct \n" +
        "'Add2' route_type, 3 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,a.ws_route_add1 ws_route_add,'' packagecode, '' packagetype, '' pincount,a.code_no codeno, \n" +
        "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,c.temperature c_grade, '' i_grade,'' w_grade,'' y_grade, '' j_grade, '' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade,'' s_grade,' ' bodysize, hw_configure  \n" +
        "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c \n" +
        "where a.sid = ? \n" +
        "and trim(ws_route_add1) is not null and ws_route_add1 != 'NA' \n" +
        "and a.ws_route_add1 = b.route_name \n" +
        "and a.sid = c.sid \n" +
        //"and c.brand = ' ' " +
        "and a.mask_option = c.mask_option \n" +
        "and b.test_mode = c.test_type \n" +
        "and a.tag != 2 \n" +
       // "and c.site = 'TEST1' \n" +
		"union \n" +
        "select distinct \n" +
        "'Add3' route_type, 4 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,a.ws_route_add2 ws_route_add,'' packagecode, '' packagetype, '' pincount,a.code_no codeno, \n" +
        "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,c.temperature c_grade, '' i_grade,'' w_grade,'' y_grade, '' j_grade, '' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade,'' s_grade,' ' bodysize, hw_configure  \n" +
        "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c \n" +
        "where a.sid = ? \n" +
        "and trim(ws_route_add2) is not null and ws_route_add2 != 'NA' \n" +
        "and a.ws_route_add2 = b.route_name \n" +
        "and a.sid = c.sid \n" +
        //"and c.brand = ' ' " +
        "and a.mask_option = c.mask_option \n" +
        "and b.test_mode = c.test_type \n" +
        "and a.tag != 2 \n" +
        //"and c.site = 'TEST1' \n" +
        "union \n" +
        "select distinct \n" +
        "'Add4' route_type, 5 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,a.ws_route_add3 ws_route_add,'' packagecode, '' packagetype, '' pincount,a.code_no codeno, \n" +
        "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,c.temperature c_grade, '' i_grade,'' w_grade,'' y_grade, '' j_grade, '' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade,'' s_grade,' ' bodysize, hw_configure  \n" +
        "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c \n" +
        "where a.sid = ? \n" +
        "and trim(ws_route_add3) is not null and ws_route_add3 != 'NA' \n" +
        "and a.ws_route_add3 = b.route_name \n" +
        "and a.sid = c.sid \n" +
        //"and c.brand = ' ' " +
        "and a.mask_option = c.mask_option \n" +
        "and b.test_mode = c.test_type \n" +
        "and a.tag != 2 \n" +
       // "and c.site = 'TEST1' \n" +
        "union \n" +
        "select distinct \n" +
        "'Add5' route_type, 6 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,a.ws_route_add4 ws_route_add,'' packagecode, '' packagetype, '' pincount,a.code_no codeno, \n" +
        "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,c.temperature c_grade, '' i_grade,'' w_grade,'' y_grade, '' j_grade, '' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade,'' s_grade,' ' bodysize, hw_configure  \n" +
        "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c \n" +
        "where a.sid = ? \n" +
        "and trim(ws_route_add4) is not null and ws_route_add4 != 'NA' \n" +
        "and a.ws_route_add4 = b.route_name \n" +
        "and a.sid = c.sid \n" +
        //"and c.brand = ' ' " +
        "and a.mask_option = c.mask_option \n" +
        "and b.test_mode = c.test_type \n" +
        "and a.tag != 2 \n" +
        //"and c.site = 'TEST1' \n" +
     // 20170516, add for subsitution route        
        "union \n" +
        "select distinct \n" +
        "'Sub.' route_type, 7 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,e.map_route ws_route_add,'' packagecode, '' packagetype, '' pincount,a.code_no codeno, \n" +
        "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,c.temperature c_grade, '' i_grade,'' w_grade,'' y_grade, '' j_grade, '' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade,'' s_grade,' ' bodysize, hw_configure  \n" +
        "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c, tf_main_route_xrom"+table+" e \n" +
        "where a.sid = ? \n" +
        "and trim(e.map_route) is not null and e.map_route != 'NA' \n" +
        "and e.map_route = b.route_name \n" +
        "and a.sid = c.sid \n" +
        "and a.sid = e.sid \n" +
        //"and c.brand = ' ' " +
        "and a.mask_option = c.mask_option \n" +
        "and b.test_mode = c.test_type \n" +
        "and e.route_type = 0 \n" +
        "and a.ws_route = e.main_route \n" +
        "and a.tag != 2 \n" +
        //"and c.site = 'TEST1' \n" +
        "union select distinct \n" +
    	  //"'Main' route_type, 1 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,a.ws_route_add, '' packagecode, '' packagetype, '' pincount,a.code_no codeno,\n" +
    	  "'Main' route_type, 1 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,a.ws_route_add, '' packagecode, '' packagetype, '' pincount,a.code_no codeno,\n" +
    	  "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,'' c_grade ,c.i_grade,'' w_grade,'' y_grade,'' j_grade, '' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade,'' s_grade ,' ' bodysize, hw_configure  \n" +
    	  "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_pbc"+table+" c \n" +
    	  "where a.sid = ? \n" +
    	  "and trim(ws_route) is not null and ws_route != 'NA' \n" +
    	  "and a.ws_route = b.route_name \n" +
    	  "and a.sid = c.sid \n" +
    	  "and a.mask_option = c.backend_option \n" +
    	  "and b.test_mode = c.test_type \n" +
    	  "and a.tag != 2 \n" +
         // "and c.site = 'TEST1' \n" +
          "union \n" +
        "select distinct \n" +
        "'Add1' route_type, 2 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,a.ws_route_add, '' packagecode, '' packagetype, '' pincount,a.code_no codeno,\n" +
        "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,'' c_grade , c.i_grade,'' w_grade,'' y_grade,'' j_grade, '' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade,'' s_grade ,' ' bodysize, hw_configure  \n" +
        "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_pbc"+table+" c \n" +
        "where a.sid = ? \n" +
        "and trim(ws_route_add) is not null and ws_route_add != 'NA' \n" +
        "and a.ws_route_add = b.route_name \n" +
        "and a.sid = c.sid \n" +
        "and a.mask_option = c.backend_option \n" +
        "and b.test_mode = c.test_type \n" +
        "and a.tag != 2 \n" +
  	    //"and c.site = 'TEST1' \n" +
        "union \n" +
    	  "select distinct \n" +
    	  "'Add2' route_type, 3 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,a.ws_route_add1 ws_route_add,'' packagecode, '' packagetype, '' pincount,a.code_no codeno, \n" +
    	  "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,'' c_grade , c.i_grade,'' w_grade,'' y_grade,'' j_grade, '' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade,'' s_grade ,' ' bodysize, hw_configure \n" +
    	  "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_pbc"+table+" c \n" +
    	  "where a.sid = ? \n" +
    	  "and trim(ws_route_add1) is not null and ws_route_add1 != 'NA' \n" +
    	  "and a.ws_route_add1 = b.route_name \n" +
    	  "and a.sid = c.sid \n" +
    	  "and a.mask_option = c.backend_option \n" +
    	  "and b.test_mode = c.test_type \n" +
    	  "and a.tag != 2 \n" +
         // "and c.site = 'TEST1' \n" +
		  "union \n" +
    	  "select distinct \n" +
    	  "'Add3' route_type, 4 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,a.ws_route_add2 ws_route_add,'' packagecode, '' packagetype, '' pincount,a.code_no codeno, \n" +
    	  "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,'' c_grade , c.i_grade,'' w_grade,'' y_grade,'' j_grade, '' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade,'' s_grade ,' ' bodysize, hw_configure  \n" +
    	  "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_pbc"+table+" c \n" +
    	  "where a.sid = ? \n" +
    	  "and trim(ws_route_add2) is not null and ws_route_add2 != 'NA' \n" +
    	  "and a.ws_route_add2 = b.route_name \n" +
    	  "and a.sid = c.sid \n" +
    	  "and a.mask_option = c.backend_option \n" +
    	  "and b.test_mode = c.test_type \n" +
    	  "and a.tag != 2 \n" +
         // "and c.site = 'TEST1' \n" +
          "union \n" +
    	  "select distinct \n" +
    	  "'Add4' route_type, 5 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,a.ws_route_add3 ws_route_add,'' packagecode, '' packagetype, '' pincount,a.code_no codeno, \n" +
    	  "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,'' c_grade , c.i_grade,'' w_grade,'' y_grade,'' j_grade, '' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade,'' s_grade ,' ' bodysize, hw_configure \n" +
    	  "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_pbc"+table+" c \n" +
    	  "where a.sid = ? \n" +
    	  "and trim(ws_route_add3) is not null and ws_route_add3 != 'NA' \n" +
    	  "and a.ws_route_add3 = b.route_name \n" +
    	  "and a.sid = c.sid \n" +
    	  "and a.mask_option = c.backend_option \n" +
    	  "and b.test_mode = c.test_type \n" +
    	  "and a.tag != 2 \n" +
         // "and c.site = 'TEST1' \n" +
          "union \n" +
    	  "select distinct \n" +
    	  "'Add5' route_type, 6 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,a.ws_route_add4 ws_route_add,'' packagecode, '' packagetype, '' pincount,a.code_no codeno, \n" +
    	  "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,'' c_grade , c.i_grade,'' w_grade,'' y_grade,'' j_grade, '' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade,'' s_grade ,' ' bodysize, hw_configure  \n" +
    	  "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_pbc"+table+" c \n" +
    	  "where a.sid = ? \n" +
    	  "and trim(ws_route_add4) is not null and ws_route_add4 != 'NA' \n" +
    	  "and a.ws_route_add4 = b.route_name \n" +
    	  "and a.sid = c.sid \n" +
    	  "and a.mask_option = c.backend_option \n" +
    	  "and b.test_mode = c.test_type \n" +
    	  "and a.tag != 2 \n" +
         // "and c.site = 'TEST1' \n" +
       // 20170516, add for subsitution route        
       	  "union \n" +
          "select distinct \n" +
          "'Sub.' route_type, 7 route_type_seq,a.product_body,'MX' brand,a.version,a.body_version,a.mask_option maskbeoption,a.mask_option_rev,a.ws_route,e.map_route ws_route_add,'' packagecode, '' packagetype, '' pincount,a.code_no codeno, \n" +
          "b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' actualprogramname,'' c_grade, '' i_grade,'' w_grade,'' y_grade, '' j_grade, '' k_grade,'' l_grade,'' n_grade,'' b_grade,'' e_grade,'' s_grade,' ' bodysize, hw_configure  \n" +
          "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_pbc"+table+" c, tf_main_route_xrom"+table+" e \n" +
          "where a.sid = ? \n" +
          "and trim(e.map_route) is not null and e.map_route != 'NA' \n" +
          "and e.map_route = b.route_name \n" +
          "and a.sid = c.sid \n" +
          "and a.sid = e.sid \n" +
       //	"and c.brand = ' ' " +
       	  "and a.mask_option = c.backend_option \n" +
       	  "and b.test_mode = c.test_type \n" +
       	  "and e.route_type = 0 \n" +
       	  "and a.ws_route = e.main_route \n" +
       	  "and a.tag != 2 \n" +
       	  //"and c.site = 'TEST1' \n" +
          
          "order by product_body,body_version,maskbeoption,mask_option_rev,ws_route,route_type_seq,ws_route_add,testmode \n";

    TDSLogger.println(sql.toString());
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1,String.valueOf(fm.getSid()));
    ps.setString(2,String.valueOf(fm.getSid()));
    ps.setString(3,String.valueOf(fm.getSid()));
    ps.setString(4,String.valueOf(fm.getSid()));
    ps.setString(5,String.valueOf(fm.getSid()));
    ps.setString(6,String.valueOf(fm.getSid()));
	ps.setString(7,String.valueOf(fm.getSid()));
    ps.setString(8,String.valueOf(fm.getSid()));
    ps.setString(9,String.valueOf(fm.getSid()));
    ps.setString(10,String.valueOf(fm.getSid()));
    ps.setString(11,String.valueOf(fm.getSid()));
    ps.setString(12,String.valueOf(fm.getSid()));
    ps.setString(13,String.valueOf(fm.getSid()));
    ps.setString(14,String.valueOf(fm.getSid()));
    ResultSet rs = ps.executeQuery();


      String GroupKey = "";
      
      while(rs.next()){
          GroupKey = "";
          String routename = "";
          GroupKey = getGroupKey(product_type, rs.getString("product_body"), rs.getString("brand"), rs.getString("maskbeoption"), rs.getString("mask_option_rev"), rs.getString("body_version"),
    	             null, null, "", rs.getString("ws_route"), rs.getString("ws_route_add"), "", 0);
          String updatetime = getLogTime(fm.getSid());
          if (rs.getString("route_type").equals("Main")) {
          	routename = rs.getString("ws_route");
          }else {
        	routename = rs.getString("ws_route_add");
          }
          /*String temperature = " ";
            if (rs.getString("temperature") != null){
          	temperature = rs.getString("temperature");
          	if (!temperature.equals("ROOM TEMP") && !temperature.equals(""))
          		temperature = temperature + "℃";
          }*/
          boolean result = CATRoutePgmAll_insert(conn, GroupKey, rs.getString("product_body"), rs.getString("brand"), 
        		  rs.getString("version"), status, updatetime, rs.getString("route_type"), 
        		  routename, rs.getString("testmode"), rs.getString("maskbeoption"), rs.getString("packagecode"), 
        		  rs.getString("packagetype"), rs.getString("pincount"), rs.getString("codeno"), rs.getString("c_grade"), 
        		  rs.getString("i_grade"),rs.getString("w_grade"),rs.getString("y_grade"),rs.getString("j_grade"),rs.getString("k_grade"),rs.getString("l_grade"),rs.getString("n_grade"),rs.getString("b_grade"),rs.getString("e_grade"), rs.getString("s_grade"), rs.getString("bodysize"), rs.getString("testertype"), 
        		  rs.getString("pgm_id"), rs.getString("programname"), rs.getString("actualprogramname"), product_type, rs.getString("hw_configure"), rs.getString("site"));
          if(result == false)
        	  return false;
        
      }
      ps.clearParameters();
      ps.close();
      rs.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    } finally {
    }
    return true;
  }
  
//select NVM FT 最新版或會簽中 TEST1 之 Product Group vs Route data
//insert data to CAT_Route_Pgm
  public static boolean insert_cat_route_pgm_all_ft_NVM(    ProTestRouteBeanAF fm,
                                          String table,
                                         String product_type,
                                         String status,
                                         Connection conn) {
    try {

    	StringBuffer sql = new StringBuffer();
        sql.append("select distinct ");
        sql.append("'Main' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize ,c.actual_file actualprogramname, hw_configure   ");
        sql.append("from tf_bom_route"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
        sql.append("and a.ft_route = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 "); 
       // sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add1' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure  ");
        sql.append("from tf_bom_route"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' ");
        sql.append("and a.ft_route_add = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
       // sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add2' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add2 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure   ");
        sql.append("from tf_bom_route"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' ");
        sql.append("and a.ft_route_add2 = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
        //sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add3' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add3 ft_route_add ,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure   ");
        sql.append("from tf_bom_route"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add3) is not null and ft_route_add3 != 'NA' ");
        sql.append("and a.ft_route_add3 = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
        //sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Main' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize,c.actual_file actualprogramname, hw_configure  ");
        sql.append("from tf_bom_route"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
        sql.append("and a.ft_route = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
        //sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add1' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure  ");
        sql.append("from tf_bom_route"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' ");
        sql.append("and a.ft_route_add = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
       // sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add2' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add2 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure  ");
        sql.append("from tf_bom_route"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' ");
        sql.append("and a.ft_route_add2 = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
       // sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add3' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add3 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure ");
        sql.append("from tf_bom_route"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add3) is not null and ft_route_add3 != 'NA' ");
        sql.append("and a.ft_route_add3 = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
        //sql.append("and c.site = 'TEST1' ");
        sql.append("order by product_body,maskbeoption,packagecode,pincount,ft_route,ft_route_add,route_type desc,testmode ");
        TDSLogger.println(sql);
        TDSLogger.println("parameter = "+fm.getSid() + "," + fm.getSid() + "," +
        		fm.getSid() + "," + fm.getSid() );
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ps.setString(1,String.valueOf(fm.getSid()));
      ps.setString(2,String.valueOf(fm.getSid()));
      ps.setString(3,String.valueOf(fm.getSid()));
      ps.setString(4,String.valueOf(fm.getSid()));
      ps.setString(5,String.valueOf(fm.getSid()));
      ps.setString(6,String.valueOf(fm.getSid()));
      ps.setString(7,String.valueOf(fm.getSid()));
      ps.setString(8,String.valueOf(fm.getSid()));
      ResultSet rs = ps.executeQuery();

      String GroupKey = "";
      
      while(rs.next()){
          GroupKey = "";
          String routename = "";
          GroupKey = getGroupKey(product_type, rs.getString("product_body"), rs.getString("brand"), rs.getString("maskbeoption"),"","",
                  rs.getString("packagecode"), rs.getString("pincount"), rs.getString("fg_with_code"), rs.getString("ft_route"), rs.getString("ft_route_add"), null, 1);
          String updatetime = getLogTime(fm.getSid());
          if (rs.getString("route_type").equals("Main")) {
        	  routename = rs.getString("ft_route");
      	} else {
      		  routename = rs.getString("ft_route_add");
      	}
          /*String temperature = " ";
            if (rs.getString("temperature") != null){
          	temperature = rs.getString("temperature");
          	if (!temperature.equals("ROOM TEMP") && !temperature.equals(""))
          		temperature = temperature + "℃";
          }*/
          boolean result = CATRoutePgmAll_insert(conn, GroupKey, rs.getString("product_body"), rs.getString("brand"), 
        		  rs.getString("version"), status, updatetime, rs.getString("route_type"), 
        		  routename, rs.getString("testmode"), rs.getString("maskbeoption"), rs.getString("packagecode"), 
        		  rs.getString("packagetype"), rs.getString("pincount"), rs.getString("codeno"), rs.getString("c_grade"), 
        		  rs.getString("i_grade"), rs.getString("w_grade"), rs.getString("y_grade"),rs.getString("j_grade"), rs.getString("k_grade"), rs.getString("l_grade"), rs.getString("n_grade"), rs.getString("b_grade"), rs.getString("e_grade"), rs.getString("s_grade"), rs.getString("bodysize"), rs.getString("testertype"), 
        		  rs.getString("pgm_id"), rs.getString("programname"), rs.getString("actualprogramname"), product_type,rs.getString("hw_configure"),rs.getString("site") );
          if(result == false)
        	  return false; 
        
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if(ps != null)
    	  ps = null;
      if(rs != null)
    	  rs = null;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    } finally {
    }
    return true;
  }
  
//select NVM FT MCP最新版或會簽中 TEST1 之 Product Group vs Route data
//insert data to CAT_Route_Pgm
  public static boolean insert_cat_route_pgm_all_ft_MCP_NVM(    ProTestRouteBeanAF fm,
                                          String table,
                                         String product_type,
                                         String status,
                                         Connection conn) {
    try {

    	StringBuffer sql = new StringBuffer();
        sql.append("select distinct ");
        sql.append("'Main' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize ,c.actual_file actualprogramname, hw_configure ");
        sql.append("from tf_bom_route_mcp"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
        sql.append("and a.ft_route = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 "); 
        //sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add1' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure ");
        sql.append("from tf_bom_route_mcp"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' ");
        sql.append("and a.ft_route_add = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
       // sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add2' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add2 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure ");
        sql.append("from tf_bom_route_mcp"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' ");
        sql.append("and a.ft_route_add2 = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
        //sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add3' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add3 ft_route_add ,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure ");
        sql.append("from tf_bom_route_mcp"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add3) is not null and ft_route_add3 != 'NA' ");
        sql.append("and a.ft_route_add3 = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
        //sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Main' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize,c.actual_file actualprogramname, hw_configure ");
        sql.append("from tf_bom_route_mcp"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
        sql.append("and a.ft_route = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
        //sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add1' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure ");
        sql.append("from tf_bom_route_mcp"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' ");
        sql.append("and a.ft_route_add = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
       // sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add2' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add2 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure ");
        sql.append("from tf_bom_route_mcp"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' ");
        sql.append("and a.ft_route_add2 = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
        //sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add3' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add3 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure ");
        sql.append("from tf_bom_route_mcp"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add3) is not null and ft_route_add3 != 'NA' ");
        sql.append("and a.ft_route_add3 = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_type = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
        //sql.append("and c.site = 'TEST1' ");
        sql.append("order by product_body,maskbeoption,packagecode,pincount,ft_route,ft_route_add,route_type desc,testmode ");
        TDSLogger.println(sql);
        TDSLogger.println("parameter = "+fm.getSid() + "," + fm.getSid() + "," +
        		fm.getSid() + "," + fm.getSid() );
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ps.setString(1,String.valueOf(fm.getSid()));
      ps.setString(2,String.valueOf(fm.getSid()));
      ps.setString(3,String.valueOf(fm.getSid()));
      ps.setString(4,String.valueOf(fm.getSid()));
      ps.setString(5,String.valueOf(fm.getSid()));
      ps.setString(6,String.valueOf(fm.getSid()));
      ps.setString(7,String.valueOf(fm.getSid()));
      ps.setString(8,String.valueOf(fm.getSid()));
      ResultSet rs = ps.executeQuery();

      String GroupKey = "";
      
      while(rs.next()){
          GroupKey = "";
          String routename = "";
          GroupKey = getGroupKey(product_type, rs.getString("product_body"), rs.getString("brand"), rs.getString("maskbeoption"),"","",
                  rs.getString("packagecode"), rs.getString("pincount"), rs.getString("fg_with_code"), rs.getString("ft_route"), rs.getString("ft_route_add"), null, 1);
          String updatetime = getLogTime(fm.getSid());
          if (rs.getString("route_type").equals("Main")) {
        	  routename = rs.getString("ft_route");
      	} else {
      		  routename = rs.getString("ft_route_add");
      	}
          /*String temperature = " ";
            if (rs.getString("temperature") != null){
          	temperature = rs.getString("temperature");
          	if (!temperature.equals("ROOM TEMP") && !temperature.equals(""))
          		temperature = temperature + "℃";
          }*/
          boolean result = CATRoutePgmAll_insert(conn, GroupKey, rs.getString("product_body"), rs.getString("brand"), 
        		  rs.getString("version"), status, updatetime, rs.getString("route_type"), 
        		  routename, rs.getString("testmode"), rs.getString("maskbeoption"), rs.getString("packagecode"), 
        		  rs.getString("packagetype"), rs.getString("pincount"), rs.getString("codeno"), rs.getString("c_grade"), 
        		  rs.getString("i_grade"), rs.getString("w_grade"), rs.getString("y_grade"),rs.getString("j_grade"), rs.getString("k_grade"), rs.getString("l_grade"), rs.getString("n_grade"), rs.getString("b_grade"), rs.getString("e_grade"), rs.getString("s_grade"), rs.getString("bodysize"), rs.getString("testertype"), 
        		  rs.getString("pgm_id"), rs.getString("programname"), rs.getString("actualprogramname"), product_type,rs.getString("hw_configure"),rs.getString("site") );
          if(result == false)
        	  return false; 
        
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if(ps != null)
    	  ps = null;
      if(rs != null)
    	  rs = null;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    } finally {
    }
    return true;
  }
//select MROM FT 最新版或會簽中 TEST1 之 Product Group vs Route data
//insert data to CAT_Route_Pgm
  public static boolean insert_cat_route_pgm_all_ft_MROM(    ProTestRouteBeanAF fm,
                                          String table,
                                         String product_type,
                                         String status,
                                         Connection conn) {
    try {
    	
    	StringBuffer sql = new StringBuffer();
     	  sql.append("select distinct ");  // ft_route from ft data
     	  sql.append("'Main' route_type,a.product_body,'MX' brand,a.version,d.prm2_code packagecode,c.package_type packagetype,c.pin_count pincount,'' codeno, a.mask_option maskbeoption,");
     	  sql.append("a.ft_route,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,c.actual_file actualprogramname,  ");
     	  //sql.append("a.ft_route_add1,a.ft_route_add2 ");
     	 sql.append("'' ft_route_add1,'' ft_route_add2, hw_configure  ");
     	  sql.append("from tf_bom_route_mrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d ");
     	  sql.append("where a.sid = ? ");
     	  sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
     	  sql.append("and a.ft_route = b.route_name ");
     	  sql.append("and a.sid = c.sid ");
     	  sql.append("and a.mask_option = c.backend_option ");
     	  sql.append("and b.test_mode = c.test_type ");
     	  sql.append("and d.package_type = c.package_type ");
     	  sql.append("and a.tag != 2 ");
     	  //sql.append("and c.site = 'TEST1' ");
     	  sql.append("union ");
     	  sql.append("select distinct ");  // ft_route from pbc data
     	  sql.append("'Main' route_type,a.product_body,'MX' brand,a.version,d.prm2_code packagecode,c.package_type packagetype,c.pin_count pincount,'' codeno, a.mask_option maskbeoption, ");
     	  sql.append("a.ft_route,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,c.actual_file actualprogramname, ");
     	  //sql.append("a.ft_route_add1, a.ft_route_add2 ");
     	 sql.append("'' ft_route_add1, '' ft_route_add2, hw_configure  ");
     	  sql.append("from tf_bom_route_mrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d ");
     	  sql.append("where a.sid = ? ");
     	  sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
     	  sql.append("and a.ft_route = b.route_name ");
     	  sql.append("and a.sid = c.sid ");
     	  sql.append("and a.mask_option = c.backend_option ");
     	  sql.append("and b.test_mode = c.test_type ");
     	  sql.append("and d.package_type = c.package_type ");
     	  sql.append("and a.tag != 2 ");
     	  //sql.append("and c.site = 'TEST1' ");
     	  sql.append("union ");
     	  sql.append("select distinct ");  // ft_route_add1 from ft data
     	  sql.append("'Add2' route_type,a.product_body,'MX' brand,a.version,d.prm2_code packagecode,c.package_type packagetype,c.pin_count pincount,'' codeno, a.mask_option maskbeoption,");
     	  sql.append("a.ft_route,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,c.actual_file actualprogramname, ");
     	  sql.append("a.ft_route_add1, a.ft_route_add2, hw_configure  ");
     	  sql.append("from tf_bom_route_mrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d ");
     	  sql.append("where a.sid = ? ");
     	  sql.append("and trim(ft_route_add1) is not null and ft_route_add1 != 'NA' ");
     	  sql.append("and a.ft_route_add1 = b.route_name ");
     	  sql.append("and a.sid = c.sid ");
     	  sql.append("and a.mask_option = c.backend_option ");
     	  sql.append("and b.test_mode = c.test_type ");
     	  sql.append("and d.package_type = c.package_type ");
     	  sql.append("and a.tag != 2 ");
     	 //sql.append("and c.site = 'TEST1' ");
     	  sql.append("union ");
     	  sql.append("select distinct ");  // ft_route_add1 from pbc data
     	  sql.append("'Add2' route_type,a.product_body,'MX' brand,a.version,d.prm2_code packagecode,c.package_type packagetype,c.pin_count pincount,'' codeno, a.mask_option maskbeoption,");
     	  sql.append("a.ft_route,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,c.actual_file actualprogramname, ");
     	  sql.append("a.ft_route_add1, a.ft_route_add2, hw_configure  ");
     	  sql.append("from tf_bom_route_mrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d ");
     	  sql.append("where a.sid = ? ");
     	  sql.append("and trim(ft_route_add1) is not null and ft_route_add1 != 'NA' ");
     	  sql.append("and a.ft_route_add1 = b.route_name ");
     	  sql.append("and a.sid = c.sid ");
     	  sql.append("and a.mask_option = c.backend_option ");
     	  sql.append("and b.test_mode = c.test_type ");
     	  sql.append("and d.package_type = c.package_type ");
     	  sql.append("and a.tag != 2 ");
     	  //sql.append("and c.site = 'TEST1' ");
     	  sql.append("union ");
     	  sql.append("select distinct "); // ft_route_add2 from ft data
     	  sql.append("'Add1' route_type,a.product_body,'MX' brand,a.version,d.prm2_code packagecode,c.package_type packagetype,c.pin_count pincount,'' codeno, a.mask_option maskbeoption,");
     	  sql.append("a.ft_route,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,c.actual_file actualprogramname, ");
     	  sql.append("a.ft_route_add1, a.ft_route_add2, hw_configure ");
     	  sql.append("from tf_bom_route_mrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d ");
     	  sql.append("where a.sid = ? ");
     	  sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' ");
     	  sql.append("and a.ft_route_add2 = b.route_name ");
     	  sql.append("and a.sid = c.sid ");
     	  sql.append("and a.mask_option = c.backend_option ");
     	  sql.append("and b.test_mode = c.test_type ");
     	  sql.append("and d.package_type = c.package_type ");
     	  sql.append("and a.tag != 2 ");
     	  //sql.append("and c.site = 'TEST1' ");
     	  sql.append("union ");
     	  sql.append("select distinct "); //ft_route_add2 from pbc data
     	  sql.append("'Add1' route_type,a.product_body,'MX' brand,a.version,d.prm2_code packagecode,c.package_type packagetype,c.pin_count pincount,'' codeno, a.mask_option maskbeoption,");
     	  sql.append("a.ft_route,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,c.actual_file actualprogramname, ");
     	  sql.append("a.ft_route_add1, a.ft_route_add2, hw_configure  ");
     	  sql.append("from tf_bom_route_mrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d ");
     	  sql.append("where a.sid = ? ");
     	  sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' ");
     	  sql.append("and a.ft_route_add2 = b.route_name ");
     	  sql.append("and a.sid = c.sid ");
     	  sql.append("and a.mask_option = c.backend_option ");
     	  sql.append("and b.test_mode = c.test_type ");
     	  sql.append("and d.package_type = c.package_type ");
     	  sql.append("and a.tag != 2 ");
     	  //sql.append("and c.site = 'TEST1' ");
    	  sql.append("order by product_body,maskbeoption,packagecode,pincount,ft_route,ft_route_add1,ft_route_add2,route_type desc,testmode ");

      TDSLogger.println(sql);	  
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ps.setString(1,String.valueOf(fm.getSid()));
      ps.setString(2,String.valueOf(fm.getSid()));
      ps.setString(3,String.valueOf(fm.getSid()));
      ps.setString(4,String.valueOf(fm.getSid()));
      ps.setString(5,String.valueOf(fm.getSid()));
      ps.setString(6,String.valueOf(fm.getSid()));
      ResultSet rs = ps.executeQuery();

      String GroupKey = "";
      
      while(rs.next()){
          GroupKey = "";
          String routename = "";
          GroupKey = getGroupKey(product_type, rs.getString("product_body"), rs.getString("brand"), rs.getString("maskbeoption"),"","",
                  rs.getString("packagecode"), rs.getString("pincount"),"", rs.getString("ft_route"), "", null, 1);
          String updatetime = getLogTime(fm.getSid());

          if (rs.getString("route_type").equals("Main")) {
        	  routename = rs.getString("ft_route");
          } else if (rs.getString("route_type").equals("Add2")) {
        	  routename = rs.getString("ft_route_add1");
          } else if (rs.getString("route_type").equals("Add1")) {
      		  routename = rs.getString("ft_route_add2");
          }
          /*String temperature = " ";
            if (rs.getString("temperature") != null){
          	temperature = rs.getString("temperature");
          	if (!temperature.equals("ROOM TEMP") && !temperature.equals(""))
          		temperature = temperature + "℃";
          }*/
          boolean result = CATRoutePgmAll_insert(conn, GroupKey, rs.getString("product_body"), rs.getString("brand"), 
        		  rs.getString("version"), status, updatetime, rs.getString("route_type"), 
        		  routename, rs.getString("testmode"), rs.getString("maskbeoption"), rs.getString("packagecode"), 
        		  rs.getString("packagetype"), rs.getString("pincount"), rs.getString("codeno"), rs.getString("c_grade"), 
        		  rs.getString("i_grade"),rs.getString("w_grade"),rs.getString("y_grade"), rs.getString("j_grade"),rs.getString("k_grade"),rs.getString("l_grade"),rs.getString("n_grade"),rs.getString("b_grade"),rs.getString("e_grade"), rs.getString("s_grade"), rs.getString("bodysize"), rs.getString("testertype"), 
        		  rs.getString("pgm_id"), rs.getString("programname"), rs.getString("actualprogramname"), product_type, rs.getString("hw_configure"), rs.getString("site"));
          if(result == false)
        	  return false; 
        
      }
      ps.clearParameters();
      ps.close();
      rs.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    } finally {
    }
    return true;
  }
  
//select XROM FT 最新版或會簽中 TEST1 之 Product Group vs Route data
//insert data to CAT_Route_Pgm
  public static boolean insert_cat_route_pgm_all_ft_XROM(    ProTestRouteBeanAF fm,
                                          String table,
                                         String product_type,
                                         String status,
                                         Connection conn) {
    try {

    	StringBuffer sql = new StringBuffer();

    	sql.append("select distinct \n");
    	sql.append("'Main' route_type,1 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	//sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade, '' s_grade,c.body_size bodysize,c.tf_comment,a.ft_comment,c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x \n");
    	sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade, '' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,a.ft_comment,c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route) is not null and ft_route != 'NA' \n");
    	sql.append("and a.ft_route = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
        //sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add1' route_type,2 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment, '' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' \n");
    	sql.append("and a.ft_route_add = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add2' route_type,3 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,a.ft_route_add1 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,'' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add1) is not null and ft_route_add1 != 'NA' \n");
    	sql.append("and a.ft_route_add1 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add3' route_type,4 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,a.ft_route_add2 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,'' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' \n");
    	sql.append("and a.ft_route_add2 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add4' route_type,5 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,a.ft_route_add3 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,'' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add3) is not null and ft_route_add3 != 'NA' \n");
    	sql.append("and a.ft_route_add3 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add5' route_type,6 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,a.ft_route_add4 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,'' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add4) is not null and ft_route_add4 != 'NA' \n");
    	sql.append("and a.ft_route_add4 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add6' route_type,7 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,a.ft_route_add5 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,'' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add5) is not null and ft_route_add5 != 'NA' \n");
    	sql.append("and a.ft_route_add5 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Sub.' route_type,8 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,e.map_route ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,'' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d, tf_main_route_xrom"+table+" e \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(e.map_route) is not null and e.map_route != 'NA' \n");
    	sql.append("and e.map_route = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	sql.append("and a.sid = e.sid \n");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	sql.append("and e.route_type = 0 \n");
    	sql.append("and a.ft_route = e.main_route \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Rwk.' route_type,9 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,e.map_route ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,'' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d, tf_main_route_xrom"+table+" e \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(e.map_route) is not null and e.map_route != 'NA' \n");
    	sql.append("and e.map_route = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	sql.append("and a.sid = e.sid \n");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	sql.append("and e.route_type = 1 \n");
    	sql.append("and a.ft_route = e.main_route \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Main' route_type,1 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	//sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,'' s_grade,c.body_size bodysize,c.tf_comment,a.ft_comment ft_comment,c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x \n");
    	sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,a.ft_comment ft_comment,c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route) is not null and ft_route != 'NA' \n");
    	sql.append("and a.ft_route = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	//sql.append("and c.site = 'TEST1'  \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add1' route_type,2 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,'' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' \n");
    	sql.append("and a.ft_route_add = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add2' route_type,3 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,a.ft_route_add1 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment, '' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add1) is not null and ft_route_add1 != 'NA' \n");
    	sql.append("and a.ft_route_add1 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add3' route_type,4 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,a.ft_route_add2 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,'' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' \n");
    	sql.append("and a.ft_route_add2 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add4' route_type,5 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,a.ft_route_add3 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,'' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add3) is not null and ft_route_add3 != 'NA' \n");
    	sql.append("and a.ft_route_add3 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add5' route_type,6 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,a.ft_route_add4 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,'' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add4) is not null and ft_route_add4 != 'NA' \n");
    	sql.append("and a.ft_route_add4 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add6' route_type,7 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,a.ft_route_add5 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,'' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add5) is not null and ft_route_add5 != 'NA' \n");
    	sql.append("and a.ft_route_add5 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Sub.' route_type,8 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,e.map_route ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,'' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d, tf_main_route_xrom"+table+" e \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(e.map_route) is not null and e.map_route != 'NA' \n");
    	sql.append("and e.map_route = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	sql.append("and a.sid = e.sid \n");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	sql.append("and e.route_type = 0 \n");
    	sql.append("and a.ft_route = e.main_route \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Rwk.' route_type,9 route_type_seq,a.product_body,'MX' brand,a.version,a.package_code packagecode,c.package_type packagetype,a.pin_count pincount,c.backend_option,a.body_version,a.code_no codeno, \n");
    	sql.append("a.ft_route,e.map_route ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,'' i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,'' s_grade,nvl(c.body_size,' ') bodysize,c.tf_comment,'' ft_comment, c.actual_file actualprogramname, a.mask_option maskbeoption, a.mask_option_rev, a.route_type route_type_x, hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d, tf_main_route_xrom"+table+" e \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(e.map_route) is not null and e.map_route != 'NA' \n");
    	sql.append("and e.map_route = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	sql.append("and a.sid = e.sid \n");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	sql.append("and e.route_type = 1 \n");
    	sql.append("and a.ft_route = e.main_route \n");
    	//sql.append("and c.site = 'TEST1' \n");
    	sql.append("order by product_body,body_version,backend_option,mask_option_rev,packagecode,pincount,ft_route,route_type_seq,ft_route_add,testmode \n");
    	TDSLogger.println(sql.toString());
    	PreparedStatement ps = conn.prepareStatement(sql.toString());

    	int i=1;
		ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	
    	ResultSet rs = ps.executeQuery();

      String GroupKey = "";
      
      while(rs.next()){
          GroupKey = "";
          String routename = "";
          GroupKey = getGroupKey(product_type, rs.getString("product_body"), rs.getString("brand"), rs.getString("maskbeoption"),rs.getString("mask_option_rev"),rs.getString("body_version"),
                  rs.getString("packagecode"), rs.getString("pincount"), "", rs.getString("ft_route"), rs.getString("ft_route_add"), null, 1);
          String updatetime = getLogTime(fm.getSid());
          if (rs.getString("route_type").equals("Main")) {
        	  routename = rs.getString("ft_route");
      	} else {
      		  routename = rs.getString("ft_route_add");
      	}
          /*String temperature = " ";
            if (rs.getString("temperature") != null){
          	temperature = rs.getString("temperature");
          	if (!temperature.equals("ROOM TEMP") && !temperature.equals(""))
          		temperature = temperature + "℃";
          }*/
          boolean result = CATRoutePgmAll_insert(conn, GroupKey, rs.getString("product_body"), rs.getString("brand"), 
        		  rs.getString("version"), status, updatetime, rs.getString("route_type"), 
        		  routename, rs.getString("testmode"), rs.getString("maskbeoption"), rs.getString("packagecode"), 
        		  rs.getString("packagetype"), rs.getString("pincount"), rs.getString("codeno"), rs.getString("c_grade"), 
        		  rs.getString("i_grade"),rs.getString("w_grade"),rs.getString("y_grade"),rs.getString("j_grade"),rs.getString("k_grade"),rs.getString("l_grade"),rs.getString("n_grade"),rs.getString("b_grade"),rs.getString("e_grade"), rs.getString("s_grade"), rs.getString("bodysize"), rs.getString("testertype"), 
        		  rs.getString("pgm_id"), rs.getString("programname"), rs.getString("actualprogramname"), product_type, rs.getString("hw_configure"), rs.getString("site"));
          if(result == false)
        	  return false; 
        
      }
      ps.clearParameters();
      ps.close();
      rs.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    } finally {
    }
    return true;
  }
  
  
//Write CAT ROUTE temp Data
  // MXICFlag1 ('Y','N', 'NA')
  // MXICflag 設定１：針對每一個 ProductGroupKey + Step_Name
  //  Step1.若為 SORT* 或 FT* 至 CAT_ROUTE_PGM 中查詢是否有資料，若有，則 "廠內release" flag (以下稱 MXICFlag) = 'Y'，否則為 'N'。
  //  Step2.若為 TQAE*，則依相對應的 FT* 至 CAT_ROUTE_PGM 中查詢是否有資料，若有，則 MXICflag = 'Y'，否則為 'N'。
  //  Step3.AVI / INK 站，MXICflag 為 'Y'。
  //  Step4.Bumping 站，MXICflag 為 'N'。
  //  Step5.其它站，則記錄此站的 MXICflag 為 'NA'。
  public static boolean insert_cat_route_all_tmp1(    ProTestRouteBeanAF fm,
		                                 String table,
		                                 String product_type,
                                         String status,
                                         Connection conn) {

    try {

      String sql = " select distinct a.productgroupkey, a.productbody, a.brand, a.version, a.status, a.maskbeoption, " +
                   " a.packagecode, a.packagetype, a.pincount, " +
                   " to_char(a.updatetime, 'yyyy-mm-dd hh24:mi:ss') updatetime , a.routetype, a.routename, b.step_seq testseq, b.step_name stepname, b.temperature, b.test_time testtime, b.time_unit testunit " + 
                   " from cat_route_pgm_all a, tf_product_route" + table + " b " +
                   " where a.productbody = ? " +
                   " and a.brand = ? " +
                   " and a.version = ? " +
                   " and a.status = ? " +
                   " and a.product_type = ? " +
                   " and a.productbody = b.product_body " +
                   " and a.brand = b.brand " +
                   " and a.version = b.version " +
                   " and a.routename = b.route_name " +
                   " and (b.step_name like 'UV%' or b.step_name like 'SORT%'  " +
                   " or b.step_name like 'BAKE%' or b.step_name like 'INK%' " +
                   " or b.step_name like 'AVI' or b.step_name like 'FT%'" +
                   " or b.step_name like 'TQAE%' or b.step_name = 'Bumping')" +
                   " order by a.productgroupkey, a.routename, testseq ";
      
      TDSLogger.println(sql);
      TDSLogger.println("parameter="+
    		  fm.getProductbody() + "," +
    		  fm.getBrand() + "," +
    		  fm.getVersion() + "," +
    		  status + "," +
    		  product_type);
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,String.valueOf(fm.getProductbody()));
      ps.setString(2,String.valueOf(fm.getBrand()));
      ps.setString(3,String.valueOf(fm.getVersion()));
      ps.setString(4,String.valueOf(status));
      ps.setString(5,String.valueOf(product_type));
      ResultSet rs = ps.executeQuery();

      
      while(rs.next()){
    	  String MXICFlag1 = ""; 
    	  String stepname = rs.getString("stepname");
    	  if(stepname.startsWith("TQAE") ){
    		  stepname = stepname.replaceAll("TQAE", "FT");
    	  }
    	  if(stepname.startsWith("SORT") ){
    		  stepname = stepname.replaceAll("SORT", "S");
    	  }
    	  if(stepname.startsWith("S") || stepname.startsWith("FT") ) {
        	  MXICFlag1 = getMXICFlag1(rs.getString("productgroupkey") , rs.getString("productbody"), 
            		      rs.getString("brand"), rs.getString("version") ,rs.getString("status"), 
            		      rs.getString("routename") , stepname , 
            		      rs.getString("maskbeoption") , rs.getString("packagecode") ,
            		      rs.getString("packagetype") , rs.getString("pincount") );
    	  }else if("AVI".equals(stepname) || "INK".equals(stepname)){
    		  MXICFlag1 = "Y";
    	  }else if("Bumping".equals(stepname)){
    		  MXICFlag1 = "N";	  
    	  }else{
    		  MXICFlag1 = "NA";
    	  }
          
          boolean result = CATRouteAllTmp_insert(conn, rs.getString("productgroupkey"), rs.getString("productbody"), 
        		  rs.getString("brand"), rs.getString("version"), rs.getString("status"), rs.getString("updatetime"),
        		  rs.getString("routetype"), rs.getString("routename"), rs.getString("testseq"), rs.getString("stepname"), 
        		  rs.getString("temperature"), rs.getString("testtime"), rs.getString("testunit"), MXICFlag1, "1");
          boolean result2 = CATRouteAllTmp_insert(conn, rs.getString("productgroupkey"), rs.getString("productbody"), 
        		  rs.getString("brand"), rs.getString("version"), rs.getString("status"), rs.getString("updatetime"),
        		  rs.getString("routetype"), rs.getString("routename"), rs.getString("testseq"), rs.getString("stepname"), 
        		  rs.getString("temperature"), rs.getString("testtime"), rs.getString("testunit"), MXICFlag1, "2");
          if(result == false || result2 == false)
        	  return false;
        
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if(ps != null)
    	  ps = null;
      if(rs != null)
    	  rs = null;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    } finally {
    }
    return true;
  }
//Write CAT ROUTE temp Data
  //MXICflag 設定２：取出 ProductGroupKey + Route_Name + Step_Name order by Step_Seq，by ProductGroupKey + Route_Name  針對每一個 Step_Name
  // Step1.若MXICflag為 ‘NA’，則MXICflag設為下一筆的 MXICflag；若本筆為最後一筆，則MXICflag設為前一筆的MXICflag。
  // Step2.若還有下一筆，取出下一筆資料，至 Step1
  // Step3.若本次過程有MXICflag為'NA' 但沒有被update，至 Step1，否則進行下一步。
  public static boolean insert_cat_route_all_tmp2(    ProTestRouteBeanAF fm,
		                                 String table,
                                         String product_type,
                                         String status,
                                         Connection conn) {

    try {

      String sql = " select productgroupkey, routetype, routename " +
                   " from cat_route_ALL_tmp  " +
                   " where productbody = ? " +
                   " and brand = ? " +
                   " and version = ? " +
                   " and status = ? " +
                   " and step = '2' " +
                   " group by productgroupkey, routetype, routename ";
      
      TDSLogger.println(sql);
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,String.valueOf(fm.getProductbody()));
      ps.setString(2,String.valueOf(fm.getBrand()));
      ps.setString(3,String.valueOf(fm.getVersion()));
      ps.setString(4,String.valueOf(status));
      ResultSet rs = ps.executeQuery();

      
      while(rs.next()){
    	while(chkMXICFlagYNNA(fm.getProductbody(), fm.getBrand(), fm.getVersion(), status, rs.getString("productgroupkey"), rs.getString("routetype"), rs.getString("routename")  )){
    	  String sql_1 = " select productgroupkey, productbody, brand, version, status, " +
          " routetype, routename, testseq, testmode, to_char(updatetime, 'yyyy-mm-dd hh24:mi:ss') updatetime, temperature, " +
          "  testtime, timeunit, flag, step  " + 
          " from cat_route_all_tmp  " +
          " where productbody = ? " +
          " and brand = ? " +
          " and version = ? " +
          " and status = ? " +
          " and productgroupkey = ? " +
          " and routetype = ? " +
          " and routename = ? " +
          " and step = '2' " +
          " order by testseq ";
    	  TDSLogger.println(sql_1);
    	  TDSLogger.println("parameter="+
    			  fm.getProductbody() + "," +
    			  fm.getBrand() + "," +
    			  fm.getVersion() + "," +
    			  status + "," +
    			  rs.getString("productgroupkey") + "," +
    			  rs.getString("routetype") + "," +
    			  rs.getString("routename") );
          PreparedStatement ps_1 = conn.prepareStatement(sql_1);
          ps_1.setString(1,String.valueOf(fm.getProductbody()));
          ps_1.setString(2,String.valueOf(fm.getBrand()));
          ps_1.setString(3,String.valueOf(fm.getVersion()));
          ps_1.setString(4,String.valueOf(status));
          ps_1.setString(5,String.valueOf(rs.getString("productgroupkey")));
          ps_1.setString(6,String.valueOf(rs.getString("routetype")));
          ps_1.setString(7,String.valueOf(rs.getString("routename")));
          ResultSet rs_1 = ps_1.executeQuery();
          HashMap Curupdate = null;
          HashMap TMPupdate = null;
          String MXICFlag2 = ""; 
          String BefMXICFlag2 = ""; 
          while(rs_1.next()){
        	  MXICFlag2 = ""; 
        	  Curupdate = new HashMap();
        	  Curupdate.put("productgroupkey", rs_1.getString("productgroupkey") );
        	  Curupdate.put("productbody", rs_1.getString("productbody") );
        	  Curupdate.put("brand", rs_1.getString("brand") );
        	  Curupdate.put("version", rs_1.getString("version") );
        	  Curupdate.put("status", rs_1.getString("status") );
        	  Curupdate.put("routetype", rs_1.getString("routetype") );
        	  Curupdate.put("routename", rs_1.getString("routename") );
        	  Curupdate.put("testseq", rs_1.getString("testseq") );
        	  Curupdate.put("testmode", rs_1.getString("testmode") );
        	  Curupdate.put("updatetime", rs_1.getString("updatetime") );
        	  Curupdate.put("temperature", rs_1.getString("temperature") );
        	  Curupdate.put("testtime", rs_1.getString("testtime") );
        	  Curupdate.put("timeunit", rs_1.getString("timeunit") );
        	  Curupdate.put("flag", rs_1.getString("flag") );
        	  Curupdate.put("step", rs_1.getString("step") );
    	      if("Y".equals(rs_1.getString("flag"))) {
    	    	  MXICFlag2 = rs_1.getString("testmode");
    	    	  //MXICFlag2 = MXICFlag2.replaceAll("TQAE", "FT");
    	      }else if("N".equals(rs_1.getString("flag"))) {
    	    	      if(rs_1.getString("testmode").startsWith("SORT"))
        	    	     MXICFlag2 = "SUBWS"; 
    	    	      else if(rs_1.getString("testmode").startsWith("FT") || rs_1.getString("testmode").startsWith("TQAE")){
    	    	    	 MXICFlag2 = "SUBFT";  
    	    	    	 if("XROM".equals(product_type)){
    	    	    		 MXICFlag2 = "SUBPB";  
    	    	    	 }	 
    	    	      }else if(rs_1.getString("testmode").startsWith("Bumping")){
    	    	    	  MXICFlag2 = "SUBBUMP"; 
    	    	      }
    	      }else if ("NA".equals(rs_1.getString("flag"))){
    	    	  TMPupdate = new HashMap();
    	    	  TMPupdate.put("productgroupkey", rs_1.getString("productgroupkey") );
    	    	  TMPupdate.put("productbody", rs_1.getString("productbody") );
    	    	  TMPupdate.put("brand", rs_1.getString("brand") );
    	    	  TMPupdate.put("version", rs_1.getString("version") );
    	    	  TMPupdate.put("status", rs_1.getString("status") );
    	    	  TMPupdate.put("routetype", rs_1.getString("routetype") );
    	          TMPupdate.put("routename", rs_1.getString("routename") );
    	          TMPupdate.put("testseq", rs_1.getString("testseq") );
    	          TMPupdate.put("testmode", rs_1.getString("testmode") );
    	          TMPupdate.put("updatetime", rs_1.getString("updatetime") );
    	          TMPupdate.put("temperature", rs_1.getString("temperature") );
    	          TMPupdate.put("testtime", rs_1.getString("testtime") );
    	          TMPupdate.put("timeunit", rs_1.getString("timeunit") );
    	          TMPupdate.put("step", rs_1.getString("step") );
    	    	  
    	      }else{
    	    	  MXICFlag2 = rs_1.getString("flag");
    	      }
    	      if (!"NA".equals(rs_1.getString("flag"))){
    	        //目前資料
    	    	boolean result = true;  
    	    	if(Curupdate!=null)  
    	    	   result = CATRouteAllTmp_update(conn, Curupdate, MXICFlag2);
    	        /*boolean result = CATRouteTmp_update(conn, rs_1.getString("productgroupkey"), rs_1.getString("productbody"), 
    	    	  rs_1.getString("brand"), rs_1.getString("version"), rs_1.getString("status"), rs_1.getDate("updatetime"),
    	    	  rs_1.getString("routename"), rs_1.getString("testseq"), rs_1.getString("testmode"), 
    	    	  rs_1.getString("temperature"), rs_1.getString("testtime"), rs_1.getString("testunit"), MXICFlag2, "2");*/
    	        //前一筆為NA的資料
    	    	boolean result2 = true;
    	    	if(TMPupdate!=null)
    	    	   result2 = CATRouteAllTmp_update(conn, TMPupdate, TMPupdate.get("testmode").toString());
    	        /*boolean result2 = CATRouteTmp_update(conn, TMPupdate.get("productgroupkey").toString(), TMPupdate.get("productbody").toString(), 
    	    		  TMPupdate.get("brand").toString(), TMPupdate.get("version").toString(), TMPupdate.get("status").toString(), (Date)TMPupdate.get("updatetime"),
    	    		  TMPupdate.get("routename").toString(), TMPupdate.get("testseq").toString(), TMPupdate.get("testmode").toString(), 
    	    		  TMPupdate.get("temperature").toString(),TMPupdate.get("testtime").toString(), TMPupdate.get("testunit").toString(), MXICFlag2, "2");*/
    	    	BefMXICFlag2 = MXICFlag2;
    	        Curupdate = null;
    	        TMPupdate = null;
    	        if(result == false || result2 == false)
    	        	  return false;
    	    }
          }  
    	  if(Curupdate != null && Curupdate.get("flag").equals("NA")){ //最後一筆為NA,設為前一筆資料,但在廠內(BAKE)則要顯示,EX: SORT1->BAKE1->SORT2->BAKE2
    		    boolean result = false;
    		    if(BefMXICFlag2.startsWith("SUB")){
    	    	  result = CATRouteAllTmp_update(conn, Curupdate, BefMXICFlag2);
                }else{
                  result = CATRouteAllTmp_update(conn, Curupdate, TMPupdate.get("testmode").toString());	
    		    }  
    	    	if(result == false )
    	       	  return false;
    		    
    	  }
    	  ps_1.clearParameters();
          ps_1.close();
          rs_1.close();
          if(ps_1 != null)
        	  ps_1 = null;
          if(rs_1 != null)
        	  rs_1 = null;
          
    	}
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if(ps != null)
    	  ps = null;
      if(rs != null)
    	  rs = null;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    } finally {
    }
    return true;
  }
  
//Write CAT ROUTE 
  //將 cat_route_tmp 整理後, 依序insert to CAT_ROUTE table
  //CAT_ROUTE 資料總規則如下 : 
  //  1.同一組 ProductGroupKey/RouteName/StepName 可能 release 多支程式，若其中有一支 release 在廠內，這組的程式就算有 release 在廠內。
  //  2.TQAE* 站是否 release 在廠內，須參照其對應的FT*站。
  //  3.AVI / INK 站點一律視為有 release 在廠內。
  //  4.Bumping (SUBBUMP) 站點一律視為有 release 在廠外。
  //  5.BAKE*/UV 站是否 release 在廠內，要看它下一點是否有 release 在廠內，如果它是最後一站，則看前一站是否有 release 在廠內。
  //  6.若有連續為相同之stepname, 則合併為一個
  //EX : SUBFT->SUBFT->SUBFT->BAKE->FT3->FT3->TQAE3
  //整理後為 :SUBFT->BAKE->FT3->TQAE3
  //步驟如下:
  //TestSeq 設定與資料輸出：再取出 ProductGroupKey + Route_Name + Step_Name + … order by Step_Seq，by 每一組 ProductGroupKey + Route_Name，TestSeq 起始為 1，針對每一個 Step_Name
  //Step1.若 MXICflag 為 'Y'，則寫出 TestSeq 及資料至 CAT_ROUTE table，TestSeq 加 1。
  //Step2.若 MXICflag 為 'N'，則  寫出 TestSeq 及資料至 CAT_ROUTE table，TestSeq 加 1，但
  //	    - 若為 XtraROM / FT產品，StepName 須輸出 SUBPB，否則
  //    	- 若為 FT產品，StepName 須輸出 SUBFT，否則
  //	    - 若為 WS 產品，StepName 須輸出 SUBWS
  //	    - 產品別可 check TF_PRODUCT.PRODUCT_TYPE
  //前進到下一個 MXICflag 為 'Y' 的 Step。
  //回到 Step1
  
  public static boolean insert_cat_route_all(    ProTestRouteBeanAF fm,
                                         String status,
                                         Connection conn) {

    try {

      String sql = " select productgroupkey, routetype, routename " +
                   " from cat_route_all_tmp  " +
                   " where productbody = ? " +
                   " and brand = ? " +
                   " and version = ? " +
                   " and status = ? " +
                   " and step = '2' " +
                   " group by productgroupkey, routetype, routename";
      
      TDSLogger.println(sql);
      TDSLogger.println("parameter="+
    		  fm.getProductbody() + "," +
    		  fm.getBrand() + "," +
    		  fm.getVersion() + "," +
    		  status);
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,String.valueOf(fm.getProductbody()));
      ps.setString(2,String.valueOf(fm.getBrand()));
      ps.setString(3,String.valueOf(fm.getVersion()));
      ps.setString(4,String.valueOf(status));
      ResultSet rs = ps.executeQuery();

      
      while(rs.next()){
    	  String sql_1 = " select productgroupkey, productbody, brand, version, status, " +
          " routetype, routename, testseq, testmode, to_char(updatetime, 'yyyy-mm-dd hh24:mi:ss') updatetime, temperature, " +
          "  testtime, timeunit, flag, step  " + 
          " from cat_route_all_tmp  " +
          " where productbody = ? " +
          " and brand = ? " +
          " and version = ? " +
          " and status = ? " +
          " and productgroupkey = ? " +
          " and routetype = ? " +
          " and routename = ? " +
          " and step = '2' " +
          " order by testseq ";
    	  TDSLogger.println(sql_1);
    	  TDSLogger.println("parameter="+
    			  fm.getProductbody() + "," +
    			  fm.getBrand() + "," +
    			  fm.getVersion() + "," +
    			  status + "," +
    			  rs.getString("productgroupkey") + "," +
    			  rs.getString("routetype") + "," +
    			  rs.getString("routename"));
          PreparedStatement ps_1 = conn.prepareStatement(sql_1);
          ps_1.setString(1,String.valueOf(fm.getProductbody()));
          ps_1.setString(2,String.valueOf(fm.getBrand()));
          ps_1.setString(3,String.valueOf(fm.getVersion()));
          ps_1.setString(4,String.valueOf(status));
          ps_1.setString(5,String.valueOf(rs.getString("productgroupkey")));
          ps_1.setString(6,String.valueOf(rs.getString("routetype")));
          ps_1.setString(7,String.valueOf(rs.getString("routename")));
          ResultSet rs_1 = ps_1.executeQuery();
          String TMPMXICFlag2 = ""; 
          int seq = 0;
          while(rs_1.next()){
        	  if(!TMPMXICFlag2.equals(rs_1.getString("flag"))) {
        		  seq ++;
        		  boolean result = CATRouteAll_insert(conn, rs_1.getString("productgroupkey"), rs_1.getString("productbody"), 
            	    	  rs_1.getString("brand"), rs_1.getString("version"), rs_1.getString("status"), rs_1.getString("updatetime"),
            	    	  rs_1.getString("routetype"), rs_1.getString("routename"), String.valueOf(seq), rs_1.getString("flag"), 
            	    	  rs_1.getString("temperature"), rs_1.getString("testtime"), rs_1.getString("timeunit"));
        		  TMPMXICFlag2 = rs_1.getString("flag");     
        		  if(result == false )
    	        	  return false;
    	      }
          }
          ps_1.clearParameters();
          ps_1.close();
          rs_1.close();
          if(ps_1 != null)
        	  ps_1 = null;
          if(rs_1 != null)
        	  rs_1 = null;
      }
      ps.clearParameters();
      ps.close();
      rs.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    } finally {
    }
    return true;
  }
  
//Write CAT PGM vs CAT ROUTE, CAT ROUTE PGM
  // if vendor is empty, then output all information
  // if textFilename is not "", 
  public static boolean insert_cat_pgm_all(    ProTestRouteBeanAF fm,
		                                 String table,
                                         String status,
                                         String product_type,
                                         Connection conn) {

    try {

      String sql = " insert into cat_pgm_all " +
    	           " select distinct a.productgroupkey, a.productbody, a.brand, a.version, a.status, " +
    	           " a.updatetime, a.routename, a.testseq, a.testmode, b.maskbeoption, " +
    	           " b.packagecode, b.packagetype, b.pincount, b.c_grade, b.i_grade, b.w_grade, b.y_grade, b.s_grade, " +
    	           " nvl(b.bodysize,' ') bodysize, b.testertype, b.pgmid, " +
                   " decode(substr(a.testmode,1,4),'TQAE', 'Q' ||substr(b.programname,2), b.programname) programname, hw_configure, b.j_grade, b.k_grade, b.l_grade, b.n_grade , b.b_grade , b.e_grade, b.site " +
                   " from cat_route_all a , cat_route_pgm_all b  " +
                   " where a.productbody = ? " +
                   " and a.brand = ? " +
                   " and a.version = ? " +
                   " and a.status = ? " +
                   " and b.product_type = ? " +
                   " and a.productgroupkey = b.productgroupkey " +
                   " and a.productbody = b.productbody " +
                   " and a.brand = b.brand " +
                   " and a.version = b.version " +
                   " and a.status = b.status " +
                   " and a.routename = b.routename " +
                   " and decode(substr(a.testmode,1,4),'TQAE','FT' ||substr(a.testmode,5),'SORT', 'S' ||substr(a.testmode,5)  ,a.testmode) = b.testmode " +
				   " and not exists (select 1\n" +
                   "                   from cat_pgm_all c\n" + 
                   "                  where  a.productgroupkey = c.productgroupkey\n" + 
                   "                     and a.productbody = c.productbody\n" + 
                   "                     and a.brand = c.brand\n" + //20180628-lai-add
                   "                     and a.status = c.status\n" + 
                   "                     and a.routename = c.routename\n" + 
                   "                     and a.testmode = c.testmode\n" + 
                   "                     and a.testseq = c.testseq\n" + 
                   "                     and b.testertype = c.testertype\n" + 
                   "                     and nvl(b.bodysize,' ')  = nvl(c.bodysize,' ')\n" + 
                   "                     and b.pgmid = c.pgmid and b.site = c.site)";
      
      TDSLogger.println(sql);
      TDSLogger.println("parameter="+
    		  fm.getProductbody() + "," +
    		  fm.getBrand() + "," +
    		  fm.getVersion() + "," +
    		  status + "," +
    		  product_type);
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,String.valueOf(fm.getProductbody()));
      ps.setString(2,String.valueOf(fm.getBrand()));
      ps.setString(3,String.valueOf(fm.getVersion()));
      ps.setString(4,String.valueOf(status));
      ps.setString(5,String.valueOf(product_type));
      //TDSLogger.println("insert_cat_route: "+fm.getProductbody() + " " + fm.getBrand() + " " + fm.getVersion() + " " +  status + " " + product_type);
      ps.executeUpdate();
      
      // NVM (C,I,S,W,Y,J,K) , MROM (C,I), XROM (C)
      String UpdSQL1 = null;
      UpdSQL1 = "update cat_pgm_all a " +
                "set c_grade = nvl((select temperature from tf_product_route"+table+ " b " +
                                " where b.product_body = a.productbody  " +
                                "  and b.brand = a.brand " +
                                "  and b.version = a.version " + 
                                "  and b.temperature is not null " + 
                                "  and b.temperature != ' ' ";
                                if("_tx".equals(table))
                                	UpdSQL1 += "  and b.tag != 2 ";
                                UpdSQL1 += "  and b.route_name = a.routename  " +
                                "  and b.step_name = a.testmode ),c_grade) ";
      if(!"XROM".equals(product_type)){ 
    	  UpdSQL1 += 
    		     ", i_grade = nvl((select temperature from tf_product_route"+table+ " b " +
                                " where b.product_body = a.productbody  " +
                                "  and b.brand = a.brand " +
                                "  and b.version = a.version " +
                                "  and b.temperature is not null " + 
                                "  and b.temperature != ' ' ";
                                if("_tx".equals(table))
                                	UpdSQL1 += "  and b.tag != 2 ";
                                UpdSQL1 += "  and b.route_name = a.routename  " +
                                "  and b.step_name = a.testmode ),i_grade) ";   
          if(!"MROM".equals(product_type)){    
                 UpdSQL1 +=  
                 ", s_grade = nvl((select temperature from tf_product_route"+table+ " b " +
                                " where b.product_body = a.productbody  " +
                                "  and b.brand = a.brand " +
                                "  and b.version = a.version " + 
                                "  and b.temperature is not null " + 
                                "  and b.temperature != ' ' ";
                                if("_tx".equals(table))
                                	UpdSQL1 += "  and b.tag != 2 ";
                                UpdSQL1 += "  and b.route_name = a.routename  " +
                                "  and b.step_name = a.testmode ),s_grade) " ;  
                UpdSQL1 +=  
                ", w_grade = nvl((select temperature from tf_product_route"+table+ " b " +
                               " where b.product_body = a.productbody  " +
                               "  and b.brand = a.brand " +
                               "  and b.version = a.version " + 
                               "  and b.temperature is not null " + 
                               "  and b.temperature != ' ' ";
                               if("_tx".equals(table))
                                   UpdSQL1 += "  and b.tag != 2 ";
                               UpdSQL1 += "  and b.route_name = a.routename  " +
                               "  and b.step_name = a.testmode ),w_grade) " ;  
               UpdSQL1 +=  
               ", y_grade = nvl((select temperature from tf_product_route"+table+ " b " +
                              " where b.product_body = a.productbody  " +
                              "  and b.brand = a.brand " +
                              "  and b.version = a.version " + 
                              "  and b.temperature is not null " + 
                              "  and b.temperature != ' ' ";
                              if("_tx".equals(table))
                                  UpdSQL1 += "  and b.tag != 2 ";
                              UpdSQL1 += "  and b.route_name = a.routename  " +
                              "  and b.step_name = a.testmode ),y_grade) " ;  
              UpdSQL1 +=  
                      ", j_grade = nvl((select temperature from tf_product_route"+table+ " b " +
                                     " where b.product_body = a.productbody  " +
                                     "  and b.brand = a.brand " +
                                     "  and b.version = a.version " + 
                                     "  and b.temperature is not null " + 
                                     "  and b.temperature != ' ' ";
                                     if("_tx".equals(table))
                                         UpdSQL1 += "  and b.tag != 2 ";
                                     UpdSQL1 += "  and b.route_name = a.routename  " +
                                     "  and b.step_name = a.testmode ),j_grade) " ;  
                     UpdSQL1 +=  
                     ", k_grade = nvl((select temperature from tf_product_route"+table+ " b " +
                                    " where b.product_body = a.productbody  " +
                                    "  and b.brand = a.brand " +
                                    "  and b.version = a.version " + 
                                    "  and b.temperature is not null " + 
                                    "  and b.temperature != ' ' ";
                                    if("_tx".equals(table))
                                        UpdSQL1 += "  and b.tag != 2 ";
                                    UpdSQL1 += "  and b.route_name = a.routename  " +
                                    "  and b.step_name = a.testmode ),k_grade) " ;  
                                    
                    UpdSQL1 += ", l_grade = nvl((select temperature from tf_product_route" + table + " b "
                            + " where b.product_body = a.productbody  " + "  and b.brand = a.brand " + "  and b.version = a.version "
                            + "  and b.temperature is not null " + "  and b.temperature != ' ' ";
                    if ("_tx".equals(table))
                        UpdSQL1 += "  and b.tag != 2 ";
                    UpdSQL1 += "  and b.route_name = a.routename  " + "  and b.step_name = a.testmode ),l_grade) ";
                    
                    UpdSQL1 += ", n_grade = nvl((select temperature from tf_product_route" + table + " b "
                            + " where b.product_body = a.productbody  " + "  and b.brand = a.brand " + "  and b.version = a.version "
                            + "  and b.temperature is not null " + "  and b.temperature != ' ' ";
                    if ("_tx".equals(table))
                        UpdSQL1 += "  and b.tag != 2 ";
                    UpdSQL1 += "  and b.route_name = a.routename  " + "  and b.step_name = a.testmode ),n_grade) ";
                    
                    UpdSQL1 += ", b_grade = nvl((select temperature from tf_product_route" + table + " b "
                            + " where b.product_body = a.productbody  " + "  and b.brand = a.brand " + "  and b.version = a.version "
                            + "  and b.temperature is not null " + "  and b.temperature != ' ' ";
                    if ("_tx".equals(table))
                        UpdSQL1 += "  and b.tag != 2 ";
                    UpdSQL1 += "  and b.route_name = a.routename  " + "  and b.step_name = a.testmode ),b_grade) ";
                    
                    UpdSQL1 += ", e_grade = nvl((select temperature from tf_product_route" + table + " b "
                            + " where b.product_body = a.productbody  " + "  and b.brand = a.brand " + "  and b.version = a.version "
                            + "  and b.temperature is not null " + "  and b.temperature != ' ' ";
                    if ("_tx".equals(table))
                        UpdSQL1 += "  and b.tag != 2 ";
                    UpdSQL1 += "  and b.route_name = a.routename  " + "  and b.step_name = a.testmode ),e_grade) ";
               
          }                  
       }
      //add by kevin for JB201200093
	  UpdSQL1 += ", programname = nvl((select distinct d.program_name from tf_product_route" + table + " b ,tf_test_parameter_ft" + table +  " c, tf_test_parameter_ft" + table+ " d" + 
										" where b.product_body = a.productbody " + 
										"	and b.brand = a.brand "	+ 
										"	and b.version = a.version "	+ 
										"	and b.qc_actual_mode is not null ";
	  									if ("_tx".equals(table))
	  										UpdSQL1 += "	and b.tag != 2 ";
	  									UpdSQL1 += "	and b.route_name = a.routename " +
	  									"	and b.step_name = a.testmode " +
	  									"   and b.version = c.version " + 
	  									"   and a.pgmid = c.pgm_id " +
	  									"   and c.sid = d.sid  " +
	  									"   and b.qc_actual_mode = d.test_type " +
	  									"   and c.product_body = d.product_body " +
	  									"   and c.brand = d.brand " +
	  									"   and c.version = d.version " +
	  									"   and c.backend_option = d.backend_option" +
	  									"   and c.pin_count = d.pin_count" +
	  									"   and c.site = d.site " +
	  									"   and c.tester = d.tester), a.programname) ";
     
      UpdSQL1 += ", pgmid = nvl((select distinct d.pgm_id from tf_product_route" + table + " b ,tf_test_parameter_ft" + table +  " c, tf_test_parameter_ft" + table+ " d" + 
              " where b.product_body = a.productbody " + 
              "   and b.brand = a.brand " + 
              "   and b.version = a.version " + 
              "   and b.qc_actual_mode is not null ";
              if ("_tx".equals(table))
                  UpdSQL1 += "    and b.tag != 2 ";
              UpdSQL1 += "    and b.route_name = a.routename " +
              "   and b.step_name = a.testmode " +
              "   and b.version = c.version " + 
              "   and a.pgmid = c.pgm_id " +
              "   and c.sid = d.sid  " +
              "   and b.qc_actual_mode = d.test_type " +
              "   and c.product_body = d.product_body " +
              "   and c.brand = d.brand " +
              "   and c.version = d.version " +
              "   and c.backend_option = d.backend_option" +
              "   and c.pin_count = d.pin_count" +
              "   and c.site = d.site " +
              "   and c.tester = d.tester), a.pgmid) ";
	  
          UpdSQL1 += 
                " where a.productbody = ? " +
                " and a.brand = ? " +
                " and a.version = ? " +
                " and a.status = ? " +
                " and a.testmode like 'TQAE%' ";
   
                TDSLogger.println(UpdSQL1);
                TDSLogger.println("parameter="+
              		  fm.getProductbody() + "," +
              		  fm.getBrand() + "," +
              		  fm.getVersion() + "," +
              		  status);
                PreparedStatement ps_1 = conn.prepareStatement(UpdSQL1);
                ps_1.setString(1,String.valueOf(fm.getProductbody()));
                ps_1.setString(2,String.valueOf(fm.getBrand()));
                ps_1.setString(3,String.valueOf(fm.getVersion()));
                ps_1.setString(4,String.valueOf(status));
                //TDSLogger.println("insert_cat_route: "+fm.getProductbody() + " " + fm.getBrand() + " " + fm.getVersion() + " " +  status );
                ps_1.executeUpdate();
                
             

      
      ps.clearParameters();
      ps.close();
      ps_1.clearParameters();
      ps_1.close();
      if(ps != null)
    	  ps = null;
      if(ps_1 != null)
    	  ps_1 = null;
      
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    } finally {
    }
    return true;
  }
  
  public static String getLogTime(String sid) {
	  String result = null;
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT to_char(LOG_TIME, 'yyyy-mm-dd hh24:mi:ss') LOG_TIME FROM TF_INFORMATION ");
	  sqlStmt.append("WHERE SID = " + sid);

	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  while (rs.next()) {
			  result = rs.getString("LOG_TIME");
			  break;
		  }
	      ps.clearParameters();
	      ps.close();
	      rs.close();
	      if(ps != null)
	    	  ps = null;
	      if(rs != null)
	    	  rs = null;
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
  }
  
  public static String getMXICFlag1(String productgroupkey, String productbody, String brand, String version,
		  String status, String routename, String stepname, String maskbeoption, String packagecode,
		  String packagetype, String pincount) {
	  String result = null;
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT COUNT(*) COUNT_NUM FROM CAT_ROUTE_PGM_ALL ");
	  sqlStmt.append(" WHERE PRODUCTGROUPKEY = '" + productgroupkey + "'");
	  sqlStmt.append("   AND PRODUCTBODY = '" + productbody + "'");
	  sqlStmt.append("   AND BRAND = '" + brand + "'");
	  sqlStmt.append("   AND VERSION = '" + version + "'");
	  sqlStmt.append("   AND STATUS = '" + status + "'");
	  sqlStmt.append("   AND MASKBEOPTION = '" + maskbeoption + "'");
	  if(pincount!=null)
	     sqlStmt.append("   AND PACKAGECODE = '" + packagecode + "'");
	  else
		 sqlStmt.append("   AND PACKAGECODE is null "); 
	  if(packagetype!=null)
	     sqlStmt.append("   AND PACKAGETYPE = '" + packagetype + "'");
	  else
		 sqlStmt.append("   AND PACKAGETYPE is null "); 
	  if(pincount!=null)
	     sqlStmt.append("   AND PINCOUNT = " + Integer.parseInt(pincount) + " ");
	  else
		 sqlStmt.append("   AND PINCOUNT is null "); 
	  sqlStmt.append("   AND ROUTENAME = '" + routename + "'");
	  sqlStmt.append("   AND TESTMODE = '" + stepname + "'");
	  

	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  while (rs.next()) {
			  int count_num = rs.getInt("COUNT_NUM");
			  if (count_num > 0)
				  result = "Y";
			  else
				  result = "N";
		  }
	      ps.clearParameters();
	      ps.close();
	      rs.close();
	      if(ps != null)
	    	  ps = null;
	      if(rs != null)
	    	  rs = null;
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
  }
  
  public static boolean chkMXICFlagYNNA( String productbody, String brand, String version,
		  String status, String productgroupkey, String routetype, String routename) {
	  boolean result = false;
	  String sql_1 = " select count(*) COUNT_NUM  " + 
      " from cat_route_all_tmp  " +
      " where productbody = ? " +
      " and brand = ? " +
      " and version = ? " +
      " and status = ? " +
      " and productgroupkey = ? " +
      " and routetype = ? " +
      " and routename = ? " +
      " and step = '2' " +
      " and flag in ('Y' ,'N', 'NA') " ;
	  TDSLogger.println(sql_1);
	  Connection conn = null;
	  try {
		  
		  conn = DBConnection.getConnection();
          PreparedStatement ps_1 = conn.prepareStatement(sql_1);
          ps_1.setString(1,String.valueOf(productbody));
          ps_1.setString(2,String.valueOf(brand));
          ps_1.setString(3,String.valueOf(version));
          ps_1.setString(4,String.valueOf(status));
          ps_1.setString(5,String.valueOf(productgroupkey));
          ps_1.setString(6,String.valueOf(routetype));
          ps_1.setString(7,String.valueOf(routename));
          ResultSet rs_1 = ps_1.executeQuery();
          while(rs_1.next()){
   		  int count_num = rs_1.getInt("COUNT_NUM");
			  if (count_num > 0)
				  result = true;
			  else
				  result = false;
		  }
	      ps_1.clearParameters();
	      ps_1.close();
	      rs_1.close();
	      if(ps_1 != null)
	    	  ps_1 = null;
	      if(rs_1 != null)
	    	  rs_1 = null;
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
  }
//Set OI to desired status
  public static boolean CATRoutePgmAll_insert(Connection conn,
		  String productgroupkey, String productbody, String brand, String version,
		  String status, String updatetime, String routetype, String routename,
		  String testmode, String maskbeoption, String packagecode, String packagetype,
		  String pincount, String codeno, String c_grade, String i_grade,String w_grade, String y_grade,String j_grade,String k_grade,String l_grade,String n_grade,String b_grade,String e_grade,
		  String s_grade, String bodysize, String testertype,
		  String pgmid, String prodgramname, String actualprogramname, String product_type,
		  String hw_configure,String site ) {
	  return CATRoutePgmAll_insert(conn, productgroupkey, productbody, brand, version,
			  status, updatetime, routetype, routename,
			  testmode, maskbeoption, packagecode, packagetype,
			  pincount, codeno, c_grade, i_grade,w_grade, y_grade,j_grade,k_grade,l_grade,n_grade,b_grade,e_grade,
			  s_grade, bodysize, testertype,
			  pgmid, prodgramname, actualprogramname, product_type,
			  hw_configure,site, "", "");
  }
  public static boolean CATRoutePgmAll_insert(Connection conn,
	  String productgroupkey, String productbody, String brand, String version,
	  String status, String updatetime, String routetype, String routename,
	  String testmode, String maskbeoption, String packagecode, String packagetype,
	  String pincount, String codeno, String c_grade, String i_grade,String w_grade, String y_grade,String j_grade,String k_grade,String l_grade,String n_grade,String b_grade,String e_grade,
	  String s_grade, String bodysize, String testertype,
	  String pgmid, String prodgramname, String actualprogramname, String product_type,
	  String hw_configure,String site, String form_factor_name, String module_option ) {

      String InsSQL1 = null;
      try {
          InsSQL1 = "insert into cat_route_pgm_all ";
          InsSQL1 += "(productgroupkey, productbody, brand, version, status, ";
          InsSQL1 += " updatetime, routetype, routename, testmode, maskbeoption, ";
          InsSQL1 += " packagecode, packagetype,  codeno, c_grade, i_grade, w_grade, y_grade,j_grade,k_grade,l_grade,n_grade,b_grade,e_grade, ";
          InsSQL1 += "  s_grade, bodysize, testertype, pgmid, programname, ";
          InsSQL1 += "  actualprogramname, product_type, pincount, hw_configure,site, form_factor_name, module_option ) ";
          InsSQL1 += " values( ?, ?, ?, ?, ?, ";
          InsSQL1 += " to_date(?, 'yyyy-mm-dd hh24:mi:ss'), ?, ?, ?, ?, ";
          InsSQL1 += " ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?, ";
          InsSQL1 += " ?, ?, ?, ?, ?, ";
          InsSQL1 += " ?, ? , ?, ?,?, ?,? ) ";
          PreparedStatement ps = conn.prepareStatement(InsSQL1);
          TDSLogger.println(InsSQL1);
          TDSLogger.println("paremeter = "+
        		  productgroupkey + "," +
        		  productbody + "," +
        		  brand + "," +
        		  version + "," +
        		  status + "," +
        		  updatetime + "," +
        		  routetype + "," +
        		  routename + "," +
        		  testmode + "," +
        		  maskbeoption + "," +
        		  packagecode + "," +
        		  packagetype + "," +
        		  codeno + "," +
        		  c_grade + "," +
        		  i_grade + "," +
                  w_grade + "," +
                  y_grade + "," +
                  j_grade + "," +
                  k_grade + "," +
                  l_grade + "," +
                  n_grade + "," +
                  b_grade + "," +
                  e_grade + "," +
        		  s_grade + "," +
        		  bodysize + "," +
        		  testertype + "," +
        		  pgmid + "," +
        		  prodgramname + "," +
        		  actualprogramname + "," +
        		  product_type + "," +
        		  pincount + "," +
        		  hw_configure + "," +
        		  site + "," + form_factor_name + "," + module_option);
          ps.setString(1, productgroupkey);
          ps.setString(2, productbody);
          ps.setString(3, brand);
          ps.setInt(4, Integer.parseInt(version));
          ps.setString(5, status);
          //new Timestamp(((java.sql.Date)updatetime).getTime())
          ps.setString(6, updatetime);
          ps.setString(7, routetype);
          ps.setString(8, routename);
          ps.setString(9, testmode);
          ps.setString(10, maskbeoption);
          ps.setString(11, packagecode);
          ps.setString(12, packagetype);
          ps.setString(13, codeno);
          ps.setString(14, c_grade);
          ps.setString(15, i_grade);
          ps.setString(16, w_grade);
          ps.setString(17, y_grade);
          ps.setString(18, j_grade);
          ps.setString(19, k_grade);
          ps.setString(20, l_grade);
          ps.setString(21, n_grade);
          ps.setString(22, b_grade);
          ps.setString(23, e_grade);
          ps.setString(24, s_grade);
          ps.setString(25, bodysize);
          ps.setString(26, testertype);
          ps.setInt(27, Integer.parseInt(pgmid));
          ps.setString(28, prodgramname);
          ps.setString(29, actualprogramname);
          ps.setString(30, product_type);
          if(pincount != null)
              ps.setInt(31, Integer.parseInt(pincount));
          else
        	  ps.setNull(31, java.sql.Types.INTEGER);
          ps.setString(32, hw_configure);
          ps.setString(33, site); 
          ps.setString(34, form_factor_name);
          ps.setString(35, module_option);
          //TDSLogger.println(ps.toString());
          ps.executeUpdate();
          ps.close();
          if(ps != null)
        	  ps = null;

      }
      catch (Exception ex) {
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          if(ex.getMessage().startsWith("ORA-00001"))
        	  return true;
          return false;
      }
      finally {
      }
      return true;
  }
//Set OI to desired status
  public static boolean CATRouteAllTmp_insert(Connection conn,
	  String productgroupkey, String productbody, String brand, String version,
	  String status, String updatetime,  String routetype, String routename, String testseq,
	  String testmode, String temperature, String testtime, String timeunit,
	  String flag, String step) {

      String InsSQL1 = null;
      try {
          InsSQL1 = "insert into cat_route_all_tmp ";
          InsSQL1 += "( productgroupkey, productbody, brand, version, status, ";
          InsSQL1 += "  routetype, routename, testseq, testmode, updatetime,  ";
          InsSQL1 += "  temperature, testtime, timeunit, flag, step ) ";
          InsSQL1 += " values( ?, ?, ?, ?, ?, ";
          InsSQL1 += " ?, ?, ?, ?, to_date(?, 'yyyy-mm-dd hh24:mi:ss'), ";
          InsSQL1 += " ?, ?, ?, ?, ? ) ";
          PreparedStatement ps = conn.prepareStatement(InsSQL1);
          TDSLogger.println(InsSQL1);
          TDSLogger.println("parameter="+
        		  productgroupkey + "," +
        		  productbody + "," +
        		  brand + "," +
        		  version + "," +
        		  status + "," +
        		  routetype + "," +
        		  routename + "," +
        		  testseq + "," +
        		  testmode + "," +
        		  updatetime + "," +
        		  temperature + "," +
        		  testtime + "," +
        		  timeunit + "," +
        		  flag + "," +
        		  step );
          ps.setString(1, productgroupkey);
          ps.setString(2, productbody);
          ps.setString(3, brand);
          ps.setInt(4, Integer.parseInt(version));
          ps.setString(5, status);
          //new Timestamp(((java.sql.Date)updatetime).getTime())
          ps.setString(6, routetype);
          ps.setString(7, routename);
          ps.setInt(8, Integer.parseInt(testseq));
          ps.setString(9, testmode);
          ps.setString(10, updatetime);
          ps.setString(11, temperature);
          if(testtime!=null)
              ps.setInt(12, Integer.parseInt(testtime));
          else
        	  ps.setNull(12, java.sql.Types.INTEGER);
          ps.setString(13, timeunit);
          ps.setString(14, flag);
          ps.setString(15, step);
          //TDSLogger.println(ps.toString());
          //TDSLogger.println("insert_cat_route_tmp1: "+productgroupkey + " " +productbody + " " + brand + " " + version + " " +  status  + " " + routename + " " + testseq + " " + testmode);
          ps.executeUpdate();
          ps.close();
          if(ps != null)
        	  ps = null;
                    
      }
      catch (Exception ex) {
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }
  
//Set OI to desired status
  public static boolean CATRouteAll_insert(Connection conn,
	  String productgroupkey, String productbody, String brand, String version,
	  String status, String updatetime,  String routetype, String routename, String testseq,
	  String testmode, String temperature, String testtime, String timeunit) {

      String InsSQL1 = null;
      try {
          InsSQL1 = "insert into cat_route_all ";
          InsSQL1 += "( productgroupkey, productbody, brand, version, status, ";
          InsSQL1 += "  routetype, routename, testseq, testmode, updatetime,  ";
          InsSQL1 += "  temperature, testtime, timeunit ) ";
          InsSQL1 += " values( ?, ?, ?, ?, ?, ";
          InsSQL1 += " ?, ?, ?, ?, to_date(?, 'yyyy-mm-dd hh24:mi:ss'), ";
          InsSQL1 += " ?, ?, ? ) ";
          PreparedStatement ps = conn.prepareStatement(InsSQL1);
          TDSLogger.println(InsSQL1);
          TDSLogger.println("parameter="+
        		  productgroupkey + "," +
        		  productbody + "," +
        		  brand + "," +
        		  version + "," +
        		  status + "," +
        		  routetype + "," +
        		  routename + "," +
        		  testseq + "," +
        		  testmode + "," +
        		  updatetime + "," +
        		  temperature + "," +
        		  testtime + "," +
        		  timeunit);
          ps.setString(1, productgroupkey);
          ps.setString(2, productbody);
          ps.setString(3, brand);
          ps.setInt(4, Integer.parseInt(version));
          ps.setString(5, status);
          //new Timestamp(((java.sql.Date)updatetime).getTime())
          ps.setString(6, routetype);
          ps.setString(7, routename);
          ps.setInt(8, Integer.parseInt(testseq));
          ps.setString(9, testmode);
          ps.setString(10, updatetime);
          ps.setString(11, temperature);
          if(testtime!=null)
             ps.setInt(12, Integer.parseInt(testtime));
          else
        	 ps.setInt(12, java.sql.Types.INTEGER); 
          ps.setString(13, timeunit);
          //TDSLogger.println(ps.toString());
          //TDSLogger.println("insert_cat_route: "+productgroupkey + " " +productbody + " " + brand + " " + version + " " +  status + " " + routename + " " + testseq + " " + testmode);
          ps.executeUpdate();
          
	      ps.clearParameters();
	      ps.close();
	      if(ps != null)
	    	  ps = null;
          
      }
      catch (Exception ex) {
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }
  
//Set OI to desired status
  public static boolean CATRouteAllTmp_update(Connection conn, HashMap data, String flag ){
		  
	  /*String productgroupkey, String productbody, String brand, String version,
	  String status, Date updatetime,  String routename, String testseq,
	  String testmode, String temperature, String testtime, String timeunit,
	  String flag, String step) {*/

      String InsSQL1 = null;
      try {
          InsSQL1 = "update cat_route_all_tmp ";
          InsSQL1 += "set flag = ? ";
          InsSQL1 += "where productgroupkey = ? ";
          InsSQL1 += "  and productbody = ? ";
          InsSQL1 += "  and brand = ? ";
          InsSQL1 += "  and version = ? ";
          InsSQL1 += "  and status = ? ";
          InsSQL1 += "  and routetype = ? ";
          InsSQL1 += "  and routename = ? ";
          InsSQL1 += "  and testseq = ? ";
          InsSQL1 += "  and testmode = ? ";
          InsSQL1 += "  and step = ?  ";
   
          PreparedStatement ps = conn.prepareStatement(InsSQL1);
          TDSLogger.println(InsSQL1);
          TDSLogger.println("parameter="+
        		  flag + "," +
        		  data.get("productgroupkey").toString() + "," +
        		  data.get("productbody").toString() + "," +
        		  data.get("brand").toString() + "," +
        		  data.get("version").toString() + "," +
        		  data.get("status").toString() + "," +
        		  data.get("routetype").toString() + "," +
        		  data.get("routename").toString() + "," +
        		  data.get("testseq").toString() + "," +
        		  data.get("testmode").toString() + "," + 
        		  data.get("step").toString() );
          ps.setString(1, flag);
          ps.setString(2, data.get("productgroupkey").toString());
          ps.setString(3, data.get("productbody").toString());
          ps.setString(4, data.get("brand").toString());
          ps.setInt(5, Integer.parseInt(data.get("version").toString()));
          ps.setString(6, data.get("status").toString());
          //new Timestamp(((java.sql.Date)updatetime).getTime())
          ps.setString(7, data.get("routetype").toString());
          ps.setString(8, data.get("routename").toString());
          ps.setInt(9, Integer.parseInt(data.get("testseq").toString()));
          ps.setString(10, data.get("testmode").toString());
          ps.setString(11, data.get("step").toString());
          //TDSLogger.println(ps.toString());
          //TDSLogger.println("insert_cat_route_tmp2: "+data.get("productgroupkey").toString() + " " +data.get("productbody").toString() + " " + data.get("brand").toString() + " " + data.get("version").toString() + " " +  data.get("status").toString()  + " " + data.get("routename").toString() + " " + data.get("testseq").toString() + " " + data.get("testmode").toString());
          ps.executeUpdate();
	      ps.clearParameters();
	      ps.close();
	      
	      if(ps != null)
	    	  ps = null;
          
      }
      catch (Exception ex) {
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }
  
//Set OI to desired status
  public static boolean CATAllError_insert(Connection conn,
	  String productbody, String brand, String version, String status) {

      String InsSQL1 = null;
      String InsertSQL = null;
      try {
          InsSQL1 = "insert into cat_all_error ";
          InsSQL1 += "(productbody, brand, version, status, updatetime) ";
          InsSQL1 += " values( ?, ?, ?, ?, sysdate) ";
          PreparedStatement ps = conn.prepareStatement(InsSQL1);
          ps.setString(1, productbody);
          ps.setString(2, brand);
          ps.setInt(3, Integer.parseInt(version));
          ps.setString(4, status);
          //TDSLogger.println(ps.toString());
          ps.executeUpdate();
	      ps.clearParameters();
	      ps.close();

	      if(ps != null)
	    	  ps = null;

      }
      catch (Exception ex) {
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }
  public static String getGroupKey(String productType, String product_body, String brand, String option, String option_rev, String body_version,
		  String package_code, String pin_count, String with_code, String route, String add_route, String sales_form, int facility) {
  String curGroupKey = null;
  if ("NVM".equals(productType)){
      if ((add_route == null) || add_route.equals("null"))
                  add_route = "";
      if (facility == 0) {
          if (productType.equals("ASM")) {
                  String buf = null;
                  if ((sales_form != null) && sales_form.equals("W"))
                          buf = " for wafer sale";
                  else
                          buf = " for package sale";
                  curGroupKey = product_body + option + buf;
          } else
                  //curGroupKey = product_body + option + with_code +  route + add_route;
        	  curGroupKey = product_body + option + with_code +  route ;
      } else {
          if (productType.equals("ASM"))
                  curGroupKey = product_body + option;
          else
                  //curGroupKey = product_body + option + with_code + pin_count + package_code + route + add_route;
        	  curGroupKey = product_body + option + with_code + pin_count + package_code + route ;
      }
  }else if ("MROM".equals(productType)){
	  if (facility == 0) {
		  String buf = null;
		  if ((sales_form != null) && sales_form.equals("W"))
			  buf = " (for wafer sale)";
		  else
			  buf = " (for package sale)";
		  curGroupKey = product_body + option + route + buf;
	  } else {
		  curGroupKey = product_body + option + pin_count + package_code + route;
	  }
  }else if ("XROM".equals(productType)){
      String brev = null, orev = null;
      if (body_version.equals("*")) brev = ""; else brev = body_version;
      if (option_rev.equals("*")) orev = ""; else orev = option_rev;
//      if ((add_route == null) || add_route.equals("null")) add_route = "";
      if (facility == 0) {
              curGroupKey = product_body + brev + option + orev +  route;
      } else {
              curGroupKey = product_body + brev + option + orev + pin_count + package_code + route;
      }
  } else if ("MMS".equals(productType)) {
	  curGroupKey = product_body + body_version + option + option_rev + pin_count + package_code + route;
  }
  return curGroupKey;
}

  


  // remove table data
  public static boolean delete_data(Connection conn,
                                    String table_name,
                                    String sid,
                                    String product_body,
                                    String brand,
                                    String version) {

      StringBuffer sql_del = new StringBuffer();
      try {
          sql_del.append("delete from "+table_name+" where sid = ? and product_body=? and brand=? and version=? ");
          PreparedStatement ps_del = conn.prepareStatement(sql_del.toString());
          ps_del.setString(1, sid);
          ps_del.setString(2, product_body);
          ps_del.setString(3, brand);
          ps_del.setString(4, version);
          ps_del.executeUpdate();
          
          ps_del.clearParameters();
          ps_del.close();

	      if(ps_del != null)
	    	  ps_del = null;

      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }

  // remove table data
  

  // remove table data
  public static boolean delete_data(Connection conn,
                                    String table_name,
                                    String product_body,
                                    String brand,
                                    String status) {

      StringBuffer sql_del = new StringBuffer();
      try {
          sql_del.append("delete from "+table_name+" where productbody = ? and brand = ?  ");
          if("A".equals(status))
             sql_del.append(" and status = ? ");
          PreparedStatement ps_del = conn.prepareStatement(sql_del.toString());
          ps_del.setString(1, product_body);
          ps_del.setString(2, brand);
          if("A".equals(status))
             ps_del.setString(3, status);
          ps_del.executeUpdate();
          ps_del.clearParameters();
          if(ps_del != null)
        	  ps_del = null;

      }
      catch (Exception ex) {
          ex.printStackTrace();
          DBConnection.rollback(conn);
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
          return false;
      }
      finally {
      }
      return true;
  }
  public static void insertMesTable(String product_body){
		TDSLogger.println("insertMesTable() - product_body: " + product_body);
		Connection con = null;
		try {
			con = DBConnection.getDBLinkConnection();
			DBConnection.setConnectionAutoCommit(con, false);
			
			String sql2 = 
				"INSERT INTO fwadmin.tbl_e8049_release_all@mesprod.world\n" +
				"        (product_body, createtime, createuserid)\n" + 
				"values(?,to_char(sysdate, 'yyyymmdd hh24miss') || '000','TIM')";
			TDSLogger.println(sql2);
			DBUtil.execDML(con, sql2, new Object[]{product_body});
			con.commit();
			
		}catch(Exception e){
			TDSLogger.println(e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			DBConnection.close(con);
		}
	}

//insert data to CAT_Route_Pgm
  public static boolean insert_cat_route_pgm_all_ft_MMS(    ProTestRouteBeanAF fm,
                                          String table,
                                         String product_type,
                                         String status,
                                         Connection conn) {
    try {

    	StringBuffer sql = new StringBuffer();
        sql.append("select distinct ");
        sql.append("'Main' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize ,c.actual_file actualprogramname, hw_configure   ");
        sql.append(", a.form_factor_name, a.module_option "); //new columns
        sql.append("from tf_bom_route_mms"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
        sql.append("and a.ft_route = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.form_factor_name = c.form_factor_name ");
        sql.append("and a.module_option = c.module_option ");
        sql.append("and a.tag != 2 "); 
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add1' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure  ");
        sql.append(", a.form_factor_name, a.module_option "); //new columns
        sql.append("from tf_bom_route_mms"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' ");
        sql.append("and a.ft_route_add = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.form_factor_name = c.form_factor_name ");
        sql.append("and a.module_option = c.module_option ");
        sql.append("and a.tag != 2 ");
       // sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add2' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add2 ft_route_add,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure   ");
        sql.append(", a.form_factor_name, a.module_option "); //new columns
        sql.append("from tf_bom_route_mms"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' ");
        sql.append("and a.ft_route_add2 = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.form_factor_name = c.form_factor_name ");
        sql.append("and a.module_option = c.module_option ");
        sql.append("and a.tag != 2 ");
        //sql.append("and c.site = 'TEST1' ");
        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add3' route_type,a.product_body,a.brand,a.version,a.package_type packagecode,c.package_type packagetype,a.pin_count pincount,'' codeno,a.backend_option maskbeoption, a.fg_with_code, ");
        sql.append("a.ft_route,a.ft_route_add3 ft_route_add ,b.test_mode testmode,c.tester testertype,c.site,c.pgm_id,c.program_name programname,c.i_grade,c.c_grade,c.w_grade,c.y_grade,c.j_grade,c.k_grade,c.l_grade,c.n_grade,c.b_grade,c.e_grade,c.s_grade,nvl(c.body_size,' ') bodysize, c.actual_file actualprogramname, hw_configure   ");
        sql.append(", a.form_factor_name, a.module_option "); //new columns
        sql.append("from tf_bom_route_mms"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add3) is not null and ft_route_add3 != 'NA' ");
        sql.append("and a.ft_route_add3 = b.route_name ");
        sql.append("and a.sid = c.sid ");
        sql.append("and a.backend_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.form_factor_name = c.form_factor_name ");
        sql.append("and a.module_option = c.module_option ");
        sql.append("and a.tag != 2 ");

        sql.append("order by product_body,maskbeoption,form_factor_name,module_option,ft_route,ft_route_add,route_type desc,testmode ");
        TDSLogger.println(sql);
        TDSLogger.println("parameter = "+fm.getSid() + "," + fm.getSid() + "," +
        		fm.getSid() + "," + fm.getSid() );
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ps.setString(1,String.valueOf(fm.getSid()));
      ps.setString(2,String.valueOf(fm.getSid()));
      ps.setString(3,String.valueOf(fm.getSid()));
      ps.setString(4,String.valueOf(fm.getSid()));
      ResultSet rs = ps.executeQuery();

      String GroupKey = "";
      
      while(rs.next()){
          GroupKey = "";
          String routename = "";
          GroupKey = getGroupKey(product_type, rs.getString("product_body"), rs.getString("brand"), rs.getString("maskbeoption"),"","",
                  rs.getString("module_option"), rs.getString("form_factor_name"), rs.getString("fg_with_code"), rs.getString("ft_route"), rs.getString("ft_route_add"), null, 1);
          String updatetime = getLogTime(fm.getSid());
          if (rs.getString("route_type").equals("Main")) {
        	  routename = rs.getString("ft_route");
      	} else {
      		  routename = rs.getString("ft_route_add");
      	}
          /*String temperature = " ";
            if (rs.getString("temperature") != null){
          	temperature = rs.getString("temperature");
          	if (!temperature.equals("ROOM TEMP") && !temperature.equals(""))
          		temperature = temperature + "℃";
          }*/
          boolean result = CATRoutePgmAll_insert(conn, GroupKey, rs.getString("product_body"), rs.getString("brand"), 
        		  rs.getString("version"), status, updatetime, rs.getString("route_type"), 
        		  routename, rs.getString("testmode"), rs.getString("maskbeoption"), rs.getString("packagecode"), 
        		  rs.getString("packagetype"), rs.getString("pincount"), rs.getString("codeno"), rs.getString("c_grade"), 
        		  rs.getString("i_grade"), rs.getString("w_grade"), rs.getString("y_grade"),rs.getString("j_grade"), rs.getString("k_grade"), rs.getString("l_grade"), rs.getString("n_grade"), rs.getString("b_grade"), rs.getString("e_grade"), rs.getString("s_grade"), rs.getString("bodysize"), rs.getString("testertype"), 
        		  rs.getString("pgm_id"), rs.getString("programname"), rs.getString("actualprogramname"), product_type,rs.getString("hw_configure"),rs.getString("site") 
        		  , rs.getString("form_factor_name"), rs.getString("module_option"));
          if(result == false)
        	  return false; 
        
      }
      ps.clearParameters();
      ps.close();
      rs.close();
      if(ps != null)
    	  ps = null;
      if(rs != null)
    	  rs = null;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    } finally {
    }
    return true;
  }
  
}
