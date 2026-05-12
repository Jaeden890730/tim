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
public class TFBasicInformationBean {
  public TFBasicInformationBean() {
  }

  private String sid;
  private String product_body;
  private String brand;
  private String version;
  private String tester;
  private String good_bin;
  private String fail_bin;
  private String remark;

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

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getRemark() {
    return remark;
  }

  public void setTester(String tester) {
    this.tester = tester;
  }

  public String getTester() {
    return tester;
  }

  public void setGood_bin(String good_bin) {
    this.good_bin = good_bin;
  }

  public String getGood_bin() {
    return good_bin;
  }

  public void setFail_bin(String fail_bin) {
    this.fail_bin = fail_bin;
  }

  public String getFail_bin() {
    return fail_bin;
  }
}
