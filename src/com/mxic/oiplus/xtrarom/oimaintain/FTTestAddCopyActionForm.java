package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;
import org.apache.struts.action.*;

public class FTTestAddCopyActionForm extends ActionForm {

  private String listControl2;
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
  private int pg_status;
  private String pg_type;
  private int pin_count;
  private String plant_name;
  private String site;
  private String subsystem_type;
  private String[] test_mode;
  private String test_mode_str;
  private String tester_type;
  private String verson;
  private Integer tag;
  private int pg_id;
  private String be_opt;
  private String device_size;
  private String tester;
  private String notes;
  private int sid;

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

  public void setTest_mode(String[] test_mode) {
    this.test_mode = test_mode;
  }

  public void setTest_mode_str(String test_mode_str) {
    this.test_mode_str = test_mode_str;
  }

  public String getListControl2() {
    return listControl2;
  }

  public void setListControl2(String listControl2) {
    this.listControl2 = listControl2;
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

  public void setActual_file(String actual_file) {
    this.actual_file = actual_file;
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

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    /** @todo: finish this method, this is just the skeleton.*/
    return null;
  }

  public void reset(ActionMapping actionMapping,
                    HttpServletRequest servletRequest) {
  }
}
