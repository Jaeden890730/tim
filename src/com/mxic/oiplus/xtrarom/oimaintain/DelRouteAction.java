package com.mxic.oiplus.xtrarom.oimaintain;

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
      ProTestRouteBean[] ptrb=OiMaintainService.GetRoute(sid);
    } catch(Exception e) {
      e.printStackTrace();
    }
    Request.setAttribute(actionMapping.getName(),fm);
    return actionMapping.findForward("DelRoute");
  }
}
