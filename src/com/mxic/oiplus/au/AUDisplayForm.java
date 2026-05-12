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

public class AUDisplayForm extends ActionForm {

  public AUDisplayForm() {
  }
  private String user_name;
  private com.mxic.oiplus.au.UserAccountForm[] user_account_list;
  private String action_type;
  private String act_action;
  private com.mxic.oiplus.au.AUACTForm[] au_act_list;
  private String grp_name;
  private com.mxic.oiplus.au.AUGroupForm[] au_group_list;
  private String dept_name;
  private com.mxic.oiplus.au.UserDepartmentForm[] user_department_list;
  private String dept_id;
  private String return_flag;
  private String dept_group;
  public String getUser_name() {
    return user_name;
  }
  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }
  public void setUser_account_list(com.mxic.oiplus.au.UserAccountForm[] user_account_list) {
    this.user_account_list = user_account_list;
  }
  public com.mxic.oiplus.au.UserAccountForm[] getUser_account_list() {
    return user_account_list;
  }
  public void setAction_type(String action_type) {
    this.action_type = action_type;
  }
  public String getAction_type() {
    return action_type;
  }
  public void setAct_action(String act_action) {
    this.act_action = act_action;
  }
  public String getAct_action() {
    return act_action;
  }
  public void setAu_act_list(com.mxic.oiplus.au.AUACTForm[] au_act_list) {
    this.au_act_list = au_act_list;
  }
  public com.mxic.oiplus.au.AUACTForm[] getAu_act_list() {
    return au_act_list;
  }
  public void setGrp_name(String grp_name) {
    this.grp_name = grp_name;
  }
  public String getGrp_name() {
    return grp_name;
  }
  public void setAu_group_list(com.mxic.oiplus.au.AUGroupForm[] au_group_list) {
    this.au_group_list = au_group_list;
  }
  public com.mxic.oiplus.au.AUGroupForm[] getAu_group_list() {
    return au_group_list;
  }
  public void setDept_name(String dept_name) {
    this.dept_name = dept_name;
  }
  public String getDept_name() {
    return dept_name;
  }
  public void setUser_department_list(com.mxic.oiplus.au.UserDepartmentForm[] user_department_list) {
    this.user_department_list = user_department_list;
  }
  public com.mxic.oiplus.au.UserDepartmentForm[] getUser_department_list() {
    return user_department_list;
  }
  public void setDept_id(String dept_id) {
    this.dept_id = dept_id;
  }
  public String getDept_id() {
    return dept_id;
  }
  public void setReturn_flag(String return_flag) {
    this.return_flag = return_flag;
  }
  public String getReturn_flag() {
    return return_flag;
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
