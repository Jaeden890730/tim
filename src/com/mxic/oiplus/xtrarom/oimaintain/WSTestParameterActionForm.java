package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

public class WSTestParameterActionForm extends ActionForm {
  private String sid;
  private String tag;
  private String pgm_id;
  private String product_body;
  private String brand;
  private String version;
  private String mask_option;
  private String test_type;
  private String tester;
  private String site;
  private String program_name;
  private String tf_comment;
  private String temperature;
  private String id;
  private String hw_configure;

  public String getHw_configure() {
	return hw_configure;
}

public void setHw_configure(String hw_configure) {
	this.hw_configure = hw_configure;
}

public void setSid(String sid) {
    this.sid = sid;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setPgm_id(String pgm_id) {
    this.pgm_id = pgm_id;
  }

  public void setProduct_body(String product_body) {
    this.product_body = product_body;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setMask_option(String mask_option) {
    this.mask_option = mask_option;
  }

  public void setTest_type(String test_type) {
    this.test_type = test_type;
  }

  public void setTester(String tester) {
    this.tester = tester;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public void setProgram_name(String program_name) {
    this.program_name = program_name;
  }

  public void setTf_comment(String tf_comment) {
    this.tf_comment = tf_comment;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSid() {
    return sid;
  }

  public String getTag() {
    return tag;
  }

  public String getPgm_id() {
    return pgm_id;
  }

  public String getProduct_body() {
    return product_body;
  }

  public String getBrand() {
    return brand;
  }

  public String getVersion() {
    return version;
  }

  public String getMask_option() {
    return mask_option;
  }

  public String getTest_type() {
    return test_type;
  }

  public String getTester() {
    return tester;
  }

  public String getSite() {
    return site;
  }

  public String getProgram_name() {
    return program_name;
  }

  public String getTf_comment() {
    return tf_comment;
  }

  public String getTemperature() {
    return temperature;
  }

  public String getId() {
    return id;
  }

  public String getChangecolor(){
    if (tag.equals("1")){
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }

  public void reset(ActionMapping actionMapping,
                    HttpServletRequest servletRequest) {
    try {
      //轉成中文big5編碼
      servletRequest.setCharacterEncoding("big5");
    } catch (UnsupportedEncodingException ex) {
    }
  }
}

