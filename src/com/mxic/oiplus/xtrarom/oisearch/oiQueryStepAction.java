package com.mxic.oiplus.xtrarom.oisearch;

//import com.mxic.oiplus.rs.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.oiplus.xtrarom.oimaintain.OiMaintainService;
import com.mxic.oiplus.xtrarom.oimaintain.PBCTestParameterBean;
import com.mxic.oiplus.oimaintain.FTTestActionForm;
import com.mxic.oiplus.oimaintain.WIPActionForm;
import com.mxic.oiplus.oimaintain.WIPCtrlBean;
import com.mxic.oiplus.oimaintain.YieldDefBean;
import com.mxic.oiplus.oimaintain.YieldDefinitionActionForm;
import com.mxic.oiplus.oimaintain.ProTestRouteBean;
import com.mxic.oiplus.oisearch.oiQueryStepAForm;

public class oiQueryStepAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    oiQueryStepAForm oiQueryStepAForm = (oiQueryStepAForm) actionForm;
    int btControl = Integer.parseInt(oiQueryStepAForm.getBtControl());
    String productbody = oiQueryStepAForm.getProduct_body();
    String brand = oiQueryStepAForm.getBrand();
    String version = oiQueryStepAForm.getVersion();
    String forward = "";

    switch (btControl) {
      case 1: //Product VS Test Route Mapping
    	  ProTestRouteBean[] list1 =
            oiQueryStepService.OIQueryStep1(productbody, brand, version);
        servletRequest.setAttribute("list1", list1);
        forward = "step1";
        break;
      case 2: //BOM VS Product Route Mapping
        TFBomRouteBean[] list2 =
            oiQueryStepService.OIQueryStep2(productbody, brand, version);
        servletRequest.setAttribute("list1", list2);
        forward = "step2";
        break;
      case 3: //BOM VS Product Recycle Route Mapping
       TFBomReRouteBean[] list3 =
           oiQueryStepService.OIQueryStep3(productbody, brand, version);
       servletRequest.setAttribute("list1", list3);
       forward = "step3";
        break;
      case 4: // WS Test Parameter Information
        TFTestParameterWSBean[] list4 =
            oiQueryStepService.OIQueryStep4(productbody, brand, version);
        servletRequest.setAttribute("list1", list4);
        forward = "step4";
        break;
      case 5: // FT Test Parameter Information
        TFTestParameterFTBean[] list5 =
            oiQueryStepService.OIQueryStep5(productbody, brand, version);
        servletRequest.setAttribute("list1", list5);
        forward = "step5";
        break;
      case 6: // Programmer/Burn-in/Cycling Test Parameter Information
    	  PBCTestParameterBean[] list6 =
              oiQueryStepService.OIQueryStep6(productbody, brand, version);
          servletRequest.setAttribute("list1", list6);
          forward = "step6";
          break;
      case 7: // Yield Definition
        //step7 導到 nvm jsp，因此重設相關資料
    	com.mxic.oiplus.oisearch.oiQueryStepAForm list70 = 
    		new com.mxic.oiplus.oisearch.oiQueryStepAForm();
    	list70.setProduct_body(productbody);
    	list70.setBrand(brand);
    	list70.setVersion(version);
    	servletRequest.setAttribute("oiQueryStepAForm", list70);
        servletRequest.setAttribute("product_type", "XROM");
    	YieldDefinitionBean[] list7 =
            oiQueryStepService.OIQueryStep7(productbody, brand, version);
       	servletRequest.setAttribute("list1", list7);
        YieldDefinitionActionForm[] list71 =
            com.mxic.oiplus.oisearch.oiQueryStepService.OIQueryStep71(productbody, brand, version);
      	servletRequest.setAttribute("list2", list71);
        forward = "step7";
        break;
      case 8: // Main Route vs Substitution Route Mapping
        TFMainRouteSubBean[] list8 =
            oiQueryStepService.OIQueryStep8(productbody, brand, version);
        servletRequest.setAttribute("list1", list8);
        forward = "step8";
        break;
      case 9: // Main Route vs Rework Route Mapping
        TFMainRouteReBean[] list9 =
            oiQueryStepService.OIQueryStep9(productbody, brand, version);
        servletRequest.setAttribute("list1", list9);
        forward = "step9";
        break;
      case 10: // WIP Control
        //step10 導到 nvm jsp，因此重設相關資料
        FTTestActionForm list90 = new FTTestActionForm();
        list90.setPd_body(productbody);
        list90.setBrand(brand);
        list90.setVersion(version);
        WIPActionForm[] list91 =
            com.mxic.oiplus.oisearch.oiQueryStepService.OIQueryStep91(productbody, brand, version);
        WIPCtrlBean[] list92 =
            com.mxic.oiplus.oisearch.oiQueryStepService.OIQueryStep92(productbody, brand, version);
        WIPActionForm[] list93 =
            com.mxic.oiplus.oisearch.oiQueryStepService.OIQueryStep93(productbody, brand, version);
        servletRequest.setAttribute("product_type", "XROM");
        servletRequest.setAttribute("list", list90);
        servletRequest.setAttribute("list1", list91);
        servletRequest.setAttribute("list2", list92);
        if (list93.length > 0)
          servletRequest.setAttribute("list3", list93);
        forward = "step10";
        break;
      case 11: // Basic Information
        //step11 導到 nvm jsp，因此重設相關資料
        com.mxic.oiplus.oisearch.oiQueryStepAForm list60 = new com.mxic.oiplus.oisearch.oiQueryStepAForm();
        list60.setProduct_body(productbody);
        list60.setBrand(brand);
        list60.setVersion(version);
        com.mxic.oiplus.oisearch.TFBasicInformationBean[] list11 =
            com.mxic.oiplus.oisearch.oiQueryStepService.OIQueryStep6(productbody, brand, version);
        servletRequest.setAttribute("oiQueryStepAForm", list60);
        servletRequest.setAttribute("list1", list11);
		int sid = Integer.parseInt(com.mxic.oiplus.oimaintain.OiMaintainService.getSid(productbody,brand,version));
        servletRequest.setAttribute("Comments", 
        		com.mxic.oiplus.oimaintain.TFIMBasicService.getBAComment(sid, "R"));
        forward = "step11";
        break;
      case 12: // WS Yield Definition 20090518
          YieldDefBean[] list12 =
              com.mxic.oiplus.oisearch.oiQueryStepService.OIQueryStep12(productbody, brand, version, "WS");
          servletRequest.setAttribute("list1", list12);
          servletRequest.setAttribute("proc_type", "WS");
          forward = "step12";
          break;
      case 13: // FT Yield Definition 20090518
    	  YieldDefBean[] list13 =
    		  com.mxic.oiplus.oisearch.oiQueryStepService.OIQueryStep12(productbody, brand, version, "FT");
          servletRequest.setAttribute("list1", list13);
          servletRequest.setAttribute("proc_type", "FT");
          forward = "step13";
          break;
      case 14: //BOM VS Product Route Mapping
          TFBomRouteBean[] list14 =
              oiQueryStepService.OIQueryStep14(productbody, brand, version);
          servletRequest.setAttribute("list1", list14);
          forward = "step14";
          break;    
      default:
        forward = "faild";
    }
    servletRequest.setAttribute("productType", OiMaintainService.getProductType(productbody, brand, version));
    return actionMapping.findForward(forward);
  }
}
