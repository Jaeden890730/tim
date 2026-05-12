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
public class TFBomReRouteBean {
  public TFBomReRouteBean() {
  }

  private String sid;
  private String product_body;
  private String brand;
  private String version;
  private String body_version;
  private String mask_option;
  private String mask_option_rev;
  private String code_no;
  private String pin_count;
  private String package_code;
  private String route_type;
  private String recycle_code;
  private String ft_route;
  private String ft_comment;
  private String ftAddroute;
  private String tag;

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

  public void setBody_version(String body_version) {
    this.body_version = body_version;
  }

  public String Body_version() {
    return body_version;
  }

  public void setCode_no(String code_no) {
    this.code_no = code_no;
  }

  public String getCode_no() {
    return code_no;
  }

  public void setPin_count(String pin_count) {
    this.pin_count = pin_count;
  }

  public String getPin_count() {
    return pin_count;
  }

  public void setPackage_code(String package_code) {
    this.package_code = package_code;
  }

  public String getPackage_code() {
    return package_code;
  }

  public void setRecycle_code(String recycle_code) {
    this.recycle_code = recycle_code;
  }

  public String getRecycle_code() {
    return recycle_code;
  }

  public void setRoute_type(String route_type) {
   this.route_type = route_type;
 }

 public String getRoute_type() {
   return route_type;
  }

  public void setFt_route(String ft_route) {
    this.ft_route = ft_route;
  }

  public String getFt_route() {
    return ft_route;
  }

  public void setMask_option(String mask_option) {
    this.mask_option = mask_option;
  }

  public String getMask_option() {
    return mask_option;
  }

  public void setMask_option_rev(String mask_option_rev) {
    this.mask_option_rev = mask_option_rev;
  }

  public String getMask_option_rev() {
    return mask_option_rev;
  }

  public void setFt_comment(String ft_comment) {
    this.ft_comment = ft_comment;
  }

  public String getFt_comment() {
    return ft_comment;
  }

  public String getFtAddroute() {
    return ftAddroute;
  }

  public void setFtAddroute(String ftAddroute) {
    this.ftAddroute = ftAddroute;
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
}
