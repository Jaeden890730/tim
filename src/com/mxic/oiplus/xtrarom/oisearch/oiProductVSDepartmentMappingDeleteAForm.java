package com.mxic.oiplus.xtrarom.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiProductVSDepartmentMappingDeleteAForm extends ActionForm {
  private String btControl;
  private String product;
  private String department;

  public String getBtControl() {
    return btControl;
  }

  public void setBtControl(String btControl) {
    this.btControl = btControl;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getProduct() {
    return product;
  }

  public String getDepartment() {
    return department;
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
