package com.mxic.oiplus.oimaintain;

public class FTTFormBean {
  private String backend_option;
  private String body_size;
  private String brand;
  private String c_grade;
  private String w_grade;
  private String y_grade;
  private String j_grade;
  private String k_grade;
  private String l_grade;
  private String n_grade;
  private String b_grade;
  private String e_grade;
  private String s_grade;
  private String comment;
  private String i_grade;
  private String pd_body;
  private String pd_code;
  private String pg_mode;
  private String pg_name;
  private int pg_status;
  private String pg_type;
  private int pin_count;
  private String plant_name;
  private String site;
  private String subsystem_type;
  private String test_mode;
  private String tester_type;
  private String verson;
  private Integer tag;
  private int pg_id;
  private String be_opt;
  private String device_size;
  private String tester;
  private String notes;
  private int sid;
  private String hw_configure;

  public FTTFormBean(String sbackend_option,
                     String sbody_size,
                     String sbrand,
                     String sc_grade,
                     String sw_grade,
                     String sy_grade,
                     String sj_grade,
                     String sk_grade,
                     String sl_grade,
                     String sn_grade,
                     String sb_grade,
                     String se_grade,
                     String ss_grade,
                     String scomment,
                     String si_grade,
                     String spd_body,
                     String spd_code,
                     String spg_mode,
                     String spg_name,
                     int spg_status,
                     String spg_type,
                     int spin_count,
                     String splant_name,
                     String ssite,
                     String ssubsystem_type,
                     String stest_mode,
                     String stester_type,
                     String sverson,
                     Integer stag,
                     int spg_id,
                     String sbe_opt,
                     String sdevice_size,
                     String stester,
                     String snotes,
                     int ssid) {

    backend_option = sbackend_option;
    body_size = sbody_size;
    brand = sbrand;
    c_grade = sc_grade;
    w_grade = sw_grade;
    y_grade = sy_grade;
    j_grade = sj_grade;
    k_grade = sk_grade;
    l_grade = sl_grade;
    n_grade = sn_grade;
    b_grade = sb_grade;
    e_grade = se_grade;
    s_grade = ss_grade;
    comment = scomment;
    i_grade = si_grade;
    pd_body = spd_body;
    pd_code = spd_code;
    pg_mode = spg_mode;
    pg_name = spg_name;
    pg_status = spg_status;
    pg_type = spg_type;
    pin_count = spin_count;
    plant_name = splant_name;
    site = ssite;
    subsystem_type = ssubsystem_type;
    test_mode = stest_mode;
    tester_type = stester_type;
    verson = sverson;
    tag = stag;
    be_opt = sbe_opt;
    device_size = sdevice_size;
    tester = stester;
    notes = snotes;
    sid = ssid;
  }

  public FTTFormBean() {
  }

  public String getBackend_option() {
    return backend_option;
  }

  public void setBackend_option(String backend_option) {
    this.backend_option = backend_option;
  }

  public void setVerson(String verson) {
    this.verson = verson;
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
    this.pin_count = pin_count;
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

  public String getTest_mode() {
    return test_mode;
  }

  public String getTester_type() {
    return tester_type;
  }

  public String getVerson() {
    return verson;
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

  private void jbInit() throws Exception {
  }

public String getHw_configure() {
	return hw_configure;
}

public void setHw_configure(String hw_configure) {
	this.hw_configure = hw_configure;
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
}
