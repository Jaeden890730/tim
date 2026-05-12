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

public class PBCTestParameterAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm_old = (ProTestRouteBeanAF) actionForm;
    if (fm_old.getProductbody() == null) {
    	OiMaintainStep ois = OiMaintainService.SearchFunction(fm_old.getSid());
    	fm_old.setProductbody(ois.getProduct_body());
    	fm_old.setBrand(ois.getBrand());
    	fm_old.setVersion(ois.getVersion());
    }
    PBCTestParameterForm fm = new PBCTestParameterForm();
    fm.setProduct_body(fm_old.getProductbody());
    fm.setBrand(fm_old.getBrand());
    fm.setVersion(fm_old.getVersion());
    fm.setSid(Integer.parseInt(fm_old.getSid()));
    String k=Request.getParameter("sid");
    OiMaintainStep ois = null;
    if (k == null){
      k = fm_old.getSid();
    } else {
      ois = OiMaintainService.SearchFunction(k);
      if (ois != null){
        fm.setSid(Integer.parseInt(ois.getSid()));
        fm.setBrand(ois.getBrand());
        fm.setProduct_body(ois.getProduct_body());
        fm.setVersion(ois.getVersion());
//        fm.setCreator(ois[0].getCreator());
      }
    }

    String pro_b = fm.getProduct_body();
    String br = fm.getBrand();
    String sid = Integer.toString(fm.getSid());
    String version = fm.getVersion();
    PBCTestParameterBean[] ptb = null;
    Connection conn = null;
    String forward = null;
    String flag = null;
    try{
      conn = DBConnection.getConnection();
      boolean chkExist = PBCService.CheckPBC_TX_Exist(sid);
      boolean chkPBC = PBCService.CheckPBC_Exist(sid, pro_b, br, version);
      boolean chkSubmit = com.mxic.oiplus.util.OIinformation.isSubmitted(sid, "TF_TEST_PARAMETER_PBC");
      /*如果PBC_TX沒有資料而且還沒有 submit，就從前一版PBC撈資料*/
      if (!chkExist && chkPBC && !chkSubmit){
    	  PBCService.PBCToPBCTx(conn, pro_b, br, sid, version);
      }
      ptb = PBCService.SelectAllFromPBC(sid, conn, pro_b, br, version);
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
    Request.setAttribute("pbcParam",ptb);
    forward = "pbcParam";
    return actionMapping.findForward(forward);
  }
}
