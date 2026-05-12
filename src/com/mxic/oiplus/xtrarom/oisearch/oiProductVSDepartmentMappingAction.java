package com.mxic.oiplus.xtrarom.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiProductVSDepartmentMappingAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    oiProductVSDepartmentMappingAForm MappingAForm =
        (oiProductVSDepartmentMappingAForm) actionForm;

    String forward = "";
    String btControl = MappingAForm.getBtControl();
    String ra_select = MappingAForm.getRa_select();

    if (btControl.equals("bt_delete")){
      servletRequest.setAttribute("list",ra_select);
      forward = "bt_delete";
    } else if(btControl.equals("bt_add")){
      forward = "bt_add";
    }
    return actionMapping.findForward(forward);
  }
}