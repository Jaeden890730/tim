package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;
import org.apache.struts.action.*;

public class FTTestVendorAction extends Action {
  public FTTestVendorAction() {
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

    FTTestVendorActionForm fTTestVendorActionForm = (FTTestVendorActionForm) actionForm;
    FTService service = new FTService();
    String listControl = fTTestVendorActionForm.getListControl();

    if (listControl.equals("add_vendor_data")) {
      String be_opt = "";
      String test_mode = "";
      int pin_count = 0;
      String pg_type = "";
      String tester = "";
      String pg_name = "";
      String actual_file = "";
      String pgm_special_control = "";
      String one_main_pgm_group_version = "";
      String body_size = "";
      String i_grade = "0";
      String c_grade = "0";
      String w_grade = "0";
      String y_grade = "0";
      String j_grade = "0";
      String k_grade = "0";
      String l_grade = "0";
      String n_grade = "0";
      String b_grade = "0";
      String e_grade = "0";
      String s_grade = "0";
      String hw_configure = "";
      String comment = "";

      be_opt = fTTestVendorActionForm.getBe_opt();
      test_mode = fTTestVendorActionForm.getTest_mode();
      pin_count = fTTestVendorActionForm.getPin_count();
      pg_type = fTTestVendorActionForm.getPg_type();
      tester = fTTestVendorActionForm.getTester();
      pg_name = fTTestVendorActionForm.getPg_name();
      actual_file = fTTestVendorActionForm.getActual_file();
      pgm_special_control = fTTestVendorActionForm.getPgm_special_control();
      one_main_pgm_group_version = fTTestVendorActionForm.getOne_main_pgm_group_version();
      int sid = fTTestVendorActionForm.getSid();
      String pd_body = fTTestVendorActionForm.getPd_body();
      String brand = fTTestVendorActionForm.getBrand();
      String version = fTTestVendorActionForm.getVersion();
      body_size=fTTestVendorActionForm.getBody_size();
      i_grade=""+fTTestVendorActionForm.getI_grade();
      c_grade=""+fTTestVendorActionForm.getC_grade();
      w_grade=""+fTTestVendorActionForm.getW_grade();
      y_grade=""+fTTestVendorActionForm.getY_grade();
      j_grade=""+fTTestVendorActionForm.getJ_grade();
      k_grade=""+fTTestVendorActionForm.getK_grade();
      l_grade=""+fTTestVendorActionForm.getL_grade();
      n_grade=""+fTTestVendorActionForm.getN_grade();
      b_grade=""+fTTestVendorActionForm.getB_grade();
      e_grade=""+fTTestVendorActionForm.getE_grade();
      s_grade=""+fTTestVendorActionForm.getS_grade();
      hw_configure=""+fTTestVendorActionForm.getHw_configure();
      comment=""+fTTestVendorActionForm.getComment();
      FTTestVendorActionForm[] rs =
          FTService.getvendor_Info(fTTestVendorActionForm.getSite_data(),
                                   be_opt, test_mode, pin_count, pg_type,
                                   tester, pg_name, actual_file, pgm_special_control, one_main_pgm_group_version, sid, pd_body, brand,
                                   version, body_size, i_grade, c_grade,w_grade,y_grade,j_grade,k_grade,l_grade,n_grade,b_grade,e_grade, s_grade,hw_configure,comment);
      return actionMapping.findForward("success");
    } else {
      return actionMapping.findForward("fail");
    }
  }

  private void jbInit() throws Exception {
  }
}
