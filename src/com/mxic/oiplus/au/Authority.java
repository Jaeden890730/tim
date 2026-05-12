package com.mxic.oiplus.au;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Authority {

  public Authority() {
  }
  private String user_id;
  private String user_name;
  private String user_password;
  private com.mxic.oiplus.au.AUACTForm[] act_action;
  private String real_name;
  private String bo_user;
  private String bo_password;
  private String dept_id;
  private String dept_name;
  private String employee_no;
  private String notes_id;
  private String dept_group;
  public String getUser_id() {
    return user_id;
  }
  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }
  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }
  public String getUser_name() {
    return user_name;
  }
  public void setUser_password(String user_password) {
    this.user_password = user_password;
  }
  public String getUser_password() {
    return user_password;
  }
  public void setAct_action(com.mxic.oiplus.au.AUACTForm[] act_action) {
    this.act_action = act_action;
  }
  public com.mxic.oiplus.au.AUACTForm[] getAct_action() {
    return act_action;
  }
  public void setReal_name(String real_name) {
    this.real_name = real_name;
  }
  public String getReal_name() {
    return real_name;
  }
  public void setBo_user(String bo_user) {
    this.bo_user = bo_user;
  }
  public String getBo_user() {
    return bo_user;
  }
  public void setBo_password(String bo_password) {
    this.bo_password = bo_password;
  }
  public String getBo_password() {
    return bo_password;
  }
  public void setDept_id(String dept_id) {
    this.dept_id = dept_id;
  }
  public String getDept_id() {
    return dept_id;
  }
  public void setDept_name(String dept_name) {
    this.dept_name = dept_name;
  }
  public String getDept_name() {
    return dept_name;
  }
  public void setEmployee_no(String employee_no) {
    this.employee_no = employee_no;
  }
  public String getEmployee_no() {
    return employee_no;
  }
  public void setNotes_id(String notes_id) {
    this.notes_id = notes_id;
  }
  public String getNotes_id() {
    return notes_id;
  }
  public void setDept_group(String dept_group) {
    this.dept_group = dept_group;
  }
  public String getDept_group() {
    return dept_group;
  }
}
