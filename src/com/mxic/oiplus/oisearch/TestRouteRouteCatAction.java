package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.oiplus.au.*;

public class TestRouteRouteCatAction extends Action {
  public TestRouteRouteCatAction() {
      try {
          jbInit();
      } catch (Exception ex) {
          ex.printStackTrace();
      }
 }



  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

      oiWSTestRouteControlAForm oiWSTestRouteControlAForm = (oiWSTestRouteControlAForm) actionForm;

      String upload = oiWSTestRouteControlAForm.getUpload();
      //String ra_select = oiWSTestRouteControlAForm.getRa_select();
      //String listControl = oiWSTestRouteControlAForm.getListControl();//TRControlAForm.getListControl();
      String listControl = servletRequest.getParameter("listControl");//TRControlAForm.getListControl();
      String ra_select = servletRequest.getParameter("raSelect");//TRControlAForm.getRa_select();
      String route_cat = servletRequest.getParameter("route_cat");//TRControlAForm.getRa_select();
      String search_routename = servletRequest.getParameter("search_routename");
      TFProductRouteBean[] TFPRBean = oiSearchService.LookUpTFProductRoute(ra_select,true);
      TDSLogger.println(listControl);
      TDSLogger.println(ra_select);
      TDSLogger.println(search_routename);

      oiWSTestRouteControlAForm.setListControl(listControl);
      oiWSTestRouteControlAForm.setRa_select(ra_select);
      oiWSTestRouteControlAForm.setRoute_cat(route_cat);
      oiWSTestRouteControlAForm.setSearch_routename(search_routename);

      HttpSession session = servletRequest.getSession();
      User Auth = (User) session.getAttribute("user");
      String user = Auth.getUserName();
      String message = "";

         boolean flag = TestRouteAddService.update_route_cat(ra_select,route_cat,user);
        servletRequest.setAttribute("list1", oiWSTestRouteControlAForm);
        if (flag == true){
          message = "您修改一筆 " +ra_select +" Route Cat ( " + route_cat + " ) 成功";
          servletRequest.setAttribute("message",message);
          servletRequest.setAttribute("search_routename",search_routename);
        } else {
          message = "修改 " +ra_select +" Route Cat 失敗";
          servletRequest.setAttribute("message",message);
          servletRequest.setAttribute("search_routename",search_routename);
        }
        return actionMapping.findForward("message_route_cat");

  }

  private void jbInit() throws Exception {
  }
}
