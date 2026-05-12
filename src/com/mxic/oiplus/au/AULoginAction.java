package com.mxic.oiplus.au;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.sql.Connection;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.mxic.oiplus.util.MessageDef;
import com.mxic.tdsplus.resource.DBConnection;
import com.mxic.tdsplus.resource.TDSResource;
import com.mxic.tdsplus.util.TDSLogger;


public class AULoginAction extends Action {

    /**
     * This is the method called on by ActionServlet
     * when a request is made.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        AULoginForm fm = (AULoginForm) form;
        Authority aufm = new Authority();
        String forward = "failure";
        int return_flag = 100;
        String logout = request.getParameter("logout");
        HttpSession session = request.getSession();
        session.removeAttribute("user_authority");
        session.removeAttribute("FlowStock");
        session.removeAttribute("critMap");
        session.removeAttribute("SubFlowStock");
        session.removeAttribute("SubMap");
        session.removeAttribute("productType");
        if (logout != null && logout.equals("logout")) {
            forward = "logout";
        } else if (fm.getAction_type().equals("login")) {
            /*
            if (AUService.checkLogin(fm, aufm)) {
                session.setAttribute("user_authority", aufm);
                User currentUser = new User();
                currentUser.setUserId(aufm.getUser_id());
                currentUser.setUserName(aufm.getUser_name());
                currentUser.setTdsGroup("group");
                currentUser.setDeptName(aufm.getDept_name());
                currentUser.setDeptNo(aufm.getDept_id());
                currentUser.setEmpNo(aufm.getEmployee_no());
                currentUser.setDeptGroup(aufm.getDept_group());
                session.setAttribute("user", currentUser);
                if(fm.getRedirectUrl()!=null && fm.getRedirectUrl().length()>0){
		            return new ActionForward(fm.getRedirectUrl());
		        }          
		    */    
            Connection conn = null;
            User currentUser = new User();
            try {
                conn = DBConnection.getConnection();
                Properties properties = TDSResource.getProperties("TDS");
                com.mxic.peis.au.AUService service = new com.mxic.peis.au.AUService(request, conn, "TIM", null, properties, true, TDSLogger.class);
                if (service.login(fm, aufm, currentUser)) {
                    if(fm.getRedirectUrl()!=null && fm.getRedirectUrl().length()>0){
                        return new ActionForward(fm.getRedirectUrl());
                    }          
                    forward = "success";
                }
            } catch (Exception e) {
                TDSLogger.println(e);
            } finally {
                DBConnection.close(conn);
            }
        } else if (fm.getAction_type().equals("changePassword")) {
            if (AUService.checkLogin(fm, aufm)) {
                fm.setUser_id(aufm.getUser_id());
                return_flag = AUService.changeUserPassword(fm);
            }
            forward = "changePassword";
        }

        fm.setReturn_flag(MessageDef.returnMessage(return_flag));
        fm.setPassword("");
        //System.out.println(mapping.getName());
        request.setAttribute(mapping.getName(), fm);
        return mapping.findForward(forward);
    }
}
