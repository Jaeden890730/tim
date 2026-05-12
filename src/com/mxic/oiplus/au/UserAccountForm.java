package com.mxic.oiplus.au;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.io.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

public class UserAccountForm extends ActionForm {

  public UserAccountForm() {
  }
  private String user_id;
  private String user_name;
  private String employee_no;
  private String real_name;
  private String password;
  private String notes_id;
  private String action_type;
  private String password_again;
  private String dept_id;
  private String dept_name;
  private String return_flag;
  private String bo_user;
  private String bo_password;
  private String bo_password_again;
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
  public void setEmployee_no(String employee_no) {
    this.employee_no = employee_no;
  }
  public String getEmployee_no() {
    return employee_no;
  }
  public void setReal_name(String real_name) {
    this.real_name = real_name;
  }
  public String getReal_name() {
    return real_name;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getPassword() {
    return password;
  }
  public void setNotes_id(String notes_id) {
    this.notes_id = notes_id;
  }
  public String getNotes_id() {
    return notes_id;
  }
  public void setAction_type(String action_type) {
    this.action_type = action_type;
  }
  public String getAction_type() {
    return action_type;
  }
  public void setPassword_again(String password_again) {
    this.password_again = password_again;
  }
  public String getPassword_again() {
    return password_again;
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
  public void setReturn_flag(String return_flag) {
    this.return_flag = return_flag;
  }
  public String getReturn_flag() {
    return return_flag;
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
  public void setBo_password_again(String bo_password_again) {
    this.bo_password_again = bo_password_again;
  }
  public String getBo_password_again() {
    return bo_password_again;
  }
  public void setDept_group(String dept_group) {
    this.dept_group = dept_group;
  }
  public String getDept_group() {
    return dept_group;
  }
  public void reset(ActionMapping actionMapping,
                  HttpServletRequest servletRequest) {
          try {
            servletRequest.setCharacterEncoding("Big5");
          }
          catch (UnsupportedEncodingException ex) {
          }
}

}
