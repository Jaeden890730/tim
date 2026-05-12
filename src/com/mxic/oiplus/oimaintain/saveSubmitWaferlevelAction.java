package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.util.TDSLogger;

public class saveSubmitWaferlevelAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

	ProWaferlevelBeanAF fm = (ProWaferlevelBeanAF) actionForm;
    String flag = Request.getParameter("flag");
    String sid = fm.getSid();
    String[] wafer_level = Request.getParameterValues("wafer_level");
    String[] revise_priority = Request.getParameterValues("revise_priority");
    String[] checked_flag = Request.getParameterValues("checked_flag");
    String[] tmpchecked_flag = null;
    
    String forward = null;
    try{
      if(wafer_level!=null){	
	      tmpchecked_flag = new String[wafer_level.length];
	      if(checked_flag!=null){
		      for(int i=0;i<wafer_level.length;i++){
		    	  for(int j=0;j<checked_flag.length;j++){
		    	    if(wafer_level[i].endsWith(checked_flag[j])){
		    	    	tmpchecked_flag[i]="Y";
		    	    }
		    	  }  
		      }
	      }
	      OiMaintainService.UpdateProdWaferlevel(sid, wafer_level, revise_priority, tmpchecked_flag);
	      //if(A.equals("")){
	      forward = "SaveProdWaferlevelParam";
	      if (flag.equals("submit")){
	        OiMaintainService.submitProdWaferlevel(sid);
	      }
      //}else{
      //Request.setAttribute("closeWindow","false");
      //Request.setAttribute("message",A + " ¥¼©w¸q");
      //forward="undefine";
      //}
      } 
    } catch (Exception e){
      e.printStackTrace();
    }
    return actionMapping.findForward(forward);
  }
}
