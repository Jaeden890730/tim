package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.IOException;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class CancellationAsignAction extends Action {
  public CancellationAsignAction() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    CancellationAsignService service = new CancellationAsignService();
    int sid = Integer.parseInt(Request.getParameter("sid"));
    boolean flag1 = service.getInfo(sid);
    try {
		servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() +"/OImaintain/searchActionX.do?sid="+sid));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    //return actionMapping.findForward("cancel_asign");
    return null;
  }

  private void jbInit() throws Exception {
  }
}
