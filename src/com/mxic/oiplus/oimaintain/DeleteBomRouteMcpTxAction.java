package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class DeleteBomRouteMcpTxAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF proTestRouteBeanAF = (ProTestRouteBeanAF) actionForm;
    String id= Request.getParameter("id");
    String sid= Request.getParameter("sid");
    try{
      OiMaintainService.DeleteBomRouteMcpTx(id,sid);
      OiMaintainService.submit(sid, "TF_BOM_MCP_ROUTE", "N");
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath()+"/OImaintain/bomProductRouteMcpAction.do?delete=Y&sid="+sid));
    } catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
