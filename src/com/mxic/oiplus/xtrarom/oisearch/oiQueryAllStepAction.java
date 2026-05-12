package com.mxic.oiplus.xtrarom.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiQueryAllStepAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    oiQueryAllStepAForm oiQueryAllStepAForm = (oiQueryAllStepAForm) actionForm;

    String productbody = oiQueryAllStepAForm.getProductbody();
    String brand = oiQueryAllStepAForm.getBrand();
    String version = oiQueryAllStepAForm.getVersion();
    String step = oiQueryAllStepAForm.getStep();
    String forward = "";
    return actionMapping.findForward(forward);
  }
}
