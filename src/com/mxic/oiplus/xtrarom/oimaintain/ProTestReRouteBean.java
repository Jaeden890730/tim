package com.mxic.oiplus.xtrarom.oimaintain;

import java.awt.*;
import javax.swing.*;

public class ProTestReRouteBean {
  private String routename;
  private String stepseq;
  private String stepname;
  private String condition;
  private String temperature;
  private String remark;
  private String productbody;
  private String brand;
  private String count;
  private String sid;
  private String version;
  private String bodyversion;
  private String maskoptrev;
  private String testtime;
  private String timeunit;
  private String tag;
  private String samplingtest;

  public void setRoutename(String routename) {
    this.routename = routename;
  }

  public void setStepseq(String stepseq) {
    this.stepseq = stepseq;
  }

  public void setStepname(String stepname) {
    this.stepname = stepname;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setProductbody(String productbody) {
    this.productbody = productbody;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setCount(String count) {
    this.count = count;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setBodyversion(String bodyversion) {
    this.bodyversion = bodyversion;
  }

  public void setMaskoptrev(String maskoptrev) {
    this.maskoptrev = maskoptrev;
  }

  public void setTesttime(String testtime) {
    this.testtime = testtime;
  }

  public void setTimeunit(String timeunit) {
    this.timeunit = timeunit;
  }

  public String getRoutename() {
    return routename;
  }

  public String getStepseq() {
    return stepseq;
  }

  public String getStepname() {
    return stepname;
  }

  public String getCondition() {
    return condition;
  }

  public String getTemperature() {
    return temperature;
  }

  public String getRemark() {
    return remark;
  }

  public String getProductbody() {
    return productbody;
  }

  public String getBrand() {
    return brand;
  }

  public String getCount() {
    return count;
  }

  public String getSid() {
    return sid;
  }

  public String getVersion() {
    return version;
  }

  public String getBodyversion() {
    return bodyversion;
  }

  public String getMaskoptrev() {
    return maskoptrev;
  }

  public String getTesttime() {
    return testtime;
  }

  public String getTimeunit() {
    return timeunit;
  }

  public String getInsertvalue(){
    if ((stepname.substring(0,1).toUpperCase().equals("B") ||
        stepname.substring(0,1).toUpperCase().equals("U") ||
        stepname.toUpperCase().startsWith("TEMPER") ||
        stepname.substring(0,1).toUpperCase().equals("C"))
    	&& (!stepname.equals("Bumping"))){
      return " ";
    } else {
      return "disabled=";
    }
  }
  
  public String getInsertvalue_s(){
	   if (	stepname.startsWith("SORT")||
			stepname.startsWith("FT") ||
			stepname.startsWith("TQAE")){
	      return " ";
	    } else {
	      return "disabled=";
	    }
	  }

  /**
   * @return the tag
   */
  public String getTag() {
	  return tag;
  }

  /**
   * @param tag the tag to set
   */
  public void setTag(String tag) {
	  this.tag = tag;
  }

public String getSamplingtest() {
	return samplingtest;
}

public void setSamplingtest(String samplingtest) {
	this.samplingtest = samplingtest;
}
}
