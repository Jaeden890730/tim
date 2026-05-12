package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class DeleteBomReRouteTxAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestReRouteBeanAF proTestReRouteBeanAF = (ProTestReRouteBeanAF) actionForm;
    String id= Request.getParameter("id");
    String sid = Request.getParameter("sid");
    try{
      OiMaintainService.DeleteBomReRouteTx(id);
      com.mxic.oiplus.oimaintain.OiMaintainService.submit(sid, "TF_BOM_REROUTE", "N");
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath()+"/OImaintain/bomProductReRouteActionX.do?delete=Y&sid="+sid));
    } catch(Exception e) {
      e.printStackTrace();
    }
    //return actionMapping.findForward("DeleteBomRouteTx");
    return null;
  }
}
