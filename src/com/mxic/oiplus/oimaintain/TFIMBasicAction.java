package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class TFIMBasicAction extends Action {
  public TFIMBasicAction() {
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

    TFIMBasicActionForm tFIMBasicActionForm = (TFIMBasicActionForm) actionForm;
    TFIMBasicService service = new TFIMBasicService();
    String listControl = tFIMBasicActionForm.getListControl();
    // return actionMapping.findForward("update_data");

    if (listControl.equals("update_row")) {

      return actionMapping.findForward("update_row");
    }
    if (listControl.equals("delete_row")) {
      String record_id = tFIMBasicActionForm.getRecord_id();
      int sid = tFIMBasicActionForm.getSid();
      String pd_body = tFIMBasicActionForm.getPd_body();
      String brand = tFIMBasicActionForm.getBrand();
      String version = tFIMBasicActionForm.getVersion();

      boolean flag1 = service.delete_row(record_id, sid, brand, version, pd_body);

      FTTestActionForm ftm = FTService.getInfo(sid);
      servletRequest.setAttribute("product_type",ftm.getProductType());
      return actionMapping.findForward("update_data");

    } else if (listControl.equals("update_data") ||
               listControl.equals("submit_data")) {
      String[] sid = servletRequest.getParameterValues("sid");
      String[] pd_body = servletRequest.getParameterValues("pd_body");
      String[] brand = servletRequest.getParameterValues("brand");
      String[] version = servletRequest.getParameterValues("version");

      String[] tester = servletRequest.getParameterValues("tester");
      String[] options = servletRequest.getParameterValues("options");
      String[] grade = servletRequest.getParameterValues("grade");
      String[] good_bin = servletRequest.getParameterValues("good_bin");
      String[] bin_type = servletRequest.getParameterValues("bin_type");
      String[] inkless_grade = servletRequest.getParameterValues("inkless_grade");
      String[] ktd_bin_flag = servletRequest.getParameterValues("ktd_bin_flag");
      String[] ipn_action = servletRequest.getParameterValues("ipn_action");
      String[] epn_speed = servletRequest.getParameterValues("epn_speed");
      String[] test_speed = servletRequest.getParameterValues("test_speed");
      String[] down_grade = servletRequest.getParameterValues("down_grade");
      String[] fail_bin = servletRequest.getParameterValues("fail_bin");
      String[] remark = servletRequest.getParameterValues("remark");
/*
      String[] auto_ship_yield = servletRequest.getParameterValues("auto_ship_yield");
      String[] stop_test_yield = servletRequest.getParameterValues("stop_test_yield");
      String[] auto_scrap_yield = servletRequest.getParameterValues("auto_scrap_yield");
      String[] mrb_yield = servletRequest.getParameterValues("mrb_yield");
      String[] sample_yield = servletRequest.getParameterValues("sample_yield");
*/
      String comments = servletRequest.getParameter("comments");

      if (listControl.equals("update_data")) {
        boolean flag1 = service.update_data(sid,
                                            pd_body,
                                            brand,
                                            version,
                                            tester,
                                            options,
                                            grade,
                                            good_bin,
                                            remark,
                                            comments,
                                            "update_cmd");
      } else if (listControl.equals("submit_data")) {
        boolean flag1 = service.update_data(sid,
                                            pd_body,
                                            brand,
                                            version,
                                            tester,
                                            options,
                                            grade,
                                            good_bin,
                                            remark,
                                            comments,
                                            "submit_cmd");
      }
      FTTestActionForm ftm = FTService.getInfo(Integer.parseInt(sid[0]));
      servletRequest.setAttribute("product_type",ftm.getProductType());
      return actionMapping.findForward("update_data");
    } else if (listControl.equals("reset_tx")) {
      int sid = tFIMBasicActionForm.getSid();
      String pd_body = tFIMBasicActionForm.getPd_body();
      String brand = tFIMBasicActionForm.getBrand();
      String version = tFIMBasicActionForm.getVersion();

      boolean flag1 =service.reset_tx(sid, brand, version, pd_body);
      FTTestActionForm ftm = FTService.getInfo(sid);
      servletRequest.setAttribute("product_type",ftm.getProductType());
      return actionMapping.findForward("update_data");
    } else {
      return actionMapping.findForward("fail");
    }
  }

  private void jbInit() throws Exception {
  }
}
