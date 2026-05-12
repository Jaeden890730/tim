package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import com.mxic.oiplus.eif.*;
import com.mxic.oiplus.resource.*;


public class OIReleaseAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    OIReleaseActionForm oIReleaseActionForm = (OIReleaseActionForm) actionForm;

//    OIReleaseService oIReleaseService = new OIReleaseService();
    String sid = String.valueOf(oIReleaseActionForm.getSid());
    String pd_body = oIReleaseActionForm.getPd_body();
    String version = oIReleaseActionForm.getVersion();
    String brand = oIReleaseActionForm.getBrand();

    EifOIRelease.do_release(sid,pd_body,version,brand,"MANUAL-TEST");

    return actionMapping.findForward("koko");
  }
}
