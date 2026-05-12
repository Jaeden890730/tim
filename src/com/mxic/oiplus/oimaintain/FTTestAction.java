package com.mxic.oiplus.oimaintain;

import java.util.ArrayList;

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
      //String[] i_grade = servletRequest.getParameterValues("i_grade");
      //String[] c_grade = servletRequest.getParameterValues("c_grade");
      //String[] w_grade = servletRequest.getParameterValues("n_grade");
      //String[] y_grade = servletRequest.getParameterValues("n_grade");
      
      
      
      String[] s_grade = null;
      if(pg_id!=null)
         s_grade = new String[pg_id.length];
      String[] comment = servletRequest.getParameterValues("comment");
      String[] hw_configure = null,i_grade = null,c_grade = null,w_grade = null,y_grade = null,j_grade = null,k_grade = null,l_grade = null,n_grade = null,b_grade=null,e_grade=null;
	  if(test_type!=null){
		hw_configure= new String[test_type.length];
		i_grade= new String[test_type.length];
		c_grade= new String[test_type.length];
		w_grade= new String[test_type.length];
		y_grade= new String[test_type.length];
	    j_grade= new String[test_type.length];
	    k_grade= new String[test_type.length];
	    l_grade= new String[test_type.length];
	    n_grade= new String[test_type.length];
	    b_grade= new String[test_type.length];
	    e_grade= new String[test_type.length];
		
        for(int i=0; i<pg_id.length; i++){
    	  //S_Grade
    	  String[] s_grade_radio = servletRequest.getParameterValues("s_grade_radio_"+i);
    	  String[] s_grade_content = servletRequest.getParameterValues("s_grade_content_"+i);
    	  if(s_grade_radio[0].equals("AEB")){
    		  String tmp = s_grade_content[0].replaceAll("<BR>", ";");
    		  tmp = tmp.replaceAll("<br>", ";");
    		  s_grade[i] = tmp.substring(0, tmp.length()-1);
    	  }else{
    		  s_grade[i]=s_grade_radio[0];
    	  }
     	  //TDSLogger.println("s_grade_radio[0]="+s_grade_radio[0]);
    	  TDSLogger.println("s_grade_content1[0]="+s_grade_content[0]);
    	  TDSLogger.println("s_grade[i]="+s_grade[i]);
    	  
          //N_Grade
          String[] n1_grade_radio = servletRequest.getParameterValues("n1_grade_radio_"+i);
          String[] n1_grade_content = servletRequest.getParameterValues("n1_grade_content_"+i);
          
          n1_grade_content[0] = n1_grade_content[0].replaceAll("¢J", "");
          n1_grade_content[0] = n1_grade_content[0].replaceAll("<br>", "<BR>");
          String[] value = n1_grade_content[0].split("<BR>");
          if((value != null)) {
              i_grade[i]="NA";
              c_grade[i]="NA";
              w_grade[i]="NA";
              y_grade[i]="NA";
              j_grade[i]="NA";
              k_grade[i]="NA";
              l_grade[i]="NA";
              n_grade[i]="NA";
              b_grade[i]="NA";
              e_grade[i]="NA";
              
              for (int j = 0 ; j < value.length; j++) {
                  if(value[j].startsWith("I:"))
                      i_grade[i] = value[j].substring(2);
                  else if(value[j].startsWith("C:"))
                      c_grade[i] = value[j].substring(2);
                  else if(value[j].startsWith("W:"))
                      w_grade[i] = value[j].substring(2);
                  else if(value[j].startsWith("Y:"))
                      y_grade[i] = value[j].substring(2);
                  else if(value[j].startsWith("J:"))
                      j_grade[i] = value[j].substring(2);
                  else if(value[j].startsWith("K:"))
                      k_grade[i] = value[j].substring(2);
                  else if(value[j].startsWith("L:"))
                      l_grade[i] = value[j].substring(2);
                  else if(value[j].startsWith("N:"))
                      n_grade[i] = value[j].substring(2);
                  else if(value[j].startsWith("B:"))
                      b_grade[i] = value[j].substring(2);
                  else if(value[j].startsWith("E:"))
                      e_grade[i] = value[j].substring(2);
              }
          }
    	  
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
                                              c_grade,w_grade,y_grade,j_grade,k_grade,l_grade,n_grade,b_grade,e_grade, s_grade, comment, hw_configure, "update_cmd");

      } else if (listControl.equals("submit_data")) {
        boolean flag1 = FTService.update_data(sid, pd_body, brand,
                                              version, test_type, be_opt, pin_count, pg_type,
                                              body_size, tester, site, pg_name, actual_file, pgm_special_control, one_main_pgm_group_version, pg_id, i_grade,
                                              c_grade,w_grade,y_grade,j_grade,k_grade,l_grade,n_grade,b_grade,e_grade, s_grade, comment, hw_configure, "submit_cmd");

      }
      return actionMapping.findForward("update_data");
    } else if (listControl.equals("delete_row")) {
      String record_list = fTTestActionForm.getRecord_list();
      //int sid = Integer.parseInt(fTTestActionForm.getSid());
      String[] sidd = servletRequest.getParameterValues("sid");
      int sid = Integer.parseInt(sidd[0]);
      String pd_body = fTTestActionForm.getPd_body();
      String brand = fTTestActionForm.getBrand();
      String version = fTTestActionForm.getVersion();
      boolean flag1 = FTService.delete_row(record_list, sid, brand, version, pd_body);
      return actionMapping.findForward("update_data");
    } else if (listControl.equals("reset_tx")) {
      //int sid = Integer.parseInt(fTTestActionForm.getSid());
      String[] sidd = servletRequest.getParameterValues("sid");
      int sid = Integer.parseInt(sidd[0]);
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
