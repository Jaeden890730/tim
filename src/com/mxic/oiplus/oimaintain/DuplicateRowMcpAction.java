package com.mxic.oiplus.oimaintain;

import java.io.IOException;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class DuplicateRowMcpAction extends Action {
	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest Request,
			HttpServletResponse servletResponse) {

		ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
		String id = Request.getParameter("id");
		String type = Request.getParameter("type");
		String sid = Request.getParameter("sid");
		BomProductRouteBean[] bom = OiMaintainService.SearchBomMcpByID(id, sid);
		int s = 1;
		if (bom != null && bom.length > 1)
			s = bom.length;

		String nextFTRouteCode = OiMaintainService.getNextRouteCode(OiMaintainService.getMaxFTRouteCodeMcp(fm.getSid()));
		String tmpFtRouteCode = bom[0].getFt_route_code();
		for (int i = 0; i < s; i++) {
			if(tmpFtRouteCode.equals(bom[i].getFt_route_code())){
				bom[i].setFt_route_code(nextFTRouteCode);
			}else{
				tmpFtRouteCode = bom[i].getFt_route_code();
				nextFTRouteCode = OiMaintainService.getNextRouteCode(nextFTRouteCode);
				bom[i].setFt_route_code(nextFTRouteCode);
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

		Request.setAttribute(actionMapping.getName(), fm);
		
		HttpSession session = Request.getSession();
	    session.setAttribute("type", type);
	    session.setAttribute("bom", bom);
	    
	    try {
			servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/insertBomDupMcpAction.do?sid=" + sid));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Request.setAttribute("DupRow",bom);

		//return actionMapping.findForward("DupRow1");
	    return null;
	}
}
