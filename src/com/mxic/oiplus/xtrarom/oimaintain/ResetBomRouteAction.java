package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class ResetBomRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {
    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String sid=Request.getParameter("sid");
    try{
      OiMaintainService.ResetBomRouteTx(sid);
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath()+"/OImaintain/bomProductRouteActionX.do?sid="+sid));
    } catch(Exception e){
      e.printStackTrace();
    }
    //return actionMapping.findForward("ResetBomRoute");
    return null;
  }
}
