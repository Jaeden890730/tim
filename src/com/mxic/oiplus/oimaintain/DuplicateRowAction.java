package com.mxic.oiplus.oimaintain;

import java.io.IOException;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class DuplicateRowAction extends Action {
    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest Request, HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String id=Request.getParameter("id");
    String type=Request.getParameter("type");
    String sid=Request.getParameter("sid");
    BomProductRouteBean[] bom=OiMaintainService.SearchBomByID(id);
        int s = 1;
        if (bom != null && bom.length > 1)
            s = bom.length;

        String maxFTRouteCode = "";
        String tmpmaxFTRouteCode = "";
        String maxSortRouteCode = "";
        String tmpmaxSortRouteCode = "";
        for (int i = 0; i < s; i++) {
            if (!tmpmaxFTRouteCode.equals(""))
                maxFTRouteCode = tmpmaxFTRouteCode;
            else
                maxFTRouteCode = OiMaintainService.getMaxFTRouteCode(bom[i].getSid());
            bom[i].setFt_route_code(OiMaintainService.getNextRouteCode(maxFTRouteCode));
            tmpmaxFTRouteCode = bom[i].getFt_route_code();
            if(type != null && type.equals("MCP")){
            	if (!tmpmaxSortRouteCode.equals(""))
                    maxSortRouteCode = tmpmaxSortRouteCode;
                else
                    maxSortRouteCode = OiMaintainService.getMaxSortRouteCode(bom[i].getProductbody(), bom[i].getBrand());
                bom[i].setSortroutecode(OiMaintainService.getNextRouteCode(maxSortRouteCode).toUpperCase());
                tmpmaxSortRouteCode = bom[i].getSortroutecode();
            }
            
        }

    if (bom != null && bom.length > 0) {
      fm.setProductbody(bom[0].getProductbody());
      fm.setBrand(bom[0].getBrand());
      fm.setSid(bom[0].getSid());
      fm.setVersion(bom[0].getVersion());
    }
    fm.setType(type);
    fm.setBom(bom);

    Request.setAttribute(actionMapping.getName(),fm);
    //Request.setAttribute("DupRow",bom);
    HttpSession session = Request.getSession();
    session.setAttribute("type", type);
    session.setAttribute("bom", bom);
    
    try {
		servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/insertBomDupAction.do?sid=" + sid));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
        //return actionMapping.findForward("DupRow1");
    return null;
  }
}
