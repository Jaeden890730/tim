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

public class UserAccountAction extends Action{

	/**
	 * This is the method called on by ActionServlet
	 * when a request is made.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	      UserAccountForm fm = (UserAccountForm)form;
	      String forward = "";

	      int return_flag = 100;
	      if(fm.getAction_type().equals("insert")){
		  return_flag = AUService.insertUserAccount(fm);
		  forward = "insert";
	      }else if(fm.getAction_type().equals("delete")){
		  AUService.deleteUserActionByCrit(fm.getUser_id(),"");
		  AUService.deleteUserGroupByCrit(fm.getUser_id(),"");
		  AUService.deleteUserAccount(fm);
		  forward = "delete";
	      }else if(fm.getAction_type().equals("update")){
		  return_flag = AUService.updateUserAccount(fm);
		  forward = "update";
	      }else if(request.getParameter("action_type").equals("edit")){
		  fm.setUser_id(request.getParameter("user_id"));
		  fm = AUService.getUserAccountByCrit(fm)[0];
		  forward = "edit";
	      }

	      fm.setReturn_flag(MessageDef.returnMessage(return_flag));
	      request.setAttribute(mapping.getName(),fm);
	      return mapping.findForward(forward);
	}
}
