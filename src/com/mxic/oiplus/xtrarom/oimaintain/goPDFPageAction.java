package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.oiplus.oimaintain.OiMaintainService;

import com.mxic.oiplus.oimaintain.OiMaintainStep;
import com.mxic.oiplus.oimaintain.ProTestRouteBeanAF;
import com.mxic.oiplus.oimaintain.WsTestBean;

public class goPDFPageAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse httpServletResponse) {
    /**@todo: complete the business logic here, this is just a skeleton.*/

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String pro_b=Request.getParameter("pro_b");
    String brand=Request.getParameter("brand");
    String version=Request.getParameter("version");
    String tx=Request.getParameter("tx");
    if (pro_b==null || brand==null || version==null){
      pro_b=fm.getProductbody();
      brand=  fm.getBrand();
      version=fm.getVersion();
    } else {
      OiMaintainStep[] ois = com.mxic.oiplus.oimaintain.OiMaintainService.SearchFunction2(version,brand,pro_b);
      if(ois!=null && ois.length>0){
        fm.setSid(ois[0].getSid());
        fm.setBrand(ois[0].getBrand());
        fm.setProductbody(ois[0].getProduct_body());
        fm.setVersion(ois[0].getVersion());
        fm.setPackage_component(ois[0].getPackage_component());
      }
    }

    WsTestBean[] wtb= com.mxic.oiplus.oimaintain.OiMaintainService.VendorName(fm.getSid());
    if("tx".equals(tx))
		wtb = OiMaintainService.VendorNameTX(fm.getSid());
	
    Request.setAttribute("AvailVendor",wtb);
    Request.setAttribute("proTestRouteBeanAF", fm);
    if("tx".equals(tx))
    	return actionMapping.findForward("AvailVendorTx");	
    else

    return actionMapping.findForward("AvailVendor");
  }
}