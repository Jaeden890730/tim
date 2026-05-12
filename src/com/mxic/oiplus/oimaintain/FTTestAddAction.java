package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class FTTestAddAction extends Action {
  public FTTestAddAction() {
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

    FTTestAddActionForm fTTestAddActionForm = (FTTestAddActionForm) actionForm;
    FTService service = new FTService();
    String listControl = fTTestAddActionForm.getListControl();

    String[] record_id = fTTestAddActionForm.getRecord_id();
    int sid = fTTestAddActionForm.getSid();
    int pg_id = fTTestAddActionForm.getPg_id();
    String pd_body = fTTestAddActionForm.getPd_body();
    String brand = fTTestAddActionForm.getBrand();
    String version = fTTestAddActionForm.getVersion();
    String test_type = fTTestAddActionForm.getTest_mode_str();
    String be_opt = fTTestAddActionForm.getBe_opt();
    int pin_count = fTTestAddActionForm.getPin_count();
    String pg_type = fTTestAddActionForm.getPg_type();
    String tester = fTTestAddActionForm.getTester();
    String site = fTTestAddActionForm.getSite();
    String pg_name = fTTestAddActionForm.getPg_name();
    String one_main_pgm_group_version = fTTestAddActionForm.getOne_main_pgm_group_version();
    String pgmflag = servletRequest.getParameter("pgmflag");

    if (listControl.equals("copy")){
      boolean flag1 = FTService.insertadd(sid,pd_body,brand,version,record_id);
      return actionMapping.findForward("copy");
    } else if(listControl.equals("check1")){
      FTTestAddActionForm[] rs =
          FTService.gettest_modeInfo(fTTestAddActionForm.getTest_mode(),
                                     brand,
                                     pd_body,
                                     sid,
                                     version,
                                     pgmflag);
      servletRequest.setAttribute("list2", rs);
      return actionMapping.findForward("success");
    } else {
      return actionMapping.findForward("fail");
    }
  }

  private void jbInit() throws Exception {
  }
}
