package com.mxic.oiplus.oimaintain;

import java.io.IOException;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.sun.mail.iap.Response;

public class DeleteBomRouteTxAction extends Action {
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest Request, HttpServletResponse servletResponse) {

    ProTestRouteBeanAF proTestRouteBeanAF = (ProTestRouteBeanAF) actionForm;
    String id= Request.getParameter("id");
    String sid = Request.getParameter("sid");
	String idd = "", mk = "", wr = "";

	if (id != null && id.split("~").length == 3) {
		idd = id.split("~")[0];// -1,111,222
		mk = id.split("~")[1];// -1,S,L
		wr = id.split("~")[2];// -1,FW139,FW127
	}
    try{
    	String msg = OiMaintainService.CheckExistYieldDefCount(sid, idd);
			String msg1 = OiMaintainService.CheckExistYieldDefCountDGRADEPRODCODE(sid, idd);
		if (msg != null) {
			servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/bomProductRouteAction.do?sid="+sid+"&delete=" + msg));
			} else if (msg1 != null) {
				servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/bomProductRouteAction.do?sid=" + sid + "&delete=" + msg1));
		} else {
			OiMaintainService.DeleteBomRouteTx(idd);
			OiMaintainService.submit(sid, "TF_BOM_ROUTE", "N");
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath()+"/OImaintain/bomProductRouteAction.do?delete=Y&sid="+sid));
		}
    } catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
