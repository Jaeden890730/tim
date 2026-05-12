package com.mxic.oiplus.oimaintain;

import java.io.IOException;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class SubmitProductTestAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String sid=Request.getParameter("sid");
    try{
      OiMaintainService.submitProductTest(sid);
      Request.setAttribute(actionMapping.getName(),fm);
      
      //HttpSession session = Request.getSession();
  	  //session.setAttribute("sid", sid);
  		servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/productRouteAction.do?sid="+sid));
    } catch (Exception e){
      e.printStackTrace();
    }
    //return actionMapping.findForward("TR");
    return null;
  }
}
