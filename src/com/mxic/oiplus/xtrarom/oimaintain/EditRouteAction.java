package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class EditRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest request,
                               HttpServletResponse servletResponse) {
    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;

    String RN= request.getParameter("RN");
    String sid=request.getParameter("sid");
    ProTestRouteBean[] pm = OiMaintainService.GetRouteStep(sid,RN);
    fm.setRoutename(RN);
    String productType = OiMaintainService.getProductType(fm.getSid());

    request.setAttribute(actionMapping.getName(),fm);
    request.setAttribute("EditRoute",pm);
    request.setAttribute("productType", productType);
    return actionMapping.findForward("EditRoute");
  }
}
