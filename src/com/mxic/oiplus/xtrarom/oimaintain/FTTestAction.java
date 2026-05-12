package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.util.TDSLogger;

public class FTTestAction extends Action {
  public FTTestAction() {
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

    FTTestActionForm fTTestActionForm = (FTTestActionForm) actionForm;
    FTService service = new FTService();
    String listControl = fTTestActionForm.getListControl();

    if (listControl.equals("add_vendor")) {
      String record_id = fTTestActionForm.getRecord_id();
      FTTestActionForm[] rs = FTService.getvendor(fTTestActionForm.getPd_body(), record_id);
      servletRequest.setAttribute("list1", rs);
      int length = rs != null?rs.length:0;
      servletRequest.setAttribute("recno", ""+length);
      return actionMapping.findForward("add_vendor");

    } else if (listControl.equals("update_data") ||
               listControl.equals("submit_data")) {
      String[] sid = servletRequest.getParameterValues("sid");
      String[] pd_body = servletRequest.getParameterValues("pd_body");
      String[] brand = servletRequest.getParameterValues("brand");
      String[] version = servletRequest.getParameterValues("version");
      String[] test_type = servletRequest.getParameterValues("test_type");
      String[] be_opt = servletRequest.getParameterValues("be_opt");
      String[] pin_count = servletRequest.getParameterValues("pin_count");
      String[] pg_type = servletRequest.getParameterValues("pg_type");
      String[] body_size = servletRequest.getParameterValues("body_size");
      String[] tester = servletRequest.getParameterValues("tester");
      String[] site = servletRequest.getParameterValues("site");
      String[] pg_name = servletRequest.getParameterValues("pg_name");
      String[] actual_file = servletRequest.getParameterValues("actual_file");
      String[] pgm_special_control = servletRequest.getParameterValues("pgm_special_control");
      String[] one_main_pgm_group_version = servletRequest.getParameterValues("one_main_pgm_group_version");
      String[] pg_id = servletRequest.getParameterValues("pg_id");
      String[] i_grade = servletRequest.getParameterValues("i_grade");
      String[] c_grade = servletRequest.getParameterValues("c_grade");
      String[] comment = servletRequest.getParameterValues("comment");
      String[] hw_configure = null;
	  if(test_type!=null){
		hw_configure= new String[test_type.length];
        for(int i=0; i<c_grade.length; i++){
    	  //HW Configure
    	  String[] hw_configure_radio = servletRequest.getParameterValues("hw_configure_radio_"+i);
    	  String[] hw_configure_content = servletRequest.getParameterValues("hw_configure_content_"+i);
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
      if (listControl.equals("update_data")) {
        boolean flag1 = FTService.update_data(sid, pd_body, brand,
                                              version, test_type, be_opt, pin_count, pg_type,
                                              body_size, tester, site, pg_name, actual_file, pgm_special_control, one_main_pgm_group_version, pg_id, i_grade,
                                              c_grade, comment, hw_configure, "update_cmd");

      } else if (listControl.equals("submit_data")) {
        boolean flag1 = FTService.update_data(sid, pd_body, brand,
                                              version, test_type, be_opt, pin_count, pg_type,
                                              body_size, tester, site, pg_name, actual_file, pgm_special_control, one_main_pgm_group_version, pg_id, i_grade,
                                              c_grade, comment, hw_configure, "submit_cmd");

      }
      return actionMapping.findForward("update_data");
    } else if (listControl.equals("delete_row")) {
      String record_list = fTTestActionForm.getRecord_list();
      int sid = fTTestActionForm.getSid();
      String pd_body = fTTestActionForm.getPd_body();
      String brand = fTTestActionForm.getBrand();
      String version = fTTestActionForm.getVersion();
      boolean flag1 = FTService.delete_row(record_list, sid, brand, version, pd_body);
      return actionMapping.findForward("update_data");
    } else if (listControl.equals("reset_tx")) {
      int sid = fTTestActionForm.getSid();
      String pd_body = fTTestActionForm.getPd_body();
      String brand = fTTestActionForm.getBrand();
      String version = fTTestActionForm.getVersion();
      boolean flag1 =FTService.reset_tx(sid);
      return actionMapping.findForward("update_data");
    } else {
      return actionMapping.findForward("fail");
    }
  }

  private void jbInit() throws Exception {
  }
}
