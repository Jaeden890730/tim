package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class DocLinkageUploadAction extends Action {
  public DocLinkageUploadAction() {
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
      DocLinkageActionForm docLinkageActionForm = (DocLinkageActionForm) actionForm;
      DocLinkageService service = new DocLinkageService();

      String upload = docLinkageActionForm.getUpload();
      int sid = Integer.parseInt(docLinkageActionForm.getSid());
      String pd_body = docLinkageActionForm.getPd_body();
      String brand = docLinkageActionForm.getBrand();
      String version = docLinkageActionForm.getVersion();

      servletRequest.setAttribute("list1", docLinkageActionForm);

      return actionMapping.findForward("upload");
  }

  private void jbInit() throws Exception {
  }
}
