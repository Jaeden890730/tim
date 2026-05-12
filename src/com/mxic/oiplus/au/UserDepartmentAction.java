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

public class UserDepartmentAction extends Action{

	/**
	 * This is the method called on by ActionServlet
	 * when a request is made.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	      UserDepartmentForm fm = (UserDepartmentForm)form;
	      String forward = "";
	      int return_flag = 100;
	      if(fm.getAction_type().equals("insert")){
		  return_flag = AUService.insertUserDepartment(fm);
		  forward = "insert";
	      }else if(fm.getAction_type().equals("delete")){
		  AUService.deleteUserDepartment(fm);
		  forward = "delete";
	      }else if(fm.getAction_type().equals("update")){
		  return_flag = AUService.updateUserDepartment(fm);
		  forward = "update";
	      }else if(request.getParameter("action_type").equals("edit")){
		  fm.setDept_id(request.getParameter("dept_id"));
		  fm = AUService.getUserDepartmentByID(fm.getDept_id());
		  forward = "edit";
	      }

	      fm.setReturn_flag(MessageDef.returnMessage(return_flag));
	      request.setAttribute(mapping.getName(),fm);
	      return mapping.findForward(forward);
	}
}
