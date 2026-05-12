package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiDeleteTheRouteAtion extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    oiDeleteTheRouteAForm oiDeleteTheRouteAForm = (oiDeleteTheRouteAForm) actionForm;
    String listControl = oiDeleteTheRouteAForm.getListControl();
    String ra_select = oiDeleteTheRouteAForm.getRa_select();
    String message = "";
    String forward = "";

    if (listControl.equals("bt_delete")) {
      boolean flag = oiSearchService.DeleteTestRoute(ra_select);
      if (flag == true) {
        message = "您成功地刪除 Route "+ra_select;
        servletRequest.setAttribute("message",message);
        forward = "bt_delete";
      } else {
        message = "刪除資料失敗 !!";
        servletRequest.setAttribute("message",message);
        forward = "bt_delete";
      }
    } else {
      message = "刪除資料失敗 !!";
      servletRequest.setAttribute("message",message);
      forward = "bt_delete";
    }
    return actionMapping.findForward(forward);
  }
}
