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

public class AUAssignForm extends ActionForm {

  public AUAssignForm() {
  }
  private String user_id;
  private String user_name;
  private String[] grp_sid;
  private String action_type;
  private String[] act_sid;
  private String grp_id;
  private String grp_name;
  private String return_flag;
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
  public void setGrp_sid(String[] grp_sid) {
    this.grp_sid = grp_sid;
  }
  public String[] getGrp_sid() {
    return grp_sid;
  }
  public void setAction_type(String action_type) {
    this.action_type = action_type;
  }
  public String getAction_type() {
    return action_type;
  }
  public void setAct_sid(String[] act_sid) {
    this.act_sid = act_sid;
  }
  public String[] getAct_sid() {
    return act_sid;
  }
  public void setGrp_id(String grp_id) {
    this.grp_id = grp_id;
  }
  public String getGrp_id() {
    return grp_id;
  }
  public void setGrp_name(String grp_name) {
    this.grp_name = grp_name;
  }
  public String getGrp_name() {
    return grp_name;
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
