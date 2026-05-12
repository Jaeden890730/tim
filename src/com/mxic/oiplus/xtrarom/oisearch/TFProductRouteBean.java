package com.mxic.oiplus.xtrarom.oisearch;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class TFProductRouteBean {
  public TFProductRouteBean() {
  }

  private String sid;
  private String product_body;
  private String brand;
  private String version;
  private String status;
  private String route_name;
  private String step_seq;
  private String step_name;
  private String conditions;
  private String test_time;
  private String time_unit;
  private String temperature;
  private String remark;
  private String count;
  private String samplingtest;


  public String getSamplingtest() {
	return samplingtest;
}

public void setSamplingtest(String samplingtest) {
	this.samplingtest = samplingtest;
}

public void setSid(String sid) {
    this.sid = sid;
  }

  public String getSid() {
    return sid;
  }

  public void setProduct_body(String product_body) {
    this.product_body = product_body;
  }

  public String getProduct_body() {
    return product_body;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getBrand() {
    return brand;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }

  public void setRoute_name(String route_name) {
    this.route_name = route_name;
  }

  public String getRoute_name() {
    return route_name;
  }

  public void setStep_seq(String step_seq) {
    this.step_seq = step_seq;
  }

  public String getStep_seq() {
    return step_seq;
  }

  public void setStep_name(String step_name) {
    this.step_name = step_name;
  }

  public String getStep_name() {
    return step_name;
  }

  public void setConditions(String conditions) {
    this.conditions = conditions;
  }

  public String getConditions() {
    return conditions;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public String getTemperature() {
    return temperature;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getRemark() {
    return remark;
  }

  public void setCount(String count) {
    this.count = count;
  }

  public String getCount() {
    return count;
  }

  public void setTest_time(String test_time) {
    this.test_time = test_time;
  }

  public String getTest_time() {
    return test_time;
  }

  public void setTime_unit(String time_unit) {
    this.time_unit = time_unit;
  }

  public String getTime_unit() {
    return time_unit;
  }

/**
 * @return the status
 */
public String getStatus() {
	return status;
}

/**
 * @param status the status to set
 */
public void setStatus(String status) {
	this.status = status;
}
}
