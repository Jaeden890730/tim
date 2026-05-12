package com.mxic.oiplus.oisearch;

import java.io.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiMainControlAForm extends ActionForm {
  private String listControl;
  private String ra_select;
  private String txt_productbody;
  private String sid;
  private String version;
  private String status;
  private String brand;
  private String creator;
  private String productType;

  public String getVersion(){
    return version;
  }

  public void setVersion(String version){
    this.version = version;
  }

  public String getSid(){
    return sid;
  }

  public void setSid(String sid){
    this.sid = sid;
  }

  public String getListControl() {
    return listControl;
  }

  public void setListControl(String listControl) {
    this.listControl = listControl;
  }

  public void setTxt_productbody(String txt_productbody) {
    this.txt_productbody = txt_productbody;
  }

  public void setRa_select(String ra_select) {
    this.ra_select = ra_select;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getRa_select() {
    return ra_select;
  }

  public String getTxt_productbody() {
    return txt_productbody;
  }

  public String getStatus() {
    return status;
  }

  public String getBrand() {
    return brand;
  }

  public String getCreator() {
    return creator;
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    /** @todo: finish this method, this is just the skeleton.*/
    return null;
  }

  public void reset(ActionMapping actionMapping,
                    HttpServletRequest servletRequest) {
    try {
      servletRequest.setCharacterEncoding("Big5");
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
