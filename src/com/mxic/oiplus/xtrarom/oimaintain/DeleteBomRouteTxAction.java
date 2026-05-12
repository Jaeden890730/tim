package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class DeleteBomRouteTxAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF proTestRouteBeanAF = (ProTestRouteBeanAF) actionForm;
    String id= Request.getParameter("id");
    String sid = Request.getParameter("sid");
    try{
      OiMaintainService.DeleteBomRouteTx(id);
      com.mxic.oiplus.oimaintain.OiMaintainService.submit(sid, "TF_BOM_ROUTE", "N");
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath()+"/OImaintain/bomProductRouteActionX.do?delete=Y&sid="+sid));

    } catch(Exception e) {
      e.printStackTrace();
    }
    return null;//actionMapping.findForward("DeleteBomRouteTx");
  }
}
