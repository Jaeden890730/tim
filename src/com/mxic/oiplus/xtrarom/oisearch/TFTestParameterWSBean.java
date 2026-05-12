package com.mxic.oiplus.xtrarom.oisearch;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class TFTestParameterWSBean {
  public TFTestParameterWSBean() {
  }

  private String sid;
  private String pgm_id;
  private String product_body;
  private String brand;
  private String version;
  private String mask_option;
  private String test_type;
  private String tester;
  private String site;
  private String program_name;
  private String pgm_special_control;
  private String one_main_pgm_group_version;
  private String i_grade;
  private String c_grade;
  private String tf_comment;
  private String temp;
  private String hw_configure;

  public String getHw_configure() {
	return hw_configure;
}

public void setHw_configure(String hw_configure) {
	this.hw_configure = hw_configure;
}

public void setSid(String sid) {
    this.sid = sid;
  }

  public String getSid() {
    return sid;
  }

  public void setPgm_id(String pgm_id) {
    this.pgm_id = pgm_id;
  }

  public String getPgm_id() {
    return pgm_id;
  }

  public void setProduct_body(String product_body) {
    this.product_body = product_body;
  }

  public String getProduct_body() {
    return product_body;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getBrand() {
    return brand;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }

  public void setMask_option(String mask_option) {
    this.mask_option = mask_option;
  }

  public String getMask_option() {
    return mask_option;
  }

  public void setTest_type(String test_type) {
    this.test_type = test_type;
  }

  public String getTest_type() {
    return test_type;
  }

  public void setTester(String tester) {
    this.tester = tester;
  }

  public String getTester() {
    return tester;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public String getSite() {
    return site;
  }

  public void setProgram_name(String program_name) {
    this.program_name = program_name;
  }

  public String getProgram_name() {
    return program_name;
  }

  public void setI_grade(String i_grade) {
    this.i_grade = i_grade;
  }

  public String getI_grade() {
    return i_grade;
  }

  public void setC_grade(String c_grade) {
    this.c_grade = c_grade;
  }

  public String getC_grade() {
    return c_grade;
  }

  public void setTf_comment(String tf_comment) {
    this.tf_comment = tf_comment;
  }

  public String getTf_comment() {
    return tf_comment;
  }

  public String getTemp() {
    return temp;
  }

  public void setTemp(String temp) {
    this.temp = temp;
  }
  public String getOne_main_pgm_group_version() {
		return one_main_pgm_group_version;
  }

  public void setOne_main_pgm_group_version(String one_main_pgm_group_version) {
	this.one_main_pgm_group_version = one_main_pgm_group_version;
  }
  public String getPgm_special_control() {
	return pgm_special_control;
  }

  public void setPgm_special_control(String pgm_special_control) {
	this.pgm_special_control = pgm_special_control;
  }	
}
