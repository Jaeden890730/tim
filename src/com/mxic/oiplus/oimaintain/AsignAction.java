package com.mxic.oiplus.oimaintain;

import com.mxic.oiplus.au.User;

import com.mxic.oiplus.common.TF_OI_GENLIST;
import com.mxic.oiplus.oimaintain.*;

import javax.servlet.http.*;

import java.util.ArrayList;

import org.apache.struts.action.*;
import com.mxic.oiplus.pdf.*;
import com.mxic.oiplus.util.*;

/* PDF 製作 sequence : 
 * 	處理及會簽中 : MakePDFx.makePDF()
 *    已生效 : MakePDF1.makePDF()
 *    
 *    廠內本文
 *    MakePDFx.makePDF() 
 *    廠內封面及差異表
 *    MakeCoverPage1.makePDFCoverPage()
 *    	MakePDFVendorDiff.makePDF() 廠商變動表
 *    	MakePDFDiff.makePDF() 差異表
 *    廠外本文及差異表
 *    MakeVendorPDF.makePDF()
 *    	廠外本文
 *    	MakeVendorCoverPage.makePDF() 差異表
 */

public class AsignAction extends Action {

  public AsignAction() {
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

	System.out.println("開始："+DateUtil.getNow());
	java.util.Date d1 = new java.util.Date();
	User user = (User) servletRequest.getSession().getAttribute("user");

    AsignActionForm asignActionForm = (AsignActionForm) actionForm;

    String listControl = asignActionForm.getListControl();
    String message1 = asignActionForm.getMessage1();
    ProTestRouteBeanAF fm = new ProTestRouteBeanAF();
    fm.setProductbody(asignActionForm.getPd_body());
    fm.setBrand(asignActionForm.getBrand());
    fm.setVersion(asignActionForm.getVersion());
    fm.setSid(Integer.toString(asignActionForm.getSid()));
    fm.setPackage_component(asignActionForm.getPackage_component());
    
    if (listControl.equals("lock")) {
        /*JB201300115 20131120 BY KEN
         * 1. 維護 NVM MX 版 e8049 時，點選 Step11. 文件管理裡的 " 產生PBR/ECR申請文件 " 按鈕時自動檢核 Step3 & Step4 此次變動的 PGM ID 是否存在相同 Product 的 KH 生效版本內，若條件成立則 show message " 此次程式變動與 KH O.I 有關，請確認是否需修改!! "
         * 2. 反之維護 NVM KH 版 e8049 時，點選 Step11. 文件管理裡的 " 產生PBR/ECR申請文件 " 按鈕時自動檢核 Step3 & Step4 此次變動的 PGM ID 是否存在相同 Product 的 MX 生效版本內，若條件成立則 show message " 此次程式變動與 MX O.I 有關，請確認是否需修改!! "
         */
        
        if(message1 != null && message1.equals("")){
            String s = EditiionCompareService.ComparePDR2BRAND(fm.getSid(),fm.getProductbody(),fm.getBrand(),fm.getVersion());
            if(s != null && s.length() > 0) {
            servletRequest.setAttribute("message1", "此次程式變動與 "+(fm.getBrand().equals("MX")?"KH":"MX")+" O.I 有關，請確認是否需修改!!(Facility:PGM_ID):"+s+" (對應版本):"+(fm.getBrand().equals("MX")?"KH":"MX")+"，確定鍵送會簽，取消鍵停在原頁"); 
                servletRequest.setAttribute("sid",fm.getSid());
                return actionMapping.findForward("failure");
            }
        } 
        
      /* 文件管理 - 送會簽 */
      boolean flag1 = FTService.exit_conform(asignActionForm.getSid(), asignActionForm.getPackage_component());

      if (flag1 == true) {
    	TF_OI_GENLIST tfoigen = new TF_OI_GENLIST();
    	TDSLogger.println("PDFList");
        MakePDFTx.makePDF(asignActionForm);
        MakeCoverPage1.makePDFCoverPage(fm, null, asignActionForm.getStatus());
        FTTestActionForm[] vl = FTService.getVendorList(asignActionForm.getSid());
        for (int i=0;i<vl.length;i++) {
          fm.setVendor(vl[i].getPlant_name());
          MakeVendorPDF.makePDF(fm, "A");
        }
        tfoigen.insertData(user.getEmpNo(), fm.getSid(), fm.getProductbody(), fm.getVersion());
        return actionMapping.findForward("success");
      } else {
    	servletRequest.setAttribute("message", "Step0 需勾選設定"); 
        return actionMapping.findForward("failure");
        //return actionMapping.findForward("fail");
      }
    } else if( listControl.equals("PDFList")){
      /* 文件管理 - 文件列表 PDF */
    	TF_OI_GENLIST tfoigen = new TF_OI_GENLIST();
    	
      TDSLogger.println("PDFList_start");
      MakePDFTx.makePDF(asignActionForm);
      MakeCoverPage1.makePDFCoverPage(fm, null, asignActionForm.getStatus());
      FTTestActionForm[] vl = FTService.getVendorList(asignActionForm.getSid());
      for (int i=0;i<vl.length;i++) {
        fm.setVendor(vl[i].getPlant_name());
        MakeVendorPDF.makePDF(fm, "P");
      }
        System.out.println("結束："+DateUtil.getNow());
        java.util.Date d2 = new java.util.Date();
        System.out.println("花費："+DateUtil.getDiffSecond(d2,d1));
      TDSLogger.println("PDFList_success");
        tfoigen.insertData(user.getEmpNo(), fm.getSid(), fm.getProductbody(), fm.getVersion());
      return actionMapping.findForward("success");
    }
    else {
    	TDSLogger.println("PDFList_fail");
      return actionMapping.findForward("fail");
    }
  }

  private void jbInit() throws Exception {
  }
}
