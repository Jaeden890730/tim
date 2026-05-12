package com.mxic.oiplus.oimaintain;

import java.io.IOException;
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
    OiMaintainStep ois = OiMaintainService.SearchFunction(sid);
    if(ois != null){
      fm.setSid(ois.getSid());
      fm.setBrand(ois.getBrand());
      fm.setProductbody(ois.getProduct_body());
      fm.setVersion(ois.getVersion());
    }
    String routename=request.getParameter("routename");
    String seq[]=request.getParameterValues("seq");
    String stepname[]=request.getParameterValues("stepname");
    String txtRemark[]=request.getParameterValues("txtRemark");

    String[] txtTesttime=request.getParameterValues("txtTesttime");
    String[] txtTimeunit=request.getParameterValues("txtTimeunit");
    String[] txtTemp=request.getParameterValues("txtTemp");
    String[] txtSamplingtest=request.getParameterValues("txtSamplingtest");
    String[] txtSamplingcond=request.getParameterValues("txtSamplingcond");
    String[] txtTimeSeq = request.getParameterValues("timeSeq");
    String[] txtTempSeq = request.getParameterValues("tempSeq");
    String[] txtSamplingtestSeq = request.getParameterValues("samplingtestSeq");
    String[] txtSamplingcondSeq = request.getParameterValues("samplingcondSeq");
        

    String[] txtRwkSeq2=request.getParameterValues("RwkSeq");
    String[] txtReworkstep=request.getParameterValues("txtReworkstep");
    String[] txtQCActualMode=request.getParameterValues("txtQCActualMode");
    String[] txtQCActualModeSeq=request.getParameterValues("qcActualModeSeq");

    String[] txtTimeSeq2 = request.getParameterValues("timeSeq2");
    String[] txtTesttime2=request.getParameterValues("txtTesttime2");
    String[] txtTimeunit2=request.getParameterValues("txtTimeunit2");
    
    // for BAKE/UV Steps
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
    HashMap samplingcondHash = new HashMap();
    if (txtSamplingcondSeq != null) {
      for (int i=0; i<txtSamplingcondSeq.length; i++)
    	  samplingcondHash.put(txtSamplingcondSeq[i], txtSamplingcond[i]);
    }
    
    // for Rework Steps (step)
    HashMap reworkHash = new HashMap();
    if (txtRwkSeq2 != null) {
      for (int i=0; i<txtRwkSeq2.length; i++) {
    	reworkHash.put(txtRwkSeq2[i], txtReworkstep[i]);
      }
    }

    // for Rework UV Steps (test time & unit)
    HashMap timeHash2 = new HashMap();
    HashMap unitHash2 = new HashMap();
    if (txtTimeSeq2 != null) {
      for (int i=0; i<txtTimeSeq2.length; i++) {
    	timeHash2.put(txtTimeSeq2[i], txtTesttime2[i]);
        unitHash2.put(txtTimeSeq2[i], txtTimeunit2[i]);
      }
    }

    // for QC Actual Test Mode
    HashMap qcHash = new HashMap();
    if (txtQCActualModeSeq != null) {
      for (int i=0; i<txtQCActualModeSeq.length; i++) {
          qcHash.put(txtQCActualModeSeq[i], txtQCActualMode[i]);
      }
    }

    try {
      countstepbean[] cts = OiMaintainService.CheckRouteExist(sid,routename);
      
      if (cts.length == 0)
    	  OiMaintainService.InsertRouteNameStep(sid, routename, 
    			  fm.getProductbody(), fm.getBrand(), fm.getVersion(),
                  seq, timeHash, unitHash,
                  stepname, tempHash, reworkHash, timeHash2, unitHash2, qcHash,txtRemark, samplingtestHash, samplingcondHash);
      else
    	  OiMaintainService.UpdateRouteStep(sid, routename,
    			  seq, timeHash, unitHash,
    			  tempHash, reworkHash, timeHash2, unitHash2, qcHash,txtRemark, samplingtestHash, samplingcondHash );

    } catch(Exception e){
      e.printStackTrace();
    }
    String productType = OiMaintainService.getProductType(sid);
    request.setAttribute(actionMapping.getName(),fm);
    
    //HttpSession session = request.getSession();
	//session.setAttribute("sid", sid);
    // NVM/XROM/MROM 共用，需回對應頁面
    try {
   		servletResponse.sendRedirect(servletResponse.encodeRedirectURL(request.getContextPath() + "/OImaintain/productRouteAction.do?sid="+sid));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    //return actionMapping.findForward("UPTRoute"+productType);
    return null;
  }
}
