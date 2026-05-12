package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.StringUtil;
import com.mxic.oiplus.util.TDSLogger;

public class addMainRouteSubAction extends Action {
  public addMainRouteSubAction() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    addMainRouteSubForm fm = (addMainRouteSubForm) actionForm;
    Connection conn;
    StringBuffer message = new StringBuffer();
    boolean errorFlag = false;
    StringBuffer insSql = new StringBuffer();

	try {
		conn = DBConnection.getConnection();
		conn.setAutoCommit(true);
	} catch (Exception e) {
		TDSLogger.println(e);
		return actionMapping.findForward("failure");
	}

    int sid = fm.getSid();
    String product_body = fm.getProduct_body();
    //String brand = " ";
    String version = fm.getVersion();
    String route_type = fm.getRoute_type();
    String main_route[] = fm.getMain_route();
    String map_route[] = fm.getMap_route();
    String remark[] = fm.getRemark();

	String v_main_route = null;
	String v_map_route = null;
	String v_remark = null;

    if (main_route == null){
    	try {
    		servletResponse.sendRedirect(servletResponse.encodeRedirectURL(servletRequest.getContextPath() + "/OImaintain/mainRouteSubActionX.do?sid="+fm.getSid()));
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	//return actionMapping.findForward("save");
    	return null;
    }	
    for (int i=0; i<main_route.length; i++) {
    	try {
    		v_main_route = main_route[i];
    		v_map_route = map_route[i];
    		v_remark = remark[i];

    		StringBuffer sql = new StringBuffer();
    		sql.append(
    				"SELECT count(*) as total_count FROM tf_main_route_xrom_tx where sid='" + sid +
    				"' and main_route='" + v_main_route +
    				"' and product_body='" + product_body +
    				"' and version='" + version +
                                "' and route_type='" + route_type +
    				"' and map_route='" + v_map_route + "' ");

    		PreparedStatement ps = conn.prepareStatement(sql.toString());
    		ResultSet rs = ps.executeQuery();
    		while (rs.next()) {
    			if (rs.getInt("total_count") == 0) {
    				insSql = new StringBuffer();
    				insSql.append("Insert into tf_main_route_xrom_tx ");
    				insSql.append("(sid,tag,route_type,product_body,version,main_route,map_route,remark) ");
    				insSql.append("values (?,?,?,?,?,?,?,?) ");
    				PreparedStatement ps_insert = conn.prepareStatement(insSql.toString());
    				ps_insert.setInt(1, sid);
    				ps_insert.setString(2, "1");
    				ps_insert.setInt(3, Integer.parseInt(route_type));
    				ps_insert.setString(4, product_body);
    				ps_insert.setInt(5, Integer.parseInt(version));
    				if (v_main_route == null || v_main_route.equals("")) {
    					ps_insert.setString(6, " ");
    				} else {
    					ps_insert.setString(6, v_main_route);
    				}
    				if (v_map_route == null || v_map_route.equals("")) {
    					ps_insert.setString(7, " ");
    				} else {
    					ps_insert.setString(7, v_map_route);
    				}
    				if (v_remark == null || v_remark.equals("")) {
    					ps_insert.setString(8, " ");
    				} else {
    					ps_insert.setString(8, StringUtil.Utf8ToBig5(v_remark));
    				}

//    				System.out.println(ps_insert.toString());
    				ps_insert.executeUpdate();
    			}
    		}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		DBConnection.rollback(conn);
    		TDSLogger.println(ex.getMessage());
    		errorFlag = true;
    		message.append("Insert fail: " + MainRouteService.toString( product_body,  version, route_type, v_main_route, v_map_route, v_remark) + "\n");
    	}
    }
	DBConnection.close(conn);
	if (errorFlag)
		return actionMapping.findForward("failure");
	servletRequest.setAttribute("sid", Integer.toString(fm.getSid()));
	ActionRedirect redirect = new ActionRedirect(actionMapping.findForward("save"));

	redirect.addParameter("sid", new Integer(fm.getSid()));

    //return actionMapping.findForward("save");
	try {
		servletResponse.sendRedirect(servletResponse.encodeRedirectURL(servletRequest.getContextPath() + "/OImaintain/mainRouteSubActionX.do?sid="+fm.getSid()));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//return redirect;
	return null;
  }

  private void jbInit() throws Exception {
  }
}
