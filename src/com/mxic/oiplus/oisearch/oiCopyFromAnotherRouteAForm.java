package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiCopyFromAnotherRouteAForm extends ActionForm {
  private String listControl;
  private String ra_select;
  private String route_name;

  public String getListControl() {
    return listControl;
  }

  public void setListControl(String listControl) {
    this.listControl = listControl;
  }

  public void setRa_select(String ra_select) {
    this.ra_select = ra_select;
  }

  public void setRoute_name(String route_name) {
    this.route_name = route_name;
  }

  public String getRa_select() {
    return ra_select;
  }

  public String getRoute_name() {
    return route_name;
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
