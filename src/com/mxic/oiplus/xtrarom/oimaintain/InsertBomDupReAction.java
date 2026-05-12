package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.IOException;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.util.TDSLogger;

public class InsertBomDupReAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestReRouteBeanAF fm = (ProTestReRouteBeanAF) actionForm;
    String id=Request.getParameter("id");
    String sid=Request.getParameter("sid");
    String epnbody=Request.getParameter("epnbody");
    String brand=Request.getParameter("brand");
    String productbody=Request.getParameter("productbody");
    String bodyversion=Request.getParameter("bodyversion");
    String pincount=Request.getParameter("pincount");
    String pkgtype=Request.getParameter("pkgtype");
    String ftroute=Request.getParameter("ftroute");
    String maskopt=Request.getParameter("maskopt");
    String maskoptrev=Request.getParameter("maskoptrev");
    String codeno=Request.getParameter("codeno");
    String routetype=Request.getParameter("routetype");
    String comment=Request.getParameter("txtComment");
    String recycle_code=Request.getParameter("recycle_code");
    String ftAddroute=Request.getParameter("ftAddroute");
    
    com.mxic.oiplus.oimaintain.OiMaintainStep ois = null;
    ois = com.mxic.oiplus.oimaintain.OiMaintainService.SearchFunction(sid);
    if(ois != null){
    	fm.setSid(ois.getSid());
    	fm.setBrand(ois.getBrand());
    	fm.setProductbody(ois.getProduct_body());
    	fm.setVersion(ois.getVersion());
    	fm.setCreator(ois.getCreator());
    }


    boolean flag =
        OiMaintainService.CheckExistBomReDup(fm.getSid(),
        		bodyversion,
        		pincount,
        		pkgtype,
        		maskopt,
                        maskoptrev,
                        codeno,
                        routetype,
        		recycle_code);
    String forward=null;
    try{
      if (!flag){
        OiMaintainService.InsertBomReDup(fm, productbody,
                                       pincount, pkgtype, maskopt, maskoptrev,
                                       codeno, routetype,
                                       comment, recycle_code,
                                       ftroute, ftAddroute);
        com.mxic.oiplus.oimaintain.OiMaintainService.submit(fm.getSid(), "TF_BOM_REROUTE", "N");
        forward="InsertBomDup";
      } else {
        Request.setAttribute("closeWindow","false");
        Request.setAttribute("message","資料表中有重複的資料");
        forward="SameDataExist";
      }
    } catch (Exception e){
      TDSLogger.println(e);
    }
    try {
    	if(forward.equals("InsertBomDup")){
    		//servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/bomProductRouteActionX.do?sid=" + sid));
    		servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/bomProductReRouteActionX.do?sid=" + sid));
    		return null;
    	}	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return actionMapping.findForward(forward);
  }
}
