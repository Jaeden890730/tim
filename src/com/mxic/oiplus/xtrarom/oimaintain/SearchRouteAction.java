package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class SearchRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String routename = Request.getParameter("txtRoutename").toUpperCase();
    String sid = Request.getParameter("sid");
    String forward = null;

    if (routename.equals("ALL")){
      RouteNameBean[] rnb=OiMaintainService.GetAllRoute(routename);
      Request.setAttribute("All",rnb);
      forward="RouteResult";
    } else {
      countstepbean[] cts = OiMaintainService.CheckRouteExist(sid,routename);
      ProTestRouteBean[] ptr = OiMaintainService.GetRouteInfo(routename);

      if (cts != null && cts.length > 0){
        Request.setAttribute("closeWindow","false");
        Request.setAttribute("message","此 Route 已 定 義");
        forward="AlreadyDefine";
        fm.setRoutename("");
        fm.setReadonly("");
      } else if (ptr != null && ptr.length > 0){
        fm.setMessage("      ");
            fm.setRoutename(routename);
            fm.setReadonly("readonly= ");
            Request.setAttribute("RouteResult",ptr);
            forward="RouteResult";
        } else {
          Request.setAttribute("closeWindow","false");
          Request.setAttribute("message","找無此ROUTE");
          forward = "NoResult";
          fm.setRoutename("");
          fm.setReadonly("");
        }
    }
    Request.setAttribute(actionMapping.getName(),fm);
    return actionMapping.findForward(forward);
  }
}
