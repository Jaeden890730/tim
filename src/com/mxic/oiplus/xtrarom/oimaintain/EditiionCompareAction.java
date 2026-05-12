package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class EditiionCompareAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {
    EditiionCompareActionForm editiionCompareActionForm =
        (EditiionCompareActionForm) actionForm;

    return actionMapping.findForward("reload");
  }
}
