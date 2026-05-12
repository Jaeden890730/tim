package com.mxic.oiplus.xtrarom.oimaintain;
import java.io.IOException;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.util.TDSLogger;

public class savePBCTestParameterAction extends Action {

	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm,
			HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) {

		PBCTestParameterForm fm = (PBCTestParameterForm) actionForm;
		servletRequest.setAttribute("proTestRouteBeanAF",fm);

		String listControl = fm.getListControl();

		if (listControl.equals("add_vendor")) {
			String record_id = fm.getRecord_id();
			PBCTestParameterForm[] rs = PBCService.getvendor(record_id);
			servletRequest.setAttribute("list1", rs);
			int length = rs != null?rs.length:0;
			servletRequest.setAttribute("recno", ""+length);
			return actionMapping.findForward("add_vendor");
		} else if (listControl.equals("update_data") ||
				listControl.equals("submit_data")) {
			String[] sid = servletRequest.getParameterValues("sid");
			String[] pd_body = servletRequest.getParameterValues("product_body");
			String[] brand = servletRequest.getParameterValues("brand");
			String[] version = servletRequest.getParameterValues("version");
			String[] test_type = servletRequest.getParameterValues("test_type");
			String[] be_opt = servletRequest.getParameterValues("backend_option");
			String[] pin_count = servletRequest.getParameterValues("pin_count");
			String[] pg_type = servletRequest.getParameterValues("package_type");
			String[] body_size = servletRequest.getParameterValues("body_size");
			String[] tester = servletRequest.getParameterValues("tester");
			String[] site = servletRequest.getParameterValues("site");
			String[] pg_name = servletRequest.getParameterValues("program_name");
			String[] actual_file = servletRequest.getParameterValues("actual_file");
			String[] pg_id = servletRequest.getParameterValues("pgm_id");
			String[] i_grade = servletRequest.getParameterValues("i_grade");
			String[] c_grade = servletRequest.getParameterValues("c_grade");
			String[] comment = servletRequest.getParameterValues("tf_comment");
			String[] hw_configure = null;
			if(test_type!=null){
				hw_configure= new String[test_type.length];
				for(int i=0; i<test_type.length; i++){
	    		  //    	HW Configure
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
				PBCService.update_data(sid, pd_body, brand,
						version, test_type, be_opt, pin_count, pg_type,
						body_size, tester, site, pg_name, actual_file, pg_id, i_grade,
						c_grade, comment, "update_cmd", hw_configure);

			} else if (listControl.equals("submit_data")) {
				PBCService.update_data(sid, pd_body, brand,
						version, test_type, be_opt, pin_count, pg_type,
						body_size, tester, site, pg_name, actual_file, pg_id, i_grade,
						c_grade, comment, "submit_cmd", hw_configure);

			}
			try {
				servletResponse.sendRedirect(servletResponse.encodeRedirectURL(servletRequest.getContextPath() + "/OImaintain/pbcTestParameterActionX.do?sid="+fm.getSid()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//return actionMapping.findForward("update_data");
			return null;
		} else if (listControl.equals("delete_row")) {
			String record_id = fm.getRecord_id();
			int sid = fm.getSid();
			String pd_body = fm.getProduct_body();
			String brand = fm.getBrand();
			String version = fm.getVersion();
			PBCService.delete_row(record_id, sid, brand, version, pd_body);
			try {
				servletResponse.sendRedirect(servletResponse.encodeRedirectURL(servletRequest.getContextPath() + "/OImaintain/pbcTestParameterActionX.do?sid="+fm.getSid()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//return actionMapping.findForward("update_data");
			return null;
		} else if (listControl.equals("reset_tx")) {
			int sid = fm.getSid();
			String pd_body = fm.getProduct_body();
			String brand = fm.getBrand();
			String version = fm.getVersion();
			PBCService.reset_tx(sid);
			try {
				servletResponse.sendRedirect(servletResponse.encodeRedirectURL(servletRequest.getContextPath() + "/OImaintain/pbcTestParameterActionX.do?sid="+fm.getSid()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//return actionMapping.findForward("update_data");
			return null;
		} else {
			return actionMapping.findForward("fail");
		}

	}

}
