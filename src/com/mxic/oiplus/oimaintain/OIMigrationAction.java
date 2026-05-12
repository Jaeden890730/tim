package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import com.mxic.oiplus.au.*;

public class OIMigrationAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    OIMigrationActionForm oIMigrationActionForm = (OIMigrationActionForm) actionForm;
//    OIMigrationService service = new OIMigrationService();

    String brand = oIMigrationActionForm.getRecord_id();
    String pd_body = oIMigrationActionForm.getPd_body();
    String version = oIMigrationActionForm.getVersion();

    boolean exit_bo = OIMigrationService.exit_version(brand, version, pd_body);

    //boolean exit_bo = true;
    if (exit_bo == true) {
      HttpSession session = servletRequest.getSession();
      User Auth = (User) session.getAttribute("user");
      String productType = (String)session.getAttribute("productType");
      String user = Auth.getUserName();
      OIMigrationActionForm[] rs =
          OIMigrationService.getProInfo(brand, pd_body, version, user, productType);
      servletRequest.setAttribute("list2", rs);
      return actionMapping.findForward("upload_migr");
    } else {
      servletRequest.setAttribute("information","前一版本已存在,無法使用此種方式新增!!");
      return actionMapping.findForward("migr_fail");
    }
  }
}
