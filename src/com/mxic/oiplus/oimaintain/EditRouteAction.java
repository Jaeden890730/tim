package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.oisearch.TFDescriptionBean;
import com.mxic.oiplus.oisearch.oiSearchService;


public class EditRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest request,
                               HttpServletResponse servletResponse) {
    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;

    String RN= request.getParameter("RN");
    String sid=request.getParameter("sid");
    OiMaintainStep ois = OiMaintainService.SearchFunction(sid);
    if(ois != null){
      fm.setSid(ois.getSid());
      fm.setBrand(ois.getBrand());
      fm.setProductbody(ois.getProduct_body());
      fm.setVersion(ois.getVersion());
    }
    ProTestRouteBean[] pm = OiMaintainService.GetRouteStep(sid,RN);
    fm.setRoutename(RN);
    String productType = OiMaintainService.getProductType(sid);

    request.setAttribute(actionMapping.getName(),fm);
    request.setAttribute("sid",sid);
    request.setAttribute("EditRoute",pm);
    TFDescriptionBean[] stepname = oiSearchService.SelectStepNameFTSTART();
    request.setAttribute("stepname", stepname);
    request.setAttribute("product_type", productType);
    return actionMapping.findForward("EditRoute");
  }
}
