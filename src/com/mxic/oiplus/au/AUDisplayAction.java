package com.mxic.oiplus.au;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AUDisplayAction extends Action{

	/**
	 * This is the method called on by ActionServlet
	 * when a request is made.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	      AUDisplayForm fm = (AUDisplayForm)form;
	      String forward = "";
	      if(fm.getAction_type().equals("user_list")){
		    fm.setUser_account_list(AUService.getUserAccountByCrit(fm));
		    forward = "user_list";
	      }else if(fm.getAction_type().equals("act_list")){
		    fm.setAu_act_list(AUService.getAUACTByCrit(fm));
		    forward = "act_list";
	      }else if(fm.getAction_type().equals("group_list")){
		    fm.setAu_group_list(AUService.getAUGroupByCrit(fm));
		    forward = "group_list";
	      }else if(fm.getAction_type().equals("dept_list")){
		    fm.setUser_department_list(AUService.getUserDepartmentByCrit(fm));
		    forward = "dept_list";
	      }else if(request.getParameter("action_type").equals("group_user")){
		    AUAssignForm afm = new AUAssignForm();
		    UserAccountForm uafm = AUService.getUserAccountByID(request.getParameter("user_id"));
		    afm.setUser_id(uafm.getUser_id());
		    afm.setUser_name(uafm.getUser_name());
		    request.setAttribute("AUAssignForm",afm);
		    forward = "group_user";
	      }else if(request.getParameter("action_type").equals("action_user")){
		    AUAssignForm afm = new AUAssignForm();
		    UserAccountForm uafm = AUService.getUserAccountByID(request.getParameter("user_id"));
		    AUACTForm[] actions = AUService.getUserAction(request.getParameter("user_id"));
		    AUGroupForm[] grp = AUService.getUserGroupBySid(request.getParameter("user_id"));
		    afm.setUser_id(uafm.getUser_id());
		    afm.setUser_name(uafm.getUser_name());
		    request.setAttribute("AUAssignForm",afm);
		    request.setAttribute("ActionList", actions);
		    request.setAttribute("userGroup", grp);
		    forward = "action_user";
	      }else if(request.getParameter("action_type").equals("action_group")){
		    if (request.getParameter("displayonly") != null)
		      if (request.getParameter("displayonly").equals("true"))
		        request.setAttribute("displayonly", "true");
		    AUAssignForm afm = new AUAssignForm();
		    AUGroupForm augfm = AUService.getAUGroupByID(request.getParameter("grp_id"));
		    afm.setGrp_id(augfm.getGrp_sid());
		    afm.setGrp_name(augfm.getGrp_name());
		    request.setAttribute("AUAssignForm",afm);
		    forward = "action_group";
	      }
	      return mapping.findForward(forward);
	}
}
