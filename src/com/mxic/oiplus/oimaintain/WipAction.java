package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class WipAction extends Action {

  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String sid = Request.getParameter("sid");
    String flag = Request.getParameter("flag");
    String ctrl_type = Request.getParameter("ctrl_type");
    String[] options = Request.getParameterValues("option_list");
    String steps = Request.getParameter("steps");
    String[] pgname1 = Request.getParameterValues("pgname1");
    String[] pgname2 = Request.getParameterValues("pgname2");
    String[] pgname3 = Request.getParameterValues("pgname3");
    String[] pgname4 = Request.getParameterValues("pgname4");
    String[] pgname5 = Request.getParameterValues("pgname5");
    String[] p1 = Request.getParameterValues("p1");
    String[] p2 = Request.getParameterValues("p2");
    String[] p3 = Request.getParameterValues("p3");
    String[] p4 = Request.getParameterValues("p4");
    String[] p5 = Request.getParameterValues("p5");
    try{

      StringBuffer optionlist = new StringBuffer("");
      if (options != null) {
        for (int i = 0; i < options.length; i++)
          optionlist.append(options[i]+",");
      }

      if (flag.equals("submit") || flag.equals("save")) {
        OiMaintainService.saveWip(sid, fm.getProductbody(),
                                  optionlist.toString(), ctrl_type, steps,
                                  pgname1, pgname2, pgname3, pgname4, pgname5,
                                  p1, p2, p3, p4, p5, flag);
      }
      else if (flag.equals("getwip"))
        OiMaintainService.getWip(sid, fm.getProductbody(),
                                 optionlist.toString(), ctrl_type);

      if (sid != null){
        FTTestActionForm ftm = FTService.getInfo(Integer.parseInt(sid));
        Request.setAttribute("sid",sid);
        Request.setAttribute("pb_body",fm.getProductbody());
        Request.setAttribute("brand",fm.getBrand());
        Request.setAttribute("version",fm.getVersion());
        Request.setAttribute("product_type",ftm.getProductType());
      }

    } catch (Exception e){
      e.printStackTrace();
    }
    return actionMapping.findForward("GetWip");
  }
}
