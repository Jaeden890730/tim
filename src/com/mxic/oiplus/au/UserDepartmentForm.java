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

public class UserDepartmentForm extends ActionForm {

  public UserDepartmentForm() {
  }
  private String action_type;
  private String dept_id;
  private String dept_name;
  private String dept_desc;
  private String return_flag;
  private String dept_group;
  public String getAction_type() {
    return action_type;
  }
  public void setAction_type(String action_type) {
    this.action_type = action_type;
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
  public void setDept_desc(String dept_desc) {
    this.dept_desc = dept_desc;
  }
  public String getDept_desc() {
    return dept_desc;
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
