package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class DuplicateRowAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String id=Request.getParameter("id");
    BomProductRouteBean[] bom=OiMaintainService.SearchBomByID(id);
    String maxFTRouteCode = OiMaintainService.getMaxFTRouteCode(bom[0].getSid());
    bom[0].setFt_route_code(OiMaintainService.getNextRouteCode(maxFTRouteCode));
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
