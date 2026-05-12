package com.mxic.oiplus.au;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class AUChangeUserPasswordAction extends Action {
    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest servletRequest,
                                 HttpServletResponse servletResponse) {
        AULoginForm fm = (AULoginForm) actionForm;
        Authority aufm = new Authority();
        String message = "";
        String forward = "";
        if (AUService.checkLogin(fm, aufm)) {
            fm.setUser_id(aufm.getUser_id());
            int flag= AUService.changeUserPassword(fm);
            //System.out.println(flag);
            if (flag == 1){
                message = "您成功修改您的密碼";
                forward = "success";
            }else{
                message = "修改您的密碼失敗!!";
                forward = "failed";
            }
        }else{
            message = "密碼錯誤!!";
            forward = "failed";
        }
        servletRequest.setAttribute("message",message);
        return actionMapping.findForward(forward);
    }
}
