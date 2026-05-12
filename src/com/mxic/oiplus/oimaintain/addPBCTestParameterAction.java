package com.mxic.oiplus.oimaintain;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.TDSLogger;

public class addPBCTestParameterAction extends Action {
  public addPBCTestParameterAction() {
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

    addPBCTestParameterForm fm = (addPBCTestParameterForm) actionForm;
    Connection conn;
    StringBuffer message = new StringBuffer();
    boolean errorFlag = false;
    StringBuffer insSql = new StringBuffer();

	try {
		conn = DBConnection.getConnection();
		conn.setAutoCommit(true);
	} catch (Exception e) {
		TDSLogger.println(e);
		return actionMapping.findForward("failure");
	}

    int sid = fm.getSid();
    String product_body = fm.getProduct_body();
    String brand = fm.getBrand();
    String version = fm.getVersion();
    String pgm_id[] = fm.getPgm_id();
    String test_type[] = fm.getTest_type();
    String backend_option[] = fm.getBackend_option();
    int pin_count[] = fm.getPin_count();
    String program_name[] = fm.getProgram_name();
    String tester[] = fm.getTester();
    String site[] = fm.getSite();
    String actual_file[] = fm.getActual_file();
    String package_type[] = fm.getPackage_type();
//    String tf_comment[] = fm.getTf_comment();
//    int i_grade[] = fm.getI_grade();
//    int c_grade[] = fm.getC_grade();
    String hw_configure[] = fm.getHw_configure();
	String v_pgm_id = null;
	String v_test_type = null;
	String v_backend_option = null;
	int v_pin_count = -1;
	String v_program_name = null;
	String v_tester = null;
	String v_site = null;
	String v_actual_file = null;
	String v_package_type = null;
	String v_body_size = null;
	//String v_hw_configure = null;


    String body_size[] = fm.getBody_size();

    if (pgm_id == null)
    	return actionMapping.findForward("save");
    for (int i=0; i<pgm_id.length; i++) {
    	try {
    		v_pgm_id = pgm_id[i];
    		v_test_type = test_type[i];
    		v_backend_option = backend_option[i];
    		v_pin_count = pin_count[i];
    		v_program_name = program_name[i];
    		v_tester = tester[i];
    		v_site = site[i];
    		v_actual_file = actual_file[i];
    		v_package_type = package_type[i];
//    		String v_tf_comment = tf_comment[i];
//    		int v_i_grade = i_grade[i];
//    		int v_c_grade = c_grade[i];
    		v_body_size = body_size[i];
    		//v_hw_configure = hw_configure[i];

    		StringBuffer sql = new StringBuffer();
    		sql.append(
    				"SELECT count(*) as total_count FROM tf_test_parameter_pbc_tx where sid='" + sid +
    				"' and pgm_id='" + v_pgm_id +
    				"' and product_body='" + product_body +
    				"' and brand='" + brand +
    				"' and version='" + version +
    				"' and test_type='" + v_test_type +
    				"' and backend_option='" + v_backend_option +
    				"' and pin_count='" + v_pin_count +
    				"' and package_type='" + v_package_type +
    				"' and body_size='" + v_body_size +
    				"' and  tester='" + v_tester +
    				"' and site='" + v_site +
    				"' and actual_file ='" + v_actual_file +
    				"' and program_name='" + v_program_name + "' ");

    		PreparedStatement ps = conn.prepareStatement(sql.toString());
    		ResultSet rs = ps.executeQuery();
    		while (rs.next()) {
    			if (rs.getInt("total_count") == 0) {
    				insSql = new StringBuffer();
    				insSql.append("Insert into tf_test_parameter_pbc_tx ");
    				insSql.append("(sid,tag,pgm_id,product_body,brand,version,test_type,backend_option,");
    				insSql.append("pin_count,package_type,body_size,tester,site,program_name,actual_file,hw_configure");
                    if (v_test_type.equals("AVI"))
                      insSql.append(",i_grade,c_grade,w_grade,y_grade,j_grade,k_grade,l_grade,n_grade,b_grade,e_grade,s_grade");
                    insSql.append(") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
                    if (v_test_type.equals("AVI"))
                      insSql.append(",'NA','NA','NA','NA','NA','NA','NA','NA','NA','NA','NA'");
                    insSql.append(")");
    				PreparedStatement ps_insert = conn.prepareStatement(insSql.toString());
    				ps_insert.setInt(1, sid);
    				ps_insert.setString(2, "1");
    				ps_insert.setInt(3, Integer.parseInt(v_pgm_id));
    				ps_insert.setString(4, product_body);
    				ps_insert.setString(5, brand);
    				ps_insert.setInt(6, Integer.parseInt(version));
    				if (v_test_type == null || v_test_type.equals("")) {
    					ps_insert.setString(7, " ");
    				} else {
    					ps_insert.setString(7, v_test_type);
    				}
    				if (v_backend_option == null || v_backend_option.equals("")) {
    					ps_insert.setString(8, " ");
    				} else {
    					ps_insert.setString(8, v_backend_option);
    				}
    				if (String.valueOf(v_pin_count) == null || String.valueOf(v_pin_count).equals("")) {
    					ps_insert.setInt(9, 0);
    				} else {
    					ps_insert.setInt(9, v_pin_count);
    				}
    				if (v_package_type == null || v_package_type.equals("")) {
    					ps_insert.setString(10, " ");
    				} else {
    					ps_insert.setString(10, v_package_type);
    				}
    				if (v_body_size == null || v_body_size.equals("")) {
    					ps_insert.setString(11, " ");
    				} else {
    					ps_insert.setString(11, v_body_size);
    				}
    				if (v_tester == null || v_tester.equals("")) {
    					ps_insert.setString(12, " ");
    				} else {
    					ps_insert.setString(12, v_tester);
    				}
    				if (v_site == null || v_site.equals("")) {
    					ps_insert.setString(13, " ");
    				} else {
    					ps_insert.setString(13, v_site);
    				}
    				if (v_program_name == null || v_program_name.equals("")) {
    					ps_insert.setString(14, " ");
    				} else {
    					ps_insert.setString(14, v_program_name);
    				}
    				if (v_actual_file == null || v_actual_file.equals("")) {
    					ps_insert.setString(15, " ");
    				} else {
    					ps_insert.setString(15, v_actual_file);
    				}
    				ps_insert.setString(16, "NA");

//    				System.out.println(ps_insert.toString());
    				ps_insert.executeUpdate();
    			}
    		}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		DBConnection.rollback(conn);
    		TDSLogger.println(ex.getMessage());
    		errorFlag = true;
    		message.append("Insert fail: " + PBCService.toString(v_pgm_id, product_body, brand, version, v_test_type, v_backend_option,
					Integer.toString(v_pin_count), v_package_type, v_body_size, v_tester, v_site, v_program_name, v_actual_file, "0", "0", "0", "0", "0","0", "0","0","0","0","0", "NA") + "\n");
    	}
    }
	DBConnection.close(conn);
	if (errorFlag)
		return actionMapping.findForward("failure");
	try {
		servletResponse.sendRedirect(servletResponse.encodeRedirectURL(servletRequest.getContextPath() + "/OImaintain/pbcTestParameterAction.do?sid="+sid));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    //return actionMapping.findForward("save");
	return null;
  }

  private void jbInit() throws Exception {
  }
}
