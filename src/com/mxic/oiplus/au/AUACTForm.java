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

public class AUACTForm extends ActionForm {

  public AUACTForm() {
  }
  private String action_type;
  private String act_sid;
  private String act_action;
  private String act_class;
  private String act_desc;
  private String return_flag;
  public String getAction_type() {
    return action_type;
  }
  public void setAction_type(String action_type) {
    this.action_type = action_type;
  }
  public void setAct_sid(String act_sid) {
    this.act_sid = act_sid;
  }
  public String getAct_sid() {
    return act_sid;
  }
  public void setAct_action(String act_action) {
    this.act_action = act_action;
  }
  public String getAct_action() {
    return act_action;
  }
  public void setAct_class(String act_class) {
    this.act_class = act_class;
  }
  public String getAct_class() {
    return act_class;
  }
  public void setAct_desc(String act_desc) {
    this.act_desc = act_desc;
  }
  public String getAct_desc() {
    return act_desc;
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
