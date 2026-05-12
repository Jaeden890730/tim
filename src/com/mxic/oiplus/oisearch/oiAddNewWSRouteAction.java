package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiAddNewWSRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    oiTestRouteAForm oiAddNewWSRouteAForm = (oiTestRouteAForm) actionForm;
    String btControl = oiAddNewWSRouteAForm.getBtControl();
    String routename = oiAddNewWSRouteAForm.getTxt_routename();
    String selType = oiAddNewWSRouteAForm.getType();
    String message = "";
    String forward = "";
    String flag = servletRequest.getParameter("flag");

    if (flag.equals("WS")){
      TFDescriptionBean[] stepname = oiSearchService.SelectStepNameWS();
      servletRequest.setAttribute("stepname", stepname);
    } else if (flag.equals("FT")){
      TFDescriptionBean[] stepname = oiSearchService.SelectStepNameFT();
      servletRequest.setAttribute("stepname", stepname);
    }

    if (btControl.equals("bt_copy")){
      forward = "CopyFromAnotherRoute";
    } else if(btControl.equals("bt_save") ){
      boolean checkRouteName = oiSearchService.CheckRouteName(routename);
      if (checkRouteName == true && flag.equals("FT")) {
        message = "RouteName輸入有錯誤，請查明是否有重覆";
        servletRequest.setAttribute("message",message);
        forward = "RouteNameRepeatFT";
      } else if(checkRouteName == true && flag.equals("WS")){
        message = "RouteName輸入有錯誤，請查明是否有重覆";
        servletRequest.setAttribute("message",message);
        forward = "RouteNameRepeatWS";
      } else {
    	String checkSameRoute = "";
    	if(oiAddNewWSRouteAForm.getTxt_routename().startsWith("F")){  
    	   checkSameRoute = oiSearchService.CheckSameRoute(oiAddNewWSRouteAForm);
    	}   
    	if (!checkSameRoute.equals("") && flag.equals("FT")) {
            message = "Test Mode組合已存在,請用 existing route name : " + checkSameRoute;
            servletRequest.setAttribute("message",message);
            forward = "RouteNameRepeatFT";
        } else if(!checkSameRoute.equals("") && flag.equals("WS")){
            message = "Test Mode組合已存在,請用 existing route name : " + checkSameRoute;
            servletRequest.setAttribute("message",message);
            forward = "RouteNameRepeatWS";
        } else {
            boolean saveflag = oiSearchService.AddNewRoute(oiAddNewWSRouteAForm);
            if (saveflag == true && flag.equals("FT")){
              message = "您成功的新增一筆Route";
              servletRequest.setAttribute("message",message);
              forward = "savesuccessFT";
            } else if (saveflag == true && flag.equals("WS")){
              message = "您成功的新增一筆Route";
              servletRequest.setAttribute("message",message);
              forward = "savesuccessWS";
            } else if (!saveflag  && flag.equals("FT")){
              message = "新增Route失敗，請確認所輸入的值是否有誤";
              servletRequest.setAttribute("message",message);
              forward = "savefailedFT";
            } else if (!saveflag  && flag.equals("WS")){
              message = "新增Route失敗，請確認所輸入的值是否有誤";
              servletRequest.setAttribute("message",message);
              forward = "savefailedWS";
            }
        }
      }
    } else {
    }
    return actionMapping.findForward(forward);
  }
}
