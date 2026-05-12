package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import com.mxic.oiplus.au.*;

public class ProductRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse Response) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String k = Request.getParameter("sid");
    String flag = "";
    ProTestRouteBean[] ptrb = null;
    //if (k == null){
    //	k = (String)Request.getSession().getAttribute("sid");
    //}
    if (k == null){
      k=fm.getSid();
    } else {
      OiMaintainStep ois = OiMaintainService.SearchFunction(k);
      if(ois != null){
        fm.setSid(ois.getSid());
        fm.setBrand(ois.getBrand());
        fm.setProductbody(ois.getProduct_body());
        fm.setVersion(ois.getVersion());
      }
    }

    try{
      boolean lastVersion =
          OiMaintainService.CheckExistProductRoute(fm.getProductbody(),
                                                   fm.getBrand(),
                                                   fm.getVersion());
      boolean ProRouteExist =
          OiMaintainService.CheckExistProductRouteTX(fm.getSid());

      /*if tf_product_route_tx has data of selected product_body,brand,version then just get the data from
           tf_product_route_tx*/
      if (ProRouteExist){
        ptrb = OiMaintainService.GetRoute(k);
      } else {/*if no data in tf_product_route_tx check if tf_product_route has last version's data*/
        if (lastVersion) {/*if tf_product_route has data then copy it into _TX table first*/
          OiMaintainService.ProRouteToProRouteTx(fm.getProductbody(),
                                                 fm.getBrand(),
                                                 fm.getSid(),
                                                 fm.getVersion());
          ptrb = OiMaintainService.GetRoute(k);
        } else {
          ptrb = OiMaintainService.GetRoute(k);
        }
      }
    } catch (Exception e){
      e.printStackTrace();
    }
    /*get  creator,sponsor1,sponsor2 from the bean*/
    OiMaintainStep ois = OiMaintainService.SearchFunction(fm.getSid());
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
    return actionMapping.findForward("TestRoute");
  }
}
