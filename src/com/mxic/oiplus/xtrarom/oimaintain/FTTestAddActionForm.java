package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class FTTestAddActionForm extends ActionForm {
  private String[] record_id;
  private String listControl;
  private String backend_option;
  private String body_size;
  private String brand;
  private String c_grade;
  private String comment;
  private String i_grade;
  private String pd_body;
  private String pd_code;
  private String pg_mode;
  private String pg_name;
  private String actual_file;
  private String pgm_special_control;
  private String one_main_pgm_group_version;
  private String one_main_pgm_num;
  private int pg_status;
  private String pg_type;
  private int pin_count;
  private String plant_name;
  private String site;
  private String subsystem_type;
  private String[] test_mode;
  private String test_mode_str;
  private String tester_type;
  private String version;
  private Integer tag;
  private int pg_id;
  private String be_opt;
  private String device_size;
  private String tester;
  private String notes;
  private int sid;
  private String os_version;
  private String pta_status;
  private String pta_auth;
  
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

  public void setTest_mode(String[] test_mode) {
    this.test_mode = test_mode;
  }
  public void setTest_mode_str(String test_mode_str) {
    this.test_mode_str = test_mode_str;
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
    this.pin_count = pin_count;
  }

  public String getListControl() {
    return listControl;
  }

  public void setPg_type(String pg_type) {
    this.pg_type = pg_type;
  }

  public void setPg_status(int pg_status) {
    this.pg_status = pg_status;
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

  public void setOne_main_pgm_num(String one_main_pgm_num) {
		this.one_main_pgm_num = one_main_pgm_num;
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

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setBody_size(String body_size) {
    this.body_size = body_size;
  }

  public void setTag(Integer tag) {
    this.tag = tag;
  }

  public void setPg_id(int pg_id) {
    this.pg_id = pg_id;
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

  public void setSid(int sid) {
    this.sid = sid;
  }

  public String getBody_size() {
    return body_size;
  }

  public String getBrand() {
    return brand;
  }

  public String getC_grade() {
    return c_grade;
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

  public String getOne_main_pgm_num() {
	return one_main_pgm_num;
  }

  public int getPg_status() {
    return pg_status;
  }

  public String getPg_type() {
    return pg_type;
  }

  public int getPin_count() {
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

  public String[] getTest_mode() {
    return test_mode;
  }

  public String getTest_mode_str() {
    return test_mode_str;
  }

  public void setListControl(String listControl) {
    this.listControl = listControl;
  }

  public void setRecord_id(String[] record_id) {
    this.record_id = record_id;
  }

  public String[] getRecord_id() {
    return record_id;
  }

  public String getTester_type() {
    return tester_type;
  }

  public String getVersion() {
    return version;
  }

  public Integer getTag() {
    return tag;
  }

  public int getPg_id() {
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

  public int getSid() {
    return sid;
  }

  public String getHaveproject() {
    if(test_mode!=null){
      return "checked";
    } else{
      return "";
    }
  }

  public String getOs_version() {
	return os_version;
}

public void setOs_version(String osVersion) {
	os_version = osVersion;
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

	public String getPta_status() {
		return pta_status;
	}

	public void setPta_status(String pta_status) {
		this.pta_status = pta_status;
	}

	public String getPta_auth() {
		if ("No PTA Info.".equals(this.pta_status)) {
			return "Y";
		} else if (this.pta_status != null && (this.pta_status.contains("已生效") || this.pta_status.contains("暫無配件")|| this.pta_status.contains("直屬主管會簽中")|| this.pta_status.contains("執行中"))) {
			return "Y";
		} else {
			return "N";
		}

	}

	public void setPta_auth(String pta_auth) {
		this.pta_auth = pta_auth;
	}
}
