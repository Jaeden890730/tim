package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;
import org.apache.struts.action.*;

public class FTTestAddCopyAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {
      FTTestAddCopyActionForm fTTestAddCopyActionForm = (FTTestAddCopyActionForm) actionForm;
      FTService service = new FTService();

      String listControl2= fTTestAddCopyActionForm.getListControl2();

      if (listControl2.equals("copy")) {
          return actionMapping.findForward("success");
      } else {
          return actionMapping.findForward("fail");
      }
  }
}
