package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class ResetBomRouteMcpAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {
    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String sid=Request.getParameter("sid");
    try{
      OiMaintainService.ResetBomRouteMcpTx(sid);
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath()+"/OImaintain/bomProductRouteMcpAction.do?sid="+sid));
    } catch(Exception e){
      e.printStackTrace();
    }
    //return actionMapping.findForward("ResetBomRoute");
    return null;
  }
}
