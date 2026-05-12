package com.mxic.oiplus.xtrarom.oimaintain;

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
    String good_bin = tFIMBasicAddActionForm.getGood_bin();
    String fail_bin = tFIMBasicAddActionForm.getFail_bin();
    String auto_ship_yield = tFIMBasicAddActionForm.getAuto_ship_yield();
    String stop_test_yield = tFIMBasicAddActionForm.getStop_test_yield();
    String auto_scrap_yield = tFIMBasicAddActionForm.getAuto_scrap_yield();
    String mrb_yield = tFIMBasicAddActionForm.getMrb_yield();
    String sample_yield = tFIMBasicAddActionForm.getSample_yield();

    if (listControl.equals("insert_row")){
      boolean flag1 =
          service.insertadd(sid, pd_body, brand, version, tester,
                            remark, good_bin, fail_bin, auto_ship_yield,
                            stop_test_yield, auto_scrap_yield, mrb_yield, sample_yield);
      return actionMapping.findForward("insert_row");
    } else {
      return actionMapping.findForward("fail");
    }
  }

  private void jbInit() throws Exception {
  }
}
