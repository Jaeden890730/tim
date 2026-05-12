package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class DuplicateRowReAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestReRouteBeanAF fm = (ProTestReRouteBeanAF) actionForm;
    String id=Request.getParameter("id");
    BomProductReRouteBean[] bom=OiMaintainService.SearchBomReByID(id);
    String maxReRouteCode = OiMaintainService.getMaxReRouteCode(bom[0].getSid());
    bom[0].setRecycle_code(OiMaintainService.getNextRouteCode(maxReRouteCode));
    if (bom != null && bom.length > 0) {
      fm.setProductbody(bom[0].getProductbody());
      fm.setBrand("MX");//bom[0].getBrand()
      fm.setSid(bom[0].getSid());
      fm.setVersion(bom[0].getVersion());
    }

    Request.setAttribute(actionMapping.getName(),fm);
    Request.setAttribute("DupRow",bom);
    return actionMapping.findForward("DupRow");
  }
}
