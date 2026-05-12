package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.IOException;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class saveMainRouteSubAction extends Action {

	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm,
			HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) {

		MainRouteSubForm fm = (MainRouteSubForm) actionForm;
		servletRequest.setAttribute("mainRouteSubBeanX",fm);

		String listControl = fm.getListControl();

		if (listControl.equals("update_data") ||
				listControl.equals("submit_data")) {
			String[] sid = servletRequest.getParameterValues("sid");
			String[] pd_body = servletRequest.getParameterValues("product_body");
			String[] version = servletRequest.getParameterValues("version");
			String[] route_type = servletRequest.getParameterValues("route_type");
			String[] main_route = servletRequest.getParameterValues("main_route");
			String[] map_route = servletRequest.getParameterValues("map_route");
			String[] remark = servletRequest.getParameterValues("remark");
			if (listControl.equals("update_data")) {
				MainRouteService.update_data(sid, pd_body, version, route_type,
						main_route, map_route, remark, "update_cmd","0");

			} else if (listControl.equals("submit_data")) {
				MainRouteService.update_data(sid, pd_body, version, route_type,
						main_route, map_route, remark, "submit_cmd","0");

			}
			try {
				servletResponse.sendRedirect(servletResponse.encodeRedirectURL(servletRequest.getContextPath() + "/OImaintain/mainRouteSubActionX.do?sid="+fm.getSid()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//return actionMapping.findForward("update_data");
			return null;
		} else if (listControl.equals("delete_row")) {
			String record_id = fm.getRecord_id();
			int sid = fm.getSid();
			String pd_body = fm.getProduct_body();
			String brand = " ";
			String version = fm.getVersion();
                        String route_type = Integer.toString(fm.getRoute_type());
			MainRouteService.delete_row(record_id, sid, route_type, version, pd_body);
			try {
				servletResponse.sendRedirect(servletResponse.encodeRedirectURL(servletRequest.getContextPath() + "/OImaintain/mainRouteSubActionX.do?sid="+fm.getSid()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//return actionMapping.findForward("update_data");
			return null;
		} else if (listControl.equals("reset_tx")) {
			int sid = fm.getSid();
			String pd_body = fm.getProduct_body();
			String brand = " ";
			String version = fm.getVersion();
                        String route_type = Integer.toString(fm.getRoute_type());
			MainRouteService.reset_tx(sid);
			try {
				servletResponse.sendRedirect(servletResponse.encodeRedirectURL(servletRequest.getContextPath() + "/OImaintain/mainRouteSubActionX.do?sid="+fm.getSid()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//return actionMapping.findForward("update_data");
			return null;
		} else {
			return actionMapping.findForward("fail");
		}

	}

}
