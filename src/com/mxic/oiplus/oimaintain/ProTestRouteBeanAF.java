package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class ProTestRouteBeanAF extends ActionForm {
  private String type;	  
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
  private String reworkstep;
  private String testtime2;
  private String timeunit2;
  private String undefine_msg;
  private String undefine_step_name;
  private String vendor;
  private String creator;
  private String entry;
  private String samplingtest;
  private String step_def;
  private String package_component;
  private String samplingcond;
  
  private BomProductRouteBean[] bom = null;


  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    /** @todo: finish this method, this is just the skeleton.*/
    return null;
  }

  public void reset(ActionMapping actionMapping,
                    HttpServletRequest servletRequest) {
  }

  public String getType() {
	return type;
  }

  public void setType(String type) {
	this.type = type;
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

  public void setReworkstep(String reworkstep) {
    this.reworkstep = reworkstep;
  }

  public void setTesttime2(String testtime2) {
	this.testtime2 = testtime2;
  }

  public void setTimeunit2(String timeunit2) {
	this.timeunit2 = timeunit2;
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

  public String getReworkstep() {
	return reworkstep;
  }

  public String getTesttime2() {
	return testtime2;
  }

  public String getTimeunit2() {
	return timeunit2;
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

  public String getEntry() {
    return entry;
  }

  public void setEntry(String entry) {
    this.entry = entry;
  }

public String getSamplingtest() {
	return samplingtest;
}

public void setSamplingtest(String samplingtest) {
	this.samplingtest = samplingtest;
}

public String getSamplingcond() {
	return samplingcond;
}

public void setSamplingcond(String samplingcond) {
	this.samplingcond = samplingcond;
}

public String getStep_def() {
    return step_def;
}

public void setStep_def(String step_def) {
    this.step_def = step_def;
}

public String getPackage_component() {
	return package_component;
}

public void setPackage_component(String package_component) {
	this.package_component = package_component;
}

public BomProductRouteBean[] getBom() {
    return bom;
}

public void setBom(BomProductRouteBean[] bom) {
    this.bom = bom;
}
}
