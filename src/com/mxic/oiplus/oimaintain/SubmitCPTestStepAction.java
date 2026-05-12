package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class SubmitCPTestStepAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String sid=Request.getParameter("sid");
    try{
      OiMaintainService.submitX(sid,"TF_CP_TEST_STEP");
      Request.setAttribute(actionMapping.getName(),fm);
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/testStepAction.do?sid="+sid));
    } catch (Exception e){
      e.printStackTrace();
    }
    //return actionMapping.findForward("TR");
    return null;
  }
}
