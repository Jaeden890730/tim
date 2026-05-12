package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.xtrarom.oimaintain.OiMaintainService;

public class expireBomRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

//    ProTestRouteBeanAF proTestRouteBeanAF = (ProTestRouteBeanAF) actionForm;
    String id= Request.getParameter("id");
    String flag = Request.getParameter("flag");
    String sid = Request.getParameter("sid");
    try{
      OiMaintainService.setBomRouteExpire(id, flag);
      com.mxic.oiplus.oimaintain.OiMaintainService.submit(sid, "TF_BOM_ROUTE", "N");
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/bomProductRouteActionX.do?sid="+sid));
    } catch(Exception e) {
      e.printStackTrace();
    }
    //return actionMapping.findForward("expireBomRoute");
    return null;
  }
}
