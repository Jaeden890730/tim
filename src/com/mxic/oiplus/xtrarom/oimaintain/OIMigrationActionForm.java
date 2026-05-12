package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

public class OIMigrationActionForm extends ActionForm {

  private String record_id;
  private String listControl;
  private String information;
  //就是record_id
  private String brand;
  private String pd_body;
  private String version;

  public String getListControl() {
    return listControl;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setRecord_id(String record_id) {
    this.record_id = record_id;
  }

  public String getRecord_id() {
    return record_id;
  }

  public void setPd_body(String pd_body) {
    this.pd_body = pd_body;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setListControl(String listControl) {
    this.listControl = listControl;
  }

  public String getBrand() {
    return brand;
  }

  public void setInformation(String information) {
    this.information = information;
  }

  public String getInformation() {
    return information;
  }

  public String getPd_body() {
    return pd_body;
  }

  public String getVersion() {
    return version;
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
}
