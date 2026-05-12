package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class ResetWSParamAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {
    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String k = servletRequest.getParameter("sid");
    OiMaintainStep ois = OiMaintainService.SearchFunction(k);
    if(ois != null){
      fm.setSid(ois.getSid());
      fm.setBrand(ois.getBrand());
      fm.setProductbody(ois.getProduct_body());
      fm.setVersion(ois.getVersion());
    }
    try{
      OiMaintainService.ResetWSParameter(fm.getSid());
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(servletRequest.getContextPath() + "/OImaintain/wsTestParameterActionX.do?sid="+fm.getSid()));
    }catch(Exception e){
      e.printStackTrace();
    }
    //return actionMapping.findForward("ResetWS");
    return null;
  }
}
