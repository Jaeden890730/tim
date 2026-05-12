package com.mxic.oiplus.au;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import javax.servlet.http.*;

import org.apache.struts.action.*;
import com.mxic.oiplus.util.*;

public class AUAssignAction extends Action{

	/**
	 * This is the method called on by ActionServlet
	 * when a request is made.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		AUAssignForm fm = (AUAssignForm)form;
		String forward = "";
		int return_flag = 100;
		if(fm.getAction_type().equals("group_user")){
			AUService.deleteUserGroupByCrit(fm.getUser_id(),"");
			return_flag = AUService.insertUserGroup(fm);
			forward = "group_user";
		}if(fm.getAction_type().equals("action_user")){
			AUAssignForm afm = new AUAssignForm();
			AUService.deleteUserActionByCrit(fm.getUser_id(),"");
			return_flag = AUService.insertUserAction(fm);
			UserAccountForm uafm = AUService.getUserAccountByID(request.getParameter("user_id"));
			AUACTForm[] actions = AUService.getUserAction(request.getParameter("user_id"));
			AUGroupForm[] grp = AUService.getUserGroupBySid(request.getParameter("user_id"));
			afm.setUser_id(uafm.getUser_id());
			afm.setUser_name(uafm.getUser_name());
			request.setAttribute("AUAssignForm",afm);
			request.setAttribute("ActionList", actions);
			request.setAttribute("userGroup", grp);
			forward = "action_user";
		}if(fm.getAction_type().equals("action_group")){
			AUService.deleteGroupActionByCrit(fm.getGrp_id(),"");
			return_flag = AUService.insertGroupAction(fm);
			forward = "action_group";
		}

		fm.setReturn_flag(MessageDef.returnMessage(return_flag));
		request.setAttribute(mapping.getName(),fm);
		return mapping.findForward(forward);
	}
}
