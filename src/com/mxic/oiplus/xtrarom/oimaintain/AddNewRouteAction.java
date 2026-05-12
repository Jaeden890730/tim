package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class AddNewRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String product_body = Request.getParameter("txtPbname");
    if (product_body == null)
      product_body = "";
    String sid = fm.getSid();


    if (product_body.equals("")) { // Add new route from route master
      fm.setRoutename("");
      fm.setReadonly("");
      Request.setAttribute(actionMapping.getName(), fm);
      fm.setMessage("");
      return actionMapping.findForward("success");
    } else { // copy routes from another product
      if (!OiMaintainService.CheckProductExist(sid,product_body)) {
        fm.setMessage(product_body+"不存在!");
      } else {
        if (OiMaintainService.CopyRouteFromProduct(sid, fm.getProductbody(), fm.getVersion(), product_body))
          fm.setMessage("Copy 完成!");
        else
          fm.setMessage("Copy 失敗!");
      }
      Request.setAttribute(actionMapping.getName(), fm);
      return actionMapping.findForward("copyok");
    }
  }
}

