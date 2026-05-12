package com.mxic.oiplus.xtrarom.oimaintain;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.oiplus.au.User;
import com.mxic.oiplus.resource.DBConnection;

public class MainRouteReAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    MainRouteReForm fm_old = (MainRouteReForm) actionForm;
    if (fm_old.getProduct_body() == null) {
    	OiMaintainStep ois = OiMaintainService.SearchFunction(String.valueOf(fm_old.getSid()));
    	fm_old.setProduct_body(ois.getProduct_body());
    	fm_old.setVersion(ois.getVersion());
    }
    MainRouteReForm fm = new MainRouteReForm();
    fm.setProduct_body(fm_old.getProduct_body());
    fm.setVersion(fm_old.getVersion());
    fm.setSid(fm_old.getSid());
    fm.setRoute_type(1);
    String k=Request.getParameter("sid");
    OiMaintainStep ois = null;
    if (k == null){
      k = Integer.toString(fm_old.getSid());
    } else {
      ois = OiMaintainService.SearchFunction(k);
      if (ois != null){
        fm.setSid(Integer.parseInt(ois.getSid()));
        fm.setProduct_body(ois.getProduct_body());
        fm.setVersion(ois.getVersion());
      }
    }

    String pro_b = fm.getProduct_body();
    String version = fm.getVersion();
    String sid = Integer.toString(fm.getSid());
    String route_type = Integer.toString(fm.getRoute_type());
    MainRouteReBean[] mrr = null;
    Connection conn = null;
    String forward = null;
    String flag = null;
    try{
      conn = DBConnection.getConnection();
      boolean chkExist = MainRouteService.CheckMainRoute_TX_Exist(sid, pro_b, version, route_type);
      boolean chkMainRoute = MainRouteService.CheckMainRoute_Exist(sid, pro_b, version, route_type);
      boolean chkSubmit = com.mxic.oiplus.util.OIinformation.isSubmitted(sid, 
    		  (route_type.equals("0")?"TF_MAIN_SUB":"TF_MAIN_REWORK"));
//      boolean chkPGexist = OiMaintainService.ChkPGToWsTx(pro_b, br, sid, version);
      /*如果MainRoute_TX有資料，就直接從MainRoute_TX撈資料*/
      if (!chkExist && chkMainRoute && !chkSubmit){
    	  MainRouteService.MainRouteToMainRouteTx(conn, pro_b, version, sid, route_type);
      }
      mrr = MainRouteService.SelectAllFromMainRoute_re(sid, conn, pro_b, version, route_type);
    } catch (Exception e){
      e.printStackTrace();
    } finally {
    	DBConnection.close(conn);
    	conn = null;
    }

    /*get  creator,sponsor1,sponsor2 from the bean*/
    ois = OiMaintainService.SearchFunction(sid);
    String cre = ois.getCreator();
    String sp1 = ois.getSponsor_1();
    String sp2 = ois.getSponsor_2();
    /**********************************************/
    /*get user name from session bean*/
    User user = (User) Request.getSession().getAttribute("user");
    String un = user.getUserName();
    /*********************************/
    /*get the status of the product (processing or in approve or released)*/
    boolean status_apply_bo = FTService.status_apply(fm.getSid());
    /**********************************************************************/
    /*If status is processing and user is the creator or sponsor1 or sponsor2, show the
            modification buttons in jsp pages*/////////
    if (status_apply_bo && un.equals(cre)){
      flag = "Show";
    } else if (status_apply_bo && un.equals(sp1)){
      flag = "Show";
    } else if (status_apply_bo && un.equals(sp2)){
      flag = "Show";
    } else {
      flag = "NotShow";
    }

    Request.setAttribute("flag",flag);
    Request.setAttribute(actionMapping.getName(),fm);
    Request.setAttribute("pro_b",fm.getProduct_body());
    Request.setAttribute("version",fm.getVersion());
    Request.setAttribute("mrrParam",mrr);
    forward = "mrrParam";
    return actionMapping.findForward(forward);
  }
}
