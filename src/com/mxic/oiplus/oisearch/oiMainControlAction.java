package com.mxic.oiplus.oisearch;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.oiplus.au.Authority;
import com.mxic.oiplus.au.User;
import com.mxic.oiplus.oimaintain.OiMaintainService;
import com.mxic.oiplus.oimaintain.OiMaintainStep;
import com.mxic.oiplus.util.TDSLogger;

public class oiMainControlAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    oiMainControlAForm controlAFrom = (oiMainControlAForm) actionForm;
    String listControl = controlAFrom.getListControl();
    String ra_select = controlAFrom.getRa_select();
    String txt_productbody = controlAFrom.getTxt_productbody();
    String version = controlAFrom.getVersion();
    String sid = controlAFrom.getSid();
    String creator = controlAFrom.getCreator();
    String status = controlAFrom.getStatus();
    String forward = "";
    HttpSession session = servletRequest.getSession();
    String productType = controlAFrom.getProductType();
    if ((productType == null) || productType.equals("null"))
    	productType = (String)session.getAttribute("productType");
    controlAFrom.setProductType(productType);

    if (listControl.equals("bt_t_r_d")) {
      return actionMapping.findForward("bt_t_r_d");
    } else if (listControl.equals("bt_delete")) {
//      HttpSession session = servletRequest.getSession();
      Authority temp = (Authority) session.getAttribute("user_authority");
      OiMaintainService.DeleteOI(sid);
      productType = (String)session.getAttribute("productType");
      IFInformationBean[] listall = oiSearchService.SelectIFinformation(1, productType);
      servletRequest.setAttribute("delete_data", "success");
      servletRequest.setAttribute("list", listall);
      return actionMapping.findForward("bt_listall");
    } else if (listControl.equals("bt_return_process")) {
//      HttpSession session = servletRequest.getSession();
      Authority temp = (Authority) session.getAttribute("user_authority");
      int return_status = OiMaintainService.ReturnOItoProcess(sid);
      if (return_status == 1)
        servletRequest.setAttribute("return_data", "success");
      else if (return_status == -1)
        servletRequest.setAttribute("return_data", "failure");
      else
        servletRequest.setAttribute("return_data", "abnormal");
      productType = (String)session.getAttribute("productType");
      IFInformationBean[] listall = oiSearchService.SelectIFinformation(1, productType);
      servletRequest.setAttribute("list", listall);
      return actionMapping.findForward("bt_listall");
    } else if (listControl.equals("bt_p_v_d")) {
//      HttpSession session = servletRequest.getSession();
      Authority temp = (Authority) session.getAttribute("user_authority");
      if (temp != null) {
        String dept_id = temp.getDept_id();
        TFProdDeptBean[] list = oiSearchService.SelectProdOFDept(dept_id);
        servletRequest.setAttribute("list", list);
      }
      return actionMapping.findForward("bt_p_v_d");
    } else if (listControl.equals("bt_listall")) {
      productType = (String)session.getAttribute("productType");
      IFInformationBean[] listall = oiSearchService.SelectIFinformation(1, productType);
      servletRequest.setAttribute("list", listall);
      return actionMapping.findForward("bt_listall");
    } else if (listControl.equals("bt_release")) {
      productType = (String)session.getAttribute("productType");
      IFInformationBean[] release = oiSearchService.SelectIFinformation(2, productType);
      servletRequest.setAttribute("list", release);
      return actionMapping.findForward("bt_release");
    } else if (listControl.equals("bt_processing")) {
      productType = (String)session.getAttribute("productType");
      IFInformationBean[] process = oiSearchService.SelectIFinformation(3, productType);
      servletRequest.setAttribute("list", process);
      return actionMapping.findForward("bt_processing");
    } else if (listControl.equals("bt_apply")) {
      productType = (String)session.getAttribute("productType");
      IFInformationBean[] apply = oiSearchService.SelectIFinformation(4, productType);
      servletRequest.setAttribute("list", apply);
      return actionMapping.findForward("bt_apply");
    } else if (listControl.equals("bt_oi_search")) {
      IFInformationBean[] listall =
          oiSearchService.SelectIFinformation(txt_productbody, ra_select);
      servletRequest.setAttribute("list", listall);
      return actionMapping.findForward("bt_oi_search");
    } else if (listControl.equals("bt_oi_maintain")) {
      IFInformationBean[] bean = oiSearchService.SelectLastIFinformation(
          txt_productbody,ra_select);
      if (bean == null) {
        IFInformationBean bean2 = new IFInformationBean();
        bean2.setProduct_body(txt_productbody);
        bean2.setBrand(ra_select);
        bean2.setVersion("0");
        ArrayList list = new ArrayList();
        list.add(bean2);
        servletRequest.setAttribute("list", list);
        return actionMapping.findForward("add_new_version");
      } else if (bean[0].getStatus().equals("已生效")) {
        bean[0].setVersion(Integer.toString(Integer.parseInt(bean[0].
            getVersion()) + 1));
        TDSLogger.println(bean[0].getVersion());
        servletRequest.setAttribute("list", bean);
        return actionMapping.findForward("add_new_version");
      } else if ((bean[0].getStatus().equals("處理中")) ||
                 (bean[0].getStatus().equals("會簽中"))) {
        IFInformationBean[] listall =
            oiSearchService.SelectSearchIFinformation(txt_productbody, ra_select);
        servletRequest.setAttribute("bt_oi_maintain", listall);
        return actionMapping.findForward("bt_oi_maintain");
      } else {
        return actionMapping.findForward("failed");
      }
    } else if (listControl.equals("bt_oi_migration")) {
      return actionMapping.findForward("bt_oi_migration");
    } else if (listControl.equals("table_search")) {
      //"已生效"進入查詢，"會簽中"||"處理中"進入維護
      TDSLogger.println(status);
      if (status.equals("已生效")||status.equals("已失效")) {
        IFInformationBean listall =
            oiSearchService.SelectOneIFinformation(sid, txt_productbody, ra_select, version);
        listall.setAuth((User)session.getAttribute("user"));
        servletRequest.setAttribute("result", listall);
        return actionMapping.findForward("table_search");
      } else if(status.equals("會簽中")){
        OiMaintainStep ois = OiMaintainService.SearchFunction(sid);
        servletRequest.setAttribute("success", ois);
        ois.setAuth((User)session.getAttribute("user"));
        return actionMapping.findForward("OiInProgress");
      } else {
        OiMaintainStep ois = OiMaintainService.SearchFunction(sid);
        ois.setAuth((User)session.getAttribute("user"));
        servletRequest.setAttribute("success", ois);
        return actionMapping.findForward("table_search_maintain");
      }
    } else if (listControl.equals("add_new_version")) {
      String message="";
      boolean flag = oiSearchService.AddNew2IFInfo(controlAFrom);
      IFInformationBean[] rs = oiSearchService.SelectAllIFinformation(productType);
      
      
      String sid1 = com.mxic.oiplus.oimaintain.OiMaintainService.getSid(controlAFrom.getTxt_productbody(),controlAFrom.getBrand(),controlAFrom.getVersion());

      OiMaintainService.TFWaferlevelToProWaferlevelTx(controlAFrom.getTxt_productbody(),
              controlAFrom.getBrand(),
              sid1,
              controlAFrom.getVersion());   
      OiMaintainService.submit(rs[0].getSid(), "TF_PROD_WAFERLEVEL");
      
      if (flag == true) {
        message = "您成功的新增一筆〝" + txt_productbody + "〞";
        servletRequest.setAttribute("message", message);
        if (rs != null) {
          servletRequest.setAttribute("list", rs);
        }
        return actionMapping.findForward("new_version_success");
      } else {
        message = "新增〝" + txt_productbody + "〞這筆資料是失敗的，麻煩重新一次";
        servletRequest.setAttribute("message", message);
        return actionMapping.findForward("new_version_success");
      }
    } else {
      return actionMapping.findForward("failed");
    }
  }
}