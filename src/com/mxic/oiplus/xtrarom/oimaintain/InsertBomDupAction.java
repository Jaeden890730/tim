package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.IOException;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class InsertBomDupAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
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
    String sortroutecode=Request.getParameter("txtRouteCode");
    String wsroute=Request.getParameter("wsroute");
    String wsaddroute=Request.getParameter("wsaddroute");
    String wsaddroute1=Request.getParameter("wsaddroute1");
    String wsaddroute2=Request.getParameter("wsaddroute2");
    String wsaddroute3=Request.getParameter("wsaddroute3");
    String wsaddroute4=Request.getParameter("wsaddroute4");
    String comment=Request.getParameter("txtComment");
    String ws_comment=Request.getParameter("txtWsComment");
    String ft_route_code=Request.getParameter("ft_route_code");
    String ftAddroute=Request.getParameter("ftAddroute");
    String ftAddroute1=Request.getParameter("ftAddroute1");
    String ftAddroute2=Request.getParameter("ftAddroute2");
    String ftAddroute3=Request.getParameter("ftAddroute3");
    String ftAddroute4=Request.getParameter("ftAddroute4");
    String ftAddroute5=Request.getParameter("ftAddroute5");
    
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
        OiMaintainService.CheckExistBomDup(fm.getSid(),
        		bodyversion,
        		pincount,
        		pkgtype,
        		maskopt,
                        maskoptrev,
                        codeno,
                        routetype,
        		sortroutecode,
        		ft_route_code);
    String forward=null;
    try{
      if (!flag){
        OiMaintainService.InsertBomDup(fm, productbody,
                                       pincount, pkgtype, maskopt, maskoptrev,
                                       codeno, routetype, sortroutecode, wsroute,
                                       wsaddroute, wsaddroute1, wsaddroute2, wsaddroute3, wsaddroute4, comment, ft_route_code,
                                       ftroute,ftAddroute, ftAddroute1, ftAddroute2, ftAddroute3, ftAddroute4, ftAddroute5, ws_comment);
        com.mxic.oiplus.oimaintain.OiMaintainService.submit(fm.getSid(), "TF_BOM_ROUTE", "N");
        forward="InsertBomDup";
      } else {
        Request.setAttribute("closeWindow","false");
        Request.setAttribute("message","資料表中有重複的資料");
        forward="SameDataExist";
      }
    } catch (Exception e){
      e.printStackTrace();
    }
    try {
    	if(forward.equals("InsertBomDup")){
    		servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/bomProductRouteActionX.do?sid=" + sid));
    		return null;
    	}	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return actionMapping.findForward(forward);
  }
}
