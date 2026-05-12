package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class OIReleaseActionForm extends ActionForm {
  private String brand;
  private String pd_body;
  private String status;
  private int sid;
  private String version;
  private String listControl;
  private String file_name;
  private String title;

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

  public void setFile_name(String file_name) {
    this.file_name = file_name;
  }

  public String getFile_name() {
    return file_name;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    /** @todo: finish this method, this is just the skeleton.*/
    return null;
  }

  public void reset(ActionMapping actionMapping,
                    HttpServletRequest servletRequest) {
  }
}
