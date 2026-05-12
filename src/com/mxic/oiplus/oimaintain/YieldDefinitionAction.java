package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import com.mxic.oiplus.util.OIinformation;

public class YieldDefinitionAction extends Action {
  public YieldDefinitionAction() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    YieldDefinitionActionForm YieldDefinitionActionForm = (YieldDefinitionActionForm) actionForm;
    int sid = YieldDefinitionActionForm.getSid();
    String pd_body = YieldDefinitionActionForm.getPd_body();
    String brand = YieldDefinitionActionForm.getBrand();
    String version = YieldDefinitionActionForm.getVersion();
    FormFile filename = YieldDefinitionActionForm.getFilename();
    String listControl = YieldDefinitionActionForm.getListControl();

    // Yield Definition 為共用頁面，jsp 需判別為何種產品
    FTTestActionForm ftm = FTService.getInfo(sid);
    servletRequest.setAttribute("product_type",ftm.getProductType());
    servletRequest.setAttribute("sid",""+sid);
    String result = "";

    if (listControl.equals("upload")) {
      // 驗證 upload 資料，若成功則置入 tf_yield_2tx
      result = YieldDefinitionService.upload_verify(sid, pd_body, brand, version, filename);

      if (!result.equals("success")) { // 驗證不成功則顯示訊息
        servletRequest.setAttribute("upload_message", result);
        return actionMapping.findForward("update_data"); // display page
      } else {
        // 驗證成功則顯示比對結果
        int presid = OIinformation.getPreviousVersion(sid);
    	YieldDefinitionActionForm[] comp =
            YieldDefinitionService.compare("U",sid,presid);
        // 如果上傳資料與現有資料相同，不需 upload
        if (comp.length == 0) {
          result = "<tr class=\"title1\"><td colspan=\"12\" align=\"left\">上傳資料與現有資料相同！</td></tr>";
          servletRequest.setAttribute("upload_message", result);
          return actionMapping.findForward("update_data"); // display page
        }
        // 顯示兩版資料比對結果
        servletRequest.setAttribute("compare_data", comp);
        return actionMapping.findForward("compare_data"); // compare result page
      }
    } else if (listControl.equals("Upload_confirm")) {
      result = YieldDefinitionService.upload_data(sid);
      if (!result.equals(""))
        servletRequest.setAttribute("upload_message", result);
      return actionMapping.findForward("update_data"); // compare result page
    } else if (listControl.equals("submit_data")) {
      YieldDefinitionService.submit(sid);
      return actionMapping.findForward("update_data");
    } else if (listControl.equals("reset_tx")) {
      boolean flag1 = YieldDefinitionService.reset_tx(sid, brand, version, pd_body);
      return actionMapping.findForward("update_data");
    } else {
      return actionMapping.findForward("fail");
    }
  }

  private void jbInit() throws Exception {
  }
}
