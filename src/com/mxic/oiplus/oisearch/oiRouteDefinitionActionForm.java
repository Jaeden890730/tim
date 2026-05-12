package com.mxic.oiplus.oisearch;

import java.io.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiRouteDefinitionActionForm extends ActionForm {
  private String product_body;

  public String getProduct_body(){
    return product_body;
  }

  public void setProduct_body(String product_body){
    this.product_body = product_body;
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
}
