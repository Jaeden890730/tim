package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.oiplus.au.*;

public class TestRouteUploadAction extends Action {
  public TestRouteUploadAction() {
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
      String remark = servletRequest.getParameter("remark");//TRControlAForm.getRa_select();
      String route_cat = servletRequest.getParameter("route_cat");//TRControlAForm.getRa_select();
      String search_routename = servletRequest.getParameter("search_routename");
      String file_type = servletRequest.getParameter("type");
      
      //TFProductRouteBean[] TFPRBean = oiSearchService.LookUpTFProductRoute(ra_select,true);
      TDSLogger.println("listControl: " + listControl + ", ra_select: " + ra_select 
    		  + ", search_routename: " + search_routename + ", file_type: " + file_type);

      oiWSTestRouteControlAForm.setListControl(listControl);
      oiWSTestRouteControlAForm.setRa_select(ra_select);
      oiWSTestRouteControlAForm.setRemark(remark);
      oiWSTestRouteControlAForm.setSearch_routename(search_routename);
      oiWSTestRouteControlAForm.setFile_type(file_type);
      TFRouteMasterBean tfRMBean = oiSearchService.getRouteMasterBean(ra_select.toUpperCase());
      if(tfRMBean != null){
    	  oiWSTestRouteControlAForm.setFile_name_testflow(tfRMBean.getFile_name_testflow());
    	  oiWSTestRouteControlAForm.setFile_name_stdexcflow(tfRMBean.getFile_name_stdexcflow());
    	  if(file_type.equalsIgnoreCase("png")){
    		  oiWSTestRouteControlAForm.setFile_list(tfRMBean.getFile_name_testflow());
    	  }else if(file_type.equalsIgnoreCase("xls")){
    		  oiWSTestRouteControlAForm.setFile_list(tfRMBean.getFile_name_stdexcflow());
    	  }
      }

      HttpSession session = servletRequest.getSession();
      User Auth = (User) session.getAttribute("user");
      String user = Auth.getUserName();
      String message = "";
      //if(listControl.equals("update_remark") ){
         boolean flag = TestRouteAddService.update_remark_route_cat(ra_select,remark,route_cat,user);
        //servletRequest.setAttribute("list1", oiWSTestRouteControlAForm);
        if (flag == true){
          message = "您修改一筆 " +ra_select +" Remark ( " + remark + " ), Route Cat. ( " + route_cat + " )  成功";
          servletRequest.setAttribute("message",message);
          servletRequest.setAttribute("search_routename",search_routename);
        } else {
          message = "修改 " +ra_select +" Remark / Route Cat 失敗";
          servletRequest.setAttribute("message",message);
          servletRequest.setAttribute("search_routename",search_routename);
        }
        //return actionMapping.findForward("message_remark");
      //}



      servletRequest.setAttribute("list1", oiWSTestRouteControlAForm);

      return actionMapping.findForward("upload");
  }

  private void jbInit() throws Exception {
  }
}
