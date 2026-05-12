package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiTestRouteAForm extends ActionForm {
  private String btControl;
  private String txt_routename;
  private String txt_step1;
  private String txt_step2;
  private String txt_step3;
  private String txt_step4;
  private String txt_step5;
  private String txt_step6;
  private String txt_step7;
  private String txt_step8;
  private String txt_step9;
  private String txt_step10;
  private String txt_step11;
  private String txt_step12;
  private String txt_step13;
  private String txt_step14;
  private String txt_step15;
  private String type;

  public String getBtControl() {
    return btControl;
  }

  public void setBtControl(String btControl) {
    this.btControl = btControl;
  }

  public void setTxt_step9(String txt_step9) {
    this.txt_step9 = txt_step9;
  }

  public void setTxt_step8(String txt_step8) {
    this.txt_step8 = txt_step8;
  }

  public void setTxt_step7(String txt_step7) {
    this.txt_step7 = txt_step7;
  }

  public void setTxt_step6(String txt_step6) {
    this.txt_step6 = txt_step6;
  }

  public void setTxt_step5(String txt_step5) {
    this.txt_step5 = txt_step5;
  }

  public void setTxt_step4(String txt_step4) {
    this.txt_step4 = txt_step4;
  }

  public void setTxt_step3(String txt_step3) {
    this.txt_step3 = txt_step3;
  }

  public void setTxt_step2(String txt_step2) {
    this.txt_step2 = txt_step2;
  }

  public void setTxt_step10(String txt_step10) {
    this.txt_step10 = txt_step10;
  }

  public void setTxt_step11(String txt_step11) {
	  this.txt_step11 = txt_step11;
  }

  public void setTxt_step12(String txt_step12) {
	  this.txt_step12 = txt_step12;
  }

  public void setTxt_step13(String txt_step13) {
	  this.txt_step13 = txt_step13;
  }

  public void setTxt_step14(String txt_step14) {
	  this.txt_step14 = txt_step14;
  }

  public void setTxt_step15(String txt_step15) {
	  this.txt_step15 = txt_step15;
  }

  public void setTxt_step1(String txt_step1) {
    this.txt_step1 = txt_step1;
  }

  public void setTxt_routename(String txt_routename) {
    this.txt_routename = txt_routename.toUpperCase();
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTxt_routename() {
    return txt_routename;
  }

  public String getTxt_step1() {
    return txt_step1;
  }

  public String getTxt_step10() {
    return txt_step10;
  }

  public String getTxt_step11() {
	return txt_step11;
  }

  public String getTxt_step12() {
	return txt_step12;
  }

  public String getTxt_step13() {
	return txt_step13;
  }

  public String getTxt_step14() {
	return txt_step14;
  }

  public String getTxt_step15() {
	return txt_step15;
	}

  public String getTxt_step2() {
    return txt_step2;
  }

  public String getTxt_step3() {
    return txt_step3;
  }

  public String getTxt_step4() {
    return txt_step4;
  }

  public String getTxt_step5() {
    return txt_step5;
  }

  public String getTxt_step6() {
    return txt_step6;
  }

  public String getTxt_step7() {
    return txt_step7;
  }

  public String getTxt_step8() {
    return txt_step8;
  }

  public String getTxt_step9() {
    return txt_step9;
  }

  public String getType() {
    return type;
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
