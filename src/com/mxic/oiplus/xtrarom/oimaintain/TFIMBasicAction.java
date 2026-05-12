package com.mxic.oiplus.xtrarom.oimaintain;

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

    if (listControl.equals("delete_row")) {
      String record_id = tFIMBasicActionForm.getRecord_id();
      int sid = tFIMBasicActionForm.getSid();
      String pd_body = tFIMBasicActionForm.getPd_body();
      String brand = tFIMBasicActionForm.getBrand();
      String version = tFIMBasicActionForm.getVersion();

      boolean flag1 = service.delete_row(record_id, sid, brand, version, pd_body);

      return actionMapping.findForward("update_data");

    } else if (listControl.equals("update_data") ||
               listControl.equals("submit_data")) {
      String[] sid = servletRequest.getParameterValues("sid");
      String[] pd_body = servletRequest.getParameterValues("pd_body");
      String[] brand = servletRequest.getParameterValues("brand");
      String[] version = servletRequest.getParameterValues("version");

      String[] tester = servletRequest.getParameterValues("tester");
      String[] good_bin = servletRequest.getParameterValues("good_bin");
      String[] fail_bin = servletRequest.getParameterValues("fail_bin");
      String[] remark = servletRequest.getParameterValues("remark");
      String[] auto_ship_yield = servletRequest.getParameterValues("auto_ship_yield");
      String[] stop_test_yield = servletRequest.getParameterValues("stop_test_yield");
      String[] auto_scrap_yield = servletRequest.getParameterValues("auto_scrap_yield");
      String[] mrb_yield = servletRequest.getParameterValues("mrb_yield");
      String[] sample_yield = servletRequest.getParameterValues("sample_yield");

      String[] tester_be = servletRequest.getParameterValues("tester_be");
      String[] good_bin_be = servletRequest.getParameterValues("good_bin_be");
      String[] fail_bin_be = servletRequest.getParameterValues("fail_bin_be");
      String[] remark_be = servletRequest.getParameterValues("remarke_be");
      String[] auto_ship_yield_be = servletRequest.getParameterValues("auto_ship_yield_be");
      String[] stop_test_yield_be = servletRequest.getParameterValues("stop_test_yield_be");
      String[] auto_scrap_yield_be = servletRequest.getParameterValues("auto_scrap_yield_be");
      String[] mrb_yield_be = servletRequest.getParameterValues("mrb_yield_be");
      String[] sample_yield_be = servletRequest.getParameterValues("sample_yield_be");

      if (listControl.equals("update_data")) {
        boolean flag1 = service.update_data(sid,
                                            pd_body,
                                            brand,
                                            version,
                                            tester,
                                            good_bin,
                                            fail_bin,
                                            remark,
                                            auto_ship_yield,
                                            stop_test_yield,
                                            auto_scrap_yield,
                                            mrb_yield,
                                            sample_yield,
                                            tester_be,
                                            "update_cmd");
      } else if (listControl.equals("submit_data")) {
        boolean flag1 = service.update_data(sid,
                                            pd_body,
                                            brand,
                                            version,
                                            tester,
                                            good_bin,
                                            fail_bin,
                                            remark,
                                            auto_ship_yield,
                                            stop_test_yield,
                                            auto_scrap_yield,
                                            mrb_yield,
                                            sample_yield,
                                            tester_be,
                                            "submit_cmd");
      }
      return actionMapping.findForward("update_data");
    } else if (listControl.equals("reset_tx")) {
      int sid = tFIMBasicActionForm.getSid();
      String pd_body = tFIMBasicActionForm.getPd_body();
      String brand = tFIMBasicActionForm.getBrand();
      String version = tFIMBasicActionForm.getVersion();

      boolean flag1 =service.reset_tx(sid, brand, version, pd_body);
      return actionMapping.findForward("update_data");
    } else {
      return actionMapping.findForward("fail");
    }
  }

  private void jbInit() throws Exception {
  }
}
