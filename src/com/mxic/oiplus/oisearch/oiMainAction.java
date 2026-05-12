package com.mxic.oiplus.oisearch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class oiMainAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {
    String message = "";
    String forward = "";
    String productType = null;

    //boolean flag = pdfBringService.makePDF("6615");
    String type = servletRequest.getParameter("type");
    HttpSession session = servletRequest.getSession();
    if (type != null) {
    	if (type.equals("0"))
    		productType = "NVM";
    	else if (type.equals("1"))
    		productType = "ASM";
    	else
        	productType = "UKN";
    } else {
		if (session.getAttribute("productType") != null)
			productType = (String)session.getAttribute("productType");
    }
    session.setAttribute("productType", productType);
    IFInformationBean[] rs = oiSearchService.SelectAllIFinformation(productType);
    if (rs != null){
      servletRequest.setAttribute("list",rs);
    }
    return actionMapping.findForward("success");
  }
}

