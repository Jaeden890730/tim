package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class ResetBomReRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {
    ProTestReRouteBeanAF fm = (ProTestReRouteBeanAF) actionForm;
    String sid=Request.getParameter("sid");
    try{
      OiMaintainService.ResetBomReRouteTx(sid);
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath()+"/OImaintain/bomProductReRouteActionX.do?sid="+sid));
    } catch(Exception e){
      e.printStackTrace();
    }
    //return actionMapping.findForward("ResetBomRoute");
    return null;
  }
}
