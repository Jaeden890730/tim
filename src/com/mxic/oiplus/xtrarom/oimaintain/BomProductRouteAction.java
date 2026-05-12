package com.mxic.oiplus.xtrarom.oimaintain;

import java.sql.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import com.mxic.oiplus.au.*;
import com.mxic.oiplus.resource.*;

public class BomProductRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String k = Request.getParameter("sid");
    String d = (String)Request.getParameter("delete");  //flag is Y if come from DeleteBomRouteTxAction.java
    if(d == null) d = "N";
    OiMaintainStep ois = null;
    String f = null;
    /*if the sid doesn't come with the web link then get the sid from ActionForm ProTestRouteBeanAF*/
    //if (k == null){
    //  ois=OiMaintainService.SearchFunction(fm.getSid());
    //} else {
      ois = OiMaintainService.SearchFunction(k);
      if(ois != null){
        fm.setSid(ois.getSid());
        fm.setBrand(ois.getBrand());
        fm.setProductbody(ois.getProduct_body());
        fm.setVersion(ois.getVersion());
        fm.setCreator(ois.getCreator());
      }
    //}
    String sid = fm.getSid();
    String pro_b = fm.getProductbody();
    String br = fm.getBrand();
    String version = fm.getVersion();
    BomProductRouteBean[] bom = null;
    String forward=null;
    Connection conn=null;
    try{
      conn = DBConnection.getConnection();
      /*check if tf_bom_route_xrom has data for selected product_code, and version*/
      boolean flag = OiMaintainService.CheckExistBomRoute(sid, pro_b, version);

      /*check if tf_bom_route_xrom_tx has data for selected product_code, and version*/
      boolean f2 = OiMaintainService.CheckExistTX(sid, conn, pro_b);

      /*check if ChkProdEpn has data for selected product_code*/
      boolean ChkProdEpn = OiMaintainService.ChkProdEpn(pro_b, sid, version);
      if(d.equals("Y")) {
          bom = OiMaintainService.BomRouteFirstVersion(pro_b, sid, version);
          forward = "BomProductRoute";
      } else {
          if (f2) {/*if tf_bom_route_xrom_tx has data, just get the data from tf_bom_route_xrom_tx*/
            bom = OiMaintainService.BomRouteFirstVersion(pro_b, sid, version);
            forward = "BomProductRoute";
          } else {
            /*if tf_bom_route_xrom_tx has no data but tf_bom_route_xrom has data,
                   copy the data from tf_bom_route_xrom to tf_bom_route_xrom_tx*/
            if (flag){
                OiMaintainService.RouteToRouteTx(pro_b,sid,version);
              if (ChkProdEpn)
                OiMaintainService.EPNtoBomTx(pro_b, conn, sid,version);
              bom = OiMaintainService.BomRouteFirstVersion(pro_b, sid, version);
              forward = "BomProductRoute";
            } else {
              if (!ChkProdEpn){
                forward = "nodata";
                Request.setAttribute("closeWindow","false");
                Request.setAttribute("message","目前無資料");
              } else {
                OiMaintainService.EPNtoBomTx(pro_b, conn, sid, version);
                bom = OiMaintainService.BomRouteFirstVersion(pro_b, sid, version);
                forward = "BomProductRoute";
              }
            }
          }
      }
    } catch(Exception e){
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
    if (status_apply_bo && un.equals(cre) ){
      f = "Show";
    } else if (status_apply_bo && un.equals(sp1)){
      f = "Show";
    } else if (status_apply_bo && un.equals(sp2)){
      f = "Show";
    } else {
      f = "NotShow";
    }
    Request.setAttribute("productType", OiMaintainService.getProductType(sid));
    Request.setAttribute("flag",f);
    Request.setAttribute("pro_b",fm.getProductbody());
    Request.setAttribute("brand",fm.getBrand());
    Request.setAttribute(actionMapping.getName(),fm);
    Request.setAttribute("BomProductRoute",bom);
    return actionMapping.findForward(forward);
  }
}
