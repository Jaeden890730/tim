package com.mxic.oiplus.oimaintain;

import java.io.IOException;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.xtrarom.oimaintain.OiMaintainService;

public class AddNewRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String product_body = Request.getParameter("txtPbname");
    String sid=Request.getParameter("sid");
	OiMaintainStep ois = null;
	ois = com.mxic.oiplus.oimaintain.OiMaintainService.SearchFunction(sid);
	if (ois != null) {
        fm.setSid(ois.getSid());
        fm.setBrand(ois.getBrand());
        fm.setProductbody(ois.getProduct_body());
        fm.setVersion(ois.getVersion());
    }
	
    if (product_body == null || product_body.equals("")) {
    	fm.setRoutename("");
    	fm.setReadonly("");
    	Request.setAttribute("sid",fm.getSid());
    	Request.setAttribute(actionMapping.getName(),fm);
    	return actionMapping.findForward("success");
    } else {
        if (!com.mxic.oiplus.xtrarom.oimaintain.OiMaintainService.CheckProductExist(fm.getSid(),product_body)) {
            fm.setMessage(product_body+"不存在!");
        } else {
            if (com.mxic.oiplus.xtrarom.oimaintain.OiMaintainService.CopyRouteFromProduct(fm.getSid(), fm.getProductbody(), fm.getVersion(), product_body))
              fm.setMessage("Copy 完成!");
            else
              fm.setMessage("Copy 失敗!");
        }
        Request.setAttribute(actionMapping.getName(), fm);
        try {
      		servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/productRouteAction.do?sid="+sid));
      	} catch (IOException e) {
      		// TODO Auto-generated catch block
      		e.printStackTrace();
      	}
        //return actionMapping.findForward("copyok");
        return null;
    }
  }
}

