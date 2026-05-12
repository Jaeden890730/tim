package com.mxic.oiplus.au;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class AuthorityAction extends Action {
    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest servletRequest,
                                 HttpServletResponse servletResponse) {
        return actionMapping.findForward("success");

    }
}
