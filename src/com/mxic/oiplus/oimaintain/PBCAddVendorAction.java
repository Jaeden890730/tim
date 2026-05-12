package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;
import org.apache.struts.action.*;

public class PBCAddVendorAction extends Action {
  public PBCAddVendorAction() {
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

    PBCTestParameterForm fm = (PBCTestParameterForm) actionForm;
    String listControl = fm.getListControl();

    if (listControl.equals("add_vendor_data")) {
      String be_opt = "";
      String test_mode = "";
      int pin_count = 0;
      String pg_type = "";
      String tester = "";
      String pg_name = "";
      String actual_file = "";
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
      String pgm_id = "";
      String hw_configure = "";

      be_opt = fm.getBackend_option();
      test_mode = fm.getTest_type();
      pin_count = fm.getPin_count();
      pg_type = fm.getPackage_type();
      tester = fm.getTester();
      pg_name = fm.getProgram_name();
      actual_file = fm.getActual_file();
      int sid = fm.getSid();
      String pd_body = fm.getProduct_body();
      String brand = fm.getBrand();
      String version = fm.getVersion();
      body_size=fm.getBody_size();
      i_grade=""+fm.getI_grade();
      c_grade=""+fm.getC_grade();
      w_grade=""+fm.getW_grade();
      y_grade=""+fm.getY_grade();
      j_grade=""+fm.getJ_grade();
      k_grade=""+fm.getK_grade();
      l_grade=""+fm.getL_grade();
      n_grade=""+fm.getN_grade();
      b_grade=""+fm.getB_grade();
      e_grade=""+fm.getE_grade();
      s_grade=""+fm.getS_grade();
      pgm_id = fm.getPgm_id();
      hw_configure=fm.getHw_configure();
      PBCService.addvendor_Info(fm.getSite_data(),
                                be_opt, test_mode, pin_count, pg_type,
                                tester, pg_name, actual_file, sid, pd_body, brand,
                                version, body_size, i_grade, c_grade,w_grade, y_grade,j_grade,k_grade,l_grade,n_grade,b_grade,e_grade, s_grade, pgm_id, hw_configure);
      return actionMapping.findForward("success");
    } else {
      return actionMapping.findForward("fail");
    }
  }

  private void jbInit() throws Exception {
  }
}
