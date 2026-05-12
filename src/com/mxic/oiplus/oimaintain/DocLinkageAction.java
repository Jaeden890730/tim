package com.mxic.oiplus.oimaintain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.oiplus.au.User;
import com.mxic.oiplus.util.TDSLogger;

public class DocLinkageAction extends Action {
  public DocLinkageAction() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {

    DocLinkageActionForm docLinkageActionForm = (DocLinkageActionForm) actionForm;
    //DocLinkageService service = new DocLinkageService();
    String listControl = docLinkageActionForm.getListControl();
    TDSLogger.println(listControl);
    TDSLogger.println("listControl");

    //HttpSession session = servletRequest.getSession();
    //User Auth = (User) session.getAttribute("user");
    //String user = Auth.getUserName();
    
    if (listControl.equals("delete_row")) {
      String record_id = docLinkageActionForm.getRecord_id();
      String record_id_str[] = record_id.split(",");
      //String doc_type = "";
      String category = record_id_str[1];

      /*
      if (category.equals("Test Flow")) {
        doc_type = "T";
      } else if (category.equals("Yield Definition")) {
        doc_type = "Y";
      }
      */

      int sid = Integer.parseInt(docLinkageActionForm.getSid());
      String pd_body = docLinkageActionForm.getPd_body();
      String brand = docLinkageActionForm.getBrand();
      String version = docLinkageActionForm.getVersion();
      TDSLogger.println(record_id);
      String seq_str = DocLinkageService.delete_row(record_id, sid, brand, version, pd_body);
      return actionMapping.findForward("update_data");
    
    }else if (listControl.equals("delete_all_row_submit")) {
	    int sid = Integer.parseInt(docLinkageActionForm.getSid());
	    String pd_body = docLinkageActionForm.getPd_body();
	    String brand = docLinkageActionForm.getBrand();
	    String version = docLinkageActionForm.getVersion();
	    String seq_str = DocLinkageService.delete_all_row_submit(sid, brand, version, pd_body);
	    return actionMapping.findForward("update_data");

  }else if (listControl.equals("update_data") ||
               listControl.equals("submit_data")) {
      String[] sid = servletRequest.getParameterValues("sid");
      String[] pd_body = servletRequest.getParameterValues("pd_body");
      String[] brand = servletRequest.getParameterValues("brand");
      String[] version = servletRequest.getParameterValues("version");
      String[] doc_name = servletRequest.getParameterValues("doc_name");
      String[] doc_name_be = servletRequest.getParameterValues("doc_name_be");
      String[] comment = servletRequest.getParameterValues("comment");
      String[] comment_be = servletRequest.getParameterValues("comment_be");
      String[] seq = servletRequest.getParameterValues("seq");

/*
      for (int i = 0; i < seq.length; i++) {
        if (String.valueOf(doc_name_be[i]) == null ||
            String.valueOf(doc_name_be[i]).equals("")) {
          TDSLogger.println(doc_name[i]);
          TDSLogger.println("doc_name");
        }
      }
*/
      String[] doc_type = servletRequest.getParameterValues("doc_type");
      if (listControl.equals("update_data")) {
        boolean flag1 = DocLinkageService.update_data(sid, pd_body, brand,
                                            version, doc_name, doc_name_be,
                                            comment, comment_be, seq, doc_type,
                                            "update_cmd");
      } else if (listControl.equals("submit_data")) {
        boolean flag1 = DocLinkageService.update_data(sid, pd_body, brand,
                                            version, doc_name, doc_name_be,
                                            comment, comment_be, seq, doc_type,
                                            "submit_cmd");
      }
      return actionMapping.findForward("update_data");
    } else if (listControl.equals("reset_tx")) {
      int sid = Integer.parseInt(docLinkageActionForm.getSid());
      String pd_body = docLinkageActionForm.getPd_body();
      String brand = docLinkageActionForm.getBrand();
      String version = docLinkageActionForm.getVersion();

      boolean flag1 = DocLinkageService.reset_tx(sid, brand, version, pd_body);
      String message = "kk";
      servletRequest.setAttribute("message", message);
      return actionMapping.findForward("update_data");
    } else if (listControl.equals("reload")){
      return actionMapping.findForward("reload");
    } else {
      return actionMapping.findForward("fail");
    }
  }

  private void jbInit() throws Exception {
  }
}
