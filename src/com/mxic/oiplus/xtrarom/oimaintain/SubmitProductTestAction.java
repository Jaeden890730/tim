package com.mxic.oiplus.xtrarom.oimaintain;

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
    } catch (Exception e){
      e.printStackTrace();
    }
    return actionMapping.findForward("TR");
  }
}
