package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class AurthMaintainAction extends Action {
  public AurthMaintainAction() {
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

    AurthMaintainActionForm aurthMaintainActionForm = (AurthMaintainActionForm) actionForm;

    String listControl = aurthMaintainActionForm.getListControl();

    // FTTestAddActionForm fTTestAddActionForm = (FTTestAddActionForm)actionForm;
    if (listControl.equals("update_data")) {
      boolean flag1 =
          AurthMaintainService.update_data(aurthMaintainActionForm.getSid(),aurthMaintainActionForm.getSponsor_1(),aurthMaintainActionForm.getSponsor_2());
      return actionMapping.findForward("update_data");
    }  else {
      return actionMapping.findForward("fail");
    }
  }

  private void jbInit() throws Exception {
  }
}
