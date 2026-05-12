package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.oiplus.oimaintain.AsignActionForm;
import com.mxic.oiplus.oimaintain.ProTestRouteBeanAF;

import com.mxic.oiplus.xtrarom.pdf.*;
import com.mxic.oiplus.util.TDSLogger;

public class AsignAction extends Action {

  /*public AsignAction() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }*/

  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    AsignActionForm asignActionForm = (AsignActionForm) actionForm;
//    FTService service = new FTService();
    String listControl = asignActionForm.getListControl();
    ProTestRouteBeanAF fm = new ProTestRouteBeanAF();
    fm.setProductbody(asignActionForm.getPd_body());
    fm.setBrand(asignActionForm.getBrand());
    fm.setVersion(asignActionForm.getVersion());
    fm.setSid(Integer.toString(asignActionForm.getSid()));

    if (listControl.equals("lock")) {
      /* 文件管理 - 送會簽 */
      boolean flag1 = FTService.exit_conform(asignActionForm.getSid());

      if (flag1 == true) {
    	TDSLogger.println("PDFList");
        MakePDFTx.makePDF(asignActionForm);
//        TDSLogger.println("PDF1");
        MakeCoverPage1.makePDFCoverPage(fm, null, asignActionForm.getStatus());
/*
        MakePDFDiff.makePDF(""+asignActionForm.getSid(),
                            asignActionForm.getPd_body(),
                            asignActionForm.getBrand(),
                            asignActionForm.getVersion(),
                            asignActionForm.getStatus(),
                            "_TX");
*/
//        TDSLogger.println("PDF2");
        FTTestActionForm[] vl = FTService.getVendorList(asignActionForm.getSid());
        for (int i=0;i<vl.length;i++) {
          fm.setVendor(vl[i].getPlant_name());
          MakeVendorPDF.makePDF(fm, "A");
        }
        return actionMapping.findForward("success");
      } else {
        return actionMapping.findForward("fail");
      }
    } else if( listControl.equals("PDFList")){
      /* 文件管理 - 文件列表 PDF */
      MakePDFTx.makePDF(asignActionForm);
//      TDSLogger.println("PDF1");
      MakeCoverPage1.makePDFCoverPage(fm, null, asignActionForm.getStatus());
/*
      MakePDFDiff.makePDF(""+asignActionForm.getSid(),
                          asignActionForm.getPd_body(),
                          asignActionForm.getBrand(),
                          asignActionForm.getVersion(),
                          asignActionForm.getStatus(),
                          "_TX");
*/
//      TDSLogger.println("PDF2");
      FTTestActionForm[] vl = FTService.getVendorList(asignActionForm.getSid());
      for (int i=0;i<vl.length;i++) {
        fm.setVendor(vl[i].getPlant_name());
        MakeVendorPDF.makePDF(fm, "P");
      }
      return actionMapping.findForward("success");
    } else {
      return actionMapping.findForward("fail");
    }
  }

  private void jbInit() throws Exception {
  }
}
