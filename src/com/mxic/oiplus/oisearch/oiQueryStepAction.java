package com.mxic.oiplus.oisearch;

//import com.mxic.oiplus.rs.*;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.oiplus.dao.TfChangeIpnLevelBean;
import com.mxic.oiplus.dao.TfChangeIpnLevelDao;
import com.mxic.oiplus.oimaintain.*;
/*
import com.mxic.oiplus.oimaintain.OiMaintainService;
import com.mxic.oiplus.oimaintain.PBCTestParameterBean;
import com.mxic.oiplus.oimaintain.FTTestActionForm;
import com.mxic.oiplus.oimaintain.WIPActionForm;
import com.mxic.oiplus.oimaintain.WIPCtrlBean;
import com.mxic.oiplus.oimaintain.YieldDefinitionActionForm;
import com.mxic.oiplus.oimaintain.ProTestRouteBean;
*/
import com.mxic.oiplus.resource.DBConnection;

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
      case 0: //Product VS Waferlevel Mapping
	  	  ProWaferlevelBean[] list0 =
	          oiQueryStepService.OIQueryStep0(productbody, brand, version);
	      servletRequest.setAttribute("list1", list0);
	      //get Change IPN Level list
	      TfChangeIpnLevelBean[] changeIpnLevelList = TfChangeIpnLevelDao.getList();
	      servletRequest.setAttribute("changeIpnLevel", changeIpnLevelList);   
	      forward = "step0";
	      break;
      case 1: //Product VS Test Route Mapping
    	  ProTestRouteBean[] list1 =
            oiQueryStepService.OIQueryStep1(productbody, brand, version);
        servletRequest.setAttribute("list1", list1);
        forward = "step1";
        break;
      case 2: //BOM VS Product Route Mapping
        TFBomRouteBean[] list2 = oiQueryStepService.OIQueryStep2(productbody, brand, version);
        servletRequest.setAttribute("list1", list2);
        servletRequest.setAttribute("brand", brand);
        forward = "step2";
        break;
      case 21: //BOM VS Product Route Mapping
          TFBomRouteBean[] list21 = oiQueryStepService.OIQueryStep2_1(productbody, brand, version);
          servletRequest.setAttribute("list1", list21);
          servletRequest.setAttribute("brand", brand);
          forward = "step2_1";
          break;
      case 3: // WS Test Parameter Information
        TFTestParameterWSBean[] list3 =
            oiQueryStepService.OIQueryStep3(productbody, brand, version);
        servletRequest.setAttribute("list1", list3);
        forward = "step3";
        break;
      case 4: // FT Test Parameter Information
        TFTestParameterFTBean[] list4 =
            oiQueryStepService.OIQueryStep4(productbody, brand, version);
        servletRequest.setAttribute("list1", list4);
        forward = "step4";
        break;
      case 5: // Programmer/Burn-in/Cycling Test Parameter Information
    	  PBCTestParameterBean[] list5 =
              oiQueryStepService.OIQueryStep5(productbody, brand, version);
          servletRequest.setAttribute("list1", list5);
          forward = "step5";
          break;
      case 6: // Basic Information
        TFBasicInformationBean[] list6 =
            oiQueryStepService.OIQueryStep6(productbody, brand, version);
        servletRequest.setAttribute("list1", list6);
		int sid = Integer.parseInt(com.mxic.oiplus.oimaintain.OiMaintainService.getSid(productbody,brand,version));
        servletRequest.setAttribute("Comments", 
        		com.mxic.oiplus.oimaintain.TFIMBasicService.getBAComment(sid, "R"));
        forward = "step6";
        break;
      case 7: // Yield Definition (Old) 20090518
        YieldDefinitionBean[] list7 =
            oiQueryStepService.OIQueryStep7(productbody, brand, version);
        servletRequest.setAttribute("list1", list7);
        YieldDefinitionActionForm[] list71 =
            oiQueryStepService.OIQueryStep71(productbody, brand, version);
        servletRequest.setAttribute("list2", list71);
        forward = "step7";
        break;
      case 12: // WS Yield Definition 20090518
          YieldDefBean[] list12 =
              oiQueryStepService.OIQueryStep12(productbody, brand, version, "WS");
          servletRequest.setAttribute("list1", list12);
          servletRequest.setAttribute("proc_type", "WS");
          forward = "step12";
          break;
      case 13: // FT Yield Definition 20090518
    	  YieldDefBean[] list13 =
              oiQueryStepService.OIQueryStep12(productbody, brand, version, "FT");
          servletRequest.setAttribute("list1", list13);
          servletRequest.setAttribute("proc_type", "FT");
          forward = "step13";
          break;
      case 8: // Test Flow Chart
        TestFlowBean[] list8 =
            oiQueryStepService.OIQueryStep8(productbody, brand, version);
        servletRequest.setAttribute("list1", list8);
        forward = "step8";
        break;
      case 9: // CP Wip Handling
        FTTestActionForm list90 = new FTTestActionForm();
        list90.setPd_body(productbody);
        list90.setBrand(brand);
        list90.setVersion(version);
        WIPActionForm[] list91 =oiQueryStepService.OIQueryStep91(productbody, brand, version);
        WIPCtrlBean[] list92 =oiQueryStepService.OIQueryStep92(productbody, brand, version);
        WIPActionForm[] list93 =oiQueryStepService.OIQueryStep93(productbody, brand, version);
        servletRequest.setAttribute("product_type", "NVM");
        servletRequest.setAttribute("list", list90);
        servletRequest.setAttribute("list1", list91);
        servletRequest.setAttribute("list2", list92);
        if (list93.length > 0)
          servletRequest.setAttribute("list3", list93);
        forward = "step9";
        break;
      case 14: //Product group key basic data
    	  TFBomRouteBean[] list14 =
            oiQueryStepService.OIQueryStep14(productbody, brand, version);
        servletRequest.setAttribute("list1", list14);
        forward = "step14";
        break;  
      default:
        forward = "failed";
    }
    servletRequest.setAttribute("productType", OiMaintainService.getProductType(productbody, brand, version));
    return actionMapping.findForward(forward);
  }
}
