package com.mxic.oiplus.xtrarom.oimaintain;


import javax.servlet.http.*;

import org.apache.struts.action.*;

public class SaveSubmitBomAction extends Action {

  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
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
    String[] wsaddroute1 = Request.getParameterValues("wsaddroute1");
    String[] wsaddroute2 = Request.getParameterValues("wsaddroute2");
    String[] wsaddroute3 = Request.getParameterValues("wsaddroute3");
    String[] wsaddroute4 = Request.getParameterValues("wsaddroute4");
    String[] ftcomment = Request.getParameterValues("txtFtComment");
    String[] wscomment = Request.getParameterValues("txtWsComment");
    String[] ftAddroute = Request.getParameterValues("ftAddroute");
    String[] ftAddroute1 = Request.getParameterValues("ftAddroute1");
    String[] ftAddroute2 = Request.getParameterValues("ftAddroute2");
    String[] ftAddroute3 = Request.getParameterValues("ftAddroute3");
    String[] ftAddroute4 = Request.getParameterValues("ftAddroute4");
    String[] ftAddroute5 = Request.getParameterValues("ftAddroute5");
    String[] ftroutecode = Request.getParameterValues("ft_route_code");
    try{
      OiMaintainService.InsertBomRoute( ftroutecode,
                                        tag, fm, epnbody, brand, productbody,
                                        codeno, pincount,
                                        pkgtype, grade,
                                        productclass, ftwithcode, maskopt,
                                        maskoptrev, sortroutecode, wsroute,
                                        wsaddroute, wsaddroute1, wsaddroute2, wsaddroute3, wsaddroute4, ftcomment, id, ftAddroute, ftAddroute1, ftAddroute2, ftAddroute3, ftAddroute4, ftAddroute5, wscomment);

      ProTestRouteBean[] ptrb = OiMaintainService.GetRoute(sid);

      if (ptrb != null && ptrb.length>0){
        fm.setSid(sid);
        fm.setProductbody(ptrb[0].getProductbody());
        fm.setBrand(ptrb[0].getBrand());
      }

      if (flag.equals("submit")){
        OiMaintainService.submitBomProductRoute(fm.getSid());
      }
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/bomProductRouteActionX.do?sid=" + sid));
    } catch (Exception e){
      e.printStackTrace();
    }
    //return actionMapping.findForward("SaveBomRoute");
    return null;
  }
}
