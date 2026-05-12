package com.mxic.oiplus.xtrarom.oimaintain;

import java.sql.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import com.mxic.oiplus.au.*;
import com.mxic.oiplus.resource.*;

public class wsTestParameterAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String k=Request.getParameter("sid");
    OiMaintainStep ois = null;
    /*if (k == null){
      k = fm.getSid();
    } else {*/
      ois = OiMaintainService.SearchFunction(k);
      if (ois != null){
        fm.setSid(ois.getSid());
        fm.setBrand(ois.getBrand());
        fm.setProductbody(ois.getProduct_body());
        fm.setVersion(ois.getVersion());
        fm.setCreator(ois.getCreator());
      }
    //}

    String pro_b = fm.getProductbody();
    String br = fm.getBrand();
    String sid = fm.getSid();
    String version = fm.getVersion();
    WsTestBean[] wtb = null;
    Connection conn = null;
    String forward = null;
    String flag = null;
    try{
      conn = DBConnection.getConnection();
      boolean chkExist = OiMaintainService.CheckWS_TX_Exist(sid, pro_b, br);
      boolean chkWS = OiMaintainService.CheckWS_Exist(sid, pro_b, br, version);
//      boolean chkPGexist = OiMaintainService.ChkPGToWsTx(pro_b, br, sid, version);
      /*如果WS_TX有資料，就直接從WS_TX撈資料*/
      if (!chkExist && chkWS) {
          //如果WS有資料,從WS拉資料
          OiMaintainService.WsToWsTx(pro_b, br, sid, version);
      }
      wtb = OiMaintainService.SelectAllFromWS(sid, conn, pro_b, br, version);
      forward = "WsParam";
    } catch (Exception e){
      e.printStackTrace();
    } finally {
      DBConnection.close(conn);
    }
    /*get  creator,sponsor1,sponsor2 from the bean*/
    ois = OiMaintainService.SearchFunction(fm.getSid());
    String cre = ois.getCreator();
    String sp1 = ois.getSponsor_1();
    String sp2 = ois.getSponsor_2();
    /**********************************************/
    /*get user name from session bean*/
    User user = (User) Request.getSession().getAttribute("user");
    String un = user.getUserName();
    /*********************************/
    /*get the status of the product (processing or in approve or released)*/
    boolean status_apply_bo = FTService.status_apply(Integer.parseInt(fm.getSid()));
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
    Request.setAttribute("WsParam",wtb);
    return actionMapping.findForward(forward);
  }
}
