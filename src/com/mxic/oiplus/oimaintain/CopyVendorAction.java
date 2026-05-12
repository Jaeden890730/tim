package com.mxic.oiplus.oimaintain;

import java.io.IOException;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class CopyVendorAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse httpServletResponse) {

    String[] chk1=Request.getParameterValues("chk1");
    String sid = Request.getParameter("sid");
    String pgm_id = Request.getParameter("pgm_id");
    String product_body = Request.getParameter("product_body");
    String brand = Request.getParameter("brand");
    String version = Request.getParameter("version");
    String mask_option = Request.getParameter("mask_option");
    String test_type = Request.getParameter("test_type");
    String tester = Request.getParameter("tester");
    String site = Request.getParameter("site");
    String program_name = Request.getParameter("program_name");
    String tf_comment = Request.getParameter("tf_comment");
    String hw_configure = Request.getParameter("hw_configure");
    String pgm_special_control = Request.getParameter("pgm_special_control");
    String temperature = Request.getParameter("temperature");
    String one_main_pgm_group_version = Request.getParameter("one_main_pgm_group_version");
    if(chk1 != null){
	    OiMaintainService.CopyVendorToWsTx(chk1,
	                                       sid,
	                                       pgm_id,
	                                       product_body,
	                                       brand,
	                                       version,
	                                       mask_option,
	                                       test_type,
	                                       tester,
	                                       site,
	                                       program_name,
	                                       hw_configure,
	                                       pgm_special_control,
	                                       tf_comment,
	                                       temperature,
	                                       one_main_pgm_group_version);
    }
    try {
		httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/wsTestParameterAction.do?sid="+sid));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    //return actionMapping.findForward("CopyThis");
    return null;
  }
}
