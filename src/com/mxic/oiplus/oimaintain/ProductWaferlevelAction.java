package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.au.*;
import com.mxic.oiplus.dao.TfChangeIpnLevelBean;
import com.mxic.oiplus.dao.TfChangeIpnLevelDao;
import com.mxic.oiplus.util.TDSLogger;

public class ProductWaferlevelAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse Response) {

    ProWaferlevelBeanAF fm = (ProWaferlevelBeanAF) actionForm;
    String k = Request.getParameter("sid");
    String flag = "";
    ProWaferlevelBean[] ptrb = null;
    TfChangeIpnLevelBean[] changeIpnLevelList = null;
    if (k == null){
      k=fm.getSid();
    } else {
      OiMaintainStep ois = OiMaintainService.SearchFunction(k);
      if(ois != null){
        fm.setSid(ois.getSid());
        fm.setBrand(ois.getBrand());
        fm.setProductbody(ois.getProduct_body());
        fm.setVersion(ois.getVersion());
      }
    }

    try{
      boolean lastVersion =
          OiMaintainService.CheckExistProductWaferlevel(fm.getProductbody(),
                                                   fm.getBrand(),
                                                   fm.getVersion());
      int lastVersion_count =
          OiMaintainService.CheckExistProductWaferlevelCount(fm.getProductbody(),
                                                   fm.getBrand(),
                                                   fm.getVersion());
      boolean ProWaferlevelExist =
          OiMaintainService.CheckExistProductWaferlevelTX(fm.getSid());
      
      int newVersion_count =
          OiMaintainService.CheckExistProductWaferlevelTXCount(fm.getProductbody(),
                                                   fm.getBrand(),
                                                   fm.getVersion());
      int Waferlevel_count =
          OiMaintainService.getWaferlevelCount();

      boolean status_apply_bo = FTService.status_apply(Integer.parseInt(fm.getSid())); //true:處理中, false:會簽中
      /*if tf_prod_waferlevel_tx has data of selected product_body,brand,version then just get the data from
           tf_prod_waferlevel_tx*/
      if (ProWaferlevelExist){//P(已有資料的處理中) / A(已有資料的會簽中)
    	  if(status_apply_bo){//找不到Status='A',true,P(處理中)
	    	  if (newVersion_count>=Waferlevel_count) { //8筆
	    		  ptrb = OiMaintainService.GetWaferlevel(k);
	       	      
	          }else{//0 or 5 筆record
	        	  OiMaintainService.ProWaferlevelToProWaferlevelTx_New(fm.getProductbody(),
	                      fm.getBrand(),
	                      fm.getSid(),
	                      fm.getVersion());
	        	  OiMaintainService.unSubmit(fm.getSid(), "TF_PROD_WAFERLEVEL");
	        	  ptrb = OiMaintainService.GetWaferlevel(k);
	          }
    	  }else{//A(會簽中),有資料
    		  ptrb = OiMaintainService.GetWaferlevel(k);
    	  }
   
      } else {/*if no data in tf_prod_waferlevel_tx check if tf_prod_waferlevel has last version's data, P(還沒有資料的處理中)/A(會簽中無資料)*/
    	if(status_apply_bo){//true:處理中  
	        if (lastVersion) {/*if tf_prod_waferlevel has data then copy it into _TX table first*/
	           if (lastVersion_count<Waferlevel_count) { //0 or 5 筆record
	        	   OiMaintainService.ProWaferlevelToProWaferlevelTx_New(fm.getProductbody(),
	                                                 fm.getBrand(),
	                                                 fm.getSid(),
	                                                 fm.getVersion());
	        	   OiMaintainService.unSubmit(fm.getSid(), "TF_PROD_WAFERLEVEL");
	           }else{//8 筆record
	        	   OiMaintainService.ProWaferlevelToProWaferlevelTx(fm.getProductbody(),
	                                                 fm.getBrand(),
	                                                 fm.getSid(),
	                                                 fm.getVersion());
	        	   OiMaintainService.unSubmit(fm.getSid(), "TF_PROD_WAFERLEVEL");
	           }
	           ptrb = OiMaintainService.GetWaferlevel(k);
	        } else {//抓tf_waferlevel資料, if tf_prod_waferlevel no data
	           OiMaintainService.TFWaferlevelToProWaferlevelTx(fm.getProductbody(),
	                    fm.getBrand(),
	                    fm.getSid(),
	                    fm.getVersion());	
	           OiMaintainService.unSubmit(fm.getSid(), "TF_PROD_WAFERLEVEL");
	           ptrb = OiMaintainService.GetWaferlevel(k);
	        }
    	}else{//false:會簽中,無資料
      	  ptrb = OiMaintainService.GetWaferlevel(k);    
    	}  
      }
      
      /*update check_flag in Downgrade IPN Project 20130924*/
      OiMaintainService.UpdateProWaferlevelTx(fm.getProductbody(),
              fm.getBrand(),
              fm.getSid(),
              fm.getVersion());
      
      OiMaintainService.submit(k, "TF_PROD_WAFERLEVEL");
      
      ptrb = OiMaintainService.GetWaferlevel(k);
      
      
    } catch (Exception e){
    	TDSLogger.println(e);
    }
    
    //get Change IPN Level list
    changeIpnLevelList = TfChangeIpnLevelDao.getList();

    /*get  creator,sponsor1,sponsor2 from the bean*/
    OiMaintainStep ois = OiMaintainService.SearchFunction(fm.getSid());
    String prod_type = OiMaintainService.getProductType(fm.getSid());

    String cre = ois.getCreator();
    String sp1 = ois.getSponsor_1();
    String sp2 = ois.getSponsor_2();
    /**********************************************/
    /*get user name from session bean*/
    User user = (User) Request.getSession().getAttribute("user");
    String un = user.getUserName();
    /*********************************/
    /*get the status of the product (processing or in approve or released)*/
    boolean status_apply_bo = FTService.status_apply(Integer.parseInt(fm.getSid()));
    /**********************************************************************/
    /*If status is processing and user is the creator or sponsor1 or sponsor2, show the
            modification buttons in jsp pages*/////////
    if (status_apply_bo && un.equals(cre) ){
      flag = "Show";
    } else if (status_apply_bo && un.equals(sp1)){
      flag = "Show";
    } else if (status_apply_bo && un.equals(sp2)){
      flag = "Show";
    } else {
      flag = "NotShow";
    }
    /************************************************************************/
    Request.setAttribute(actionMapping.getName(), fm);
    Request.setAttribute("flag", flag);
    Request.setAttribute("ProdWaferlevel", ptrb);
    Request.setAttribute("changeIpnLevel", changeIpnLevelList);    
    Request.setAttribute("prod_type", prod_type);
    Request.setAttribute("sid", fm.getSid());
    return actionMapping.findForward("ProdWaferlevel");
  }
}
