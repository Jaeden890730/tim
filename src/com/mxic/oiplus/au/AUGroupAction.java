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

public class AUGroupAction extends Action{

	/**
	 * This is the method called on by ActionServlet
	 * when a request is made.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	      AUGroupForm fm = (AUGroupForm)form;
	      String forward = "";
	      int return_flag = 100;
	      if(fm.getAction_type().equals("insert")){
		  return_flag = AUService.insertAUGroup(fm);
		  forward = "insert";
	      }else if(request.getParameter("action_type").equals("delete")){
		  fm.setGrp_sid(request.getParameter("grp_sid"));
		  AUService.deleteGroupActionByCrit(fm.getGrp_sid(),"");
		  AUService.deleteUserGroupByCrit("",fm.getGrp_sid());
		  AUService.deleteAUGroup(fm);
		  forward = "delete";
	      }else if(fm.getAction_type().equals("update")){
		  return_flag = AUService.updateAUGroup(fm);
		  forward = "update";
	      }else if(request.getParameter("action_type").equals("edit")){
		  fm.setGrp_sid(request.getParameter("grp_sid"));
		  fm = AUService.getAUGroupByCrit(fm)[0];
		  forward = "edit";
	      }

	      fm.setReturn_flag(MessageDef.returnMessage(return_flag));
	      request.setAttribute(mapping.getName(),fm);
	      return mapping.findForward(forward);
	}
}
