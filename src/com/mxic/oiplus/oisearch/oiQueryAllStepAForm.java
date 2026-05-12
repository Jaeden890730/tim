package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiQueryAllStepAForm extends ActionForm {
  private String brand;
  private String productbody;
  private String step;
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

  public void setStep(String step) {
    this.step = step;
  }

  public void setProductbody(String productbody) {
    this.productbody = productbody;
  }

  public String getProductbody() {
    return productbody;
  }

  public String getStep() {
    return step;
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
