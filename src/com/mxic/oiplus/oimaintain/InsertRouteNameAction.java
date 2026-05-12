package com.mxic.oiplus.oimaintain;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class InsertRouteNameAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm  =  (ProTestRouteBeanAF) actionForm;

    String sid = Request.getParameter("sid");
    String routename = Request.getParameter("routename");
    String pro_b = Request.getParameter("pro_b");
    String brand = Request.getParameter("brand");
    String version = Request.getParameter("version");
    String[] seq = Request.getParameterValues("seq");
    String[] stepname = Request.getParameterValues("stepname");
    String[] txtTime = Request.getParameterValues("txtTime");
    String[] txtUnit = Request.getParameterValues("txtUnit");
    String[] txtTemp = Request.getParameterValues("txtTemp");
    String[] txtSamplingtest = Request.getParameterValues("txtSamplingtest");
    String[] txtRemark = Request.getParameterValues("txtRemark");
    String[] txtTimeSeq = Request.getParameterValues("timeSeq");
    String[] txtTempSeq = Request.getParameterValues("tempSeq");
    String[] txtSamplingtestSeq = Request.getParameterValues("samplingtestSeq");
    
    HashMap timeHash = new HashMap();
    HashMap unitHash = new HashMap();
    if (txtTimeSeq != null) {
      for (int i=0; i<txtTimeSeq.length; i++) {
    	timeHash.put(txtTimeSeq[i], txtTime[i]);
        unitHash.put(txtTimeSeq[i], txtUnit[i]);
      }
    }
    HashMap tempHash = new HashMap();
    if (txtTempSeq != null) {
      for (int i=0; i<txtTempSeq.length; i++)
      	tempHash.put(txtTempSeq[i], txtTemp[i]);
    }
    HashMap samplingtestHash = new HashMap();
    if (txtSamplingtestSeq != null) {
      for (int i=0; i<txtSamplingtestSeq.length; i++)
    	samplingtestHash.put(txtSamplingtestSeq[i], txtSamplingtest[i]);
    }
    try{
/*
    	OiMaintainService.InsertRouteNameStep(sid, routename, pro_b, brand,
                                            version, seq, timeHash,unitHash,
                                            stepname, tempHash, txtRemark);
*/                                            
      fm.setRoutename("    ");
      fm.setReadonly(" ");
      servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/addNewRouteAction.do?sid="+sid));
    }catch(Exception e){
      e.printStackTrace();
    }
    //return actionMapping.findForward("InsertRouteName");
    return null;
  }
}
