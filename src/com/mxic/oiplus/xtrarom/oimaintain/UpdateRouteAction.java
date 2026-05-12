package com.mxic.oiplus.xtrarom.oimaintain;

import java.util.HashMap;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class UpdateRouteAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest request,
                               HttpServletResponse servletResponse) {
    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String sid= request.getParameter("sid");
    String routename=request.getParameter("routename");
    String seq[]=request.getParameterValues("seq");
    String txtTesttime[]=request.getParameterValues("txtTesttime");
    String txtTimeunit[]=request.getParameterValues("txtTimeunit");
    String txtTemp[]=request.getParameterValues("txtTemp");
    String[] txtSamplingtest=request.getParameterValues("txtSamplingtest");
    String txtRemark[]=request.getParameterValues("txtRemark");
    String[] txtTimeSeq = request.getParameterValues("timeSeq");
    String[] txtTempSeq = request.getParameterValues("tempSeq");
    String[] txtSamplingtestSeq = request.getParameterValues("samplingtestSeq");
    
    HashMap timeHash = new HashMap();
    HashMap unitHash = new HashMap();
    if (txtTimeSeq != null) {
      for (int i=0; i<txtTimeSeq.length; i++) {
    	timeHash.put(txtTimeSeq[i], txtTesttime[i]);
        unitHash.put(txtTimeSeq[i], txtTimeunit[i]);
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

    try {
      OiMaintainService.UpdateRouteStep(sid, routename,
                                        seq, timeHash, unitHash,
                                        tempHash, txtRemark, samplingtestHash);

    } catch(Exception e){
      e.printStackTrace();
    }
    request.setAttribute(actionMapping.getName(),fm);
    return actionMapping.findForward("UPTRoute");
  }
}
