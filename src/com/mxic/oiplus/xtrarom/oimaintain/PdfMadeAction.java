package com.mxic.oiplus.xtrarom.oimaintain;
import java.io.IOException;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import com.mxic.oiplus.xtrarom.pdf.*;
import com.mxic.oiplus.oimaintain.ProTestRouteBeanAF;


public class PdfMadeAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse httpServletResponse) {

    //**包含全部該產品資訊的ActionForm**************************
     ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
     //*******************************************************

//    String pro_b=fm.getProductbody();
//    String version=fm.getVersion();
//    String br=fm.getBrand();
    String flag=Request.getParameter("flag");//判別這PDF這給廠內還是VENDOR的
    String vendorname=Request.getParameter("vendorname");//VENDOR名稱

    if(flag.equals("vendor")){
      fm.setVendor(vendorname);//如果是給VENDOR的，將ActionForm中Vendor的property設為vendor名稱
      //*******將整個ActionForm帶入下列Function,下列Function所需參數皆為(ActionForm,檔案名稱)
//      MakeCoverPage1.makePDFCoverPage(fm, vendorname, "R");
//      MakeVendorCoverPage.makePDF(fm);
      MakeVendorPDF.makePDF(fm,"R");
    } else if(flag.equals("mxic")){
      fm.setVendor("");
      MakeCoverPage1.makePDFCoverPage(fm, null, "R");
      MakePDF1.makePDF(fm);
/*
      MakePDFDiff.makePDF(fm.getSid(),
                          fm.getProductbody(),
                          fm.getBrand(),
                          fm.getVersion(),
                          "R",
                          "");
*/
    }
/*
    else if(flag.equals("vendorDiff")){
      MakePDFVendorDiff.makePDF(fm);
    }
*/
    try {
		httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/goPDFPageActionX.do?sid="+fm.getSid()+"&productbody="+fm.getProductbody()+"&brand="+fm.getBrand()+"&version="+fm.getVersion()+"&package_component="+fm.getPackage_component()));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    //return actionMapping.findForward("MakePDF");
    return null;
  }
}