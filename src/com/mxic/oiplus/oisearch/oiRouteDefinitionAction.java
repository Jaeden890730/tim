package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.au.User;
import com.mxic.oiplus.util.StringUtil;

public class oiRouteDefinitionAction extends Action {
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

		User user = (User) servletRequest.getSession().getAttribute("user");
		String listControl = servletRequest.getParameter("listControl");
		String form_no = servletRequest.getParameter("formno");
		String ra_select = servletRequest.getParameter("raSelect");
		String[] item = (ra_select + ";EOF").split(";");
		String forward = "";

		if (listControl.equals("ro_update") || listControl.equals("ro_insert") || listControl.equals("ro_delete")) {
			TFRouteDefinitionBeanAF btupdate = new TFRouteDefinitionBeanAF();
			btupdate.setUpdater(user.getEmpNo());
			if (!btupdate.getAuthorityUser()) {
				servletRequest.setAttribute("message", "auth fail !!");
				return actionMapping.findForward("ro_back");
			}
		}

		if (listControl.equals("ckproduct")) {
			TFRouteDefinitionBeanAF routeSearchForm = (TFRouteDefinitionBeanAF) actionForm;
			String product_body = routeSearchForm.getProduct_body();
			TFRouteDefinitionBeanAF[] btsearch = oiSearchService.SelectRouteOption(product_body);
			TFRouteDefinitionBeanAF[] btsearch2 = oiSearchService.SelectAvailableRouteOption(product_body, user.getUserName());
			servletRequest.setAttribute("list", btsearch);
			servletRequest.setAttribute("list2", btsearch2);
			servletRequest.setAttribute("product_body", product_body);
			forward = "RouteDefintionList";
		} else if (listControl.equals("ro_update")) {
			TFRouteDefinitionBeanAF btupdate = new TFRouteDefinitionBeanAF();
			btupdate.setFt_route(item[0]);
			btupdate.setFt_route_add(item[1]);
			btupdate.setWs_route(item[2]);
			btupdate.setWs_route_add(item[3]);
			btupdate.setRoute_option(item[4]);
			btupdate.setRoute_option_description(StringUtil.Utf8ToBig5(item[5]));
			servletRequest.setAttribute("bean", btupdate);
			servletRequest.setAttribute("bean_type", "ro_update");
			forward = "ro_update";
		} else if (listControl.equals("ro_insert")) {
			TFRouteDefinitionBeanAF btupdate = new TFRouteDefinitionBeanAF();
			servletRequest.setAttribute("bean", btupdate);
			servletRequest.setAttribute("bean_type", "ro_insert");
			forward = "ro_insert";
		} else if (listControl.equals("ro_delete")) {
			String ro_delete = oiSearchService.deleteRouteOption(item);
			if ("true".equals(ro_delete)) {
				servletRequest.setAttribute("message", "Delete success !!");
				oiSearchService.insertRouteOptionLog(item[4],user.getEmpNo(),form_no+"~DELETE", item);
			} else {
				servletRequest.setAttribute("message", "Error while deleting !! " + ro_delete);
			}
			forward = "ro_delete";
		} else if (listControl.equals("ro_update_bean")) {
			TFRouteDefinitionBeanAF tfControlForm = (TFRouteDefinitionBeanAF) actionForm;
			String ro_update = oiSearchService.updateRouteOption(tfControlForm);
			if ("true".equals(ro_update)) {
				servletRequest.setAttribute("message", "Update success !!");
				oiSearchService.insertRouteOptionLog(tfControlForm.getRoute_option(),user.getEmpNo(),form_no+"~UPDATE",tfControlForm);
			} else {
				servletRequest.setAttribute("message", "Error while updating data !! " + ro_update);
			}
			forward = "ro_update_bean";
		} else if (listControl.equals("ro_insert_bean")) {
			TFRouteDefinitionBeanAF tfControlForm = (TFRouteDefinitionBeanAF) actionForm;
			String ro_insert = oiSearchService.insertRouteOption(tfControlForm);
			if ("true".equals(ro_insert)) {
				servletRequest.setAttribute("message", "Insert success !!");
				oiSearchService.insertRouteOptionLog(tfControlForm.getRoute_option(),user.getEmpNo(),form_no+"~INSERT",tfControlForm);
			} else {
				servletRequest.setAttribute("message", "Error while inserting data !! " + ro_insert);
			}
			forward = "ro_insert_bean";
		}  else if (listControl.equals("ro_back")) {
			forward = "ro_back";
		}

		return actionMapping.findForward(forward);
	}
}
