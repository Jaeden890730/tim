package com.mxic.oiplus.oimaintain;

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
    String listControl = oIReleaseActionForm.getListControl();

    if (listControl != null && listControl.equals("resendefile")) {
        boolean b = EifOIReleaseService.ftp_to_dcc(pd_body, brand, version, "MK360");
        
        if(b == false)
            servletRequest.setAttribute("message", "檔案無傳送，無法找到或傳送正確OI，請檢查參數");
        else
            servletRequest.setAttribute("message", "檔案已傳送，請收信查看");
        return actionMapping.findForward("koko1");
    } else {

    EifOIRelease.do_release(sid,pd_body,version,brand,"MANUAL-TEST");
    }

    return actionMapping.findForward("koko");
  }
}
