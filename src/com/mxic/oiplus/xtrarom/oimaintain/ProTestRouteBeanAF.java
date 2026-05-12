package com.mxic.oiplus.xtrarom.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class ProTestRouteBeanAF extends ActionForm {
  private String brand;
  private String condition;
  private String count;
  private String productbody;
  private String remark;
  private String routename;
  private String sid;
  private String stepname;
  private String stepseq;
  private String temperature;
  private String message;
  private String readonly;
  private String version;
  private String bodyversion;
  private String testtime;
  private String timeunit;
  private String undefine_msg;
  private String undefine_step_name;
  private String vendor;
  private String creator;
  private String samplingtest;

  public String getSamplingtest() {
	return samplingtest;
}

public void setSamplingtest(String samplingtest) {
	this.samplingtest = samplingtest;
}

public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    /** @todo: finish this method, this is just the skeleton.*/
    return null;
  }

  public void reset(ActionMapping actionMapping,
                    HttpServletRequest servletRequest) {
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public void setCount(String count) {
    this.count = count;
  }

  public void setProductbody(String productbody) {
    this.productbody = productbody;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setRoutename(String routename) {
    this.routename = routename;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public void setStepname(String stepname) {
    this.stepname = stepname;
  }

  public void setStepseq(String stepseq) {
    this.stepseq = stepseq;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setReadonly(String readonly) {
    this.readonly = readonly;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setBodyversion(String bodyversion) {
    this.bodyversion = bodyversion;
  }

  public void setTesttime(String testtime) {
    this.testtime = testtime;
  }

  public void setTimeunit(String timeunit) {
    this.timeunit = timeunit;
  }

  public String getBrand() {
    return brand;
  }

  public String getCondition() {
    return condition;
  }

  public String getCount() {
    return count;
  }

  public String getProductbody() {
    return productbody;
  }

  public String getRemark() {
    return remark;
  }

  public String getRoutename() {
    return routename;
  }

  public String getSid() {
    return sid;
  }

  public String getStepname() {
    return stepname;
  }

  public String getStepseq() {
    return stepseq;
  }

  public String getTemperature() {
    return temperature;
  }

  public String getMessage() {
    return message;
  }

  public String getReadonly() {
    return readonly;
  }

  public String getVersion() {
    return version;
  }

  public String getBodyversion() {
    return bodyversion;
  }

  public String getTesttime() {
    return testtime;
  }

  public String getTimeunit() {
    return timeunit;
  }

  public String getUndefine_msg() {
    return undefine_msg;
  }

  public void setUndefine_msg(String undefine_msg) {
    this.undefine_msg = undefine_msg;
  }

  public String getUndefine_step_name() {
    return undefine_step_name;
  }

  public void setUndefine_step_name(String undefine_step_name) {
    this.undefine_step_name = undefine_step_name;
  }

  public String getVendor() {
    return vendor;
  }

  public void setVendor(String vendor) {
    this.vendor = vendor;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }
}
