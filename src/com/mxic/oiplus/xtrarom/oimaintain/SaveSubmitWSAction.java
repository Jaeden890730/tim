package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.util.TDSLogger;

public class SaveSubmitWSAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String flag = Request.getParameter("flag");
    String sid = Request.getParameter("sid");
    OiMaintainStep ois = OiMaintainService.SearchFunction(sid);
    if(ois != null){
      fm.setSid(ois.getSid());
      fm.setBrand(ois.getBrand());
      fm.setProductbody(ois.getProduct_body());
      fm.setVersion(ois.getVersion());
    }
    //String[] id = Request.getParameterValues("id");
    String[] pgm_id = Request.getParameterValues("pgm_id");
    String[] temp = Request.getParameterValues("temp");
    String[] tf_comment = Request.getParameterValues("tf_comment");
    String[] test_type = Request.getParameterValues("test_type");
    String[] site = Request.getParameterValues("site");
    String[] hw_configure = null;
	if(test_type!=null)
		hw_configure= new String[test_type.length];
    wtbean[] wtb2 = null;
    String A = null;
    String forward = null;
    try{
      if(test_type!=null){	
        for(int i=0; i<test_type.length; i++){
    		//    	HW Configure
    		String[] hw_configure_radio = Request.getParameterValues("hw_configure_radio_"+i);
    		String[] hw_configure_content = Request.getParameterValues("hw_configure_content_"+i);
    		if(hw_configure_radio[0].equals("HWConfigure")){
    			String tmp = hw_configure_content[0].replaceAll("<BR>", ";");
    			tmp = tmp.replaceAll("<br>", ";");
    			hw_configure[i] = tmp.substring(0, tmp.length()-1);
    		}else{
    			hw_configure[i]=hw_configure_radio[0];
    		}
    		//TDSLogger.println("hw_configure_radio[0]="+hw_configure_radio[0]);
    		TDSLogger.println("hw_configure_content1[0]="+hw_configure_content[0]);
    		TDSLogger.println("hw_configure[i]="+hw_configure[i]);
        }	
      }
    	
      OiMaintainService.UpdateWSParameter(sid, test_type, pgm_id, temp, tf_comment, site, hw_configure);
      //if(A.equals("")){
      forward = "SaveWSParam";
      if (flag.equals("submit")){
        OiMaintainService.submitWSTestParameter(sid);
      }
      //}else{
      //Request.setAttribute("closeWindow","false");
      //Request.setAttribute("message",A + " ¥¼©w¸q");
      //forward="undefine";
      //}
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/wsTestParameterActionX.do?sid="+sid));
    } catch (Exception e){
      e.printStackTrace();
    }
    //return actionMapping.findForward(forward);
    return null;
  }
}
