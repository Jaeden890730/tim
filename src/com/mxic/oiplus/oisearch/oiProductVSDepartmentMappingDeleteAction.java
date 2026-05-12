package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiProductVSDepartmentMappingDeleteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    oiProductVSDepartmentMappingDeleteAForm
        MappingDeleteAForm = (oiProductVSDepartmentMappingDeleteAForm) actionForm;

    String product_body = MappingDeleteAForm.getProduct();
    String dept = MappingDeleteAForm.getDepartment();
    String message = "";
    String forward = "";

    if (MappingDeleteAForm.getBtControl().equals("bt_delete")){
      boolean flag = oiSearchService.DeleteProductVsDept(product_body,dept);
      if (flag){
        message="您成功刪除一個 product body";
        forward = "success";
      }
    }
    return actionMapping.findForward(forward);
  }
}
