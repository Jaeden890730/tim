package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.oiplus.au.User;

public class SearchAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    OiMaintainStep ois = null;
    HttpSession session = Request.getSession();
    String sid = Request.getParameter("sid");
    /*if(sid == null){
      sid = fm.getSid();
    }else{*/
      ois = OiMaintainService.SearchFunction(sid);
      if (ois != null) {
        fm.setSid(ois.getSid());
        fm.setBrand(ois.getBrand());
        fm.setProductbody(ois.getProduct_body());
        fm.setVersion(ois.getVersion());
      }
    //}
    ois.setAuth((User)session.getAttribute("user"));
    String forward = "success";
    Request.setAttribute(actionMapping.getName(),fm);
    Request.setAttribute(forward,ois);

    return actionMapping.findForward(forward);
  }
}
