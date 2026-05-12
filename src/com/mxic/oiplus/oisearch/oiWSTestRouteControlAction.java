package com.mxic.oiplus.oisearch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.oiplus.util.TDSLogger;

public class oiWSTestRouteControlAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    oiWSTestRouteControlAForm TRControlAForm =
        (oiWSTestRouteControlAForm) actionForm;

    String listControl = servletRequest.getParameter("listControl");//TRControlAForm.getListControl();
    String ra_select = servletRequest.getParameter("raSelect");//TRControlAForm.getRa_select();
    String forward = "";
    TDSLogger.println(listControl);
    TDSLogger.println(ra_select);
    String b = ra_select.substring(2,2);

    if (listControl.equals("bt_update")) {
      TFProductRouteBean[] TFPRBean = oiSearchService.LookUpTFProductRoute(ra_select, true);
      if (TFPRBean == null) {
        String type = oiSearchService.RouteTypeMode(ra_select);
        if (type.equals("W")){
          TFDescriptionBean[] stepname = oiSearchService.SelectStepNameWS();
          servletRequest.setAttribute("stepname", stepname);
        } else if (type.equals("P")){
          TFDescriptionBean[] stepname = oiSearchService.SelectStepNameFT();
          servletRequest.setAttribute("stepname", stepname);
        }
        TFRouteMasterBean[] SelectTestRoute = oiSearchService.SelectTestRoute(type, ra_select);
        servletRequest.setAttribute("list", SelectTestRoute);
        forward = "bt_update";
      } else {
        servletRequest.setAttribute("list", TFPRBean);
        forward = "TFPRBeanNotNull";
      }
    } else if (listControl.equals("bt_delete")) {
      TFProductRouteBean[] TFPRBean = oiSearchService.LookUpTFProductRoute(ra_select, true);
      if (TFPRBean == null) {
        TFRouteMasterBean[] DeleteTestRoute = oiSearchService.SelectTestRoute("W", ra_select);
        servletRequest.setAttribute("list", DeleteTestRoute);
        forward = "bt_delete";
      } else {
        servletRequest.setAttribute("list", TFPRBean);
        forward = "TFPRBeanNotNull";
      }
    } else if(listControl.equals("bt_usage")){
      TFProductRouteBean[] TFPRBean = oiSearchService.LookUpTFProductRoute(ra_select, true);
      servletRequest.setAttribute("list", TFPRBean);
      forward = "TFPRBeanNotNull";
    } else if(listControl.equals("bt_usage_active")){
        TFProductRouteBean[] TFPRBean = oiSearchService.LookUpTFProductRoute(ra_select, false);
        servletRequest.setAttribute("list", TFPRBean);
        forward = "TFPRBeanNotNull";  
    }
    return actionMapping.findForward(forward);
    }
}