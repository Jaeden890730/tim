package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class AddVendorAction extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest Request,
                               HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm=(ProTestRouteBeanAF)actionForm;
    /*ID column of tf_test_parameter_ws_tx*/
    //String id=Request.getParameter("id");
    String k=Request.getParameter("sid");
    OiMaintainStep ois = OiMaintainService.SearchFunction(k);
    if(ois != null){
      fm.setSid(ois.getSid());
      fm.setBrand(ois.getBrand());
      fm.setProductbody(ois.getProduct_body());
      fm.setVersion(ois.getVersion());
    }
    String pgm_id=Request.getParameter("pgm_id");
    /**************************************/
    /*put the id into the function to get the row data that the user selected*/
    WsTestBean[] wtb2=OiMaintainService.RowDataSelected(pgm_id);
    /*************************************************************************/
    /*get all the available vendor names except the ones that already exist in tf_test_parameter_ws_tx*/
    wtbean[] wtb=OiMaintainService.VendorList(wtb2[0].getMask_option(),wtb2[0].getTest_type(),wtb2[0].getTester(),wtb2[0].getProgram_name(),wtb2[0].getPgm_id(),wtb2[0].getSid());
    /**************************************************************************************************/
    /*fm.Message will be shown on Add_Vendor.jsp*/
    if (wtb != null && wtb.length > 0){
      fm.setMessage(" ");/*if it found some vendors, the message will be blank*/
    } else {
      fm.setMessage("查無合乎條件的VENDOR"); /* if nothing found, this message will be displayed*/
    }

    Request.setAttribute(actionMapping.getName(),fm);
    Request.setAttribute("list",wtb2);
    Request.setAttribute("AddVendor",wtb);
    return actionMapping.findForward("AddVendor");
  }
}
