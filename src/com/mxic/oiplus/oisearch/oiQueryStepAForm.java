package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiQueryStepAForm extends ActionForm {
  private String brand;
  private String btControl;
  private String product_body;
  private String version;

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setProduct_body(String product_body) {
    this.product_body = product_body;
  }

  public void setBtControl(String btControl) {
    this.btControl = btControl;
  }

  public String getBtControl() {
    return btControl;
  }

  public String getProduct_body() {
    return product_body;
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
  }
}
