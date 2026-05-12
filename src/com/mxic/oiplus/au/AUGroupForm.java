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

public class AUGroupForm extends ActionForm {

  public AUGroupForm() {
  }
  private String action_type;
  private String grp_sid;
  private String grp_name;
  private String grp_desc;
  private String return_flag;
  public String getAction_type() {
    return action_type;
  }
  public void setAction_type(String action_type) {
    this.action_type = action_type;
  }
  public void setGrp_sid(String grp_sid) {
    this.grp_sid = grp_sid;
  }
  public String getGrp_sid() {
    return grp_sid;
  }
  public void setGrp_name(String grp_name) {
    this.grp_name = grp_name;
  }
  public String getGrp_name() {
    return grp_name;
  }
  public void setGrp_desc(String grp_desc) {
    this.grp_desc = grp_desc;
  }
  public String getGrp_desc() {
    return grp_desc;
  }
  public void setReturn_flag(String return_flag) {
    this.return_flag = return_flag;
  }
  public String getReturn_flag() {
    return return_flag;
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
