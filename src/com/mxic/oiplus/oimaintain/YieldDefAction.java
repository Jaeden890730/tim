package com.mxic.oiplus.oimaintain;

import java.sql.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.au.*;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.TDSLogger;

public class YieldDefAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String k=Request.getParameter("sid");
    String proc_type=Request.getParameter("proc_type");
    String message=(String)Request.getSession().getAttribute("message");
    String message_alarm=(String)Request.getSession().getAttribute("message_alarm");

    OiMaintainStep ois = null;
    if (k == null){
      k = fm.getSid();
    } else {
      ois = OiMaintainService.SearchFunction(k);
      if (ois != null){
        fm.setSid(ois.getSid());
        fm.setBrand(ois.getBrand());
        fm.setProductbody(ois.getProduct_body());
        fm.setVersion(ois.getVersion());
        fm.setCreator(ois.getCreator());
      }
    }

    String pro_b = fm.getProductbody();
    String br = fm.getBrand();
    String sid = fm.getSid();
    String version = fm.getVersion();
    String pre_version = String.valueOf(Integer.parseInt(version)-1);
    YieldDefBean[] ydb = null;
    Connection conn = null;
    String forward = null;
    String show_priv = null;
    String productType = OiMaintainService.getProductType(sid);
    try{
      conn = DBConnection.getConnection();
      boolean chkTxExist = OiMaintainService.isEmpry(pro_b, br, version, "tf_yield_definition_tx", proc_type);//false
      boolean chkYield = OiMaintainService.isEmpry(pro_b, br, pre_version, "tf_yield_definition", proc_type);//true
      boolean chkSubmit = com.mxic.oiplus.util.OIinformation.isSubmitted(sid, "TF_YIELD_"+proc_type);//false

      /*如果 WS_YIELD_TX 沒有資料而且還沒有 submit，就從前一版撈資料*/
      if (!chkTxExist && chkYield && !chkSubmit) {//false, true, false
          YieldDefService.YieldToYieldTX(sid, pro_b, br, version, proc_type);
          if("WS".equals(proc_type))
        	  YieldDefService.copyYieldGroupItemsToTX(conn, sid, pro_b, br, version, proc_type);
          
      }
      ydb = YieldDefService.RetrieveYield(sid, conn, "_tx", proc_type, productType, true);
      /*if(ydb==null || ydb.length==0){
    	  ydb = YieldDefService.RetrieveYield(sid, conn, "", proc_type, productType);
      }*/
      forward = "yieldDef";
      if(!"WS".equals(proc_type)){
    	  forward = "yieldDefFT";
      }
    } catch (Exception e){
      TDSLogger.println(e);
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
    	show_priv = "Show";
    } else if (status_apply_bo && un.equals(sp1)){
    	show_priv = "Show";
    } else if (status_apply_bo && un.equals(sp2)){
    	show_priv = "Show";
    } else {
    	show_priv = "NotShow";
    }
    
    Request.setAttribute("product_type", productType);
    Request.setAttribute("show_priv",show_priv);
    Request.setAttribute(actionMapping.getName(),fm);
    Request.setAttribute("proc_type",proc_type);
    Request.setAttribute("message",message);
    Request.setAttribute("message_alarm",message_alarm);
    Request.setAttribute("yieldDef",ydb);
    return actionMapping.findForward(forward);
  }
}
