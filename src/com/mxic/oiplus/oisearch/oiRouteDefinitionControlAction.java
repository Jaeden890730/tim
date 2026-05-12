package com.mxic.oiplus.oisearch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.mxic.oiplus.au.User;

import com.mxic.oiplus.util.TDSLogger;
import com.mxic.oiplus.util.StringUtil;

public class oiRouteDefinitionControlAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    TFRouteDefinitionBeanAF tfControlForm =
        (TFRouteDefinitionBeanAF) actionForm;

    User user =  (User) servletRequest.getSession().getAttribute("user");

    String product_body = servletRequest.getParameter("product_body");
    String listControl = servletRequest.getParameter("listControl");
    String ra_select = servletRequest.getParameter("raSelect");
    String ra_select2 = servletRequest.getParameter("raSelect2");
    String forward = "";

    if (listControl.equals("bt_insert")) {
      TFRouteDefinitionBeanAF btinsert = new TFRouteDefinitionBeanAF();
      String item[] = (ra_select2+";EOF").split(";");
      btinsert.setProduct_body(item[0]);
      btinsert.setFt_route(item[1]);
      btinsert.setFt_route_add(item[2]);
      btinsert.setWs_route(item[3]);
      btinsert.setWs_route_add(item[4]);
      btinsert.setRoute_option(item[5]);
      servletRequest.setAttribute("bean", btinsert);
      servletRequest.setAttribute("bean_type", "bt_insert");
      forward = "bt_insert";
    } else if (listControl.equals("bt_insert_bean")) {
      boolean bt_update = oiSearchService.insertRouteDefinition(tfControlForm, user.getUserName());
      if (bt_update)
        servletRequest.setAttribute("message", "Insert success !!");
      else
        servletRequest.setAttribute("message", "Error while inserting data !!");
      forward = "bt_insert_bean";
    } else if (listControl.equals("bt_update")) {
      TFRouteDefinitionBeanAF btupdate = new TFRouteDefinitionBeanAF();
      String item[] = (ra_select+";EOF").split(";");
      btupdate.setProduct_body(item[0]);
      btupdate.setFt_route(item[1]);
      btupdate.setFt_route_add(item[2]);
      btupdate.setWs_route(item[3]);
      btupdate.setWs_route_add(item[4]);
      btupdate.setRoute_option(item[5]);
      btupdate.setRoute_option_description(StringUtil.Utf8ToBig5(item[6]));
      servletRequest.setAttribute("bean", btupdate);
      servletRequest.setAttribute("bean_type", "bt_update");
      forward = "bt_update";
    } else if (listControl.equals("bt_update_bean")) {
      boolean bt_update = oiSearchService.updateRouteDefinition(tfControlForm, user.getUserName());
      if (bt_update)
        servletRequest.setAttribute("message", "Update success !!");
      else
        servletRequest.setAttribute("message", "Error while updating data !!");
      forward = "bt_update_bean";
    } else if (listControl.equals("bt_delete")) {
      boolean bt_delete = oiSearchService.deleteRouteDefinition(ra_select);
      if (bt_delete)
        servletRequest.setAttribute("message", "Delete success !!");
      else
        servletRequest.setAttribute("message", "Error while deleting !!");
      forward = "bt_delete";
    } else if (listControl.equals("bt_prm2")) {
      boolean bt_prm2 = oiSearchService.SentRouteDefinitionToPRM2(product_body);
      if (bt_prm2)
        servletRequest.setAttribute("message", "Sent to PRM2 success !!");
      else
        servletRequest.setAttribute("message", "Error while Sending to PRM2 !!");
      forward = "bt_prm2";
    } else if (listControl.equals("bt_back")) {
      forward = "bt_back";
    }

    if (listControl.equals("bt_insert_bean") ||
        listControl.equals("bt_update_bean") ||
        listControl.equals("bt_delete") ||
        listControl.equals("bt_back") ||
        listControl.equals("bt_prm2")) {
      TFRouteDefinitionBeanAF[] btsearch =
          oiSearchService.SelectRouteOption(product_body);
      TFRouteDefinitionBeanAF[] btsearch2 =
          oiSearchService.SelectAvailableRouteOption(product_body,user.getUserName());
      servletRequest.setAttribute("list", btsearch);
      servletRequest.setAttribute("list2", btsearch2);
    }

    return actionMapping.findForward(forward);
  }
}