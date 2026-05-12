package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class DelWSTestParamAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    //String id = Request.getParameter("id");
    String pgm_id = Request.getParameter("pgm_id");
    String sid = Request.getParameter("sid");
    try {
      OiMaintainService.DeleteWSParameter(sid,pgm_id);
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/wsTestParameterActionX.do?sid="+sid));
    } catch (Exception e) {
      e.printStackTrace();
    }
    //return actionMapping.findForward("DeleteWS");
    return null;
  }
}
