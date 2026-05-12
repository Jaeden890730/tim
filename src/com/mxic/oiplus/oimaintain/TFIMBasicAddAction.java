package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class TFIMBasicAddAction extends Action {
  public TFIMBasicAddAction() {
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

    TFIMBasicAddActionForm tFIMBasicAddActionForm = (TFIMBasicAddActionForm) actionForm;
    TFIMBasicService service = new TFIMBasicService();
    String listControl = tFIMBasicAddActionForm.getListControl();

    int sid = tFIMBasicAddActionForm.getSid();
    String pd_body = tFIMBasicAddActionForm.getPd_body();
    String brand = tFIMBasicAddActionForm.getBrand();
    String version = tFIMBasicAddActionForm.getVersion();
    String tester = tFIMBasicAddActionForm.getTester();
    String remark = tFIMBasicAddActionForm.getRemark();
    String options = tFIMBasicAddActionForm.getOptions();
    String grade = tFIMBasicAddActionForm.getGrade();
    String bin_type = tFIMBasicAddActionForm.getBin_type();
    String inkless_grade = tFIMBasicAddActionForm.getInkless_grade();
    String ktd_bin_flag = tFIMBasicAddActionForm.getKtd_bin_flag();
    //String ipn_action = tFIMBasicAddActionForm.getIpn_action();
    String ipn_action = servletRequest.getParameter("ipn_action_hidden");
    String down_grade = tFIMBasicAddActionForm.getDown_grade();
    String epn_speed = tFIMBasicAddActionForm.getEpn_speed();
    String test_speed = tFIMBasicAddActionForm.getTest_speed();
    String good_bin = tFIMBasicAddActionForm.getGood_bin();
    String fail_bin = tFIMBasicAddActionForm.getFail_bin();
	String ib_bin = (tFIMBasicAddActionForm.getIb_bin() == null) ? "NA":tFIMBasicAddActionForm.getIb_bin();
    String auto_ship_yield = tFIMBasicAddActionForm.getAuto_ship_yield();
    String stop_test_yield = tFIMBasicAddActionForm.getStop_test_yield();
    String auto_scrap_yield = tFIMBasicAddActionForm.getAuto_scrap_yield();
    String mrb_yield = tFIMBasicAddActionForm.getMrb_yield();
    String sample_yield = tFIMBasicAddActionForm.getSample_yield();

    if (listControl.equals("insert_row")){
      boolean flag1 =
          service.insertadd(sid, pd_body, brand, version, tester,
                            options, grade, good_bin,ib_bin, bin_type, inkless_grade,ktd_bin_flag, ipn_action,
                            epn_speed, test_speed, down_grade, fail_bin, remark,
                            auto_ship_yield,
                            stop_test_yield, auto_scrap_yield, mrb_yield,
                            sample_yield);
      FTTestActionForm ftm = FTService.getInfo(sid);
      servletRequest.setAttribute("product_type",ftm.getProductType());
      return actionMapping.findForward("insert_row");
    } else if (listControl.equals("update_row")){
        boolean flag1 =
            service.update_row(sid, pd_body, brand, version, tester,
                              options, grade, good_bin,ib_bin, bin_type, inkless_grade,ktd_bin_flag, ipn_action,
                              epn_speed, test_speed, down_grade, fail_bin, remark, auto_ship_yield,
                              stop_test_yield, auto_scrap_yield, mrb_yield, sample_yield);
       FTTestActionForm ftm = FTService.getInfo(sid);
       servletRequest.setAttribute("product_type",ftm.getProductType());
        return actionMapping.findForward("insert_row");
    } else {
      return actionMapping.findForward("fail");
    }
  }

  private void jbInit() throws Exception {
  }
}
