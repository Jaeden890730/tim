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

public class AULoginForm extends ActionForm {

  public AULoginForm() {
  }
  private String user_name;
  private String password;
  private String new_password;
  private String password_again;
  private String action_type;
  private String user_id;
  private String return_flag;
  private String redirectUrl;
  
  public String getRedirectUrl() {
	return redirectUrl;
  }
  public void setRedirectUrl(String redirectUrl) {
	this.redirectUrl = redirectUrl;
  }
  public String getUser_name() {
    return user_name;
  }
  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getPassword() {
    return password;
  }
  public void setNew_password(String new_password) {
    this.new_password = new_password;
  }
  public String getNew_password() {
    return new_password;
  }
  public void setPassword_again(String password_again) {
    this.password_again = password_again;
  }
  public String getPassword_again() {
    return password_again;
  }
  public void setAction_type(String action_type) {
    this.action_type = action_type;
  }
  public String getAction_type() {
    return action_type;
  }
  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }
  public String getUser_id() {
    return user_id;
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
