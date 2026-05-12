package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiUpdateWSTestRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    oiTestRouteAForm oiUpdateWSTestRouteAForm =
        (oiTestRouteAForm) actionForm;

    String btControl = oiUpdateWSTestRouteAForm.getBtControl();
    String message = "";
    String forward = "";
    if (btControl.equals("bt_update")){
      boolean flag = oiSearchService.UpdateEmpBasicData(oiUpdateWSTestRouteAForm);
      if (flag == true){
        message = "您成功修改一筆 Route";
        servletRequest.setAttribute("message",message);
        forward = "bt_update";
      } else {
        message = "修改 Route 失敗";
        servletRequest.setAttribute("message",message);
        forward="bt_update";
      }
    }
    return actionMapping.findForward(forward);
  }
}
