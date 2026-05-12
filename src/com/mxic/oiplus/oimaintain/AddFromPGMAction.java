package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class AddFromPGMAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String k = Request.getParameter("sid");
    OiMaintainStep ois = OiMaintainService.SearchFunction(k);
    if(ois != null){
      fm.setSid(ois.getSid());
      fm.setBrand(ois.getBrand());
      fm.setProductbody(ois.getProduct_body());
      fm.setVersion(ois.getVersion());
    }
    String pro_b=fm.getProductbody();
    String pgmflag = Request.getParameter("pgmflag");
    /*get all available mask option of the selected product_body*/
    wtbean[] wtb = OiMaintainService.GetAvailableMaskOption(pro_b, pgmflag);

    Request.setAttribute(actionMapping.getName(),fm);
    Request.setAttribute("AddPGM",wtb);
    Request.setAttribute("pgmflag",pgmflag);
    return actionMapping.findForward("AddPGM");
  }
}
