package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiTestRouteSearchAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    oiTestRouteSearchAForm testRouteSearchAForm = (oiTestRouteSearchAForm) actionForm;
    String strMode = "";//testRouteSearchAForm.getMode();
    String strRouteName = testRouteSearchAForm.getTxt_routename();
    if (strRouteName != null)
       strRouteName = strRouteName.replace('%','*');
    String strControl = testRouteSearchAForm.getBtControl();
    String forward = "";

    if (strControl.equals("bt_search")) {
      strMode = oiSearchService.RouteTypeMode(strRouteName);
      TFRouteMasterBean[] btsearch =
          oiSearchService.SelectTestRoute(strMode, strRouteName.toUpperCase());
      servletRequest.setAttribute("list", btsearch);
      servletRequest.setAttribute("search_routename", strRouteName);
      forward="TestRouteList";
    } else if (strControl.equals("WS")) {
      TFDescriptionBean[] stepname = oiSearchService.SelectStepNameWS();
      servletRequest.setAttribute("stepname", stepname);
      forward="TestRouteAddWS";
    } else if (strControl.equals("FT")) {
      TFDescriptionBean[] stepname = oiSearchService.SelectStepNameFT();
      servletRequest.setAttribute("stepname", stepname);
      forward="TestRouteAddFT";
    } else {
    }
    return actionMapping.findForward(forward);
  }
}
