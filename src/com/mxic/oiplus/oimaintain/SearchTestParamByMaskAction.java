package com.mxic.oiplus.oimaintain;

import java.io.IOException;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class SearchTestParamByMaskAction extends Action {
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
    WsTestBean[] wtb = null;
    wtbean[] wtb2 = null;
    WsTestBean[] wtb3 = null;
    String pro_b = fm.getProductbody();
    String[] chkbx = Request.getParameterValues("chkbx");
    String[] chkbx2 = Request.getParameterValues("chkbx2");
    String flag = Request.getParameter("flag");
    String[] pgm_id = Request.getParameterValues("pgm_id");
    String[] mask_option = Request.getParameterValues("mask_option");
    String[] test_type = Request.getParameterValues("test_type");
    String[] tester = Request.getParameterValues("tester");
    String[] site= Request.getParameterValues("site");
    String[] program_name = Request.getParameterValues("program_name");
    String[] pgm_special_control = Request.getParameterValues("pgm_special_control");
    String[] one_main_pgm_group_version = Request.getParameterValues("one_main_pgm_group_version");
    String pgmflag = Request.getParameter("pgmflag");
    if (flag.equals("search")){
      wtb = OiMaintainService.GetPGMByMaskOption(pro_b, chkbx, fm.getSid(),pgmflag);
      wtb2 = OiMaintainService.GetAvailableMaskOption(pro_b,pgmflag);
    } else if (flag.equals("copy")){
      OiMaintainService.CopyToWsTx(fm, chkbx2, pgm_id, mask_option, test_type, tester,
                                   site, program_name, pgm_special_control, one_main_pgm_group_version, pro_b);
      try {
		servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/wsTestParameterAction.do?sid="+fm.getSid()));
      } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
      }
      //return actionMapping.findForward("CopyFromPGM");
      return null;
    }
    Request.setAttribute("AddPGM",wtb2);
    Request.setAttribute("PGMResult",wtb);
    Request.setAttribute("pgmflag",pgmflag);
    return actionMapping.findForward("PGMResult");
  }
}
