package com.mxic.oiplus.oimaintain;

import java.io.*;
import java.util.Vector;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class FTTestActionForm extends ActionForm {
  private String[] site_data;
  private String site_str;
  private String record_id;
  private String record_list;
  private String listControl;
  private String backend_option;
  private String body_size;
  private String brand;
  private String c_grade;
  private String s_grade;
  private String comment;
  private String i_grade;
  private String w_grade;
  private String y_grade;
  private String j_grade;
  private String k_grade;
  private String l_grade;
  private String n_grade;
  private String b_grade;
  private String e_grade;
  private String pd_body;
  private String pd_code;
  private String pg_mode;
  private String pg_name;
  private String actual_file;
  private String pgm_special_control;
  private String one_main_pgm_group_version;
  private String pg_status;
  private String pg_type;
  private String pin_count;
  private String plant_name;
  private String site;
  private String subsystem_type;
  private String test_mode;
  private String tester_type;
  private String version;
  private String tag;
  private String pg_id;
  private String be_opt;
  private String device_size;
  private String tester;
  private String notes;
  private String status;
  private String sid;
  private Vector temperatureList;
  private String hw_configure;
  private String package_component;

  public String getListControl() {
    return listControl;
  }

  public String getBackend_option() {
    return backend_option;
  }

  public void setBackend_option(String backend_option) {
    this.backend_option = backend_option;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setTester_type(String tester_type) {
    this.tester_type = tester_type;
  }

  public void setTest_mode(String test_mode) {
    this.test_mode = test_mode;
  }

  public void setSubsystem_type(String subsystem_type) {
    this.subsystem_type = subsystem_type;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public void setPlant_name(String plant_name) {
    this.plant_name = plant_name;
  }

  public void setPin_count(int pin_count) {
    this.pin_count = String.valueOf(pin_count);
  }

  public void setPg_type(String pg_type) {
    this.pg_type = pg_type;
  }

  public void setPg_status(int pg_status) {
    this.pg_status = String.valueOf(pg_status);
  }

  public void setPg_name(String pg_name) {
    this.pg_name = pg_name;
  }

  public void setActual_file(String actual_file) {
    this.actual_file = actual_file;
  }
  
  public void setPgm_special_control(String pgm_special_control) {
	this.pgm_special_control = pgm_special_control;
  }
  public void setOne_main_pgm_group_version(String one_main_pgm_group_version) {
		this.one_main_pgm_group_version = one_main_pgm_group_version;
  }

  public void setRecord_id(String record_id) {
    this.record_id = record_id;
  }

  public String getRecord_id() {
    return record_id;
  }

  public void setRecord_list(String record_list) {
    this.record_list = record_list;
  }

  public String getRecord_list() {
    return record_list;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public void setSite_data(String[] site_data) {
    this.site_data = site_data;
  }

  public String[] getSite_data() {
    return site_data;
  }

  public void setPg_mode(String pg_mode) {
    this.pg_mode = pg_mode;
  }

  public void setPd_code(String pd_code) {
    this.pd_code = pd_code;
  }

  public void setPd_body(String pd_body) {
    this.pd_body = pd_body;
  }

  public void setI_grade(String i_grade) {
    this.i_grade = i_grade;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setC_grade(String c_grade) {
    this.c_grade = c_grade;
  }
  
  public void setS_grade(String s_grade) {
	    this.s_grade = s_grade;
	  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setBody_size(String body_size) {
    this.body_size = body_size;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setPg_id(int pg_id) {
    this.pg_id = String.valueOf(pg_id);
  }

  public void setBe_opt(String be_opt) {
    this.be_opt = be_opt;
  }

  public void setDevice_size(String device_size) {
    this.device_size = device_size;
  }

  public void setTester(String tester) {
    this.tester = tester;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setSid(String sid) {
    this.sid = String.valueOf(sid);
  }

  public String getBody_size() {
    return body_size;
  }

  public void setListControl(String listControl) {
    this.listControl = listControl;
  }

  public String getBrand() {
    return brand;
  }

  public void setSite_str(String site_str) {
    this.site_str = site_str;
  }
  
  public void setHw_configure(String hw_configure) {
	    this.hw_configure = hw_configure;
	  }

  public String getSite_str() {
    return site_str;
  }

  public String getC_grade() {
    return c_grade;
  }
  
  public String getS_grade() {
	    return s_grade;
	  }

  public String getComment() {
    return comment;
  }

  public String getI_grade() {
    return i_grade;
  }

  public String getPd_body() {
    return pd_body;
  }

  public String getPd_code() {
    return pd_code;
  }

  public String getPg_mode() {
    return pg_mode;
  }

  public String getPg_name() {
    return pg_name;
  }

  public String getActual_file() {
    return actual_file;
  }
  
  public String getPgm_special_control() {
    return pgm_special_control;
  }
  
  public String getOne_main_pgm_group_version() {
		return one_main_pgm_group_version;
  }

  public String getPg_status() {
    return pg_status;
  }

  public String getPg_type() {
    return pg_type;
  }

  public String getPin_count() {
    return pin_count;
  }

  public String getPlant_name() {
    return plant_name;
  }

  public String getSite() {
    return site;
  }

  public String getSubsystem_type() {
    return subsystem_type;
  }

  public String getTest_mode() {
    return test_mode;
  }

  public String getTester_type() {
    return tester_type;
  }

  public String getVersion() {
    return version;
  }

  public String getTag() {
    return tag;
  }

  public String getPg_id() {
    return pg_id;
  }

  public String getBe_opt() {
    return be_opt;
  }

  public String getDevice_size() {
    return device_size;
  }

  public String getTester() {
    return tester;
  }

  public String getNotes() {
    return notes;
  }

  public String getSid() {
    return sid;
  }
  
  public String getHw_configure() {
	    return hw_configure;
	  }

  public String getChangecolor(){
    if (tag.equals("1")){
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    /** @todo: finish this method, this is just the skeleton.*/
    return null;
  }

  public void reset(ActionMapping actionMapping,
                    HttpServletRequest servletRequest) {
    try {
      //轉成中文big5編碼
      servletRequest.setCharacterEncoding("big5");
    } catch (UnsupportedEncodingException ex) {
    }
  }

  /**
   * @return the temperatureList
   */
  public Vector getTemperatureList() {
	  return temperatureList;
  }

  /**
   * @param temperatureList the temperatureList to set
   */
  public void setTemperatureList(Vector temperatureList) {
	  this.temperatureList = temperatureList;
  }
  public String getProductType() {
	  return OiMaintainService.getProductType(sid);
  }

public String getN1_grade() {
    StringBuffer s = new StringBuffer();
    if(i_grade!=null)
        s.append("I:"+i_grade+";");
    if(c_grade!=null)
        s.append("C:"+c_grade+";");
    if(w_grade!=null)
        s.append("W:"+w_grade+";");
    if(y_grade!=null)
        s.append("Y:"+y_grade+";");
    if(j_grade!=null)
        s.append("J:"+j_grade+";");
    if(k_grade!=null)
        s.append("K:"+k_grade+";");
    if(l_grade!=null)
        s.append("L:"+l_grade+";");
    if(n_grade!=null)
        s.append("N:"+n_grade+";");
    if(b_grade!=null)
        s.append("B:"+b_grade+";");
    if(e_grade!=null)
        s.append("E:"+e_grade+";");
    
    if(s.length() > 0) {
        return s.substring(0, s.toString().length()-1);
    } else {
      return "";  
    }
}



public String getW_grade() {
    return w_grade;
}

public void setW_grade(String w_grade) {
    this.w_grade = w_grade;
}

public String getY_grade() {
    return y_grade;
}

public void setY_grade(String y_grade) {
    this.y_grade = y_grade;
}

public String getJ_grade() {
    return j_grade;
}

public void setJ_grade(String j_grade) {
    this.j_grade = j_grade;
}

public String getK_grade() {
    return k_grade;
}

public void setK_grade(String k_grade) {
    this.k_grade = k_grade;
}

public String getL_grade() {
    return l_grade;
}

public void setL_grade(String l_grade) {
    this.l_grade = l_grade;
}

public String getN_grade() {
    return n_grade;
}

public void setN_grade(String n_grade) {
    this.n_grade = n_grade;
}

public String getB_grade() {
    return b_grade;
}

public void setB_grade(String b_grade) {
    this.b_grade = b_grade;
}

public String getE_grade() {
    return e_grade;
}

public void setE_grade(String e_grade) {
    this.e_grade = e_grade;
}

public String getPackage_component() {
	return package_component;
}

public void setPackage_component(String package_component) {
	this.package_component = package_component;
}

}
