package com.mxic.oiplus.oimaintain;

import java.sql.Connection;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import com.mxic.oiplus.au.*;
import com.mxic.tdsplus.resource.DBConnection;

public class TestStepAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse Response) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String k = Request.getParameter("sid");
    String rn = Request.getParameter("RN");
    String rn1 = Request.getParameter("RN1");
    String flag = "";
    ProTestRouteBean[] ptrb = null;
    
    String[] rns = null;
    String[] rn1s = null;
    
    /*if (k == null){
      k=fm.getSid();
    } else {*/
      OiMaintainStep ois = OiMaintainService.SearchFunction(k);
      if(ois != null){
        fm.setSid(ois.getSid());
        fm.setBrand(ois.getBrand());
        fm.setProductbody(ois.getProduct_body());
        fm.setVersion(ois.getVersion());
      }
    //}
    Connection conn = null;
    try{
        
        conn = DBConnection.getConnection();
        conn.setAutoCommit(false);
        if(rn != null && rn.length() > 0) {
            rns = rn.split(",");
            
        }
        if(rn1 != null && rn1.length() > 0) {
            rn1s = rn1.split(",");
        }
      if(rns != null)
      for (int i = 0 ; i < rns.length; i++) {
          OiMaintainService.UpdateTestStep(conn, k,rn1s[i],rns[i]);
      }

      /*這個頁面直接參考tf_product_route_tx，不需要額外成立新的表格；另外也不需要從前版進一版，因為這個動作已經在route_mapping完成了*/
      ptrb = OiMaintainService.GetTestStepDef(conn, k);
     
    } catch (Exception e) {
        e.printStackTrace();
        DBConnection.rollback(conn);
    } finally {
        DBConnection.close(conn);
        conn = null;
    }
    /*get  creator,sponsor1,sponsor2 from the bean*/
    ois = OiMaintainService.SearchFunction(fm.getSid());
    String prod_type = OiMaintainService.getProductType(fm.getSid());
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
    if (status_apply_bo && un.equals(cre) ){
      flag = "Show";
    } else if (status_apply_bo && un.equals(sp1)){
      flag = "Show";
    } else if (status_apply_bo && un.equals(sp2)){
      flag = "Show";
    } else {
      flag = "NotShow";
    }
    /************************************************************************/
    Request.setAttribute(actionMapping.getName(), fm);
    Request.setAttribute("flag", flag);
    Request.setAttribute("TestRoute", ptrb);
    Request.setAttribute("prod_type", prod_type);
    Request.setAttribute("sid", fm.getSid());
    Request.setAttribute("rwkFinished", OiMaintainService.rwkFinished(fm.getSid()));
    Request.setAttribute("rwkInconsistent", OiMaintainService.rwkInconsistent(fm.getSid()));
    return actionMapping.findForward("TestStep");
  }
}
