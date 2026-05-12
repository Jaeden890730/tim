package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiTestRouteSearchAForm extends ActionForm {
  public oiTestRouteSearchAForm() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private String btControl;
  private String mode;
  private String txt_routename;
  private String routecate;

  public String getBtControl() {
    return btControl;
  }

  public void setBtControl(String btControl) {
    this.btControl = btControl;
  }

  public void setTxt_routename(String txt_routename) {
    this.txt_routename = txt_routename;
  }

  public String getTxt_routename() {
    return txt_routename;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getMode() {
    return mode;
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    /** @todo: finish this method, this is just the skeleton.*/
    return null;
  }

  public void reset(ActionMapping actionMapping,
                    HttpServletRequest servletRequest) {
  }

  private void jbInit() throws Exception {
  }

  public String getRoutecate() {
    return routecate;
  }

  public void setRoutecate(String routecate) {
    this.routecate = routecate;
  }
}
