package com.mxic.oiplus.oimaintain;

import java.io.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

public class AsignActionForm extends ActionForm {
  private String brand;
  private String pd_body;
  private String status;
  private int sid;
  private String version;
  private String listControl;
  private String message1;
  private String package_component;
  
  public void setVersion(String version) {
    this.version = version;
  }

  public void setPd_body(String pd_body) {
    this.pd_body = pd_body;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getListControl() {
    return listControl;
  }

  public void setListControl(String listControl) {
    this.listControl = listControl;
  }

  public void setSid(int sid) {
    this.sid = sid;
  }

  public String getBrand() {
    return brand;
  }

  public String getPd_body() {
    return pd_body;
  }

  public String getVersion() {
    return version;
  }

  public int getSid() {
    return sid;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    /** @todo: finish this method, this is just the skeleton.*/
    return null;
  }

  public void reset(ActionMapping actionMapping,
                    HttpServletRequest servletRequest) {
    try {
      //轉成中文big5編碼
      servletRequest.setCharacterEncoding("big5");
    } catch (UnsupportedEncodingException ex) {
    }
  }

public String getMessage1() {
    return message1;
}

public void setMessage1(String message1) {
    this.message1 = message1;
}

public String getPackage_component() {
	return package_component;
}

public void setPackage_component(String package_component) {
	this.package_component = package_component;
}

}
