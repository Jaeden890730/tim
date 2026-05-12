package com.mxic.oiplus.oimaintain;

import java.io.IOException;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class DelRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String sid = Request.getParameter("sid");
    String RN = Request.getParameter("RN");
    try {
      OiMaintainService.DeleteRoute(sid, RN);
//      ProTestRouteBean[] ptrb=OiMaintainService.GetRoute(sid);
    } catch(Exception e) {
      e.printStackTrace();
    }
    String productType = OiMaintainService.getProductType(sid);
    Request.setAttribute(actionMapping.getName(),fm);
    
    //HttpSession session = Request.getSession();
	//session.setAttribute("sid", sid);
    try {
		servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/productRouteAction.do?sid="+sid));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    //return actionMapping.findForward("DelRoute"+productType);
    return null;
  }
}
