package com.mxic.oiplus.oimaintain;

import java.io.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;


public class AurthMaintainActionForm extends ActionForm {
  private String listControl;
  private String brand;
  private String pd_body;
  private String version;
  private String creator;
  private String sponsor_1;
  private String sponsor_2;
  private int sid;
  private String productType;

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getCreator() {
    return creator;
  }

  public void setSponsor_1(String sponsor_1) {
    this.sponsor_1 = sponsor_1;
  }

  public String getSponsor_1() {
    return sponsor_1;
  }

  public void setSponsor_2(String sponsor_2) {
    this.sponsor_2 = sponsor_2;
  }

  public String getSponsor_2() {
    return sponsor_2;
  }

  public String getListControl() {
    return listControl;
  }

  public void setPd_body(String pd_body) {
    this.pd_body = pd_body;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setSid(int sid) {
    this.sid = sid;
  }

  public void setListControl(String listControl) {
    this.listControl = listControl;
  }

  public String getBrand() {
    return brand;
  }

  public String getPd_body() {
    return pd_body;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }

  public int getSid() {
    return sid;
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

/**
 * @return Returns the productType.
 */
public String getProductType() {
	return productType;
}

/**
 * @param productType The productType to set.
 */
public void setProductType(String productType) {
	this.productType = productType;
}
}
