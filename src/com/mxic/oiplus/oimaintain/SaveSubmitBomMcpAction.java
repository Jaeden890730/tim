package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class SaveSubmitBomMcpAction extends Action {

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
    String[] fgwithcode = Request.getParameterValues("fgwithcode");
    String[] pincount = Request.getParameterValues("pincount");
    String[] pkgtype = Request.getParameterValues("pkgtype");
    String[] grade = Request.getParameterValues("grade");
    String[] productclass = Request.getParameterValues("productclass");
    String[] ftwithcode = Request.getParameterValues("ftroute");
    //String[] maskopt = Request.getParameterValues("maskopt");
    String[] dbwithcode = Request.getParameterValues("dbwithcode");
    String[] sortroutecode = Request.getParameterValues("txtRouteCode");
    String[] wsroute = Request.getParameterValues("wsroute");
    String[] wsaddroute = Request.getParameterValues("wsaddroute");
    String[] comment = Request.getParameterValues("txtComment");
    String[] wscomment = Request.getParameterValues("txtWsComment");
    String[] ftAddroute = Request.getParameterValues("ftAddroute");
    String[] ftAddroute2= Request.getParameterValues("ftAddroute2");
    String[] ftAddroute3 = Request.getParameterValues("ftAddroute3");
    String[] ftroutecode = Request.getParameterValues("ft_route_code");
    String[] salesform = Request.getParameterValues("salesForm");
    String[] endurance = Request.getParameterValues("endurance");
    String[] wsspecialcontrol = Request.getParameterValues("wsspecialcontrol");
    String[] quality_level = Request.getParameterValues("quality_level");
    String[] quality_level_comment = Request.getParameterValues("quality_level_comment");

    String[] component_no = Request.getParameterValues("component_no");
    String[] com_prod_body = Request.getParameterValues("com_prod_body");
    String[] com_mask_option = Request.getParameterValues("com_mask_option");
    String[] com_backend_option = Request.getParameterValues("com_backend_option");
    //String[] mcp_flag = Request.getParameterValues("mcp_flag");
    try{
      OiMaintainService.InsertBomRouteMcp( ftroutecode,
                                        tag, fm, epnbody, brand, productbody,
                                        beoption, fgwithcode, pincount,
                                        pkgtype, grade,
                                        productclass, ftwithcode,
                                        dbwithcode, sortroutecode, wsroute,
                                        wsaddroute, comment, id, ftAddroute, ftAddroute2, ftAddroute3, 
                                        wscomment, salesform, endurance, wsspecialcontrol, quality_level, quality_level_comment, 
                                        component_no, com_prod_body, com_mask_option, com_backend_option);

      ProTestRouteBean[] ptrb = OiMaintainService.GetRoute(sid);

      if (ptrb != null && ptrb.length>0){
        fm.setSid(sid);
        fm.setProductbody(ptrb[0].getProductbody());
        fm.setBrand(ptrb[0].getBrand());
      }

      if (flag.equals("submit")){
        OiMaintainService.submitBomMcpProductRoute(fm.getSid());
      }else if (flag.equals("save")){
          OiMaintainService.submit(sid, "TF_BOM_MCP_ROUTE", "N");
      }
    //HttpSession session = Request.getSession();
      //session.setAttribute("sid", sid);
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/bomProductRouteMcpAction.do?sid=" + sid));
    } catch (Exception e){
      e.printStackTrace();
    }
    //return actionMapping.findForward("SaveBomRoute");
    return null;
  }
}
