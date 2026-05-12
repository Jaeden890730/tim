package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class SaveSubmitBomReAction extends Action {

  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestReRouteBeanAF fm = (ProTestReRouteBeanAF) actionForm;
    String sid = Request.getParameter("sid");
    String flag = Request.getParameter("flag");
    String[] epnbody = Request.getParameterValues("epnbody");
    String[] id = Request.getParameterValues("id");
    String[] tag  = Request.getParameterValues("tag");
    String[] brand = Request.getParameterValues("brand");
    String[] productbody = Request.getParameterValues("productbody");
    String[] beoption = Request.getParameterValues("beoption");
    String[] codeno = Request.getParameterValues("codeno");
    String[] pincount = Request.getParameterValues("pincount");
    String[] pkgtype = Request.getParameterValues("pkgtype");
    String[] grade = Request.getParameterValues("grade");
    String[] productclass = Request.getParameterValues("productclass");
    String[] ftwithcode = Request.getParameterValues("ftroute");
    String[] routetype = Request.getParameterValues("route_type");
    String[] maskopt = Request.getParameterValues("maskopt");
    String[] maskoptrev = Request.getParameterValues("maskoptrev");
    String[] dbwithcode = Request.getParameterValues("dbwithcode");
    String[] sortroutecode = Request.getParameterValues("txtRouteCode");
    String[] wsroute = Request.getParameterValues("wsroute");
    String[] wsaddroute = Request.getParameterValues("wsaddroute");
    String[] ftcomment = Request.getParameterValues("txtFtComment");
    String[] wscomment = Request.getParameterValues("txtWsComment");
    String[] ftAddroute = Request.getParameterValues("ftAddroute");
    String[] recyclecode = Request.getParameterValues("recycle_code");
    try{
      OiMaintainService.InsertBomReRoute( recyclecode,
                                        tag, fm, epnbody, brand, productbody,
                                        codeno, pincount,
                                        pkgtype, grade,
                                        productclass, ftwithcode, maskopt,
                                        maskoptrev, ftcomment, id, ftAddroute);

      ProTestReRouteBean[] ptrb = OiMaintainService.GetReRoute(sid);

      if (ptrb != null && ptrb.length>0){
        fm.setSid(sid);
        fm.setProductbody(ptrb[0].getProductbody());
        fm.setBrand(ptrb[0].getBrand());
      }

      if (flag.equals("submit")){
        OiMaintainService.submitBomProductReRoute(fm.getSid());
      }
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/bomProductReRouteActionX.do?sid=" + sid));
    } catch (Exception e){
      e.printStackTrace();
    }
    //return actionMapping.findForward("SaveBomRoute");
    return null;
  }
}
