package com.mxic.oiplus.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.gprs.applyform.UploadFileConstant;
import com.mxic.gprs.basicdata.UploadFileActionForm;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.tdsplus.auto.common.DB;
import com.mxic.tdsplus.pgm.PG_DAO;
import com.mxic.tdsplus.pgm.Util;

public class Conv2ExcelAction extends Action{

	public static String MakeBOMString(String sid, String table) {

		Connection conn = null;
		String result = "";

		try {
			String sql =
				"SELECT 1 tag, product_body,brand,to_char(version) version,backend_option,fg_with_code,\n" +
				"to_char(pin_count) pin_count,package_type,ft_route_code,nvl(ft_route,' ') ft_route,nvl(ft_route_add,' ') ft_route_add, nvl(ft_route_add2,' ') ft_route_add2, nvl(ft_route_add3,' ') ft_route_add3,endurance,nvl(tf_comment,' ') tf_comment,nvl(mcp_flag,' ') mcp_flag,\n" +
				"mask_option,db_with_code,sort_route_code,ws_route,wsspecialcontrol, nvl(ws_route_add,' ') ws_route_add," +
				"TF_CHECK_STEP('AVI',ws_route) avi, TF_CHECK_STEP('INK',ws_route) ink, nvl(tf_ws_comment,' ') tf_ws_comment, nvl(quality_level,' ') quality_level,nvl(quality_level_comment,' ') quality_level_comment  \n" +
				"from tf_bom_route"+table+"\nwhere sid = " + sid +
				" union all SELECT 0 tag, 'Product Body','Brand','Version','Backend Option','FG with Code',\n" +
				"'Pin Count','Package Type','FT Route Code','FT Route','FT Route Add1','FT Route Add2','FT Route Add3','FT Special Control','FT Comment','MCP Flag',\n" +
				"'Mask Option','DB with Code','Sort Route Code','WS Route','WS Special Control','WS Route Add','AVI','INK','WS Comment','Quality Level','Quality Llevel Comment' from dual \n" +
				" order by tag,product_body,brand,version,backend_option,fg_with_code,\n" +
				"pin_count,package_type,ft_route_code,ft_route,ft_route_add,ft_route_add2,ft_route_add3,\n" +
				"mask_option,db_with_code,sort_route_code,ws_route,wsspecialcontrol,ws_route_add,quality_level, quality_level_comment\n";

			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			result = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=big5\">\n" +
	          "</head><body><table border>\n" ;
			while (rs.next()) {
				result = result + "<tr><td>" + rs.getString("product_body") + "</td>" +
				"<td>" + rs.getString("brand") + "</td>" +
				"<td>" + rs.getString("version") + "</td>" +
				"<td>" + rs.getString("backend_option") + "</td>" +
				"<td>" + rs.getString("fg_with_code") + "</td>" +
				"<td>" + rs.getString("pin_count") + "</td>" +
				"<td>" + rs.getString("package_type") + "</td>" +
				"<td>" + rs.getString("ft_route_code") + "</td>" +
				"<td>" + rs.getString("ft_route") + "</td>" +
				"<td>" + rs.getString("ft_route_add") + "</td>" +
				"<td>" + rs.getString("ft_route_add2") + "</td>" +
				"<td>" + rs.getString("ft_route_add3") + "</td>" +
				"<td>" + rs.getString("endurance") + "</td>" +
				"<td>" + rs.getString("tf_comment") + "</td>" +
				"<td>" + rs.getString("quality_level") + "</td>" +
				"<td>" + rs.getString("quality_level_comment") +"</td>\n" +
				"<td>" + rs.getString("mcp_flag") + "</td>" +
				"<td>" + rs.getString("mask_option") + "</td>" +
				"<td>" + rs.getString("db_with_code") + "</td>" +
				"<td>" + rs.getString("sort_route_code") + "</td>" +
				"<td>" + rs.getString("ws_route") + "</td>" +
				"<td>" + rs.getString("wsspecialcontrol") + "</td>" +
				"<td>" + rs.getString("ws_route_add") + "</td>" +
				"<td>" + rs.getString("avi") + "</td>" +
				"<td>" + rs.getString("ink") + "</td>" +
				"<td>" + rs.getString("tf_ws_comment") + "</td></tr>" ;
			}
			ps.close();
			rs.close();
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return result;
		} finally {
          DBConnection.close(conn);
		}
		return result;
	}

	public static String MakeBomMcpString(String sid, String table) {

		Connection conn = null;
		String result = "";

		try {
			String sql =
				"SELECT 1 tag, product_body,brand,to_char(version) version,backend_option,fg_with_code,\n" +
				"to_char(pin_count) pin_count,package_type,ft_route_code,nvl(ft_route,' ') ft_route,nvl(ft_route_add,' ') ft_route_add, nvl(ft_route_add2,' ') ft_route_add2, nvl(ft_route_add3,' ') ft_route_add3, endurance,\n" +
				"nvl(tf_comment,' ') tf_comment,to_char(component_no) component_no,nvl(com_prod_body,' ') com_prod_body,\n" +
				"nvl(com_mask_option,' ') com_mask_option,nvl(com_backend_option,' ') com_backend_option,\n" +
				"db_with_code,nvl(sort_route_code,' ') sort_route_code,nvl(ws_route,' ') ws_route,nvl(ws_route_add,' ') ws_route_add," +
				"TF_CHECK_STEP('AVI',ws_route) avi, TF_CHECK_STEP('INK',ws_route) ink, nvl(tf_ws_comment,' ') tf_ws_comment, nvl(quality_level,' ') quality_level,nvl(quality_level_comment,' ') quality_level_comment \n" +
				"from tf_bom_route_mcp"+table+"\nwhere sid = " + sid +
				" union all SELECT 0 tag, 'Product Body','Brand','Version','Backend Option','FG with Code',\n" +
				"'Pin Count','Package Type','FT Route Code','FT Route','FT Route Add1','FT Route Add2','FT Route Add3','FT Special Control'," +
				"'FT Comment','Component No','Component Product','COM Mask Opt.','COM BE Option',\n" +
				"'DB with Code','Sort Route Code','WS Route','WS Route Add','AVI','INK','WS Comment','Quality Level','Quality Llevel Comment' from dual \n" +
				" order by tag,ft_route_code,component_no,quality_level, quality_level_comment \n";

			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			result = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=big5\">\n" +
	          "</head><body><table border>\n" ;
			while (rs.next()) {
				result = result + "<tr><td>" + rs.getString("product_body") + "</td>" +
				"<td>" + rs.getString("brand") + "</td>" +
				"<td>" + rs.getString("version") + "</td>" +
				"<td>" + rs.getString("backend_option") + "</td>" +
				"<td>" + rs.getString("fg_with_code") + "</td>" +
				"<td>" + rs.getString("pin_count") + "</td>" +
				"<td>" + rs.getString("package_type") + "</td>" +
				"<td>" + rs.getString("ft_route_code") + "</td>" +
				"<td>" + rs.getString("ft_route") + "</td>" +
				"<td>" + rs.getString("ft_route_add") + "</td>" +
				"<td>" + rs.getString("ft_route_add2") + "</td>" +
				"<td>" + rs.getString("ft_route_add3") + "</td>" +
				"<td>" + rs.getString("endurance") + "</td>" +
				"<td>" + rs.getString("tf_comment") + "</td>" +
				"<td>" + rs.getString("quality_level") + "</td>" +
				"<td>" + rs.getString("quality_level_comment") +"</td>\n" +
				"<td>" + rs.getString("component_no") + "</td>" +
				"<td>" + rs.getString("com_prod_body") + "</td>" +
				"<td>" + rs.getString("com_mask_option") + "</td>" +
				"<td>" + rs.getString("com_backend_option") + "</td>" +
				"<td>" + rs.getString("db_with_code") + "</td>" +
				"<td>" + rs.getString("sort_route_code") + "</td>" +
				"<td>" + rs.getString("ws_route") + "</td>" +
				"<td>" + rs.getString("ws_route_add") + "</td>" +
				"<td>" + rs.getString("avi") + "</td>" +
				"<td>" + rs.getString("ink") + "</td>" +
				"<td>" + rs.getString("tf_ws_comment") +"</td></tr>\n";
			}
			ps.close();
			rs.close();
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return result;
		} finally {
          DBConnection.close(conn);
		}
		return result;
	}	
	
	public static String MakeBOMMROMString(String sid, String table) {

		Connection conn = null;
		String result = "";

		try {
			String sql =
				"SELECT 1 tag, product_body,to_char(version) version,mask_option,sales_form," +
				"ws_route,nvl(ft_route,' ') ft_route, nvl(ft_route_add1,' ') ft_route_add1, nvl(ft_route_add2,' ') ft_route_add2, " +
				"nvl(ft_comment, ' ') ft_comment " +
				"from tf_bom_route_mrom"+table+" where sid = " + sid +
				" union all SELECT 0 tag, 'Product Body','Version','Mask Option','Sales Form'," +
				"'WS Route','FT Route', 'FT Route Add 1', 'FT Route Add 2', 'FT Comment' from dual " +
				" order by tag,product_body,version,mask_option,sales_form," +
				"ws_route,ft_route ";

			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			result = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=big5\">\n" +
	          "</head><body><table border>\n" ;
			while (rs.next()) {
				result = result + "<tr><td>" + rs.getString("product_body") + "</td>" +
				"<td>" +rs.getString("version") + "</td>" +
				"<td>" +rs.getString("mask_option") + "</td>" +
				"<td>" +rs.getString("sales_form") + "</td>" +
				"<td>" +rs.getString("ws_route") + "</td>" +
				"<td>" +rs.getString("ft_route") + "</td>" +
				"<td>" +rs.getString("ft_route_add1") + "</td>" +
				"<td>" +rs.getString("ft_route_add2") + "</td>" +
				"<td>" +rs.getString("ft_comment") +"</td></tr>\n";
			}
			ps.close();
			rs.close();
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return result;
		} finally {
          DBConnection.close(conn);
		}
		return result;
	}
	//LAI-ADD-20070521-START
  //For XtraROM
  public static String MakeBOM_XROM_String(String sid, String table) {

    Connection conn = null;
    String result = "";

    try {
      String sql =
          "SELECT 1 tag, product_body,to_char(version) version,to_char(body_version) body_version,mask_option,mask_option_rev,code_no," +
          "to_char(pin_count) pin_count,package_code,route_type,ft_route_code,ft_route,nvl(ft_route_add,' ') ft_route_add, nvl(ft_route_add1,' ') ft_route_add1,nvl(ft_route_add2,' ') ft_route_add2, nvl(ft_route_add3,' ') ft_route_add3,nvl(ft_route_add4,' ') ft_route_add4, nvl(ft_route_add5,' ') ft_route_add5,nvl(ft_comment,' ') ft_comment," +
          "sort_route_code,ws_route,nvl(ws_route_add,' ') ws_route_add,nvl(ws_route_add1,' ') ws_route_add1,nvl(ws_route_add2,' ') ws_route_add2,nvl(ws_route_add3,' ') ws_route_add3,nvl(ws_route_add4,' ') ws_route_add4,nvl(ws_comment,' ') ws_comment " +
          "from tf_bom_route"+table+" where sid = " + sid +
          " union all SELECT 0 tag, 'Product Body','Version','Body Version','Mask Option','Mask Option rev.','Code No'," +
          "'Pin Count','Package Code','Route Type','FT Route Code','FT Route','FT Route Add','FT Route Add1','FT Route Add2','FT Route Add3','FT Route Add4','FT Route Add5','FT Comment'," +
          "'Sort Route Code','WS Route','WS Route Add','WS Route Add1','WS Route Add2','WS Route Add3','WS Route Add4','WS Comment' from dual " +
          " order by tag,product_body,version,body_version,mask_option,mask_option_rev,code_no," +
          "pin_count,package_code,route_type,ft_route_code,ft_route,ft_route_add,ft_route_add1,ft_route_add2,ft_route_add3,ft_route_add4,ft_route_add5," +
          "sort_route_code,ws_route,ws_route_add,ws_route_add1,ws_route_add2,ws_route_add3,ws_route_add4 ";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      TDSLogger.println(sql);
      ResultSet rs = ps.executeQuery();
      result = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=big5\">\n" +
      "</head><body><table border>\n" ;
      while (rs.next()) {
    	  result = result + "<tr><td>" + rs.getString("product_body") + "</td>" +
    	  "<td>" +rs.getString("version") + "</td>" +
    	  "<td>" +rs.getString("body_version") + "</td>" +
    	  "<td>" +rs.getString("mask_option") + "</td>" +
    	  "<td>" +rs.getString("mask_option_rev") + "</td>" +
    	  "<td>" +rs.getString("code_no") + "</td>" +
    	  "<td>" +rs.getString("pin_count") + "</td>" +
    	  "<td>" +rs.getString("package_code") + "</td>" +
    	  "<td>" +rs.getString("route_type") + "</td>" +
    	  "<td>" +rs.getString("ft_route_code") + "</td>" +
    	  "<td>" +rs.getString("ft_route") + "</td>" +
    	  "<td>" +rs.getString("ft_route_add") + "</td>" +
    	  "<td>" +rs.getString("ft_route_add1") + "</td>" +
    	  "<td>" +rs.getString("ft_route_add2") + "</td>" +
    	  "<td>" +rs.getString("ft_route_add3") + "</td>" +
    	  "<td>" +rs.getString("ft_route_add4") + "</td>" +
    	  "<td>" +rs.getString("ft_route_add5") + "</td>" +
    	  "<td>" +rs.getString("ft_comment") + "</td>" +
    	  "<td>" +rs.getString("sort_route_code") + "</td>" +
    	  "<td>" +rs.getString("ws_route") + "</td>" +
    	  "<td>" +rs.getString("ws_route_add") + "</td>" +
    	  "<td>" +rs.getString("ws_route_add1") + "</td>" +
    	  "<td>" +rs.getString("ws_route_add2") + "</td>" +
    	  "<td>" +rs.getString("ws_route_add3") + "</td>" +
    	  "<td>" +rs.getString("ws_route_add4") + "</td>" +
    	  "<td>" +rs.getString("ws_comment") +"</td></tr>\n";
      }
      ps.close();
      rs.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return result;
    } finally {
      DBConnection.close(conn);
    }
    return result;
  }

  //For REROUTE XtraROM
  public static String MakeBOM_REROUTE_XROM_String(String sid, String table) {

    Connection conn = null;
    String result = "";

    try {
      String sql =
          "SELECT 1 tag, product_body,to_char(version) version,to_char(body_version) body_version,mask_option,mask_option_rev,code_no," +
          "to_char(pin_count) pin_count,package_code,route_type,recycle_code,ft_route,nvl(ft_route_add,' ') ft_route_add,nvl(ft_comment,' ') ft_comment " +
          "from tf_bom_reroute"+table+" where sid = " + sid +
          " union all SELECT 0 tag, 'Product Body','Version','Body Version','Mask Option','Mask Option rev.','Code No'," +
          "'Pin Count','Package Code','Route Type','FT Route Code','FT Route','FT Route Add','FT Comment'  from dual " +
          " order by tag,product_body,version,body_version,mask_option,mask_option_rev,code_no," +
          "pin_count,package_code,route_type,recycle_code,ft_route,ft_route_add ";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      TDSLogger.println(sql);
      ResultSet rs = ps.executeQuery();
      result = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=big5\">\n" +
      "</head><body><table border>\n" ;
      while (rs.next()) {
    	  result = result + "<tr><td>" + rs.getString("product_body") + "</td>" +
    	  "<td>" +rs.getString("version") + "</td>" +
    	  "<td>" +rs.getString("body_version") + "</td>" +
    	  "<td>" +rs.getString("mask_option") + "</td>" +
    	  "<td>" +rs.getString("mask_option_rev") + "</td>" +
    	  "<td>" +rs.getString("pin_count") + "</td>" +
    	  "<td>" +rs.getString("package_code") + "</td>" +
    	  "<td>" +rs.getString("code_no") + "</td>" +
    	  "<td>" +rs.getString("route_type") + "</td>" +
    	  "<td>" +rs.getString("recycle_code") + "</td>" +
    	  "<td>" +rs.getString("ft_route") + "</td>" +
    	  "<td>" +rs.getString("ft_route_add") + "</td>" +
    	  "<td>" +rs.getString("ft_comment") +"</td></tr>\n";
      }
      ps.close();
      rs.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return result;
    } finally {
      DBConnection.close(conn);
    }
    return result;
  }
//LAI-ADD-20070521-END

  //For YIELD download
  public static String MakeYIELD_String(String sid, String table) {

    Connection conn = null;
    String result = "";

    try {
      String sql =
          "SELECT 1 tag, seq, to_char(yid) yid, product_code,test_mode,nvl(auto_ship,' ') auto_ship, \n" +
          "nvl(hold_pe,' ') hold_pe, \n" +
          "nvl(hold_bin,' ') hold_bin, \n" +
          "nvl(hold_bin_cri,' ') hold_bin_cri, \n" +
          "nvl(auto_scrap,' ') auto_scrap, \n" +
          "nvl(stop,' ') stop, \n" +
          "nvl(mrb,' ') mrb, \n" +
          "nvl(sampling_yield,' ') sampling_yield, \n" +
          "nvl(notes,' ') notes \n" +
          "from tf_yield"+table +
          " where sid = " + sid + "\n" +
          "union all SELECT 0 tag, 0 seq, 'Yid','Product Code','Test Mode','Auto Ship','Hold PE',\n" +
          "'Hold Bin','Hold Bin Criteria','Auto Scrap','STOP','MRB','Sampling Yield','Notes' from dual\n" +
          "order by tag, seq";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      TDSLogger.println(sql);
      ResultSet rs = ps.executeQuery();
      result = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=big5\">\n" +
          "</head><body><table border>\n" ;
      while (rs.next()) {
        result = result + "<tr><td>" + rs.getString("yid") + "</td>" +
            "<td>" + rs.getString("product_code").trim() + "</td>" +
            "<td>" + rs.getString("test_mode").trim() + "</td>" +
            "<td>" + rs.getString("auto_ship").trim() + "</td>" +
            "<td>" + rs.getString("hold_pe").trim() + "</td>" +
            "<td>" + rs.getString("hold_bin").trim() + "</td>" +
            "<td>" + rs.getString("hold_bin_cri").trim() + "</td>" +
            "<td>" + rs.getString("auto_scrap").trim() + "</td>" +
            "<td>" + rs.getString("stop").trim() + "</td>" +
            "<td>" + rs.getString("mrb").trim() + "</td>" +
            "<td>" + rs.getString("sampling_yield").trim() + "</td>" +
            "<td>" + rs.getString("notes").trim() +"</td></tr>\n";
      }
      result += "</table></body></html>";
      ps.close();
      rs.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return result;
    } finally {
      DBConnection.close(conn);
    }
    return result;
  }
//MAO-20081121-END

	/**
	 * This is the method called on by ActionServlet
	 * when a request is made.
	 */
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {

		Conv2ExcelForm fm = (Conv2ExcelForm) form;
		String fileName = fm.getFileName();
		String sid = fm.getSid();
		String type = fm.getType();
		String result = null;

		if (type.equals("BOM_TX"))
          result = MakeBOMString(sid, "_tx");
		else if (type.equals("BOM_MCP_TX"))
	          result = MakeBomMcpString(sid, "_tx");
		else if (type.equals("BOM_MROM_TX"))
          result = MakeBOMMROMString(sid, "_tx");
        else if (type.equals("BOM_XROM_TX"))//LAI-ADD-20070521
          result = MakeBOM_XROM_String(sid, "_xrom_tx");
        else if (type.equals("BOM_REROUTE_XROM_TX"))//LAI-ADD-20070521
          result = MakeBOM_REROUTE_XROM_String(sid, "_xrom_tx");
        else if (type.equals("YIELD_TX"))//MAO-20081121
          result = MakeYIELD_String(sid, "_tx");
		else if (type.equals("NVM") || type.equals("MROM") || type.equals("XROM")) {
			HashMap opConts = null;
			this.downloadFile(mapping, form, request, response, type);
			return null;
		}
		else if (type.equals("PG_INTERVAL")) {
			HashMap opConts = null;
			this.downloadFilePG(mapping, form, request, response, sid, fileName);
			return null;
		} else
          result = fm.getReportResult();
//		System.out.println(reportResult);
//		System.out.println("=====================================");
//		HTMLParser parser =  new HTMLParser(reportResult);
//		String result = parser.getNewHTML();
//		System.out.println(result);
		request.setAttribute("reportResult", result);
		request.setAttribute("fileName", fileName);

		return mapping.findForward("success");
	}
	
	public ActionForward downloadFile(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, String type) {
		response.setContentType("text/xml; charset=big5");
		String fileName = type + ".xls";
		StringBuffer s = null;
		Connection conn = null;
		File f = null;
		OutputStream os = null;
		FileInputStream fis = null;
		try {
			conn = DBConnection.getConnection();
			XLSForm xls = new XLSForm();
			xls.setFileName(type);
			HashMap sheets = new HashMap();
			String[] mode = {"0","1"};
			HashMap[] data1 = {};
			for(int i = 0 ; i < 2; i++) {
				s = new StringBuffer(
						"\n" +
								"SELECT T3.SEQ,\n" + 
								"       T3.FACILITY,\n" + 
								"       T1.BRAND,\n" + 
								"       T3.BRAND PRODLEVEL, \n" +
								"       T1.VERSION,\n" + 
								"       T3.PRODUCT_CODE,\n" + 
								"       T3.BRAND,\n" + 
								"       T3.TEST_MODE,\n" + 
								"       T3.LOWER_LIMIT,\n" + 
								"       T3.FLAG1,\n" + 
								"       T3.UPPER_LIMIT,\n" + 
								"       T3.FLAG2,\n" + 
								"       T3.ITEM_TYPE,\n" + 
								"       T3.ITEM,\n" + 
								"       T3.ITEM_MODE2,\n" + 
								"       T3.ITEM_BINS2,\n" + 
								"       T3.ACTION,\n" + 
								"       T3.CHANGE_IPN,\n" + 
								"       T3.ROUTE_NAME,\n" + 
								"       T3.START_STEP,\n" + 
								"       T3.REMARK,\n" + 
								"       T3.DG_ACTION,\n" + 
								"       T3.ITEM_SEQ,\n" + 
								"       T3.BY_LOT_DG,\n" + 
								"       T3.DGRADE_SPECIAL_IPN\n" + 
								"  FROM TF_INFORMATION T1, TF_CURRENT_VERSION_VW T2, TF_YIELD_DEFINITION T3\n" + 
								" WHERE T1.SID = T2.SID\n" + 
								"   AND T1.PRODUCT_TYPE = '" + type + "'" + 
								"   AND T1.SID = T3.SID\n" + 
								"   AND T3.FACILITY = "+mode[i]+"\n" + 
								" ORDER BY T3.PRODUCT_CODE, T1.BRAND");
				data1 = DB.qryLinkedHashMapBySql(conn, s.toString(), new String[] {});
				sheets.put(mode[i].equals("0") ? "CP" : "FT", data1);
			}
			
			/*AP(M)_New #37953: Req_201900055 : e8049?出功能新增BOM表,CP程式及FT程式*/
			String[] mode1 = {"WS","FT"};
			for(int i1 = 0 ; i1 < 2; i1++) {
				s = new StringBuffer(
						"\n" +
                        "select T3.*\n" +
                        "  FROM TF_INFORMATION T1, TF_CURRENT_VERSION_VW T2, TF_TEST_PARAMETER_"+mode1[i1]+" T3\n" + 
                        " WHERE T1.SID = T2.SID\n" + 
                        "   AND T1.PRODUCT_TYPE = '" + type + "'\n" + 
                        "   AND T1.SID = T3.SID\n" + 
                        " ORDER BY T3.PRODUCT_BODY, T1.BRAND");
				//System.out.println(s.toString());
				data1 = DB.qryLinkedHashMapBySql(conn, s.toString(), new String[] {});
				sheets.put(mode1[i1].equals("WS") ? "CP_PG" : "FT_PG", data1);
			}

			String b = type.equals("NVM") ? "" : "_"+type;
			s = new StringBuffer(
                    "select T3.*\n" +
                    " FROM TF_INFORMATION T1, TF_CURRENT_VERSION_VW T2, TF_BOM_ROUTE" + b + " T3\n" + 
                    "WHERE T1.SID = T2.SID\n" + 
                    "  AND T1.PRODUCT_TYPE = '" + type + "'\n" + 
                    "  AND T1.SID = T3.SID\n" + 
                    "ORDER BY T3.PRODUCT_BODY, T1.BRAND");
			//System.out.println(s.toString());
			data1 = DB.qryLinkedHashMapBySql(conn, s.toString(), new String[] {});
			sheets.put("BOM_SCP", data1);
		
			s = new StringBuffer(
                    "select T3.*\n" +
                    " FROM TF_INFORMATION T1, TF_CURRENT_VERSION_VW T2, TF_BOM_ROUTE_MCP T3\n" + 
                    "WHERE T1.SID = T2.SID\n" + 
                    "  AND T1.PRODUCT_TYPE = '" + type + "'\n" + 
                    "  AND T1.SID = T3.SID\n" + 
                    "ORDER BY T3.PRODUCT_BODY, T1.BRAND");
			//System.out.println(s.toString());
			data1 = DB.qryLinkedHashMapBySql(conn, s.toString(), new String[] {});
			sheets.put("BOM_MCP", data1);
			
			xls.setSheets(sheets);
            /*這裡不要再判斷data.length內容，因為rom 不會有bom_mcp，不然都不會進行createXls*/
			xls.createXls();
			f = new File(xls.getFullFileName());

			fis = new FileInputStream(f.getPath());
			if (fis == null) {
				response.setContentType("text/html; charset=big5");
				java.io.PrintWriter out = response.getWriter();
				out.print("<script>javascript:alert('檔案不存在');history.back();</script>");
				out.flush();
				return null;
			}
			if (fileName.endsWith(".xls")) {
				response.setContentType("application/vnd.ms-excel");
			} else if (fileName.endsWith(".pdf")) {
				response.setContentType("application/pdf");
			} else {
				response.setContentType("application/octect-stream");
			}
			fileName = new String(fileName.getBytes("big5"), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			os = response.getOutputStream();
			int byteRead;
			while (-1 != (byteRead = fis.read())) {
				os.write(byteRead);
			}
			if (fis != null) {
				fis.close();
			}
			os.close();
			response.setStatus(response.SC_OK);
			response.flushBuffer();
		} catch (Exception e) {
			request.setAttribute("message", "Error:" + e.getMessage().replaceAll("\n", "").replaceAll("\\\\", "\\\\\\\\"));
			return actionMapping.findForward("msg");
		} finally {
			try {
				DBConnection.close(conn);
				if (fis != null)
					fis.close();
				if (os != null)
					os = null;
				if (f.exists())
					f.delete();
			} catch (Exception e) {
				e.fillInStackTrace();
				TDSLogger.println(e.getMessage());
			}
		}
		return null;
	}
	
	public ActionForward downloadFilePG(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, String time, String status) {
		response.setContentType("text/xml; charset=big5");
		String fileName = "Program.xls";
		StringBuffer s = new StringBuffer("select * from pg_test_program p where 1 = 1 \n");
		Connection conn = null;
		File f = null;
		OutputStream os = null;
		FileInputStream fis = null;
		try {
			conn = DBConnection.getConnection();
			XLSForm xls = new XLSForm();
			xls.setFileName("Program");
			HashMap sheets = new HashMap();
			HashMap[] data1 = {};
			
			if(status != null && status.equals("ALL")){
				if(time != null && time.length() > 0) {
					String st = time.split("-")[0];
					String et = time.split("-")[1];
					s.append("and p.apply_date < to_date('"+et+" 00:00:00','yyyymmdd hh24:mi:ss') \n");	
					s.append("and p.apply_date > to_date('"+st+" 00:00:00','yyyymmdd hh24:mi:ss') \n");
				}
				data1 = DB.qryLinkedHashMapBySql(conn, s.toString(), new String[] {});
				for (int i = 0; i < data1.length; i++) {
					String ss = Util.transStatus_new((String) data1[i].get("PROGRAM_STATUS"));
					data1[i].put("PROGRAM_STATUS", ss);
				}
				sheets.put("Program", data1);
				xls.setSheets(sheets);
			}else{
				s.append("and p.program_status not in (55,60) \n");
				
				data1 = DB.qryLinkedHashMapBySql(conn, s.toString()+" and p.program_mode = 'ENG' AND P.TEST_MODE LIKE 'S%' ", new String[] {});
				for (int i = 0; i < data1.length; i++) {
					String ss = Util.transStatus_new((String) data1[i].get("PROGRAM_STATUS"));
					data1[i].put("PROGRAM_STATUS", ss);
				}
				sheets.put("ENG_WS", data1);
				
				data1 = DB.qryLinkedHashMapBySql(conn, s.toString()+" and p.program_mode = 'ENG' AND P.TEST_MODE LIKE 'FT%' ", new String[] {});
				for (int i = 0; i < data1.length; i++) {
					String ss = Util.transStatus_new((String) data1[i].get("PROGRAM_STATUS"));
					data1[i].put("PROGRAM_STATUS", ss);
				}
				sheets.put("ENG_FT", data1);
				
				data1 = DB.qryLinkedHashMapBySql(conn, s.toString()+" and p.program_mode = 'ERUN' ", new String[] {});
				for (int i = 0; i < data1.length; i++) {
					String ss = Util.transStatus_new((String) data1[i].get("PROGRAM_STATUS"));
					data1[i].put("PROGRAM_STATUS", ss);
				}
				sheets.put("ERUN", data1);
				
				data1 = DB.qryLinkedHashMapBySql(conn, s.toString()+" and p.program_mode = 'PROD' AND P.TEST_MODE LIKE 'S%' ", new String[] {});
				for (int i = 0; i < data1.length; i++) {
					String ss = Util.transStatus_new((String) data1[i].get("PROGRAM_STATUS"));
					data1[i].put("PROGRAM_STATUS", ss);
				}
				sheets.put("PROD_WS", data1);
				
				data1 = DB.qryLinkedHashMapBySql(conn, s.toString()+" and p.program_mode = 'PROD' AND P.TEST_MODE LIKE 'FT%' ", new String[] {});
				for (int i = 0; i < data1.length; i++) {
					String ss = Util.transStatus_new((String) data1[i].get("PROGRAM_STATUS"));
					data1[i].put("PROGRAM_STATUS", ss);
				}
				sheets.put("PROD_FT", data1);
			}

			xls.setSheets(sheets);
			if(data1.length != 0 ) {
				xls.createXls();
				f = new File(xls.getFullFileName());
			}

			
			fis = new FileInputStream(f.getPath());
			if (fis == null) {
				response.setContentType("text/html; charset=big5");
				java.io.PrintWriter out = response.getWriter();
				out.print("<script>javascript:alert('檔案不存在');history.back();</script>");
				out.flush();
				return null;
			}
			if (fileName.endsWith(".xls")) {
				response.setContentType("application/vnd.ms-excel");
			} else if (fileName.endsWith(".pdf")) {
				response.setContentType("application/pdf");
			} else {
				response.setContentType("application/octect-stream");
			}
			fileName = new String(fileName.getBytes("big5"), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			os = response.getOutputStream();
			int byteRead;
			while (-1 != (byteRead = fis.read())) {
				os.write(byteRead);
			}
			if (fis != null) {
				fis.close();
			}
			os.close();
			response.setStatus(response.SC_OK);
			response.flushBuffer();
		} catch (Exception e) {
			request.setAttribute("message", "Error:" + e.getMessage().replaceAll("\n", "").replaceAll("\\\\", "\\\\\\\\"));
			return actionMapping.findForward("msg");
		} finally {
			try {
				DBConnection.close(conn);
				if (fis != null)
					fis.close();
				if (os != null)
					os = null;
				if (f.exists())
					f.delete();
			} catch (Exception e) {
				e.fillInStackTrace();
				TDSLogger.println(e.getMessage());
			}
		}
		return null;
	}
	
}
