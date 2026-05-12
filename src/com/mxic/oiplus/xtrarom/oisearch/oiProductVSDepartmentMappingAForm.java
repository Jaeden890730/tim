package com.mxic.oiplus.xtrarom.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiProductVSDepartmentMappingAForm extends ActionForm {
  private String btControl;
  private String ra_select;

  public String getBtControl() {
    return btControl;
  }

  public void setBtControl(String btControl) {
    this.btControl = btControl;
  }

  public void setRa_select(String ra_select) {
    this.ra_select = ra_select;
  }

  public String getRa_select() {
    return ra_select;
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
