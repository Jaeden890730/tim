package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiCopyFromAnotherRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    oiCopyFromAnotherRouteAForm AnotherRouteAForm = (oiCopyFromAnotherRouteAForm) actionForm;
    String listControl = AnotherRouteAForm.getListControl();
    String ra_select = AnotherRouteAForm.getRa_select();
    String route_name = AnotherRouteAForm.getRoute_name();
    String forward="";
    //String flag=servletRequest.getParameter("flag");
    if (listControl.equals("bt_search")){
      TFRouteMasterBean[] list = oiSearchService.SelectTheTestRoute(route_name);
      servletRequest.setAttribute("list",list);
      forward = "bt_search";
    } else if (listControl.equals("bt_copy")){
      TFRouteMasterBean[] list = oiSearchService.SelectTheTestRoute(ra_select);
      if (list[0].getType().equals("W")){
        TFDescriptionBean[] stepname = oiSearchService.SelectStepNameWS();
        servletRequest.setAttribute("stepname", stepname);
        servletRequest.setAttribute("list",list);
        forward = "bt_copy_WS";
      } else if(list[0].getType().equals("P")){
        TFDescriptionBean[] stepname = oiSearchService.SelectStepNameFT();
        servletRequest.setAttribute("stepname", stepname);
        //TFRouteMasterBean[] list = oiSearchService.SelectTheTestRoute(ra_select);
        servletRequest.setAttribute("list",list);
        forward = "bt_copy_FT";
      }
    } else {
    }
    return actionMapping.findForward(forward);
  }
}
